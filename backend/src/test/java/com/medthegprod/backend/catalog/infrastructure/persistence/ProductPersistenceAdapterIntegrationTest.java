package com.medthegprod.backend.catalog.infrastructure.persistence;

import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.model.ProductStatus;
import com.medthegprod.backend.catalog.domain.model.ProductType;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class ProductPersistenceAdapterIntegrationTest {

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
    private ProductRepository productRepository;

    @Test
    void shouldSaveAndRetrieveProduct() {

        Product product = new Product(
                ProductId.generate(),
                "Dark Trap Beat",
                "Dark trap instrumental",
                ProductType.BEAT,
                Money.eur(new BigDecimal("19.99")));

        Product saved = productRepository.save(product);

        Product retrieved = productRepository
                .findById(saved.getId())
                .orElseThrow();

        assertEquals(
                saved.getId(),
                retrieved.getId());

        assertEquals(
                "Dark Trap Beat",
                retrieved.getTitle());

        assertEquals(
                ProductType.BEAT,
                retrieved.getType());

        assertEquals(
                new BigDecimal("19.99"),
                retrieved.getPrice().amount());

        assertEquals(
                ProductStatus.DRAFT,
                retrieved.getStatus());
    }
}