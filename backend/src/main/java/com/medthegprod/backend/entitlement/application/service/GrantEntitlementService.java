package com.medthegprod.backend.entitlement.application.service;

import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.entitlement.application.usecase.GrantEntitlementUseCase;
import com.medthegprod.backend.entitlement.domain.model.Entitlement;
import com.medthegprod.backend.entitlement.domain.repository.EntitlementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class GrantEntitlementService
        implements GrantEntitlementUseCase {

    private final EntitlementRepository entitlementRepository;

    public GrantEntitlementService(
            EntitlementRepository entitlementRepository) {
        this.entitlementRepository = entitlementRepository;
    }

    @Override
    public Entitlement execute(
            UUID customerId,
            ProductId productId,
            UUID orderId) {

        if (customerId == null) {
            throw new IllegalArgumentException(
                    "Customer ID cannot be null");
        }

        if (productId == null) {
            throw new IllegalArgumentException(
                    "Product ID cannot be null");
        }

        if (orderId == null) {
            throw new IllegalArgumentException(
                    "Order ID cannot be null");
        }

        return entitlementRepository
                .findByCustomerIdAndProductId(
                        customerId,
                        productId.value())
                .orElseGet(() -> {

                    Entitlement entitlement = Entitlement.create(
                            customerId,
                            productId,
                            orderId);

                    return entitlementRepository
                            .save(entitlement);
                });
    }
}