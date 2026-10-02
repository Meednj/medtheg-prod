# Graph Report - medtheg-prod  (2026-10-01)

## Corpus Check
- 388 files · ~256,016 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 66 file(s) not represented in the graph (top: .csv 53, (none) 5, .old 3)

## Summary
- 3899 nodes · 7499 edges · 251 communities (171 shown, 80 thin omitted)
- Extraction: 93% EXTRACTED · 7% INFERRED · 0% AMBIGUOUS · INFERRED: 535 edges (avg confidence: 0.81)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `b6514467`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- UserId
- .generate
- org.springframework.stereotype.Component
- io.swagger.v3.oas.annotations.tags.Tag
- GlobalRestExceptionHandler.java
- Money
- Modular monolith decision
- uuid
- icon/generate.py
- org.junit.jupiter.api.Test
- EntitlementController.java
- ProductRepository
- .addCategory
- ProductController.java
- Order
- ProductControllerIntegrationTest.java
- DigitalAssetType
- AuthController.java
- UserPersistenceAdapterIntegrationTest.java
- OrderItem
- Entitlement
- GetCustomerLibraryService.java
- ProductTypeEntity
- StripeWebhookController.java
- compilerOptions
- DigitalAssetTypeEntity
- OrderId
- org.springframework.context.annotation.Bean
- Payment
- package.json
- compilerOptions
- ProductJpaRepository
- scripts/core.py
- User
- DesignSystemGenerator
- Product
- ProductEntity
- validate_data.py
- UserJpaRepository
- TestStyleTaxonomy
- org.springframework.transaction.annotation.Transactional
- gray
- UserRepository
- Tailwind CSS Utility Reference
- test_design_system_mode.py
- devDependencies
- EntitlementPersistenceAdapter.java
- EntitlementEntity
- MicrometerBusinessMetricsTest.java
- .generate
- ProductPersistenceAdapter.java
- design_system.py
- dependencies
- Brand Guidelines v1.0
- .oxlintrc.json
- BackendApplication.java
- scripts
- Flyway migrations
- React Query / API client
- OrderRepository
- tsconfig.json
- medtheg-backend scrape job
- DownloadUrlResponse.java
- Frontend HTML entrypoint
- generate-slide.py
- Vite
- Graphify repository guidance
- ProductId
- Medtheg Hero Artwork
- React Logo
- Infrastructure local data services
- com.backend:backend
- Design
- Canvas Design System
- pathlib
- Form & Input Components
- Tailwind CSS Responsive Design
- Typography Specifications
- slide_search_core.py
- MedTheG Frontend Design Specification
- Logo Usage Rules
- Component Specifications
- html-token-validator.py
- shadcn/ui Accessibility Patterns
- TestTailwindConfigGenerator
- Asset Approval Checklist
- Logo AI Prompt Engineering
- Color Palette Management
- CIP Deliverable Guide
- States and Variants
- UI Styling Skill
- Workflow
- StoreHeader.tsx
- Design System
- Tailwind CSS Customization
- spacing
- TailwindConfigGenerator
- Routing by Task Type
- shadcn/ui Theming & Customization
- TestThresholdGate
- Asset Organization Guide
- Primary Color Meanings
- Core Logo Types
- fetch-background.py
- color
- ProductSearchQuery
- Brand Consistency Checklist
- CIP Mockup Prompt Engineering
- Color Semantics
- MedTheG Frontend Design Direction
- Design Principles
- Design Principles
- fontSize
- TestShadcnInstaller
- extract-colors.cjs
- CIP Design Reference
- Icon Design Reference
- Copywriting Formulas
- cip/core.py
- Copywriting Formulas
- UserEntity
- CatalogRefreshTest
- Banner Design - Multi-Format Creative Banner System
- Messaging Framework
- Brand Voice Framework
- validate-asset.cjs
- Layout Patterns
- Tailwind Integration
- radius
- Layout Patterns
- TestWebStackFreshness
- EntitlementRepository
- update.md
- Logo Design Reference
- org.springframework.boot.test.context.SpringBootTest
- BM25
- Token Architecture
- json
- design-tokens-starter.json
- Slides
- ProductStatusEntity
- TestDomainDetection
- TestSearchDomains
- HomePage.tsx
- Primitive Tokens
- embed-tokens.cjs
- validate-tokens.cjs
- card
- Core Visual Elements
- inject-brand-context.cjs
- CIP Design Style Guide
- cip/generate.py
- render-html.py
- Quick Reference
- ShadcnInstaller
- sync-brand-to-tokens.cjs
- Brand
- Slide Strategies
- Component Tokens
- generate-tokens.cjs
- button
- duration
- Slide Strategies
- TestNativeDesktopStackFreshness
- BM25
- test_tailwind_config_gen.py
- Entitlement module
- Security architecture
- 2. Brand system
- input
- ui-ux-pro-max
- .authenticate
- Slides Reference
- HTML Slide Template
- TestBm25CoreBehavior
- HTML Slide Template
- Query Contract
- TestFixtureValidation
- Stripe signed webhook
- Root local dependency orchestration
- Backend API Contract
- Backend Current State
- Pre-Delivery Checklist
- Prerequisites
- Brand Guidelines Template
- $type
- radius
- lg
- sm
- Common Rules for Professional UI
- Example Workflow
- W. Admin
- padding-y
- xl
- none
- Tips for Better Results
- Backend architecture
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
- react
- Skeleton.tsx
- AGENTS.md
- slides-create.md
- create.md
- L. Navigation

## God Nodes (most connected - your core abstractions)
1. `ProductId` - 101 edges
2. `Product` - 95 edges
3. `ProductRepository` - 65 edges
4. `TailwindConfigGenerator` - 58 edges
5. `Order` - 51 edges
6. `UserId` - 48 edges
7. `ProductControllerIntegrationTest` - 43 edges
8. `User` - 42 edges
9. `Entitlement` - 40 edges
10. `OrderId` - 40 edges

## Surprising Connections (you probably didn't know these)
- `Orders and Payments` --references--> `OrderPaidEvent`  [INFERRED]
  docs/api-contract.md → backend/src/main/java/com/medthegprod/backend/sales/application/event/OrderPaidEvent.java
