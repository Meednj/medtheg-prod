# Sales Module Architecture

## 1. Purpose

The Sales module is responsible for the commercial lifecycle of customer purchases.

Its current responsibilities are:

* Creating customer orders
* Calculating order totals
* Preserving historical product pricing
* Managing order lifecycle
* Creating payment sessions
* Persisting payment information
* Integrating with Stripe
* Processing Stripe payment webhooks
* Marking orders as paid after verified payment

The module does **not** currently manage:

* Customer product ownership
* Digital downloads
* Entitlements
* Refund processing
* Coupons or discounts
* Subscriptions
* Tax calculation
* Invoicing

Those capabilities may be introduced as separate modules or application capabilities later.

---

## 2. Architectural Approach

The Sales module follows the project's overall **Modular Monolith + Hexagonal Architecture** approach.

```text
                    SALES MODULE
                         │
          ┌──────────────┴──────────────┐
          │                             │
       Domain                       Application
          │                             │
    ┌─────┴─────┐                 ┌─────┴─────┐
    │           │                 │           │
  Order      Payment          Use Cases    Services
    │           │                 │           │
    └─────┬─────┘                 └─────┬─────┘
          │                             │
          └──────────────┬──────────────┘
                         │
                  Infrastructure
                         │
          ┌──────────────┼──────────────┐
          │              │              │
      PostgreSQL       Stripe         Web API
```

The domain and application layers do not directly depend on Stripe or PostgreSQL.

External infrastructure is accessed through ports and adapters.

---

## 3. Module Structure

```text
sales/
├── domain/
│   ├── model/
│   │   ├── Order.java
│   │   ├── OrderId.java
│   │   ├── OrderItem.java
│   │   ├── OrderItemId.java
│   │   ├── OrderStatus.java
│   │   ├── OrderPage.java
│   │   ├── Payment.java
│   │   └── PaymentStatus.java
│   │
│   └── repository/
│       ├── OrderRepository.java
│       └── PaymentRepository.java
│
├── application/
│   ├── port/
│   │   └── PaymentGateway.java
│   │
│   ├── usecase/
│   │   ├── CreateOrderUseCase.java
│   │   ├── GetOrderUseCase.java
│   │   ├── ListCustomerOrdersUseCase.java
│   │   ├── CreatePaymentUseCase.java
│   │   └── MarkOrderAsPaidUseCase.java
│   │
│   └── service/
│       ├── CreateOrderService.java
│       ├── GetOrderService.java
│       ├── ListCustomerOrdersService.java
│       ├── CreatePaymentService.java
│       ├── MarkOrderAsPaidService.java
│       ├── OrderNotFoundException.java
│       └── OrderAccessDeniedException.java
│
└── infrastructure/
    ├── persistence/
    │   ├── entity/
    │   │   ├── OrderEntity.java
    │   │   ├── OrderItemEntity.java
    │   │   └── PaymentEntity.java
    │   │
    │   ├── mapper/
    │   │   ├── OrderMapper.java
    │   │   └── PaymentMapper.java
    │   │
    │   ├── repository/
    │   │   ├── OrderJpaRepository.java
    │   │   └── PaymentJpaRepository.java
    │   │
    │   ├── OrderPersistenceAdapter.java
    │   └── PaymentPersistenceAdapter.java
    │
    ├── payment/
    │   ├── StripeProperties.java
    │   ├── StripeConfiguration.java
    │   ├── StripePaymentAdapter.java
    │   └── StripeWebhookController.java
    │
    └── web/
        ├── AuthenticatedUser.java
        ├── dto/
        │   ├── CreateOrderRequest.java
        │   ├── OrderResponse.java
        │   └── OrderHistoryResponse.java
        ├── mapper/
        │   └── OrderWebMapper.java
        └── controller/
            └── OrderController.java
```

---

# 4. Domain Model

## 4.1 Order

