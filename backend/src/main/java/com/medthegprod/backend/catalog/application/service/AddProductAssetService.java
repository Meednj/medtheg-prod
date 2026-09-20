package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.application.usecase.AddProductAssetUseCase;
import com.medthegprod.backend.catalog.domain.model.AssetId;
import com.medthegprod.backend.catalog.domain.model.DigitalAsset;
import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddProductAssetService implements AddProductAssetUseCase {

    private final ProductRepository productRepository;

    public AddProductAssetService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product execute(
            ProductId productId,
            DigitalAssetType type,
            String storageKey) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        DigitalAsset asset = new DigitalAsset(
                AssetId.generate(),
                type,
                storageKey);

        product.addAsset(asset);

        return productRepository.save(product);
    }
}