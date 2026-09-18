package com.medthegprod.backend.catalog.domain.repository;

import java.util.Optional;

import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;

public interface ProductRepository {
    
    Product save(Product product);

    Optional<Product> findById(ProductId productId);
}
