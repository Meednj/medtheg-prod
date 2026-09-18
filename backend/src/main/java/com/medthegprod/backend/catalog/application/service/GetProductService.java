package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.application.usecase.GetProductUseCase;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class GetProductService implements GetProductUseCase {

    private final ProductRepository productRepository;

    public GetProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(ProductId productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}