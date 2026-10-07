# Graph Report - medtheg-prod  (2026-10-07)

## Corpus Check
- 451 files · ~268,031 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 67 file(s) not represented in the graph (top: .csv 53, (none) 5, .old 3)

## Summary
- 4217 nodes · 8649 edges · 260 communities (173 shown, 87 thin omitted)
- Extraction: 93% EXTRACTED · 7% INFERRED · 0% AMBIGUOUS · INFERRED: 598 edges (avg confidence: 0.81)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `543b8ab3`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Money
- design_system.py
- UserPersistenceAdapterIntegrationTest.java
- org.springframework.transaction.annotation.Transactional
- validate_data.py
- test_data_contracts.py
- SecurityConfig.java
- uuid
- BusinessMetrics
- GlobalRestExceptionHandler.java
- org.junit.jupiter.api.Test
- gray
- generate_design_system
- scripts/core.py
- ProductJpaRepository
- .generate
- Tailwind CSS Utility Reference
- package.json
- HomePage.tsx
- Order
- Payment
- ProductRepository
- react
- Brand Guidelines v1.0
- ProductType
- Design
- Canvas Design System
- OrderItem
- StripeWebhookController.java
- Form & Input Components
- Tailwind CSS Responsive Design
- DigitalAssetType
- AudioPlayer.tsx
- AppRoutes.tsx
- Typography Specifications
- slide_search_core.py
- DesignSystemGenerator
- OrderRepository
- UserId
- MedTheG Frontend Design Specification
- Logo Usage Rules
- Component Specifications
- html-token-validator.py
- shadcn/ui Accessibility Patterns
- TestTailwindConfigGenerator
- OrderPaidEvent
- client.ts
- io.swagger.v3.oas.annotations.tags.Tag
- User
- Asset Approval Checklist
- Logo AI Prompt Engineering
- Color Palette Management
- CIP Deliverable Guide
- States and Variants
- UI Styling Skill
- TestStyleTaxonomy
- ProductId
- @tanstack/react-query
- Workflow
- .addCategory
- cip/core.py
- Design System
- Tailwind CSS Customization
- TailwindConfigGenerator
- icon/generate.py
- dto.ts
- generate-slide.py
- spacing
- RegisterUserServiceTest.java
- UserEntity
- MinioAssetStorageAdapter.java
- ProductPage.tsx
- test_text_layout_resilience.py
- org.springframework.stereotype.Component
- Routing by Task Type
- shadcn/ui Theming & Customization
- TestThresholdGate
- ProductControllerIntegrationTest.java
- DigitalAssetTypeEntity
- .generate
- org.springframework.context.annotation.Bean
- compilerOptions
- Asset Organization Guide
- Primary Color Meanings
- Core Logo Types
- persist_design_system
- fetch-background.py
- color
- useAdminProductMutations.ts
- Brand Consistency Checklist
- CIP Mockup Prompt Engineering
- Color Semantics
- MedTheG Frontend Design Direction
- compilerOptions
- Design Principles
- Design Principles
- generate_cip_set
- fontSize
- .eur
- org.springframework.context.annotation.Configuration
- extract-colors.cjs
- CIP Design Reference
- Icon Design Reference
- Copywriting Formulas
- Copywriting Formulas
- TestShadcnInstaller
- OrderPersistenceAdapter.java
- CatalogRefreshTest
- ProductEntity
- UserJpaRepository
- V5__create_orders_tables.sql
- Banner Design - Multi-Format Creative Banner System
- Messaging Framework
- Brand Voice Framework
- test_validate_tokens.py
- validate-asset.cjs
- Layout Patterns
- Tailwind Integration
- radius
- Layout Patterns
- MicrometerBusinessMetricsTest.java
- TestWebStackFreshness
- update.md
- Logo Design Reference
- Token Architecture
- design-tokens-starter.json
- ShadcnInstaller
- TestDomainDetection
- TestSearchDomains
- Primitive Tokens
- embed-tokens.cjs
- validate-tokens.cjs
- card
- Core Visual Elements
- inject-brand-context.cjs
- CIP Design Style Guide
- TestNativeDesktopStackFreshness
- Quick Reference
- .findAll
- sync-brand-to-tokens.cjs
- Brand
- Slide Strategies
- Component Tokens
- generate-tokens.cjs
- button
- duration
- Slide Strategies
- Modular monolith decision
- Entitlement module
- Security architecture
- 2. Brand system
- CatalogPage.tsx
- logo/search.py
- input
- ui-ux-pro-max
- Slides Reference
- HTML Slide Template
- HTML Slide Template
- Query Contract
- ProductStatusEntity
- Stripe signed webhook
- products
- Root local dependency orchestration
- Backend API Contract
- Backend Current State
- SpringEventPublisher
- Pre-Delivery Checklist
- Prerequisites
- ProductCategoryEntity
- Slides
- .oxlintrc.json
- Brand Guidelines Template
- $type
- radius
- lg
- sm
- TestBm25CoreBehavior
- Common Rules for Professional UI
- Example Workflow
- BackendApplication.java
- TestTokenizer
- W. Admin
- padding-y
- xl
- none
- Tips for Better Results
- Backend architecture
- Flyway migrations
- React Query / API client
- 16
- 1
- 3
- 8
- destructive
- destructive-foreground
- muted
- primary-foreground
- ring
- secondary-foreground
- BCryptPasswordHasher
- V4__create_users_table.sql
- Skeleton.tsx
- tsconfig.json
- medtheg-backend scrape job
- AGENTS.md
- slides-create.md
- create.md
- TestMetricMath
- TestFixtureValidation
- TestDiagnosticsContracts
- ProductMapper.java
- DownloadUrlResponse.java
- L. Navigation
- Frontend HTML entrypoint
- Vite
- Graphify repository guidance
- Medtheg Hero Artwork
- React Logo
- Infrastructure local data services
- com.backend:backend

## God Nodes (most connected - your core abstractions)
1. `ProductId` - 107 edges
2. `Product` - 99 edges
3. `ProductRepository` - 70 edges
4. `TailwindConfigGenerator` - 58 edges
5. `ProductControllerIntegrationTest` - 55 edges
6. `Order` - 52 edges
7. `UserId` - 49 edges
8. `OrderId` - 45 edges
9. `DigitalAssetType` - 43 edges
10. `Entitlement` - 43 edges

