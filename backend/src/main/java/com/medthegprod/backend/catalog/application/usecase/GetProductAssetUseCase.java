package com.medthegprod.backend.catalog.application.usecase;

import java.util.UUID;

public interface GetProductAssetUseCase {

    ProductAsset execute(UUID assetId);
}