package com.medthegprod.backend.catalog.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.model.ProductStatus;
import com.medthegprod.backend.catalog.domain.model.ProductType;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class PublishProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private PublishProductService publishProductService;

    @BeforeEach
    void setUp() {
        publishProductService = new PublishProductService(productRepository);
    }

    @Test
    void shouldPublishProduct() {
        ProductId productId = ProductId.generate();

        Product product = new Product(
                productId,
                "Dark Trap Beat",
                "Dark trap instrumental",
                ProductType.BEAT,
                Money.eur(new BigDecimal("19.99")));

        product.addCategory(ProductCategory.BEATS);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(productRepository.save(product))
                .thenReturn(product);

        Product result = publishProductService.execute(productId);

        assertEquals(
                ProductStatus.PUBLISHED,
                result.getStatus());

        verify(productRepository).findById(productId);
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        ProductId productId = ProductId.generate();

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> publishProductService.execute(productId));

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldRejectProductWithoutCategory() {
        ProductId productId = ProductId.generate();

        Product product = new Product(
                productId,
                "Dark Trap Beat",
                "Dark trap instrumental",
                ProductType.BEAT,
                Money.eur(new BigDecimal("19.99")));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        assertThrows(
                IllegalStateException.class,
                () -> publishProductService.execute(productId));

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldRejectAlreadyPublishedProduct() {
        ProductId productId = ProductId.generate();

        Product product = new Product(
                productId,
                "Dark Trap Beat",
                "Dark trap instrumental",
                ProductType.BEAT,
                Money.eur(new BigDecimal("19.99"))
        );

        product.addCategory(ProductCategory.BEATS);
        product.publish();

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        assertThrows(
                IllegalStateException.class,
                () -> publishProductService.execute(productId)
        );

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any());
    }
}
