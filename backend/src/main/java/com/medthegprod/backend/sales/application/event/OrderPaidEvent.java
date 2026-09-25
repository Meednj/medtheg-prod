package com.medthegprod.backend.sales.application.event;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record OrderPaidEvent(
                UUID orderId,
                UUID customerId,
                List<UUID> productIds) {

        public OrderPaidEvent {
                orderId = Objects.requireNonNull(orderId, "Order ID cannot be null");
                customerId = Objects.requireNonNull(customerId, "Customer ID cannot be null");
                productIds = List.copyOf(Objects.requireNonNull(productIds, "Product IDs cannot be null"));
        }
}