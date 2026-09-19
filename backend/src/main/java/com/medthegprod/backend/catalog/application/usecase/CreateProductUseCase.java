package com.medthegprod.backend.catalog.application.usecase;

import java.math.BigDecimal;
import java.util.Set;

import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductType;

public interface CreateProductUseCase {
    
    Product execute(String title, String description, ProductType type, BigDecimal price, Set<ProductCategory> categories);
    
}