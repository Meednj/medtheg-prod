package com.medthegprod.backend.catalog.infrastructure.web.dto;

import com.medthegprod.backend.catalog.domain.model.ProductStatus;
import com.medthegprod.backend.catalog.domain.model.ProductType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record ProductResponse(
                UUID id,
                String title,
                String description,
                ProductType type,
                BigDecimal price,
                String currency,
                ProductStatus status,
                Set<com.medthegprod.backend.catalog.domain.model.ProductCategory> categories,
                UUID coverAssetId,
                UUID previewAssetId,
                List<DigitalAssetResponse> assets) {
}