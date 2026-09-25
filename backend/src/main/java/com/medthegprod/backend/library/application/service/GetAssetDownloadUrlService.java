package com.medthegprod.backend.library.application.service;

import com.medthegprod.backend.catalog.application.usecase.GetProductAssetUseCase;
import com.medthegprod.backend.catalog.application.usecase.ProductAsset;

import com.medthegprod.backend.entitlement.domain.model.Entitlement;
import com.medthegprod.backend.entitlement.domain.model.EntitlementStatus;
import com.medthegprod.backend.entitlement.domain.repository.EntitlementRepository;
import com.medthegprod.backend.library.application.port.AssetStorage;
import com.medthegprod.backend.library.application.usecase.GetAssetDownloadUrlUseCase;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class GetAssetDownloadUrlService
        implements GetAssetDownloadUrlUseCase {
                
    private static final Duration DOWNLOAD_URL_EXPIRATION = Duration.ofMinutes(15);

    private final EntitlementRepository entitlementRepository;
    private final GetProductAssetUseCase getProductAssetUseCase;
    private final AssetStorage assetStorage;

    public GetAssetDownloadUrlService(
            EntitlementRepository entitlementRepository,
            GetProductAssetUseCase getProductAssetUseCase,
            AssetStorage assetStorage) {

        this.entitlementRepository = entitlementRepository;
        this.getProductAssetUseCase = getProductAssetUseCase;
        this.assetStorage = assetStorage;
    }

    @Override
    public String execute(
            UUID customerId,
            UUID assetId,
            Duration expiration) {

        if (customerId == null) {
            throw new IllegalArgumentException(
                    "Customer ID cannot be null");
        }

        if (assetId == null) {
            throw new IllegalArgumentException(
                    "Asset ID cannot be null");
        }

        Duration effectiveExpiration = expiration != null
                ? expiration
                : DOWNLOAD_URL_EXPIRATION;

        if (effectiveExpiration.isZero()
                || effectiveExpiration.isNegative()) {
            throw new IllegalArgumentException(
                    "Expiration must be positive");
        }

        ProductAsset productAsset = getProductAssetUseCase.execute(assetId);

        UUID productId = productAsset.productId().value();

        Entitlement entitlement = entitlementRepository
                .findByCustomerIdAndProductId(
                        customerId,
                        productId)
                .orElseThrow(() -> new LibraryAccessDeniedException(
                                        "Customer does not own this product"));

        if (entitlement.getStatus() != EntitlementStatus.ACTIVE) {
            throw new LibraryAccessDeniedException(
                                        "Customer entitlement is not active");
        }

        return assetStorage.generateDownloadUrl(
                productAsset.asset().getStorageKey(),
                effectiveExpiration);
    }
}