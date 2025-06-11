package com.trang.estore.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {
    @Autowired
    private CategoryRepository categoryRepository;

    @Mapping(target = "categoryId", source = "category.id")
    public abstract ProductDto toDto(Product product);

    @Mapping(target = "category", source = "categoryId")
    public abstract Product toProduct(ProductDto productDto);

    public abstract void update(ProductDto productDto, @MappingTarget Product product);


    public Category resolveCategory(Byte categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.findById(categoryId).orElse(null);
    }
}
