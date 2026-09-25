package com.medthegprod.backend.library.infrastructure.storage;

import com.medthegprod.backend.library.application.port.AssetStorage;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.Http.Method;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class MinioAssetStorageAdapter implements AssetStorage {

    private final MinioClient minioClient;
    private final MinioProperties properties;

    public MinioAssetStorageAdapter(
            MinioClient minioClient,
            MinioProperties properties) {
        this.minioClient = minioClient;
        this.properties = properties;
    }

    @Override
    public void upload(
            String storageKey,
            InputStream inputStream,
            long contentLength,
            String contentType) {

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.bucket())
                            .object(storageKey)
                            .stream(
                                    inputStream,
                                    contentLength,
                                    -1L)
                            .contentType(
                                    contentType != null
                                            ? contentType
                                            : "application/octet-stream")
                            .build());
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to upload asset to storage",
                    e);
        }
    }

    @Override
    public String generateDownloadUrl(
            String storageKey,
            Duration expiration) {

        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(properties.bucket())
                            .object(storageKey)
                            .expiry(
                                    Math.toIntExact(
                                            expiration.toSeconds()),
                                    TimeUnit.SECONDS)
                            .build());
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to generate asset download URL",
                    e);
        }
    }
}