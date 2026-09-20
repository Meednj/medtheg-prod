package com.medthegprod.backend.identity.infrastructure.web.dto;

import com.medthegprod.backend.identity.domain.model.Role;
import com.medthegprod.backend.identity.domain.model.UserStatus;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        Role role,
        UserStatus status) {
}