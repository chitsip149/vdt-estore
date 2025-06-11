package com.trang.estore.carts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "product", source = "product")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "totalPrice", expression = "java( new java.math.BigDecimal(cartItem.getQuantity()).multiply(cartItem.getProduct().getPrice()) )")
    CartItemDto toDto(CartItem cartItem);

    void update(@MappingTarget CartItem cartItem, CartItemDto cartItemDto);
}
