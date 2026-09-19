package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.application.query.ProductSearchQuery;
import com.medthegprod.backend.catalog.application.usecase.ListProductsUseCase;
import com.medthegprod.backend.catalog.domain.repository.ProductPage;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ListProductsService implements ListProductsUseCase {

    private final ProductRepository productRepository;

    public ListProductsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductPage execute(ProductSearchQuery query) {

        if (query.page() < 0) {
            throw new IllegalArgumentException("Page cannot be negative");
        }

        if (query.size() < 1 || query.size() > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        return productRepository.findAll(query);
    }
}