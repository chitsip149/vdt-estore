package com.trang.estore.orders;

import com.trang.estore.carts.ProductCartItemDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class OrderItemMapper {

    public abstract OrderItemDto toDto(OrderItem orderItem);

    public ProductCartItemDto getProductCartItemDto(OrderItem orderItem) {
        var productCartItemDto = new ProductCartItemDto();
        productCartItemDto.setId(orderItem.getProduct().getId());
        productCartItemDto.setName(orderItem.getProduct().getName());
        productCartItemDto.setPrice(orderItem.getUnitPrice());
        return productCartItemDto;
    }
}
