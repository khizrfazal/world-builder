package com.my.worldbuilder.common.security;

import com.my.worldbuilder.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final long expiration;

    public JwtService(
            JwtEncoder jwtEncoder,
            @Value("${spring.jwt.expiration}") long expiration
    ) {
        this.jwtEncoder = jwtEncoder;
        this.expiration = expiration;
    }

    public String generateToken(User user) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusMillis(expiration))
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}