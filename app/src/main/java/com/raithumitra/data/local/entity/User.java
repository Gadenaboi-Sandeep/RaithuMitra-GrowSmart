package com.raithumitra.data.local.entity;

public class User {
    public int id; // ID from backend

    public String email;
    public String fullName; // Flattened for display
    public String role; // FARMER, CORPORATE, WORKER, OWNER
    public String location;

    // Auth Token (Optional to store here, or in SessionManager)
    // We keep basic profile info here for UI binding

    public User(int id, String fullName, String role, String phoneNumber, String address) {
        this.id = id;
        this.fullName = fullName;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String phoneNumber;
    public String address;
    // public String email; // Backend uses phoneNumber as ID
}
