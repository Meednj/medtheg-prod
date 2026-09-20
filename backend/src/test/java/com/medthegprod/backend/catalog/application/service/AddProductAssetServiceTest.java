package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;
import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.model.ProductType;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddProductAssetServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private AddProductAssetService service;

    @Test
    void shouldAddAssetToProduct() {

        ProductId productId = ProductId.generate();

        Product product = new Product(
                productId,
                "Dark Beat",
                "Dark trap beat",
                ProductType.BEAT,
                Money.eur(new BigDecimal("19.99")));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(productRepository.save(product))
                .thenReturn(product);

        Product result = service.execute(
                productId,
                DigitalAssetType.AUDIO_WAV,
                "products/dark-beat/full.wav");

        assertEquals(1, result.getAssets().size());

        assertEquals(
                DigitalAssetType.AUDIO_WAV,
                result.getAssets().getFirst().getType());

        assertEquals(
                "products/dark-beat/full.wav",
                result.getAssets().getFirst().getStorageKey());

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
                () -> service.execute(
                        productId,
                        DigitalAssetType.AUDIO_WAV,
                        "products/test/full.wav"));

        verify(productRepository, never()).save(any());
    }
}