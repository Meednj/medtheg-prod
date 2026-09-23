package com.medthegprod.backend.sales.infrastructure.payment;

import com.medthegprod.backend.sales.application.usecase.MarkOrderAsPaidUseCase;
import com.medthegprod.backend.sales.domain.model.OrderId;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/webhooks/stripe")
public class StripeWebhookController {

    private final StripeProperties properties;
    private final MarkOrderAsPaidUseCase markOrderAsPaidUseCase;

    public StripeWebhookController(
            StripeProperties properties,
            MarkOrderAsPaidUseCase markOrderAsPaidUseCase) {
        this.properties = properties;
        this.markOrderAsPaidUseCase = markOrderAsPaidUseCase;
    }

    @PostMapping
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

        if ("checkout.session.completed".equals(event.getType())) {
            handleCheckoutCompleted(event);
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