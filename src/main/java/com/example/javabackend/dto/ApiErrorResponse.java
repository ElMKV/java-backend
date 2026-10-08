package com.example.javabackend.dto;

public record ApiErrorResponse(
        int status,
        String message
) {
}