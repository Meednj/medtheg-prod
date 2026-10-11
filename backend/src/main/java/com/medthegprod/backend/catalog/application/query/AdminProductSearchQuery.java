package com.medthegprod.backend.catalog.application.query;

import com.medthegprod.backend.catalog.domain.model.ProductStatus;
import com.medthegprod.backend.catalog.domain.model.ProductType;

public record AdminProductSearchQuery(
        int page,
        int size,
        ProductStatus status,
        ProductType type,
        String search,
        String sortBy,
        String direction) {
}
