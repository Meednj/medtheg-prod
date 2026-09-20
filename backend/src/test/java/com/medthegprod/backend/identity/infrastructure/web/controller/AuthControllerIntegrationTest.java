package com.medthegprod.backend.identity.infrastructure.web.controller;

import com.medthegprod.backend.identity.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("medtheg_prod")
            .withUsername("medtheg")
            .withPassword("medtheg_dev_password");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void cleanDatabase() {
        userJpaRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        	"email": "Alice@Example.com",
                        	"password": "plain-password"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());

        assert userJpaRepository.findByEmail("alice@example.com").isPresent();
        assert userJpaRepository.findByEmail("alice@example.com")
                .orElseThrow()
                .getPasswordHash()
                .startsWith("$2");
    }

    @Test
    void shouldRejectInvalidEmail() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        	"email": "not-an-email",
                        	"password": "plain-password"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectShortPassword() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        	"email": "alice@example.com",
                        	"password": "short"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {
        registerUser("alice@example.com", "plain-password");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        	"email": "ALICE@EXAMPLE.COM",
                        	"password": "another-password"
                        }
                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("USER_ALREADY_EXISTS"));
    }

    private void registerUser(String email, String password) throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        	"email": "%s",
                        	"password": "%s"
                        }
                        """.formatted(email, password)))
                .andExpect(status().isCreated());
    }
}