- `Purchase Boundary` --references--> `OrderPaidEvent`  [INFERRED]
  docs/architecture/current-state.md → backend/src/main/java/com/medthegprod/backend/sales/application/event/OrderPaidEvent.java
- `S. Payment success and cancel` --references--> `OrderResponse`  [INFERRED]
  docs/medtheg-frontend-design-specification.md → backend/src/main/java/com/medthegprod/backend/sales/infrastructure/web/dto/OrderResponse.java
- `C. Graphify synthesis` --references--> `ProductResponse`  [INFERRED]
  docs/medtheg-frontend-design-specification.md → backend/src/main/java/com/medthegprod/backend/catalog/infrastructure/web/dto/ProductResponse.java
- `C. Graphify synthesis` --references--> `GetCustomerLibraryUseCase`  [INFERRED]
  docs/medtheg-frontend-design-specification.md → backend/src/main/java/com/medthegprod/backend/library/application/usecase/GetCustomerLibraryUseCase.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Backend modular business modules** — docs_architecture_backend_identity_module, docs_architecture_backend_catalog_module, docs_architecture_backend_sales_module, docs_architecture_backend_entitlement_module, docs_architecture_backend_library_module [EXTRACTED 1.00]
- **Local observability stack** — docs_architecture_deployment_prometheus, docs_architecture_deployment_grafana, infrastructure_monitoring_grafana_provisioning_datasources_prometheus_prometheus_datasource [EXTRACTED 1.00]
- **Purchase to ownership flow** — docs_architecture_sales_stripe_webhook, docs_architecture_sales_orderpaidevent, docs_architecture_overview_entitlement_ownership [EXTRACTED 1.00]
- **medtheg backend Prometheus scrape configuration** — infrastructure_monitoring_prometheus_prometheus_medtheg_backend, infrastructure_monitoring_prometheus_prometheus_actuator_prometheus_endpoint, infrastructure_monitoring_prometheus_prometheus_docker_backend_target [EXTRACTED 1.00]

## Communities (251 total, 80 thin omitted)

### Community 0 - "UserId"
Cohesion: 0.08
Nodes (16): UserId, ListCustomerOrdersService, CreateOrderUseCase, Item, CreatePaymentUseCase, PaymentResult, ListCustomerOrdersUseCase, OrderPage (+8 more)

### Community 2 - "org.springframework.stereotype.Component"
Cohesion: 0.06
Nodes (16): ProductPublishedEvent, BusinessMetrics, GrantEntitlementUseCase, OrderPaidEventListener, SpringEventPublisher, MicrometerBusinessMetrics, MicrometerSalesMetrics, OrderPaidMetricsListener (+8 more)

### Community 3 - "io.swagger.v3.oas.annotations.tags.Tag"
Cohesion: 0.11
Nodes (6): UploadProductAssetUseCase, AssetUploadResponse, ProductAssetController, GetAssetDownloadUrlUseCase, DownloadUrlResponse, LibraryAssetController

### Community 4 - "GlobalRestExceptionHandler.java"
Cohesion: 0.09
Nodes (16): ProductNotFoundException, InvalidCredentialsException, UserAlreadyExistsException, UserSuspendedException, ApiError, GlobalRestExceptionHandler, LibraryAccessDeniedException, OrderAccessDeniedException (+8 more)

### Community 5 - "Money"
Cohesion: 0.13
Nodes (7): Money, AddProductAssetServiceTest, GetProductPreviewUrlServiceTest, GetProductServiceTest, RemoveProductAssetServiceTest, UpdateProductServiceTest, OrderPaidEventListenerTest

### Community 6 - "Modular monolith decision"
Cohesion: 0.20
Nodes (7): Catalog module, DigitalAsset, Product aggregate, Architecture overview, Customer ownership entitlement, OrderPaidEvent, Med The G Prod

### Community 7 - "uuid"
Cohesion: 0.11
Nodes (6): OrderPaidEvent, OrderStatus, CANCELLED, PAID, PENDING, REFUNDED

### Community 8 - "icon/generate.py"
Cohesion: 0.10
Nodes (13): apply_color(), apply_viewbox_size(), extract_svgs(), generate_batch(), generate_icon(), generate_sizes(), load_env(), main() (+5 more)

### Community 10 - "EntitlementController.java"
Cohesion: 0.16
Nodes (7): GetProductPreviewUrlUseCase, PreviewUrlResponse, ProductPreviewController, ListCustomerEntitlementsUseCase, EntitlementController, GetCustomerLibraryUseCase, LibraryController

### Community 11 - "ProductRepository"
Cohesion: 0.08
Nodes (18): EventPublisher, AddProductAssetService, ArchiveProductService, CreateProductService, GetAdminProductService, GetProductAssetService, GetProductPreviewUrlService, GetProductService (+10 more)

### Community 13 - "ProductController.java"
Cohesion: 0.08
Nodes (14): ArchiveProductUseCase, GetAdminProductUseCase, ProductStatus, ARCHIVED, DRAFT, PUBLISHED, ProductController, AddProductAssetRequest (+6 more)

### Community 14 - "Order"
Cohesion: 0.10
Nodes (4): Order, OrderEntity, OrderItemEntity, OrderMapper

### Community 15 - "ProductControllerIntegrationTest.java"
Cohesion: 0.10
Nodes (3): JwtTokenGenerator, ProductPersistenceAdapterIntegrationTest, AuthControllerIntegrationTest

### Community 16 - "DigitalAssetType"
Cohesion: 0.15
Nodes (13): AddProductAssetUseCase, AssetId, DigitalAsset, DigitalAssetType, AUDIO_MP3, AUDIO_PREVIEW, AUDIO_WAV, IMAGE (+5 more)

### Community 17 - "AuthController.java"
Cohesion: 0.12
Nodes (7): AuthenticateUserUseCase, AuthenticationResult, RegisterUserUseCase, AuthController, LoginRequest, RegisterRequest, UserWebMapper

### Community 18 - "UserPersistenceAdapterIntegrationTest.java"
Cohesion: 0.17
Nodes (8): Role, ADMIN, CUSTOMER, UserStatus, ACTIVE, SUSPENDED, AuthResponse, UserResponse

