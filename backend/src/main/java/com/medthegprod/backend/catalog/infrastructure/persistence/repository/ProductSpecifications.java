package com.medthegprod.backend.catalog.infrastructure.persistence.repository;

import com.medthegprod.backend.catalog.infrastructure.persistence.entity.ProductEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.enums.ProductCategoryEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.enums.ProductTypeEntity;

import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecifications {

    private ProductSpecifications() {
    }

    public static Specification<ProductEntity> hasType(ProductTypeEntity type) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<ProductEntity> hasCategory(
            ProductCategoryEntity category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isMember(category, root.get("categories"));
    }

    public static Specification<ProductEntity> containsText(String search) {
        return (root, query, criteriaBuilder) -> {
            String pattern = "%" + search.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("title")),
                            pattern),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("description")),
                            pattern));
        };
    }
}