package com.medthegprod.backend.catalog.domain.model;

import java.util.Objects;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Product {
    private final ProductId id;
    private String title;
    private String description;
    private final ProductType type;
    private Money price;
    private ProductStatus status;
    private final List<DigitalAsset> assets = new ArrayList<>();
    private final Set<ProductCategory> categories = new HashSet<>();
    
    public Product(
        ProductId id,
        String title,
        String description,
        ProductType type,
        Money price
    ) {
        this.id = Objects.requireNonNull(id, "Product ID cannot be null");
        this.title = validateTitle(title);
        this.description = description;
        this.type = Objects.requireNonNull(type, "Product type cannot be null");
        this.price = Objects.requireNonNull(price, "Price cannot be null");
        this.status = ProductStatus.DRAFT;
    }

    public void publish() {
        if (status != ProductStatus.DRAFT) {
            throw new IllegalStateException(
                    "Only draft products can be published");
        }

        status = ProductStatus.PUBLISHED;
    }

    public void archive() {
        if (status != ProductStatus.PUBLISHED) {
            throw new IllegalStateException(
                    "Only published products can be archived");
        }

        status = ProductStatus.ARCHIVED;
    }

    public void changePrice(Money newPrice) {
        this.price = Objects.requireNonNull(
                newPrice,
                "Price cannot be null");
    }

    public void updateTitle(String newTitle) {
        this.title = validateTitle(newTitle);
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    private String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Product title cannot be blank");
        }

        return title.trim();
    }

    public void addAsset(DigitalAsset asset) {
        Objects.requireNonNull(asset, "Asset cannot be null");

        boolean alreadyExists = assets.stream()
                .anyMatch(existing -> existing.getId().equals(asset.getId()));

        if (alreadyExists) {
            throw new IllegalArgumentException(
                    "Asset already belongs to this product");
        }

        assets.add(asset);
    }

    public void removeAsset(AssetId assetId) {
        boolean removed = assets.removeIf(
                asset -> asset.getId().equals(assetId));

        if (!removed) {
            throw new IllegalArgumentException(
                    "Asset does not belong to this product");
        }
    }

    public void addCategory(ProductCategory category) {
        Objects.requireNonNull(category, "Category cannot be null");
        categories.add(category);
    }

    public void removeCategory(ProductCategory category) {
        categories.remove(category);
    }

    public static Product reconstitute(
            ProductId id,
            String title,
            String description,
            ProductType type,
            Money price,
            ProductStatus status) {
        Product product = new Product(
                id,
                title,
                description,
                type,
                price);

        product.status = Objects.requireNonNull(
                status,
                "Product status cannot be null");

        return product;
    }

    public Set<ProductCategory> getCategories() {
        return Collections.unmodifiableSet(categories);
    }

    public ProductId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ProductType getType() {
        return type;
    }

    public Money getPrice() {
        return price;
    }

    public ProductStatus getStatus() {
        return status;
    }
    
    public List<DigitalAsset> getAssets() {
        return Collections.unmodifiableList(assets);
    }
}
