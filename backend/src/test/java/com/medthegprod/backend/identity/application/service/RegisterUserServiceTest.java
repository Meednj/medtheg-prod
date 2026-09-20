package com.medthegprod.backend.identity.application.service;

import com.medthegprod.backend.identity.application.port.PasswordHasher;
import com.medthegprod.backend.identity.domain.model.Role;
import com.medthegprod.backend.identity.domain.model.User;
import com.medthegprod.backend.identity.domain.model.UserStatus;
import com.medthegprod.backend.identity.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegisterUserServiceTest {

    private UserRepository userRepository;
    private PasswordHasher passwordHasher;
    private RegisterUserService registerUserService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordHasher = mock(PasswordHasher.class);
        registerUserService = new RegisterUserService(
                userRepository,
                passwordHasher);
    }

    @Test
    void shouldNormalizeHashAndSaveUser() {
        when(userRepository.existsByEmail("alice@example.com"))
                .thenReturn(false);
        when(passwordHasher.hash("plain-password"))
                .thenReturn("hashed-password");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = registerUserService.execute(
                "  Alice@Example.com ",
                "plain-password");

        assertEquals("alice@example.com", result.getEmail());
        assertEquals("hashed-password", result.getPasswordHash());
        assertEquals(Role.CUSTOMER, result.getRole());
        assertEquals(UserStatus.ACTIVE, result.getStatus());
        verify(userRepository).existsByEmail("alice@example.com");
        verify(passwordHasher).hash("plain-password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldRejectDuplicateEmailBeforeHashingOrSaving() {
        when(userRepository.existsByEmail("alice@example.com"))
                .thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> registerUserService.execute(
                        " Alice@Example.com ",
                        "plain-password"));

        assertTrue(exception.getMessage().contains("alice@example.com"));
        verify(passwordHasher, never()).hash(any(String.class));
        verify(userRepository, never()).save(any(User.class));
    }
}
