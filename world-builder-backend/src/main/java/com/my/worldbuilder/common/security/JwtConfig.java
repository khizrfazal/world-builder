package com.my.worldbuilder.common.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtProperties props;
    private final PemUtils pemUtils;

    @Bean
    public RSAPrivateKey privateKey() throws Exception {
        return pemUtils.loadPrivateKey(props.getPrivateKey());
    }

    @Bean
    public RSAPublicKey publicKey() throws Exception {
        return pemUtils.loadPublicKey(props.getPublicKey());
    }

    @Bean
    public JwtEncoder jwtEncoder(RSAPrivateKey privateKey) {
        return new NimbusJwtEncoder((jwkSelector, context) ->
        {
            try {
                return jwkSelector.select(new JWKSet(new RSAKey.Builder(publicKey()).privateKey(privateKey).build()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        );
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey publicKey) {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
}