## Surprising Connections (you probably didn't know these)
- `Catalog` --references--> `DigitalAssetType`  [INFERRED]
  docs/api-contract.md → backend/src/main/java/com/medthegprod/backend/catalog/domain/model/DigitalAssetType.java
- `Activating the product artwork transition` --references--> `ProductResponse`  [INFERRED]
  docs/frontend-motion.md → backend/src/main/java/com/medthegprod/backend/catalog/infrastructure/web/dto/ProductResponse.java
- `Purchase Boundary` --references--> `OrderPaidEvent`  [INFERRED]
  docs/architecture/current-state.md → backend/src/main/java/com/medthegprod/backend/sales/application/event/OrderPaidEvent.java
- `S. Payment success and cancel` --references--> `OrderResponse`  [INFERRED]
  docs/medtheg-frontend-design-specification.md → backend/src/main/java/com/medthegprod/backend/sales/infrastructure/web/dto/OrderResponse.java
- `Final polish notes` --references--> `ProductResponse`  [INFERRED]
  docs/frontend-motion.md → backend/src/main/java/com/medthegprod/backend/catalog/infrastructure/web/dto/ProductResponse.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Backend modular business modules** — docs_architecture_backend_identity_module, docs_architecture_backend_catalog_module, docs_architecture_backend_sales_module, docs_architecture_backend_entitlement_module, docs_architecture_backend_library_module [EXTRACTED 1.00]
- **Local observability stack** — docs_architecture_deployment_prometheus, docs_architecture_deployment_grafana, infrastructure_monitoring_grafana_provisioning_datasources_prometheus_prometheus_datasource [EXTRACTED 1.00]
- **Purchase to ownership flow** — docs_architecture_sales_stripe_webhook, docs_architecture_sales_orderpaidevent, docs_architecture_overview_entitlement_ownership [EXTRACTED 1.00]
- **medtheg backend Prometheus scrape configuration** — infrastructure_monitoring_prometheus_prometheus_medtheg_backend, infrastructure_monitoring_prometheus_prometheus_actuator_prometheus_endpoint, infrastructure_monitoring_prometheus_prometheus_docker_backend_target [EXTRACTED 1.00]

## Communities (260 total, 87 thin omitted)

### Community 0 - "Money"
Cohesion: 0.14
Nodes (9): AssetId, DigitalAsset, Money, AddProductAssetServiceTest, GetProductCoverUrlServiceTest, GetProductPreviewUrlServiceTest, GetProductServiceTest, RemoveProductAssetServiceTest (+1 more)

### Community 1 - "design_system.py"
Cohesion: 0.05
Nodes (10): load_env(), generate_html(), get_deliverable_info(), get_image_base64(), main(), format_brief(), format_results(), main() (+2 more)

### Community 2 - "UserPersistenceAdapterIntegrationTest.java"
Cohesion: 0.09
Nodes (3): AuthControllerIntegrationTest, MetricsIntegrationTest, ObservabilityIntegrationTest

### Community 3 - "org.springframework.transaction.annotation.Transactional"
Cohesion: 0.06
Nodes (16): GetProductAssetUseCase, GrantEntitlementService, ListCustomerEntitlementsService, Entitlement, EntitlementId, EntitlementStatus, ACTIVE, REVOKED (+8 more)

### Community 4 - "validate_data.py"
Cohesion: 0.07
Nodes (42): read_rows(), TestAccessibilityGuidance, TestChartsTypographyAndIcons, TestCurrentReactGuidance, TestSemanticColors, _catalog_date(), _check_app_interface_contract(), _check_catalog_contract() (+34 more)

### Community 5 - "test_data_contracts.py"
Cohesion: 0.07
Nodes (12): apply_decision_rules(), _object_without_duplicates(), parse_decision_rules(), _validate_action(), read_rows(), split_values(), style_identities(), TestGeneratedCatalogContract (+4 more)

### Community 7 - "uuid"
Cohesion: 0.09
Nodes (3): LibraryAsset, LibraryItem, LibraryAssetResponse

### Community 8 - "BusinessMetrics"
Cohesion: 0.12
Nodes (8): BusinessMetrics, MicrometerBusinessMetrics, MicrometerSalesMetrics, OrderPaidMetricsListener, ProductPublishedMetricsListener, SalesMetrics, ProductPublicationMetricsIntegrationTest, OrderPaidMetricsListenerTest

### Community 9 - "GlobalRestExceptionHandler.java"
Cohesion: 0.08
Nodes (16): ProductNotFoundException, InvalidCredentialsException, UserAlreadyExistsException, UserSuspendedException, ApiError, GlobalRestExceptionHandler, LibraryAccessDeniedException, OrderAccessDeniedException (+8 more)

### Community 11 - "gray"
Cohesion: 0.05
Nodes (53): $type, $value, $type, $value, $type, $value, $type, $value (+45 more)

### Community 12 - "generate_design_system"
Cohesion: 0.12
Nodes (7): ansi_ljust(), format_ascii_box(), format_markdown(), generate_design_system(), hex_to_ansi(), section_header(), TestPersistence

### Community 13 - "scripts/core.py"
Cohesion: 0.06
Nodes (31): BM25, _contains_phrase(), detect_domain(), _domain_keywords(), _exact_match_diagnostic(), _exact_row_identity(), _exact_stack_identifier(), _file_signature() (+23 more)

### Community 16 - "Tailwind CSS Utility Reference"
Cohesion: 0.05
Nodes (43): Arbitrary Values, Aspect Ratio, Background Colors, Border Color, Border Radius, Border Style, Border Width, Borders (+35 more)

### Community 17 - "package.json"
Cohesion: 0.05
Nodes (42): dependencies, react, react-dom, react-router-dom, tailwindcss, @tailwindcss/vite, @tanstack/react-query, zustand (+34 more)

### Community 18 - "HomePage.tsx"
Cohesion: 0.10
Nodes (25): Activating the product artwork transition, Archive and artwork layers, Final polish notes, Hero dust formation, MedTheG motion implementation notes, Preview transport, CoverUrlDto, ArtworkPresentation (+17 more)

