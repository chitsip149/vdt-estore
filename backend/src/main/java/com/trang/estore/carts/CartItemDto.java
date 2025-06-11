package com.trang.estore.carts;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private ProductCartItemDto product;
    private int quantity;
    private BigDecimal totalPrice;

    public BigDecimal getTotalPrice() {
        return BigDecimal.valueOf(quantity).multiply(product.getPrice());
    }
}
