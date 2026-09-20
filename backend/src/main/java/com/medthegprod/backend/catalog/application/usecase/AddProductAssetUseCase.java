package com.medthegprod.backend.catalog.application.usecase;

import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;

public interface AddProductAssetUseCase {

    Product execute(
            ProductId productId,
            DigitalAssetType type,
            String storageKey);
}