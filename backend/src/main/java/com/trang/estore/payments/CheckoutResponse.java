package com.trang.estore.payments;

import lombok.Data;

@Data
public class CheckoutResponse {
    private Long id;
    private String checkoutUrl;
}
