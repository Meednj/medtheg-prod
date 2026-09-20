package com.medthegprod.backend.identity.application.usecase;

import com.medthegprod.backend.identity.domain.model.User;

public interface RegisterUserUseCase {

    User execute(
            String email,
            String password);
}