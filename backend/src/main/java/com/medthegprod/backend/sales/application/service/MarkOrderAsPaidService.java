package com.medthegprod.backend.sales.application.service;

import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderId;
import com.medthegprod.backend.sales.domain.model.OrderStatus;
import com.medthegprod.backend.sales.domain.model.Payment;
import com.medthegprod.backend.sales.domain.repository.OrderRepository;
import com.medthegprod.backend.sales.domain.repository.PaymentRepository;
import com.medthegprod.backend.sales.application.event.OrderPaidEvent;
import com.medthegprod.backend.sales.application.port.EventPublisher;
import com.medthegprod.backend.sales.application.usecase.MarkOrderAsPaidUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;

@Service
public class MarkOrderAsPaidService implements MarkOrderAsPaidUseCase {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final EventPublisher eventPublisher;

    public MarkOrderAsPaidService(
            OrderRepository orderRepository,
            PaymentRepository paymentRepository,
            EventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void execute(
            OrderId orderId,
            String checkoutSessionId) {
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        Objects.requireNonNull(
                checkoutSessionId,
                "Checkout session ID cannot be null");

        Payment payment = paymentRepository
                .findByCheckoutSessionId(checkoutSessionId)
                .orElseThrow(() -> new IllegalStateException(
                        "Payment not found for checkout session: "
                                + checkoutSessionId));

        if (!payment.getOrderId().equals(orderId)) {
            throw new IllegalStateException(
                    "Payment does not belong to the specified order");
        }

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (order.getStatus() == OrderStatus.PAID) {
            return;
        }

        OffsetDateTime completedAt = OffsetDateTime.now();

        payment.markAsCompleted(completedAt);
        paymentRepository.save(payment);

        order.markAsPaid(completedAt);
        orderRepository.save(order);

        eventPublisher.publish(
                new OrderPaidEvent(
                        order.getId().value(),
                        order.getCustomerId().value(),
                        order.getItems()
                                .stream()
                                .map(item -> item.getProductId().value())
                                .toList()));
    }

}