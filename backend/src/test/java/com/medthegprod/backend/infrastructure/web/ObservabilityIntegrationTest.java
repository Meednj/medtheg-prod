package com.medthegprod.backend.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ObservabilityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGenerateRequestIdWhenMissing() throws Exception {

        mockMvc.perform(
                get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(
                        header().string(
                                "X-Request-ID",
                                matchesPattern(
                                        "[0-9a-fA-F-]{36}")));
    }

    @Test
    void shouldPreserveProvidedRequestId() throws Exception {

        mockMvc.perform(
                get("/actuator/health")
                        .header(
                                "X-Request-ID",
                                "test-request-123"))
                .andExpect(status().isOk())
                .andExpect(
                        header().string(
                                "X-Request-ID",
                                "test-request-123"));
    }

    @Test
    void shouldExposeLivenessEndpoint() throws Exception {

        mockMvc.perform(
                get("/actuator/health/liveness"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldExposeReadinessEndpoint() throws Exception {

        mockMvc.perform(
                get("/actuator/health/readiness"))
                .andExpect(status().isOk());
    }
}