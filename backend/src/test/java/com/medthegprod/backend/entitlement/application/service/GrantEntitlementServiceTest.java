package com.medthegprod.backend.entitlement.application.service;

import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.entitlement.domain.model.Entitlement;
import com.medthegprod.backend.entitlement.domain.repository.EntitlementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrantEntitlementServiceTest {

    @Mock
    private EntitlementRepository entitlementRepository;

    @Test
    void shouldReturnExistingEntitlementForRepeatedPaidEvent() {
        UUID customerId = UUID.randomUUID();
        ProductId productId = ProductId.generate();
        Entitlement existing = Entitlement.create(customerId, productId, UUID.randomUUID());
        when(entitlementRepository.findByCustomerIdAndProductId(customerId, productId.value()))
                .thenReturn(Optional.of(existing));

        Entitlement result = new GrantEntitlementService(entitlementRepository)
                .execute(customerId, productId, UUID.randomUUID());

        assertSame(existing, result);
        verify(entitlementRepository, never()).save(any());
    }

    @Test
    void shouldCreateEntitlementWhenCustomerDoesNotOwnProduct() {
        UUID customerId = UUID.randomUUID();
        ProductId productId = ProductId.generate();
        UUID orderId = UUID.randomUUID();
        when(entitlementRepository.findByCustomerIdAndProductId(customerId, productId.value()))
                .thenReturn(Optional.empty());
        when(entitlementRepository.save(any(Entitlement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Entitlement result = new GrantEntitlementService(entitlementRepository)
                .execute(customerId, productId, orderId);

        assertSame(customerId, result.getCustomerId());
        assertSame(productId, result.getProductId());
        assertSame(orderId, result.getOrderId());
        verify(entitlementRepository).save(any(Entitlement.class));
    }
}