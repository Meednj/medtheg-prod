package com.medthegprod.backend.sales.infrastructure.web.mapper;

import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderItem;
import com.medthegprod.backend.sales.infrastructure.web.dto.OrderResponse;

public final class OrderWebMapper {

    private OrderWebMapper() {
    }

    public static OrderResponse toResponse(Order order) {

        String currency = order.total()
                .currency()
                .getCurrencyCode();

        return new OrderResponse(
                order.getId().value(),
                order.getCustomerId().value(),
                order.getStatus(),
                order.total().amount(),
                currency,
                order.getCreatedAt(),
                order.getPaidAt(),
                order.getItems()
                        .stream()
                        .map(OrderWebMapper::toItemResponse)
                        .toList());
    }

    private static OrderResponse.OrderItemResponse toItemResponse(
            OrderItem item) {
        return new OrderResponse.OrderItemResponse(
                item.getId().value(),
                item.getProductId().value(),
                item.getProductTitle(),
                item.getUnitPrice().amount(),
                item.getUnitPrice().currency().getCurrencyCode(),
                item.getQuantity(),
                item.subtotal().amount());
    }
}