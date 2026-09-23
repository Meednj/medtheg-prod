package com.medthegprod.backend.sales.application.service;

import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.application.port.PaymentGateway;
import com.medthegprod.backend.sales.application.usecase.CreatePaymentUseCase;
import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderId;
import com.medthegprod.backend.sales.domain.model.OrderItem;
import com.medthegprod.backend.sales.domain.model.OrderItemId;
import com.medthegprod.backend.sales.domain.repository.OrderRepository;
import com.medthegprod.backend.sales.domain.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePaymentServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private PaymentRepository paymentRepository;

    private CreatePaymentService service;

    @BeforeEach
    void setUp() {
        service = new CreatePaymentService(
                orderRepository,
                paymentRepository,
                paymentGateway);
    }

    @Test
    void shouldCreatePaymentForCustomerOrder() {
        UserId customerId = UserId.generate();
        OrderId orderId = OrderId.generate();
        Order order = createPendingOrder(orderId, customerId);

        when(orderRepository.findById(orderId))
                .thenReturn(java.util.Optional.of(order));
        when(paymentGateway.createPayment(order))
                .thenReturn(new PaymentGateway.PaymentSession(
                        "pi_test_123",
                        "cs_test_123",
                        "https://checkout.stripe.test/session"));

        CreatePaymentUseCase.PaymentResult result = service.execute(
                orderId,
                customerId.value());

        assertEquals(orderId.value(), result.orderId());
        assertEquals("pi_test_123", result.paymentId());
        assertEquals("https://checkout.stripe.test/session", result.checkoutUrl());
        verify(paymentGateway).createPayment(order);
    }

    @Test
    void shouldRejectNonexistentOrder() {
        OrderId orderId = OrderId.generate();
        UserId customerId = UserId.generate();

        when(orderRepository.findById(orderId))
                .thenReturn(java.util.Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> service.execute(orderId, customerId.value()));
        verifyNoInteractions(paymentGateway);
    }

    @Test
    void shouldRejectOrderBelongingToAnotherCustomer() {
        OrderId orderId = OrderId.generate();
        UserId owner = UserId.generate();
        UserId attacker = UserId.generate();
        Order order = createPendingOrder(orderId, owner);

        when(orderRepository.findById(orderId))
                .thenReturn(java.util.Optional.of(order));

        assertThrows(OrderAccessDeniedException.class,
                () -> service.execute(orderId, attacker.value()));
        verifyNoInteractions(paymentGateway);
    }

    @Test
    void shouldRejectAlreadyPaidOrder() {
        OrderId orderId = OrderId.generate();
        UserId customerId = UserId.generate();
        Order order = createPendingOrder(orderId, customerId);
        order.markAsPaid(OffsetDateTime.now());

        when(orderRepository.findById(orderId))
                .thenReturn(java.util.Optional.of(order));

        assertThrows(IllegalStateException.class,
                () -> service.execute(orderId, customerId.value()));
        verifyNoInteractions(paymentGateway);
    }

    @Test
    void shouldRejectCancelledOrder() {
        OrderId orderId = OrderId.generate();
        UserId customerId = UserId.generate();
        Order order = createPendingOrder(orderId, customerId);
        order.cancel();

        when(orderRepository.findById(orderId))
                .thenReturn(java.util.Optional.of(order));

        assertThrows(IllegalStateException.class,
                () -> service.execute(orderId, customerId.value()));
        verifyNoInteractions(paymentGateway);
    }

    private Order createPendingOrder(OrderId orderId, UserId customerId) {
        Order order = new Order(orderId, customerId, OffsetDateTime.now());
        order.addItem(new OrderItem(
                OrderItemId.generate(),
                ProductId.generate(),
                "Dark Beat",
                Money.eur(new BigDecimal("15.00")),
                1));
        return order;
    }
}