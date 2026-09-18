package com.medthegprod.backend.catalog.infrastructure.web.dto;

import com.medthegprod.backend.catalog.domain.model.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(

        @NotBlank String title,

        String description,

        @NotNull ProductType type,

        @NotNull @DecimalMin(value = "0.00") BigDecimal price) {
}