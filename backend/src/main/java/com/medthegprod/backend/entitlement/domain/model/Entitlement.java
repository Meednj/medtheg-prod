package com.medthegprod.backend.entitlement.domain.model;

import com.medthegprod.backend.catalog.domain.model.ProductId;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Entitlement {

    private final EntitlementId id;
    private final UUID customerId;
    private final ProductId productId;
    private final UUID orderId;
    private final OffsetDateTime grantedAt;

    private EntitlementStatus status;
    private OffsetDateTime revokedAt;

    public Entitlement(
            EntitlementId id,
            UUID customerId,
            ProductId productId,
            UUID orderId,
            EntitlementStatus status,
            OffsetDateTime grantedAt,
            OffsetDateTime revokedAt) {
        this.id = Objects.requireNonNull(id);
        this.customerId = Objects.requireNonNull(customerId);
        this.productId = Objects.requireNonNull(productId);
        this.orderId = Objects.requireNonNull(orderId);
        this.status = Objects.requireNonNull(status);
        this.grantedAt = Objects.requireNonNull(grantedAt);
        this.revokedAt = revokedAt;
    }

    public void revoke(OffsetDateTime revokedAt) {
        if (status == EntitlementStatus.REVOKED) {
            return;
        }

        this.status = EntitlementStatus.REVOKED;
        this.revokedAt = Objects.requireNonNull(revokedAt);
    }

    public EntitlementId getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public OffsetDateTime getGrantedAt() {
        return grantedAt;
    }

    public EntitlementStatus getStatus() {
        return status;
    }

    public OffsetDateTime getRevokedAt() {
        return revokedAt;
    }
}