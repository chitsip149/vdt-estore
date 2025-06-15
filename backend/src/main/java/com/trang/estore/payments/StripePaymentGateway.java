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
import java.util.Map;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway {

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
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

        } catch (StripeException e) {
            System.out.println("Stripe exception when creating session: " + e.getMessage());
            throw new PaymentException("Cannot create checkout session");
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest webhookRequest) {
        try {
            var payload = webhookRequest.getPayload();
            var signature = webhookRequest.getHeaders().get("stripe-signature");

            System.out.println("Received webhook signature: " + signature);

            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);

            System.out.println("Stripe event type: " + event.getType());

            return switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                    Long orderId = extractOrderId(event);
                    System.out.println("Payment succeeded for order: " + orderId);
                    yield Optional.of(new PaymentResult(orderId, OrderStatus.PAID));
                }
                case "payment_intent.payment_failed" -> {
                    Long orderId = extractOrderId(event);
                    System.out.println("Payment failed for order: " + orderId);
                    yield Optional.of(new PaymentResult(orderId, OrderStatus.FAILED));
                }
                default -> {
                    System.out.println("Unhandled event type: " + event.getType());
                    yield Optional.empty();
                }
            };

        } catch (SignatureVerificationException e) {
            System.out.println("Invalid signature: " + e.getMessage());
            throw new PaymentException("Invalid signature");
        } catch (Exception e) {
            System.out.println("Exception while handling webhook: " + e.getMessage());
            throw new PaymentException("Webhook processing error");
        }
    }

    private Long extractOrderId(Event event) {
        var deserializer = event.getDataObjectDeserializer();

        var stripeObject = deserializer.getObject().orElseThrow(
                () -> new PaymentException("Could not deserialize Stripe event")
        );

        if (!(stripeObject instanceof PaymentIntent paymentIntent)) {
            throw new PaymentException("Unexpected Stripe object type: " + stripeObject.getClass().getName());
        }

        Map<String, String> metadata = paymentIntent.getMetadata();
        if (metadata == null || !metadata.containsKey("order_id")) {
            throw new PaymentException("Missing order_id in PaymentIntent metadata");
        }

        String orderIdStr = metadata.get("order_id");
        System.out.println("Extracted order_id: " + orderIdStr);

        return Long.valueOf(orderIdStr);
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(createPriceData(item))
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