`Order` is the main aggregate of the Sales module.

An order contains:

* Order ID
* Customer ID
* Order items
* Status
* Creation timestamp
* Payment timestamp

An order starts in the `PENDING` state.

```text
PENDING
   │
   ├── markAsPaid()
   │       ↓
   │     PAID
   │
   └── cancel()
           ↓
        CANCELLED

PAID
 │
 └── refund()
         ↓
      REFUNDED
```

The domain object controls valid state transitions instead of allowing application services to modify the status arbitrarily.

---

## 4.2 OrderStatus

Current statuses:

```text
PENDING
PAID
CANCELLED
REFUNDED
```

The current lifecycle rules are:

| Current status | Operation | Result    |
| -------------- | --------- | --------- |
| PENDING        | Pay       | PAID      |
| PENDING        | Cancel    | CANCELLED |
| PAID           | Refund    | REFUNDED  |
| PAID           | Pay       | Rejected  |
| CANCELLED      | Pay       | Rejected  |
| REFUNDED       | Pay       | Rejected  |

These rules are enforced by the domain model.

---

## 4.3 OrderItem

An `OrderItem` represents a purchased product inside an order.

It stores:

* Item ID
* Product ID
* Product title
* Unit price
* Quantity

The unit price is copied from the Catalog when the order is created.

This is intentional.

For example:

```text
Catalog

Beat X
Current price: €19.99
```

Customer purchases it:

```text
OrderItem

Beat X
Historical price: €19.99
```

Later, the Catalog price changes:

```text
Beat X
Current price: €29.99
```

The existing order remains:

```text
Beat X
Purchased for: €19.99
```

Therefore, historical orders are not affected by future Catalog price changes.

---

## 4.4 Order Total

The order total is calculated from its items:

```text
Order total =
Σ (unit price × quantity)
```

The calculation belongs to the domain rather than the controller.

---

# 5. Creating Orders

The order creation flow is:

```text
Authenticated Customer
        │
        ▼
POST /api/orders
        │
        ▼
CreateOrderUseCase
        │
        ▼
CreateOrderService
        │
        ├── Validate request
        ├── Load products from Catalog
        ├── Verify products are PUBLISHED
        ├── Read current product prices
        ├── Create OrderItems
        └── Save Order
```

The customer ID is obtained from the authenticated JWT.

The client cannot choose another customer's ID.

---

# 6. Catalog Integration

Sales depends on the Catalog through the application-level use case:

```text
CreateOrderService
       │
       ▼
GetProductUseCase
       │
       ▼
Catalog
```

When creating an order, the Sales module retrieves the product from Catalog and uses:

* Product ID
* Product title
* Product price
* Product publication status

Only published products can currently be purchased.

The Sales module does not access Catalog database tables directly.

---

# 7. Order Access Control

Customers can only access their own orders.

The authenticated user is extracted from the JWT:

```text
JWT
 │
 └── subject = User UUID
          │
          ▼
   AuthenticatedUser
          │
          ▼
        UserId
```

When retrieving an order, the application verifies:

```text
order.customerId == authenticatedUser.id
```

An access violation results in an `OrderAccessDeniedException`.

This prevents customers from retrieving another customer's order by simply changing an order UUID.

---

# 8. Payment Model

Payments are represented separately from orders.

A `Payment` contains:

* Payment ID
* Order ID
* Provider
* Provider payment ID
* Checkout session ID
* Checkout URL
* Payment status
* Creation timestamp
* Completion timestamp

Current statuses:

```text
CREATED
COMPLETED
FAILED
```

The relationship is:

```text
Order 1 ───────── 1 Payment
```

The current implementation creates one payment record for an order.

---

# 9. PaymentGateway Port

The application does not directly depend on Stripe.

Instead, it defines:

```java
PaymentGateway
```

The port exposes a payment-session abstraction:

