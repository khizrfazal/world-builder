package com.my.worldbuilder.common.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Value("${spring.jwt.secret}")
    private String secret;

    @Bean
    public SecretKey jwtSecretKey() {
        return new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {

        OctetSequenceKey jwk = new OctetSequenceKey.Builder(jwtSecretKey)
                .algorithm(com.nimbusds.jose.JWSAlgorithm.HS256)
                .keyID("jwt-key")
                .build();

        JWKSet jwkSet = new JWKSet(jwk);

        ImmutableJWKSet<SecurityContext> jwkSource =
                new ImmutableJWKSet<>(jwkSet);

        NimbusJwtEncoder encoder = new NimbusJwtEncoder(jwkSource);

        encoder.setJwkSelector(keys -> keys.get(0));

        return encoder;
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
        return NimbusJwtDecoder
                .withSecretKey(jwtSecretKey)
                .build();
    }
}