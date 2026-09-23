package com.medthegprod.backend.sales.domain.model;

import java.util.List;

public record OrderPage(
        List<Order> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}