package com.example.javabackend;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final String secret;
    private final long expiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.secret = secret;
        this.expiration = expiration;
    }

    public String generateToken(Long id, Role role) {
        SecretKey key = getSigningKey();

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(id.toString())
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public Long extractUserId(String token) {
        String id = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        return Long.valueOf(id);
    }

    public Role extractRole(String token) {
        String role = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload().get("role", String.class);
        return  Role.valueOf(role);
    }
}