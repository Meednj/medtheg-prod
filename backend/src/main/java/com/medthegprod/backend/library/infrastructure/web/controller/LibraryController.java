package com.medthegprod.backend.library.infrastructure.web.controller;

import com.medthegprod.backend.library.application.usecase.GetCustomerLibraryUseCase;
import com.medthegprod.backend.library.infrastructure.web.dto.LibraryItemResponse;
import com.medthegprod.backend.sales.infrastructure.web.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@Tag(name = "Library", description = "Customer-owned digital assets")
@SecurityRequirement(name = "bearerAuth")
public class LibraryController {

    private final GetCustomerLibraryUseCase getCustomerLibraryUseCase;

    public LibraryController(
            GetCustomerLibraryUseCase getCustomerLibraryUseCase) {
        this.getCustomerLibraryUseCase = getCustomerLibraryUseCase;
    }

    @GetMapping
    @Operation(summary = "Get my library", description = "Returns digital assets for the authenticated customer's active entitlements.")
    public List<LibraryItemResponse> getLibrary(
            Authentication authentication) {

        var customerId = AuthenticatedUser.getUserId(authentication);

        return getCustomerLibraryUseCase
                .execute(customerId.value())
                .stream()
                .map(LibraryItemResponse::from)
                .toList();
    }
}
