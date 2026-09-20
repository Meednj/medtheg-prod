package com.medthegprod.backend.identity.application.usecase;

public interface AuthenticateUserUseCase {

    AuthenticationResult authenticate(
            String email,
            String password);
}