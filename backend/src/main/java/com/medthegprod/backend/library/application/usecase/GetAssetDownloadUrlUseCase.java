package com.medthegprod.backend.library.application.usecase;

import java.time.Duration;
import java.util.UUID;

public interface GetAssetDownloadUrlUseCase {

    String execute(
            UUID customerId,
            UUID assetId,
            Duration expiration);
}