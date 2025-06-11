package com.trang.estore.payments;

import com.trang.estore.orders.Order;

import java.util.Optional;

public interface PaymentGateway {

    CheckoutSession createCheckoutSession (Order order);

    //when the user makes the payment, stripes send us a few events
    //out of these, we are only interested in one event and that is paymentIntent
    //succeeded or failed
    //so not all the requests returns a payment result, so we change the return type to optional
    //
     // now PaymentGateway has the ability to take a WebhookRequest and return a PaymentResult
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest webhookRequest);

}
