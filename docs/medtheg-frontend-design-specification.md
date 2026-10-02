# MedTheG Frontend Design Specification

**Status:** Final design phase specification. **No frontend implementation is authorized by this document.** This specification supersedes the earlier exploratory direction in [frontend-design-direction.md](frontend-design-direction.md). Backend/API contracts remain the source of truth for functionality.

## A. Design concept — The Monochrome Pressing Room

MedTheG is an independent production label archive rendered as a black-and-white editorial publication. It should feel like black vinyl, photocopied studio notes, monochrome studio photography, and a tightly engineered playback deck. The store is cinematic through composition, scale, silence, grain, and real product artwork—not through colored UI or effects.

Use **brutalist editorial structure**: visible column lines where useful, squared edges, oversized Space Grotesk display type, stark black/white reversals, deliberately asymmetrical compositions, and small technical metadata. Keep purchase information and controls direct and predictable. Experimental art direction belongs in the editorial frame; essential navigation, product facts, and actions stay conventional enough to use quickly.

The four signature experiences are the audio player, product discovery, the artwork-led product page, and the private digital library. The admin uses the same identity with a denser production-workbench layout.

## B. UX/UI Pro research synthesis

The installed UX/UI Pro search script was executed locally. Its outputs are design research, not a color mandate:

- The product-wide `--design-system` query recommended the **Brutalism** style and a **Feature-Rich Showcase** pattern. Brutalism’s relevant characteristics were stark contrast, bold type, visible borders, sharp corners, asymmetry, and large blocks. The pattern’s useful principle is a clear feature hierarchy and a strong, repeatable primary action. Its default hero → feature cards → social proof sequence is rejected for MedTheG because the requested music editorial experience must not become a landing-page template.
- The Brutalism dataset included red, blue, and yellow. Those colors are explicitly discarded under the MedTheG monochrome requirement; retain only its structural guidance.
- The design-system query returned Righteous/Poppins for a broad music category. A more specific typography search returned **Kinetic Brutalism (Space Grotesk)** as the best match for music/culture apps and underground product drops. This is the selected font direction: Space Grotesk leads both display and interface typography. A technical mono face is limited to short metadata.
- The style search also surfaced **E-Ink / Paper** as a monochrome, matte, textured reference. Borrow its grain and paper/ink contrast selectively; retain the dark listening-room base.
- UX/UI Pro’s motion output recommended a stagger-list reveal of 300–450ms with about 60ms between items, plus reduced-motion handling. Apply this only to short editorial/product sequences, with a restrained ease and no overshoot; playback controls remain immediate and stable.
- The landing-pattern search initially returned no match for “editorial music storefront”; on the required narrower retry it returned Product Demo + Features and Scroll-Triggered Storytelling. Use only the applicable interaction guidance: if preview audio is part of a product story, provide explicit play/pause, a static non-audio fallback, and no autoplay. A story must remain understandable without scroll choreography; progress, chapters, and scroll effects are optional and must simplify on mobile/reduced motion.
- UX/UI Pro’s audio-oriented UX search surfaced visible focus, native button semantics, accessible names for icon buttons, and user control of rotating content. Its responsive/accessibility search returned larger touch targets, at least 8px spacing between adjacent mobile targets, `prefers-reduced-motion`, visible focus, and platform-sensitive target sizing. Use semantic HTML, minimum 44px web touch controls for primary player actions, and stronger 48px targets where practical.
- The React stack search recommends profiling with React DevTools before optimizing, typing React event handlers, and virtualizing very long lists (guidance threshold: over 100 items). Apply only when the corresponding workload exists.

UX/UI Pro results can include generic or conflicting advice. MedTheG constraints and backend behavior govern the final choices.

## C. Graphify synthesis

Graphify ran against the existing `graphify-out/graph.json` (1,395 nodes). Focused outputs provide a structural cross-check of the repository:

- `ProductController.java` is in the Product Commands community and links its asset operations to `ProductResponse`; the shortest-path output from the controller to response was `ProductController → .addAsset() → ProductResponse` (two hops, extracted edges).
- `LibraryController.java` imports `GetCustomerLibraryUseCase`, `LibraryItemResponse`, and `AuthenticatedUser`. This confirms the customer library is a protected API-backed feature, not a separate downloadable product catalog.
- `OrderPaidEvent` is connected to `MarkOrderAsPaidService` and the entitlement event listener. Graphify marks the service execution edge as inferred and the event/listener references as extracted. Combined with the architecture docs, this supports the flow: verified payment completion → paid-order event → entitlement → library access.
- Broader BFS queries found many loosely related nodes and were truncated; they are not treated as evidence for individual UX capabilities. The API contract and controller/DTO code below determine exact fields and actions.

