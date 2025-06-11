package com.trang.estore.orders;

import com.trang.estore.carts.CartIsEmptyException;
import com.trang.estore.carts.CartNotFoundException;
import com.trang.estore.common.ErrorDto;

import com.trang.estore.payments.PaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        var list = orderService.getOrders();
        System.out.println(list);

        return ResponseEntity.ok(list);

    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long orderId) {
        var orderDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderDto);


    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<?> handleCartNotFoundException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDto("Cart not found")
        );
    }

    @ExceptionHandler(CartIsEmptyException.class)
    public ResponseEntity<?> handleCartIsEmptyException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDto("Cart is empty")
        );
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto("Order not found")
        );
    }

    @ExceptionHandler(OrderNotAccessibleException.class)
    public ResponseEntity<?> handleOrderNotAccessibleException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorDto("Order not accessible")
        );
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorDto("Error creating a checkout session")
        );
    }
}
