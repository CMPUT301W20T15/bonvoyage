package com.example.bonvoyage.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{

    private String email_address;
    private String password;
    private String first_name;
    private String last_name;
    private String phone_number;
    public User(String email_address, String password, String first_name, String last_name, String phone_number) {
        this.email_address = email_address;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
    }

    public User() {

    }

    protected User(Parcel in) {
        email_address = in.readString();
        password = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        phone_number = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email_address + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + first_name + '\'' +
                ", lastname='" + last_name + '\'' +
                ", phonenumber='" + phone_number + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email_address);
        dest.writeString(password);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(phone_number);
    }
}
