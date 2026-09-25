package com.medthegprod.backend.catalog.infrastructure.web.dto;

import com.medthegprod.backend.catalog.application.usecase.ProductAsset;

import java.util.UUID;

public record ProductAssetResponse(
        UUID productId,
        UUID assetId,
        String type,
        String storageKey) {

    public static ProductAssetResponse from(ProductAsset productAsset) {
        return new ProductAssetResponse(
                productAsset.productId().value(),
                productAsset.asset().getId().value(),
                productAsset.asset().getType().name(),
                productAsset.asset().getStorageKey());
    }
}