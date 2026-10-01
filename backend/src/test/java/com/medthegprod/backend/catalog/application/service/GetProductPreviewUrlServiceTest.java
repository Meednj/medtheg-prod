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
class GetProductPreviewUrlServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AssetStorage assetStorage;

    @Test
    void shouldGenerateUrlOnlyForPublishedPreviewAsset() {
        Product product = publishedProduct();
        AssetId assetId = AssetId.generate();
        product.addAsset(new DigitalAsset(
                assetId,
                DigitalAssetType.AUDIO_PREVIEW,
                "products/beat/preview.mp3"));

        when(productRepository.findPublishedById(product.getId()))
                .thenReturn(Optional.of(product));
        when(assetStorage.generateDownloadUrl(
                "products/beat/preview.mp3",
                Duration.ofMinutes(15)))
                .thenReturn("https://storage.test/preview");

        GetProductPreviewUrlService service = new GetProductPreviewUrlService(
                productRepository,
                assetStorage);

        assertEquals(
                "https://storage.test/preview",
                service.execute(
                        product.getId().value(),
                        assetId.value(),
                        Duration.ofMinutes(15)));
        verify(assetStorage).generateDownloadUrl(
                "products/beat/preview.mp3",
                Duration.ofMinutes(15));
    }

    @Test
    void shouldRejectProtectedAssetAsPublicPreview() {
        Product product = publishedProduct();
        AssetId assetId = AssetId.generate();
        product.addAsset(new DigitalAsset(
                assetId,
                DigitalAssetType.AUDIO_WAV,
                "products/beat/full.wav"));
        when(productRepository.findPublishedById(product.getId()))
                .thenReturn(Optional.of(product));

        GetProductPreviewUrlService service = new GetProductPreviewUrlService(
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