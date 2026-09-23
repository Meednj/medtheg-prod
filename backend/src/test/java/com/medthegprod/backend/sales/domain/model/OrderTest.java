package com.medthegprod.backend.sales.domain.model;

import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.identity.domain.model.UserId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order createOrder() {
        return new Order(
                OrderId.generate(),
                UserId.generate(),
                OffsetDateTime.now());
    }

    private OrderItem createItem(String title, String price, int quantity) {
        return new OrderItem(
                OrderItemId.generate(),
                ProductId.generate(),
                title,
                Money.eur(new BigDecimal(price)),
                quantity);
    }

    @Test
    void newOrderShouldBePending() {
        Order order = createOrder();

        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void shouldAddItemToOrder() {
        Order order = createOrder();

        OrderItem item = createItem("Dark Beat", "15.00", 1);

        order.addItem(item);

        assertEquals(1, order.getItems().size());
        assertEquals(item, order.getItems().get(0));
    }

    @Test
    void shouldCalculateItemSubtotal() {
        OrderItem item = createItem("Dark Beat", "15.00", 3);

        assertEquals(
                new BigDecimal("45.00"),
                item.subtotal().amount());
    }

    @Test
    void shouldCalculateOrderTotal() {
        Order order = createOrder();

        order.addItem(createItem("Beat One", "15.00", 1));
        order.addItem(createItem("Beat Two", "20.00", 2));

        assertEquals(
                new BigDecimal("55.00"),
                order.total().amount());
    }

    @Test
    void shouldNotAllowDuplicateProduct() {
        Order order = createOrder();

        ProductId productId = ProductId.generate();

        order.addItem(new OrderItem(
                OrderItemId.generate(),
                productId,
                "Dark Beat",
                Money.eur(new BigDecimal("15.00")),
                1));

        assertThrows(
                IllegalArgumentException.class,
                () -> order.addItem(new OrderItem(
                        OrderItemId.generate(),
                        productId,
                        "Dark Beat",
                        Money.eur(new BigDecimal("15.00")),
                        1)));
    }
}