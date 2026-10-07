# Frontend Architecture

## Current State

The frontend is a Vite application using React 19, TypeScript, React Router 7,
TanStack Query 5, and Zustand. It is located in `frontend/` and keeps the
monochrome editorial shell, artwork presentation, and motion components in
`src/`.

The first API-backed slice is implemented. Catalog routes and product detail
read the documented public product endpoints through `src/api/client.ts` and
TanStack Query. Authentication uses the login/register endpoints with one
Zustand session store; the API client reads its bearer token from that store.
Zustand also owns the client-side cart, while server state remains in Query.

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

`src/App.tsx` provides the shared Query client and router, then mounts the
route shell. Route-level catalog, product, and homepage sections consume real
API data while preserving the existing visual treatment. The centralized audio
controller owns the single media element and in-memory temporary preview URL;
the audio store contains playback state only.

## Planned Integration Boundary

The frontend communicates with the backend through a centralized REST client
and JWT bearer authentication:

```text
React UI
  ↓
React Query / API client
  ↓
Spring Boot REST API
```

The public catalog, checkout handoff, order archive, private library, and
admin product workbench now consume the implemented API surfaces. The API
contract remains the source of truth for later product roadmap work.

unavailable treatment and does not invent a URL or expose storage keys.
The backend exposes product asset IDs and a published-product cover endpoint
that returns a temporary URL for IMAGE assets. The frontend requests that URL
through `ProductArtwork` and never invents a URL or exposes storage keys.

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
