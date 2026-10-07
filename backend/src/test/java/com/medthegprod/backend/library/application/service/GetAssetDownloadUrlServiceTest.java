package com.medthegprod.backend.library.application.service;

import com.medthegprod.backend.catalog.application.usecase.GetProductAssetUseCase;
import com.medthegprod.backend.catalog.application.usecase.ProductAsset;
import com.medthegprod.backend.catalog.domain.model.AssetId;
import com.medthegprod.backend.catalog.domain.model.DigitalAsset;
import com.medthegprod.backend.catalog.domain.model.DigitalAssetType;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.entitlement.domain.model.Entitlement;
import com.medthegprod.backend.entitlement.domain.model.EntitlementStatus;
import com.medthegprod.backend.entitlement.domain.repository.EntitlementRepository;
import com.medthegprod.backend.library.application.port.AssetStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAssetDownloadUrlServiceTest {

    @Mock
    private EntitlementRepository entitlementRepository;

    @Mock
    private GetProductAssetUseCase getProductAssetUseCase;

    @Mock
    private AssetStorage assetStorage;

    @Test
    void shouldGenerateTemporaryUrlForActiveEntitlement() {
        UUID customerId = UUID.randomUUID();
        ProductId productId = ProductId.generate();
        AssetId assetId = AssetId.generate();
        DigitalAsset asset = new DigitalAsset(assetId, DigitalAssetType.AUDIO_WAV, "private/asset.wav");
        when(getProductAssetUseCase.execute(assetId.value())).thenReturn(new ProductAsset(productId, asset));
        when(entitlementRepository.findByCustomerIdAndProductId(customerId, productId.value()))
                .thenReturn(Optional.of(Entitlement.create(customerId, productId, UUID.randomUUID())));
        when(assetStorage.generateDownloadUrl("private/asset.wav", Duration.ofMinutes(15)))
                .thenReturn("https://storage.test/temporary");

        String result = new GetAssetDownloadUrlService(entitlementRepository, getProductAssetUseCase, assetStorage)
                .execute(customerId, assetId.value(), Duration.ofMinutes(15));

        assertEquals("https://storage.test/temporary", result);
        verify(assetStorage).generateDownloadUrl("private/asset.wav", Duration.ofMinutes(15));
    }

    @Test
    void shouldRejectRevokedEntitlement() {
        UUID customerId = UUID.randomUUID();
        ProductId productId = ProductId.generate();
        AssetId assetId = AssetId.generate();
        DigitalAsset asset = new DigitalAsset(assetId, DigitalAssetType.PDF, "private/guide.pdf");
        Entitlement entitlement = Entitlement.create(customerId, productId, UUID.randomUUID());
        entitlement.revoke(java.time.OffsetDateTime.now());
        when(getProductAssetUseCase.execute(assetId.value())).thenReturn(new ProductAsset(productId, asset));
        when(entitlementRepository.findByCustomerIdAndProductId(customerId, productId.value()))
                .thenReturn(Optional.of(entitlement));

        assertEquals(EntitlementStatus.REVOKED, entitlement.getStatus());
        assertThrows(LibraryAccessDeniedException.class,
                () -> new GetAssetDownloadUrlService(entitlementRepository, getProductAssetUseCase, assetStorage)
                        .execute(customerId, assetId.value(), Duration.ofMinutes(15)));
    }
}