```text
PaymentGateway
       │
       ▼
PaymentSession
       │
       ├── paymentId
       ├── checkoutSessionId
       └── checkoutUrl
```

This allows the application to work with a payment provider without depending on its SDK.

The current implementation uses Stripe.

Future providers could be added through additional adapters without changing the core payment use case.

---

# 10. Stripe Adapter

Stripe is implemented as an infrastructure adapter:

```text
application
     │
     ▼
PaymentGateway
     ▲
     │
StripePaymentAdapter
     │
     ▼
Stripe API
```

`StripePaymentAdapter` is responsible for translating the application's payment request into a Stripe Checkout Session.

The Stripe session contains:

* Order line items
* Product titles
* Historical order prices
* Quantities
* Currency
* Success URL
* Cancel URL
* Order ID metadata

The adapter uses the prices stored in `OrderItem`.

It does not query Catalog again.

---

# 11. Payment Creation Flow

The current payment flow is:

```text
Customer
   │
   ▼
POST /api/orders/{orderId}/payment
   │
   ▼
CreatePaymentUseCase
   │
   ▼
CreatePaymentService
   │
   ├── Load Order
   ├── Verify ownership
   ├── Verify PENDING status
   ├── Check existing payment
   │
   └── PaymentGateway
          │
          ▼
   StripePaymentAdapter
          │
          ▼
   Stripe Checkout Session
          │
          ▼
   Payment CREATED
```

The API returns the checkout URL so the frontend can redirect the customer to Stripe Checkout.

---

# 12. Payment Idempotency

Payment creation checks whether an existing payment already exists for the order.

This prevents the application from unnecessarily creating multiple payment sessions for the same order.

Payment persistence also contains database-level uniqueness constraints for:

```text
(provider, provider_payment_id)
checkout_session_id
```

This provides an additional layer of protection against duplicate payment records.

---

# 13. Stripe Webhook

The application does not consider the customer redirect to the success page as proof of payment.

Payment confirmation comes from Stripe's webhook.

```text
Stripe
  │
  │ checkout.session.completed
  ▼
StripeWebhookController
  │
  ├── Verify Stripe signature
  ├── Deserialize Checkout Session
  ├── Verify payment_status = paid
  ├── Read orderId metadata
  └── MarkOrderAsPaidUseCase
```

This is important because the frontend success URL can be accessed without completing a legitimate payment.

The Stripe webhook is therefore the authoritative payment confirmation mechanism.

---

# 14. Local Stripe Webhook Development

During local development, Stripe CLI forwards Stripe events to the local Spring Boot application.

```text
Stripe
   │
   ▼
Stripe CLI
   │
   ▼
localhost:8080/api/webhooks/stripe
```

The webhook secret generated by Stripe CLI is supplied through:

```text
STRIPE_WEBHOOK_SECRET
```

The Stripe secret key is supplied through:

```text
STRIPE_SECRET_KEY
```

Secrets must not be committed to Git.

---

# 15. Order Payment Confirmation

When Stripe confirms a successful Checkout Session:

```text
checkout.session.completed
        │
        ▼
payment_status = paid
        │
        ▼
Find Payment by checkoutSessionId
        │
        ▼
Verify Payment belongs to Order
        │
        ▼
Payment → COMPLETED
        │
        ▼
Order → PAID
```

The completion timestamp is recorded for both payment/order processing as appropriate.

The operation is transactional at the application-service level.

---

# 16. Webhook Idempotency

Stripe webhooks may be delivered more than once.

The application therefore treats payment completion as an idempotent operation.

Conceptually:

```text
Webhook #1
    ↓
Payment CREATED
    ↓
Payment COMPLETED
    ↓
Order PAID


Webhook #2
    ↓
Payment already COMPLETED
    ↓
Order already PAID
    ↓
No duplicate business operation
```

The domain model also protects the payment state transition:

```java
if (status == PaymentStatus.COMPLETED) {
    return;
}
```

