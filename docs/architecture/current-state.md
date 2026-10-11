# Backend Current State

## Scope

The backend is a Spring Boot 4.1.1 / Java 21 modular monolith. The current implementation contains Identity, Catalog, Sales, Entitlement, and Library modules using domain models, application use cases, repository or provider ports, and infrastructure adapters.

## Catalog Boundary

Catalog persistence stores products, categories, and typed digital assets in PostgreSQL through Flyway migrations. Public product reads and searches are constrained at the repository adapter to `PUBLISHED` products. Draft and archived products are available only through the authenticated administrator route and management operations.

The product API exposes categories and safe asset references. Cover images use the `IMAGE` asset type and preview audio uses `AUDIO_PREVIEW`. Public product responses do not expose storage keys. A published preview can be requested through a short-lived presigned URL; purchased assets remain behind the authenticated Library entitlement check.

The Production Desk now uses the separate `GET /api/products/admin` endpoint
to browse DRAFT, PUBLISHED, and ARCHIVED products with bounded pagination,
search, status/type filters, and update/title/price sorting. Product responses
include the persisted update timestamp. Admin-only preview signing supports
IMAGE and AUDIO_PREVIEW on draft products while retaining asset ownership and
type checks. The frontend editor changes title, description, price (EUR), and
categories; type remains fixed after creation. Asset upload and asset-reference
removal use the existing catalog operations.

## Purchase Boundary

Sales creates orders and Stripe Checkout sessions. Stripe webhook signatures are verified before processing. A paid `checkout.session.completed` event completes the payment and order idempotently, then publishes `OrderPaidEvent`. Entitlement consumes that event in `AFTER_COMMIT` handling and grants a unique customer/product entitlement.

The frontend success redirect is informational only. Backend payment and order state is authoritative.

The admin UI was verified by frontend lint/build and the focused catalog
integration suite. A full browser walkthrough of admin workflows was not
performed as part of those checks. The current change does not verify or claim
the complete hosted Stripe payment lifecycle.

## Access and Security

Identity returns a user response from registration and a JWT-bearing authentication response from login. Spring Security maps JWT roles to administrator or customer authorities. Customer order, entitlement, and library access is resolved from the authenticated subject and checked in application services.

Library downloads return the existing `{ url }` response shape and use expiring MinIO presigned URLs. The centralized application error shape is also used for authentication and authorization failures.

## Known Operational Preconditions

The full integration suite requires Docker for PostgreSQL Testcontainers. Production operation still requires replacement of development credentials, managed secret storage, restricted actuator exposure, and deployment-specific infrastructure configuration.
