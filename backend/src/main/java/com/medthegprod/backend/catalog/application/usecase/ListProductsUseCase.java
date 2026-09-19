package com.medthegprod.backend.catalog.application.usecase;

import com.medthegprod.backend.catalog.application.query.ProductSearchQuery;
import com.medthegprod.backend.catalog.domain.repository.ProductPage;

public interface ListProductsUseCase {

    ProductPage execute(ProductSearchQuery query);
}