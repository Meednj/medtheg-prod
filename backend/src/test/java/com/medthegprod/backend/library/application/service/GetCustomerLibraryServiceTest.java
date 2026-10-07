package com.medthegprod.backend.library.application.service;

import com.medthegprod.backend.catalog.application.usecase.GetProductUseCase;
import com.medthegprod.backend.catalog.application.usecase.ListProductAssetsUseCase;
import com.medthegprod.backend.catalog.application.usecase.ProductAsset;
import com.medthegprod.backend.catalog.domain.model.DigitalAsset;
import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;
import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.model.ProductType;
import com.medthegprod.backend.entitlement.domain.model.Entitlement;
import com.medthegprod.backend.entitlement.domain.repository.EntitlementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCustomerLibraryServiceTest {

    @Mock
    private EntitlementRepository entitlementRepository;

    @Mock
    private GetProductUseCase getProductUseCase;

    @Mock
    private ListProductAssetsUseCase listProductAssetsUseCase;

    @Test
    void shouldListOnlyActiveEntitlementsAndNeverExposeStorageKeys() {
        UUID customerId = UUID.randomUUID();
        ProductId productId = ProductId.generate();
        Product product = new Product(productId, "Owned Beat", "Description", ProductType.BEAT,
                Money.eur(new BigDecimal("10.00")));
        product.addCategory(ProductCategory.BEATS);
        product.publish();
        Entitlement active = Entitlement.create(customerId, productId, UUID.randomUUID());
        Entitlement revoked = Entitlement.create(customerId, ProductId.generate(), UUID.randomUUID());
        revoked.revoke(java.time.OffsetDateTime.now());
        DigitalAsset asset = new DigitalAsset(com.medthegprod.backend.catalog.domain.model.AssetId.generate(),
                DigitalAssetType.AUDIO_MP3, "private/owned.mp3");
        when(entitlementRepository.findByCustomerId(customerId)).thenReturn(List.of(active, revoked));
        when(getProductUseCase.execute(productId)).thenReturn(product);
        when(listProductAssetsUseCase.execute(productId)).thenReturn(List.of(new ProductAsset(productId, asset)));

        var result = new GetCustomerLibraryService(entitlementRepository, getProductUseCase, listProductAssetsUseCase)
                .execute(customerId);

        assertEquals(1, result.size());
        assertEquals(active.getId().value(), result.get(0).entitlementId());
        assertEquals(asset.getId().value(), result.get(0).assets().get(0).assetId());
        assertEquals("AUDIO_MP3", result.get(0).assets().get(0).type());
    }
}