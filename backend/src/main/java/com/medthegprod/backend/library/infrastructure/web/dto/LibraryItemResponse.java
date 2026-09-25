package com.medthegprod.backend.library.infrastructure.web.dto;

import com.medthegprod.backend.library.application.usecase.LibraryItem;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record LibraryItemResponse(
        UUID entitlementId,
        UUID productId,
        UUID orderId,
        String title,
        String description,
        String type,
        BigDecimal price,
        String currency,
        OffsetDateTime grantedAt,
        List<LibraryAssetResponse> assets) {

    public static LibraryItemResponse from(LibraryItem item) {
        return new LibraryItemResponse(
                item.entitlementId(),
                item.productId(),
                item.orderId(),
                item.title(),
                item.description(),
                item.type(),
                item.price(),
                item.currency().getCurrencyCode(),
                item.grantedAt(),
                item.assets()
                        .stream()
                        .map(LibraryAssetResponse::from)
                        .toList());
    }
}
