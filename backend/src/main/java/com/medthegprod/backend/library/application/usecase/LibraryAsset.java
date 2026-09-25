package com.medthegprod.backend.library.application.usecase;

import java.util.UUID;

public record LibraryAsset(
                UUID assetId,
                UUID productId,
                String type) {
}