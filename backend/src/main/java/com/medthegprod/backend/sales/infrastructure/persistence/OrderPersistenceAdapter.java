package com.medthegprod.backend.sales.infrastructure.persistence;

import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderId;
import com.medthegprod.backend.sales.domain.model.OrderPage;
import com.medthegprod.backend.sales.domain.repository.OrderRepository;
import com.medthegprod.backend.sales.infrastructure.persistence.entity.OrderEntity;
import com.medthegprod.backend.sales.infrastructure.persistence.mapper.OrderMapper;
import com.medthegprod.backend.sales.infrastructure.persistence.repository.OrderJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class OrderPersistenceAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    public OrderPersistenceAdapter(
            OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    @Transactional
    public Order save(Order order) {

        OrderEntity entity = OrderMapper.toEntity(order);

        OrderEntity savedEntity = orderJpaRepository.save(entity);

        return OrderMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(OrderId orderId) {

        return orderJpaRepository
                .findById(orderId.value())
                .map(OrderMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPage findByCustomerId(
            UserId customerId,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<OrderEntity> result = orderJpaRepository.findByCustomerId(
                customerId.value(),
                pageable);

        return new OrderPage(
                result.getContent()
                        .stream()
                        .map(OrderMapper::toDomain)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }
}