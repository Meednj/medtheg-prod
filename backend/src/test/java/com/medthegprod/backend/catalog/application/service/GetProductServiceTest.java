package com.medthegprod.backend.catalog.application.service;

import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Test
    void publicLookupUsesPublishedRepositoryBoundary() {
        ProductId productId = ProductId.generate();
        when(productRepository.findPublishedById(productId))
                .thenReturn(Optional.empty());

        GetProductService service = new GetProductService(productRepository);

        assertThrows(ProductNotFoundException.class,
                () -> service.execute(productId));
        verify(productRepository).findPublishedById(productId);
    }
}