package com.medthegprod.backend.identity.infrastructure.web.controller;

import com.medthegprod.backend.identity.application.service.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class IdentityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleUserAlreadyExists(
            UserAlreadyExistsException exception) {
        return Map.of(
                "error", "USER_ALREADY_EXISTS",
                "message", exception.getMessage());
    }
}