package com.medthegprod.backend.catalog.domain.model;

import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId {
        if (value == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }

}