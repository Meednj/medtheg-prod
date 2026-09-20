package com.medthegprod.backend.identity.infrastructure.persistence;

import com.medthegprod.backend.identity.domain.model.User;
import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.identity.domain.repository.UserRepository;
import com.medthegprod.backend.identity.infrastructure.persistence.entity.UserEntity;
import com.medthegprod.backend.identity.infrastructure.persistence.mapper.UserMapper;
import com.medthegprod.backend.identity.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class UserPersistenceAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    @Transactional
    public User save(User user) {

        UserEntity entity = UserMapper.toEntity(user);

        UserEntity savedEntity = userJpaRepository.save(entity);

        return UserMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UserId userId) {

        return userJpaRepository
                .findById(userId.value())
                .map(UserMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {

        return userJpaRepository
                .findByEmail(email.trim().toLowerCase())
                .map(UserMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {

        return userJpaRepository.existsByEmail(
                email.trim().toLowerCase());
    }
}