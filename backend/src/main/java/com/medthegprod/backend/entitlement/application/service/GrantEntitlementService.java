package com.medthegprod.backend.entitlement.application.service;

import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.entitlement.application.usecase.GrantEntitlementUseCase;
import com.medthegprod.backend.entitlement.domain.model.Entitlement;
import com.medthegprod.backend.entitlement.domain.model.EntitlementId;
import com.medthegprod.backend.entitlement.domain.model.EntitlementStatus;
import com.medthegprod.backend.entitlement.domain.repository.EntitlementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class GrantEntitlementService implements GrantEntitlementUseCase {

    private final EntitlementRepository entitlementRepository;

    public GrantEntitlementService(
            EntitlementRepository entitlementRepository) {
        this.entitlementRepository = entitlementRepository;
    }

    @Override
    @Transactional
    public Entitlement execute(
            UUID customerId,
            ProductId productId,
            UUID orderId) {
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(productId);
        Objects.requireNonNull(orderId);

        var existing = entitlementRepository
                .findByCustomerIdAndProductId(
                        customerId,
                        productId.value());

        if (existing.isPresent()) {
            return existing.get();
        }

        Entitlement entitlement = new Entitlement(
                EntitlementId.generate(),
                customerId,
                productId,
                orderId,
                EntitlementStatus.ACTIVE,
                OffsetDateTime.now(),
                null);

        return entitlementRepository.save(entitlement);
    }
}