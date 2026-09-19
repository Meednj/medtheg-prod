package com.medthegprod.backend.catalog.domain.repository;

import com.medthegprod.backend.catalog.domain.model.Product;

import java.util.List;

public record ProductPage(
        List<Product> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}