## D. Monochrome design tokens

All interface pixels use neutral grayscale only. Each color token has equal red, green, and blue channels. Product artwork and user-provided media are content and may retain their native color inside their image boundary; UI chrome, states, text, focus, borders, overlays, gradients, shadows, and glows remain monochrome.

| Token | Value | Intended use |
|---|---|---|
| `mono-1000` | `#000000` | Black panels, image mats, inverse actions |
| `mono-950` | `#0A0A0A` | Main page canvas |
| `mono-900` | `#141414` | Header, structural surfaces |
| `mono-800` | `#242424` | Inputs, player bed, selected rows |
| `mono-700` | `#3A3A3A` | Dividers/borders on dark surfaces |
| `mono-600` | `#666666` | Non-text detail or large text only where contrast passes |
| `mono-400` | `#A3A3A3` | Secondary text on dark surfaces |
| `mono-200` | `#D6D6D6` | High-emphasis secondary text |
| `mono-100` | `#F5F5F5` | White editorial fields and primary text |
| `mono-0` | `#FFFFFF` | Highest contrast and hover inversion |

Semantic tokens are mapped from these neutrals: `canvas`, `surface-1`, `surface-2`, `surface-inverse`, `text-primary`, `text-secondary`, `text-disabled`, `border-subtle`, `border-strong`, `focus-ring`, `play-progress`, and `scrim`. Never introduce a hue for success, error, warning, selection, price, or active playback. Meet 4.5:1 contrast for normal text and verify each actual pair; `mono-600` is not default body text on the dark canvas.

## E. Typography system

- **Display and interface:** Space Grotesk. UX/UI Pro’s specific music/culture pairing places it at the top of the search results and recommends 700 for display with 400–600 available for interface text. Use 700–800 for short poster headlines, 600–700 for section heads/product titles, 500–600 for controls, and 400–500 for body text.
- **Technical metadata:** Space Mono (from the Kinetic Motion pairing result) for terse IDs, asset types, timestamps, and compact filter labels only. Avoid mono paragraphs.
- **Scale:** label 12px; caption 14px; body 16px minimum on mobile, 17–18px on long product descriptions; small heading 22–24px; section 32–48px; display fluid 56–120px where the composition allows. Use `clamp()`-style responsive sizing rather than a fixed oversized mobile headline.
- Display/navigation may use uppercase selectively with tight tracking. Body copy uses normal casing, 1.45–1.6 line-height, and a 60–75 character reading measure.
- Self-host properly licensed font files if approved at implementation time; otherwise use the defined system sans/mono fallbacks. Avoid an external font request as a rendering dependency.

## F. Layout and grid system

- Editorial pages use a 12-column desktop grid, asymmetric 7/5 or 8/4 compositions, and a consistent max content width around 1440px. Product reading copy uses a narrower measure.
- A baseline 4-column mobile grid expands to 8 columns at tablet and 12 at desktop. Grid lines are deliberate editorial marks in selected sections, not a visible box around every item.
- Product grids are image-led with clear gaps and no card chrome by default. Use ruled rows, horizontal indexes, and open whitespace to vary density.
- Reserve fixed space for artwork and asynchronous content to avoid layout shift. Keep one page scroll axis; avoid nested scroll panes except a deliberate dialog/sheet.
- Layering is reserved for a single artwork/text overlap, a player transport, or a modal scrim. Avoid a site-wide stack of glass panels.

## G. Spacing

Use a 4px base rhythm: 4 / 8 / 12 / 16 / 24 / 32 / 48 / 64 / 96 / 128px. The UX/UI Pro design-system density dial was 4/10 (standard-to-spacious); use the roomy steps for editorial chapters and tighter steps for admin rows and player details.

Page gutters: 20px mobile, 28–36px tablet, 48–72px desktop. Keep at least 8px between adjacent mobile tap targets. Blank space is an intentional hierarchy tool, especially around the hero, player, and section breaks.

## H. Borders and radius

Use 0px for artwork and most editorial containers; 2–4px for utility controls. Avoid rounded product-card shells and pill-shaped primary buttons. Filter chips may be compact rounded rectangles if selected state is visible in both shape/text and monochrome contrast. Borders default to 1px gray; strong/focus borders use 2px white or black with a 2px offset where needed. Tables/forms use dividers rather than enclosing every field in a card.

## I. Shadows and surfaces