### Community 19 - "Order"
Cohesion: 0.09
Nodes (4): Order, OrderEntity, OrderItemEntity, OrderMapper

### Community 20 - "Payment"
Cohesion: 0.07
Nodes (9): Payment, PaymentStatus, COMPLETED, CREATED, FAILED, PaymentEntity, PaymentMapper, PaymentPersistenceAdapter (+1 more)

### Community 21 - "ProductRepository"
Cohesion: 0.10
Nodes (16): EventPublisher, CreateProductService, GetProductAssetService, GetProductCoverUrlService, GetProductPreviewUrlService, GetProductService, ListProductAssetsService, PublishProductService (+8 more)

### Community 22 - "react"
Cohesion: 0.09
Nodes (19): Button(), ButtonProps, Dialog(), DialogProps, EmptyStateProps, ErrorStateProps, Input(), InputProps (+11 more)

### Community 23 - "Brand Guidelines v1.0"
Cohesion: 0.05
Nodes (37): 1. Color Palette, 2. Typography, 3. Logo Usage, 4. Voice & Tone, 5. Imagery Guidelines, 6. Design Components, Accessibility, AI Image Generation (+29 more)

### Community 24 - "ProductType"
Cohesion: 0.10
Nodes (17): UpdateProductService, CreateProductUseCase, UpdateProductUseCase, ProductCategory, BEATS, BUNDLES, COURSES, KITS (+9 more)

### Community 25 - "Design"
Cohesion: 0.06
Nodes (35): Banner Design (Built-in), Banner: Design Rules, Banner: Quick Size Reference, Banner: Top Art Styles, Banner: Workflow, CIP Design (Built-in), CIP: Generate Brief, CIP: Generate Mockups (+27 more)

### Community 26 - "Canvas Design System"
Cohesion: 0.06
Nodes (35): 1. Visual Communication First, 2. Minimal Text Integration, 3. Expert Craftsmanship, 4. Systematic Patterns, Analog Meditation, Approach, Canvas Boundaries, Canvas Design System (+27 more)

### Community 27 - "OrderItem"
Cohesion: 0.15
Nodes (3): OrderItem, OrderItemId, OrderTest

### Community 28 - "StripeWebhookController.java"
Cohesion: 0.13
Nodes (3): MarkOrderAsPaidUseCase, StripeWebhookController, StripeWebhookControllerTest

### Community 29 - "Form & Input Components"
Cohesion: 0.06
Nodes (32): Accordion, Alert, Alert Dialog, Avatar, Badge, Button, Card, Checkbox (+24 more)

### Community 30 - "Tailwind CSS Responsive Design"
Cohesion: 0.06
Nodes (32): 1. Mobile-First Design, 2. Consistent Breakpoint Usage, 3. Test at Breakpoint Boundaries, 4. Use Container for Content Width, 5. Progressive Enhancement, 6. Avoid Too Many Breakpoints, Best Practices, Breakpoint System (+24 more)

### Community 31 - "DigitalAssetType"
Cohesion: 0.07
Nodes (17): UploadProductAssetService, ProductAsset, UploadProductAssetUseCase, DigitalAssetType, AUDIO_MP3, AUDIO_PREVIEW, AUDIO_WAV, IMAGE (+9 more)

### Community 32 - "AudioPlayer.tsx"
Cohesion: 0.13
Nodes (17): audioController, AudioState, AudioStatus, AudioStore, AudioTrack, useAudioStore, AudioPlayer(), GlobalMiniPlayer() (+9 more)

### Community 33 - "AppRoutes.tsx"
Cohesion: 0.12
Nodes (32): getErrorMessage(), isApiError(), getLibrary(), getLibraryAssetDownloadUrl(), getOrder(), getOrders(), queryKeys, AppRoutes() (+24 more)

### Community 34 - "Typography Specifications"
Cohesion: 0.06
Nodes (30): Accessibility, Base System, Best Practices, Clean & Modern, Common Font Pairings, Contrast Requirements, CSS Implementation, Editorial (+22 more)

### Community 35 - "slide_search_core.py"
Cohesion: 0.08
Nodes (17): format_context(), format_result(), main(), BM25, calculate_pattern_break(), detect_domain(), get_background_config(), get_color_for_emotion() (+9 more)

### Community 36 - "DesignSystemGenerator"
Cohesion: 0.05
Nodes (13): _contrast_ratio(), _derive_dark_palette(), DesignSystemGenerator, _filter_anti_patterns_for_mode(), _palette_is_dark(), _relative_luminance(), _resolve_dial(), _select_palette_for_mode() (+5 more)

### Community 37 - "OrderRepository"
Cohesion: 0.14
Nodes (12): EventPublisher, CreatePaymentService, GetOrderService, MarkOrderAsPaidService, OrderStatus, CANCELLED, PAID, PENDING (+4 more)

### Community 38 - "UserId"
Cohesion: 0.07
Nodes (19): UserId, ListCustomerOrdersService, CreateOrderUseCase, Item, CreatePaymentUseCase, PaymentResult, GetOrderUseCase, ListCustomerOrdersUseCase (+11 more)

### Community 39 - "MedTheG Frontend Design Specification"
Cohesion: 0.07
Nodes (30): A. Design concept — The Monochrome Pressing Room, AA. Component architecture, AB. State architecture, AC. Frontend folder architecture, AD. Implementation order (after design approval), API and design caveats to preserve, B. UX/UI Pro research synthesis, D. Monochrome design tokens (+22 more)

### Community 40 - "Logo Usage Rules"
Cohesion: 0.07
Nodes (28): Absolute Don'ts, Approved Backgrounds, Before Using Logo, Clear Space, Co-branding, Color Rules, Color Usage, Color Variants (+20 more)

### Community 41 - "Component Specifications"
Cohesion: 0.07
Nodes (28): Alert, Anatomy, Anatomy, Anatomy, Anatomy, Anatomy, Badge, Button (+20 more)

### Community 42 - "html-token-validator.py"
Cohesion: 0.11
Nodes (12): get_context(), is_allowed_exception(), is_allowed_rgba(), is_inside_block(), load_css_variables(), main(), print_result(), print_summary() (+4 more)

