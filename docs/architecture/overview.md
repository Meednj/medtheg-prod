# Architecture Overview

## 1. Architecture Style

`medtheg-prod` is designed as a **modular monolith** using **Hexagonal Architecture / Ports and Adapters** with DDD-inspired domain boundaries.

The application is deployed as a single Spring Boot application while maintaining clear internal business modules.

```text
                         medtheg-prod
                              │
                    ┌─────────┴─────────┐
                    │   Spring Boot     │
                    │   Application     │
                    └─────────┬─────────┘
                              │
       ┌──────────────────────┼──────────────────────┐
       │                      │                      │
       ▼                      ▼                      ▼
   Identity                Catalog                 Sales
       │                      │                      │
       │                      │              ┌───────┴────────┐
       │                      │              │                │
       │                      │            Orders          Payments
       │                      │                               │
       │                      │                            Stripe
       │                      │
       └──────────────────────┼───────────────────────────────┘
                              │
                              ▼
                         Entitlement
```

---

## 2. Current Business Modules

The backend currently contains four major business modules:

### Identity

Responsible for:

* User registration
* Authentication
* Password hashing
* JWT generation
* Roles
* User status
* Authorization integration

### Catalog

Responsible for:

* Products
* Product types
* Categories
* Prices
* Product lifecycle
* Digital assets
* Product publication

### Sales

Responsible for:

* Orders
* Order items
* Historical pricing
* Payment records
* Stripe Checkout
* Payment confirmation
* Order lifecycle

### Entitlement

Responsible for:

* Customer ownership
* Access rights to purchased products
* Entitlement lifecycle
* Customer product library

---

## 3. Module Interaction

The main purchase flow is event-driven.

```text
                    CUSTOMER
                        │
                        ▼
                     Catalog
                        │
                        ▼
                      Sales
                        │
                  Create Order
                        │
                        ▼
                    PENDING
                        │
                        ▼
                  Stripe Checkout
                        │
                        ▼
                     Payment
                        │
                        ▼
                  Stripe Webhook
                        │
                        ▼
                     PAID
                        │
                        ▼
                OrderPaidEvent
                        │
                        ▼
                  Entitlement
                        │
                        ▼
               Customer Ownership
```

Sales does not directly invoke Entitlement.

Instead, Sales publishes an `OrderPaidEvent`.

---

## 4. OrderPaidEvent

`OrderPaidEvent` represents the business event generated when a payment has been successfully confirmed.

The event contains:

```text
OrderPaidEvent
├── orderId
├── customerId
└── productIds[]
```

The event is published only after:

```text
Payment → COMPLETED
Order   → PAID
```

The event can then be consumed by other modules.

```text
                    OrderPaidEvent
                          │
            ┌─────────────┼─────────────┐
            │             │             │
            ▼             ▼             ▼
       Entitlement      Email        Analytics
            │
            ▼
     Customer Ownership
```

Only the Entitlement consumer is currently implemented.

---

## 5. Dependency Direction

The project follows a dependency direction from business rules toward infrastructure.

```text
Infrastructure
      │
      ▼
Application
      │
      ▼
Domain
```

The domain must not depend on:

* PostgreSQL
* JPA
* Redis
* Stripe
* HTTP
* Spring framework APIs

Infrastructure adapters implement the ports defined by the application/domain layers.

---

## 6. Persistence Architecture

Each module owns its persistence implementation.

```text
Domain Repository Port
        │
        ▼
Persistence Adapter
        │
        ▼
Spring Data Repository
        │
        ▼
JPA Entity
        │
        ▼
PostgreSQL
```

Current database areas include:

```text
Identity
└── users

Catalog
├── products
├── product_categories
└── product_assets

Sales
├── orders
├── order_items
└── payments

Entitlement
└── entitlements
```

Cross-module database coupling is intentionally minimized.

For example, Sales and Entitlement use application-level identifiers rather than relying on PostgreSQL foreign keys to tables owned by other modules.

---

## 7. Payment Architecture

Payment providers are isolated behind an application port.

```text
CreatePaymentService
        │
        ▼
PaymentGateway
        │
        ▼
StripePaymentAdapter
        │
        ▼
Stripe API
```

This allows the payment provider implementation to change without changing the core Sales domain.

The current provider is Stripe.

---

## 8. Event Infrastructure

Application events use a port-and-adapter approach.

```text
Sales Application
        │
        ▼
EventPublisher
        │
        ▼
SpringEventPublisher
        │
        ▼
Spring ApplicationEventPublisher
        │
        ▼
Event Listener
```

This keeps event publication independent from the specific event technology used by the infrastructure.

---

## 9. Entitlement Architecture

The Entitlement module represents customer access rights.

Its core relationship is:

```text
Customer
    │
    │ owns
    ▼
Product
```

The ownership is represented by an `Entitlement`.

```text
Entitlement
├── id
├── customerId
├── productId
├── orderId
├── status
├── grantedAt
└── revokedAt
```

The originating order is stored for traceability.

---

## 10. Purchase-to-Ownership Flow

The complete current business flow is:

```text
Customer
   │
   ▼
Create Order
   │
   ▼
Order PENDING
   │
   ▼
Create Stripe Payment
   │
   ▼
Payment CREATED
   │
   ▼
Customer Pays
   │
   ▼
Stripe Webhook
   │
   ▼
Verify Signature
   │
   ▼
Payment COMPLETED
   │
   ▼
Order PAID
   │
   ▼
OrderPaidEvent
   │
   ▼
Grant Entitlement
   │
   ▼
Customer Owns Product
```

This provides the backend foundation for the future **My Library** and secure digital-delivery features.

---

## 11. Why Modular Monolith?

The project intentionally uses a modular monolith instead of microservices at this stage.

Benefits include:

* Simple deployment
* Simple local development
* Single database infrastructure
* Lower operational complexity
* Clear business boundaries
* Easier debugging
* Ability to introduce asynchronous processing later
* Possibility of extracting modules into services if future requirements justify it

The architecture therefore separates business responsibilities without introducing unnecessary distributed-system complexity.

---

## 12. Current Architectural State

Current modules:

```text
┌──────────────────────────────────────────────┐
│              Spring Boot App                 │
│                                              │
│  ┌──────────┐ ┌─────────┐ ┌────────┐       │
│  │ Identity │ │ Catalog │ │ Sales  │       │
│  └──────────┘ └─────────┘ └───┬────┘       │
│                                │            │
│                         OrderPaidEvent      │
│                                │            │
│                                ▼            │
│                         ┌────────────┐       │
│                         │Entitlement │       │
│                         └────────────┘       │
│                                              │
└──────────────────────────────────────────────┘
```

The backend now supports the core path from:

**authentication → catalog → order → payment → confirmed purchase → customer ownership.**

The next major capability is exposing and enriching customer ownership through the My Library API and eventually connecting it to secure digital asset delivery.
