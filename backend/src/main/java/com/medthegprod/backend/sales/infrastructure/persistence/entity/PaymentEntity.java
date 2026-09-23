package com.medthegprod.backend.sales.infrastructure.persistence.entity;

import com.medthegprod.backend.sales.domain.model.PaymentStatus;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments", uniqueConstraints = {
        @UniqueConstraint(name = "uk_payments_provider_payment_id", columnNames = { "provider", "provider_payment_id" })
})
public class PaymentEntity {

    @Id
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(nullable = false, length = 50)
    private String provider;

    @Column(name = "provider_payment_id", nullable = false, length = 255)
    private String providerPaymentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PaymentStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "checkout_session_id", nullable = false, unique = true)
    private String checkoutSessionId;

    @Column(name = "checkout_url", nullable = false, length = 1000)
    private String checkoutUrl;

    protected PaymentEntity() {
    }

    public PaymentEntity(
            UUID id,
            UUID orderId,
            String provider,
            String providerPaymentId,
            PaymentStatus status,
            OffsetDateTime createdAt,
            OffsetDateTime completedAt,
            String checkoutSessionId,
            String checkoutUrl) {
        this.id = id;
        this.orderId = orderId;
        this.provider = provider;
        this.providerPaymentId = providerPaymentId;
        this.status = status;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.checkoutSessionId = checkoutSessionId;
        this.checkoutUrl = checkoutUrl;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getProvider() {
        return provider;
    }

    public String getProviderPaymentId() {
        return providerPaymentId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getCompletedAt() {
        return completedAt;
    }

    public String getCheckoutSessionId() {
        return checkoutSessionId;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }
}