### Community 43 - "shadcn/ui Accessibility Patterns"
Cohesion: 0.07
Nodes (28): Accordion, Alert, ARIA Labels, Checkbox and Radio, Color Contrast, Command Palette Navigation, Component-Specific Patterns, Dialog/Modal Navigation (+20 more)

### Community 45 - "OrderPaidEvent"
Cohesion: 0.36
Nodes (4): GrantEntitlementUseCase, OrderPaidEventListener, OrderPaidEvent, OrderPaidEventListenerTest

### Community 46 - "client.ts"
Cohesion: 0.13
Nodes (20): loginRequest(), registerRequest(), apiBaseUrl, parseResponseBody(), setApiTokenProvider(), setApiUnauthorizedHandler(), TokenProvider, UnauthorizedHandler (+12 more)

### Community 47 - "io.swagger.v3.oas.annotations.tags.Tag"
Cohesion: 0.09
Nodes (16): GetProductCoverUrlUseCase, GetProductPreviewUrlUseCase, CoverUrlResponse, ProductCoverController, PreviewUrlResponse, ProductPreviewController, ListCustomerEntitlementsUseCase, EntitlementController (+8 more)

### Community 48 - "User"
Cohesion: 0.09
Nodes (9): AuthenticateUserUseCase, AuthenticationResult, RegisterUserUseCase, User, AuthController, LoginRequest, RegisterRequest, UserResponse (+1 more)

### Community 49 - "Asset Approval Checklist"
Cohesion: 0.08
Nodes (25): Accessibility, Archival, Asset Approval Checklist, Automation Support, Color Compliance, Common Issues & Fixes, Content Accessibility, Content Quality (+17 more)

### Community 50 - "Logo AI Prompt Engineering"
Cohesion: 0.08
Nodes (25): Common Pitfalls, Core Prompt Structure, Detailed Brief, Eco/Sustainable, Effective Keywords by Style, Fashion Brand, Healthcare, Industry-Specific Prompts (+17 more)

### Community 51 - "Color Palette Management"
Cohesion: 0.08
Nodes (24): Accessibility Requirements, Brand Compliance Validation, Checking Contrast, Color Documentation Format, Color Extraction, Color Palette Examples, Color Palette Management, Color System Structure (+16 more)

### Community 52 - "CIP Deliverable Guide"
Cohesion: 0.08
Nodes (24): Apparel, Business Card, Car/Sedan, CIP Deliverable Guide, Core Identity, Digital Assets, Email Signature, Envelope (+16 more)

### Community 53 - "States and Variants"
Cohesion: 0.08
Nodes (24): Accessibility, Accessibility Requirements, ARIA States, Color Contrast, Color Variants, Disabled States, Error Messages, Error States (+16 more)

### Community 54 - "UI Styling Skill"
Cohesion: 0.08
Nodes (24): Accessibility Patterns, Alternative: Tailwind-Only Setup, Best Practices, Common Patterns, Component Layer: shadcn/ui, Component Library Guide, Component + Styling Setup, Core Stack (+16 more)

### Community 55 - "TestStyleTaxonomy"
Cohesion: 0.10
Nodes (6): _query_wants_dark(), _resolve_color_mode(), _style_is_dark_primary(), TestModeResolution, read_rows(), TestStyleTaxonomy

### Community 56 - "ProductId"
Cohesion: 0.06
Nodes (17): ProductPublishedEvent, AddProductAssetService, ArchiveProductService, GetAdminProductService, RemoveProductAssetService, AddProductAssetUseCase, ArchiveProductUseCase, GetAdminProductUseCase (+9 more)

### Community 57 - "@tanstack/react-query"
Cohesion: 0.31
Nodes (4): queryClient, App(), react-dom, @tanstack/react-query

### Community 58 - "Workflow"
Cohesion: 0.08
Nodes (23): Art Direction Styles (Reuse from Banner), Color & Contrast, Design Best Practices, HTML Design Rules, HTML Template Structure, Option A: Chrome Headless CLI (Recommended — zero dependencies), Option B: chrome-devtools skill, Option C: Playwright script (+15 more)

### Community 60 - "cip/core.py"
Cohesion: 0.06
Nodes (13): BM25, detect_domain(), get_cip_brief(), _load_csv(), search(), search_all(), _search_csv(), BM25 (+5 more)

### Community 61 - "Design System"
Cohesion: 0.09
Nodes (22): Best Practices, Chart.js Integration, Command, Component Spec Pattern, Contextual Decision Flow, Decision System CSVs, Design System, Integration (+14 more)

### Community 62 - "Tailwind CSS Customization"
Cohesion: 0.09
Nodes (22): @apply Directive, Best Practices, Color Customization, Complete Tailwind Config, Configuration Examples, Content Configuration, Custom Color Palette, Custom Font Sizes (+14 more)

### Community 63 - "TailwindConfigGenerator"
Cohesion: 0.08
Nodes (4): main(), TailwindConfigGenerator, _strip_to_object(), TestGeneratedConfigIsValidJs

### Community 64 - "icon/generate.py"
Cohesion: 0.10
Nodes (13): apply_color(), apply_viewbox_size(), extract_svgs(), generate_batch(), generate_icon(), generate_sizes(), load_env(), main() (+5 more)

### Community 65 - "dto.ts"
Cohesion: 0.13
Nodes (20): apiGet(), CatalogQuery, CreateOrderItemRequest, CreateOrderRequest, CreatePaymentResponseDto, DownloadUrlDto, LibraryAssetDto, LibraryItemDto (+12 more)

### Community 66 - "generate-slide.py"
Cohesion: 0.13
Nodes (11): _e(), generate_chart_slide(), generate_cta_slide(), generate_deck(), generate_metrics_slide(), generate_problem_slide(), generate_solution_slide(), generate_testimonial_slide() (+3 more)

### Community 67 - "spacing"
Cohesion: 0.09
Nodes (22): $type, $value, $type, $value, $type, $value, $type, $value (+14 more)

### Community 68 - "RegisterUserServiceTest.java"
Cohesion: 0.12
Nodes (7): PasswordHasher, TokenGenerator, AuthenticateUserService, RegisterUserService, UserRepository, RegisterUserServiceTest, AuthTestHelper

### Community 69 - "UserEntity"
Cohesion: 0.12
Nodes (9): Role, ADMIN, CUSTOMER, UserStatus, ACTIVE, SUSPENDED, UserEntity, UserMapper (+1 more)

