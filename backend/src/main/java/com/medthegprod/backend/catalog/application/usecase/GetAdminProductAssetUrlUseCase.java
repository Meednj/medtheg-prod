package com.medthegprod.backend.catalog.application.usecase;

import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;

import java.time.Duration;
import java.util.UUID;

public interface GetAdminProductAssetUrlUseCase {
    String execute(UUID productId, UUID assetId, DigitalAssetType expectedType, Duration expiration);
}
