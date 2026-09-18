package com.medthegprod.backend.catalog.domain.model;

import java.util.UUID;

public record AssetId(UUID value) {

    public AssetId {
        if (value == null) {
            throw new IllegalArgumentException("Asset ID cannot be null");
        }
    }

    public static AssetId generate() {
        return new AssetId(UUID.randomUUID());
    }
}