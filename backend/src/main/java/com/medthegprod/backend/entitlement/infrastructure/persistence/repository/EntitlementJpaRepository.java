package com.medthegprod.backend.entitlement.infrastructure.persistence.repository;

import com.medthegprod.backend.entitlement.infrastructure.persistence.entity.EntitlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntitlementJpaRepository
        extends JpaRepository<EntitlementEntity, UUID> {

    Optional<EntitlementEntity> findByCustomerIdAndProductId(
            UUID customerId,
            UUID productId);

    List<EntitlementEntity> findByCustomerId(UUID customerId);
}