package com.medthegprod.backend.catalog.infrastructure.web.dto;

import java.util.List;

public record ProductPageResponse(
        List<ProductResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}