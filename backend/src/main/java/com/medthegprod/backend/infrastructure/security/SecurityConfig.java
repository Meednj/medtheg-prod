package com.medthegprod.backend.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http) throws Exception {

                return http
                                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

                                .authorizeHttpRequests(auth -> auth

                                                // public authentication endpoints
                                                .requestMatchers(
                                                                "/api/auth/register",
                                                                "/api/auth/login")
                                                .permitAll()

                                                // Stripe authenticates this endpoint with its signature.
                                                .requestMatchers("/api/webhooks/stripe")
                                                .permitAll()

                                                // OpenAPI resources are public documentation.
                                                .requestMatchers(
                                                                "/v3/api-docs/**",
                                                                "/swagger-ui.html",
                                                                "/swagger-ui/**")
                                                .permitAll()

                                                // public product browsing
                                                .requestMatchers(
                                                                org.springframework.http.HttpMethod.GET,
                                                                "/api/products",
                                                                "/api/products/**")
                                                .permitAll()

                                                // product management requires ADMIN
                                                .requestMatchers("/api/products/**")
                                                .hasRole("ADMIN")

                                                // actuator health
                                                .requestMatchers(
                                                                "/actuator/health",
                                                                "/actuator/health/**",
                                                                "/actuator/prometheus")
                                                .permitAll()

                                                .anyRequest()
                                                .authenticated())

                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .authenticationEntryPoint(authenticationEntryPoint())
                                                .accessDeniedHandler(accessDeniedHandler())
                                                .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                                                jwtAuthenticationConverter())))

                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint(authenticationEntryPoint())
                                                .accessDeniedHandler(accessDeniedHandler()))

                                .build();
        }

        @Bean
        public AuthenticationEntryPoint authenticationEntryPoint() {
                return (request, response, exception) -> writeSecurityError(
                                response,
                                401,
                                "UNAUTHORIZED",
                                "Authentication is required to access this resource");
        }

        @Bean
        public AccessDeniedHandler accessDeniedHandler() {
                return (request, response, exception) -> writeSecurityError(
                                response,
                                403,
                                "FORBIDDEN",
                                "You do not have permission to access this resource");
        }

        private void writeSecurityError(
                        jakarta.servlet.http.HttpServletResponse response,
                        int status,
                        String code,
                        String message) throws java.io.IOException {
                response.setStatus(status);
                response.setContentType("application/json");
                response.getOutputStream().write(("{\"status\":" + status
                                + ",\"code\":\"" + code
                                + "\",\"error\":\"" + code
                                + "\",\"message\":\"" + message + "\"}")
                                .getBytes(StandardCharsets.UTF_8));
        }

        @Bean
        public JwtAuthenticationConverter jwtAuthenticationConverter() {

                JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

                authoritiesConverter.setAuthoritiesClaimName("role");
                authoritiesConverter.setAuthorityPrefix("ROLE_");

                JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

                converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

                return converter;
        }
}