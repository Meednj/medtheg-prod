package com.medthegprod.backend.entitlement.infrastructure.persistence.mapper;

import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.entitlement.domain.model.Entitlement;
import com.medthegprod.backend.entitlement.domain.model.EntitlementId;
import com.medthegprod.backend.entitlement.infrastructure.persistence.entity.EntitlementEntity;
import org.springframework.stereotype.Component;

@Component
public class EntitlementMapper {

    public EntitlementEntity toEntity(Entitlement entitlement) {
        return new EntitlementEntity(
                entitlement.getId().value(),
                entitlement.getCustomerId(),
                entitlement.getProductId().value(),
                entitlement.getOrderId(),
                entitlement.getStatus(),
                entitlement.getGrantedAt(),
                entitlement.getRevokedAt());
    }

    public Entitlement toDomain(EntitlementEntity entity) {
        return new Entitlement(
                new EntitlementId(entity.getId()),
                entity.getCustomerId(),
                new ProductId(entity.getProductId()),
                entity.getOrderId(),
                entity.getStatus(),
                entity.getGrantedAt(),
                entity.getRevokedAt());
    }
}