package com.medthegprod.backend.sales.domain.model;

import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.identity.domain.model.UserId;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {

    private final OrderId id;
    private final UserId customerId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final OffsetDateTime createdAt;
    private OffsetDateTime paidAt;

    public Order(
            OrderId id,
            UserId customerId,
            OffsetDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "Order ID cannot be null");
        this.customerId = Objects.requireNonNull(customerId, "Customer ID cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");

        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    public void addItem(OrderItem item) {
        Objects.requireNonNull(item, "Order item cannot be null");

        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Items can only be added to pending orders");
        }

        boolean productAlreadyExists = items.stream()
                .anyMatch(existing -> existing.getProductId().equals(item.getProductId()));

        if (productAlreadyExists) {
            throw new IllegalArgumentException(
                    "Product already exists in this order");
        }

        items.add(item);
    }

    public void markAsPaid(OffsetDateTime paidAt) {
        Objects.requireNonNull(paidAt, "Paid at cannot be null");

        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending orders can be paid");
        }

        if (items.isEmpty()) {
            throw new IllegalStateException(
                    "An order cannot be paid without items");
        }

        this.status = OrderStatus.PAID;
        this.paidAt = paidAt;
    }

    public void cancel() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending orders can be cancelled");
        }

        status = OrderStatus.CANCELLED;
    }

    public void refund() {
        if (status != OrderStatus.PAID) {
            throw new IllegalStateException(
                    "Only paid orders can be refunded");
        }

        status = OrderStatus.REFUNDED;
    }

    public Money total() {
        if (items.isEmpty()) {
            return Money.eur(BigDecimal.ZERO);
        }

        Money first = items.get(0).subtotal();

        BigDecimal totalAmount = items.stream()
                .skip(1)
                .map(item -> item.subtotal().amount())
                .reduce(
                        first.amount(),
                        BigDecimal::add);

        return new Money(
                totalAmount,
                first.currency());
    }

    public OrderId getId() {
        return id;
    }

    public UserId getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getPaidAt() {
        return paidAt;
    }
}