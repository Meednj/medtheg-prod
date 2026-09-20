package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.application.usecase.RemoveProductAssetUseCase;
import com.medthegprod.backend.catalog.domain.model.AssetId;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RemoveProductAssetService implements RemoveProductAssetUseCase {

    private final ProductRepository productRepository;

    public RemoveProductAssetService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product execute(
            ProductId productId,
            AssetId assetId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.removeAsset(assetId);

        return productRepository.save(product);
    }
}