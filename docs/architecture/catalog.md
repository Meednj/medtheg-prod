# Catalog Domain

## Responsibility

The Catalog bounded context manages products that are available
for sale on Med The G Store.

## Product

Product is the aggregate root.

A Product contains:

- ProductId
- ProductType
- title
- description
- Money
- ProductStatus
- DigitalAsset references
- category references

## Product Types

Initially supported:

- BEAT
- SAMPLE_PACK
- DRUM_KIT
- COURSE
- BUNDLE

Product types are represented as domain data initially rather than
separate inheritance hierarchies. Specialized domain models may be
introduced when product-specific business rules justify them.

## Product Lifecycle

```text
DRAFT → PUBLISHED → ARCHIVED