package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductType;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateProductServiceTest {

    private ProductRepository productRepository;
    private CreateProductService createProductService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);

        createProductService = new CreateProductService(productRepository);
    }

    @Test
    void shouldCreateAndSaveProduct() {

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product result = createProductService.execute(
                "Dark Trap Beat",
                "Dark trap instrumental",
                ProductType.BEAT,
                new BigDecimal("19.99"),
                Set.of());

        assertNotNull(result);
        assertEquals("Dark Trap Beat", result.getTitle());
        assertEquals(ProductType.BEAT, result.getType());
        assertEquals(
                new BigDecimal("19.99"),
                result.getPrice().amount());

        verify(productRepository).save(any(Product.class));
    }
}