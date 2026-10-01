package com.medthegprod.backend.infrastructure.observability;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MicrometerBusinessMetricsTest {

    @Test
    void shouldIncrementProductPublishedCounter() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();

        MicrometerBusinessMetrics metrics = new MicrometerBusinessMetrics(registry);

        metrics.productPublished();
        metrics.productPublished();

        assertEquals(
                2.0,
                registry
                        .counter("medtheg_catalog_products_published")
                        .count());
    }
}