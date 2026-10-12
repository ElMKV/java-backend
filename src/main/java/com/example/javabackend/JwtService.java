package com.example.javabackend;

import org.springframework.beans.factory.annotation.Value;

public class JwtService {
    @Value("${jwt.secret}")
    String secret;

    @Value("${jwt.expiration}")
    long expiration;
}
