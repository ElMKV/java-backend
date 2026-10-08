package com.example.javabackend.dto;

import jakarta.validation.constraints.*;

public record UserRequest(
        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @NotNull
        @Min(0)
        @Max(150)
        Integer age,

        @NotNull
        Boolean isMale
) {
}