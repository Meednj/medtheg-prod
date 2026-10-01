package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.application.usecase.ArchiveProductUseCase;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArchiveProductService implements ArchiveProductUseCase {

    private final ProductRepository productRepository;

    public ArchiveProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product execute(ProductId productId) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        product.archive();
        return productRepository.save(product);
    }
}