package com.medthegprod.backend.sales.infrastructure.web.dto;

import com.medthegprod.backend.sales.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID customerId,
        OrderStatus status,
        BigDecimal total,
        String currency,
        OffsetDateTime createdAt,
        OffsetDateTime paidAt,
        List<OrderItemResponse> items) {

    public record OrderItemResponse(
            UUID id,
            UUID productId,
            String productTitle,
            BigDecimal unitPrice,
            String currency,
            int quantity,
            BigDecimal subtotal) {
    }
}