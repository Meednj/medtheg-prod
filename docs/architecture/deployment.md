# Deployment and Operations

## Local Runtime

The project currently runs as a Spring Boot backend plus a Vite frontend. The
root Docker Compose file provides the backend's local dependencies:

```text
PostgreSQL 17  localhost:5432
Redis 8        localhost:6379
MinIO          localhost:9000 / console localhost:9001
Prometheus     localhost:9090
Grafana        localhost:3000
```

Persistent named volumes are used for PostgreSQL, Redis, MinIO, Prometheus, and
Grafana data. The backend and frontend are run from their respective project
directories during development.

## Backend Configuration

Spring configuration is in `backend/src/main/resources/application.yml`.
Database and local service endpoints have development defaults. The following
values are intended to come from the environment outside local development:

- `STRIPE_SECRET_KEY`
- `STRIPE_WEBHOOK_SECRET`
- `STRIPE_SUCCESS_URL`
- `STRIPE_CANCEL_URL`
- `MINIO_ENDPOINT`
- `MINIO_ACCESS_KEY`
- `MINIO_SECRET_KEY`
- `MINIO_BUCKET`

## Observability

The backend exposes health information, liveness/readiness probes, and
Prometheus metrics through Spring Boot Actuator:

- `/actuator/health`
- `/actuator/health/liveness`
- `/actuator/health/readiness`
- `/actuator/prometheus`

Prometheus is configured under `infrastructure/monitoring/prometheus`, while
Grafana provisioning and dashboards are under
`infrastructure/monitoring/grafana`.

Each HTTP request receives an `X-Request-ID`. It is stored in SLF4J MDC and
included in request logs with method, path, status, and duration. Sensitive
request data is excluded.

The backend also exposes the public catalog preview endpoint, which generates
short-lived object-storage URLs. Protected library downloads remain
customer-authenticated and entitlement-checked.

## Containerization Status

There is currently infrastructure for the MinIO image and local dependency
orchestration. A production backend image, Kubernetes manifests, cloud
infrastructure, and CI/CD pipeline are not yet part of this repository.

## Operational Considerations

Before a shared or production deployment, replace development credentials,
restrict actuator access, configure managed PostgreSQL/Redis/object storage,
provide a durable secret-management solution, and define backup and recovery
procedures for persistent data.

## Observability

The backend uses a lightweight request-correlation mechanism for HTTP
diagnostics.

Each incoming request receives an `X-Request-ID`. If the client provides
a valid request ID, it is reused; otherwise the backend generates a UUID.

The request ID is stored in SLF4J MDC for the lifetime of the request and
removed after processing. This allows application logs to include the
correlation ID without passing HTTP-specific data through application or
domain services.

HTTP requests are logged with:

- HTTP method
- request path
- response status
- execution duration
- request ID

Sensitive information such as authorization headers, JWTs, passwords,
request bodies, payment credentials, and storage credentials is not logged.

Spring Boot Actuator exposes health information and Kubernetes-compatible
liveness and readiness probes:

- `/actuator/health`
- `/actuator/health/liveness`
- `/actuator/health/readiness`

Observability is implemented in the infrastructure layer and does not
introduce HTTP or logging dependencies into the domain layer.
