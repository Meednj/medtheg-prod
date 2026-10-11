# Catalog Module

## Overview

Catalog manages products and typed digital-asset references inside the modular
monolith. Products have a title, description, immutable type, EUR price,
lifecycle status, categories, assets, and audit timestamps. PostgreSQL stores
catalog metadata; MinIO stores file bytes through the storage port.

Product types are `BEAT`, `SAMPLE_PACK`, `DRUM_KIT`, `COURSE`, and `BUNDLE`.
Statuses are `DRAFT`, `PUBLISHED`, and `ARCHIVED`, with supported transitions
`DRAFT → PUBLISHED → ARCHIVED`. Categories are `BEATS`, `KITS`, `COURSES`, and
`BUNDLES`.

Digital assets store an ID, a `DigitalAssetType`, and an internal storage key.
Supported types are `IMAGE`, `AUDIO_PREVIEW`, `AUDIO_MP3`, `AUDIO_WAV`,
`STEMS`, `MIDI`, `PDF`, `VIDEO`, and `OTHER`. API product responses expose only
asset IDs and types; storage keys remain internal.

## Application and persistence

Application use cases cover product create/read/update, publish/archive,
public search, admin search, asset upload/add/remove, and temporary cover or
preview URL generation. Services depend on the `ProductRepository` and
`AssetStorage` ports. `ProductPersistenceAdapter` implements the repository
with Spring Data JPA and maps entities to domain objects. Flyway owns database
schema changes; Hibernate validates the schema.

Public product search always constrains results to `PUBLISHED`. Admin search
uses a separate query type and endpoint so status filters cannot broaden the
public catalog. Both paths are paginated and size-bounded. Admin ordering
supports updated time, title, and price; search matches title and description.

## REST API

Public endpoints:

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/api/products?page=0&size=12&type=&category=&search=&sortBy=createdAt&direction=desc` | Search published products |
| `GET` | `/api/products/{id}` | Get a published product |
| `GET` | `/api/products/{productId}/assets/{assetId}/cover` | Temporary URL for a published IMAGE |
| `GET` | `/api/products/{productId}/assets/{assetId}/preview` | Temporary URL for a published AUDIO_PREVIEW |

Admin endpoints require the `ADMIN` role:

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `POST` | `/api/products` | Create a DRAFT product |
| `GET` | `/api/products/admin?page=0&size=20&status=&type=&search=&sortBy=updatedAt&direction=desc` | List any product status with pagination and filters |
| `GET` | `/api/products/admin/{id}` | Retrieve a product in any status |
| `GET` | `/api/products/admin/{productId}/assets/{assetId}/preview?type=IMAGE\|AUDIO_PREVIEW` | Temporary preview URL for the owned IMAGE or AUDIO_PREVIEW on draft or live products |
| `PUT` | `/api/products/{id}` | Update title, description, price, and categories |
| `POST` | `/api/products/{id}/publish` | Publish a valid draft |
| `POST` | `/api/products/{id}/archive` | Archive a published product |
| `POST` | `/api/products/{productId}/assets/upload` | Upload/register file (`file`, optional explicit `type`); response has `assetId` and `type` |
| `DELETE` | `/api/products/{productId}/assets/{assetId}` | Remove an asset reference |

The admin list accepts optional `status` and `type` enum values, `search` up to
100 characters, `page` starting at zero, and `size` from 1 to 100. Product
responses include `updatedAt`, which comes from the database audit column.
Product type cannot be changed after creation and currency remains EUR. The
asset API does not store filename, byte size, or audio duration. Asset removal
removes the product reference; it does not promise object-store deletion.

The admin preview endpoint only signs IMAGE and AUDIO_PREVIEW asset types. It
is secured by Spring role authorization and checks that the asset belongs to
the requested product. Protected MP3/WAV/stems/downloads remain behind the
Library entitlement workflow.

## Tests

Catalog tests cover domain invariants, application services, PostgreSQL
persistence, and Spring web integration with MockMvc/Testcontainers. The
ProductController integration suite covers admin listing authorization,
status/type/search filters, pagination, public catalog behavior, and restricted
admin asset preview types.
