package com.medthegprod.backend.catalog.infrastructure.persistence;

import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.ProductEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.mapper.ProductMapper;
import com.medthegprod.backend.catalog.infrastructure.persistence.repository.ProductJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductPersistenceAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    public ProductPersistenceAdapter(
            ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Product save(Product product) {

        ProductEntity entity = ProductMapper.toEntity(product);

        ProductEntity savedEntity = productJpaRepository.save(entity);

        return ProductMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(ProductId productId) {

        return productJpaRepository
                .findById(productId.value())
                .map(ProductMapper::toDomain);
    }
}