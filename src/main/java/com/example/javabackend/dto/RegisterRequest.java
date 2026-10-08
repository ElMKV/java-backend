package com.example.javabackend.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @NotBlank
        @Size(min = 8, max = 72)
        String password,

        @NotNull
        @Min(0)
        @Max(150)
        Integer age,

        @NotNull
        Boolean isMale
) {

}