### Community 70 - "MinioAssetStorageAdapter.java"
Cohesion: 0.14
Nodes (4): MinioAssetStorageAdapter, MinioProperties, StripePaymentAdapter, StripeProperties

### Community 71 - "ProductPage.tsx"
Cohesion: 0.21
Nodes (13): CartLine(), formatPrice(), Badge(), BadgeProps, useProductQuery(), CartPage(), formatPrice(), formatPrice() (+5 more)

### Community 72 - "test_text_layout_resilience.py"
Cohesion: 0.18
Nodes (3): read_rows(), TestTextLayoutDataContracts, TestTextLayoutRetrieval

### Community 74 - "Routing by Task Type"
Cohesion: 0.10
Nodes (19): Banner Design Tasks, Brand Identity Tasks, Component Creation, Corporate Identity Program Tasks, Design Routing Guide, Design System Migration, Icon Design Tasks, Implementation Tasks (+11 more)

### Community 75 - "shadcn/ui Theming & Customization"
Cohesion: 0.10
Nodes (19): Base Color Presets, Best Practices, Color Customization, Color Format, Component Customization, CSS Variable System, Customize Styles, Customize Variants (+11 more)

### Community 78 - "DigitalAssetTypeEntity"
Cohesion: 0.12
Nodes (11): DigitalAssetEntity, DigitalAssetTypeEntity, AUDIO_MP3, AUDIO_PREVIEW, AUDIO_WAV, IMAGE, MIDI, OTHER (+3 more)

### Community 79 - ".generate"
Cohesion: 0.27
Nodes (4): PaymentGateway, PaymentSession, CreatePaymentServiceTest, GetOrderServiceTest

### Community 81 - "compilerOptions"
Cohesion: 0.10
Nodes (19): compilerOptions, allowArbitraryExtensions, allowImportingTsExtensions, erasableSyntaxOnly, jsx, lib, module, moduleDetection (+11 more)

### Community 82 - "Asset Organization Guide"
Cohesion: 0.11
Nodes (18): Asset Entry (manifest.json), Asset Organization Guide, By Campaign, By Status, By Type, Cleanup Workflow, Components, Directory Structure (+10 more)

### Community 83 - "Primary Color Meanings"
Cohesion: 0.11
Nodes (18): Accessibility Considerations, Analogous, Black, Blue, Color Combinations by Industry, Color Harmony Types, Complementary, Green (+10 more)

### Community 84 - "Core Logo Types"
Cohesion: 0.11
Nodes (18): 1. Wordmark (Logotype), 2. Lettermark (Monogram), 3. Pictorial Mark (Brand Mark), 4. Abstract Mark, 5. Mascot, 6. Emblem, 7. Combination Mark, Aesthetic Styles (+10 more)

### Community 85 - "persist_design_system"
Cohesion: 0.13
Nodes (7): _detect_page_type(), format_master_md(), format_page_override_md(), _generate_intelligent_overrides(), persist_design_system(), safe_slug(), _write_persisted_file()

### Community 86 - "fetch-background.py"
Cohesion: 0.16
Nodes (9): generate_css_for_background(), get_background_image(), get_curated_images(), get_overlay_css(), get_pexels_search_url(), load_backgrounds_config(), load_brand_colors(), main() (+1 more)

### Community 87 - "color"
Cohesion: 0.11
Nodes (19): $type, $value, background, foreground, muted-foreground, primary, primary-hover, secondary (+11 more)

### Community 88 - "useAdminProductMutations.ts"
Cohesion: 0.15
Nodes (25): archiveProduct(), createProduct(), deleteProductAsset(), getAdminProduct(), ProductInput, publishProduct(), updateProduct(), uploadProductAsset() (+17 more)

### Community 89 - "Brand Consistency Checklist"
Cohesion: 0.11
Nodes (17): Audit Frequency, Brand Consistency Checklist, Channel Audit, Collateral, Colors, Common Issues, Email, Imagery (+9 more)

### Community 90 - "CIP Mockup Prompt Engineering"
Cohesion: 0.11
Nodes (17): Apparel (Polo/T-Shirt), Base Prompt Structure, Business Card, CIP Mockup Prompt Engineering, Context Modifiers, Corporate Minimal, Deliverable-Specific Modifiers, Letterhead (+9 more)

### Community 91 - "Color Semantics"
Cohesion: 0.11
Nodes (17): Accent, Applying Semantic Tokens, Background & Foreground, Border & Ring, Color Semantics, Dark Mode Overrides, Destructive, Interactive States (+9 more)

### Community 92 - "MedTheG Frontend Design Direction"
Cohesion: 0.11
Nodes (18): 10. Responsive strategy, 11. Accessibility strategy, 12. Component architecture, 13. State architecture, 14. Frontend folder architecture, 15. Implementation order after design approval, 1. Design concept, 3. Route map (+10 more)

### Community 93 - "compilerOptions"
Cohesion: 0.12
Nodes (16): compilerOptions, allowImportingTsExtensions, erasableSyntaxOnly, lib, module, moduleDetection, noEmit, noFallthroughCasesInSwitch (+8 more)

### Community 94 - "Design Principles"
Cohesion: 0.12
Nodes (15): 22 Art Direction Styles, Banner Sizes & Art Direction Styles Reference, Complete Banner Sizes, CTA Rules, Design Principles, Pinterest Research Queries, Print, Print Specs (+7 more)

### Community 95 - "Design Principles"
Cohesion: 0.12
Nodes (15): 22 Art Direction Styles, Banner Sizes & Art Direction Styles Reference, Complete Banner Sizes, CTA Rules, Design Principles, Pinterest Research Queries, Print, Print Specs (+7 more)

### Community 96 - "generate_cip_set"
Cohesion: 0.24
Nodes (6): build_cip_prompt(), check_logo_required(), generate_cip_set(), generate_with_nano_banana(), load_logo_image(), main()

### Community 97 - "fontSize"
Cohesion: 0.12
Nodes (16): $type, $value, $type, $value, $type, $value, $type, $value (+8 more)

### Community 100 - "org.springframework.context.annotation.Configuration"
Cohesion: 0.24
Nodes (3): OpenApiConfig, MinioConfiguration, StripeConfiguration

