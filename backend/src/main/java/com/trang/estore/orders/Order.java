package com.trang.estore.orders;

import com.trang.estore.users.User;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems = new HashSet<>();

    public void addItem(OrderItem item) {
        orderItems.add(item);
    }


}