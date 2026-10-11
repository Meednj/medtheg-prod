package com.medthegprod.backend.catalog.application.usecase;

import com.medthegprod.backend.catalog.application.query.AdminProductSearchQuery;
import com.medthegprod.backend.catalog.domain.repository.ProductPage;

public interface ListAdminProductsUseCase {
    ProductPage execute(AdminProductSearchQuery query);
}
