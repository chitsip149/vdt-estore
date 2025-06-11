package com.trang.estore.carts;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddProductCartDto {

    @NotNull
    private Long productId;
}
