package com.medthegprod.backend.catalog.infrastructure.observability;

import com.medthegprod.backend.catalog.application.port.BusinessMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductPublicationMetricsIntegrationTest {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private BusinessMetrics businessMetrics;

    @Test
    void shouldExposeProductPublicationCounter() {
        double before = meterRegistry
                .counter("medtheg_catalog_products_published")
                .count();

        businessMetrics.productPublished();

        double after = meterRegistry
                .counter("medtheg_catalog_products_published")
                .count();

        assertEquals(before + 1, after);
    }
}