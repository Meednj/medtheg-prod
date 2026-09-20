package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.domain.model.AssetId;
import com.medthegprod.backend.catalog.domain.model.DigitalAsset;
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
class RemoveProductAssetServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private RemoveProductAssetService service;

    @Test
    void shouldRemoveAssetFromProduct() {

        ProductId productId = ProductId.generate();

        Product product = new Product(
                productId,
                "Dark Beat",
                "Dark trap beat",
                ProductType.BEAT,
                Money.eur(new BigDecimal("19.99")));

        AssetId assetId = AssetId.generate();

        DigitalAsset asset = new DigitalAsset(
                assetId,
                DigitalAssetType.AUDIO_WAV,
                "products/dark-beat/full.wav");

        product.addAsset(asset);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(productRepository.save(product))
                .thenReturn(product);

        Product result = service.execute(
                productId,
                assetId);

        assertTrue(result.getAssets().isEmpty());

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
                        AssetId.generate()));

        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenAssetDoesNotExist() {

        ProductId productId = ProductId.generate();

        Product product = new Product(
                productId,
                "Dark Beat",
                "Dark trap beat",
                ProductType.BEAT,
                Money.eur(new BigDecimal("19.99")));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.execute(
                        productId,
                        AssetId.generate()));

        verify(productRepository, never()).save(any());
    }
}