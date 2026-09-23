package com.medthegprod.backend.sales.application.service;

import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.application.usecase.ListCustomerOrdersUseCase;
import com.medthegprod.backend.sales.domain.model.OrderPage;
import com.medthegprod.backend.sales.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ListCustomerOrdersService
        implements ListCustomerOrdersUseCase {

    private final OrderRepository orderRepository;

    public ListCustomerOrdersService(
            OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPage execute(
            UserId customerId,
            int page,
            int size) {
        Objects.requireNonNull(
                customerId,
                "Customer ID cannot be null");

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        return orderRepository.findByCustomerId(
                customerId,
                page,
                size);
    }
}