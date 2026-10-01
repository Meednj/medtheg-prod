# Graph Report - medtheg-prod  (2026-10-01)

## Corpus Check
- Corpus is ~30,310 words - fits in a single context window. You may not need a graph.

## Summary
- 1395 nodes · 4028 edges · 76 communities (44 shown, 32 thin omitted)
- Extraction: 88% EXTRACTED · 12% INFERRED · 0% AMBIGUOUS · INFERRED: 467 edges (avg confidence: 0.8)
- Token cost: 11,760 input · 10,740 output

## Community Hubs (Navigation)
- Web Controllers
- Catalog Services
- Domain Events
- Payment Persistence
- Exception Handling
- Shared Domain Types
- Architecture Documentation
- Order Persistence
- Product Commands
- Catalog Integration Tests
- Identity Integration Tests
- Product Queries
- Order Queries
- Catalog Web API
- Order Entities
- Observability Tests
- Digital Assets
- Authentication API
- Identity Domain
- Order Tests
- Entitlement Domain
- Library Services
- Catalog Entities
- Stripe Webhooks
- TypeScript Configuration
- Asset Entities
- Payment Services
- Asset Services
- Object Storage
- Order Payments
- React Packages
- Node TypeScript Config
- Product Persistence
- Library Responses
- User Domain
- JWT Security
- Product Lifecycle
- Product Entity
- User Entity
- User Persistence
- Spring Security
- Registration Services
- JWT Token Generation
- Order Persistence Adapter
- Order Creation Tests
- Authentication Services
- Infrastructure Configuration
- Product Listing
- Frontend Dependencies
- Entitlement Persistence
- Entitlement Entity
- Business Metrics
- Frontend Assets
- Product Specifications
- Stripe Payments
- Frontend Runtime
- Product Status Entity
- Frontend Linting
- Spring Boot App
- Frontend Build Scripts
- Database Architecture
- Frontend Architecture
- Vite Configuration
- TypeScript Project References
- Prometheus Monitoring
- Library Download API
- Frontend Entry Point
- Favicon Artwork
- Vite Logo
- Graphify Instructions
- Social Icon Sprite
- Hero Brand Artwork
- React Logo
- Docker Services
- Miscellaneous

## God Nodes (most connected - your core abstractions)
1. `ProductId` - 85 edges
2. `Product` - 82 edges
3. `ProductRepository` - 51 edges
4. `Order` - 51 edges
5. `UserId` - 48 edges
6. `User` - 42 edges
7. `ProductControllerIntegrationTest` - 42 edges
8. `Entitlement` - 40 edges
9. `OrderId` - 40 edges
10. `ProductType` - 37 edges

## Surprising Connections (you probably didn't know these)
- `Med The G Prod` --references--> `Backend architecture`  [EXTRACTED]
  README.md → docs/architecture/backend.md
- `Backend Spring configuration` --conceptually_related_to--> `PostgreSQL catalog persistence`  [INFERRED]
  backend/src/main/resources/application.yml → docs/architecture/catalog.md
- `Backend Spring configuration` --conceptually_related_to--> `Stripe signed webhook`  [INFERRED]
  backend/src/main/resources/application.yml → docs/architecture/sales.md
- `Med The G Prod` --references--> `Modular monolith decision`  [EXTRACTED]
  README.md → docs/adr/001-modular-monolith.md
- `Backend Spring configuration` --references--> `MinIO signed download URL`  [EXTRACTED]
  backend/src/main/resources/application.yml → docs/architecture/library.md

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Backend modular business modules** — docs_architecture_backend_identity_module, docs_architecture_backend_catalog_module, docs_architecture_backend_sales_module, docs_architecture_backend_entitlement_module, docs_architecture_backend_library_module [EXTRACTED 1.00]
- **Purchase to ownership flow** — docs_architecture_sales_stripe_webhook, docs_architecture_sales_orderpaidevent, docs_architecture_overview_entitlement_ownership [EXTRACTED 1.00]
- **Local observability stack** — docs_architecture_deployment_prometheus, docs_architecture_deployment_grafana, infrastructure_monitoring_grafana_provisioning_datasources_prometheus_prometheus_datasource [EXTRACTED 1.00]
- **medtheg backend Prometheus scrape configuration** — infrastructure_monitoring_prometheus_prometheus_medtheg_backend, infrastructure_monitoring_prometheus_prometheus_actuator_prometheus_endpoint, infrastructure_monitoring_prometheus_prometheus_docker_backend_target [EXTRACTED 1.00]

