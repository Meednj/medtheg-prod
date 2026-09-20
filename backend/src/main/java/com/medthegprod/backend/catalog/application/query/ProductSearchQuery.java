package com.medthegprod.backend.catalog.application.query;

import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductType;

public record ProductSearchQuery(
                int page,
                int size,
                ProductType type,
                ProductCategory category,
                String search,
                String sortBy,
                String direction) {
}