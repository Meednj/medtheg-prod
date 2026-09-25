package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.application.usecase.ListProductAssetsUseCase;
import com.medthegprod.backend.catalog.application.usecase.ProductAsset;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListProductAssetsService
        implements ListProductAssetsUseCase {

    private final ProductRepository productRepository;

    public ListProductAssetsService(
            ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    @Override
    public List<ProductAsset> execute(ProductId productId) {

        if (productId == null) {
            throw new IllegalArgumentException(
                    "Product ID cannot be null");
        }

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(productId));

        return product.getAssets()
                .stream()
                .map(asset ->
                        new ProductAsset(
                                product.getId(),
                                asset))
                .toList();
    }
}

