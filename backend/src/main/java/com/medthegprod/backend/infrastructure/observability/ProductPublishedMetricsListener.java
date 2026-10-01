package com.medthegprod.backend.infrastructure.observability;

import com.medthegprod.backend.catalog.application.event.ProductPublishedEvent;
import com.medthegprod.backend.catalog.application.port.BusinessMetrics;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ProductPublishedMetricsListener {

    private final BusinessMetrics businessMetrics;

    public ProductPublishedMetricsListener(
            BusinessMetrics businessMetrics) {
        this.businessMetrics = businessMetrics;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductPublishedEvent event) {
        businessMetrics.productPublished();
    }
}