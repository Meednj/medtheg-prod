# Library Module Architecture

## Purpose

The Library module is the customer-facing access layer for purchased digital
products. It does not decide whether a purchase succeeded; it verifies the
customer's entitlement and then asks object storage for a temporary download
URL.

## Current Structure

```text
library/
├── application/
│   ├── port/AssetStorage.java
│   ├── service/GetCustomerLibraryService.java
│   ├── service/GetAssetDownloadUrlService.java
│   └── usecase/
└── infrastructure/
    ├── storage/
    │   ├── MinioAssetStorageAdapter.java
    │   └── MinioConfiguration.java
    └── web/
        ├── LibraryController.java
        └── LibraryAssetController.java
```

The module uses application ports for storage and keeps MinIO-specific code in
the infrastructure adapter.

## Access Flow

```text
Authenticated customer
        ↓
Library controller
        ↓
Library application service
        ↓
Verify active entitlement
        ↓
Resolve product asset
        ↓
AssetStorage port
        ↓
MinIO signed download URL
```

Customers can view their purchased library and request a download URL for an
asset they are entitled to access. The URL is time-limited so the application
does not need to proxy the file contents through the API.

## Storage Configuration

Local development uses MinIO at `http://localhost:9000` with the `medtheg-assets`
bucket by default. The bucket and credentials are configurable through
`MINIO_ENDPOINT`, `MINIO_ACCESS_KEY`, `MINIO_SECRET_KEY`, and `MINIO_BUCKET`.

## Current Boundary

Entitlement is the source of ownership. Library is the source of customer
access and download URL generation. A separate delivery or streaming workflow
has not yet been implemented.