### Community 101 - "extract-colors.cjs"
Cohesion: 0.20
Nodes (11): calculateCompliance(), colorDistance(), displayPalette(), extractHexColors(), findNearestBrandColor(), fs, generateImageMagickCommand(), hexToRgb() (+3 more)

### Community 102 - "CIP Design Reference"
Cohesion: 0.13
Nodes (14): CIP Brief (Start Here), CIP Design Reference, Commands, Deliverable Categories, Design Styles, Detailed References, Generate Mockups, HTML Presentation Features (+6 more)

### Community 103 - "Icon Design Reference"
Cohesion: 0.13
Nodes (14): Available Styles, CLI Options, Commands, Generate Batch Variations, Generate Multiple Sizes, Generate Single Icon, Icon Categories, Icon Design Reference (+6 more)

### Community 104 - "Copywriting Formulas"
Cohesion: 0.13
Nodes (14): AIDA (Attention-Interest-Desire-Action), Before-After-Bridge, Contrast Patterns, Copywriting Formulas, Core Formulas, Cost of Inaction, FAB (Features-Advantages-Benefits), Formula-to-Slide Mapping (+6 more)

### Community 105 - "Copywriting Formulas"
Cohesion: 0.13
Nodes (14): AIDA (Attention-Interest-Desire-Action), Before-After-Bridge, Contrast Patterns, Copywriting Formulas, Core Formulas, Cost of Inaction, FAB (Features-Advantages-Benefits), Formula-to-Slide Mapping (+6 more)

### Community 110 - "UserJpaRepository"
Cohesion: 0.20
Nodes (3): UserJpaRepository, UserPersistenceAdapter, UserPersistenceAdapterIntegrationTest

### Community 111 - "V5__create_orders_tables.sql"
Cohesion: 0.22
Nodes (12): idx_order_items_order_id, idx_order_items_product_id, idx_orders_customer_id, idx_orders_status, order_items, orders, idx_payments_order_id, payments (+4 more)

### Community 112 - "Banner Design - Multi-Format Creative Banner System"
Cohesion: 0.14
Nodes (13): Art Direction Styles (Top 10), Banner Design - Multi-Format Creative Banner System, Banner Size Quick Reference, Design Rules, Prerequisites, Security, Step 1: Gather Requirements (AskUserQuestion), Step 2: Research & Art Direction (+5 more)

### Community 113 - "Messaging Framework"
Cohesion: 0.14
Nodes (13): Core Statements, Elevator Pitches, Framework Structure, Message Architecture, Message by Audience, Message Testing, Messaging Framework, Mission Statement (+5 more)

### Community 114 - "Brand Voice Framework"
Cohesion: 0.14
Nodes (13): Brand Voice Framework, Character Spectrum, Emotion Spectrum, Language Spectrum, Step 1: Define Personality Traits, Step 2: Create Voice Chart, Step 3: Context Adaptation, Tone Spectrum (+5 more)

### Community 115 - "test_validate_tokens.py"
Cohesion: 0.18
Nodes (3): _run(), test_flags_hardcoded_hex_sharing_line_with_token(), test_token_only_line_reports_no_violation()

### Community 116 - "validate-asset.cjs"
Cohesion: 0.25
Nodes (13): checkManifest(), formatBytes(), formatOutput(), fs, main(), parseFilename(), path, RULES (+5 more)

### Community 117 - "Layout Patterns"
Cohesion: 0.14
Nodes (13): Card Styles, Component Variants, CSS Structures, Feature Grid (3 columns), Layout Decision Flow, Layout Patterns, Layout Selection by Use Case, Metric Styles (+5 more)

### Community 118 - "Tailwind Integration"
Cohesion: 0.14
Nodes (13): Animation Tokens, Base Layer, Button Example, Component Classes, CSS Variables Setup, Dark Mode Toggle, HSL Format Benefits, shadcn/ui Alignment (+5 more)

### Community 119 - "radius"
Cohesion: 0.19
Nodes (14): $type, $value, $type, $value, $type, $value, primitive, radius (+6 more)

### Community 120 - "Layout Patterns"
Cohesion: 0.14
Nodes (13): Card Styles, Component Variants, CSS Structures, Feature Grid (3 columns), Layout Decision Flow, Layout Patterns, Layout Selection by Use Case, Metric Styles (+5 more)

### Community 123 - "update.md"
Cohesion: 0.15
Nodes (12): Color Presets, Examples, Files Modified, Important, Overview, Skills Used, Step 1: Gather Brand Input, Step 2: Update Brand Guidelines (+4 more)

### Community 124 - "Logo Design Reference"
Cohesion: 0.15
Nodes (12): Available Styles, Color Psychology, Commands, Design Brief (Start Here), Detailed References, Generate Logo, Industry Defaults, Logo Design Reference (+4 more)

### Community 125 - "Token Architecture"
Cohesion: 0.15
Nodes (12): Categories, Dark Mode, File Organization, Layer 1: Primitive Tokens, Layer 2: Semantic Tokens, Layer 3: Component Tokens, Layer Overview, Migration from Flat Tokens (+4 more)

### Community 126 - "design-tokens-starter.json"
Cohesion: 0.15
Nodes (12): component, $type, $value, dark, semantic, $schema, $type, $value (+4 more)

### Community 130 - "Primitive Tokens"
Cohesion: 0.17
Nodes (11): Border Radius, Color Scales, Gray Scale, Motion / Duration, Primary Colors (Blue), Primitive Tokens, Shadows, Spacing Scale (+3 more)

### Community 131 - "embed-tokens.cjs"
Cohesion: 0.17
Nodes (8): args, fs, minimal, MINIMAL_TOKENS, path, projectRoot, tokensPath, wrapStyle

### Community 132 - "validate-tokens.cjs"
Cohesion: 0.24
Nodes (11): extensions, formatReport(), fs, getFiles(), main(), parseArgs(), path, patterns (+3 more)

### Community 133 - "card"
Cohesion: 0.20
Nodes (12): $type, $value, bg, bg, padding, shadow, card, bg (+4 more)

### Community 136 - "Core Visual Elements"
Cohesion: 0.18
Nodes (10): Color Palette, Colors, Core Visual Elements, Logo, Logo, Quick Checks, Typography, Typography (+2 more)

