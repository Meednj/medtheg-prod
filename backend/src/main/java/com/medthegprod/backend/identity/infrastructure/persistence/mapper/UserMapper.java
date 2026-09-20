package com.medthegprod.backend.identity.infrastructure.persistence.mapper;

import com.medthegprod.backend.identity.domain.model.User;
import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.identity.infrastructure.persistence.entity.UserEntity;

import java.time.OffsetDateTime;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserEntity toEntity(User user) {

        OffsetDateTime now = OffsetDateTime.now();

        return new UserEntity(
                user.getId().value(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole(),
                user.getStatus(),
                now,
                now);
    }

    public static User toDomain(UserEntity entity) {

        return new User(
                new UserId(entity.getId()),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRole(),
                entity.getStatus());
    }
}