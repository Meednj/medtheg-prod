package com.medthegprod.backend.catalog.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medthegprod.backend.catalog.infrastructure.persistence.repository.ProductJpaRepository;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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

        @Autowired
        private ProductJpaRepository productJpaRepository;

        @BeforeEach
        void cleanDatabase() {
                productJpaRepository.deleteAll();
        }

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
                                .andExpect(jsonPath("$.title")
                                                .value("Dark Trap Beat"))
                                .andExpect(jsonPath("$.description")
                                                .value("Dark trap instrumental"))
                                .andExpect(jsonPath("$.type")
                                                .value("BEAT"))
                                .andExpect(jsonPath("$.price")
                                                .value(19.99))
                                .andExpect(jsonPath("$.currency")
                                                .value("EUR"))
                                .andExpect(jsonPath("$.status")
                                                .value("DRAFT"));
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
                                .andExpect(jsonPath("$.title")
                                                .value("Dark Trap Beat"))
                                .andExpect(jsonPath("$.type")
                                                .value("BEAT"))
                                .andExpect(jsonPath("$.price")
                                                .value(19.99))
                                .andExpect(jsonPath("$.status")
                                                .value("DRAFT"));
        }

        @Test
        void shouldReturn404WhenProductDoesNotExist() throws Exception {

                UUID unknownProductId = UUID.randomUUID();

                mockMvc.perform(
                                get("/api/products/" + unknownProductId))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error")
                                                .value("PRODUCT_NOT_FOUND"));
        }

        @Test
        void shouldListProductsWithPagination() throws Exception {

                String firstProduct = """
                                {
                                    "title": "Beat One",
                                    "description": "First beat",
                                    "type": "BEAT",
                                    "price": 19.99
                                }
                                """;

                String secondProduct = """
                                {
                                    "title": "Beat Two",
                                    "description": "Second beat",
                                    "type": "BEAT",
                                    "price": 24.99
                                }
                                """;

                mockMvc.perform(
                                post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(firstProduct))
                                .andExpect(status().isCreated());

                mockMvc.perform(
                                post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(secondProduct))
                                .andExpect(status().isCreated());

                mockMvc.perform(
                                get("/api/products?page=0&size=1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.content.length()").value(1))
                                .andExpect(jsonPath("$.page").value(0))
                                .andExpect(jsonPath("$.size").value(1))
                                .andExpect(jsonPath("$.totalElements").value(2))
                                .andExpect(jsonPath("$.totalPages").value(2));
        }

        @Test
        void shouldRejectNegativePage() throws Exception {

                mockMvc.perform(
                                get("/api/products?page=-1&size=12"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error")
                                                .value("INVALID_REQUEST"));
        }

        @Test
        void shouldRejectInvalidPageSize() throws Exception {

                mockMvc.perform(
                                get("/api/products?page=0&size=0"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error")
                                                .value("INVALID_REQUEST"));
        }

        @Test
        void shouldSortProductsByPriceAscending() throws Exception {

                createProduct(
                                "Expensive Beat",
                                "BEAT",
                                30.00,
                                "BEATS");

                createProduct(
                                "Cheap Beat",
                                "BEAT",
                                10.00,
                                "BEATS");

                mockMvc.perform(
                                get("/api/products?sortBy=price&direction=asc"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(2))
                                .andExpect(jsonPath("$.content[0].title")
                                                .value("Cheap Beat"))
                                .andExpect(jsonPath("$.content[1].title")
                                                .value("Expensive Beat"));
        }

        @Test
        void shouldCreateProductWithCategory() throws Exception {

                String requestBody = """
                                {
                                    "title": "Dark Trap Beat",
                                    "description": "Dark trap instrumental",
                                    "type": "BEAT",
                                    "price": 19.99,
                                    "categories": ["BEATS"]
                                }
                                """;

                mockMvc.perform(
                                post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody))
                                .andExpect(status().isCreated());
        }

        @Test
        void shouldFilterProductsByCategory() throws Exception {

                createProduct(
                                "Dark Trap Beat",
                                "BEAT",
                                19.99,
                                "BEATS");

                createProduct(
                                "Melodic Beat",
                                "BEAT",
                                24.99,
                                "BEATS");

                createProduct(
                                "Java Course",
                                "COURSE",
                                49.99,
                                "COURSES");

                mockMvc.perform(
                                get("/api/products?category=BEATS"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(2))
                                .andExpect(jsonPath("$.content[0].title")
                                                .value("Melodic Beat"))
                                .andExpect(jsonPath("$.content[1].title")
                                                .value("Dark Trap Beat"));
        }

        @Test
        void shouldFilterProductsByTypeAndCategory() throws Exception {

                createProduct(
                                "Dark Trap Beat",
                                "BEAT",
                                19.99,
                                "BEATS");

                createProduct(
                                "Java Course",
                                "COURSE",
                                49.99,
                                "COURSES");

                createProduct(
                                "Trap Kit",
                                "DRUM_KIT",
                                29.99,
                                "KITS");

                mockMvc.perform(
                                get("/api/products?type=BEAT&category=BEATS"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1))
                                .andExpect(jsonPath("$.content[0].title")
                                                .value("Dark Trap Beat"))
                                .andExpect(jsonPath("$.content[0].type")
                                                .value("BEAT"));
        }

        private void createProduct(
                        String title,
                        String type,
                        double price,
                        String category) throws Exception {

                String requestBody = """
                                {
                                    "title": "%s",
                                    "description": "Test product",
                                    "type": "%s",
                                    "price": %s,
                                    "categories": ["%s"]
                                }
                                """.formatted(
                                title,
                                type,
                                price,
                                category);

                mockMvc.perform(
                                post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody))
                                .andExpect(status().isCreated());
        }

        @Test
        void shouldSearchProductsByTitle() throws Exception {
                createProduct(
                                "Dark Trap Beat",
                                "BEAT",
                                19.99,
                                "BEATS");

                createProduct(
                                "Melodic Piano Beat",
                                "BEAT",
                                24.99,
                                "BEATS");

                mockMvc.perform(
                                get("/api/products?search=dark"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1))
                                .andExpect(jsonPath("$.content[0].title")
                                                .value("Dark Trap Beat"));
        }

        @Test
        void shouldSearchProductsByDescription() throws Exception {
                String firstProduct = """
                                {
                                    "title": "Trap Beat",
                                    "description": "Dark cinematic atmosphere",
                                    "type": "BEAT",
                                    "price": 19.99,
                                    "categories": ["BEATS"]
                                }
                                """;

                String secondProduct = """
                                {
                                    "title": "Piano Beat",
                                    "description": "Smooth melodic atmosphere",
                                    "type": "BEAT",
                                    "price": 24.99,
                                    "categories": ["BEATS"]
                                }
                                """;

                mockMvc.perform(
                                post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(firstProduct))
                                .andExpect(status().isCreated());

                mockMvc.perform(
                                post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(secondProduct))
                                .andExpect(status().isCreated());

                mockMvc.perform(
                                get("/api/products?search=cinematic"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1))
                                .andExpect(jsonPath("$.content[0].title")
                                                .value("Trap Beat"));
        }

        @Test
        void shouldSearchWithTypeAndCategoryFilters() throws Exception {
                createProduct(
                                "Dark Trap Beat",
                                "BEAT",
                                19.99,
                                "BEATS");

                createProduct(
                                "Dark Java Course",
                                "COURSE",
                                49.99,
                                "COURSES");

                createProduct(
                                "Melodic Beat",
                                "BEAT",
                                24.99,
                                "BEATS");

                mockMvc.perform(
                                get("/api/products?search=dark&type=BEAT&category=BEATS"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1))
                                .andExpect(jsonPath("$.content[0].title")
                                                .value("Dark Trap Beat"));
        }

        @Test
        void shouldSearchCaseInsensitively() throws Exception {
                createProduct(
                                "Dark Trap Beat",
                                "BEAT",
                                19.99,
                                "BEATS");

                mockMvc.perform(
                                get("/api/products?search=DARK"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1));
        }

        @Test
        void shouldUpdateProduct() throws Exception {
                String createRequest = """
                                {
                                    "title": "Old Beat",
                                    "description": "Old description",
                                    "type": "BEAT",
                                    "price": 19.99,
                                    "categories": ["BEATS"]
                                }
                                """;

                String response = mockMvc.perform(
                                post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(createRequest))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                ObjectMapper objectMapper = new ObjectMapper();

                String productId = objectMapper
                                .readTree(response)
                                .get("id")
                                .asText();

                String updateRequest = """
                                {
                                    "title": "New Beat",
                                    "description": "Updated description",
                                    "type": "BEAT",
                                    "price": 29.99,
                                    "categories": ["BEATS"]
                                }
                                """;

                mockMvc.perform(
                                put("/api/products/" + productId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(updateRequest))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(productId))
                                .andExpect(jsonPath("$.title").value("New Beat"))
                                .andExpect(jsonPath("$.description")
                                                .value("Updated description"))
                                .andExpect(jsonPath("$.price").value(29.99))
                                .andExpect(jsonPath("$.status").value("DRAFT"));
        }

        @Test
        void shouldReturn404WhenUpdatingUnknownProduct() throws Exception {
                String requestBody = """
                                {
                                    "title": "Updated Beat",
                                    "description": "Updated",
                                    "type": "BEAT",
                                    "price": 29.99,
                                    "categories": ["BEATS"]
                                }
                                """;

                mockMvc.perform(
                                put("/api/products/" + UUID.randomUUID())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error")
                                                .value("PRODUCT_NOT_FOUND"));
        }

        @Test
        void shouldPublishProduct() throws Exception {
                String createRequest = """
                                {
                                    "title": "Dark Trap Beat",
                                    "description": "Dark trap instrumental",
                                    "type": "BEAT",
                                    "price": 19.99,
                                    "categories": ["BEATS"]
                                }
                                """;

                String response = mockMvc.perform(
                                post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(createRequest))
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
                                post("/api/products/" + productId + "/publish"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(productId))
                                .andExpect(jsonPath("$.title").value("Dark Trap Beat"))
                                .andExpect(jsonPath("$.type").value("BEAT"))
                                .andExpect(jsonPath("$.price").value(19.99))
                                .andExpect(jsonPath("$.status").value("PUBLISHED"));
        }

        @Test
        void shouldRejectPublishingProductWithoutCategory() throws Exception {
                String createRequest = """
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
                                                .content(createRequest))
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
                                post("/api/products/" + productId + "/publish"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("INVALID_PRODUCT_STATE"));
        }

        @Test
        void shouldRejectPublishingAlreadyPublishedProduct() throws Exception {
                String createRequest = """
                                {
                                    "title": "Dark Trap Beat",
                                    "description": "Dark trap instrumental",
                                    "type": "BEAT",
                                    "price": 19.99,
                                    "categories": ["BEATS"]
                                }
                                """;

                String response = mockMvc.perform(
                                post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(createRequest))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                ObjectMapper objectMapper = new ObjectMapper();

                String productId = objectMapper
                                .readTree(response)
                                .get("id")
                                .asText();

                // First publication succeeds
                mockMvc.perform(
                                post("/api/products/" + productId + "/publish"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("PUBLISHED"));

                // Second publication is rejected
                mockMvc.perform(
                                post("/api/products/" + productId + "/publish"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("INVALID_PRODUCT_STATE"));
        }

        @Test
        void shouldReturn404WhenPublishingUnknownProduct() throws Exception {
                UUID unknownProductId = UUID.randomUUID();

                mockMvc.perform(
                                post("/api/products/" + unknownProductId + "/publish"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error")
                                                .value("PRODUCT_NOT_FOUND"));
        }

}