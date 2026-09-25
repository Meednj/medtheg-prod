package com.medthegprod.backend.entitlement.infrastructure.web.dto;

import com.medthegprod.backend.entitlement.domain.model.Entitlement;

import java.time.OffsetDateTime;
import java.util.UUID;

public record EntitlementResponse(
        UUID id,
        UUID productId,
        UUID orderId,
        String status,
        OffsetDateTime grantedAt) {

    public static EntitlementResponse from(
            Entitlement entitlement) {
        return new EntitlementResponse(
                entitlement.getId().value(),
                entitlement.getProductId().value(),
                entitlement.getOrderId(),
                entitlement.getStatus().name(),
                entitlement.getGrantedAt());
    }
}