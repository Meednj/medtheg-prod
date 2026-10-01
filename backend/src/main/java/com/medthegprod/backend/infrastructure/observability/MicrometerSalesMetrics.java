package com.medthegprod.backend.infrastructure.observability;

import com.medthegprod.backend.sales.application.port.SalesMetrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MicrometerSalesMetrics implements SalesMetrics {

    private final Counter ordersPaid;

    public MicrometerSalesMetrics(MeterRegistry meterRegistry) {
        this.ordersPaid = Counter.builder(
                "medtheg_sales_orders_paid")
                .description("Number of orders successfully paid")
                .register(meterRegistry);
    }

    @Override
    public void orderPaid() {
        ordersPaid.increment();
    }
}