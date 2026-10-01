# Database Architecture

## Database and Migration Strategy

The application uses PostgreSQL 17 in local development. Flyway applies the
versioned SQL migrations in
`backend/src/main/resources/db/migration`. Hibernate is configured with
`ddl-auto: validate`, which means the application validates the schema but does
not create or alter tables at runtime.

## Current Migrations

| Migration | Purpose                            |
| --------- | ---------------------------------- |
| `V1`      | Products                           |
| `V2`      | Product categories                 |
| `V3`      | Product digital assets             |
| `V4`      | Users                              |
| `V5`      | Orders and order items             |
| `V6`      | Payments                           |
| `V7`      | Stripe Checkout session identifier |
| `V8`      | Entitlements                       |

## Tables and Ownership

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

The `library` module does not currently own a separate table. It composes
entitlements with catalog assets and uses the storage adapter to produce
download URLs.

## Important Relationships

- `order_items.order_id` references `orders.id` and cascades on deletion.
- `entitlements.order_id` references `orders.id` and cascades on deletion.
- `entitlements` has a unique constraint on `(customer_id, product_id)`.
- Payments have a unique Checkout Session identifier, preventing duplicate
  application payment records for one Stripe session.
- Product and customer identifiers crossing module boundaries are stored as
  application-level UUIDs rather than cross-module foreign keys.

## Data Integrity

The schema includes checks for non-negative prices and quantities of at least
one. Timestamps use `TIMESTAMP WITH TIME ZONE`. Product, order, payment, and
entitlement state transitions are enforced by domain models as well as stored
status values.

## Local Services

The root Docker Compose configuration provisions PostgreSQL with database
`medtheg_prod`, user `medtheg`, and a persistent `postgres_data` volume. These
credentials are development defaults and must be replaced for shared or
production environments.
