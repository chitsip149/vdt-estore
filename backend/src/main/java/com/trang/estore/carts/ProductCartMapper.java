package com.trang.estore.carts;

import com.trang.estore.products.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductCartMapper {

    public Product toProduct(ProductCartItemDto productCartItemDto);
}
