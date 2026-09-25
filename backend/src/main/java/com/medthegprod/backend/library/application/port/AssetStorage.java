package com.medthegprod.backend.library.application.port;

import java.io.InputStream;
import java.time.Duration;

public interface AssetStorage {

    void upload(
            String storageKey,
            InputStream inputStream,
            long contentLength,
            String contentType);

    String generateDownloadUrl(
            String storageKey,
            Duration expiration);
}