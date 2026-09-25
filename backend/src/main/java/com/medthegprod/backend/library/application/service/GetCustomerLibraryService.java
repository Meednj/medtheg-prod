package com.medthegprod.backend.library.application.service;

import com.medthegprod.backend.catalog.application.usecase.GetProductUseCase;
import com.medthegprod.backend.catalog.application.usecase.ListProductAssetsUseCase;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.entitlement.domain.model.Entitlement;
import com.medthegprod.backend.entitlement.domain.model.EntitlementStatus;
import com.medthegprod.backend.entitlement.domain.repository.EntitlementRepository;
import com.medthegprod.backend.library.application.usecase.GetCustomerLibraryUseCase;
import com.medthegprod.backend.library.application.usecase.LibraryAsset;
import com.medthegprod.backend.library.application.usecase.LibraryItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetCustomerLibraryService implements GetCustomerLibraryUseCase {

    private final EntitlementRepository entitlementRepository;
    private final GetProductUseCase getProductUseCase;
    private final ListProductAssetsUseCase listProductAssetsUseCase;

    public GetCustomerLibraryService(
            EntitlementRepository entitlementRepository,
            GetProductUseCase getProductUseCase,
            ListProductAssetsUseCase listProductAssetsUseCase) {
        this.entitlementRepository = entitlementRepository;
        this.getProductUseCase = getProductUseCase;
        this.listProductAssetsUseCase = listProductAssetsUseCase;
    }

    @Override
    public List<LibraryItem> execute(UUID customerId) {

        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        return entitlementRepository.findByCustomerId(customerId)
                .stream()
                .filter(entitlement -> entitlement
                        .getStatus() == EntitlementStatus.ACTIVE)
                .map(this::toLibraryItem)
                .toList();
    }

    private LibraryItem toLibraryItem(Entitlement entitlement) {

        Product product = getProductUseCase.execute(
                entitlement.getProductId());

        List<LibraryAsset> assets = listProductAssetsUseCase
                .execute(entitlement.getProductId())
                .stream()
                .map(productAsset -> new LibraryAsset(
                        productAsset.asset().getId().value(),
                        productAsset.productId().value(),
                        productAsset.asset().getType().name()))
                .toList();

        return new LibraryItem(
                entitlement.getId().value(),
                entitlement.getProductId().value(),
                entitlement.getOrderId(),
                product.getTitle(),
                product.getDescription(),
                product.getType().name(),
                product.getPrice().amount(),
                product.getPrice().currency(),
                entitlement.getGrantedAt(),
                assets);
    }

}