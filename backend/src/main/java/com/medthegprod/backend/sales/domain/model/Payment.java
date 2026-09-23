package com.medthegprod.backend.sales.domain.model;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Payment {

    private final UUID id;
    private final OrderId orderId;
    private final String provider;
    private final String providerPaymentId;
    private PaymentStatus status;
    private final OffsetDateTime createdAt;
    private OffsetDateTime completedAt;
    private final String checkoutSessionId;
    private final String checkoutUrl;

    
    public Payment(
            UUID id,
            OrderId orderId,
            String provider,
            String providerPaymentId,
            PaymentStatus status,
            OffsetDateTime createdAt,
            OffsetDateTime completedAt,
            String checkoutSessionId,
            String checkoutUrl) {
        this.id = Objects.requireNonNull(id);
        this.orderId = Objects.requireNonNull(orderId);
        this.provider = Objects.requireNonNull(provider);
        this.providerPaymentId = Objects.requireNonNull(providerPaymentId);
        this.checkoutSessionId = Objects.requireNonNull(checkoutSessionId);
        this.checkoutUrl = Objects.requireNonNull(checkoutUrl);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.completedAt = completedAt;
    }

    public void markAsCompleted(OffsetDateTime completedAt) {
        if (status == PaymentStatus.COMPLETED) {
            return;
        }

        this.status = PaymentStatus.COMPLETED;
        this.completedAt = Objects.requireNonNull(completedAt);
    }

    public UUID getId() {
        return id;
    }

    public OrderId getOrderId() {
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