### Community 137 - "inject-brand-context.cjs"
Cohesion: 0.31
Nodes (10): extractColorsFromTable(), extractCoreAttributes(), extractHexColors(), extractImageStyle(), extractTypography(), extractVoice(), fs, generatePromptAddition() (+2 more)

### Community 138 - "CIP Design Style Guide"
Cohesion: 0.18
Nodes (10): Bold Dynamic, CIP Design Style Guide, Classic Traditional, Color Psychology, Corporate Minimal, Fresh Modern, Luxury Premium, Modern Tech (+2 more)

### Community 141 - "Quick Reference"
Cohesion: 0.18
Nodes (11): 10. Charts & Data (LOW), 1. Accessibility (CRITICAL), 2. Touch & Interaction (CRITICAL), 3. Performance (HIGH), 4. Style Selection (HIGH), 5. Layout & Responsive (HIGH), 6. Typography & Color (MEDIUM), 7. Animation (MEDIUM) (+3 more)

### Community 143 - "sync-brand-to-tokens.cjs"
Cohesion: 0.29
Nodes (8): adjustBrightness(), { execFileSync }, extractColorsFromMarkdown(), fs, generateColorScale(), main(), path, updateDesignTokens()

### Community 144 - "Brand"
Cohesion: 0.20
Nodes (9): Brand, Brand Sync Workflow, Quick Start, References, Routing, Scripts, Subcommands, Templates (+1 more)

### Community 145 - "Slide Strategies"
Cohesion: 0.20
Nodes (9): Common Structures, Duarte Sparkline Pattern, Matching Strategy to Context, Product Demo (6 slides), Sales Pitch (9 slides), Search Commands, Slide Strategies, Strategy Selection (+1 more)

### Community 146 - "Component Tokens"
Cohesion: 0.20
Nodes (9): Alert Tokens, Badge Tokens, Button Tokens, Card Tokens, Component Tokens, Dialog/Modal Tokens, Input Tokens, Table Tokens (+1 more)

### Community 147 - "generate-tokens.cjs"
Cohesion: 0.36
Nodes (9): flattenTokens(), fs, generateCSS(), generateTailwind(), main(), parseArgs(), path, resolveReference() (+1 more)

### Community 148 - "button"
Cohesion: 0.20
Nodes (10): fg, font-size, hover-bg, button, $type, $value, $type, $value (+2 more)

### Community 149 - "duration"
Cohesion: 0.20
Nodes (10): fast, normal, slow, $type, $value, $type, $value, duration (+2 more)

### Community 150 - "Slide Strategies"
Cohesion: 0.20
Nodes (9): Common Structures, Duarte Sparkline Pattern, Matching Strategy to Context, Product Demo (6 slides), Sales Pitch (9 slides), Search Commands, Slide Strategies, Strategy Selection (+1 more)

### Community 153 - "Modular monolith decision"
Cohesion: 0.20
Nodes (7): Catalog module, DigitalAsset, Product aggregate, Architecture overview, Customer ownership entitlement, OrderPaidEvent, Med The G Prod

### Community 156 - "Entitlement module"
Cohesion: 0.22
Nodes (9): Entitlement module, OrderPaidEvent, Sales module, AssetStorage port, Library module, MinIO signed download URL, EventPublisher port, OrderPaidEvent (+1 more)

### Community 157 - "Security architecture"
Cohesion: 0.22
Nodes (8): Identity module, JWT authentication, PasswordHasher port, TokenGenerator port, User aggregate, BCrypt password hashing, OAuth2 resource server, Security architecture

### Community 158 - "2. Brand system"
Cohesion: 0.22
Nodes (9): 2. Brand system, Audio-player visual language, Color palette, Image and product-card language, Monochrome state language, Motion, hover, and interaction, Navigation language, Space, shape, depth, and borders (+1 more)

### Community 159 - "CatalogPage.tsx"
Cohesion: 0.25
Nodes (5): CatalogPage(), categoryDetails, formatPrice(), productTypes, sortOptions

### Community 161 - "input"
Cohesion: 0.29
Nodes (8): padding-x, input, $type, $value, focus-ring, padding-x, $type, $value

### Community 162 - "ui-ux-pro-max"
Cohesion: 0.25
Nodes (7): How to Use, Primary Use Cases, Recommended, Rule Categories by Priority, Skip, ui-ux-pro-max, When to Apply

### Community 163 - "Slides Reference"
Cohesion: 0.29
Nodes (6): Key Features, Knowledge Base, Slides Reference, Usage, When to Use, Workflow

### Community 164 - "HTML Slide Template"
Cohesion: 0.29
Nodes (6): Animation Classes, Background Images, Base Structure, Chart.js Integration, CSS Variables Reference, HTML Slide Template

### Community 165 - "HTML Slide Template"
Cohesion: 0.29
Nodes (6): Animation Classes, Background Images, Base Structure, Chart.js Integration, CSS Variables Reference, HTML Slide Template

### Community 166 - "Query Contract"
Cohesion: 0.29
Nodes (7): Query Contract, Step 1: Analyze User Requirements, Step 2: Generate Design System (new projects/pages), Step 2b: Persist Design System (Master + Overrides Pattern), Step 2c: Design Dials (optional), Step 3: Supplement with Detailed Searches (as needed), Step 4: Stack Guidelines

### Community 167 - "ProductStatusEntity"
Cohesion: 0.18
Nodes (10): ProductStatusEntity, ARCHIVED, DRAFT, PUBLISHED, ProductTypeEntity, BEAT, BUNDLE, COURSE (+2 more)

### Community 168 - "Stripe signed webhook"
Cohesion: 0.29
Nodes (6): Backend Spring configuration, PostgreSQL catalog persistence, ProductPersistenceAdapter, ProductRepository port, PaymentGateway port, Stripe signed webhook

### Community 169 - "products"
Cohesion: 0.33
Nodes (4): products, product_categories, idx_product_assets_product_id, product_assets

### Community 170 - "Root local dependency orchestration"
Cohesion: 0.33
Nodes (6): Root local dependency orchestration, Deployment and operations, Grafana dashboards, Prometheus metrics, Grafana dashboard provisioning, Prometheus datasource

