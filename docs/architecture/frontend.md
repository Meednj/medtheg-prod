# Frontend Architecture

## Runtime and state

The frontend is a Vite application using React 19, TypeScript, React Router 7,
TanStack Query 5, and Zustand. The app router is a data router so route-level
navigation blocking can protect unsaved product edits. Server data is queried
and invalidated through TanStack Query; authentication, cart, and active audio
state remain in their existing Zustand stores.

The centralized API client attaches the current bearer token and normalizes
errors. Admin requests use the same client and authenticated session as other
protected operations. No credentials or storage keys are stored by the admin
screens.

## Shared layout

`AppShell` owns the single global `StoreHeader`, page main landmark, shared
audio player, and footer for storefront, account, library, and admin routes.
Pages provide their own context-specific heading and optional breadcrumb; they
do not render a second brand header. `StoreHeader` is the sole global MedTheG
brand lockup. The shell derives route classes without changing content
structure, avoiding per-page header shifts.

## Production Desk

The authenticated admin catalog uses `GET /api/products/admin` rather than the
published-only public catalog. URL parameters retain `page`, `size`, `status`,
`type`, `search`, `sortBy`, `direction`, and the selected product ID across
listing, detail, and edit routes. Results are server-paginated (20 by default,
up to 100); filters combine on the server. The listing shows cover art through
short-lived admin URLs, title, type, categories, price/currency, lifecycle
status, and last-updated time. Loading, retry, empty, and pagination states are
provided.

Previous/Next product controls use the same filtered and sorted server result
set and cross page boundaries. They preserve query state and identify their
position. Direct product-ID lookup remains available when an administrator
already has a product reference.

The editor supports title, description, price in EUR, and categories. Product
type is read-only after creation because the current domain keeps it immutable;
currency is fixed to EUR by the API. Save validates the fields, exposes pending
and error states, and invalidates public/admin product caches after success.
Reset restores saved values. React Router blocks in-app navigation when edits
are dirty, and the browser's `beforeunload` prompt covers refresh or tab close.
If a refetch finds server values changed, saving is paused until the editor
loads the latest values or the administrator leaves the draft aside.
The current API has no version/ETag write precondition, so this catches changes
visible during refetch but cannot reject a write racing with the final save.

The asset section lists IDs and `DigitalAssetType`, uploads with an explicit
type, validates basic file type and a 250 MB client limit, shows pending and
result states, and confirms deletion of an asset reference. AUDIO_PREVIEW files
play through a temporary admin presigned URL; displayed duration is read from
the browser media metadata. Upload progress is not available in the fetch
client, and the API does not persist filename, size, or duration metadata, so
those are not represented as durable server fields. Removing an asset
reference does not claim to remove the object from MinIO. Full-quality assets
are never used as previews.

## Animation and accessibility

Motion uses the existing CSS token primitives in `src/styles/motion.css`; no
animation dependency is installed. The Production Desk uses a short entrance
stagger for result rows and restrained artwork/button transitions. Form
submission, navigation, playback, and file input remain immediate. Global
`prefers-reduced-motion` styles reduce transitions and disable nonessential
animation. Semantic links, buttons, labels, field errors, visible focus, status
announcements, and touch-sized navigation controls are retained.

## Development and verification

```powershell
cd frontend
npm run lint
npm run build
```

There is no configured frontend unit-test script in `package.json`. Browser
behavior must be reported separately from lint/build results; this document
does not claim a hosted payment or full commerce lifecycle verification.
