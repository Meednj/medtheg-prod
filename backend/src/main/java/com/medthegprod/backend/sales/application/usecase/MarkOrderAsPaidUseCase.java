package com.medthegprod.backend.sales.application.usecase;

import com.medthegprod.backend.sales.domain.model.OrderId;

public interface MarkOrderAsPaidUseCase {

    void execute(OrderId orderId, String checkoutSessionId);
}