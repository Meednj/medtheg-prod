package com.medthegprod.backend.infrastructure.observability;

import com.medthegprod.backend.sales.application.event.OrderPaidEvent;
import com.medthegprod.backend.sales.application.port.SalesMetrics;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderPaidMetricsListener {

    private final SalesMetrics salesMetrics;

    public OrderPaidMetricsListener(SalesMetrics salesMetrics) {
        this.salesMetrics = salesMetrics;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderPaidEvent event) {
        salesMetrics.orderPaid();
    }
}