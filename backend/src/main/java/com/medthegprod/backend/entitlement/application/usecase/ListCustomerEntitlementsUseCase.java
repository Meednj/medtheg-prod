package com.medthegprod.backend.entitlement.application.usecase;

import com.medthegprod.backend.entitlement.domain.model.Entitlement;

import java.util.List;
import java.util.UUID;

public interface ListCustomerEntitlementsUseCase {

    List<Entitlement> execute(UUID customerId);
}