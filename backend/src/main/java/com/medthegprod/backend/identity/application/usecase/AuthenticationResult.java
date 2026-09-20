package com.medthegprod.backend.identity.application.usecase;

import com.medthegprod.backend.identity.domain.model.User;

public record AuthenticationResult(
        User user,
        String accessToken) {
}