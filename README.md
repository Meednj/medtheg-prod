# Med The G Prod

Med The G Prod is a digital music marketplace under active development. The
backend provides the core catalog, identity, commerce, entitlement, and asset
library capabilities. The frontend is currently the Vite/React starter screen
and has not yet been connected to the backend API.

## Repository Layout

```text
backend/          Spring Boot API and modular-monolith business modules
frontend/         React 19 + TypeScript + Vite application
infrastructure/   MinIO image and monitoring configuration
docs/             Architecture notes and architecture decision records
```

## Technology Stack

- Java 21 and Spring Boot 4.1.1
- PostgreSQL 17 with Spring Data JPA and Flyway migrations
- Redis 8, configured for local development
- Stripe Checkout and signed webhook confirmation
- MinIO for object storage and signed asset download URLs
- Prometheus metrics and Grafana dashboards
- React 19, TypeScript 6, Vite 8, and React Router
- Testcontainers-backed PostgreSQL integration tests

## Local Development

Start the local dependencies from the repository root:

```bash
docker compose up -d
```

Run the backend:

```bash
cd backend
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

Run the frontend in a second terminal:

```bash
cd frontend
npm install
npm run dev
```

The backend uses PostgreSQL at `localhost:5432`, Redis at `localhost:6379`,
and MinIO at `localhost:9000` by default. Stripe secrets are supplied through
`STRIPE_SECRET_KEY` and `STRIPE_WEBHOOK_SECRET`. See
`backend/src/main/resources/application.yml` for the complete local
configuration surface.

## Useful URLs

| Resource            | URL                                     |
| ------------------- | --------------------------------------- |
| Frontend dev server | `http://localhost:5173`                 |
| Swagger UI          | `http://localhost:8080/swagger-ui.html` |
| OpenAPI document    | `http://localhost:8080/v3/api-docs`     |
| Health              | `http://localhost:8080/actuator/health` |
| Prometheus          | `http://localhost:9090`                 |
| Grafana             | `http://localhost:3000`                 |
| MinIO console       | `http://localhost:9001`                 |

## Verification

Backend tests can be run with:

```bash
cd backend
./mvnw test
```

Frontend checks can be run with:

```bash
cd frontend
npm run lint
npm run build
```

## Documentation

The architecture documentation is in [docs/architecture](docs/architecture).
The modular-monolith decision is recorded in
[ADR-001](docs/adr/001-modular-monolith.md). The documentation describes the
implemented state as of the current working tree; planned work is labelled as
future work.
