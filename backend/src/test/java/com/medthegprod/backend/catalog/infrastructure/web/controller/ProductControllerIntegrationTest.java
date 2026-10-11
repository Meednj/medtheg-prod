package com.medthegprod.backend.catalog.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medthegprod.backend.library.application.port.AssetStorage;
import com.medthegprod.backend.catalog.infrastructure.persistence.repository.ProductJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Supplier;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        @Autowired
        private JwtEncoder jwtEncoder;

        @MockitoBean
        private AssetStorage assetStorage;

        private String adminToken;
        private String customerToken;

        @BeforeEach
        void setUp() {
                productJpaRepository.deleteAll();

                adminToken = tokenForRole("ADMIN");
                customerToken = tokenForRole("CUSTOMER");
        }

        @Test
        void shouldRejectUnauthenticatedProductCreation() throws Exception {
                mockMvc.perform(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                    "title": "Dark Trap Beat",
                                                    "description": "Dark trap instrumental",
                                                    "type": "BEAT",
                                                    "price": 19.99
                                                }
                                                """))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void shouldRejectCustomerProductCreation() throws Exception {
                mockMvc.perform(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + customerToken)
                                .content("""
                                                {
                                                    "title": "Dark Trap Beat",
                                                    "description": "Dark trap instrumental",
                                                    "type": "BEAT",
                                                    "price": 19.99
                                                }
                                                """))
                                .andExpect(status().isForbidden());
        }

        @Test
        void shouldRestrictAdminProductAndAssetOperationsToAdmins() throws Exception {
                UUID draftProductId = createDraftProduct("Authorization draft");
                UUID publishedProductId = createDraftProduct("Authorization published");
                mockMvc.perform(authenticated(post("/api/products/{id}/publish", publishedProductId)))
                                .andExpect(status().isOk());

                UUID assetProductId = createDraftProduct("Authorization asset");
                UUID assetId = addTestAsset(assetProductId);

                String createRequest = """
                                {
                                    "title": "Created by admin",
                                    "description": "Authorization matrix",
                                    "type": "BEAT",
                                    "price": 19.99,
                                    "categories": ["BEATS"]
                                }
                                """;
                String updateRequest = """
                                {
                                    "title": "Updated by admin",
                                    "description": "Authorization matrix",
                                    "type": "BEAT",
                                    "price": 29.99,
                                    "categories": ["BEATS"]
                                }
                                """;

                assertAdminAuthorization(
                                () -> post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(createRequest),
                                HttpStatus.CREATED);
                assertAdminAuthorization(
                                () -> get("/api/products/admin/{id}", draftProductId),
                                HttpStatus.OK);
                assertAdminAuthorization(
                                () -> put("/api/products/{id}", draftProductId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(updateRequest),
                                HttpStatus.OK);
                assertAdminAuthorization(
                                () -> post("/api/products/{id}/publish", draftProductId),
                                HttpStatus.OK);
                assertAdminAuthorization(
                                () -> post("/api/products/{id}/archive", publishedProductId),
                                HttpStatus.OK);
                assertAdminAuthorization(
                                () -> delete("/api/products/{productId}/assets/{assetId}", assetProductId, assetId),
                                HttpStatus.OK);
        }

        private void assertAdminAuthorization(
                        Supplier<MockHttpServletRequestBuilder> request,
                        HttpStatus adminStatus) throws Exception {
                mockMvc.perform(request.get())
                                .andExpect(status().isUnauthorized());
                mockMvc.perform(request.get().header("Authorization", "Bearer " + customerToken))
                                .andExpect(status().isForbidden());
                mockMvc.perform(authenticated(request.get()))
                                .andExpect(status().is(adminStatus.value()));
        }

        private UUID createDraftProduct(String title) throws Exception {
                String requestBody = """
                                {
                                    "title": "%s",
                                    "description": "Authorization test fixture",
                                    "type": "BEAT",
                                    "price": 19.99,
                                    "categories": ["BEATS"]
                                }
                                """.formatted(title);
                String response = mockMvc.perform(authenticated(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();
                return UUID.fromString(new ObjectMapper().readTree(response).get("id").asText());
        }

        private UUID addTestAsset(UUID productId) throws Exception {
                String requestBody = """
                                {
                                    "type": "AUDIO_WAV",
                                    "storageKey": "authorization-tests/%s.wav"
                                }
                                """.formatted(UUID.randomUUID());
                String response = mockMvc.perform(authenticated(post("/api/products/{productId}/assets", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();
                return UUID.fromString(new ObjectMapper().readTree(response).get("assets").get(0).get("id").asText());
        }

        @Test
        void shouldRestrictAssetRegistrationToAdminsAndPersistTheAsset() throws Exception {
                UUID productId = createDraftProduct("Asset registration authorization");
                String requestBody = """
                                {
                                    "type": "MIDI",
                                    "storageKey": "authorization-tests/%s.mid"
                                }
                                """.formatted(UUID.randomUUID());

                assertAdminAuthorization(
                                () -> post("/api/products/{productId}/assets", productId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody),
                                HttpStatus.OK);

                mockMvc.perform(get("/api/products/admin/{productId}", productId)
                                .header("Authorization", "Bearer " + adminToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.assets.length()").value(1))
                                .andExpect(jsonPath("$.assets[0].type").value("MIDI"));
        }

        @Test
        void shouldKeepProductListingPublic() throws Exception {
                mockMvc.perform(get("/api/products"))
                                .andExpect(status().isOk());
        }

        @Test
        void shouldListAllProductStatesOnlyForAdminsWithFiltersAndPagination() throws Exception {
                createDraftProduct("Desk draft Alpha");
                UUID secondDraftId = createDraftProduct("Desk draft Zeta");
                String samplePackJson = mockMvc.perform(authenticated(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {"title":"Desk sample pack","description":"Catalog filter","type":"SAMPLE_PACK","price":12.00,"categories":["KITS"]}
                                                """)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();
                UUID samplePackId = UUID.fromString(new ObjectMapper().readTree(samplePackJson).get("id").asText());
                UUID publishedId = createDraftProduct("Desk published Beta");
                mockMvc.perform(authenticated(post("/api/products/{id}/publish", publishedId)))
                                .andExpect(status().isOk());
                UUID archivedId = createDraftProduct("Desk archived Gamma");
                mockMvc.perform(authenticated(post("/api/products/{id}/publish", archivedId)))
                                .andExpect(status().isOk());
                mockMvc.perform(authenticated(post("/api/products/{id}/archive", archivedId)))
                                .andExpect(status().isOk());

                mockMvc.perform(get("/api/products/admin"))
                                .andExpect(status().isUnauthorized());
                mockMvc.perform(get("/api/products/admin")
                                .header("Authorization", "Bearer " + customerToken))
                                .andExpect(status().isForbidden());
                mockMvc.perform(authenticated(get("/api/products/admin")
                                .param("status", "DRAFT")
                                .param("type", "BEAT")
                                .param("search", "desk")
                                .param("sortBy", "title")
                                .param("direction", "asc")
                                .param("page", "0")
                                .param("size", "1")))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1))
                                .andExpect(jsonPath("$.content[0].status").value("DRAFT"))
                                .andExpect(jsonPath("$.totalElements").value(2))
                                .andExpect(jsonPath("$.totalPages").value(2));
                mockMvc.perform(authenticated(get("/api/products/admin")
                                .param("status", "DRAFT")
                                .param("type", "BEAT")
                                .param("search", "desk")
                                .param("sortBy", "title")
                                .param("direction", "asc")
                                .param("page", "1")
                                .param("size", "1")))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1))
                                .andExpect(jsonPath("$.content[0].id").value(secondDraftId.toString()));
                mockMvc.perform(authenticated(get("/api/products/admin")
                                .param("status", "DRAFT")
                                .param("type", "SAMPLE_PACK")
                                .param("search", "desk")))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1))
                                .andExpect(jsonPath("$.content[0].id").value(samplePackId.toString()));
                mockMvc.perform(authenticated(get("/api/products/admin")
                                .param("status", "ARCHIVED")
                                .param("type", "BEAT")))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id").value(archivedId.toString()));
                mockMvc.perform(authenticated(get("/api/products/admin")
                                .param("status", "PUBLISHED")))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id").value(publishedId.toString()));
                mockMvc.perform(get("/api/products").param("search", "Desk"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1))
                                .andExpect(jsonPath("$.content[0].id").value(publishedId.toString()));
        }

        @Test
        void shouldAllowAdminsToPreviewOnlyImageAndAudioPreviewAssets() throws Exception {
                UUID productId = createDraftProduct("Admin preview draft");
                String assetResponse = mockMvc.perform(authenticated(post("/api/products/{productId}/assets", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {"type":"AUDIO_PREVIEW","storageKey":"test/admin-preview.mp3"}
                                                """)))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();
                UUID assetId = UUID.fromString(new ObjectMapper().readTree(assetResponse)
                                .get("assets").get(0).get("id").asText());
                when(assetStorage.generateDownloadUrl(anyString(), any())).thenReturn("https://storage.example/temporary");

                mockMvc.perform(get("/api/products/admin/{productId}/assets/{assetId}/preview", productId, assetId)
                                .param("type", "AUDIO_PREVIEW"))
                                .andExpect(status().isUnauthorized());
                mockMvc.perform(get("/api/products/admin/{productId}/assets/{assetId}/preview", productId, assetId)
                                .header("Authorization", "Bearer " + customerToken)
                                .param("type", "AUDIO_PREVIEW"))
                                .andExpect(status().isForbidden());
                mockMvc.perform(authenticated(get("/api/products/admin/{productId}/assets/{assetId}/preview", productId, assetId)
                                .param("type", "AUDIO_PREVIEW")))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.url").value("https://storage.example/temporary"));
                mockMvc.perform(authenticated(get("/api/products/admin/{productId}/assets/{assetId}/preview", productId, assetId)
                                .param("type", "AUDIO_MP3")))
                                .andExpect(status().isBadRequest());
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
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody)))
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
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody)))
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
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void shouldHideDraftFromPublicAndAllowAdminRead() throws Exception {

                String requestBody = """
                                {
                                    "title": "Dark Trap Beat",
                                    "description": "Dark trap instrumental",
                                    "type": "BEAT",
                                    "price": 19.99
                                }
                                """;

                String response = mockMvc.perform(
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody)))
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
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error")
                                                .value("PRODUCT_NOT_FOUND"));

                mockMvc.perform(
                                get("/api/products/admin/" + productId)
                                                .header("Authorization", "Bearer " + adminToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(productId))
                                .andExpect(jsonPath("$.status").value("DRAFT"));
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
                createProduct("Beat One", "BEAT", 19.99, "BEATS");
                createProduct("Beat Two", "BEAT", 24.99, "BEATS");

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
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody)))
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

                String response = mockMvc.perform(
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody)))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                String productId = new ObjectMapper()
                                .readTree(response)
                                .get("id")
                                .asText();

                mockMvc.perform(
                                authenticated(post("/api/products/" + productId + "/publish")))
                                .andExpect(status().isOk());
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
                createProductWithDescription(
                                "Trap Beat",
                                "Dark cinematic atmosphere",
                                19.99,
                                "BEATS");
                createProductWithDescription(
                                "Piano Beat",
                                "Smooth melodic atmosphere",
                                24.99,
                                "BEATS");

                mockMvc.perform(
                                get("/api/products?search=cinematic"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1))
                                .andExpect(jsonPath("$.content[0].title")
                                                .value("Trap Beat"));
        }

        private void createProductWithDescription(
                        String title,
                        String description,
                        double price,
                        String category) throws Exception {
                String requestBody = """
                                {
                                    "title": "%s",
                                    "description": "%s",
                                    "type": "BEAT",
                                    "price": %s,
                                    "categories": ["%s"]
                                }
                                """.formatted(title, description, price, category);

                String response = mockMvc.perform(
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody)))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                String productId = new ObjectMapper()
                                .readTree(response)
                                .get("id")
                                .asText();

                mockMvc.perform(
                                authenticated(post("/api/products/" + productId + "/publish")))
                                .andExpect(status().isOk());
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
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(createRequest)))
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
                                authenticated(put("/api/products/" + productId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(updateRequest)))
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
                                authenticated(put("/api/products/" + UUID.randomUUID())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody)))
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
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(createRequest)))
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
                                authenticated(post("/api/products/" + productId + "/publish")))
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
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(createRequest)))
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
                                authenticated(post("/api/products/" + productId + "/publish")))
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
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(createRequest)))
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
                                authenticated(post("/api/products/" + productId + "/publish")))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("PUBLISHED"));

                // Second publication is rejected
                mockMvc.perform(
                                authenticated(post("/api/products/" + productId + "/publish")))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("INVALID_PRODUCT_STATE"));
        }

        @Test
        void shouldReturn404WhenPublishingUnknownProduct() throws Exception {
                UUID unknownProductId = UUID.randomUUID();

                mockMvc.perform(
                                authenticated(post("/api/products/" + unknownProductId + "/publish")))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error")
                                                .value("PRODUCT_NOT_FOUND"));
        }

        @Test
        void shouldAddAssetToProduct() throws Exception {

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
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(createRequest)))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                ObjectMapper objectMapper = new ObjectMapper();

                String productId = objectMapper
                                .readTree(response)
                                .get("id")
                                .asText();

                String storageKey = "products/" + productId + "/full.wav";

                String assetRequest = """
                                {
                                    "type": "AUDIO_WAV",
                                    "storageKey": "%s"
                                }
                                """.formatted(storageKey);

                mockMvc.perform(
                                authenticated(post("/api/products/" + productId + "/assets")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(assetRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(productId))
                                .andExpect(jsonPath("$.assets").isArray())
                                .andExpect(jsonPath("$.assets.length()").value(1))
                                .andExpect(jsonPath("$.assets[0].type")
                                                .value("AUDIO_WAV"))
                                .andExpect(jsonPath("$.assets[0].storageKey")
                                                .doesNotExist());
        }

        @Test
        void shouldReturnSafeUploadMetadataWithoutStorageKey() throws Exception {
                String createRequest = """
                                {
                                    "title": "Upload contract product",
                                    "description": "Upload integration test",
                                    "type": "BEAT",
                                    "price": 19.99,
                                    "categories": ["BEATS"]
                                }
                                """;
                String created = mockMvc.perform(authenticated(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createRequest)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();
                UUID productId = UUID.fromString(new ObjectMapper().readTree(created).get("id").asText());

                MockMultipartFile file = new MockMultipartFile(
                                "file", "preview.mp3", "audio/mpeg", "audio-bytes".getBytes());

                String uploadResponse = mockMvc.perform(multipart("/api/products/{productId}/assets/upload", productId)
                                .file(file)
                                .header("Authorization", "Bearer " + adminToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.assetId").exists())
                                .andExpect(jsonPath("$.type").value("AUDIO_MP3"))
                                .andExpect(jsonPath("$.storageKey").doesNotExist())
                                .andExpect(jsonPath("$.bucket").doesNotExist())
                                .andExpect(jsonPath("$.objectKey").doesNotExist())
                                .andReturn().getResponse().getContentAsString();
                assertFalse(uploadResponse.contains("storageKey"));
                assertEquals(2, new ObjectMapper().readTree(uploadResponse).size(),
                                "Upload response must contain only assetId and type");
                String assetId = new ObjectMapper().readTree(uploadResponse).get("assetId").asText();

                mockMvc.perform(get("/api/products/admin/{productId}", productId)
                                .header("Authorization", "Bearer " + adminToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.assets[0].id").value(assetId))
                                .andExpect(jsonPath("$.assets[0].type").value("AUDIO_MP3"))
                                .andExpect(jsonPath("$.assets[0].storageKey").doesNotExist());

                verify(assetStorage).upload(anyString(), any(), eq((long) "audio-bytes".length()), eq("audio/mpeg"));
        }

        @Test
        void shouldUploadImageAsImageWhenRequestedTypeMatchesMimeType() throws Exception {
                String created = mockMvc.perform(authenticated(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                    "title": "Image upload contract product",
                                                    "type": "BEAT",
                                                    "price": 19.99,
                                                    "categories": ["BEATS"]
                                                }
                                                """)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();
                UUID productId = UUID.fromString(new ObjectMapper().readTree(created).get("id").asText());
                MockMultipartFile image = new MockMultipartFile(
                                "file", "cover.png", "image/png", new byte[] { 1, 2, 3 });

                mockMvc.perform(multipart("/api/products/{productId}/assets/upload", productId)
                                .file(image)
                                .param("type", "IMAGE")
                                .header("Authorization", "Bearer " + adminToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.type").value("IMAGE"))
                                .andExpect(jsonPath("$.storageKey").doesNotExist());
        }

        @Test
        void shouldUploadAudioPreviewAndMakeItAvailableThroughPublicPreviewEndpoint() throws Exception {
                String created = mockMvc.perform(authenticated(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                    "title": "Preview upload contract product",
                                                    "type": "BEAT",
                                                    "price": 19.99,
                                                    "categories": ["BEATS"]
                                                }
                                                """)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();
                UUID productId = UUID.fromString(new ObjectMapper().readTree(created).get("id").asText());
                MockMultipartFile preview = new MockMultipartFile(
                                "file", "preview.wav", "audio/wav", new byte[] { 1, 2, 3, 4 });

                String uploadResponse = mockMvc.perform(multipart("/api/products/{productId}/assets/upload", productId)
                                .file(preview)
                                .param("type", "AUDIO_PREVIEW")
                                .header("Authorization", "Bearer " + adminToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.type").value("AUDIO_PREVIEW"))
                                .andExpect(jsonPath("$.storageKey").doesNotExist())
                                .andReturn().getResponse().getContentAsString();
                UUID assetId = UUID.fromString(new ObjectMapper().readTree(uploadResponse).get("assetId").asText());

                mockMvc.perform(get("/api/products/admin/{productId}", productId)
                                .header("Authorization", "Bearer " + adminToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.assets[0].type").value("AUDIO_PREVIEW"))
                                .andExpect(jsonPath("$.assets[0].storageKey").doesNotExist());

                mockMvc.perform(authenticated(post("/api/products/{productId}/publish", productId)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("PUBLISHED"));

                when(assetStorage.generateDownloadUrl(anyString(), any(Duration.class)))
                                .thenReturn("https://storage.invalid/temporary-preview");
                mockMvc.perform(get("/api/products/{productId}/assets/{assetId}/preview", productId, assetId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.url").value("https://storage.invalid/temporary-preview"));

                verify(assetStorage).upload(anyString(), any(), eq(4L), eq("audio/wav"));
        }

        @Test
        void shouldRejectAnAudioPreviewTypeForAnImageUpload() throws Exception {
                String created = mockMvc.perform(authenticated(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                    "title": "Mismatched asset contract product",
                                                    "type": "BEAT",
                                                    "price": 19.99,
                                                    "categories": ["BEATS"]
                                                }
                                                """)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();
                UUID productId = UUID.fromString(new ObjectMapper().readTree(created).get("id").asText());
                MockMultipartFile image = new MockMultipartFile(
                                "file", "cover.png", "image/png", new byte[] { 1, 2, 3 });

                mockMvc.perform(multipart("/api/products/{productId}/assets/upload", productId)
                                .file(image)
                                .param("type", "AUDIO_PREVIEW")
                                .header("Authorization", "Bearer " + adminToken))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));

                verifyNoInteractions(assetStorage);
        }

        @Test
        void shouldRejectUnauthenticatedAssetUpload() throws Exception {
                MockMultipartFile file = new MockMultipartFile(
                                "file", "preview.mp3", "audio/mpeg", "audio".getBytes());

                mockMvc.perform(multipart("/api/products/{productId}/assets/upload", UUID.randomUUID())
                                .file(file))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void shouldRejectCustomerAssetUpload() throws Exception {
                MockMultipartFile file = new MockMultipartFile(
                                "file", "preview.mp3", "audio/mpeg", "audio".getBytes());

                mockMvc.perform(multipart("/api/products/{productId}/assets/upload", UUID.randomUUID())
                                .file(file)
                                .header("Authorization", "Bearer " + customerToken))
                                .andExpect(status().isForbidden());
        }

        @Test
        void shouldReturn404WhenAddingAssetToUnknownProduct() throws Exception {

                UUID unknownProductId = UUID.randomUUID();

                String requestBody = """
                                {
                                    "type": "AUDIO_WAV",
                                    "storageKey": "products/%s/full.wav"
                                }
                                """.formatted(unknownProductId);

                mockMvc.perform(
                                authenticated(post("/api/products/" + unknownProductId + "/assets")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error")
                                                .value("PRODUCT_NOT_FOUND"));
        }

        @Test
        void shouldRemoveAssetFromProduct() throws Exception {

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
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(createRequest)))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                ObjectMapper objectMapper = new ObjectMapper();

                String productId = objectMapper
                                .readTree(response)
                                .get("id")
                                .asText();

                String storageKey = "products/" + productId + "/full.wav";

                String assetRequest = """
                                {
                                    "type": "AUDIO_WAV",
                                    "storageKey": "%s"
                                }
                                """.formatted(storageKey);

                String assetResponse = mockMvc.perform(
                                authenticated(post("/api/products/" + productId + "/assets")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(assetRequest)))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                String assetId = objectMapper
                                .readTree(assetResponse)
                                .get("assets")
                                .get(0)
                                .get("id")
                                .asText();

                mockMvc.perform(
                                authenticated(delete("/api/products/" + productId + "/assets/" + assetId)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(productId))
                                .andExpect(jsonPath("$.assets").isArray())
                                .andExpect(jsonPath("$.assets.length()").value(0));
        }

        @Test
        void shouldReturn400WhenRemovingUnknownAsset() throws Exception {

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
                                authenticated(post("/api/products")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(createRequest)))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                ObjectMapper objectMapper = new ObjectMapper();

                String productId = objectMapper
                                .readTree(response)
                                .get("id")
                                .asText();

                UUID unknownAssetId = UUID.randomUUID();

                mockMvc.perform(
                                authenticated(delete("/api/products/" + productId + "/assets/" + unknownAssetId)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error")
                                                .value("INVALID_REQUEST"));
        }

        private MockHttpServletRequestBuilder authenticated(MockHttpServletRequestBuilder request) {
                return request.header(
                                "Authorization",
                                "Bearer " + adminToken);
        }

        private String tokenForRole(String role) {
                Instant now = Instant.now();

                JwtClaimsSet claims = JwtClaimsSet.builder()
                                .subject(UUID.randomUUID().toString())
                                .issuedAt(now)
                                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                                .claim("email", role.toLowerCase() + "@test.com")
                                .claim("role", role)
                                .build();

                return jwtEncoder
                                .encode(JwtEncoderParameters.from(claims))
                                .getTokenValue();
        }
}
