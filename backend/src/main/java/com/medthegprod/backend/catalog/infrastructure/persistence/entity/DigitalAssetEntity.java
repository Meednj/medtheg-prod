package com.medthegprod.backend.catalog.infrastructure.persistence.entity;

import com.medthegprod.backend.catalog.infrastructure.persistence.entity.enums.DigitalAssetTypeEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "product_assets")
public class DigitalAssetEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private DigitalAssetTypeEntity type;

    @Column(name = "storage_key", nullable = false, unique = true)
    private String storageKey;

    protected DigitalAssetEntity() {
    }

    public DigitalAssetEntity(
            UUID id,
            ProductEntity product,
            DigitalAssetTypeEntity type,
            String storageKey) {
        this.id = id;
        this.product = product;
        this.type = type;
        this.storageKey = storageKey;
    }

    public UUID getId() {
        return id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public DigitalAssetTypeEntity getType() {
        return type;
    }

    public String getStorageKey() {
        return storageKey;
    }
}