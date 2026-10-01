package com.medthegprod.backend.infrastructure.observability;

import com.medthegprod.backend.catalog.application.event.ProductPublishedEvent;
import com.medthegprod.backend.catalog.application.port.BusinessMetrics;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ProductPublishedMetricsListenerTest {

    @Test
    void shouldIncrementMetricWhenProductIsPublished() {
        BusinessMetrics businessMetrics = mock(BusinessMetrics.class);

        ProductPublishedMetricsListener listener = new ProductPublishedMetricsListener(businessMetrics);

        ProductId productId = ProductId.generate();

        listener.handle(
                new ProductPublishedEvent(productId));

        verify(businessMetrics).productPublished();
    }
}