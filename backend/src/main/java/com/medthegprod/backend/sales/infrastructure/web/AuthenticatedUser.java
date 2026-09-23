package com.medthegprod.backend.sales.infrastructure.web;

import com.medthegprod.backend.identity.domain.model.UserId;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public final class AuthenticatedUser {

    private AuthenticatedUser() {
    }

    public static UserId getUserId(Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getPrincipal();

        return new UserId(
                UUID.fromString(jwt.getSubject()));
    }
}