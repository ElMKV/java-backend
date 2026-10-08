package com.example.javabackend.dto;


public record UserResponse(
        Long id,
        String name,
        String email,
        Integer age,
        Boolean isMale
) {


}