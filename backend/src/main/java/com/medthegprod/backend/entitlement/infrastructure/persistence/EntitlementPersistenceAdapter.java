package com.medthegprod.backend.entitlement.infrastructure.persistence;

import com.medthegprod.backend.entitlement.domain.model.Entitlement;
import com.medthegprod.backend.entitlement.domain.model.EntitlementId;
import com.medthegprod.backend.entitlement.domain.repository.EntitlementRepository;
import com.medthegprod.backend.entitlement.infrastructure.persistence.mapper.EntitlementMapper;
import com.medthegprod.backend.entitlement.infrastructure.persistence.repository.EntitlementJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EntitlementPersistenceAdapter
        implements EntitlementRepository {

    private final EntitlementJpaRepository entitlementJpaRepository;
    private final EntitlementMapper entitlementMapper;

    public EntitlementPersistenceAdapter(
            EntitlementJpaRepository entitlementJpaRepository,
            EntitlementMapper entitlementMapper) {
        this.entitlementJpaRepository = entitlementJpaRepository;
        this.entitlementMapper = entitlementMapper;
    }

    @Override
    public Entitlement save(Entitlement entitlement) {
        return entitlementMapper.toDomain(
                entitlementJpaRepository.save(
                        entitlementMapper.toEntity(entitlement)));
    }

    @Override
    public Optional<Entitlement> findById(EntitlementId id) {
        return entitlementJpaRepository
                .findById(id.value())
                .map(entitlementMapper::toDomain);
    }

    @Override
    public Optional<Entitlement> findByCustomerIdAndProductId(
            UUID customerId,
            UUID productId) {
        return entitlementJpaRepository
                .findByCustomerIdAndProductId(customerId, productId)
                .map(entitlementMapper::toDomain);
    }

    @Override
    public List<Entitlement> findByCustomerId(UUID customerId) {
        return entitlementJpaRepository
                .findByCustomerId(customerId)
                .stream()
                .map(entitlementMapper::toDomain)
                .toList();
    }
}