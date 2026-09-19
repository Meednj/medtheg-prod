package com.medthegprod.backend.catalog.application.usecase;

import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.model.ProductType;

import java.math.BigDecimal;
import java.util.Set;

public interface UpdateProductUseCase {

    Product execute(
            ProductId productId,
            String title,
            String description,
            ProductType type,
            BigDecimal price,
            Set<ProductCategory> categories);
}