package com.trang.estore.payments;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@AllArgsConstructor
@Tag(name = "Checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    @Operation(summary = "Send a check out request")
    public CheckoutResponse checkout(@RequestBody CheckoutRequest checkoutRequest) {
        var cartId = checkoutRequest.getCartId();
        return checkoutService.checkout(cartId);

    }

    @Operation(summary = "Webhook request from Stripe")
    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ){
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }
}
