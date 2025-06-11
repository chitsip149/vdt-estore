package com.trang.estore.payments;

import com.trang.estore.carts.Cart;
import com.trang.estore.carts.CartIsEmptyException;
import com.trang.estore.carts.CartNotFoundException;
import com.trang.estore.carts.CartItemMapper;
import com.trang.estore.carts.CartMapper;
import com.trang.estore.carts.CartRepository;
import com.trang.estore.orders.Order;
import com.trang.estore.orders.OrderItem;
import com.trang.estore.orders.OrderStatus;
import com.trang.estore.orders.OrderItemRepository;
import com.trang.estore.orders.OrderRepository;
import com.trang.estore.users.UserRepository;
import com.trang.estore.carts.CartService;
import com.trang.estore.users.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    @Transactional
    public CheckoutResponse checkout(UUID cartId) {

        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        if (cart.isEmpty()) {
            throw new CartIsEmptyException();
        }
        var order = new Order();

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        var user = userRepository.findById(userId).orElseThrow();

        setOrderInfo(order, user, cart);
        orderRepository.save(order);
        orderItemRepository.saveAll(order.getOrderItems());

        try {
            var session = paymentGateway.createCheckoutSession(order);

            cartService.deleteCartItems(cartId);
            var checkoutResponse = new CheckoutResponse();
            checkoutResponse.setId(order.getId());
            checkoutResponse.setCheckoutUrl(session.getCheckoutUrl());
            return checkoutResponse;
        } catch (PaymentException e) {
            orderRepository.delete(order);
            throw e;
        }
    }

    public void handleWebhookEvent(WebhookRequest webhookRequest) {
        paymentGateway
                .parseWebhookRequest(webhookRequest)
                .ifPresent(paymentResult -> {
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });


    }

    private void setOrderInfo(Order order, User user, Cart cart) {
        order.setCustomer(user);
        order.setStatus(OrderStatus.PENDING);
        cart.getCartItems().forEach(item -> {
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setProduct(item.getProduct());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            orderItem.setTotalPrice(cartItemMapper.toDto(item).getTotalPrice());

            order.addItem(orderItem);
        });
        order.setTotalPrice(cartMapper.toCartDto(cart).getTotalPrice());
    }
}
