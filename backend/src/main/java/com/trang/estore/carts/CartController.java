package com.trang.estore.carts;

import com.trang.estore.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    @Operation(summary = "Create a new cart")
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriComponentsBuilder
    ){
        var cartDto = cartService.createCart();
        var uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @Operation(summary = "Add an item into a cart")
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addCartItem(
            @PathVariable UUID cartId,
            @Valid @RequestBody AddProductCartDto addProductCartDto){
        var cartItemDto = cartService.addCartItem(cartId, addProductCartDto.getProductId());
        return ResponseEntity.ok(cartItemDto);
    }

    @Operation(summary = "Get the information of a cart including all the cart items it has")
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId){
        var cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDto);
    }

    @Operation(summary = "Change the quantity of an item in a cart")
    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductCartDto updateProductCartDto){
        var cartItemDto = cartService.updateCart(cartId, productId, updateProductCartDto.getQuantity());
        return ResponseEntity.ok(cartItemDto);
    }

    @Operation(summary = "Remove an item in a cart with cartId and productId specified in path")
    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> deleteCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId
    ){
        cartService.deleteCartItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Clear a cart")
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> deleteCartItems(
            @PathVariable UUID cartId
    ){
        cartService.deleteCartItems(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Cart not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Product not found"));
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartItemNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Cart item not found"));
    }
}
