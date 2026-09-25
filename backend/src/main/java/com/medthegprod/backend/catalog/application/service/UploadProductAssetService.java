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
            InputStream inputStream) {

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
                determineAssetType(contentType),
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
            String contentType) {

        if (contentType == null) {
            return DigitalAssetType.OTHER;
        }

        return switch (contentType.toLowerCase()) {
            case "audio/mpeg",
                    "audio/mp3" ->
                DigitalAssetType.AUDIO_MP3;

            case "audio/wav",
                    "audio/x-wav",
                    "audio/wave" ->
                DigitalAssetType.AUDIO_WAV;

            case "audio/midi",
                    "audio/x-midi" ->
                DigitalAssetType.MIDI;

            case "application/pdf" ->
                DigitalAssetType.PDF;

            default ->
                contentType.startsWith("video/")
                        ? DigitalAssetType.VIDEO
                        : DigitalAssetType.OTHER;
        };
    }
}