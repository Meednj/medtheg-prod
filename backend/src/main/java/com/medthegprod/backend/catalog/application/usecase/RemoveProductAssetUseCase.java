package com.medthegprod.backend.catalog.application.usecase;

import com.medthegprod.backend.catalog.domain.model.AssetId;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;

public interface RemoveProductAssetUseCase {

    Product execute(
            ProductId productId,
            AssetId assetId);
}