package com.medthegprod.backend.sales.infrastructure.persistence;

import com.medthegprod.backend.sales.domain.model.OrderId;
import com.medthegprod.backend.sales.domain.model.Payment;
import com.medthegprod.backend.sales.domain.repository.PaymentRepository;
import com.medthegprod.backend.sales.infrastructure.persistence.entity.PaymentEntity;
import com.medthegprod.backend.sales.infrastructure.persistence.mapper.PaymentMapper;
import com.medthegprod.backend.sales.infrastructure.persistence.repository.PaymentJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class PaymentPersistenceAdapter implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentPersistenceAdapter(
            PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    @Transactional
    public Payment save(Payment payment) {
        PaymentEntity entity = PaymentMapper.toEntity(payment);
        PaymentEntity saved = paymentJpaRepository.save(entity);
        return PaymentMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findByProviderPaymentId(
            String provider,
            String providerPaymentId) {
        return paymentJpaRepository
                .findByProviderAndProviderPaymentId(
                        provider,
                        providerPaymentId)
                .map(PaymentMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findByOrderId(OrderId orderId) {
        return paymentJpaRepository
                .findByOrderId(orderId.value())
                .map(PaymentMapper::toDomain);
    }

    @Override
    public Optional<Payment> findByCheckoutSessionId(String checkoutSessionId) {
        return paymentJpaRepository
                .findByCheckoutSessionId(checkoutSessionId)
                .map(PaymentMapper::toDomain);
    }
}