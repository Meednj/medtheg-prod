package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.application.event.ProductPublishedEvent;
import com.medthegprod.backend.catalog.application.port.EventPublisher;
import com.medthegprod.backend.catalog.application.usecase.PublishProductUseCase;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublishProductService implements PublishProductUseCase {

    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    public PublishProductService(
            ProductRepository productRepository,
            EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public Product execute(ProductId productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.publish();

        Product savedProduct = productRepository.save(product);

        eventPublisher.publish(
                new ProductPublishedEvent(savedProduct.getId()));

        return savedProduct;
    }
}