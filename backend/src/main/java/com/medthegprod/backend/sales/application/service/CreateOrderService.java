package com.medthegprod.backend.sales.application.service;

import com.medthegprod.backend.catalog.application.usecase.GetProductUseCase;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.model.ProductStatus;
import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.application.usecase.CreateOrderUseCase;
import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderId;
import com.medthegprod.backend.sales.domain.model.OrderItem;
import com.medthegprod.backend.sales.domain.model.OrderItemId;
import com.medthegprod.backend.sales.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final GetProductUseCase getProductUseCase;

    public CreateOrderService(
            OrderRepository orderRepository,
            GetProductUseCase getProductUseCase) {
        this.orderRepository = orderRepository;
        this.getProductUseCase = getProductUseCase;
    }

    @Override
    @Transactional
    public Order execute(
            UserId customerId,
            List<Item> items) {
        Objects.requireNonNull(
                customerId,
                "Customer ID cannot be null");

        Objects.requireNonNull(
                items,
                "Order items cannot be null");

        if (items.isEmpty()) {
            throw new IllegalArgumentException(
                    "An order must contain at least one item");
        }

        Order order = new Order(
                OrderId.generate(),
                customerId,
                OffsetDateTime.now());

        for (Item item : items) {

            Product product = getProductUseCase.execute(
                    new ProductId(item.productId()));

            if (product.getStatus() != ProductStatus.PUBLISHED) {
                throw new IllegalStateException(
                        "Product is not available for purchase");
            }

            OrderItem orderItem = new OrderItem(
                    OrderItemId.generate(),
                    product.getId(),
                    product.getTitle(),
                    product.getPrice(),
                    item.quantity());

            order.addItem(orderItem);
        }

        return orderRepository.save(order);
    }
}