This prevents repeated webhook deliveries from repeatedly applying the same transition.

---

# 17. Persistence Architecture

Sales persistence follows the same ports-and-adapters model used by the other modules.

```text
Domain Repository
       │
       ▼
Persistence Adapter
       │
       ▼
Spring Data JPA Repository
       │
       ▼
JPA Entity
       │
       ▼
PostgreSQL
```

There are separate persistence paths for Orders and Payments.

---

# 18. Database Tables

Current Sales tables:

```text
orders
order_items
payments
```

### `orders`

Stores:

* ID
* Customer ID
* Status
* Creation timestamp
* Payment timestamp

### `order_items`

Stores:

* ID
* Order ID
* Product ID
* Historical product title
* Unit price
* Currency
* Quantity

### `payments`

Stores:

* ID
* Order ID
* Provider
* Provider payment ID
* Checkout session ID
* Checkout URL
* Status
* Creation timestamp
* Completion timestamp

---

# 19. Database Module Boundaries

The Sales database tables intentionally do not use foreign keys to the Catalog or Identity modules.

For example:

```text
orders.customer_id
```

does not have a PostgreSQL foreign key to:

```text
users.id
```

Likewise:

```text
order_items.product_id
```

does not have a PostgreSQL foreign key to:

```text
products.id
```

The relationship is maintained at the application/domain level.

This reduces coupling between modules and keeps the modular monolith easier to evolve.

The Payment table does have a foreign key to its own Sales aggregate:

```text
payments.order_id → orders.id
```

because Order and Payment belong to the same module.

---

# 20. REST API

## Create Order

```http
POST /api/orders
```

Requires authentication.

Example request:

```json
{
  "items": [
    {
      "productId": "PRODUCT_UUID",
      "quantity": 1
    }
  ]
}
```

---

## Get Order

```http
GET /api/orders/{orderId}
```

Requires authentication.

The authenticated customer must own the order.

---

## Order History

```http
GET /api/orders
```

Requires authentication.

Returns the authenticated customer's orders with pagination.

---

## Create Payment

```http
POST /api/orders/{orderId}/payment
```

Requires authentication.

The authenticated customer must own the order.

Returns the Stripe checkout information.

---

## Stripe Webhook

```http
POST /api/webhooks/stripe
```

This endpoint is called by Stripe rather than by the frontend.

It validates the Stripe signature before processing the event.

---

# 21. Security Rules

Current Sales security behavior:

```text
/api/orders/**
        ↓
Authenticated users only
```

The customer ID is always derived from the authenticated JWT.

The frontend is never trusted to provide the customer identity.

The Stripe webhook uses Stripe's signature verification mechanism rather than application authentication.

---

# 22. Transaction Boundaries

Application services are responsible for transactional operations.

Examples:

```text
CreateOrderService
        ↓
@Transactional
```

```text
CreatePaymentService
        ↓
@Transactional
```

```text
MarkOrderAsPaidService
        ↓
@Transactional
```

The goal is to ensure that related persistence operations are committed consistently.

---

# 23. Current Payment State

The current implementation supports:

```text
Create Order
      ↓
PENDING
      ↓
Create Stripe Checkout
      ↓
Payment CREATED
      ↓
Customer pays
      ↓
Stripe webhook
      ↓
Payment COMPLETED
      ↓
Order PAID
```

The following payment capabilities are intentionally deferred:

* Refund API integration
* Payment retry workflow
* Failed-payment recovery
* Payment expiration
* Multiple payment attempts per order
* Stripe event persistence
* Stripe event replay handling
* Payment reconciliation

These can be added when the core commerce workflow requires them.

---

# 24. Future Domain Events

The Sales module will eventually publish domain/application events when important business transitions occur.

The primary planned event is:

```text
OrderPaidEvent
```

The intended flow is:

