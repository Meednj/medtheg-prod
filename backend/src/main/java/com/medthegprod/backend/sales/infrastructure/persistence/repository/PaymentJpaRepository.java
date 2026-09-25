package com.medthegprod.backend.sales.infrastructure.persistence.repository;

import com.medthegprod.backend.sales.infrastructure.persistence.entity.PaymentEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository
                extends JpaRepository<PaymentEntity, UUID> {

        Optional<PaymentEntity> findByProviderAndProviderPaymentId(
                        String provider,
                        String providerPaymentId);

        Optional<PaymentEntity> findByOrderId(UUID orderId);

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        Optional<PaymentEntity> findByCheckoutSessionId(String checkoutSessionId);
}