package com.trang.estore.payments;

import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {
    private UUID cartId;
}
