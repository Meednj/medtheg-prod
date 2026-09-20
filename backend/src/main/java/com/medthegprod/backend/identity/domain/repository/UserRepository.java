package com.medthegprod.backend.identity.domain.repository;

import com.medthegprod.backend.identity.domain.model.User;
import com.medthegprod.backend.identity.domain.model.UserId;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId userId);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}