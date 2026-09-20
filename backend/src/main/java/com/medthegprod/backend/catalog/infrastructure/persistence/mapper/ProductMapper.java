package com.medthegprod.backend.catalog.infrastructure.persistence.mapper;

import com.medthegprod.backend.catalog.domain.model.AssetId;
import com.medthegprod.backend.catalog.domain.model.DigitalAsset;
import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;
import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.model.ProductStatus;
import com.medthegprod.backend.catalog.domain.model.ProductType;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.DigitalAssetEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.ProductEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.enums.DigitalAssetTypeEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.enums.ProductCategoryEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.enums.ProductStatusEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.enums.ProductTypeEntity;

import java.time.OffsetDateTime;
import java.util.Currency;

public final class ProductMapper {

        private ProductMapper() {
        }

        public static ProductEntity toEntity(Product product) {

                OffsetDateTime now = OffsetDateTime.now();

                ProductEntity entity = new ProductEntity(
                                product.getId().value(),
                                product.getTitle(),
                                product.getDescription(),
                                ProductTypeEntity.valueOf(product.getType().name()),
                                product.getPrice().amount(),
                                product.getPrice().currency().getCurrencyCode(),
                                ProductStatusEntity.valueOf(product.getStatus().name()),
                                now,
                                now);

                product.getCategories().forEach(category -> entity.getCategories().add(
                                ProductCategoryEntity.valueOf(category.name())));

                product.getAssets().forEach(asset -> {

                        DigitalAssetEntity assetEntity = new DigitalAssetEntity(
                                        asset.getId().value(),
                                        entity,
                                        DigitalAssetTypeEntity.valueOf(asset.getType().name()),
                                        asset.getStorageKey());

                        entity.getAssets().add(assetEntity);
                });

                return entity;
        }

        public static Product toDomain(ProductEntity entity) {

                Product product = Product.reconstitute(
                                new ProductId(entity.getId()),
                                entity.getTitle(),
                                entity.getDescription(),
                                ProductType.valueOf(entity.getType().name()),
                                new Money(
                                                entity.getPrice(),
                                                Currency.getInstance(entity.getCurrency())),
                                ProductStatus.valueOf(entity.getStatus().name()));

                entity.getCategories().forEach(category -> product.addCategory(
                                ProductCategory.valueOf(category.name())));

                entity.getAssets().forEach(asset -> product.addAsset(
                                new DigitalAsset(
                                                new AssetId(asset.getId()),
                                                DigitalAssetType.valueOf(asset.getType().name()),
                                                asset.getStorageKey())));

                return product;
        }
}