package com.medthegprod.backend.catalog.infrastructure.web.dto;

import java.util.UUID;

import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;

public record DigitalAssetResponse(
        UUID id,
        DigitalAssetType type,
        String storageKey) {
}