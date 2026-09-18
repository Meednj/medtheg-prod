package com.medthegprod.backend.catalog.infrastructure.persistence.repository;

import com.medthegprod.backend.catalog.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

}