### Community 19 - "OrderItem"
Cohesion: 0.15
Nodes (4): OrderItem, OrderItemId, OrderTest, OrderPersistenceAdapterIntegrationTest

### Community 20 - "Entitlement"
Cohesion: 0.14
Nodes (4): Subcommands, Entitlement, EntitlementId, EntitlementResponse

### Community 21 - "GetCustomerLibraryService.java"
Cohesion: 0.25
Nodes (5): LibraryAsset, LibraryItem, LibraryAssetResponse, LibraryItemResponse, C. Graphify synthesis

### Community 22 - "ProductTypeEntity"
Cohesion: 0.29
Nodes (6): ProductTypeEntity, BEAT, BUNDLE, COURSE, DRUM_KIT, SAMPLE_PACK

### Community 23 - "StripeWebhookController.java"
Cohesion: 0.12
Nodes (4): MarkOrderAsPaidUseCase, StripePaymentAdapter, StripeProperties, StripeWebhookController

### Community 24 - "compilerOptions"
Cohesion: 0.10
Nodes (19): compilerOptions, allowArbitraryExtensions, allowImportingTsExtensions, erasableSyntaxOnly, jsx, lib, module, moduleDetection (+11 more)

### Community 25 - "DigitalAssetTypeEntity"
Cohesion: 0.12
Nodes (11): DigitalAssetEntity, DigitalAssetTypeEntity, AUDIO_MP3, AUDIO_PREVIEW, AUDIO_WAV, IMAGE, MIDI, OTHER (+3 more)

### Community 26 - "OrderId"
Cohesion: 0.19
Nodes (6): PaymentGateway, PaymentSession, CreatePaymentService, OrderId, PaymentRepository, CreatePaymentServiceTest

### Community 28 - "org.springframework.context.annotation.Bean"
Cohesion: 0.06
Nodes (7): JwtKeyConfig, SecurityConfig, OpenApiConfig, MinioAssetStorageAdapter, MinioConfiguration, MinioProperties, StripeConfiguration

### Community 29 - "Payment"
Cohesion: 0.07
Nodes (9): Payment, PaymentStatus, COMPLETED, CREATED, FAILED, PaymentEntity, PaymentMapper, PaymentPersistenceAdapter (+1 more)

### Community 30 - "package.json"
Cohesion: 0.10
Nodes (19): name, private, type, version, @babel/core, babel-plugin-react-compiler, oxlint, react-dom (+11 more)

### Community 31 - "compilerOptions"
Cohesion: 0.12
Nodes (16): compilerOptions, allowImportingTsExtensions, erasableSyntaxOnly, lib, module, moduleDetection, noEmit, noFallthroughCasesInSwitch (+8 more)

### Community 33 - "scripts/core.py"
Cohesion: 0.06
Nodes (31): BM25, _contains_phrase(), detect_domain(), _domain_keywords(), _exact_match_diagnostic(), _exact_row_identity(), _exact_stack_identifier(), _file_signature() (+23 more)

### Community 35 - "DesignSystemGenerator"
Cohesion: 0.06
Nodes (13): DesignSystemGenerator, apply_decision_rules(), _object_without_duplicates(), parse_decision_rules(), _validate_action(), TestReasoningMatch, read_rows(), split_values() (+5 more)

### Community 36 - "Product"
Cohesion: 0.09
Nodes (16): UpdateProductService, CreateProductUseCase, UpdateProductUseCase, Product, ProductCategory, BEATS, BUNDLES, COURSES (+8 more)

### Community 38 - "validate_data.py"
Cohesion: 0.07
Nodes (43): read_rows(), TestAccessibilityGuidance, TestChartsTypographyAndIcons, TestCurrentReactGuidance, TestSemanticColors, _catalog_date(), _check_app_interface_contract(), _check_catalog_contract() (+35 more)

### Community 39 - "UserJpaRepository"
Cohesion: 0.24
Nodes (3): UserJpaRepository, UserPersistenceAdapter, UserPersistenceAdapterIntegrationTest

### Community 44 - "gray"
Cohesion: 0.05
Nodes (53): $type, $value, $type, $value, $type, $value, $type, $value (+45 more)

### Community 45 - "UserRepository"
Cohesion: 0.22
Nodes (6): PasswordHasher, TokenGenerator, AuthenticateUserService, RegisterUserService, UserRepository, RegisterUserServiceTest

### Community 46 - "Tailwind CSS Utility Reference"
Cohesion: 0.05
Nodes (43): Arbitrary Values, Aspect Ratio, Background Colors, Border Color, Border Radius, Border Style, Border Width, Borders (+35 more)

### Community 47 - "test_design_system_mode.py"
Cohesion: 0.08
Nodes (12): _contrast_ratio(), _derive_dark_palette(), _palette_is_dark(), _query_wants_dark(), _relative_luminance(), _resolve_color_mode(), _select_palette_for_mode(), _style_is_dark_primary() (+4 more)

### Community 48 - "devDependencies"
Cohesion: 0.17
Nodes (12): devDependencies, @babel/core, babel-plugin-react-compiler, oxlint, @rolldown/plugin-babel, @types/babel__core, @types/node, @types/react (+4 more)

### Community 49 - "EntitlementPersistenceAdapter.java"
Cohesion: 0.33
Nodes (3): EntitlementPersistenceAdapter, EntitlementMapper, EntitlementJpaRepository

### Community 50 - "EntitlementEntity"
Cohesion: 0.20
Nodes (4): EntitlementStatus, ACTIVE, REVOKED, EntitlementEntity

### Community 53 - ".generate"
Cohesion: 0.16
Nodes (3): _filter_anti_patterns_for_mode(), _resolve_dial(), TestAntiPatternGating

### Community 54 - "ProductPersistenceAdapter.java"
Cohesion: 0.16
Nodes (6): ProductCategoryEntity, BEATS, BUNDLES, COURSES, KITS, ProductSpecifications

### Community 55 - "design_system.py"
Cohesion: 0.05
Nodes (17): ansi_ljust(), _detect_page_type(), format_ascii_box(), format_markdown(), format_master_md(), format_page_override_md(), generate_design_system(), _generate_intelligent_overrides() (+9 more)

