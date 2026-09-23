package com.medthegprod.backend.sales.application.usecase;

import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderId;

public interface GetOrderUseCase {

    Order execute(
            OrderId orderId,
            UserId customerId);
}