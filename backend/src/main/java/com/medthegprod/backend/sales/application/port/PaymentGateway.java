package com.medthegprod.backend.sales.application.port;

import com.medthegprod.backend.sales.domain.model.Order;

public interface PaymentGateway {

    PaymentSession createPayment(Order order);

    record PaymentSession(
            String paymentId,
            String checkoutSessionId,
            String checkoutUrl) {
    }
}