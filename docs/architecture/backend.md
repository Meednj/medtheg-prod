# Backend Architecture

## Runtime

The backend is a Spring Boot 4.1.1 application running on Java 21. It is
packaged and deployed as one modular-monolith application. The main entry point
is `BackendApplication` under `com.medthegprod.backend`.

## Module Boundaries

The backend currently contains five business modules:

| Module      | Responsibility                                              |
| ----------- | ----------------------------------------------------------- |
| Identity    | Registration, login, users, roles, and account status       |
| Catalog     | Products, categories, lifecycle, and digital assets         |
| Sales       | Orders, payments, Stripe Checkout, and payment confirmation |
| Entitlement | Ownership granted after a paid order                        |
| Library     | Customer library views and time-limited asset download URLs |

Each module is organized around domain models, application use cases/services,
repository or external-service ports, and infrastructure adapters. JPA
entities, REST DTOs, and provider clients remain in infrastructure packages.

## Request Flow

```text
HTTP request
	↓
REST controller and DTO validation
	↓
Application service / use case
	↓
Domain model and repository ports
	↓
Persistence or external-service adapter
	↓
HTTP response
```

Spring Security is applied at the HTTP boundary. Authentication and
authorization details are documented in [security.md](security.md).

## Cross-Module Flow

Sales publishes `OrderPaidEvent` after a verified Stripe payment changes the
payment and order state. Entitlement listens after transaction commit and
creates ownership records. Library reads those entitlements before requesting a
signed object-storage URL. This keeps payment processing separate from access
and delivery concerns.

## Persistence Rules

Application code depends on repository ports. Spring Data repositories and JPA
entities are hidden behind persistence adapters. Flyway owns schema changes and
Hibernate runs with `ddl-auto: validate`, so runtime startup checks the schema
without generating or modifying it.

## API Surface

The REST API currently covers:

- `/api/auth/register` and `/api/auth/login`
- `/api/products` and product asset operations
- `/api/products/admin/{id}` for administrator lifecycle reads
- `/api/products/{productId}/assets/{assetId}/preview` for expiring previews
- `/api/orders` and payment-session creation
- `/api/webhooks/stripe`
- customer entitlements and library asset operations
- `/actuator/health`, readiness/liveness probes, and Prometheus metrics

OpenAPI is available at `/v3/api-docs` with Swagger UI at
`/swagger-ui.html`.

The complete implemented request and response contract is maintained in
[`docs/api-contract.md`](../api-contract.md).

## Testing Approach

The backend combines domain unit tests, application service tests, Spring web
integration tests, persistence integration tests, and event/observability
tests. PostgreSQL integration tests use Testcontainers so persistence behavior
is checked against a real PostgreSQL instance.
