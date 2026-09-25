package com.medthegprod.backend.entitlement.infrastructure.event;

import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.entitlement.application.usecase.GrantEntitlementUseCase;
import com.medthegprod.backend.sales.application.event.OrderPaidEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderPaidEventListenerTest {

    @Mock
    private GrantEntitlementUseCase grantEntitlementUseCase;

    @InjectMocks
    private OrderPaidEventListener listener;

    @Test
    void shouldGrantEntitlementForEachProductWhenOrderIsPaid() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();

        var event = new OrderPaidEvent(
                orderId,
                customerId,
                List.of(productId1, productId2));

        // Act
        listener.handle(event);

        // Assert
        verify(grantEntitlementUseCase, times(1))
                .execute(
                        customerId,
                        new ProductId(productId1),
                        orderId);

        verify(grantEntitlementUseCase, times(1))
                .execute(
                        customerId,
                        new ProductId(productId2),
                        orderId);
    }
}