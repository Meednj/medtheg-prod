package com.medthegprod.backend.sales.infrastructure.persistence;

import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderItem;
import com.medthegprod.backend.sales.domain.model.OrderItemId;
import com.medthegprod.backend.sales.domain.model.OrderStatus;
import com.medthegprod.backend.sales.domain.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderPersistenceAdapterIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldSaveAndRetrieveOrder() {

        UserId customerId = UserId.generate();

        Order order = new Order(
                com.medthegprod.backend.sales.domain.model.OrderId.generate(),
                customerId,
                java.time.OffsetDateTime.now());

        OrderItem item = new OrderItem(
                OrderItemId.generate(),
                ProductId.generate(),
                "Dark Beat",
                Money.eur(new BigDecimal("15.00")),
                1);

        order.addItem(item);

        Order saved = orderRepository.save(order);

        assertNotNull(saved);
        assertEquals(order.getId(), saved.getId());
        assertEquals(customerId, saved.getCustomerId());
        assertEquals(OrderStatus.PENDING, saved.getStatus());
        assertEquals(1, saved.getItems().size());

        Optional<Order> retrieved = orderRepository.findById(order.getId());

        assertTrue(retrieved.isPresent());

        Order loaded = retrieved.get();

        assertEquals(order.getId(), loaded.getId());
        assertEquals(customerId, loaded.getCustomerId());
        assertEquals(OrderStatus.PENDING, loaded.getStatus());

        assertEquals(1, loaded.getItems().size());

        OrderItem loadedItem = loaded.getItems().get(0);

        assertEquals(
                item.getProductId(),
                loadedItem.getProductId());

        assertEquals(
                item.getProductTitle(),
                loadedItem.getProductTitle());

        assertEquals(
                item.getUnitPrice().amount(),
                loadedItem.getUnitPrice().amount());

        assertEquals(
                item.getUnitPrice().currency(),
                loadedItem.getUnitPrice().currency());

        assertEquals(
                item.getQuantity(),
                loadedItem.getQuantity());
    }
}