# Frontend Architecture

## Current State

The frontend is a Vite application using React 19, TypeScript, and React DOM.
It is located in `frontend/` and currently contains the default Vite starter
experience: a counter, Vite and React links, and the starter assets in
`src/assets`.

There are no application routes, API clients, authentication screens, or
marketplace workflows implemented yet. `react-router-dom` and
`@tanstack/react-query` are already listed as dependencies for the planned
application surface, but they are not currently used by `App.tsx`.

## Current Entry Flow

```text
frontend/index.html
	↓
src/main.tsx
	↓
src/App.tsx
	↓
src/App.css and src/index.css
```

The current page imports the generated Vite, React, and hero assets. The
frontend is therefore useful as a development shell, but it is not yet a
client for the backend marketplace.

## Planned Integration Boundary

The frontend will eventually communicate with the backend through the REST API
and JWT bearer authentication:

```text
React UI
  ↓
React Query / API client
  ↓
Spring Boot REST API
```

The planned user-facing areas are product browsing, authentication, checkout,
order history, customer entitlements, and the downloadable library. The API
contract should be treated as the source of truth while those screens are
implemented.

For catalog cards, use `categories`, `coverAssetId`, and `previewAssetId` from
the product response. Request preview playback through the public preview URL
endpoint. Do not assume the Stripe success route means the order is already
paid; refresh backend order or payment state instead.

## Development Commands

```bash
npm run dev
npm run lint
npm run build
```

The build runs TypeScript project compilation followed by the Vite production
build.
