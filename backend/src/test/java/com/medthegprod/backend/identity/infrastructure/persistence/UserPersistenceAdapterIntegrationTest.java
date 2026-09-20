package com.medthegprod.backend.identity.infrastructure.persistence;

import com.medthegprod.backend.identity.domain.model.Role;
import com.medthegprod.backend.identity.domain.model.User;
import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.identity.domain.model.UserStatus;
import com.medthegprod.backend.identity.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class UserPersistenceAdapterIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("medtheg_prod")
            .withUsername("medtheg")
            .withPassword("medtheg_dev_password");

    @DynamicPropertySource
    static void configureDatabase(
            DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl);

        registry.add(
                "spring.datasource.username",
                postgres::getUsername);

        registry.add(
                "spring.datasource.password",
                postgres::getPassword);
    }

    @Autowired
    private UserPersistenceAdapter userPersistenceAdapter;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void cleanDatabase() {
        userJpaRepository.deleteAll();
    }

    @Test
    void shouldPersistAndRetrieveUser() {

        User user = new User(
                UserId.generate(),
                "test@example.com",
                "$2a$10$hashed-password",
                Role.CUSTOMER,
                UserStatus.ACTIVE);

        userPersistenceAdapter.save(user);

        var result = userPersistenceAdapter.findById(user.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getEmail())
                .isEqualTo("test@example.com");
        assertThat(result.get().getRole())
                .isEqualTo(Role.CUSTOMER);
        assertThat(result.get().getStatus())
                .isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void shouldFindUserByEmail() {

        User user = new User(
                UserId.generate(),
                "customer@example.com",
                "$2a$10$hashed-password",
                Role.CUSTOMER,
                UserStatus.ACTIVE);

        userPersistenceAdapter.save(user);

        var result = userPersistenceAdapter
                .findByEmail("CUSTOMER@EXAMPLE.COM");

        assertThat(result).isPresent();
        assertThat(result.get().getId())
                .isEqualTo(user.getId());
    }

    @Test
    void shouldCheckIfEmailExists() {

        User user = new User(
                UserId.generate(),
                "existing@example.com",
                "$2a$10$hashed-password",
                Role.CUSTOMER,
                UserStatus.ACTIVE);

        userPersistenceAdapter.save(user);

        assertThat(
                userPersistenceAdapter
                        .existsByEmail("existing@example.com"))
                .isTrue();

        assertThat(
                userPersistenceAdapter
                        .existsByEmail("unknown@example.com"))
                .isFalse();
    }
}