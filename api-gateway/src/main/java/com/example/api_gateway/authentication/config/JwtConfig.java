package com.example.api_gateway.authentication.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

    public String getSecretKey() {
        return secretKey;
    }

    public long getExpirationMs() {
        return expirationMs;
    }
}
