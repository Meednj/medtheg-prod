package com.medthegprod.backend.catalog.infrastructure.persistence.repository;

import com.medthegprod.backend.catalog.infrastructure.persistence.entity.ProductEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.enums.ProductStatusEntity;
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
                        @Param("id") UUID id);

        @Query("""
                        SELECT DISTINCT p
                        FROM ProductEntity p
                        LEFT JOIN FETCH p.categories
                        WHERE p.id = :id
                          AND p.status = :status
                        """)
        Optional<ProductEntity> findByIdWithCategoriesAndStatus(
                        @Param("id") UUID id,
                        @Param("status") ProductStatusEntity status);

        Optional<ProductEntity> findByAssets_Id(UUID assetId);

}
