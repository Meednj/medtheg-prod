package com.medthegprod.backend.entitlement.application.service;

import com.medthegprod.backend.entitlement.application.usecase.ListCustomerEntitlementsUseCase;
import com.medthegprod.backend.entitlement.domain.model.Entitlement;
import com.medthegprod.backend.entitlement.domain.repository.EntitlementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ListCustomerEntitlementsService
        implements ListCustomerEntitlementsUseCase {

    private final EntitlementRepository entitlementRepository;

    public ListCustomerEntitlementsService(
            EntitlementRepository entitlementRepository) {
        this.entitlementRepository = entitlementRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entitlement> execute(UUID customerId) {

        Objects.requireNonNull(
                customerId,
                "Customer ID cannot be null");

        return entitlementRepository
                .findByCustomerId(customerId)
                .stream()
                .filter(entitlement -> entitlement
                        .getStatus() == com.medthegprod.backend.entitlement.domain.model.EntitlementStatus.ACTIVE)
                .toList();
    }
}