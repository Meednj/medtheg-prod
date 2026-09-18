package com.medthegprod.backend.catalog.infrastructure.web.mapper;

import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.infrastructure.web.dto.ProductResponse;

public final class ProductWebMapper {

    private ProductWebMapper() {
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId().value(),
                product.getTitle(),
                product.getDescription(),
                product.getType(),
                product.getPrice().amount(),
                product.getPrice().currency().getCurrencyCode(),
                product.getStatus());
    }
}