package com.trang.estore.orders;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String status;
    private Instant createdAt;
    private List<OrderItemDto> orderItems;
    private BigDecimal totalPrice;
}
