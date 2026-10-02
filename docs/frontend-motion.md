# MedTheG motion implementation notes

The interface keeps most elements calm while reserving a signature transformation for the homepage hero. Motion tokens live in `frontend/src/styles/motion.css`; interactions use `--motion-micro`, `--motion-object`, or `--motion-editorial` and respect `prefers-reduced-motion`.

## Hero dust formation

`frontend/src/components/motion/HeroDustTitle.tsx` samples the real heading font and line positions into an offscreen canvas, then draws a capped set of fine monochrome particles toward those glyph points over 1.48 seconds. The animation runs once on page entry and stops; the semantic heading stays in the DOM and becomes the final crisp rendering when the canvas clears. Reduced-motion users receive the ordinary heading immediately, with no particle simulation. The surrounding editorial rule, artwork crop, metadata, and CTA resolve afterward in a short sequence. This uses Canvas and CSS only; no animation dependency was added.

The homepage pressings are temporary visual records, not products. They have no persisted IDs or real product artwork and must not be linked to a fabricated product route or receive a shared-element transition.

## Archive and artwork layers

`frontend/src/features/archive/ArchiveItem.tsx` is the reusable focusable archive item. `frontend/src/components/editorial/EditorialArtwork.tsx` provides the print layers for image/artwork, mask, registration rule, index, and metadata. Hover and keyboard focus use the same contact-sheet focus state: neighboring items quiet without disappearing, the selected print receives a restrained 1â€“3px registration offset, its line passes behind the opaque artwork, and the title/index shift their visual positions without changing DOM order. The homepage data currently has no price field, persisted product ID, or artwork URL, so the reusable item accepts an optional price but the preview records omit it.

The category routes still render `PlaceholderPage`; their empty-state title spacing now varies between compact Beats, spacious Kits, document-led Courses, and layered Bundles. Actual grid density, artwork scale, and category interaction wait for real catalog entries, without introducing separate color themes. React Router View Transitions are scoped to category navigation. Product navigation continuity remains reserved for the real artwork transition described below.

## Activating the product artwork transition

Once catalog cards and the product page consume real `ProductResponse` objects:

1. Use the same `product.id` in the catalog item and `/products/:id` response.
2. Put `style={{ viewTransitionName: releaseArtworkTransitionName(product.id) }}` on the actual artwork element in both the source catalog item and destination product page.
3. Navigate to the product route with React Router's `viewTransition` option on that product link only. Keep it off generic navigation, account, cart, and unrelated links.
4. Keep source and destination artwork present for the same navigation and verify behavior when an image fails to load and when reduced motion is enabled.

The helper in `frontend/src/features/catalog/releaseArtworkTransition.ts` provides a safe, stable name. It does not enable or claim a transition by itself. The product route is currently a placeholder, so the shared transition remains inactive until real API-backed catalog artwork and product details are connected. Public `ProductResponse` includes `id`, `price`, `currency`, `coverAssetId`, and asset references, but the public response does not contain an artwork URL. There is currently no public cover-image delivery endpoint: the existing preview route only signs `AUDIO_PREVIEW` assets. The backend prerequisite is a published-product cover delivery URL or endpoint for `IMAGE` assets; until then actual cover artwork and artwork continuity cannot be activated. The backend's `GET /api/products/{productId}/assets/{assetId}/preview` returns a temporary presigned audio preview URL (15-minute expiry); request it for playback and keep it in player memory only. Never put it in Zustand persistence or local storage.

## Preview transport

The current transport is explicitly a visual demo with no audio source. It has no fabricated waveform or duration. Its finite 18-second progress cue starts at zero: play advances, pause freezes, resume continues, and ended stays settled until replay. The mini player is a full-width structural strip with a finite mechanical indicator. Reduced-motion mode disables decorative choreography while preserving the finite progress cue. Do not treat the visual demo's playhead as real playback progress. When API playback is implemented, replace the demo state transitions behind the existing player controls with one shared `HTMLAudioElement` controller; retain the signed preview URL only in memory and refresh it after expiration.

## Final polish notes

The archive presentation model carries title, index, metadata, and `ArtworkPresentation` (`src`, `alt`, optional fallback mark); selected state and its callback stay outside the artwork component. Pointer hover, keyboard focus, and touch selection share the same focus treatment. Nearby releases remain readable, with artwork offsets capped at 1â€“3px. `EditorialRule` is the reusable horizontal/vertical grayscale rule primitive.

The visual player demo has no fabricated waveform or duration. Its finite 18-second progress cue starts at zero and settles at ended; it is not actual playback progress. The mini player is a full-width structural strip, with a finite mechanical indicator. Reduced-motion mode removes decorative movement while keeping the finite progress state cue.

The public `ProductResponse` provides `coverAssetId`, but the current API has no public cover-image delivery endpoint. The `/api/products/{productId}/assets/{assetId}/preview` route signs audio previews only. Backend/API work must provide a published-product image delivery URL before production catalog and detail artwork can use the component. Never derive an image URL from `coverAssetId` or a storage key. Product route transitions remain disabled until both routes use the same real product ID and actual artwork.

Graphify's existing output was treated as sufficient for this visual pass. The local launcher has repeatedly failed with `uv trampoline failed to canonicalize script path`; it was not retried. Restore the Graphify launcher/tooling path before the next requested graph update. No generated product IDs, URLs, audio samples, or BPM data were introduced here.
