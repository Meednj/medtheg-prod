package com.medthegprod.backend.catalog.infrastructure.web.mapper;

import java.util.List;

import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.infrastructure.web.dto.DigitalAssetResponse;
import com.medthegprod.backend.catalog.infrastructure.web.dto.ProductResponse;

public final class ProductWebMapper {

    private ProductWebMapper() {
    }

    public static ProductResponse toResponse(Product product) {
        List<DigitalAssetResponse> assets = product.getAssets()
                .stream()
                .map(asset -> new DigitalAssetResponse(
                        asset.getId().value(),
                        asset.getType(),
                        asset.getStorageKey()))
                .toList();

        return new ProductResponse(
                product.getId().value(),
                product.getTitle(),
                product.getDescription(),
                product.getType(),
                product.getPrice().amount(),
                product.getPrice().currency().getCurrencyCode(),
                product.getStatus(),
                assets);
    }
}