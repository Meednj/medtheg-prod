package com.medthegprod.backend.infrastructure.observability;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MicrometerSalesMetricsTest {

    @Test
    void shouldIncrementOrderPaidCounter() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();

        MicrometerSalesMetrics metrics = new MicrometerSalesMetrics(registry);

        metrics.orderPaid();
        metrics.orderPaid();

        assertEquals(
                2.0,
                registry
                        .counter("medtheg_sales_orders_paid")
                        .count());
    }
}