### Community 56 - "dependencies"
Cohesion: 0.29
Nodes (7): dependencies, react, react-dom, react-router-dom, tailwindcss, @tailwindcss/vite, @tanstack/react-query

### Community 57 - "Brand Guidelines v1.0"
Cohesion: 0.05
Nodes (37): 1. Color Palette, 2. Typography, 3. Logo Usage, 4. Voice & Tone, 5. Imagery Guidelines, 6. Design Components, Accessibility, AI Image Generation (+29 more)

### Community 58 - ".oxlintrc.json"
Cohesion: 0.33
Nodes (5): plugins, rules, react/only-export-components, react/rules-of-hooks, $schema

### Community 60 - "scripts"
Cohesion: 0.40
Nodes (5): scripts, build, dev, lint, preview

### Community 62 - "React Query / API client"
Cohesion: 0.50
Nodes (4): Frontend architecture, React Query / API client, React UI, Spring Boot REST API

### Community 63 - "OrderRepository"
Cohesion: 0.12
Nodes (8): GetProductUseCase, EventPublisher, CreateOrderService, GetOrderService, MarkOrderAsPaidService, GetOrderUseCase, OrderRepository, CreateOrderServiceTest

### Community 65 - "medtheg-backend scrape job"
Cohesion: 0.67
Nodes (3): Spring Actuator Prometheus endpoint, Docker host backend target on port 8080, medtheg-backend scrape job

### Community 68 - "generate-slide.py"
Cohesion: 0.13
Nodes (11): _e(), generate_chart_slide(), generate_cta_slide(), generate_deck(), generate_metrics_slide(), generate_problem_slide(), generate_solution_slide(), generate_testimonial_slide() (+3 more)

### Community 71 - "ProductId"
Cohesion: 0.13
Nodes (5): ListProductAssetsService, ListProductAssetsUseCase, ProductAsset, ProductId, ProductAssetResponse

### Community 76 - "Design"
Cohesion: 0.06
Nodes (35): Banner Design (Built-in), Banner: Design Rules, Banner: Quick Size Reference, Banner: Top Art Styles, Banner: Workflow, CIP Design (Built-in), CIP: Generate Brief, CIP: Generate Mockups (+27 more)

### Community 77 - "Canvas Design System"
Cohesion: 0.06
Nodes (35): 1. Visual Communication First, 2. Minimal Text Integration, 3. Expert Craftsmanship, 4. Systematic Patterns, Analog Meditation, Approach, Canvas Boundaries, Canvas Design System (+27 more)

### Community 78 - "pathlib"
Cohesion: 0.07
Nodes (5): main(), TestMetricMath, read_rows(), TestTextLayoutDataContracts, TestTextLayoutRetrieval

### Community 79 - "Form & Input Components"
Cohesion: 0.06
Nodes (32): Accordion, Alert, Alert Dialog, Avatar, Badge, Button, Card, Checkbox (+24 more)

### Community 80 - "Tailwind CSS Responsive Design"
Cohesion: 0.06
Nodes (32): 1. Mobile-First Design, 2. Consistent Breakpoint Usage, 3. Test at Breakpoint Boundaries, 4. Use Container for Content Width, 5. Progressive Enhancement, 6. Avoid Too Many Breakpoints, Best Practices, Breakpoint System (+24 more)

### Community 81 - "Typography Specifications"
Cohesion: 0.06
Nodes (30): Accessibility, Base System, Best Practices, Clean & Modern, Common Font Pairings, Contrast Requirements, CSS Implementation, Editorial (+22 more)

### Community 82 - "slide_search_core.py"
Cohesion: 0.08
Nodes (17): format_context(), format_result(), main(), BM25, calculate_pattern_break(), detect_domain(), get_background_config(), get_color_for_emotion() (+9 more)

### Community 83 - "MedTheG Frontend Design Specification"
Cohesion: 0.07
Nodes (30): A. Design concept — The Monochrome Pressing Room, AA. Component architecture, AB. State architecture, AC. Frontend folder architecture, AD. Implementation order (after design approval), API and design caveats to preserve, B. UX/UI Pro research synthesis, D. Monochrome design tokens (+22 more)

### Community 84 - "Logo Usage Rules"
Cohesion: 0.07
Nodes (28): Absolute Don'ts, Approved Backgrounds, Before Using Logo, Clear Space, Co-branding, Color Rules, Color Usage, Color Variants (+20 more)

### Community 85 - "Component Specifications"
Cohesion: 0.07
Nodes (28): Alert, Anatomy, Anatomy, Anatomy, Anatomy, Anatomy, Badge, Button (+20 more)

### Community 86 - "html-token-validator.py"
Cohesion: 0.12
Nodes (12): get_context(), is_allowed_exception(), is_allowed_rgba(), is_inside_block(), load_css_variables(), main(), print_result(), print_summary() (+4 more)

### Community 87 - "shadcn/ui Accessibility Patterns"
Cohesion: 0.07
Nodes (28): Accordion, Alert, ARIA Labels, Checkbox and Radio, Color Contrast, Command Palette Navigation, Component-Specific Patterns, Dialog/Modal Navigation (+20 more)

### Community 89 - "Asset Approval Checklist"
Cohesion: 0.08
Nodes (25): Accessibility, Archival, Asset Approval Checklist, Automation Support, Color Compliance, Common Issues & Fixes, Content Accessibility, Content Quality (+17 more)

### Community 90 - "Logo AI Prompt Engineering"
Cohesion: 0.08
Nodes (25): Common Pitfalls, Core Prompt Structure, Detailed Brief, Eco/Sustainable, Effective Keywords by Style, Fashion Brand, Healthcare, Industry-Specific Prompts (+17 more)

### Community 91 - "Color Palette Management"
Cohesion: 0.08
Nodes (24): Accessibility Requirements, Brand Compliance Validation, Checking Contrast, Color Documentation Format, Color Extraction, Color Palette Examples, Color Palette Management, Color System Structure (+16 more)

### Community 92 - "CIP Deliverable Guide"
Cohesion: 0.08
Nodes (24): Apparel, Business Card, Car/Sedan, CIP Deliverable Guide, Core Identity, Digital Assets, Email Signature, Envelope (+16 more)