## Communities (76 total, 32 thin omitted)

### Community 0 - "Web Controllers"
Cohesion: 0.05
Nodes (22): UploadProductAssetUseCase, AssetUploadResponse, ProductAssetController, ListCustomerEntitlementsUseCase, EntitlementController, EntitlementResponse, GetAssetDownloadUrlUseCase, GetCustomerLibraryUseCase (+14 more)

### Community 1 - "Catalog Services"
Cohesion: 0.09
Nodes (3): PublishProductServiceTest, ProductTest, ProductPersistenceAdapterIntegrationTest

### Community 2 - "Domain Events"
Cohesion: 0.05
Nodes (17): ProductPublishedEvent, BusinessMetrics, GrantEntitlementUseCase, OrderPaidEventListener, SpringEventPublisher, MicrometerBusinessMetrics, MicrometerSalesMetrics, OrderPaidMetricsListener (+9 more)

### Community 3 - "Payment Persistence"
Cohesion: 0.07
Nodes (10): Payment, PaymentStatus, COMPLETED, CREATED, FAILED, PaymentRepository, PaymentEntity, PaymentMapper (+2 more)

### Community 4 - "Exception Handling"
Cohesion: 0.10
Nodes (9): ProductNotFoundException, InvalidCredentialsException, UserAlreadyExistsException, UserSuspendedException, ApiError, GlobalRestExceptionHandler, LibraryAccessDeniedException, OrderAccessDeniedException (+1 more)

### Community 5 - "Shared Domain Types"
Cohesion: 0.16
Nodes (7): Money, ProductId, OrderItemId, AddProductAssetServiceTest, RemoveProductAssetServiceTest, UpdateProductServiceTest, OrderPaidEventListenerTest

### Community 6 - "Architecture Documentation"
Cohesion: 0.05
Nodes (40): Backend Spring configuration, Root local dependency orchestration, Backend architecture, Catalog module, Entitlement module, Identity module, Library module, OrderPaidEvent (+32 more)

### Community 7 - "Order Persistence"
Cohesion: 0.11
Nodes (6): ProductAssetResponse, OrderStatus, CANCELLED, PAID, PENDING, REFUNDED

### Community 8 - "Product Commands"
Cohesion: 0.09
Nodes (16): UpdateProductService, CreateProductUseCase, UpdateProductUseCase, ProductCategory, BEATS, BUNDLES, COURSES, KITS (+8 more)

### Community 11 - "Product Queries"
Cohesion: 0.13
Nodes (12): EventPublisher, AddProductAssetService, CreateProductService, GetProductAssetService, GetProductService, ListProductAssetsService, PublishProductService, RemoveProductAssetService (+4 more)

### Community 12 - "Order Queries"
Cohesion: 0.14
Nodes (9): UserId, CreateOrderService, GetOrderService, ListCustomerOrdersService, GetOrderUseCase, ListCustomerOrdersUseCase, OrderId, OrderPage (+1 more)

### Community 13 - "Catalog Web API"
Cohesion: 0.13
Nodes (7): AddProductAssetUseCase, PublishProductUseCase, ProductController, DigitalAssetResponse, ProductPageResponse, ProductResponse, ProductWebMapper

### Community 14 - "Order Entities"
Cohesion: 0.11
Nodes (3): OrderEntity, OrderItemEntity, OrderMapper

### Community 15 - "Observability Tests"
Cohesion: 0.12
Nodes (4): BackendApplicationTests, MetricsIntegrationTest, ObservabilityIntegrationTest, OrderControllerIntegrationTest

