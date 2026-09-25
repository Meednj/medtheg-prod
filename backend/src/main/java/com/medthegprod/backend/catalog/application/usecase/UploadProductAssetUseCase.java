package com.medthegprod.backend.catalog.application.usecase;

import java.io.InputStream;
import java.util.UUID;

public interface UploadProductAssetUseCase {

    ProductAsset execute(
            UUID productId,
            String fileName,
            String contentType,
            long contentLength,
            InputStream inputStream);
}