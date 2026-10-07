package com.medthegprod.backend.sales.application.service;

import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderId;
import com.medthegprod.backend.sales.domain.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Test
    void shouldReturnOwnedOrder() {
        UserId customerId = UserId.generate();
        Order order = new Order(OrderId.generate(), customerId, OffsetDateTime.now());
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertSame(order, new GetOrderService(orderRepository).execute(order.getId(), customerId));
    }

    @Test
    void shouldRejectOrderOwnedByAnotherCustomer() {
        UserId owner = UserId.generate();
        Order order = new Order(OrderId.generate(), owner, OffsetDateTime.now());
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(OrderAccessDeniedException.class,
                () -> new GetOrderService(orderRepository).execute(order.getId(), UserId.generate()));
    }

    @Test
    void shouldRejectMissingOrder() {
        OrderId orderId = OrderId.generate();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> new GetOrderService(orderRepository).execute(orderId, UserId.generate()));
    }
}