Surfaces progress from `mono-950` canvas to `mono-900` structure, `mono-800` controls, then `mono-100`/white editorial sheets. White areas are intentional contrast events, not an alternate colored theme. Prefer border, offset, and tonal plane changes to drop shadows. If an overlay needs separation, use a soft neutral black shadow and/or grayscale scrim. Blur is used only for an overlay backing; no decorative glass blur. Grayscale gradients may support image legibility, never create colored light.

## J. Image and artwork treatment

- Keep cover art square and unaltered, with fixed aspect ratio and object-fit cover. Do not globally desaturate it: its native color is permitted as product content. Keep the rest of the interface monochrome.
- Surround artwork with black/white mats, crop edges, editorial captions, or monochrome photographic grain. Never lay essential small text directly over busy art without a solid grayscale backing.
- Use optimized responsive images and lazy-load below the fold. Reserve image dimensions to prevent layout shift.
- If cover art is absent, use a black/gray paper-texture field with a visible “Artwork unavailable” label; do not invent a cover.
- Product cards may show preview controls outside the art plane. Do not add extra decorative colored visualizers.

## K. Motion system

Use motion to indicate playback, reveal a product image, or reinforce a chapter transition—not to decorate every element.

- Control feedback: 120–180ms opacity/brightness/border/inversion change.
- Route, menu, and sheet transitions: 180–280ms with restrained ease; maintain focus and scroll context.
- UX/UI Pro’s 300–450ms stagger-list recommendation applies to short editorial sequences of at most a handful of products. Stagger around 50–70ms, stop when offscreen, and avoid bounce/overshoot. Product data remains readable if animation never runs.
- Artwork reveals can use a masked wipe or opacity transition; avoid animating layout dimensions.
- Player state changes are immediate. Waveform motion is limited to active playback and pauses when reduced motion is requested or when hidden/offscreen.
- Respect `prefers-reduced-motion`: remove parallax, stagger, scroll-scrub, and nonessential reveals; show the final static state. No autoplay, auto-rotating carousel, or scroll hijacking.

## L. Navigation

### Shared store shell

Desktop masthead: wordmark at left; BEATS, KITS, COURSES, BUNDLES as the primary index; search, account, and cart at right. Use text labels first, with consistent SVG line icons only when they improve scanning. The active route uses a white rule/weight change, not a hue. Header may become compact/sticky after scroll but must not obscure focused content.

Mobile masthead: wordmark, menu control, and cart. The menu opens a full-height monochrome panel with category index, search, login/account links. Use a focus-managed dialog, Escape to close, restore focus to trigger. Search can be a dedicated `/search` route only if included in product navigation later; requested routes do not require a separate search route, so catalog search state stays in query parameters.

Add a skip-to-content link, semantic `<nav>`, `aria-current` on active links, descriptive labels for icon-only controls, and visible keyboard focus.

## M. Homepage `/`

**Layout and hierarchy:** A continuous editorial listening session, not a conventional marketing stack. (1) Compact masthead; (2) oversized “Make room for the low end” style statement beside a real featured cover; (3) a functional featured-preview transport that reads as a small listening desk; (4) latest releases as an open, asymmetric index; (5) category discovery rendered as a typographic directory; (6) one featured kit/course/bundle editorial chapter if live published data supports it; (7) restrained footer/navigation. Omit testimonials, invented creator quotes, fake metrics, and empty feature cards.

**Components:** StoreHeader, editorial statement, FeaturedRelease, AudioPlayer/PlayButton, LatestReleaseGrid, CategoryIndex, optional CuratedReleaseFeature, Footer.

**Interactions:** The primary CTA is “Explore latest releases”; featured preview is explicit play/pause with no autoplay. Cards navigate to product pages; optional add-to-cart remains secondary. Provide a static cover/metadata fallback if preview audio fails or no preview exists. Use public catalog requests and do not hardcode permanent product IDs.

**Responsive:** Desktop hero is split/asymmetric; artwork and headline share the grid. Tablet narrows the split. Mobile orders title → artwork → preview/action, then release index. Do not shrink a desktop composition into unreadable columns.

**Motion:** One restrained image reveal and a brief product-list stagger; disable both under reduced motion. No scroll-lock/pin sequences.

## N. Catalog pages `/beats`, `/kits`, `/courses`, `/bundles`

**Layout and hierarchy:** Category masthead with oversized title and one line of category context, followed by result count and filter/sort strip, then artwork-led grid or ruled list. Filters use supported URL/API fields only: `category`, `type`, `search`, `sortBy`, `direction`, `page`, `size`. Use actual category values `BEATS`, `KITS`, `COURSES`, `BUNDLES`; product types are separate values (`BEAT`, `SAMPLE_PACK`, `DRUM_KIT`, `COURSE`, `BUNDLE`). Do not conflate these enums.

