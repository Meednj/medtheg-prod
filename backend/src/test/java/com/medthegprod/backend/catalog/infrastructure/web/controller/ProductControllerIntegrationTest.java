package com.medthegprod.backend.catalog.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

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
        private MockMvc mockMvc;



        @Test
        void shouldCreateProduct() throws Exception {

                String requestBody = """
                                {
                                    "title": "Dark Trap Beat",
                                    "description": "Dark trap instrumental",
                                    "type": "BEAT",
                                    "price": 19.99
                                }
                                """;

                mockMvc.perform(
                                post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.title").value("Dark Trap Beat"))
                                .andExpect(jsonPath("$.description").value("Dark trap instrumental"))
                                .andExpect(jsonPath("$.type").value("BEAT"))
                                .andExpect(jsonPath("$.price").value(19.99))
                                .andExpect(jsonPath("$.currency").value("EUR"))
                                .andExpect(jsonPath("$.status").value("DRAFT"));
        }

        @Test
        void shouldRejectProductWithBlankTitle() throws Exception {

                String requestBody = """
                                {
                                    "title": "",
                                    "description": "Dark trap instrumental",
                                    "type": "BEAT",
                                    "price": 19.99
                                }
                                """;

                mockMvc.perform(
                                post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void shouldRejectProductWithNegativePrice() throws Exception {

                String requestBody = """
                                {
                                    "title": "Dark Trap Beat",
                                    "description": "Dark trap instrumental",
                                    "type": "BEAT",
                                    "price": -10
                                }
                                """;

                mockMvc.perform(
                                post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void shouldGetProductById() throws Exception {

                String requestBody = """
                                {
                                    "title": "Dark Trap Beat",
                                    "description": "Dark trap instrumental",
                                    "type": "BEAT",
                                    "price": 19.99
                                }
                                """;

                String response = mockMvc.perform(
                                post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                ObjectMapper objectMapper = new ObjectMapper();

                String productId = objectMapper
                                .readTree(response)
                                .get("id")
                                .asText();

                mockMvc.perform(
                                get("/api/products/" + productId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(productId))
                                .andExpect(jsonPath("$.title").value("Dark Trap Beat"))
                                .andExpect(jsonPath("$.type").value("BEAT"))
                                .andExpect(jsonPath("$.price").value(19.99))
                                .andExpect(jsonPath("$.status").value("DRAFT"));
        }

        @Test
        void shouldReturn404WhenProductDoesNotExist() throws Exception {

                UUID unknownProductId = UUID.randomUUID();

                mockMvc.perform(
                                get("/api/products/" + unknownProductId))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("PRODUCT_NOT_FOUND"));
        }
}