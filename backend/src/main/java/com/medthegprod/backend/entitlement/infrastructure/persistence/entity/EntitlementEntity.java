package com.medthegprod.backend.entitlement.infrastructure.persistence.entity;

import com.medthegprod.backend.entitlement.domain.model.EntitlementStatus;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "entitlements", uniqueConstraints = {
        @UniqueConstraint(name = "uk_entitlements_customer_product", columnNames = { "customer_id", "product_id" })
}, indexes = {
        @Index(name = "idx_entitlements_customer_id", columnList = "customer_id"),
        @Index(name = "idx_entitlements_product_id", columnList = "product_id"),
        @Index(name = "idx_entitlements_order_id", columnList = "order_id")
})
public class EntitlementEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private EntitlementStatus status;

    @Column(name = "granted_at", nullable = false)
    private OffsetDateTime grantedAt;

    @Column(name = "revoked_at")
    private OffsetDateTime revokedAt;

    protected EntitlementEntity() {
    }

    public EntitlementEntity(
            UUID id,
            UUID customerId,
            UUID productId,
            UUID orderId,
            EntitlementStatus status,
            OffsetDateTime grantedAt,
            OffsetDateTime revokedAt) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.orderId = orderId;
        this.status = status;
        this.grantedAt = grantedAt;
        this.revokedAt = revokedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public EntitlementStatus getStatus() {
        return status;
    }

    public OffsetDateTime getGrantedAt() {
        return grantedAt;
    }

    public OffsetDateTime getRevokedAt() {
        return revokedAt;
    }
}