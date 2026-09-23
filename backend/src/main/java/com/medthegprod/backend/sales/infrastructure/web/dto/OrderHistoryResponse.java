package com.medthegprod.backend.sales.infrastructure.web.dto;

import com.medthegprod.backend.sales.domain.model.OrderPage;
import com.medthegprod.backend.sales.infrastructure.web.mapper.OrderWebMapper;
import java.util.List;

public record OrderHistoryResponse(
        List<OrderResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {

    public static OrderHistoryResponse from(
            OrderPage page) {
        return new OrderHistoryResponse(
                page.content()
                        .stream()
                        .map(OrderWebMapper::toResponse)
                        .toList(),
                page.page(),
                page.size(),
                page.totalElements(),
                page.totalPages());
    }
}