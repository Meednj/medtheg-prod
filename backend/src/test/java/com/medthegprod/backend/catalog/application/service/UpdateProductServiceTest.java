package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.model.ProductType;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private UpdateProductService updateProductService;

    @BeforeEach
    void setUp() {
        updateProductService = new UpdateProductService(productRepository);
    }

    @Test
    void shouldUpdateProduct() {
        ProductId productId = ProductId.generate();

        Product product = new Product(
                productId,
                "Old Beat",
                "Old description",
                ProductType.BEAT,
                Money.eur(new BigDecimal("19.99")));

        product.addCategory(ProductCategory.BEATS);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(productRepository.save(product))
                .thenReturn(product);

        Product result = updateProductService.execute(
                productId,
                "New Beat",
                "Updated description",
                ProductType.BEAT,
                new BigDecimal("29.99"),
                Set.of(ProductCategory.BEATS));

        assertEquals("New Beat", result.getTitle());
        assertEquals("Updated description", result.getDescription());
        assertEquals(
                new BigDecimal("29.99"),
                result.getPrice().amount());
        assertEquals(ProductType.BEAT, result.getType());
        assertEquals(
                Set.of(ProductCategory.BEATS),
                result.getCategories());
        assertEquals(
                com.medthegprod.backend.catalog.domain.model.ProductStatus.DRAFT,
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
                () -> updateProductService.execute(
                        productId,
                        "New Beat",
                        "Updated description",
                        ProductType.BEAT,
                        new BigDecimal("29.99"),
                        Set.of(ProductCategory.BEATS)));

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldReplaceExistingCategories() {
        ProductId productId = ProductId.generate();

        Product product = new Product(
                productId,
                "Old Beat",
                "Old description",
                ProductType.BEAT,
                Money.eur(new BigDecimal("19.99")));

        product.addCategory(ProductCategory.BEATS);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(productRepository.save(product))
                .thenReturn(product);

        updateProductService.execute(
                productId,
                "Updated Beat",
                "Updated description",
                ProductType.BEAT,
                new BigDecimal("29.99"),
                Set.of(ProductCategory.KITS));

        assertEquals(
                Set.of(ProductCategory.KITS),
                product.getCategories());

        assertFalse(
                product.getCategories()
                        .contains(ProductCategory.BEATS));
    }

    @Test
    void shouldKeepProductStatusUnchanged() {
        ProductId productId = ProductId.generate();

        Product product = new Product(
                productId,
                "Beat",
                "Description",
                ProductType.BEAT,
                Money.eur(new BigDecimal("19.99")));

        product.publish();

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(productRepository.save(product))
                .thenReturn(product);

        Product result = updateProductService.execute(
                productId,
                "Updated Beat",
                "Updated description",
                ProductType.BEAT,
                new BigDecimal("29.99"),
                Set.of(ProductCategory.BEATS));

        assertEquals(
                com.medthegprod.backend.catalog.domain.model.ProductStatus.PUBLISHED,
                result.getStatus());
    }
}