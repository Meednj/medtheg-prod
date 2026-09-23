package com.medthegprod.backend.sales.application.service;

import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.application.port.PaymentGateway;
import com.medthegprod.backend.sales.application.usecase.CreatePaymentUseCase;
import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderId;
import com.medthegprod.backend.sales.domain.model.OrderStatus;
import com.medthegprod.backend.sales.domain.model.Payment;
import com.medthegprod.backend.sales.domain.model.PaymentStatus;
import com.medthegprod.backend.sales.domain.repository.OrderRepository;
import com.medthegprod.backend.sales.domain.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class CreatePaymentService implements CreatePaymentUseCase {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;

    public CreatePaymentService(
            OrderRepository orderRepository,
            PaymentRepository paymentRepository,
            PaymentGateway paymentGateway) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
    }

    @Override
    @Transactional
    public PaymentResult execute(
            OrderId orderId,
            UUID customerId) {
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        Objects.requireNonNull(customerId, "Customer ID cannot be null");

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (!order.getCustomerId().equals(new UserId(customerId))) {
            throw new OrderAccessDeniedException();
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending orders can be paid");
        }

        var existingPayment = paymentRepository.findByOrderId(orderId);

        if (existingPayment.isPresent()) {
            Payment payment = existingPayment.get();

            return new PaymentResult(
                    order.getId().value(),
                    payment.getProviderPaymentId(),
                    payment.getCheckoutUrl());
        }

        PaymentGateway.PaymentSession session = paymentGateway.createPayment(order);

        Payment payment = new Payment(
                UUID.randomUUID(),
                orderId,
                "STRIPE",
                session.paymentId(),
                PaymentStatus.CREATED,
                OffsetDateTime.now(),
                null,
                session.checkoutSessionId(),
                session.checkoutUrl());

        paymentRepository.save(payment);

        return new PaymentResult(
                order.getId().value(),
                session.paymentId(),
                session.checkoutUrl());
    }
}