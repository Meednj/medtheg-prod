package com.medthegprod.backend.infrastructure.observability;

import com.medthegprod.backend.catalog.application.port.BusinessMetrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MicrometerBusinessMetrics implements BusinessMetrics {

    private final Counter productsPublished;

    public MicrometerBusinessMetrics(MeterRegistry meterRegistry) {
        this.productsPublished = Counter.builder(
                "medtheg_catalog_products_published")
                .description("Number of products published in the catalog")
                .register(meterRegistry);
    }

    @Override
    public void productPublished() {
        productsPublished.increment();
    }
}