package com.trang.estore.orders;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getOrders() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) auth.getPrincipal();
        var orders = orderRepository.findAllByCustomer(userId);
        var list= new ArrayList<OrderDto>();
        for (var order : orders) {
            list.add(orderMapper.toDto((Order) order));
        }
        return list;
    }

    public OrderDto getOrder(Long orderId) {
        var order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        if (order.getCustomer().getId() != SecurityContextHolder.getContext().getAuthentication().getPrincipal()) {
            throw new OrderNotAccessibleException();
        }
        return orderMapper.toDto(order);
    }
}