**Components:** CatalogHeader, SearchField, FilterControls/FilterSheet, SortSelect, ProductGrid/ProductRow, ProductArtwork, Pagination, empty/error/skeleton states.

**Interactions:** Query params preserve filter/search/sort and browser back/forward. Public API returns published products only. A product card shows title, category/type, price/currency, artwork and preview button only when `previewAssetId` is present. Don’t promise filters (BPM, key, creator, license, tags) absent from DTO/API. Cards remain fully operable without hover.

**Responsive:** Two-column mobile artwork grid only while titles/prices remain legible; otherwise use a single ruled list. Tablet uses 2–3 columns; desktop 3–4. Mobile filters open an accessible sheet with apply/clear controls and 44px+ touch targets.

**Motion:** Selected filter state changes through grayscale border/fill inversion; no animated card lift that shifts grid geometry. A short reveal can be used for the first visible items only.

## O. Product detail `/products/:id`

**Layout and hierarchy:** Artwork and audio are the visual center. Desktop uses an asymmetric artwork/player column and purchase-information column. The first view contains category, product title, type, price/currency, primary purchase action, cover and preview control. Description and included asset-type list follow, then related published products.

**Data/components:** Public `GET /api/products/{id}` returns product ID, title, description, type, price, currency, status, categories, `coverAssetId`, `previewAssetId`, and asset `{id,type}` references. Use `GET /api/products/{productId}/assets/{assetId}/preview` for the expiring preview URL. Components: ProductArtwork, ProductHeading, CategoryLabel, Price, AudioPlayer, ProductPurchasePanel, ProductDescription, ProductAssets (types only), RelatedProducts.

**Interactions:** Add to cart updates client state; if protected checkout is attempted unauthenticated, preserve cart and return to login. Show “In your cart” as client state; show “Owned” only when library data confirms an active entitlement. Draft/archived/unavailable products render not-found state. Preview URL is requested on play and refreshed on a subsequent attempt after expiration/error. Never show storage keys, private file URLs, filenames, file sizes, BPM, key, license terms, or download actions unless explicitly present in an authorized response.

**Responsive:** Mobile stack is artwork → title/price → primary action → audio → description/assets. A bottom purchase bar may appear after the main action scrolls away, but must reserve page space and not cover player controls or keyboard focus. Tablet/desktop maintain artwork dominance and a stable info column.

**Motion:** Cover may reveal with a grayscale mask; title and price appear without delay. Player state has its own immediate feedback. Avoid parallax on product art.

## P. Audio player

The player is a MedTheG signature transport, shared between product previews and a compact cross-route mini-player.

**Layout and controls:** Artwork thumbnail/title; large Play/Pause; thin seek track with a time-progression trace; elapsed/duration; volume/mute; optional close. Use grayscale played/unplayed segments, and an accessible slider plus visible textual times. API provides no waveform samples, so any waveform-shaped line is an ornamental timeline derived from progress, not a representation of the audio signal.

**Behavior:** One active item at a time. Play requests/refreshes the short-lived preview URL, then reports loading, playing, paused, buffering, ended, or recoverable error. Do not autoplay. Starting another preview changes the single active item. Avoid stale URL persistence. Provide volume control on desktop and a larger expanded control panel on mobile. Media errors never block product browsing/purchase.

**Components:** AudioController (single `HTMLAudioElement` lifecycle), AudioStore state, MiniPlayer, PlayerPanel, PlayButton, SeekSlider, TimeReadout, VolumeControl, optional static ProgressTrace.

**Responsive/accessibility:** Compact bottom transport on mobile and narrow full-width transport on desktop; content has bottom inset while it is visible. Play/pause is a labeled button with pressed state. Seek slider has a name, value, arrow-key operation, Home/End behavior, and textual time. Announce load/error and track change, not every second. No keyboard shortcut may steal input focus.

**Motion:** Progress follows playback; no constant equalizer animation. Reduced motion makes the trace static and leaves clear progress state.

## Q. Cart `/cart`

**Layout and hierarchy:** Editorial list of cart lines on the left/top; order estimate, continue action, and sign-in context in a separate summary region. Avoid boxed card rows. Empty state is a plain typographic invitation back to catalog.

**Components:** CartLine, ProductArtwork, QuantityControl, RemoveAction, CartSummary, EmptyCartState, ErrorBanner.

**Interactions:** Cart is client-side. Quantity/remove is reversible and announced accessibly. Order totals are estimates; `POST /api/orders` validates current product/quantity and snapshots server prices. Preserve cart on API failure. Prevent duplicate submits and communicate unavailable product errors.

