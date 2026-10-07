package com.medthegprod.backend.catalog.infrastructure.web.controller;

import com.medthegprod.backend.catalog.application.usecase.GetProductCoverUrlUseCase;
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
@Tag(name = "Catalog", description = "Public product artwork")
public class ProductCoverController {

    private final GetProductCoverUrlUseCase getProductCoverUrlUseCase;

    public ProductCoverController(GetProductCoverUrlUseCase getProductCoverUrlUseCase) {
        this.getProductCoverUrlUseCase = getProductCoverUrlUseCase;
    }

    @GetMapping("/{productId}/assets/{assetId}/cover")
    @Operation(summary = "Get a product cover URL", description = "Returns a temporary URL for a published product's IMAGE asset.")
    public CoverUrlResponse getCoverUrl(
            @PathVariable UUID productId,
            @PathVariable UUID assetId) {
        return new CoverUrlResponse(
                getProductCoverUrlUseCase.execute(productId, assetId, Duration.ofMinutes(15)));
    }

    public record CoverUrlResponse(String url) {
    }
}