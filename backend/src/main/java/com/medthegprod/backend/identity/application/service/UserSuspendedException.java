package com.medthegprod.backend.identity.application.service;

public class UserSuspendedException extends RuntimeException {

    public UserSuspendedException() {
        super("User account is suspended");
    }
}