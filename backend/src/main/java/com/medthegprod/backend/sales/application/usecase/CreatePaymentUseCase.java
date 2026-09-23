package com.medthegprod.backend.sales.application.usecase;

import com.medthegprod.backend.sales.domain.model.OrderId;

import java.util.UUID;

public interface CreatePaymentUseCase {

    PaymentResult execute(
            OrderId orderId,
            UUID customerId);

    record PaymentResult(
            UUID orderId,
            String paymentId,
            String checkoutUrl) {
    }
}