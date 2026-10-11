package com.medthegprod.backend.catalog.infrastructure.web.controller;

import com.medthegprod.backend.catalog.application.usecase.GetProductPreviewUrlUseCase;
import com.medthegprod.backend.catalog.application.usecase.GetAdminProductAssetUrlUseCase;
import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Catalog", description = "Public product previews")
public class ProductPreviewController {

    private final GetProductPreviewUrlUseCase getProductPreviewUrlUseCase;
    private final GetAdminProductAssetUrlUseCase getAdminProductAssetUrlUseCase;

    public ProductPreviewController(
            GetProductPreviewUrlUseCase getProductPreviewUrlUseCase,
            GetAdminProductAssetUrlUseCase getAdminProductAssetUrlUseCase) {
        this.getProductPreviewUrlUseCase = getProductPreviewUrlUseCase;
        this.getAdminProductAssetUrlUseCase = getAdminProductAssetUrlUseCase;
    }

    @GetMapping("/{productId}/assets/{assetId}/preview")
    @Operation(summary = "Get a product preview URL", description = "Returns a temporary URL for a published product's AUDIO_PREVIEW asset.")
    public PreviewUrlResponse getPreviewUrl(
            @PathVariable UUID productId,
            @PathVariable UUID assetId) {
        return new PreviewUrlResponse(
                getProductPreviewUrlUseCase.execute(productId, assetId, Duration.ofMinutes(15)));
    }

    @GetMapping("/admin/{productId}/assets/{assetId}/preview")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get an admin asset preview URL", description = "Returns a temporary URL for a product IMAGE or AUDIO_PREVIEW asset. Requires ADMIN.", security = @SecurityRequirement(name = "bearerAuth"))
    public PreviewUrlResponse getAdminPreviewUrl(
            @PathVariable UUID productId,
            @PathVariable UUID assetId,
            @RequestParam DigitalAssetType type) {
        return new PreviewUrlResponse(getAdminProductAssetUrlUseCase.execute(
                productId, assetId, type, Duration.ofMinutes(15)));
    }

    public record PreviewUrlResponse(String url) {
    }
}
