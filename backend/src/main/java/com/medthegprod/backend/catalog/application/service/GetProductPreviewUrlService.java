package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.application.usecase.GetProductPreviewUrlUseCase;
import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import com.medthegprod.backend.library.application.port.AssetStorage;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class GetProductPreviewUrlService implements GetProductPreviewUrlUseCase {

    private static final Duration PREVIEW_URL_EXPIRATION = Duration.ofMinutes(15);

    private final ProductRepository productRepository;
    private final AssetStorage assetStorage;

    public GetProductPreviewUrlService(
            ProductRepository productRepository,
            AssetStorage assetStorage) {
        this.productRepository = productRepository;
        this.assetStorage = assetStorage;
    }

    @Override
    public String execute(UUID productId, UUID assetId, Duration expiration) {
        if (productId == null || assetId == null) {
            throw new IllegalArgumentException("Product and asset IDs are required");
        }

        Product product = productRepository
                .findPublishedById(new ProductId(productId))
                .orElseThrow(() -> new ProductNotFoundException(new ProductId(productId)));

        var asset = product.getAssets().stream()
                .filter(candidate -> candidate.getId().value().equals(assetId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product preview asset not found"));

        if (asset.getType() != DigitalAssetType.AUDIO_PREVIEW) {
            throw new IllegalArgumentException("Asset is not a public audio preview");
        }

        Duration effectiveExpiration = expiration == null
                ? PREVIEW_URL_EXPIRATION
                : expiration;
        if (effectiveExpiration.isZero() || effectiveExpiration.isNegative()) {
            throw new IllegalArgumentException("Expiration must be positive");
        }

        return assetStorage.generateDownloadUrl(asset.getStorageKey(), effectiveExpiration);
    }
}