### Community 16 - "Digital Assets"
Cohesion: 0.15
Nodes (13): ProductAsset, AssetId, DigitalAsset, DigitalAssetType, AUDIO_MP3, AUDIO_PREVIEW, AUDIO_WAV, MIDI (+5 more)

### Community 17 - "Authentication API"
Cohesion: 0.11
Nodes (7): AuthenticateUserUseCase, AuthenticationResult, RegisterUserUseCase, AuthController, LoginRequest, RegisterRequest, UserWebMapper

### Community 18 - "Identity Domain"
Cohesion: 0.14
Nodes (8): Role, ADMIN, CUSTOMER, UserStatus, ACTIVE, SUSPENDED, AuthResponse, UserResponse

### Community 19 - "Order Tests"
Cohesion: 0.19
Nodes (4): OrderItem, OrderItemResponse, OrderTest, OrderPersistenceAdapterIntegrationTest

### Community 21 - "Library Services"
Cohesion: 0.15
Nodes (5): ListProductAssetsUseCase, GrantEntitlementService, ListCustomerEntitlementsService, EntitlementRepository, GetCustomerLibraryService

### Community 22 - "Catalog Entities"
Cohesion: 0.15
Nodes (12): ProductCategoryEntity, BEATS, BUNDLES, COURSES, KITS, ProductTypeEntity, BEAT, BUNDLE (+4 more)

### Community 23 - "Stripe Webhooks"
Cohesion: 0.13
Nodes (4): EventPublisher, MarkOrderAsPaidService, MarkOrderAsPaidUseCase, StripeWebhookController

### Community 24 - "TypeScript Configuration"
Cohesion: 0.10
Nodes (19): compilerOptions, allowArbitraryExtensions, allowImportingTsExtensions, erasableSyntaxOnly, jsx, lib, module, moduleDetection (+11 more)

### Community 25 - "Asset Entities"
Cohesion: 0.12
Nodes (10): DigitalAssetEntity, DigitalAssetTypeEntity, AUDIO_MP3, AUDIO_PREVIEW, AUDIO_WAV, MIDI, OTHER, PDF (+2 more)

### Community 26 - "Payment Services"
Cohesion: 0.29
Nodes (4): PaymentGateway, PaymentSession, CreatePaymentService, CreatePaymentServiceTest

### Community 27 - "Asset Services"
Cohesion: 0.18
Nodes (4): UploadProductAssetService, GetProductAssetUseCase, AssetStorage, GetAssetDownloadUrlService

### Community 28 - "Object Storage"
Cohesion: 0.17
Nodes (3): MinioAssetStorageAdapter, MinioProperties, StripeProperties

### Community 30 - "React Packages"
Cohesion: 0.12
Nodes (16): name, private, type, version, @babel/core, babel-plugin-react-compiler, oxlint, react-router-dom (+8 more)

### Community 31 - "Node TypeScript Config"
Cohesion: 0.12
Nodes (16): compilerOptions, allowImportingTsExtensions, erasableSyntaxOnly, lib, module, moduleDetection, noEmit, noFallthroughCasesInSwitch (+8 more)

### Community 33 - "Library Responses"
Cohesion: 0.17
Nodes (7): EntitlementStatus, ACTIVE, REVOKED, LibraryAsset, LibraryItem, LibraryAssetResponse, LibraryItemResponse

### Community 36 - "Product Lifecycle"
Cohesion: 0.17
Nodes (5): Product, ProductStatus, ARCHIVED, DRAFT, PUBLISHED

### Community 41 - "Registration Services"
Cohesion: 0.31
Nodes (4): PasswordHasher, RegisterUserService, UserRepository, RegisterUserServiceTest

### Community 45 - "Authentication Services"
Cohesion: 0.21
Nodes (3): TokenGenerator, AuthenticateUserService, AuthTestHelper

### Community 46 - "Infrastructure Configuration"
Cohesion: 0.24
Nodes (3): OpenApiConfig, MinioConfiguration, StripeConfiguration

### Community 47 - "Product Listing"
Cohesion: 0.30
Nodes (4): ProductSearchQuery, ListProductsService, ListProductsUseCase, ProductPage