**Responsive:** Single-column item rows on mobile with summary/action after items; tablet/desktop list plus sticky summary where it does not obscure content.

**Motion:** Small grayscale confirmation on add/remove; no layout bounce. Respect reduced motion.

## R. Checkout `/checkout`

**Layout/hierarchy:** Focused review/continue state. This is an API handoff, not an in-site payment form. Show products, server-authoritative order totals returned from order creation when available, and a clear “Continue to secure checkout” action.

**Components:** CheckoutReview, OrderSummary, SubmitButton, ErrorState.

**Interactions:** Customer JWT required. Submit `POST /api/orders` with `{items:[{productId,quantity}]}`, then `POST /api/orders/{orderId}/payment`; redirect to returned `checkoutUrl`. Disable duplicate submission. If payment session creation fails, retain cart and explain retry. Never collect or proxy card data.

**Responsive/motion:** One column at all widths; compact centered max-width on desktop. Loading replaces button label with text and monochrome progress indicator. No celebratory animation before confirmed payment.

## S. Payment success and cancel

StripePaymentAdapter currently appends `?orderId=<UUID>` to both configured return URLs. Treat the parameter as a lookup hint, never proof of payment.

**Success `/payment/success?orderId=...`:** Calm confirmation-in-progress layout, order ID reference, and links to `/orders` and `/library`. Refresh the TanStack Query for `GET /api/orders?page=0&size=12` and match the ID against returned order history. `OrderResponse` contains status (`PENDING`, `PAID`, `CANCELLED`, `REFUNDED`), total/currency, dates, and item snapshots. If found as `PAID`, show confirmed state. If still `PENDING`, explain the webhook confirmation is processing and offer manual refresh; use only a bounded retry cadence, never indefinite polling. If absent from the page, show the same safe pending/lookup-help state. Never infer payment from URL or redirect alone.

**Cancel `/payment/cancel?orderId=...`:** State that checkout was returned/canceled and offer “Return to cart” or order history. Do not assert the order is deleted, canceled, or unpaid unless order-history status says so. Keep cart contents available.

**Visual/responsive/motion:** Narrow status panel with monochrome icon, headline, and body; no confetti or colored success/error styling. Mobile is a single readable status screen. A subtle progress mark is optional, static under reduced motion.

## T. Orders `/orders`

**Layout/hierarchy:** Private order archive with newest order first as returned by API; rows show created date, status text, item snapshot, total/currency, and paid date when present. Group detail expands inline rather than requiring an unsupported per-order route.

**Data/components:** `GET /api/orders?page=0&size=12`; page response includes `content`, `page`, `size`, `totalElements`, `totalPages`. Order rows use returned item `productTitle`, `unitPrice`, quantity, subtotal. Status badges pair labels (`Pending`, `Paid`, `Cancelled`, `Refunded`) with monochrome icon/shape.

**Interactions:** Pagination follows server response. Order ID may be copied with visible success text. No receipt, refund, invoice, or payment-detail action is shown because no such endpoint is documented.

**Responsive/motion:** Mobile turns columns into stacked labeled rows; desktop uses a ruled table/list. Expand/collapse is keyboard-operable and reduced-motion safe.

## U. Library `/library` and `/library/:productId`

**Concept:** A private digital vault: sparse, quiet, archived, and unmistakably customer-owned. Keep this distinct from a public streaming catalog.

**Layout/components:** Library index uses a high-contrast title and typographic release list/grid with title, product type, price/currency/granted date/order ID if useful, and asset counts. Product detail groups authorized assets by type (`AUDIO_MP3`, `AUDIO_WAV`, `STEMS`, `MIDI`, `PDF`, `VIDEO`, `OTHER`) and provides per-asset download buttons. DTO returns no cover ID, so use text-led entries or enrich with the public product endpoint when it is published and accessible; fallback remains artwork-free. Do not assume every library item is still public.

**Interactions:** `GET /api/library` returns active entitlement products/assets. `GET /api/library/assets/{assetId}/download` returns `{url}` after ownership check; request on click and immediately open the temporary URL. Link expires after 15 minutes. Do not persist/display signed URLs or imply completed transfer/progress—the API only supplies a URL. The library DTO contains entitlementId, productId, orderId, title, description, type, price, currency, grantedAt, and assetId/productId/type.

**Responsive/motion:** Mobile is a text-led list with collapsible asset groups and large download targets. Desktop can use a two-column release index/detail pane. Loading/empty/error states stay quiet and monochrome. No animated vault-door metaphor.

## V. Authentication `/login` and `/register`

