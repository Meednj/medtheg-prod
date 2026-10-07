package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.domain.model.AssetId;
import com.medthegprod.backend.catalog.domain.model.DigitalAsset;
import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;
import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.model.ProductType;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import com.medthegprod.backend.library.application.port.AssetStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProductCoverUrlServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AssetStorage assetStorage;

    @Test
    void shouldGenerateUrlOnlyForPublishedImageAsset() {
        Product product = publishedProduct();
        AssetId assetId = AssetId.generate();
        product.addAsset(new DigitalAsset(
                assetId,
                DigitalAssetType.IMAGE,
                "products/beat/cover.jpg"));

        when(productRepository.findPublishedById(product.getId()))
                .thenReturn(Optional.of(product));
        when(assetStorage.generateDownloadUrl(
                "products/beat/cover.jpg",
                Duration.ofMinutes(15)))
                .thenReturn("https://storage.test/cover");

        GetProductCoverUrlService service = new GetProductCoverUrlService(
                productRepository,
                assetStorage);

        assertEquals(
                "https://storage.test/cover",
                service.execute(
                        product.getId().value(),
                        assetId.value(),
                        Duration.ofMinutes(15)));
        verify(assetStorage).generateDownloadUrl(
                "products/beat/cover.jpg",
                Duration.ofMinutes(15));
    }

    @Test
    void shouldRejectProtectedAssetAsPublicCover() {
        Product product = publishedProduct();
        AssetId assetId = AssetId.generate();
        product.addAsset(new DigitalAsset(
                assetId,
                DigitalAssetType.AUDIO_WAV,
                "products/beat/full.wav"));
        when(productRepository.findPublishedById(product.getId()))
                .thenReturn(Optional.of(product));

        GetProductCoverUrlService service = new GetProductCoverUrlService(
                productRepository,
                assetStorage);

        assertThrows(IllegalArgumentException.class,
                () -> service.execute(
                        product.getId().value(),
                        assetId.value(),
                        Duration.ofMinutes(15)));
    }

    private Product publishedProduct() {
        Product product = new Product(
                ProductId.generate(),
                "Dark Beat",
                "Dark instrumental",
                ProductType.BEAT,
                Money.eur(new BigDecimal("19.99")));
        product.addCategory(ProductCategory.BEATS);
        product.publish();
        return product;
    }
}