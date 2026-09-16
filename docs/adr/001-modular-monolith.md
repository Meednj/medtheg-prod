# ADR-001: Use a Modular Monolith

## Status

Accepted

## Context

Med The G Prod is a new application that requires several business
domains including catalog, orders, payments, licensing and digital
delivery.

Starting directly with microservices would introduce unnecessary
distributed-system complexity at this stage.

## Decision

The backend will initially be implemented as a modular monolith.

Each business domain will have explicit boundaries and will follow
Hexagonal Architecture principles.

The modules will be designed so that they can potentially be extracted
into independent services in the future if there is a real need.

## Consequences

### Positive

- Simpler local development
- Simpler deployment
- Easier transactions
- Lower operational complexity
- Clear business boundaries
- Easier testing
- Allows future extraction of modules if necessary

### Negative

- Requires discipline to maintain module boundaries
- Modules share the same application runtime
- Scaling individual modules independently is not possible initially