### Community 93 - "States and Variants"
Cohesion: 0.08
Nodes (24): Accessibility, Accessibility Requirements, ARIA States, Color Contrast, Color Variants, Disabled States, Error Messages, Error States (+16 more)

### Community 94 - "UI Styling Skill"
Cohesion: 0.08
Nodes (24): Accessibility Patterns, Alternative: Tailwind-Only Setup, Best Practices, Common Patterns, Component Layer: shadcn/ui, Component Library Guide, Component + Styling Setup, Core Stack (+16 more)

### Community 95 - "Workflow"
Cohesion: 0.08
Nodes (23): Art Direction Styles (Reuse from Banner), Color & Contrast, Design Best Practices, HTML Design Rules, HTML Template Structure, Option A: Chrome Headless CLI (Recommended — zero dependencies), Option B: chrome-devtools skill, Option C: Playwright script (+15 more)

### Community 96 - "StoreHeader.tsx"
Cohesion: 0.14
Nodes (18): Button(), ButtonProps, Dialog(), DialogProps, Input(), InputProps, IconName, LineIcon() (+10 more)

### Community 97 - "Design System"
Cohesion: 0.09
Nodes (22): Best Practices, Chart.js Integration, Command, Component Spec Pattern, Contextual Decision Flow, Decision System CSVs, Design System, Integration (+14 more)

### Community 98 - "Tailwind CSS Customization"
Cohesion: 0.09
Nodes (22): @apply Directive, Best Practices, Color Customization, Complete Tailwind Config, Configuration Examples, Content Configuration, Custom Color Palette, Custom Font Sizes (+14 more)

### Community 99 - "spacing"
Cohesion: 0.09
Nodes (22): $type, $value, $type, $value, $type, $value, $type, $value (+14 more)

### Community 101 - "Routing by Task Type"
Cohesion: 0.10
Nodes (19): Banner Design Tasks, Brand Identity Tasks, Component Creation, Corporate Identity Program Tasks, Design Routing Guide, Design System Migration, Icon Design Tasks, Implementation Tasks (+11 more)

### Community 102 - "shadcn/ui Theming & Customization"
Cohesion: 0.10
Nodes (19): Base Color Presets, Best Practices, Color Customization, Color Format, Component Customization, CSS Variable System, Customize Styles, Customize Variants (+11 more)

### Community 104 - "Asset Organization Guide"
Cohesion: 0.11
Nodes (18): Asset Entry (manifest.json), Asset Organization Guide, By Campaign, By Status, By Type, Cleanup Workflow, Components, Directory Structure (+10 more)

### Community 105 - "Primary Color Meanings"
Cohesion: 0.11
Nodes (18): Accessibility Considerations, Analogous, Black, Blue, Color Combinations by Industry, Color Harmony Types, Complementary, Green (+10 more)

### Community 106 - "Core Logo Types"
Cohesion: 0.11
Nodes (18): 1. Wordmark (Logotype), 2. Lettermark (Monogram), 3. Pictorial Mark (Brand Mark), 4. Abstract Mark, 5. Mascot, 6. Emblem, 7. Combination Mark, Aesthetic Styles (+10 more)

### Community 107 - "fetch-background.py"
Cohesion: 0.16
Nodes (9): generate_css_for_background(), get_background_image(), get_curated_images(), get_overlay_css(), get_pexels_search_url(), load_backgrounds_config(), load_brand_colors(), main() (+1 more)

### Community 108 - "color"
Cohesion: 0.11
Nodes (19): $type, $value, background, foreground, muted-foreground, primary, primary-hover, secondary (+11 more)

### Community 109 - "ProductSearchQuery"
Cohesion: 0.23
Nodes (4): ProductSearchQuery, ListProductsService, ListProductsUseCase, ProductPage

### Community 110 - "Brand Consistency Checklist"
Cohesion: 0.11
Nodes (17): Audit Frequency, Brand Consistency Checklist, Channel Audit, Collateral, Colors, Common Issues, Email, Imagery (+9 more)

### Community 111 - "CIP Mockup Prompt Engineering"
Cohesion: 0.11
Nodes (17): Apparel (Polo/T-Shirt), Base Prompt Structure, Business Card, CIP Mockup Prompt Engineering, Context Modifiers, Corporate Minimal, Deliverable-Specific Modifiers, Letterhead (+9 more)

### Community 112 - "Color Semantics"
Cohesion: 0.11
Nodes (17): Accent, Applying Semantic Tokens, Background & Foreground, Border & Ring, Color Semantics, Dark Mode Overrides, Destructive, Interactive States (+9 more)

### Community 113 - "MedTheG Frontend Design Direction"
Cohesion: 0.11
Nodes (18): 10. Responsive strategy, 11. Accessibility strategy, 12. Component architecture, 13. State architecture, 14. Frontend folder architecture, 15. Implementation order after design approval, 1. Design concept, 3. Route map (+10 more)

### Community 114 - "Design Principles"
Cohesion: 0.12
Nodes (15): 22 Art Direction Styles, Banner Sizes & Art Direction Styles Reference, Complete Banner Sizes, CTA Rules, Design Principles, Pinterest Research Queries, Print, Print Specs (+7 more)

### Community 115 - "Design Principles"
Cohesion: 0.12
Nodes (15): 22 Art Direction Styles, Banner Sizes & Art Direction Styles Reference, Complete Banner Sizes, CTA Rules, Design Principles, Pinterest Research Queries, Print, Print Specs (+7 more)

### Community 116 - "fontSize"
Cohesion: 0.12
Nodes (16): $type, $value, $type, $value, $type, $value, $type, $value (+8 more)

### Community 118 - "extract-colors.cjs"
Cohesion: 0.20
Nodes (11): calculateCompliance(), colorDistance(), displayPalette(), extractHexColors(), findNearestBrandColor(), fs, generateImageMagickCommand(), hexToRgb() (+3 more)

### Community 119 - "CIP Design Reference"
Cohesion: 0.13
Nodes (14): CIP Brief (Start Here), CIP Design Reference, Commands, Deliverable Categories, Design Styles, Detailed References, Generate Mockups, HTML Presentation Features (+6 more)

