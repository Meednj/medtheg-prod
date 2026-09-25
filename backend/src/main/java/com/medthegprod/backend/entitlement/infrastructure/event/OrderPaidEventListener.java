package com.medthegprod.backend.entitlement.infrastructure.event;

import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.entitlement.application.usecase.GrantEntitlementUseCase;
import com.medthegprod.backend.sales.application.event.OrderPaidEvent;

import java.util.UUID;

import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderPaidEventListener {

    private final GrantEntitlementUseCase grantEntitlementUseCase;

    public OrderPaidEventListener(
            GrantEntitlementUseCase grantEntitlementUseCase) {
        this.grantEntitlementUseCase = grantEntitlementUseCase;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderPaidEvent event) {

        for (UUID productId : event.productIds()) {
            grantEntitlementUseCase.execute(
                    event.customerId(),
                    new ProductId(productId),
                    event.orderId());
        }
    }
}