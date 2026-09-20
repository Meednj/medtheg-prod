package com.medthegprod.backend.identity.application.service;

import com.medthegprod.backend.identity.application.port.PasswordHasher;
import com.medthegprod.backend.identity.application.port.TokenGenerator;
import com.medthegprod.backend.identity.application.usecase.AuthenticationResult;
import com.medthegprod.backend.identity.application.usecase.AuthenticateUserUseCase;
import com.medthegprod.backend.identity.domain.model.User;
import com.medthegprod.backend.identity.domain.model.UserStatus;
import com.medthegprod.backend.identity.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticateUserService
        implements AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;

    public AuthenticateUserService(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationResult authenticate(
            String email,
            String password) {

        String normalizedEmail = email.trim().toLowerCase();

        User user = userRepository
                .findByEmail(normalizedEmail)
                .orElseThrow(InvalidCredentialsException::new);

        if (user.getStatus() != UserStatus.ACTIVE) {

            throw new UserSuspendedException();
        }

        if (!passwordHasher.matches(
                password,
                user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = tokenGenerator.generate(user);

        return new AuthenticationResult(
                user,
                token);
    }
}