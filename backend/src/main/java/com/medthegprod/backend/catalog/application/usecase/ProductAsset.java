package com.medthegprod.backend.catalog.application.usecase;

import com.medthegprod.backend.catalog.domain.model.DigitalAsset;
import com.medthegprod.backend.catalog.domain.model.ProductId;

public record ProductAsset(
        ProductId productId,
        DigitalAsset asset) {
}