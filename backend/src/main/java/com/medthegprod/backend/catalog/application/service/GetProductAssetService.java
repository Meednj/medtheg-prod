package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.application.usecase.GetProductAssetUseCase;
import com.medthegprod.backend.catalog.application.usecase.ProductAsset;
import com.medthegprod.backend.catalog.domain.model.DigitalAsset;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetProductAssetService implements GetProductAssetUseCase {

    private final ProductRepository productRepository;

    public GetProductAssetService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductAsset execute(UUID assetId) {

        Product product = productRepository
                .findByAssetId(assetId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product asset not found: " + assetId));

        DigitalAsset asset = product.getAssets()
                .stream()
                .filter(candidate -> candidate.getId().value().equals(assetId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product asset not found: " + assetId));

        return new ProductAsset(
                product.getId(),
                asset);
    }
}