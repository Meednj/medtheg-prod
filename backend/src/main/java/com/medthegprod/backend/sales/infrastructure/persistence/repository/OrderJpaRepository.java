package com.medthegprod.backend.sales.infrastructure.persistence.repository;

import com.medthegprod.backend.sales.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderJpaRepository
        extends JpaRepository<OrderEntity, UUID> {

    Page<OrderEntity> findByCustomerId(
            UUID customerId,
            Pageable pageable);
}