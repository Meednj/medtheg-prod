# Backend API Contract

This document describes the implemented Spring Boot API. The controller and DTO sources under `backend/src/main/java` are the source of truth.

## Authentication

`POST /api/auth/register` is public and accepts `{ email, password }`. It returns `201` with the created user (`id`, `email`, `role`, `status`) and does not return a token.

`POST /api/auth/login` is public and accepts `{ email, password }`. It returns `{ userId, email, role, accessToken }`. Emails are normalized, passwords are BCrypt-hashed, suspended users are rejected, and duplicate emails return `409`.

Bearer JWT authentication uses the RSA resource-server configuration. Roles are read from the `role` claim and mapped to `ROLE_ADMIN` or `ROLE_CUSTOMER`.

## Catalog

Public endpoints:

- `GET /api/products?page=0&size=12&type=&category=&search=&sortBy=createdAt&direction=desc`
- `GET /api/products/{id}`
- `GET /api/products/{productId}/assets/{assetId}/preview`

Public catalog queries return only `PUBLISHED` products. Draft and archived products behave as not found. Product responses include `id`, `title`, `description`, `type`, `price`, `currency`, `status`, `categories`, `coverAssetId`, `previewAssetId`, and asset references containing only `id` and `type`. Storage keys are never included in public product responses.

The preview endpoint returns `{ url }` for a published product's `AUDIO_PREVIEW` asset. The URL is presigned and expires after 15 minutes. It cannot be used to access protected purchased assets.

Administrator endpoints require `ADMIN`:

- `POST /api/products` creates a draft.
- `GET /api/products/admin/{id}` retrieves a product in any lifecycle state.
- `PUT /api/products/{id}` updates a product.
- `POST /api/products/{id}/publish` publishes a draft with at least one category.
- `POST /api/products/{id}/archive` archives a published product.
- `POST /api/products/{productId}/assets` adds an asset reference.
- `POST /api/products/{productId}/assets/upload` uploads an asset.
- `DELETE /api/products/{productId}/assets/{assetId}` removes an asset.

Product categories are `BEATS`, `KITS`, `COURSES`, and `BUNDLES`. Asset types include `IMAGE`, `AUDIO_PREVIEW`, `AUDIO_MP3`, `AUDIO_WAV`, `STEMS`, `MIDI`, `PDF`, `VIDEO`, and `OTHER`.

## Orders and Payments

Authenticated customers use:

- `POST /api/orders` with `{ items: [{ productId, quantity }] }`.
- `GET /api/orders?page=0&size=12` for their own paginated order history.
- `POST /api/orders/{orderId}/payment` to create a Stripe Checkout Session.

The payment response is `{ orderId, paymentId, checkoutUrl }`. Ownership, order state, and payment state are checked before creating a session.

`POST /api/webhooks/stripe` is public at the HTTP layer but requires a valid `Stripe-Signature`. Only `checkout.session.completed` events whose `payment_status` is `paid` complete payment. Completion is idempotent and publishes `OrderPaidEvent` after the payment transaction succeeds.

Stripe redirects use the configured success and cancel URLs. Reaching the success URL is not proof that an order is paid; the backend order and payment state remains authoritative.

## Entitlements and Library

Authenticated customers may call `GET /api/entitlements` and `GET /api/library`. Entitlements are unique per customer and product, and only active entitlements appear in the library.

`GET /api/library/assets/{assetId}/download` returns `{ url }` after verifying the authenticated customer owns the product through an active entitlement and the asset belongs to that product. The URL is a 15-minute MinIO presigned URL. Storage keys and permanent public URLs are not exposed.

## Errors and Operations

Application errors use:

```json
{
  "timestamp": "2026-10-01T12:00:00Z",
  "status": 404,
  "code": "PRODUCT_NOT_FOUND",
  "error": "PRODUCT_NOT_FOUND",
  "message": "Product not found: ...",
  "path": "/api/products/...",
  "errors": {}
}
```

Validation and malformed requests return `400`; missing or invalid authentication returns `401`; insufficient roles and inactive library ownership return `403`; missing resources return `404`; duplicate users return `409`; unexpected failures return `500`.

Public operational endpoints include `/actuator/health`, `/actuator/health/liveness`, `/actuator/health/readiness`, and `/actuator/prometheus`. Requests receive `X-Request-ID`, which is also placed in MDC for request logging.
