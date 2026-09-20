package com.medthegprod.backend.identity.domain.model;

import java.util.Objects;

public class User {

    private final UserId id;
    private final String email;
    private final String passwordHash;
    private final Role role;
    private UserStatus status;

    public User(
            UserId id,
            String email,
            String passwordHash,
            Role role,
            UserStatus status) {
        this.id = Objects.requireNonNull(id, "User ID cannot be null");
        this.email = normalizeEmail(email);

        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException(
                    "Password hash cannot be blank");
        }

        this.passwordHash = passwordHash;
        this.role = Objects.requireNonNull(role, "Role cannot be null");
        this.status = Objects.requireNonNull(
                status,
                "User status cannot be null");
    }

    private static String normalizeEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email cannot be blank");
        }

        return email.trim().toLowerCase();
    }

    public UserId getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void suspend() {
        status = UserStatus.SUSPENDED;
    }

    public void activate() {
        status = UserStatus.ACTIVE;
    }
}