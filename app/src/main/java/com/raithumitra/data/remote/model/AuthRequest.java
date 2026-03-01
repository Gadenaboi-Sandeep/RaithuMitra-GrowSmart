package com.raithumitra.data.remote.model;

public class AuthRequest {
    private String phoneNumber;
    private String password;
    private String fullName;
    private String role;
    private String address;

    public AuthRequest(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public AuthRequest(String fullName, String phoneNumber, String password, String role, String address) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.address = address;
    }
}
