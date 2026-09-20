package com.medthegprod.backend.identity.infrastructure.web.controller;

import com.medthegprod.backend.identity.application.usecase.AuthenticateUserUseCase;
import com.medthegprod.backend.identity.application.usecase.AuthenticationResult;
import com.medthegprod.backend.identity.application.usecase.RegisterUserUseCase;
import com.medthegprod.backend.identity.domain.model.User;
import com.medthegprod.backend.identity.infrastructure.web.dto.AuthResponse;
import com.medthegprod.backend.identity.infrastructure.web.dto.LoginRequest;
import com.medthegprod.backend.identity.infrastructure.web.dto.RegisterRequest;
import com.medthegprod.backend.identity.infrastructure.web.dto.UserResponse;
import com.medthegprod.backend.identity.infrastructure.web.mapper.UserWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(
            RegisterUserUseCase registerUserUseCase,
            AuthenticateUserUseCase authenticateUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(
            @Valid @RequestBody RegisterRequest request) {

        User user = registerUserUseCase.execute(
                request.email(),
                request.password());

        return UserWebMapper.toResponse(user);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request) {

        AuthenticationResult result = authenticateUserUseCase.authenticate(
                request.email(),
                request.password());

        User user = result.user();

        return new AuthResponse(
                user.getId().value(),
                user.getEmail(),
                user.getRole(),
                result.accessToken());
    }
}