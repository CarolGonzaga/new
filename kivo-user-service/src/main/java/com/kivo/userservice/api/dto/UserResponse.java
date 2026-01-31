package com.kivo.userservice.api.dto;

public record UserResponse(
        Long id,
        String email,
        String name,
        String role,
        boolean active
) { }
