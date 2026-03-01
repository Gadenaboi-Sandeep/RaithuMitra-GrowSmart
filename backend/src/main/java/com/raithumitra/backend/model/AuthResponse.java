package com.raithumitra.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String role;
    private String message;
    private String fullName;
    private String phoneNumber;
    private String address;
}
