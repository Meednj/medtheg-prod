# Med The G Prod — Architecture Overview

## Architecture Style

Med The G Prod is initially designed as a modular monolith.

The backend uses Hexagonal Architecture and DDD-inspired domain organization.

The frontend is built with React and TypeScript.

## High-Level Architecture

```text
React + TypeScript
        |
        | HTTPS / REST
        v
Spring Boot API
        |
        +----------------------+
        |                      |
        v                      v
   PostgreSQL                Redis
        |
        v
 Object Storage

External integrations:
- Stripe
- Email provider
```
## Bounded Contexts

The application is organized around business capabilities rather than
technical layers.

### Identity

Responsible for users, authentication and authorization.

### Catalog

Responsible for products available for sale, including beats,
sample packs, courses and bundles.

### Shopping

Responsible for customer carts and cart items before checkout.

### Sales

Responsible for checkout, orders and the purchase lifecycle.

### Licensing & Entitlements

Responsible for defining and granting the rights associated with
purchased products.

### Digital Delivery

Responsible for secure delivery and download of purchased digital
assets.

### Learning

Responsible for course-specific behavior such as lessons,
enrollments and learning progress.


## Core Business Flow

```text
Catalog
   ↓
Shopping
   ↓
Sales
   ↓
Payment
   ↓
OrderPaidEvent
   ↓
Licensing & Entitlements
   ↓
Digital Delivery
