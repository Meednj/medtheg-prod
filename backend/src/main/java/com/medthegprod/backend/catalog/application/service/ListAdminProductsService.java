package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.application.query.AdminProductSearchQuery;
import com.medthegprod.backend.catalog.application.usecase.ListAdminProductsUseCase;
import com.medthegprod.backend.catalog.domain.repository.ProductPage;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ListAdminProductsService implements ListAdminProductsUseCase {
    private final ProductRepository productRepository;

    public ListAdminProductsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductPage execute(AdminProductSearchQuery query) {
        if (query.page() < 0) {
            throw new IllegalArgumentException("Page cannot be negative");
        }
        if (query.size() < 1 || query.size() > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }
        if (query.search() != null && query.search().length() > 100) {
            throw new IllegalArgumentException("Search query cannot exceed 100 characters");
        }
        return productRepository.findAdminAll(query);
    }
}
