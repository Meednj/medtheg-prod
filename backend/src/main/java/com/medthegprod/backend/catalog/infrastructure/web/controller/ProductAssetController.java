package com.medthegprod.backend.catalog.infrastructure.web.controller;

import com.medthegprod.backend.catalog.application.usecase.ProductAsset;
import com.medthegprod.backend.catalog.application.usecase.UploadProductAssetUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductAssetController {

    private final UploadProductAssetUseCase uploadProductAssetUseCase;

    public ProductAssetController(
            UploadProductAssetUseCase uploadProductAssetUseCase) {
        this.uploadProductAssetUseCase = uploadProductAssetUseCase;
    }

    @PostMapping("/{productId}/assets/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssetUploadResponse> upload(
            @PathVariable UUID productId,
            @RequestParam("file") MultipartFile file) throws IOException {

        ProductAsset productAsset = uploadProductAssetUseCase.execute(
                productId,
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getInputStream());

        return ResponseEntity.ok(
                new AssetUploadResponse(
                        productAsset.asset().getId().value(),
                        productAsset.asset().getStorageKey()));
    }

    public record AssetUploadResponse(
            UUID assetId,
            String storageKey) {
    }
}