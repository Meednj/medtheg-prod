package com.medthegprod.backend.catalog.infrastructure.web.controller;

import com.medthegprod.backend.catalog.application.usecase.AddProductAssetUseCase;
import com.medthegprod.backend.catalog.application.usecase.CreateProductUseCase;
import com.medthegprod.backend.catalog.application.usecase.GetProductUseCase;
import com.medthegprod.backend.catalog.application.usecase.ListProductsUseCase;
import com.medthegprod.backend.catalog.application.usecase.PublishProductUseCase;
import com.medthegprod.backend.catalog.application.usecase.RemoveProductAssetUseCase;
import com.medthegprod.backend.catalog.application.usecase.UpdateProductUseCase;
import com.medthegprod.backend.catalog.domain.model.AssetId;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductPage;
import com.medthegprod.backend.catalog.infrastructure.web.dto.AddProductAssetRequest;
import com.medthegprod.backend.catalog.infrastructure.web.dto.CreateProductRequest;
import com.medthegprod.backend.catalog.infrastructure.web.dto.ProductPageResponse;
import com.medthegprod.backend.catalog.infrastructure.web.dto.ProductResponse;
import com.medthegprod.backend.catalog.infrastructure.web.dto.UpdateProductRequest;
import com.medthegprod.backend.catalog.infrastructure.web.mapper.ProductWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import com.medthegprod.backend.catalog.application.query.ProductSearchQuery;
import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Catalog", description = "Public product browsing and administrator product management")
public class ProductController {

        private final CreateProductUseCase createProductUseCase;
        private final GetProductUseCase getProductUseCase;
        private final ListProductsUseCase listProductsUseCase;
        private final UpdateProductUseCase updateProductUseCase;
        private final PublishProductUseCase publishProductUseCase;
        private final AddProductAssetUseCase addProductAssetUseCase;
        private final RemoveProductAssetUseCase removeProductAssetUseCase;

        public ProductController(
                        CreateProductUseCase createProductUseCase,
                        GetProductUseCase getProductUseCase,
                        ListProductsUseCase listProductsUseCase,
                        UpdateProductUseCase updateProductUseCase,
                        PublishProductUseCase publishProductUseCase,
                        AddProductAssetUseCase addProductAssetUseCase,
                        RemoveProductAssetUseCase removeProductAssetUseCase) {
                this.createProductUseCase = createProductUseCase;
                this.getProductUseCase = getProductUseCase;
                this.listProductsUseCase = listProductsUseCase;
                this.updateProductUseCase = updateProductUseCase;
                this.publishProductUseCase = publishProductUseCase;
                this.addProductAssetUseCase = addProductAssetUseCase;
                this.removeProductAssetUseCase = removeProductAssetUseCase;
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        @Operation(summary = "Create a product", description = "Creates a draft product. Requires the ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
        public ProductResponse createProduct(
                        @Valid @RequestBody CreateProductRequest request) {
                Product product = createProductUseCase.execute(
                                request.title(),
                                request.description(),
                                request.type(),
                                request.price(),
                                request.categories());

                return ProductWebMapper.toResponse(product);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get a product", description = "Returns a published or draft product by ID. This endpoint is public.")
        public ProductResponse getProduct(
                        @PathVariable UUID id) {
                Product product = getProductUseCase.execute(
                                new ProductId(id));

                return ProductWebMapper.toResponse(product);
        }

        @GetMapping
        @Operation(summary = "List products", description = "Returns public products with pagination and optional filtering. This endpoint is public.")
        public ProductPageResponse getProducts(

                        @RequestParam(defaultValue = "0") int page,

                        @RequestParam(defaultValue = "12") int size,

                        @RequestParam(required = false) ProductType type,

                        @RequestParam(required = false) ProductCategory category,

                        @RequestParam(required = false) String search,

                        @RequestParam(defaultValue = "createdAt") String sortBy,

                        @RequestParam(defaultValue = "desc") String direction) {

                ProductPage result = listProductsUseCase.execute(
                                new ProductSearchQuery(
                                                page,
                                                size,
                                                type,
                                                category,
                                                search,
                                                sortBy,
                                                direction));

                return new ProductPageResponse(
                                result.content()
                                                .stream()
                                                .map(ProductWebMapper::toResponse)
                                                .toList(),
                                result.page(),
                                result.size(),
                                result.totalElements(),
                                result.totalPages());
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update a product", description = "Updates a product. Requires the ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
        public ProductResponse updateProduct(
                        @PathVariable UUID id,
                        @Valid @RequestBody UpdateProductRequest request) {
                Product product = updateProductUseCase.execute(
                                new ProductId(id),
                                request.title(),
                                request.description(),
                                request.type(),
                                request.price(),
                                request.categories());

                return ProductWebMapper.toResponse(product);
        }

        @PostMapping("/{id}/publish")
        @Operation(summary = "Publish a product", description = "Publishes a product. Requires the ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
        public ProductResponse publishProduct(
                        @PathVariable UUID id) {
                Product product = publishProductUseCase.execute(
                                new ProductId(id));

                return ProductWebMapper.toResponse(product);
        }

        @PostMapping("/{productId}/assets")
        @Operation(summary = "Add a product asset", description = "Adds an asset reference to a product. Requires the ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
        public ProductResponse addAsset(
                        @PathVariable UUID productId,
                        @Valid @RequestBody AddProductAssetRequest request) {
                Product product = addProductAssetUseCase.execute(
                                new ProductId(productId),
                                request.type(),
                                request.storageKey());

                return ProductWebMapper.toResponse(product);
        }

        @DeleteMapping("/{productId}/assets/{assetId}")
        @Operation(summary = "Remove a product asset", description = "Removes an asset from a product. Requires the ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
        public ProductResponse removeAsset(
                        @PathVariable UUID productId,
                        @PathVariable UUID assetId) {
                Product product = removeProductAssetUseCase.execute(
                                new ProductId(productId),
                                new AssetId(assetId));

                return ProductWebMapper.toResponse(product);
        }
}