### Community 171 - "Backend API Contract"
Cohesion: 0.29
Nodes (6): Authentication, Backend API Contract, Catalog, Entitlements and Library, Errors and Operations, Orders and Payments

### Community 172 - "Backend Current State"
Cohesion: 0.29
Nodes (6): Access and Security, Backend Current State, Catalog Boundary, Known Operational Preconditions, Purchase Boundary, Scope

### Community 174 - "Pre-Delivery Checklist"
Cohesion: 0.33
Nodes (6): Accessibility, Interaction, Layout, Light/Dark Mode, Pre-Delivery Checklist, Visual Quality

### Community 175 - "Prerequisites"
Cohesion: 0.33
Nodes (6): Available Domains, Available Stacks, How to Use This Skill, Output Formats, Prerequisites, Search Reference

### Community 176 - "ProductCategoryEntity"
Cohesion: 0.40
Nodes (5): ProductCategoryEntity, BEATS, BUNDLES, COURSES, KITS

### Community 177 - "Slides"
Cohesion: 0.33
Nodes (5): References (Knowledge Base), Routing, Slides, Subcommands, When to Use

### Community 178 - ".oxlintrc.json"
Cohesion: 0.33
Nodes (5): plugins, rules, react/only-export-components, react/rules-of-hooks, $schema

### Community 179 - "Brand Guidelines Template"
Cohesion: 0.40
Nodes (4): Brand Guidelines Template, Document Structure, Extractable Fields, Usage

### Community 180 - "$type"
Cohesion: 0.60
Nodes (5): $type, $value, border, border, border

### Community 181 - "radius"
Cohesion: 0.60
Nodes (5): radius, radius, radius, $type, $value

### Community 182 - "lg"
Cohesion: 0.60
Nodes (5): lg, $type, $value, lg, lg

### Community 183 - "sm"
Cohesion: 0.60
Nodes (5): sm, sm, sm, $type, $value

### Community 185 - "Common Rules for Professional UI"
Cohesion: 0.40
Nodes (5): Common Rules for Professional UI, Icons & Visual Elements, Interaction (App), Layout & Spacing, Light/Dark Mode Contrast

### Community 186 - "Example Workflow"
Cohesion: 0.40
Nodes (5): Example Workflow, Step 1: Analyze Requirements, Step 2: Generate Design System, Step 3: Supplement with Detailed Searches (as needed), Step 4: Stack Guidelines

### Community 189 - "W. Admin"
Cohesion: 0.40
Nodes (5): `/admin`, `/admin/products`, `/admin/products/:id` and `/edit`, `/admin/products/new`, W. Admin

### Community 190 - "padding-y"
Cohesion: 0.67
Nodes (4): padding-y, padding-y, $type, $value

### Community 191 - "xl"
Cohesion: 0.67
Nodes (4): xl, xl, $type, $value

### Community 192 - "none"
Cohesion: 0.67
Nodes (4): $type, $value, none, none

### Community 193 - "Tips for Better Results"
Cohesion: 0.50
Nodes (4): Common Sticking Points, Pre-Delivery Checklist, Query Strategy, Tips for Better Results

### Community 194 - "Backend architecture"
Cohesion: 0.50
Nodes (4): Backend architecture, Catalog module, Identity module, Library module

### Community 196 - "React Query / API client"
Cohesion: 0.50
Nodes (4): Frontend architecture, React Query / API client, React UI, Spring Boot REST API

### Community 197 - "16"
Cohesion: 0.67
Nodes (3): $type, $value, 16

### Community 198 - "1"
Cohesion: 0.67
Nodes (3): $type, $value, 1

### Community 199 - "3"
Cohesion: 0.67
Nodes (3): $type, $value, 3

### Community 200 - "8"
Cohesion: 0.67
Nodes (3): $type, $value, 8

### Community 201 - "destructive"
Cohesion: 0.67
Nodes (3): destructive, $type, $value

### Community 202 - "destructive-foreground"
Cohesion: 0.67
Nodes (3): destructive-foreground, $type, $value

### Community 203 - "muted"
Cohesion: 0.67
Nodes (3): muted, $type, $value

### Community 204 - "primary-foreground"
Cohesion: 0.67
Nodes (3): primary-foreground, $type, $value

### Community 205 - "ring"
Cohesion: 0.67
Nodes (3): ring, $type, $value

### Community 206 - "secondary-foreground"
Cohesion: 0.67
Nodes (3): secondary-foreground, $type, $value

### Community 212 - "medtheg-backend scrape job"
Cohesion: 0.67
Nodes (3): Spring Actuator Prometheus endpoint, Docker host backend target on port 8080, medtheg-backend scrape job

### Community 238 - "ProductMapper.java"
Cohesion: 0.11
Nodes (9): ProductSearchQuery, ListProductsService, ListProductsUseCase, ProductStatus, ARCHIVED, DRAFT, PUBLISHED, ProductPage (+1 more)

## Knowledge Gaps
- **1195 isolated node(s):** `fs`, `path`, `fs`, `path`, `fs` (+1190 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1856 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **87 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ProductResponse` connect `ProductId` to `uuid`, `ProductMapper.java`, `io.swagger.v3.oas.annotations.tags.Tag`, `HomePage.tsx`, `ProductType`, `DigitalAssetType`?**
  _High betweenness centrality (0.088) - this node is a cross-community bridge._
- **Why does `Final polish notes` connect `HomePage.tsx` to `ProductId`?**
  _High betweenness centrality (0.085) - this node is a cross-community bridge._
- **Are the 16 inferred relationships involving `ProductId` (e.g. with `.execute()` and `.execute()`) actually correct?**
  _`ProductId` has 16 INFERRED edges - model-reasoned connections that need verification._
- **What connects `fs`, `path`, `fs` to the rest of the system?**
  _1195 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Money` be split into smaller, more focused modules?**
  _Cohesion score 0.13671274961597543 - nodes in this community are weakly interconnected._
- **Should `design_system.py` be split into smaller, more focused modules?**
  _Cohesion score 0.05191146881287726 - nodes in this community are weakly interconnected._
- **Should `UserPersistenceAdapterIntegrationTest.java` be split into smaller, more focused modules?**
  _Cohesion score 0.08710801393728224 - nodes in this community are weakly interconnected._