package com.medthegprod.backend.sales.infrastructure.payment.stripe;

import com.medthegprod.backend.sales.application.usecase.MarkOrderAsPaidUseCase;
import com.medthegprod.backend.sales.domain.model.OrderId;
import com.stripe.net.Webhook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class StripeWebhookControllerTest {

    private static final String WEBHOOK_SECRET = "whsec_test_secret";

    @Mock
    private MarkOrderAsPaidUseCase markOrderAsPaidUseCase;

    private StripeWebhookController controller;

    @BeforeEach
    void setUp() {
        controller = new StripeWebhookController(
                new StripeProperties("sk_test_key", WEBHOOK_SECRET, "success", "cancel"),
                markOrderAsPaidUseCase);
    }

    @Test
    void verifiesSignatureAndAcknowledgesPaidFixtureWithoutAnApplicationOrder() throws Exception {
        String payload = checkoutCompletedPayload("2026-08-26.dahlia", "{}");

        ResponseEntity<Void> response = controller.handleWebhook(payload, signatureFor(payload));

        assertEquals(200, response.getStatusCode().value());
        verifyNoInteractions(markOrderAsPaidUseCase);
    }

    @Test
    void rejectsInvalidSignature() throws Exception {
        String payload = checkoutCompletedPayload("2026-08-26.dahlia", "{}");
        String invalidSignature = "t=" + Instant.now().getEpochSecond() + ",v1=invalid";

        ResponseEntity<Void> response = controller.handleWebhook(payload, invalidSignature);

        assertEquals(400, response.getStatusCode().value());
        verifyNoInteractions(markOrderAsPaidUseCase);
    }

    @Test
    void marksPaidApplicationSessionUsingCompatibleFallbackDeserialization() throws Exception {
        UUID orderId = UUID.randomUUID();
        String metadata = "{\"orderId\":\"" + orderId + "\"}";
        String payload = checkoutCompletedPayload("2026-08-26.dahlia", metadata);

        ResponseEntity<Void> response = controller.handleWebhook(payload, signatureFor(payload));

        assertEquals(200, response.getStatusCode().value());
        verify(markOrderAsPaidUseCase).execute(new OrderId(orderId), "cs_test_123");
    }

    private static String checkoutCompletedPayload(String apiVersion, String metadata) {
        return """
                {"id":"evt_test","object":"event","api_version":"%s","type":"checkout.session.completed",
                 "data":{"object":{"id":"cs_test_123","object":"checkout.session","payment_status":"paid","metadata":%s}}}
                """.formatted(apiVersion, metadata);
    }

    private static String signatureFor(String payload) throws Exception {
        long timestamp = Instant.now().getEpochSecond();
        String signedPayload = timestamp + "." + payload;
        return "t=" + timestamp + ",v1=" + Webhook.Util.computeHmacSha256(WEBHOOK_SECRET, signedPayload);
    }
}
