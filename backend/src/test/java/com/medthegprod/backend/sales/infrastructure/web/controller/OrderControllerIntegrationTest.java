package com.medthegprod.backend.sales.infrastructure.web.controller;

import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.model.ProductType;
import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import com.medthegprod.backend.identity.domain.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.medthegprod.backend.sales.application.usecase.CreatePaymentUseCase;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;



import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private ProductRepository productRepository;

    @MockitoBean
    private CreatePaymentUseCase createPaymentUseCase;

    private String customerToken;

    private UserId customerId;

    @BeforeEach
    void setUp() {

        customerId = UserId.generate();

        Instant now = Instant.now();

        var claims = org.springframework.security.oauth2.jwt.JwtClaimsSet
                .builder()
                .subject(customerId.value().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .claim("email", "customer@test.com")
                .claim("role", "CUSTOMER")
                .build();

        customerToken = jwtEncoder
                .encode(
                        org.springframework.security.oauth2.jwt.JwtEncoderParameters
                                .from(claims))
                .getTokenValue();
    }

    @Test
    void shouldCreateOrderForAuthenticatedCustomer() throws Exception {

        ProductId productId = ProductId.generate();

        Product product = new Product(
                productId,
                "Dark Beat",
                "Dark underground beat",
                ProductType.BEAT,
                Money.eur(new BigDecimal("15.00")));

        product.addCategory(ProductCategory.BEATS);
        product.publish();

        productRepository.save(product);

        String request = """
                {
                    "items": [
                        {
                            "productId": "%s",
                            "quantity": 1
                        }
                    ]
                }
                """.formatted(productId.value());

        mockMvc.perform(
                post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .header(
                                "Authorization",
                                "Bearer " + customerToken)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId")
                        .value(customerId.value().toString()))
                .andExpect(jsonPath("$.status")
                        .value("PENDING"))
                .andExpect(jsonPath("$.total")
                        .value(15.00))
                .andExpect(jsonPath("$.currency")
                        .value("EUR"))
                .andExpect(jsonPath("$.items.length()")
                        .value(1))
                .andExpect(jsonPath("$.items[0].productId")
                        .value(productId.value().toString()))
                .andExpect(jsonPath("$.items[0].productTitle")
                        .value("Dark Beat"))
                .andExpect(jsonPath("$.items[0].unitPrice")
                        .value(15.00))
                .andExpect(jsonPath("$.items[0].quantity")
                        .value(1));
    }

    @Test
    void shouldRejectUnauthenticatedCustomer() throws Exception {

        ProductId productId = ProductId.generate();

        String request = """
                {
                    "items": [
                        {
                            "productId": "%s",
                            "quantity": 1
                        }
                    ]
                }
                """.formatted(productId.value());

        mockMvc.perform(
                post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectInvalidQuantity() throws Exception {

            ProductId productId = ProductId.generate();

            String request = """
                            {
                                "items": [
                                    {
                                        "productId": "%s",
                                        "quantity": 0
                                    }
                                ]
                            }
                            """.formatted(productId.value());

            mockMvc.perform(
                            post("/api/orders")
                                            .contentType(APPLICATION_JSON)
                                            .header(
                                                            "Authorization",
                                                            "Bearer " + customerToken)
                                            .content(request))
                            .andExpect(status().isBadRequest());
    }
    
    @Test
    void shouldCreatePaymentForAuthenticatedCustomer() throws Exception {

            UUID orderId = UUID.randomUUID();

            CreatePaymentUseCase.PaymentResult paymentResult = new CreatePaymentUseCase.PaymentResult(
                            orderId,
                            "pi_test_123",
                            "https://checkout.stripe.test/session");

            when(createPaymentUseCase.execute(
                            new com.medthegprod.backend.sales.domain.model.OrderId(orderId),
                            customerId.value())).thenReturn(paymentResult);

            mockMvc.perform(
                            post("/api/orders/{orderId}/payment", orderId)
                                            .header(
                                                            "Authorization",
                                                            "Bearer " + customerToken))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.orderId")
                                            .value(orderId.toString()))
                            .andExpect(jsonPath("$.paymentId")
                                            .value("pi_test_123"))
                            .andExpect(jsonPath("$.checkoutUrl")
                                            .value("https://checkout.stripe.test/session"));
    }

    @Test
    void shouldRejectUnauthenticatedPaymentRequest() throws Exception {

            UUID orderId = UUID.randomUUID();

            mockMvc.perform(
                            post("/api/orders/{orderId}/payment", orderId))
                            .andExpect(status().isUnauthorized());
    }
}