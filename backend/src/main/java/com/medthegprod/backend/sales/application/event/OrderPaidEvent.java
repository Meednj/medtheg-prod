package com.medthegprod.backend.sales.application.event;

import java.util.List;
import java.util.UUID;

public record OrderPaidEvent(
        UUID orderId,
        UUID customerId,
        List<UUID> productIds) {
}