package com.medthegprod.backend.sales.infrastructure.persistence.mapper;

import com.medthegprod.backend.sales.domain.model.OrderId;
import com.medthegprod.backend.sales.domain.model.Payment;
import com.medthegprod.backend.sales.infrastructure.persistence.entity.PaymentEntity;

public final class PaymentMapper {

    private PaymentMapper() {
    }

    public static PaymentEntity toEntity(Payment payment) {
        PaymentEntity entity = new PaymentEntity(
                payment.getId(),
                payment.getOrderId().value(),
                payment.getProvider(),
                payment.getProviderPaymentId(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getCompletedAt(),
                payment.getCheckoutSessionId(),
                payment.getCheckoutUrl());

        return entity;
    }

    public static Payment toDomain(PaymentEntity entity) {
        return new Payment(
                entity.getId(),
                new OrderId(entity.getOrderId()),
                entity.getProvider(),
                entity.getProviderPaymentId(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getCompletedAt(),
                entity.getCheckoutSessionId(),
                entity.getCheckoutUrl());
    }
}