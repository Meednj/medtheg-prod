package com.medthegprod.backend.sales.application.usecase;

import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.domain.model.Order;

import java.util.List;
import java.util.UUID;

public interface CreateOrderUseCase {

    Order execute(
            UserId customerId,
            List<Item> items);

    record Item(
            UUID productId,
            int quantity) {
    }
}