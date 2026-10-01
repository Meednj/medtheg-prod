package com.medthegprod.backend.infrastructure.observability;

import com.medthegprod.backend.sales.application.event.OrderPaidEvent;
import com.medthegprod.backend.sales.application.port.SalesMetrics;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OrderPaidMetricsListenerTest {

    @Test
    void shouldIncrementMetricWhenOrderIsPaid() {
        SalesMetrics salesMetrics = mock(SalesMetrics.class);

        OrderPaidMetricsListener listener = new OrderPaidMetricsListener(salesMetrics);

        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        OrderPaidEvent event = new OrderPaidEvent(
                orderId,
                customerId,
                List.of(productId));

        listener.handle(event);

        verify(salesMetrics).orderPaid();
    }
}