### Community 120 - "Icon Design Reference"
Cohesion: 0.13
Nodes (14): Available Styles, CLI Options, Commands, Generate Batch Variations, Generate Multiple Sizes, Generate Single Icon, Icon Categories, Icon Design Reference (+6 more)

### Community 121 - "Copywriting Formulas"
Cohesion: 0.13
Nodes (14): AIDA (Attention-Interest-Desire-Action), Before-After-Bridge, Contrast Patterns, Copywriting Formulas, Core Formulas, Cost of Inaction, FAB (Features-Advantages-Benefits), Formula-to-Slide Mapping (+6 more)

### Community 122 - "cip/core.py"
Cohesion: 0.10
Nodes (11): detect_domain(), get_cip_brief(), _load_csv(), search(), search_all(), _search_csv(), detect_domain(), _load_csv() (+3 more)

### Community 123 - "Copywriting Formulas"
Cohesion: 0.13
Nodes (14): AIDA (Attention-Interest-Desire-Action), Before-After-Bridge, Contrast Patterns, Copywriting Formulas, Core Formulas, Cost of Inaction, FAB (Features-Advantages-Benefits), Formula-to-Slide Mapping (+6 more)

### Community 126 - "Banner Design - Multi-Format Creative Banner System"
Cohesion: 0.14
Nodes (13): Art Direction Styles (Top 10), Banner Design - Multi-Format Creative Banner System, Banner Size Quick Reference, Design Rules, Prerequisites, Security, Step 1: Gather Requirements (AskUserQuestion), Step 2: Research & Art Direction (+5 more)

### Community 127 - "Messaging Framework"
Cohesion: 0.14
Nodes (13): Core Statements, Elevator Pitches, Framework Structure, Message Architecture, Message by Audience, Message Testing, Messaging Framework, Mission Statement (+5 more)

### Community 128 - "Brand Voice Framework"
Cohesion: 0.14
Nodes (13): Brand Voice Framework, Character Spectrum, Emotion Spectrum, Language Spectrum, Step 1: Define Personality Traits, Step 2: Create Voice Chart, Step 3: Context Adaptation, Tone Spectrum (+5 more)

### Community 129 - "validate-asset.cjs"
Cohesion: 0.25
Nodes (13): checkManifest(), formatBytes(), formatOutput(), fs, main(), parseFilename(), path, RULES (+5 more)

### Community 130 - "Layout Patterns"
Cohesion: 0.14
Nodes (13): Card Styles, Component Variants, CSS Structures, Feature Grid (3 columns), Layout Decision Flow, Layout Patterns, Layout Selection by Use Case, Metric Styles (+5 more)

### Community 131 - "Tailwind Integration"
Cohesion: 0.14
Nodes (13): Animation Tokens, Base Layer, Button Example, Component Classes, CSS Variables Setup, Dark Mode Toggle, HSL Format Benefits, shadcn/ui Alignment (+5 more)

### Community 132 - "radius"
Cohesion: 0.19
Nodes (14): $type, $value, $type, $value, $type, $value, primitive, radius (+6 more)

### Community 133 - "Layout Patterns"
Cohesion: 0.14
Nodes (13): Card Styles, Component Variants, CSS Structures, Feature Grid (3 columns), Layout Decision Flow, Layout Patterns, Layout Selection by Use Case, Metric Styles (+5 more)

### Community 135 - "EntitlementRepository"
Cohesion: 0.23
Nodes (4): GrantEntitlementService, ListCustomerEntitlementsService, EntitlementRepository, GetCustomerLibraryService

### Community 136 - "update.md"
Cohesion: 0.15
Nodes (12): Color Presets, Examples, Files Modified, Important, Overview, Skills Used, Step 1: Gather Brand Input, Step 2: Update Brand Guidelines (+4 more)

### Community 137 - "Logo Design Reference"
Cohesion: 0.15
Nodes (12): Available Styles, Color Psychology, Commands, Design Brief (Start Here), Detailed References, Generate Logo, Industry Defaults, Logo Design Reference (+4 more)

### Community 138 - "org.springframework.boot.test.context.SpringBootTest"
Cohesion: 0.12
Nodes (4): BackendApplicationTests, MetricsIntegrationTest, ObservabilityIntegrationTest, OrderControllerIntegrationTest

### Community 140 - "Token Architecture"
Cohesion: 0.15
Nodes (12): Categories, Dark Mode, File Organization, Layer 1: Primitive Tokens, Layer 2: Semantic Tokens, Layer 3: Component Tokens, Layer Overview, Migration from Flat Tokens (+4 more)

### Community 141 - "json"
Cohesion: 0.07
Nodes (8): format_brief(), format_results(), main(), format_output(), generate_design_brief(), _run(), test_flags_hardcoded_hex_sharing_line_with_token(), test_token_only_line_reports_no_violation()

### Community 142 - "design-tokens-starter.json"
Cohesion: 0.15
Nodes (12): component, $type, $value, dark, semantic, $schema, $type, $value (+4 more)

### Community 143 - "Slides"
Cohesion: 0.40
Nodes (4): References (Knowledge Base), Routing, Slides, When to Use

### Community 144 - "ProductStatusEntity"
Cohesion: 0.33
Nodes (4): ProductStatusEntity, ARCHIVED, DRAFT, PUBLISHED

### Community 147 - "HomePage.tsx"
Cohesion: 0.19
Nodes (13): App(), AppRoutes(), Badge(), BadgeProps, BreadcrumbItem, Breadcrumbs(), homepagePreviewData, VisualPlayerProvider() (+5 more)

### Community 148 - "Primitive Tokens"
Cohesion: 0.17
Nodes (11): Border Radius, Color Scales, Gray Scale, Motion / Duration, Primary Colors (Blue), Primitive Tokens, Shadows, Spacing Scale (+3 more)

### Community 149 - "embed-tokens.cjs"
Cohesion: 0.17
Nodes (8): args, fs, minimal, MINIMAL_TOKENS, path, projectRoot, tokensPath, wrapStyle

### Community 150 - "validate-tokens.cjs"
Cohesion: 0.24
Nodes (11): extensions, formatReport(), fs, getFiles(), main(), parseArgs(), path, patterns (+3 more)

