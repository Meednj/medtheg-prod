package com.medthegprod.backend.catalog.application.usecase;

import java.time.Duration;
import java.util.UUID;

public interface GetProductPreviewUrlUseCase {

    String execute(UUID productId, UUID assetId, Duration expiration);
}