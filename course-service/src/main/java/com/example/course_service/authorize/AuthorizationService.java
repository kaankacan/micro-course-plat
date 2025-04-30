package com.example.course_service.authorize;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class AuthorizationService {

    @Value("${jwt.secret}")
    private String secretKey;

    public void checkAdminRole(String token) {
        Claims claims = extractClaims(token);

        String role = claims.get("role", String.class);

        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("You are not authorized to perform this action!");
        }
    }

    private Claims extractClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
