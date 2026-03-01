package com.raithumitra.backend.model;

import lombok.Data;

@Data
public class AuthRequest {
    private String phoneNumber;
    private String password;
    private String fullName; // Optional for login, required for signup
    private String role;     // Optional for login, required for signup
    private String address;  // Optional
}
