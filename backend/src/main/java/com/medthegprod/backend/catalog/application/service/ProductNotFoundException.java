package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.domain.model.ProductId;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(ProductId productId) {
        super("Product not found: " + productId.value());
    }
}