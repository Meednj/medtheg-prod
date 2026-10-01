package com.medthegprod.backend.catalog.infrastructure.web.controller;

import com.medthegprod.backend.catalog.application.usecase.GetProductPreviewUrlUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Catalog", description = "Public product previews")
public class ProductPreviewController {

    private final GetProductPreviewUrlUseCase getProductPreviewUrlUseCase;

    public ProductPreviewController(GetProductPreviewUrlUseCase getProductPreviewUrlUseCase) {
        this.getProductPreviewUrlUseCase = getProductPreviewUrlUseCase;
    }

    @GetMapping("/{productId}/assets/{assetId}/preview")
    @Operation(summary = "Get a product preview URL", description = "Returns a temporary URL for a published product's AUDIO_PREVIEW asset.")
    public PreviewUrlResponse getPreviewUrl(
            @PathVariable UUID productId,
            @PathVariable UUID assetId) {
        return new PreviewUrlResponse(
                getProductPreviewUrlUseCase.execute(productId, assetId, Duration.ofMinutes(15)));
    }

    public record PreviewUrlResponse(String url) {
    }
}