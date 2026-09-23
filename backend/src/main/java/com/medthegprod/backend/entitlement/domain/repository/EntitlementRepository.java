package com.medthegprod.backend.entitlement.domain.repository;

import com.medthegprod.backend.entitlement.domain.model.Entitlement;
import com.medthegprod.backend.entitlement.domain.model.EntitlementId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntitlementRepository {

    Entitlement save(Entitlement entitlement);

    Optional<Entitlement> findById(EntitlementId id);

    Optional<Entitlement> findByCustomerIdAndProductId(
            UUID customerId,
            UUID productId);

    List<Entitlement> findByCustomerId(UUID customerId);
}