package com.trang.estore.carts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CartMapper {

    @Autowired
    private CartItemMapper cartItemMapper;

    @Mapping(target = "items", source = "cartItems")
    @Mapping(target = "totalPrice", expression =
            "java(cartDto.getItems().stream()" +
                    ".map(cartItemDto -> cartItemDto.getTotalPrice())" +
                    ".reduce(new java.math.BigDecimal(0), (curTotal, element) -> curTotal.add(element)))")
    public abstract CartDto toCartDto(Cart cart);

    public CartItemDto cartItemToCartItemDto(CartItem cartItem) {
        return cartItemMapper.toDto(cartItem);
    }


}
