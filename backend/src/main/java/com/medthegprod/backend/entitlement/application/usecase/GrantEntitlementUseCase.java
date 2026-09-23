package com.medthegprod.backend.entitlement.application.usecase;

import com.medthegprod.backend.entitlement.domain.model.Entitlement;
import com.medthegprod.backend.catalog.domain.model.ProductId;

import java.util.UUID;

public interface GrantEntitlementUseCase {

    Entitlement execute(
            UUID customerId,
            ProductId productId,
            UUID orderId);
}