### Community 151 - "card"
Cohesion: 0.20
Nodes (12): $type, $value, bg, bg, padding, shadow, card, bg (+4 more)

### Community 154 - "Core Visual Elements"
Cohesion: 0.18
Nodes (10): Color Palette, Colors, Core Visual Elements, Logo, Logo, Quick Checks, Typography, Typography (+2 more)

### Community 155 - "inject-brand-context.cjs"
Cohesion: 0.31
Nodes (10): extractColorsFromTable(), extractCoreAttributes(), extractHexColors(), extractImageStyle(), extractTypography(), extractVoice(), fs, generatePromptAddition() (+2 more)

### Community 156 - "CIP Design Style Guide"
Cohesion: 0.18
Nodes (10): Bold Dynamic, CIP Design Style Guide, Classic Traditional, Color Psychology, Corporate Minimal, Fresh Modern, Luxury Premium, Modern Tech (+2 more)

### Community 157 - "cip/generate.py"
Cohesion: 0.19
Nodes (7): build_cip_prompt(), check_logo_required(), generate_cip_set(), generate_with_nano_banana(), load_env(), load_logo_image(), main()

### Community 158 - "render-html.py"
Cohesion: 0.21
Nodes (4): generate_html(), get_deliverable_info(), get_image_base64(), main()

### Community 159 - "Quick Reference"
Cohesion: 0.18
Nodes (11): 10. Charts & Data (LOW), 1. Accessibility (CRITICAL), 2. Touch & Interaction (CRITICAL), 3. Performance (HIGH), 4. Style Selection (HIGH), 5. Layout & Responsive (HIGH), 6. Typography & Color (MEDIUM), 7. Animation (MEDIUM) (+3 more)

### Community 161 - "sync-brand-to-tokens.cjs"
Cohesion: 0.29
Nodes (8): adjustBrightness(), { execFileSync }, extractColorsFromMarkdown(), fs, generateColorScale(), main(), path, updateDesignTokens()

### Community 162 - "Brand"
Cohesion: 0.20
Nodes (9): Brand, Brand Sync Workflow, Quick Start, References, Routing, Scripts, Subcommands, Templates (+1 more)

### Community 163 - "Slide Strategies"
Cohesion: 0.20
Nodes (9): Common Structures, Duarte Sparkline Pattern, Matching Strategy to Context, Product Demo (6 slides), Sales Pitch (9 slides), Search Commands, Slide Strategies, Strategy Selection (+1 more)

### Community 164 - "Component Tokens"
Cohesion: 0.20
Nodes (9): Alert Tokens, Badge Tokens, Button Tokens, Card Tokens, Component Tokens, Dialog/Modal Tokens, Input Tokens, Table Tokens (+1 more)

### Community 165 - "generate-tokens.cjs"
Cohesion: 0.36
Nodes (9): flattenTokens(), fs, generateCSS(), generateTailwind(), main(), parseArgs(), path, resolveReference() (+1 more)

### Community 166 - "button"
Cohesion: 0.20
Nodes (10): fg, font-size, hover-bg, button, $type, $value, $type, $value (+2 more)

### Community 167 - "duration"
Cohesion: 0.20
Nodes (10): fast, normal, slow, $type, $value, $type, $value, duration (+2 more)

### Community 168 - "Slide Strategies"
Cohesion: 0.20
Nodes (9): Common Structures, Duarte Sparkline Pattern, Matching Strategy to Context, Product Demo (6 slides), Sales Pitch (9 slides), Search Commands, Slide Strategies, Strategy Selection (+1 more)

### Community 174 - "Entitlement module"
Cohesion: 0.22
Nodes (9): Entitlement module, OrderPaidEvent, Sales module, AssetStorage port, Library module, MinIO signed download URL, EventPublisher port, OrderPaidEvent (+1 more)

### Community 175 - "Security architecture"
Cohesion: 0.22
Nodes (8): Identity module, JWT authentication, PasswordHasher port, TokenGenerator port, User aggregate, BCrypt password hashing, OAuth2 resource server, Security architecture

### Community 176 - "2. Brand system"
Cohesion: 0.22
Nodes (9): 2. Brand system, Audio-player visual language, Color palette, Image and product-card language, Monochrome state language, Motion, hover, and interaction, Navigation language, Space, shape, depth, and borders (+1 more)

### Community 177 - "input"
Cohesion: 0.29
Nodes (8): padding-x, input, $type, $value, focus-ring, padding-x, $type, $value

### Community 178 - "ui-ux-pro-max"
Cohesion: 0.25
Nodes (7): How to Use, Primary Use Cases, Recommended, Rule Categories by Priority, Skip, ui-ux-pro-max, When to Apply

### Community 181 - "Slides Reference"
Cohesion: 0.29
Nodes (6): Key Features, Knowledge Base, Slides Reference, Usage, When to Use, Workflow

### Community 182 - "HTML Slide Template"
Cohesion: 0.29
Nodes (6): Animation Classes, Background Images, Base Structure, Chart.js Integration, CSS Variables Reference, HTML Slide Template

### Community 184 - "HTML Slide Template"
Cohesion: 0.29
Nodes (6): Animation Classes, Background Images, Base Structure, Chart.js Integration, CSS Variables Reference, HTML Slide Template

### Community 185 - "Query Contract"
Cohesion: 0.29
Nodes (7): Query Contract, Step 1: Analyze User Requirements, Step 2: Generate Design System (new projects/pages), Step 2b: Persist Design System (Master + Overrides Pattern), Step 2c: Design Dials (optional), Step 3: Supplement with Detailed Searches (as needed), Step 4: Stack Guidelines

### Community 187 - "Stripe signed webhook"
Cohesion: 0.29
Nodes (6): Backend Spring configuration, PostgreSQL catalog persistence, ProductPersistenceAdapter, ProductRepository port, PaymentGateway port, Stripe signed webhook

### Community 188 - "Root local dependency orchestration"
Cohesion: 0.33
Nodes (6): Root local dependency orchestration, Deployment and operations, Grafana dashboards, Prometheus metrics, Grafana dashboard provisioning, Prometheus datasource

