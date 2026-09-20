package com.medthegprod.backend.identity.application.service;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String email) {
        super("User already exists: " + email);
    }
}