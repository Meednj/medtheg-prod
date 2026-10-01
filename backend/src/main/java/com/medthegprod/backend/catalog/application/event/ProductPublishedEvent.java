package com.medthegprod.backend.catalog.application.event;

import com.medthegprod.backend.catalog.domain.model.ProductId;

public record ProductPublishedEvent(ProductId productId) {
}