**Layout:** Narrow centered or left-aligned editorial form on dark monochrome canvas, with a simple wordmark, short context, visible labels, and no decorative split-pane marketing panel. Links between sign-in/register remain explicit.

**Login:** `POST /api/auth/login` returns userId/email/role/accessToken. Centralize bearer token attachment. After login, return to requested protected destination or storefront. Suspended/invalid credentials show actionable inline error; clear stale auth on 401.

**Register:** `POST /api/auth/register` returns id/email/role/status but no token. On 201, tell the user account is created and take them to login; never silently treat registration as authenticated.

**Accessibility/responsive/motion:** Persistent labels, correct email/password autocomplete, paste/password-manager support, visible focus, field-level errors, plus a focusable linked error summary after failed submit. Mobile full-width fields/buttons; desktop uses same narrow measure. No password reveal icon without an accessible label. Avoid animation except a short reduced-motion-safe form reveal.

## W. Admin

### `/admin`

Professional production workbench landing: “Studio / Admin” identity, create draft action, and product-ID lookup. No fake dashboard metrics, charts, or activity feeds. The actual API has no admin list endpoint.

### `/admin/products`

Honest product-management index with create action and direct product ID lookup; do not render a fabricated table. Public `/api/products` returns published products only and cannot stand in for an admin all-status list. Explain that full admin discovery requires an API endpoint if the user wants searchable admin listing.

### `/admin/products/new`

Labeled draft form for title, description, product type, price; category selection uses `BEATS`, `KITS`, `COURSES`, `BUNDLES`. `POST /api/products` creates draft. After creation, navigate to `/admin/products/:id` and surface returned ID/status.

### `/admin/products/:id` and `/edit`

Load any lifecycle state using `GET /api/products/admin/{id}`. Edit through `PUT /api/products/{id}` with title, description, type, price, categories. Show lifecycle badge, save feedback, assets by type, publish/archive controls, and confirmation dialogs. Publish requires at least one category; archive is only for published product and is not deletion. Existing published product can be read through the public endpoint but admin route must use admin lookup to support drafts/archived.

Asset workflow uses admin upload `POST /api/products/{productId}/assets/upload` with multipart field `file`; controller responds `{assetId, storageKey}`. Add asset uses `{type, storageKey}` at `POST /api/products/{productId}/assets`; remove via DELETE. **Security/API caveat:** the current upload contract returns storageKey to browser code. Never render, log, send to analytics, or persist it; hold transiently only to complete the add-asset request. If the requirement means the browser must never receive a storage key at all, backend contract change is prerequisite—this design does not conceal that API behavior.

Product responses derive `coverAssetId` from an IMAGE asset and `previewAssetId` from an AUDIO_PREVIEW asset (mapper returns the first matching type). Update request has no explicit cover/preview fields, reorder endpoint, or assignment endpoint. Admin may show which IDs the server currently returns, but must not promise independent assignment/reordering. Confirm controller mapping behavior as source of truth.

**Admin visual/interaction/responsive:** Same monochrome editorial frame, denser ruled rows and form groups; “Studio / Admin” breadcrumb. Full-width stacked form on mobile, constrained form width plus asset/status rail at desktop. Upload uses native file input/keyboard interaction; show progress only if actual upload behavior supports it. Confirm publish/archive/remove, preserve form on errors, and never rely on hover or color to reveal status. Motion limited to dialogs and upload feedback.

## X. Loading, error, empty, and notification states

- **Loading:** monochrome skeletons with fixed dimensions; short tasks use a textual spinner/label. No animated shimmer under reduced motion.
- **Errors:** preserve user input; concise message; next action; request/correlation ID only if support flow needs it. Structured API errors include status/code/message/path/errors. Map validation to fields and authorization/404 to route-level explanation. Never use red.
- **Empty:** one sentence describing the state and one relevant action: browse for empty cart/library/catalog; create/lookup for admin. Avoid illustration unless grayscale and useful.
- **Success:** textual confirmation plus shape/icon and focus/announcement as appropriate; never green.
- **Notifications:** grayscale toast with icon + text, dismissible, live-region politeness appropriate to urgency. Avoid auto-dismiss for important errors. Dialogs have labeled title, initial focus, Escape close where safe, and focus restoration.
- **Stale/unavailable:** explain whether content is stale, inaccessible, or not found without claiming payment/ownership/access not confirmed by API.

## Y. Responsive behavior

Breakpoints are layout thresholds, not device assumptions: 640px small-to-medium, 768px tablet, 1024px wide tablet/small desktop, 1440px large desktop. Verify 375px portrait and mobile landscape. No horizontal page overflow; zoom remains enabled.

