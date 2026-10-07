package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.application.usecase.ProductAsset;
import com.medthegprod.backend.catalog.application.usecase.UploadProductAssetUseCase;
import com.medthegprod.backend.catalog.domain.model.AssetId;
import com.medthegprod.backend.catalog.domain.model.DigitalAsset;
import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import com.medthegprod.backend.library.application.port.AssetStorage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class UploadProductAssetService implements UploadProductAssetUseCase {

    private final ProductRepository productRepository;
    private final AssetStorage assetStorage;

    public UploadProductAssetService(
            ProductRepository productRepository,
            AssetStorage assetStorage) {
        this.productRepository = productRepository;
        this.assetStorage = assetStorage;
    }

    @Override
    @Transactional
    public ProductAsset execute(
            UUID productId,
            String fileName,
            String contentType,
            long contentLength,
            InputStream inputStream,
            DigitalAssetType requestedType) {

        if (productId == null) {
            throw new IllegalArgumentException(
                    "Product ID cannot be null");
        }

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException(
                    "File name cannot be blank");
        }

        if (contentLength <= 0) {
            throw new IllegalArgumentException(
                    "File cannot be empty");
        }

        if (inputStream == null) {
            throw new IllegalArgumentException(
                    "Input stream cannot be null");
        }

        DigitalAssetType assetType = determineAssetType(contentType, requestedType);

        Product product = productRepository
                .findById(new ProductId(productId))
                .orElseThrow(() -> new ProductNotFoundException(
                        new ProductId(productId)));

        String storageKey = buildStorageKey(
                productId,
                fileName);

        assetStorage.upload(
                storageKey,
                inputStream,
                contentLength,
                contentType);

        DigitalAsset asset = new DigitalAsset(
                AssetId.generate(),
                assetType,
                storageKey);

        product.addAsset(asset);

        Product savedProduct = productRepository.save(product);

        return new ProductAsset(
                savedProduct.getId(),
                asset);
    }

    private String buildStorageKey(
            UUID productId,
            String fileName) {

        String safeFileName = fileName
                .replaceAll("[^a-zA-Z0-9._-]", "_");

        return "products/"
                + productId
                + "/"
                + UUID.randomUUID()
                + "-"
                + safeFileName;
    }

    private DigitalAssetType determineAssetType(
            String contentType,
            DigitalAssetType requestedType) {

        String normalizedContentType = contentType == null
                ? null
                : contentType.toLowerCase(Locale.ROOT);

        if (requestedType != null) {
            if (!isCompatible(requestedType, normalizedContentType)) {
                throw new IllegalArgumentException(
                        "Uploaded file content type is not valid for " + requestedType);
            }
            return requestedType;
        }

        if (normalizedContentType == null) {
            return DigitalAssetType.OTHER;
        }

        return inferAssetType(normalizedContentType);
    }

    private boolean isCompatible(
            DigitalAssetType requestedType,
            String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return false;
        }

        return switch (requestedType) {
            case IMAGE -> Set.of("image/jpeg", "image/png", "image/webp").contains(contentType);
            case AUDIO_PREVIEW -> isPlayableAudio(contentType);
            case AUDIO_MP3 -> Set.of("audio/mpeg", "audio/mp3").contains(contentType);
            case AUDIO_WAV -> Set.of("audio/wav", "audio/x-wav", "audio/wave").contains(contentType);
            case STEMS -> Set.of("application/zip", "application/x-zip-compressed").contains(contentType);
            case MIDI -> Set.of("audio/midi", "audio/x-midi").contains(contentType);
            case PDF -> "application/pdf".equals(contentType);
            case VIDEO -> contentType.startsWith("video/");
            case OTHER -> !isKnownAssetContentType(contentType);
        };
    }

    private boolean isKnownAssetContentType(String contentType) {
        return contentType.startsWith("image/")
                || contentType.startsWith("audio/")
                || contentType.startsWith("video/")
                || "application/pdf".equals(contentType)
                || Set.of("application/zip", "application/x-zip-compressed").contains(contentType);
    }

    private boolean isPlayableAudio(String contentType) {
        return Set.of(
                "audio/mpeg", "audio/mp3",
                "audio/wav", "audio/x-wav", "audio/wave",
                "audio/ogg", "audio/webm", "audio/aac",
                "audio/flac", "audio/mp4").contains(contentType);
    }

    private DigitalAssetType inferAssetType(String contentType) {
        return switch (contentType) {
            case "image/jpeg", "image/png", "image/webp" -> DigitalAssetType.IMAGE;
            case "audio/mpeg", "audio/mp3" -> DigitalAssetType.AUDIO_MP3;
            case "audio/wav", "audio/x-wav", "audio/wave" -> DigitalAssetType.AUDIO_WAV;
            case "audio/midi", "audio/x-midi" -> DigitalAssetType.MIDI;
            case "application/pdf" -> DigitalAssetType.PDF;
            default -> contentType.startsWith("video/") ? DigitalAssetType.VIDEO : DigitalAssetType.OTHER;
        };
    }
}
