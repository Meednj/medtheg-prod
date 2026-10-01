package com.medthegprod.backend.sales.infrastructure.payment.stripe;

import com.medthegprod.backend.sales.application.usecase.MarkOrderAsPaidUseCase;
import com.medthegprod.backend.sales.domain.model.OrderId;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/webhooks/stripe")
@Tag(name = "Payments", description = "Stripe payment webhook")
public class StripeWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);

    private final StripeProperties properties;
    private final MarkOrderAsPaidUseCase markOrderAsPaidUseCase;

    public StripeWebhookController(
            StripeProperties properties,
            MarkOrderAsPaidUseCase markOrderAsPaidUseCase) {
        this.properties = properties;
        this.markOrderAsPaidUseCase = markOrderAsPaidUseCase;
    }

    @PostMapping
    @Operation(summary = "Receive a Stripe webhook", description = "Public endpoint validated with the Stripe-Signature header. It does not require JWT authentication.")
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {
        final Event event;

        try {
            event = Webhook.constructEvent(
                    payload,
                    signature,
                    properties.webhookSecret());
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        }

        switch (event.getType()) {
            case "checkout.session.completed" -> handleCheckoutCompleted(event);
            default -> logger.debug("Ignoring unsupported Stripe event type: {}", event.getType());
        }

        return ResponseEntity.ok().build();
    }

    private void handleCheckoutCompleted(Event event) {

        Session session = (Session) event
                .getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException(
                        "Unable to deserialize Stripe Checkout Session"));

        if (!"paid".equals(session.getPaymentStatus())) {
            return;
        }

        String orderId = session
                .getMetadata()
                .get("orderId");

        if (orderId == null || orderId.isBlank()) {
            throw new IllegalStateException(
                    "Stripe Checkout Session is missing orderId metadata");
        }

        markOrderAsPaidUseCase.execute(
                new OrderId(UUID.fromString(orderId)),
                session.getId());
    }
}