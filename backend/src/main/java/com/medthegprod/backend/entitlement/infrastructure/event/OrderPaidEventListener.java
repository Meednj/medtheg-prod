package com.medthegprod.backend.entitlement.infrastructure.event;

import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.entitlement.application.usecase.GrantEntitlementUseCase;
import com.medthegprod.backend.sales.application.event.OrderPaidEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderPaidEventListener {

    private final GrantEntitlementUseCase grantEntitlementUseCase;

    public OrderPaidEventListener(
            GrantEntitlementUseCase grantEntitlementUseCase) {
        this.grantEntitlementUseCase = grantEntitlementUseCase;
    }

    @EventListener
    public void handle(OrderPaidEvent event) {

        for (var productId : event.productIds()) {

            grantEntitlementUseCase.execute(
                    event.customerId(),
                    new ProductId(productId),
                    event.orderId());
        }
    }
}