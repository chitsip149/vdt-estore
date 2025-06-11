package com.trang.estore.carts;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateProductCartDto {

    @Min(value = 1, message = "Quantity must be larger than or equal to 1")
    @Max(value = 100, message = "Quantity must be less than or equal to 100")
    private int quantity;
}