### Community 48 - "Frontend Dependencies"
Cohesion: 0.17
Nodes (12): devDependencies, @babel/core, babel-plugin-react-compiler, oxlint, @rolldown/plugin-babel, @types/babel__core, @types/node, @types/react (+4 more)

### Community 49 - "Entitlement Persistence"
Cohesion: 0.33
Nodes (3): EntitlementPersistenceAdapter, EntitlementMapper, EntitlementJpaRepository

### Community 53 - "Frontend Assets"
Cohesion: 0.28
Nodes (3): App(), react, react-dom

### Community 56 - "Frontend Runtime"
Cohesion: 0.29
Nodes (7): dependencies, react, react-dom, react-router-dom, tailwindcss, @tailwindcss/vite, @tanstack/react-query

### Community 57 - "Product Status Entity"
Cohesion: 0.33
Nodes (4): ProductStatusEntity, ARCHIVED, DRAFT, PUBLISHED

### Community 58 - "Frontend Linting"
Cohesion: 0.33
Nodes (5): plugins, rules, react/only-export-components, react/rules-of-hooks, $schema

### Community 60 - "Frontend Build Scripts"
Cohesion: 0.40
Nodes (5): scripts, build, dev, lint, preview

### Community 62 - "Frontend Architecture"
Cohesion: 0.50
Nodes (4): Frontend architecture, React Query / API client, React UI, Spring Boot REST API

### Community 63 - "Vite Configuration"
Cohesion: 0.50
Nodes (3): @rolldown/plugin-babel, vite, @vitejs/plugin-react

### Community 65 - "Prometheus Monitoring"
Cohesion: 0.67
Nodes (3): Spring Actuator Prometheus endpoint, Docker host backend target on port 8080, medtheg-backend scrape job

## Knowledge Gaps
- **160 isolated node(s):** `com.backend:backend`, `AUDIO_PREVIEW`, `AUDIO_MP3`, `AUDIO_WAV`, `STEMS` (+155 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 328 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **32 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ProductId` connect `Shared Domain Types` to `Catalog Services`, `Domain Events`, `Exception Handling`, `Order Persistence`, `Product Commands`, `Product Queries`, `Catalog Web API`, `Order Entities`, `Digital Assets`, `Order Tests`, `Entitlement Domain`, `Library Services`, `Catalog Entities`, `Asset Services`, `Product Persistence`, `Product Lifecycle`, `Product Entity`, `Order Creation Tests`, `Product Listing`, `Entitlement Entity`?**
  _High betweenness centrality (0.114) - this node is a cross-community bridge._
- **Why does `Product` connect `Product Lifecycle` to `Product Persistence`, `Catalog Services`, `Library Responses`, `Shared Domain Types`, `Product Entity`, `Product Commands`, `Product Queries`, `Order Creation Tests`, `Catalog Web API`, `Product Listing`, `Digital Assets`, `Catalog Entities`?**
  _High betweenness centrality (0.048) - this node is a cross-community bridge._
- **Why does `UserId` connect `Order Queries` to `Web Controllers`, `User Domain`, `Shared Domain Types`, `User Entity`, `User Persistence`, `Order Persistence`, `Registration Services`, `Identity Integration Tests`, `Order Persistence Adapter`, `Order Creation Tests`, `Order Entities`, `Observability Tests`, `Identity Domain`, `Payment Services`, `Order Payments`?**
  _High betweenness centrality (0.044) - this node is a cross-community bridge._
- **Are the 12 inferred relationships involving `ProductId` (e.g. with `.execute()` and `.toDomain()`) actually correct?**
  _`ProductId` has 12 INFERRED edges - model-reasoned connections that need verification._
- **What connects `com.backend:backend`, `AUDIO_PREVIEW`, `AUDIO_MP3` to the rest of the system?**
  _160 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Web Controllers` be split into smaller, more focused modules?**
  _Cohesion score 0.05220883534136546 - nodes in this community are weakly interconnected._
- **Should `Catalog Services` be split into smaller, more focused modules?**
  _Cohesion score 0.08713850837138508 - nodes in this community are weakly interconnected._