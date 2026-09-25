package com.medthegprod.backend.catalog.application.usecase;

import com.medthegprod.backend.catalog.domain.model.ProductId;
import java.util.List;

public interface ListProductAssetsUseCase {
    List<ProductAsset> execute(ProductId productId);
}