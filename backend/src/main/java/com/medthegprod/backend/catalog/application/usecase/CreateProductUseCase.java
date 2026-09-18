package com.medthegprod.backend.catalog.application.usecase;

import java.math.BigDecimal;

import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductType;

public interface CreateProductUseCase {
    
    Product execute(String title, String description, ProductType type, BigDecimal price);
    
}