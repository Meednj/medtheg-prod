package com.medthegprod.backend.identity.infrastructure.web.mapper;

import com.medthegprod.backend.identity.domain.model.User;
import com.medthegprod.backend.identity.infrastructure.web.dto.UserResponse;

public final class UserWebMapper {

    private UserWebMapper() {
    }

    public static UserResponse toResponse(User user) {

        return new UserResponse(
                user.getId().value(),
                user.getEmail(),
                user.getRole(),
                user.getStatus());
    }
}