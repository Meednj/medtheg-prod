package com.medthegprod.backend.catalog.application.service;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.medthegprod.backend.catalog.application.usecase.CreateProductUseCase;
import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.model.ProductType;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;

@Service
public class CreateProductService implements CreateProductUseCase {

    private final ProductRepository productRepository;

    public CreateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(
            String title,
            String description,
            ProductType type,
            BigDecimal price,
            Set<ProductCategory> categories) {
        Product product = new Product(
                ProductId.generate(),
                title,
                description,
                type,
                Money.eur(price));

        if (categories != null) {
            categories.forEach(product::addCategory);
        }

        return productRepository.save(product);
    }
}
