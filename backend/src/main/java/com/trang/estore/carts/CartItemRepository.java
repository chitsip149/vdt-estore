package com.trang.estore.carts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    public CartItem findByCartIdAndProductId(UUID cartId, Long productId);
}