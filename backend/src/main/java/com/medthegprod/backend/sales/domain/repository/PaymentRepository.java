package com.medthegprod.backend.sales.domain.repository;

import com.medthegprod.backend.sales.domain.model.Payment;
import com.medthegprod.backend.sales.domain.model.OrderId;

import java.util.Optional;


public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByProviderPaymentId(
            String provider,
            String providerPaymentId);

    Optional<Payment> findByOrderId(OrderId orderId);
    
    Optional<Payment> findByCheckoutSessionId(String checkoutSessionId);
}