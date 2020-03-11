package com.example.bonvoyage;

public class User {
    // Active RideRequest
    // History
    // Email
    // Phone number

    // TODO: add payment information

    // TODO: Add profile class

    private final String email;
    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return "rider";
    }
}
