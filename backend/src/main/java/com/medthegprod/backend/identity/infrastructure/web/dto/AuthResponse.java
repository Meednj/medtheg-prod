package com.medthegprod.backend.identity.infrastructure.web.dto;

import com.medthegprod.backend.identity.domain.model.Role;

import java.util.UUID;

public record AuthResponse(
        UUID userId,
        String email,
        Role role,
        String accessToken) {
}