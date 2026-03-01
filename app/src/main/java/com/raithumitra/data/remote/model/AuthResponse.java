package com.raithumitra.data.remote.model;

public class AuthResponse {
    private String token;
    private String role;
    private String message;
    private String fullName;
    private String phoneNumber;
    private String address;

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }
}
