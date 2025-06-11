package com.trang.estore.carts;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCartItemDto {
    private Long id;
    private String name;
    private BigDecimal price;
}
