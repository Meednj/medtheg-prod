package com.medthegprod.backend.sales.infrastructure.persistence.repository;

import com.medthegprod.backend.sales.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository
        extends JpaRepository<PaymentEntity, UUID> {

    Optional<PaymentEntity> findByProviderAndProviderPaymentId(
            String provider,
            String providerPaymentId);

    Optional<PaymentEntity> findByOrderId(UUID orderId);
    
    Optional<PaymentEntity> findByCheckoutSessionId(String checkoutSessionId);
}