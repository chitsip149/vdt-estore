package com.trang.estore.orders;

import com.trang.estore.carts.ProductCartItemDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private ProductCartItemDto product;
    private int quantity;
    private BigDecimal totalPrice;
}
