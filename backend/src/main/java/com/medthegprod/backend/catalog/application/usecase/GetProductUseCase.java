package com.medthegprod.backend.catalog.application.usecase;

import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;

public interface GetProductUseCase {

    Product execute(ProductId productId);
}