package com.medthegprod.backend.sales.infrastructure.payment;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe")
public record StripeProperties(
                String secretKey,
                String webhookSecret,
                String successUrl,
                String cancelUrl) {
}