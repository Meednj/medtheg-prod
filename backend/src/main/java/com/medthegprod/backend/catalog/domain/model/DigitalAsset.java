package com.medthegprod.backend.catalog.domain.model;

import java.util.Objects;

public class DigitalAsset {

    private final AssetId id;
    private final DigitalAssetType type;
    private final String storageKey;

    public DigitalAsset(
            AssetId id,
            DigitalAssetType type,
            String storageKey) {
        this.id = Objects.requireNonNull(
                id,
                "Asset ID cannot be null");

        this.type = Objects.requireNonNull(
                type,
                "Asset type cannot be null");

        if (storageKey == null || storageKey.isBlank()) {
            throw new IllegalArgumentException(
                    "Storage key cannot be blank");
        }

        this.storageKey = storageKey;
    }

    public AssetId getId() {
        return id;
    }

    public DigitalAssetType getType() {
        return type;
    }

    public String getStorageKey() {
        return storageKey;
    }
}