package com.medthegprod.backend.library.application.usecase;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

public record LibraryItem(
        UUID entitlementId,
        UUID productId,
        UUID orderId,
        String title,
        String description,
        String type,
        BigDecimal price,
        Currency currency,
        OffsetDateTime grantedAt,
        List<LibraryAsset> assets) {
}