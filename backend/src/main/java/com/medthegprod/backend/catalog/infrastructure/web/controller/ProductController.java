package com.medthegprod.backend.catalog.infrastructure.web.controller;

import com.medthegprod.backend.catalog.application.usecase.CreateProductUseCase;
import com.medthegprod.backend.catalog.application.usecase.GetProductUseCase;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.infrastructure.web.dto.CreateProductRequest;
import com.medthegprod.backend.catalog.infrastructure.web.dto.ProductResponse;
import com.medthegprod.backend.catalog.infrastructure.web.mapper.ProductWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            GetProductUseCase getProductUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.getProductUseCase = getProductUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        Product product = createProductUseCase.execute(
                request.title(),
                request.description(),
                request.type(),
                request.price());

        return ProductWebMapper.toResponse(product);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(
            @PathVariable UUID id) {
        Product product = getProductUseCase.execute(
                new ProductId(id));

        return ProductWebMapper.toResponse(product);
    }
}