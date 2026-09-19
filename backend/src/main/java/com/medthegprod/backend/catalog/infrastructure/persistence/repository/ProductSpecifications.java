package com.medthegprod.backend.catalog.infrastructure.persistence.repository;

import com.medthegprod.backend.catalog.infrastructure.persistence.entity.ProductCategoryEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.ProductEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.ProductTypeEntity;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecifications {

    private ProductSpecifications() {
    }

    public static Specification<ProductEntity> hasType(
            ProductTypeEntity type) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<ProductEntity> hasCategory(
            ProductCategoryEntity category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isMember(
                category,
                root.get("categories"));
    }
}