package com.medthegprod.backend.sales.application.service;

import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.application.usecase.GetOrderUseCase;
import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderId;
import com.medthegprod.backend.sales.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class GetOrderService implements GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Order execute(
            OrderId orderId,
            UserId customerId) {
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        Objects.requireNonNull(customerId, "Customer ID cannot be null");

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (!order.getCustomerId().equals(customerId)) {
            throw new OrderAccessDeniedException();
        }

        return order;
    }
}