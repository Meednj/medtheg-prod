package com.medthegprod.backend.entitlement.infrastructure.web.controller;

import com.medthegprod.backend.entitlement.application.usecase.ListCustomerEntitlementsUseCase;
import com.medthegprod.backend.entitlement.infrastructure.web.dto.EntitlementResponse;
import com.medthegprod.backend.sales.infrastructure.web.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/entitlements")
public class EntitlementController {

    private final ListCustomerEntitlementsUseCase listCustomerEntitlementsUseCase;

    public EntitlementController(
            ListCustomerEntitlementsUseCase listCustomerEntitlementsUseCase) {
        this.listCustomerEntitlementsUseCase = listCustomerEntitlementsUseCase;
    }

    @GetMapping
    public List<EntitlementResponse> getMyEntitlements(
            Authentication authentication) {
        var customerId = AuthenticatedUser.getUserId(authentication);

        return listCustomerEntitlementsUseCase
                .execute(customerId.value())
                .stream()
                .map(EntitlementResponse::from)
                .toList();
    }
}