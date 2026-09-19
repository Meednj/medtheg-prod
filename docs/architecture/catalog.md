# Catalog Module

## Overview

The Catalog module manages the products available in the MedTheG production platform.

The current catalog supports:

- Beats
- Sample packs
- Drum kits
- Courses
- Bundles

The module is implemented inside the modular monolith using a Hexagonal Architecture approach.

## Domain Model

The main aggregate root is `Product`.

A product contains:

- `ProductId`
- title
- description
- `ProductType`
- price represented by the `Money` value object
- `ProductStatus`
- digital assets
- product categories

### Product Types

```text
BEAT
SAMPLE_PACK
DRUM_KIT
COURSE
BUNDLE
```

### Product Status

```text
DRAFT
PUBLISHED
ARCHIVED
```

Products currently follow these lifecycle rules:

```text
DRAFT → PUBLISHED → ARCHIVED
```

Invalid transitions are rejected by the domain model.

## Value Objects

### ProductId

Wraps the product UUID and prevents null identifiers.

### Money

Represents an amount together with its currency.

The catalog currently creates prices in EUR.

## Digital Assets

`DigitalAsset` represents a digital file associated with a product.

The domain stores a storage key rather than the physical file itself.

Supported asset types currently include:

```text
AUDIO_PREVIEW
AUDIO_MP3
AUDIO_WAV
STEMS
MIDI
PDF
VIDEO
OTHER
```

The actual object storage implementation will be introduced later.

## Categories

The catalog currently uses high-level categories:

```text
BEATS
KITS
COURSES
BUNDLES
```

More specific concepts such as Trap, Drill, Dark, etc. are intentionally not modeled as categories. They can be introduced later as tags or searchable attributes.

## Application Layer

The application layer exposes use cases through interfaces.

Current use cases:

- `CreateProductUseCase`
- `GetProductUseCase`

Application services coordinate domain objects and repository ports without depending directly on PostgreSQL or Spring Data.

## Persistence

Persistence is implemented through an adapter.

The domain exposes:

```text
ProductRepository
```

The infrastructure layer implements this port using:

```text
ProductPersistenceAdapter
        ↓
ProductJpaRepository
        ↓
PostgreSQL
```

JPA entities are kept separate from domain objects.

`ProductMapper` is responsible for converting between persistence entities and domain models.

## Database

The catalog currently uses PostgreSQL.

The initial schema is managed by Flyway:

```text
V1__create_products_table.sql
```

The `products` table contains:

- id
- title
- description
- type
- price
- currency
- status
- created_at
- updated_at

Hibernate is configured with:

```text
ddl-auto: validate
```

Flyway is therefore responsible for schema evolution.

## REST API

Current endpoints:

```text
POST /api/products
GET  /api/products/{id}
```

### Create Product

```http
POST /api/products
Content-Type: application/json
```

Example:

```json
{
  "title": "Dark Trap Beat",
  "description": "Dark trap instrumental",
  "type": "BEAT",
  "price": 19.99
}
```

A successfully created product starts with:

```text
status = DRAFT
currency = EUR
```

### Get Product

```http
GET /api/products/{id}
```

A missing product returns HTTP `404`.

Invalid request data returns HTTP `400`.

## Testing

The catalog is tested at multiple levels.

### Domain tests

The `ProductTest` suite verifies:

- product creation
- lifecycle transitions
- invalid lifecycle transitions
- price validation
- title validation
- digital asset management
- duplicate asset prevention
- category management
- domain reconstitution

### Application tests

`CreateProductServiceTest` verifies that the application service creates a product and persists it through the repository port.

### Persistence integration tests

`ProductPersistenceAdapterIntegrationTest` uses Testcontainers PostgreSQL to verify real persistence behavior.

### Web integration tests

`ProductControllerIntegrationTest` uses:

- Spring Boot
- MockMvc
- Testcontainers PostgreSQL

It verifies:

- product creation
- validation failures
- product retrieval
- missing-product handling

## Architectural Rules

The Catalog module follows these rules:

1. Domain code must not depend on infrastructure.
2. Application services depend on repository ports, not JPA repositories.
3. JPA entities are not exposed directly through the REST API.
4. Domain models are mapped to persistence entities.
5. Database schema changes are managed with Flyway.
6. External infrastructure such as PostgreSQL, object storage, payment providers and Redis must remain behind adapters or ports where appropriate.

## Catalog querying supports:

- Pagination
- Sorting
- Product type filtering
- Product category filtering
- Text search across title and description
- Composable filters using JPA Specifications
