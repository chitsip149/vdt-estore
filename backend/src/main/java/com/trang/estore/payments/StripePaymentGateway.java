package com.trang.estore.payments;

import com.trang.estore.orders.Order;
import com.trang.estore.orders.OrderItem;
import com.trang.estore.orders.OrderStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway {

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        //create a checkout session
        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel")
                    .setPaymentIntentData(
                            SessionCreateParams.PaymentIntentData.builder()
                                    .putMetadata("order_id", order.getId().toString())
                                    .build()
                    );

            order.getOrderItems().forEach(item -> {
                var lineItem = createLineItem(item);
                builder.addLineItem(lineItem);
            });
            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        } catch (StripeException e){
            System.out.println(e.getMessage());
            throw new PaymentException("Cannot create checkout session");
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest webhookRequest) {
        try {
            //take WebhookRequest -> return a new object {orderId, paymentStatus}
            var payload = webhookRequest.getPayload();
            var signature = webhookRequest.getHeaders().get("stripe-signature");
            System.out.println(signature);

            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
            System.out.println(extractOrderId(event));


            //charge -> (Charge) stripeObject
            //payment_intent.succeeded -> (PaymentIntent) stripeObject

            return switch (event.getType()) {
                case "payment_intent.succeeded" ->
                    //Update order status (PAID)

                    Optional.of(new PaymentResult(extractOrderId(event), OrderStatus.PAID));

                case "payment_intent.payment_failed" ->
                    //Update order status (FAILED)
                    Optional.of(new PaymentResult(extractOrderId(event), OrderStatus.FAILED));

                default ->
                    Optional.empty();

            };

        } catch (SignatureVerificationException e) {
            throw new PaymentException("Invalid signature");
        }
    }

    private Long extractOrderId(Event event) {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                () -> new PaymentException("Could not deserialize Stripe event, check the SDK and API version")
        );
        var paymentIntent = (PaymentIntent) stripeObject;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(
                        createPriceData(item)
                )
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("vnd")
                .setUnitAmountDecimal(item.getUnitPrice())
                .setProductData(createProductData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName())
                .build();
    }
}
