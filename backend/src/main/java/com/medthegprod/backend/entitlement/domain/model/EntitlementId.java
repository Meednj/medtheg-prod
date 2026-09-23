package com.medthegprod.backend.entitlement.domain.model;

import java.util.Objects;
import java.util.UUID;

public record EntitlementId(UUID value) {

    public EntitlementId {
        Objects.requireNonNull(value, "Entitlement ID cannot be null");
    }

    public static EntitlementId generate() {
        return new EntitlementId(UUID.randomUUID());
    }
}