package com.medthegprod.backend.sales.infrastructure.persistence.mapper;

import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderId;
import com.medthegprod.backend.sales.domain.model.OrderItem;
import com.medthegprod.backend.sales.domain.model.OrderItemId;
import com.medthegprod.backend.sales.infrastructure.persistence.entity.OrderEntity;
import com.medthegprod.backend.sales.infrastructure.persistence.entity.OrderItemEntity;

import java.util.Currency;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderEntity toEntity(Order order) {

        OrderEntity entity = new OrderEntity(
                order.getId().value(),
                order.getCustomerId().value(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getPaidAt());

        order.getItems().forEach(item -> {

            OrderItemEntity itemEntity = new OrderItemEntity(
                    item.getId().value(),
                    entity,
                    item.getProductId().value(),
                    item.getProductTitle(),
                    item.getUnitPrice().amount(),
                    item.getUnitPrice().currency().getCurrencyCode(),
                    item.getQuantity());

            entity.getItems().add(itemEntity);
        });

        return entity;
    }

    public static Order toDomain(OrderEntity entity) {

        Order order = new Order(
                new OrderId(entity.getId()),
                new UserId(entity.getCustomerId()),
                entity.getCreatedAt());

        entity.getItems().forEach(item -> {

            OrderItem itemDomain = new OrderItem(
                    new OrderItemId(item.getId()),
                    new ProductId(item.getProductId()),
                    item.getProductTitle(),
                    new Money(
                            item.getUnitPrice(),
                            Currency.getInstance(item.getCurrency())),
                    item.getQuantity());

            order.addItem(itemDomain);
        });

        restoreStatus(order, entity);

        return order;
    }

    private static void restoreStatus(
            Order order,
            OrderEntity entity) {
        switch (entity.getStatus()) {

            case PENDING -> {
                // New Order already starts as PENDING.
            }

            case PAID -> order.markAsPaid(entity.getPaidAt());

            case CANCELLED -> order.cancel();

            case REFUNDED -> {
                order.markAsPaid(entity.getPaidAt());
                order.refund();
            }
        }
    }
}