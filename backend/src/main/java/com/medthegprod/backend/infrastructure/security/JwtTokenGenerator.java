package com.medthegprod.backend.infrastructure.security;

import com.medthegprod.backend.identity.application.port.TokenGenerator;
import com.medthegprod.backend.identity.domain.model.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtTokenGenerator implements TokenGenerator {

    private final JwtEncoder jwtEncoder;

    public JwtTokenGenerator(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String generate(User user) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getId().value().toString())
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}