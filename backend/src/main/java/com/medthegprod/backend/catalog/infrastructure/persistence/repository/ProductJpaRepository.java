package com.medthegprod.backend.catalog.infrastructure.persistence.repository;

import com.medthegprod.backend.catalog.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;
import java.util.UUID;

public interface ProductJpaRepository
                extends JpaRepository<ProductEntity, UUID>,
                JpaSpecificationExecutor<ProductEntity> {

    @Query("""
            SELECT DISTINCT p
            FROM ProductEntity p
            LEFT JOIN FETCH p.categories
            WHERE p.id = :id
            """)
    Optional<ProductEntity> findByIdWithCategories(
            @Param("id") UUID id
    );
    
}
