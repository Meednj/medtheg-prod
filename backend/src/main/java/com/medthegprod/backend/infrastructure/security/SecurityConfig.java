package com.medthegprod.backend.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

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
                        .requestMatchers("/actuator/health")
                        .permitAll()

                        .anyRequest()
                        .authenticated())

                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(
                        jwtAuthenticationConverter())))

                .build();
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