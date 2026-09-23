package com.medthegprod.backend.sales.infrastructure.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(

        @NotEmpty(message = "Order must contain at least one item") @Valid List<Item> items

) {

    public record Item(

            @NotNull(message = "Product ID is required") UUID productId,

            @NotNull(message = "Quantity is required") @Positive(message = "Quantity must be greater than zero") Integer quantity) {
    }
}