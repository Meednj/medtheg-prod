# Backend Finalization Report

## Files Changed

Backend changes cover catalog visibility and assets, product preview/archive flows, security error responses, and authenticated order history. Tests were updated for published-only catalog behavior, safe asset responses, preview security, and order listing.

Documentation was updated in `docs/api-contract.md`, `docs/architecture/`, and `docs/architecture/current-state.md`.

No frontend code was modified.

## Issues Fixed

- Public product reads and listings now expose only `PUBLISHED` products.
- Administrators can retrieve draft and archived products.
- Administrators can archive published products.
- Product responses expose categories, cover references, and preview references without storage keys.
- Public preview URLs are limited to `AUDIO_PREVIEW` assets and expire after 15 minutes.
- `GET /api/orders` now invokes the authenticated customer order-list use case.
- Authentication and authorization failures now use the centralized API error shape.

## API Contract Changes

- Added `GET /api/products/admin/{id}` for administrator lifecycle reads.
- Added `POST /api/products/{id}/archive`.
- Added `GET /api/products/{productId}/assets/{assetId}/preview`.
- Product responses now include `categories`, `coverAssetId`, and `previewAssetId`.
- Product asset responses no longer expose `storageKey`.
- The existing library download response remains `{ "url": "..." }`.

## Security Changes

- Public catalog access is repository-constrained to published products.
- Protected purchased assets remain entitlement-checked.
- Preview URLs cannot be generated for protected asset types.
- Security `401` and `403` responses include `timestamp`, `status`, `code`, `error`, `message`, `path`, and `errors`.

## Tests

- Added published-visibility service coverage.
- Added preview URL and protected-asset coverage.
- Added authenticated order-list coverage.
- Focused catalog/order integration tests: 37 passed.
- Full backend suite: 109 passed, 0 failures, 0 errors, 0 skipped.
- `git diff --check`: passed.

The full suite was run with `-Dmanagement.health.redis.enabled=false` because Windows blocked Docker host port publication for Redis. This is an environment limitation, not a production configuration change.

## Remaining Issue

Before production deployment, resolve the local/host Redis port conflict and verify actuator Redis health without the test-only override.
