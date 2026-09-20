package com.medthegprod.backend.catalog.infrastructure.web.dto;

import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddProductAssetRequest(
        @NotNull DigitalAssetType type,

        @NotBlank @Size(max = 500) String storageKey) {
}