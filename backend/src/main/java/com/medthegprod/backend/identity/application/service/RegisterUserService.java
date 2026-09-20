package com.medthegprod.backend.identity.application.service;

import com.medthegprod.backend.identity.application.port.PasswordHasher;
import com.medthegprod.backend.identity.application.usecase.RegisterUserUseCase;
import com.medthegprod.backend.identity.domain.model.Role;
import com.medthegprod.backend.identity.domain.model.User;
import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.identity.domain.model.UserStatus;
import com.medthegprod.backend.identity.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterUserService(
            UserRepository userRepository,
            PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    @Transactional
    public User execute(
            String email,
            String password) {
        String normalizedEmail = email.trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException(normalizedEmail);
        }

        String passwordHash = passwordHasher.hash(password);

        User user = new User(
                UserId.generate(),
                normalizedEmail,
                passwordHash,
                Role.CUSTOMER,
                UserStatus.ACTIVE);

        return userRepository.save(user);
    }
}