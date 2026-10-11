package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.application.usecase.GetAdminProductAssetUrlUseCase;
import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import com.medthegprod.backend.library.application.port.AssetStorage;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@Service
public class GetAdminProductAssetUrlService implements GetAdminProductAssetUrlUseCase {
    private static final Set<DigitalAssetType> PREVIEWABLE_TYPES = Set.of(
            DigitalAssetType.IMAGE, DigitalAssetType.AUDIO_PREVIEW);

    private final ProductRepository productRepository;
    private final AssetStorage assetStorage;

    public GetAdminProductAssetUrlService(ProductRepository productRepository, AssetStorage assetStorage) {
        this.productRepository = productRepository;
        this.assetStorage = assetStorage;
    }

    @Override
    public String execute(UUID productId, UUID assetId, DigitalAssetType expectedType, Duration expiration) {
        if (productId == null || assetId == null || expectedType == null) {
            throw new IllegalArgumentException("Product, asset, and asset type are required");
        }
        if (!PREVIEWABLE_TYPES.contains(expectedType)) {
            throw new IllegalArgumentException("Only product images and audio previews can be previewed");
        }
        if (expiration == null || expiration.isZero() || expiration.isNegative()) {
            throw new IllegalArgumentException("Expiration must be positive");
        }

        Product product = productRepository.findById(new ProductId(productId))
                .orElseThrow(() -> new ProductNotFoundException(new ProductId(productId)));
        var asset = product.getAssets().stream()
                .filter(candidate -> candidate.getId().value().equals(assetId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product asset not found"));
        if (asset.getType() != expectedType) {
            throw new IllegalArgumentException("Asset is not of the requested preview type");
        }
        return assetStorage.generateDownloadUrl(asset.getStorageKey(), expiration);
    }
}