### Community 189 - "Backend API Contract"
Cohesion: 0.29
Nodes (6): Authentication, Backend API Contract, Catalog, Entitlements and Library, Errors and Operations, Orders and Payments

### Community 190 - "Backend Current State"
Cohesion: 0.29
Nodes (6): Access and Security, Backend Current State, Catalog Boundary, Known Operational Preconditions, Purchase Boundary, Scope

### Community 192 - "Pre-Delivery Checklist"
Cohesion: 0.33
Nodes (6): Accessibility, Interaction, Layout, Light/Dark Mode, Pre-Delivery Checklist, Visual Quality

### Community 193 - "Prerequisites"
Cohesion: 0.33
Nodes (6): Available Domains, Available Stacks, How to Use This Skill, Output Formats, Prerequisites, Search Reference

### Community 196 - "Brand Guidelines Template"
Cohesion: 0.40
Nodes (4): Brand Guidelines Template, Document Structure, Extractable Fields, Usage

### Community 197 - "$type"
Cohesion: 0.60
Nodes (5): $type, $value, border, border, border

### Community 198 - "radius"
Cohesion: 0.60
Nodes (5): radius, radius, radius, $type, $value

### Community 199 - "lg"
Cohesion: 0.60
Nodes (5): lg, $type, $value, lg, lg

### Community 200 - "sm"
Cohesion: 0.60
Nodes (5): sm, sm, sm, $type, $value

### Community 202 - "Common Rules for Professional UI"
Cohesion: 0.40
Nodes (5): Common Rules for Professional UI, Icons & Visual Elements, Interaction (App), Layout & Spacing, Light/Dark Mode Contrast

### Community 203 - "Example Workflow"
Cohesion: 0.40
Nodes (5): Example Workflow, Step 1: Analyze Requirements, Step 2: Generate Design System, Step 3: Supplement with Detailed Searches (as needed), Step 4: Stack Guidelines

### Community 204 - "W. Admin"
Cohesion: 0.40
Nodes (5): `/admin`, `/admin/products`, `/admin/products/:id` and `/edit`, `/admin/products/new`, W. Admin

### Community 205 - "padding-y"
Cohesion: 0.67
Nodes (4): padding-y, padding-y, $type, $value

### Community 206 - "xl"
Cohesion: 0.67
Nodes (4): xl, xl, $type, $value

### Community 207 - "none"
Cohesion: 0.67
Nodes (4): $type, $value, none, none

### Community 208 - "Tips for Better Results"
Cohesion: 0.50
Nodes (4): Common Sticking Points, Pre-Delivery Checklist, Query Strategy, Tips for Better Results

### Community 209 - "Backend architecture"
Cohesion: 0.50
Nodes (4): Backend architecture, Catalog module, Identity module, Library module

### Community 210 - "16"
Cohesion: 0.67
Nodes (3): $type, $value, 16

### Community 211 - "1"
Cohesion: 0.67
Nodes (3): $type, $value, 1

### Community 212 - "3"
Cohesion: 0.67
Nodes (3): $type, $value, 3

### Community 213 - "8"
Cohesion: 0.67
Nodes (3): $type, $value, 8

### Community 214 - "destructive"
Cohesion: 0.67
Nodes (3): destructive, $type, $value

### Community 215 - "destructive-foreground"
Cohesion: 0.67
Nodes (3): destructive-foreground, $type, $value

### Community 216 - "muted"
Cohesion: 0.67
Nodes (3): muted, $type, $value

### Community 217 - "primary-foreground"
Cohesion: 0.67
Nodes (3): primary-foreground, $type, $value

### Community 218 - "ring"
Cohesion: 0.67
Nodes (3): ring, $type, $value

### Community 219 - "secondary-foreground"
Cohesion: 0.67
Nodes (3): secondary-foreground, $type, $value

### Community 222 - "react"
Cohesion: 0.13
Nodes (8): EmptyStateProps, ErrorStateProps, SelectProps, ToastProps, VisualPlayerContext, VisualPlayerValue, VisualTrack, react

## Knowledge Gaps
- **1170 isolated node(s):** `fs`, `path`, `fs`, `path`, `fs` (+1165 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1804 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **80 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ShadcnInstaller` connect `ShadcnInstaller` to `.test_get_installed_components_empty`, `.test_add_components_no_components`, `.test_add_components_already_installed`, `.test_add_components_no_config`, `.test_add_components_dry_run`, `.test_add_all_components_success`, `json`, `TestShadcnInstaller`, `.check_shadcn_config`, `.test_add_all_components_dry_run`, `.test_list_installed_empty`, `.test_init_default_project_root`?**
  _High betweenness centrality (0.024) - this node is a cross-community bridge._
- **Why does `ProductId` connect `ProductId` to `.generate`, `org.springframework.stereotype.Component`, `GlobalRestExceptionHandler.java`, `Money`, `EntitlementRepository`, `uuid`, `ProductRepository`, `ProductController.java`, `Order`, `DigitalAssetType`, `OrderItem`, `Entitlement`, `.generate`, `Product`, `ProductEntity`, `EntitlementEntity`, `ProductPersistenceAdapter.java`, `OrderRepository`, `ProductSearchQuery`?**
  _High betweenness centrality (0.021) - this node is a cross-community bridge._
- **Why does `MedTheG Frontend Design Specification` connect `MedTheG Frontend Design Specification` to `frontend-design-direction.md`, `L. Navigation`, `W. Admin`, `GetCustomerLibraryService.java`?**
  _High betweenness centrality (0.020) - this node is a cross-community bridge._
- **Are the 15 inferred relationships involving `ProductId` (e.g. with `.execute()` and `.execute()`) actually correct?**
  _`ProductId` has 15 INFERRED edges - model-reasoned connections that need verification._
- **What connects `fs`, `path`, `fs` to the rest of the system?**
  _1170 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `UserId` be split into smaller, more focused modules?**
  _Cohesion score 0.0841813135985199 - nodes in this community are weakly interconnected._
- **Should `org.springframework.stereotype.Component` be split into smaller, more focused modules?**
  _Cohesion score 0.055178652193577565 - nodes in this community are weakly interconnected._