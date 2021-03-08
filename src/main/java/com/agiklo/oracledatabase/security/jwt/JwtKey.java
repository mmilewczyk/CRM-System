package com.agiklo.oracledatabase.security.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.security.Key;

@Configuration
@AllArgsConstructor
public class JwtKey {
    private final JwtConfiguration jwtConfiguration;

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtConfiguration.getKey().getBytes());
    }
}
