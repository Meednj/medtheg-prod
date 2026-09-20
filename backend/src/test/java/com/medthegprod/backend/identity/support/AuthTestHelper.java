package com.medthegprod.backend.identity.support;

import com.medthegprod.backend.identity.application.port.TokenGenerator;
import com.medthegprod.backend.identity.domain.model.Role;
import com.medthegprod.backend.identity.domain.model.User;
import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.identity.domain.model.UserStatus;

public class AuthTestHelper {

    public static User adminUser() {
        return new User(
                UserId.generate(),
                "admin@test.com",
                "$2a$10$dummy",
                Role.ADMIN,
                UserStatus.ACTIVE);
    }

    public static String adminToken(TokenGenerator tokenGenerator) {
        return tokenGenerator.generate(adminUser());
    }
}