| Area | Mobile | Tablet | Desktop |
|---|---|---|---|
| Navigation | Wordmark + menu/cart; focus-managed full-screen index | Compact category nav where it fits | Full masthead, search/account/cart |
| Homepage | Vertical title/art/player; single-column chapter sequence | Split hero, two-column product sections | Asymmetric 12-column editorial grid |
| Catalog | 1–2 columns depending artwork/title legibility; filter sheet | 2–3 columns and inline controls | 3–4 columns or ruled list with filter row |
| Product | Art → facts/CTA → player/details; optional reserved-space bottom CTA | Two-column art/info | Asymmetric art/player and purchase column |
| Audio | Fixed compact transport; expandable accessible panel | Compact full-width transport | Persistent slim transport with content inset |
| Cart | Stacked lines and summary | List plus summary | List and sticky summary column |
| Checkout/return | Focused single-column handoff/status | Same centered status | Narrow maximum content width |
| Orders | Stacked labeled records | Compact table/list | Ruled table with page controls |
| Library | Text-led list, grouped downloadable assets | Grid/list | Index and detail pane |
| Admin | Stacked field groups and asset sections | Two-column grouped form | Form with status/asset side rail |

For small viewports, primary player hit areas should be at least 44×44 CSS px, with 8px gaps. At larger touch targets, use 48px. Sticky elements reserve their own scroll space and never obscure keyboard focus.

## Z. Accessibility

Target WCAG 2.2 AA, with the UX/UI Pro focus appearance guidance treated as a visual floor: visible focus with at least a 2 CSS px perimeter and clear state contrast (the cited 3:1 focus appearance level is AAA guidance, not misrepresented as AA). Normal text contrast is at least 4.5:1; large text/control boundaries at least 3:1 where relevant.

Use landmarks, skip link, sequential heading hierarchy, labels/hints/errors, native links/buttons, accessible icon names, `aria-current`, `aria-pressed`, and status announcements only when meaningful. Product art receives alt text identifying the work when informative; decorative repeated art is hidden from assistive tech. Selection/status is communicated by label, icon/shape, and contrast—not color alone. Keyboard can reach every action, menu, filter, player control, dialog, and admin workflow. Audio never autoplays. Reduced-motion mode is fully supported. Keep content readable at 200% zoom.

## AA. Component architecture

```text
App / Providers / Router / RouteGuard
Foundation: Button, TextField, Select, Checkbox, Dialog, Badge, Skeleton,
            Toast, ErrorSummary, EmptyState, ErrorState, Pagination
Navigation: StoreHeader, MobileNavDialog, SearchField, AccountMenu,
            CartLink, Breadcrumbs, AdminHeader
Catalog: ProductArtwork, ProductCard, ProductRow, ProductGrid,
         CatalogToolbar, FilterSheet, CategoryIndex
Audio: AudioController, AudioStore, AudioPlayer, MiniPlayer, PlayButton,
       SeekSlider, TimeReadout, VolumeControl, ProgressTrace
Commerce: CartLine, CartSummary, CheckoutReview, PaymentReturnStatus, OrderRow
Library: LibraryIndex, LibraryItem, AssetGroup, DownloadAction
Admin: AdminShell, ProductIdLookup, ProductForm, AssetUploader,
       AssetList, AssetTypeLabel, LifecycleActions
Pages: HomePage, CatalogPage, ProductPage, LoginPage, RegisterPage,
       CartPage, CheckoutPage, PaymentSuccessPage, PaymentCancelPage,
       OrdersPage, LibraryPage, LibraryProductPage, AdminHomePage,
       AdminProductsPage, AdminProductNewPage, AdminProductPage,
       AdminProductEditPage, NotFoundPage
```

Foundation components are plain semantic controls, not an imposed UI kit. Use consistent inline SVG icon shapes/strokes; no emoji icons and no additional icon library unless implementation demonstrates a real need. Product cards are not generic rounded containers. Feature containers own queries and mutations; presentational components receive typed data and callbacks.

## AB. State architecture

| State | Owner | Rules |
|---|---|---|
| Product pages, product details, orders, library | TanStack Query | Server state only; typed queries, retries for transient failures, explicit invalidation after mutations. Never duplicate in Zustand. |
| Cart | Zustand | Client state, optional local persistence. Revalidate through order creation; cart is not authoritative pricing. |
| Audio | Zustand for active track/UI; one AudioController owns the actual media element | Do not serialize media element or expiring URL. One track at a time. |
| Auth | Small auth store/context | User ID/email/role/token from login. No refresh/logout endpoint documented; no invented refresh flow. Keep token handling centralized and clear on 401. |
| URL | React Router search params | Catalog search, category/type, page, size, sortBy, direction. Back/forward restores state. |
| Local React state | Page/component | Form drafts, menu/sheet/dialog, transient upload state, inline submission feedback. |
| Browser state | Audio element/location | Playback buffering/time; temporary redirect/download URL only in immediate runtime use. |

