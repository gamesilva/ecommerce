package com.gabriel.ecommerce.product;

import com.gabriel.ecommerce.category.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        Integer id,
        @NotNull(message = "Product name is required")
        String name,
        @NotNull(message = "Description name is required")
        String description,
        @Positive(message = "Available quantity shoud be positive")
        double availableQuantity,
        @Positive(message = "Price quantity shoud be positive")
        BigDecimal price,
        @NotNull(message = "Product category is required")
        Integer categoryId
) {
}
