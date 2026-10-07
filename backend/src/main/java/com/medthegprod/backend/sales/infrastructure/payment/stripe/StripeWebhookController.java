package com.medthegprod.backend.sales.infrastructure.payment.stripe;

import com.medthegprod.backend.sales.application.usecase.MarkOrderAsPaidUseCase;
import com.medthegprod.backend.sales.domain.model.OrderId;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
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
            logger.warn("Stripe webhook signature verification failed");
            return ResponseEntity.badRequest().build();
        }

        try {
            switch (event.getType()) {
                case "checkout.session.completed" -> handleCheckoutCompleted(event);
                default -> logger.debug("Ignoring unsupported Stripe event type: {}", event.getType());
            }
        } catch (RuntimeException e) {
            String causeType = e.getCause() == null ? "none" : e.getCause().getClass().getSimpleName();
            logger.error("Stripe webhook processing failed eventId={} eventType={} exceptionType={} causeType={}",
                    event.getId(), event.getType(), e.getClass().getSimpleName(), causeType);
            throw e;
        }

        return ResponseEntity.ok().build();
    }

    private void handleCheckoutCompleted(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        StripeObject object = deserializer.getObject().orElse(null);

        if (object == null) {
            logger.info("Deserializing Stripe event with API version mismatch eventId={} apiVersion={}",
                    event.getId(), event.getApiVersion());
            try {
                object = deserializer.deserializeUnsafe();
            } catch (EventDataObjectDeserializationException e) {
                throw new IllegalStateException("Unable to deserialize Stripe Checkout Session", e);
            }
        }

        if (!(object instanceof Session session)) {
            throw new IllegalStateException("Stripe checkout.session.completed contained an unexpected object type");
        }

        if (!"paid".equals(session.getPaymentStatus())) {
            return;
        }

        String orderId = session
                .getMetadata()
                .get("orderId");

        if (orderId == null || orderId.isBlank()) {
            logger.warn("Ignoring verified checkout.session.completed event without orderId metadata eventId={}",
                    event.getId());
            return;
        }

        markOrderAsPaidUseCase.execute(
                new OrderId(UUID.fromString(orderId)),
                session.getId());
    }
}