Use TanStack Query cache keys keyed by product/list filters/customer session. Query cache clears on logout/user change. React Compiler is already enabled in Vite configuration; do not add memoization by habit. Profile real bottlenecks first, and virtualize only genuinely long lists.

## AC. Frontend folder architecture

Fits the current Vite app without adopting another framework:

```text
frontend/src/
  app/          app providers, router, route guards, shells
  api/          fetch client, DTOs, query keys, error normalization
  components/   foundation, navigation, catalog, audio, commerce, library, admin
  features/     auth, catalog, cart, orders, library, admin feature hooks/actions
  pages/        route-level composition only
  state/        Zustand cart/audio/auth stores
  styles/       grayscale tokens, Tailwind entry, base typography, motion
  assets/       local brand marks and approved static assets
```

Current frontend is a Vite React 19.2/TypeScript app with React Router 7, TanStack Query 5, Tailwind CSS 4, and React Compiler already enabled in `vite.config.ts`. Zustand is part of the requested target stack but is **not currently in `frontend/package.json`**; add it only when frontend implementation is authorized. No other dependency is justified by this spec. Existing app remains the Vite starter until implementation approval.

## AD. Implementation order (after design approval)

1. Resolve product/API issues: storage-key exposure from upload, admin product discovery, cover/preview assignment limitations, and the library artwork gap.
2. Define grayscale CSS/Tailwind semantic tokens, typography, global focus, and reusable responsive shells.
3. Set up Router, API client/DTOs, Query provider, authentication boundary, and route error handling.
4. Build shared navigation and product artwork/card/index primitives; implement public catalog routes and URL state.
5. Implement the single audio controller/store and accessible player, then connect expiring preview URLs.
6. Compose the homepage from live catalog items and resilient editorial slots.
7. Implement product detail and client cart.
8. Implement order creation, Stripe checkout redirect, bounded payment-return order refresh, and order history.
9. Implement library and temporary download flow.
10. Implement admin create/edit/detail/lifecycle/asset workflow from verified controller contracts; keep missing list/assignment features explicit.
11. Refine mobile layouts, loading/error/empty states, contrast and keyboard behavior continuously as each feature lands.

## Research and constraint provenance

| Source | Decisions grounded by it |
|---|---|
| UX/UI Pro | Brutalism structural reference; Feature-Rich Showcase hierarchy (with template sequence rejected); Space Grotesk music/culture pairing; E-Ink/Paper texture reference; stagger timing/reduced-motion; editorial/product-story controls; focus, semantic control, touch, responsive, and React performance guidance. Its colored palette values were not adopted. |
| Graphify | ProductController/ProductResponse and asset relationships; LibraryController/GetCustomerLibraryUseCase/LibraryItemResponse relationship; OrderPaidEvent → payment completion and entitlement listener relationship. Extracted edges were preferred; inferred event execution edge is identified as inferred. |
| MedTheG brand requirements | Strict black/white/grayscale UI; premium underground music culture; editorial/brutalist/cinematic treatment; no generic ecommerce/SaaS/Spotify clone; audio, product discovery, product detail, and private library as signature experiences; no implementation during design phase. |
| Repository/API/backend | Route/API field availability, product/category/type/asset enums, published-only public catalog, preview/download URL expiry, auth responses, order status/history, Stripe redirect query parameters and webhook authority, library DTO fields, admin lifecycle/upload/update constraints, and current frontend dependencies/compiler setup. |

## API and design caveats to preserve

1. Upload returns `storageKey` to the browser even though the UI must not reveal it. A strict “browser never receives a key” requirement needs an API change before implementation.
2. `/admin/products` has no all-status product-list API; this specification uses ID lookup and create rather than a fake table.
3. Product update has no explicit cover/preview selection. Server response derives `coverAssetId` and `previewAssetId` from available IMAGE/AUDIO_PREVIEW assets; expose returned state, not unsupported assignment controls.
4. Library DTO has no cover asset ID. A text-led vault is valid; published-product enrichment is optional and must degrade when the public product is no longer available.
5. Success/cancel redirects include `orderId`; only order history status is authoritative. Success must handle delayed webhook confirmation without implying payment from redirect.
6. Backend product preview supplies temporary preview playback only; purchased assets are accessed through library download URLs. The backend does not provide a protected streaming endpoint or download-progress signal.
