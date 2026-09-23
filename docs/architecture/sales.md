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
* Publishing an `OrderPaidEvent` after a successful payment

The module does **not** manage customer ownership or digital asset delivery directly.

Those responsibilities belong to other modules.

---

## 2. Module Structure

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
│   ├── event/
│   │   └── OrderPaidEvent.java
│   │
│   ├── port/
│   │   ├── EventPublisher.java
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
    ├── payment/
    │   ├── StripeProperties.java
    │   ├── StripeConfiguration.java
    │   ├── StripePaymentAdapter.java
    │   └── StripeWebhookController.java
    │
    └── web/
```

---

## 3. Order Payment Lifecycle

The current payment lifecycle is:

```text
Order PENDING
      │
      ▼
Create Stripe Checkout Session
      │
      ▼
Payment CREATED
      │
      ▼
Customer completes payment
      │
      ▼
Stripe checkout.session.completed
      │
      ▼
StripeWebhookController
      │
      ├── Verify webhook signature
      ├── Verify payment_status = paid
      ├── Read orderId metadata
      └── Find Payment by checkoutSessionId
      │
      ▼
MarkOrderAsPaidService
      │
      ├── Payment → COMPLETED
      └── Order → PAID
      │
      ▼
OrderPaidEvent
```

The frontend success URL is not considered proof of payment.

Stripe's signed webhook is the authoritative confirmation mechanism.

---

## 4. OrderPaidEvent

After an order has been successfully marked as paid, the Sales module publishes:

```java
OrderPaidEvent
```

The event contains:

* `orderId`
* `customerId`
* `productIds`

Conceptually:

```text
OrderPaidEvent
├── orderId
├── customerId
└── productIds[]
```

For example:

```text
Order #123
├── Beat A
├── Beat B
└── Drum Kit C

        ↓

OrderPaidEvent
├── orderId = 123
├── customerId = customer-456
└── productIds = [beat-A, beat-B, kit-C]
```

The event represents a business fact:

> The customer successfully paid for the products contained in this order.

---

## 5. Event Publisher Port

Sales publishes events through the application port:

```text
EventPublisher
```

The Sales application layer does not directly depend on Spring's event system.

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
```

This preserves dependency inversion and keeps the application layer independent of the concrete event infrastructure.

---

## 6. Event-Driven Module Communication

The Sales module does not directly call the Entitlement module.

Instead:

```text
Sales
  │
  │ OrderPaidEvent
  ▼
Event infrastructure
  │
  ▼
Entitlement
```

This avoids coupling the payment workflow to customer ownership logic.

The Sales module only publishes the business event.

The receiving module decides what to do with it.

---

## 7. OrderPaidEvent Processing

The current event flow is:

```text
Stripe
   │
   ▼
StripeWebhookController
   │
   ▼
MarkOrderAsPaidService
   │
   ├── Complete Payment
   ├── Mark Order PAID
   │
   └── Publish OrderPaidEvent
                │
                ▼
       OrderPaidEventListener
                │
                ▼
        GrantEntitlementUseCase
```

The Entitlement module creates one active entitlement for each product contained in the paid order.

---

## 8. Event Idempotency

Stripe may deliver the same webhook more than once.

The payment completion flow is therefore designed to be idempotent.

```text
Webhook #1
    ↓
Payment COMPLETED
Order PAID
    ↓
OrderPaidEvent
    ↓
Entitlement created


Webhook #2
    ↓
Payment already COMPLETED
Order already PAID
    ↓
No duplicate business operation
```

The Entitlement database also enforces:

```text
UNIQUE (customer_id, product_id)
```

This prevents duplicate ownership records for the same customer and product.

---

## 9. Cross-Module Responsibility

The responsibility boundary is:

| Concern              | Module                        |
| -------------------- | ----------------------------- |
| Product catalog      | Catalog                       |
| Order creation       | Sales                         |
| Order lifecycle      | Sales                         |
| Payment              | Sales                         |
| Stripe integration   | Sales infrastructure          |
| Payment confirmation | Sales                         |
| Customer ownership   | Entitlement                   |
| Digital access       | Entitlement / future delivery |
| Digital downloads    | Future delivery capability    |

Sales therefore answers:

> "Did the customer successfully purchase the product?"

Entitlement answers:

> "Does the customer have access to the product?"

---

## 10. Future Event Consumers

`OrderPaidEvent` is intentionally designed to support additional consumers.

Future consumers may include:

```text
OrderPaidEvent
      │
      ├── Entitlement
      │
      ├── Digital Delivery
      │
      ├── Email Notification
      │
      └── Analytics
```

This means new post-purchase capabilities can be added without modifying the Stripe payment implementation.

---

## 11. Architectural Patterns

The Sales module currently demonstrates:

### Repository Pattern

Persistence is abstracted through:

* `OrderRepository`
* `PaymentRepository`

### Ports and Adapters

External payment infrastructure is accessed through:

* `PaymentGateway`
* `StripePaymentAdapter`

Events are abstracted through:

* `EventPublisher`
* `SpringEventPublisher`

### Adapter Pattern

`StripePaymentAdapter` converts the application's payment abstraction into Stripe API operations.

`SpringEventPublisher` adapts the application event port to Spring's event infrastructure.

### Domain Model

`Order` and `Payment` encapsulate their own business state transitions.

### Dependency Inversion

Application logic depends on abstractions rather than infrastructure implementations.

### Event-Driven Communication

`OrderPaidEvent` allows independent modules to react to successful purchases without direct module coupling.

### Idempotency

Payment completion and entitlement creation are protected against duplicate processing.

---

## 12. Current State

The Sales module currently supports:

```text
Create Order
     ↓
PENDING
     ↓
Create Stripe Checkout
     ↓
Payment CREATED
     ↓
Customer Payment
     ↓
Stripe Webhook
     ↓
Payment COMPLETED
     ↓
Order PAID
     ↓
OrderPaidEvent
     ↓
Entitlement
```

The next planned capability is the customer-facing library/access layer built on top of Entitlements.

---

## 13. Future Improvements

Production hardening will eventually include:

* Stripe webhook event persistence
* More robust event deduplication
* Payment retry handling
* Refund processing
* Payment reconciliation
* Failed-payment recovery
* Multiple payment attempts per order
* Production secret management
* Asynchronous event processing if required by scale

These are intentionally deferred until the core commerce workflow is complete.
