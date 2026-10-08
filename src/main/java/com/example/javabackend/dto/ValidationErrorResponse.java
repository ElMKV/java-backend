package com.example.javabackend.dto;

import java.util.Map;

public record ValidationErrorResponse(
        int status,
        Map<String, String> errors
) {
}