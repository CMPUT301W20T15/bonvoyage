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

    public User(String firstname, String lastname, String email, String phonenumber, String password){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
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


    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    // this is for testing only
    public String getUserType() {
        return "rider";
    }
}
