package com.example.bonvoyage;
public abstract class User {

    // Active RideRequest
    // History
    // Email
    // Phone number
    private String firstname;
    private String lastname;
    private String email;
    private String phonenumber;
    private String password;
    private int wallet;

    public User(String firstname, String lastname, String email, String phonenumber, String password, int wallet){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
        this.wallet = wallet;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getEmail() {
        return email;
    }
    public String getPhonenumber() {
        return phonenumber;
    }
    public String getPassword() {
        return password;
    }
    public int getWallet() {
        return wallet;
    }

    // this is for testing only
    public String getUserType() {
        return "driver";
    }
}
