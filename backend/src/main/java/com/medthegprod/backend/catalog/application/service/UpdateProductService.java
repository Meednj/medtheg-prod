package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.application.usecase.UpdateProductUseCase;
import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.model.ProductType;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepository productRepository;

    public UpdateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product execute(
            ProductId productId,
            String title,
            String description,
            ProductType type,
            BigDecimal price,
            Set<ProductCategory> categories) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.updateTitle(title);
        product.updateDescription(description);
        product.changePrice(Money.eur(price));

        product.clearCategories();

        if (categories != null) {
            categories.forEach(product::addCategory);
        }

        return productRepository.save(product);
    }
}