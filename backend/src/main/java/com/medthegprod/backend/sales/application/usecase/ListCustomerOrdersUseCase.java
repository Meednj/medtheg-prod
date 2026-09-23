package com.medthegprod.backend.sales.application.usecase;

import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.domain.model.OrderPage;

public interface ListCustomerOrdersUseCase {

    OrderPage execute(
            UserId customerId,
            int page,
            int size);
}