package com.example.bonvoyage;

public class User {
    // TODO: This needs to be made abstract
    private final String username;
    private final String userType;
    public User(String username, String userType) {
        this.username = username;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public String getUserType() {
        return userType;
    }
}