```text
Stripe Webhook
       ↓
Mark Order as Paid
       ↓
OrderPaidEvent
       ↓
┌──────┴───────────┬───────────────┐
│                  │               │
Entitlement     Digital         Email
               Delivery
│                  │               │
Ownership       Download       Confirmation
```

This will prevent the Stripe integration from becoming directly coupled to every business action triggered by a successful purchase.

---

# 25. Planned Entitlement Integration

Customer ownership is deliberately not part of the Sales module.

The future Entitlement module will consume the successful-purchase event:

```text
Sales
  │
  │ OrderPaidEvent
  ▼
Entitlement
  │
  ▼
Customer owns Product
```

For an order containing multiple products:

```text
Order PAID
 ├── Beat A
 ├── Beat B
 └── Drum Kit C
       │
       ▼
Entitlements
 ├── Customer → Beat A
 ├── Customer → Beat B
 └── Customer → Drum Kit C
```

This separation allows ownership rules to evolve independently from payment processing.

---

# 26. Architectural Rules

The Sales module follows these rules:

1. Domain objects must not depend on Stripe SDK classes.
2. Domain objects must not depend on Spring Data or JPA.
3. Stripe integration must remain inside infrastructure.
4. Payment providers must be accessed through `PaymentGateway`.
5. Database access must go through repository ports.
6. Customers must never provide their own customer ID for order operations.
7. Order ownership must be verified server-side.
8. Payment confirmation must come from a verified Stripe webhook.
9. The frontend success URL must never be treated as proof of payment.
10. Historical order prices must not be read from the current Catalog after order creation.
11. Payment completion must be idempotent.
12. Cross-module database foreign keys should be avoided unless there is a strong architectural reason.
13. Business state transitions must be enforced by the domain model.
14. Secrets such as Stripe API keys and webhook secrets must never be committed to source control.

---

# 27. Architectural Patterns Used

The Sales module currently demonstrates several important design patterns:

### Repository Pattern

```text
OrderRepository
PaymentRepository
```

Abstracts persistence from the domain.

### Ports & Adapters

```text
PaymentGateway
       ↓
StripePaymentAdapter
```

Keeps external payment infrastructure isolated.

### Adapter Pattern

`StripePaymentAdapter` translates the application's payment abstraction into Stripe's API.

### Domain Model

`Order` and `Payment` encapsulate their own business state and transitions.

### Dependency Inversion

Application/domain logic depends on abstractions rather than concrete infrastructure implementations.

### Idempotency

Payment completion can safely handle repeated webhook delivery.

---

# 28. Testing Strategy

The Sales module uses a pragmatic testing strategy.

Current tests cover the most important business and integration behavior, including:

* Order creation
* Authentication requirements
* Request validation
* Payment service behavior
* Order ownership
* Invalid order states
* Application context startup

Integration testing uses Spring Boot and Testcontainers where database behavior needs to be verified.

The project intentionally avoids creating a large number of low-value tests for simple getters, DTOs, or framework-generated behavior.

The priority is testing business rules and important integration boundaries.

---

# 29. Current Limitations

The current implementation is suitable for the development/MVP stage but still has production-hardening work remaining.

Known limitations include:

* Stripe webhook event storage is not yet implemented.
* Payment attempts are currently simplified to one payment per order.
* Refund processing is not implemented.
* Failed-payment recovery is not implemented.
* Stripe reconciliation is not implemented.
* Entitlements are not yet connected.
* Digital asset delivery is not yet implemented.
* Production Stripe secret management is not yet configured.
* Production webhook infrastructure is not yet deployed.

These limitations are intentional because the project is being developed incrementally.

---

# 30. Next Evolution

The next major Sales-related evolution is to introduce domain/application events:

```text
Order PAID
   ↓
OrderPaidEvent
   ↓
Entitlement Module
   ↓
Digital Product Ownership
```

This will connect the current payment system to the digital-product delivery architecture while keeping the Sales module independent from ownership and download concerns.
