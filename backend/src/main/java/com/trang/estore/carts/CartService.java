package com.trang.estore.carts;

import com.trang.estore.products.ProductNotFoundException;
import com.trang.estore.products.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;

    private CartItemRepository cartItemRepository;
    private CartItemMapper cartItemMapper;

    private ProductRepository productRepository;

    public CartDto createCart(){
        var cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toCartDto(cart);

    }

    public CartItemDto addCartItem(UUID cartId, Long productId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();

        var product = productRepository.findById(productId).orElse(null);
        if (product == null) throw new ProductNotFoundException();

        var cartItem = cart.findItem(productId);
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cart.getCartItems().add(cartItem);
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }
        cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        return cartMapper.toCartDto(cart);
    }

    public CartItemDto updateCart(UUID cartId, Long productId, int quantity){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();

        var product = productRepository.findById(productId).orElse(null);
        if (product == null) throw new ProductNotFoundException();

        var cartItem = cart.findItem(productId);
        if (cartItem == null) {
            throw new CartItemNotFoundException();
        }
        var cartItemDto = cartItemMapper.toDto(cartItem);
        cartItemDto.setQuantity(quantity);
        cartItemMapper.update(cartItem, cartItemDto);
        cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(cartItem);
    }

    public void deleteCartItem(UUID cartId, Long productId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        var cartItem = cart.findItem(productId);
        if (cartItem == null) {
            throw new CartItemNotFoundException();
        }
        cartItemRepository.delete(cartItem);
    }

    public void deleteCartItems(UUID cartId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        var items = cart.getCartItems();
        cartItemRepository.deleteAll(items);
    }
}
