package com.medthegprod.backend.catalog.infrastructure.web.controller;

import com.medthegprod.backend.catalog.application.usecase.CreateProductUseCase;
import com.medthegprod.backend.catalog.application.usecase.GetProductUseCase;
import com.medthegprod.backend.catalog.application.usecase.ListProductsUseCase;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductPage;
import com.medthegprod.backend.catalog.infrastructure.web.dto.CreateProductRequest;
import com.medthegprod.backend.catalog.infrastructure.web.dto.ProductPageResponse;
import com.medthegprod.backend.catalog.infrastructure.web.dto.ProductResponse;
import com.medthegprod.backend.catalog.infrastructure.web.mapper.ProductWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import com.medthegprod.backend.catalog.application.query.ProductSearchQuery;
import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductType;

@RestController
@RequestMapping("/api/products")
public class ProductController {

        private final CreateProductUseCase createProductUseCase;
        private final GetProductUseCase getProductUseCase;
        private final ListProductsUseCase listProductsUseCase;

        public ProductController(
                        CreateProductUseCase createProductUseCase,
                        GetProductUseCase getProductUseCase,
                        ListProductsUseCase listProductsUseCase) {
                this.createProductUseCase = createProductUseCase;
                this.getProductUseCase = getProductUseCase;
                this.listProductsUseCase = listProductsUseCase;
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
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
        public ProductResponse getProduct(
                        @PathVariable UUID id) {
                Product product = getProductUseCase.execute(
                                new ProductId(id));

                return ProductWebMapper.toResponse(product);
        }

        @GetMapping
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
}