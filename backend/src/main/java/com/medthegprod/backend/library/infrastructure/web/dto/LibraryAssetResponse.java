package com.medthegprod.backend.library.infrastructure.web.dto;

import com.medthegprod.backend.library.application.usecase.LibraryAsset;

import java.util.UUID;

public record LibraryAssetResponse(
        UUID assetId,
        UUID productId,
        String type) {
    public static LibraryAssetResponse from(LibraryAsset asset) {
        return new LibraryAssetResponse(
                asset.assetId(),
                asset.productId(),
                asset.type());
    }
}