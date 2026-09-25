package com.medthegprod.backend.library.infrastructure.web.controller;

import com.medthegprod.backend.library.application.usecase.GetAssetDownloadUrlUseCase;
import com.medthegprod.backend.sales.infrastructure.web.AuthenticatedUser;

import java.time.Duration;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/library/assets")
public class LibraryAssetController {

        private final GetAssetDownloadUrlUseCase getAssetDownloadUrlUseCase;

        public LibraryAssetController(
                        GetAssetDownloadUrlUseCase getAssetDownloadUrlUseCase) {
                this.getAssetDownloadUrlUseCase = getAssetDownloadUrlUseCase;
        }

        @GetMapping("/{assetId}/download")
        public DownloadUrlResponse getDownloadUrl(
                        @PathVariable UUID assetId,
                        Authentication authentication) {

                var customerId = AuthenticatedUser.getUserId(authentication);

                String url = getAssetDownloadUrlUseCase.execute(
                                customerId.value(),
                                assetId,
                                Duration.ofMinutes(15));

                return new DownloadUrlResponse(url);
        }

        public record DownloadUrlResponse(String url) {
        }
}