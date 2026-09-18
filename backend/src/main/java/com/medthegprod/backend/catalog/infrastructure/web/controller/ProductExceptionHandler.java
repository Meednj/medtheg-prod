package com.medthegprod.backend.catalog.infrastructure.web.controller;

import com.medthegprod.backend.catalog.application.service.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleProductNotFound(
            ProductNotFoundException exception) {
        return Map.of(
                "error", "PRODUCT_NOT_FOUND",
                "message", exception.getMessage());
    }
}