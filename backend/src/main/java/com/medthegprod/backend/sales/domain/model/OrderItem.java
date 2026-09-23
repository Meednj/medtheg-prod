package com.medthegprod.backend.sales.domain.model;

import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.ProductId;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem {

    private final OrderItemId id;
    private final ProductId productId;
    private final String productTitle;
    private final Money unitPrice;
    private final int quantity;

    public OrderItem(
            OrderItemId id,
            ProductId productId,
            String productTitle,
            Money unitPrice,
            int quantity) {
        this.id = Objects.requireNonNull(id, "Order item ID cannot be null");
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");

        if (productTitle == null || productTitle.isBlank()) {
            throw new IllegalArgumentException("Product title cannot be blank");
        }

        this.productTitle = productTitle.trim();
        this.unitPrice = Objects.requireNonNull(unitPrice, "Unit price cannot be null");

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        this.quantity = quantity;
    }

    public OrderItemId getId() {
        return id;
    }

    public ProductId getProductId() {
        return productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money subtotal() {
        return new Money(
                unitPrice.amount().multiply(BigDecimal.valueOf(quantity)),
                unitPrice.currency());
    }
}