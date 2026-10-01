# Backend Current State

## Scope

The backend is a Spring Boot 4.1.1 / Java 21 modular monolith. The current implementation contains Identity, Catalog, Sales, Entitlement, and Library modules using domain models, application use cases, repository or provider ports, and infrastructure adapters.

## Catalog Boundary

Catalog persistence stores products, categories, and typed digital assets in PostgreSQL through Flyway migrations. Public product reads and searches are constrained at the repository adapter to `PUBLISHED` products. Draft and archived products are available only through the authenticated administrator route and management operations.

The product API exposes categories and safe asset references. Cover images use the `IMAGE` asset type and preview audio uses `AUDIO_PREVIEW`. Public product responses do not expose storage keys. A published preview can be requested through a short-lived presigned URL; purchased assets remain behind the authenticated Library entitlement check.

## Purchase Boundary

Sales creates orders and Stripe Checkout sessions. Stripe webhook signatures are verified before processing. A paid `checkout.session.completed` event completes the payment and order idempotently, then publishes `OrderPaidEvent`. Entitlement consumes that event in `AFTER_COMMIT` handling and grants a unique customer/product entitlement.

The frontend success redirect is informational only. Backend payment and order state is authoritative.

## Access and Security

Identity returns a user response from registration and a JWT-bearing authentication response from login. Spring Security maps JWT roles to administrator or customer authorities. Customer order, entitlement, and library access is resolved from the authenticated subject and checked in application services.

Library downloads return the existing `{ url }` response shape and use expiring MinIO presigned URLs. The centralized application error shape is also used for authentication and authorization failures.

## Known Operational Preconditions

The full integration suite requires Docker for PostgreSQL Testcontainers. Production operation still requires replacement of development credentials, managed secret storage, restricted actuator exposure, and deployment-specific infrastructure configuration.
