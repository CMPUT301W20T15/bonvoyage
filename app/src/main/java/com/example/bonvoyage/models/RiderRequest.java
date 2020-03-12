package com.example.bonvoyage.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class RiderRequest {
    private GeoPoint startGeopoint;
    private GeoPoint endGeopoint;
    private @ServerTimestamp Date timestamp;
    private String userEmail;
    private String status;
    private String phoneNumber;
    private String firstName;
    private String lastName;

    public RiderRequest(GeoPoint startGeopoint, GeoPoint endGeopoint, Date timestamp, String userEmail, String status, String phoneNumber, String firstName, String lastName){
        this.startGeopoint = startGeopoint;
        this.endGeopoint = endGeopoint;
        this.timestamp = timestamp;
        this.userEmail = userEmail;
        this.status = status;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public RiderRequest(){

    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GeoPoint getStartGeopoint() {
        return startGeopoint;
    }

    public void setStartGeopoint(GeoPoint startGeopoint) {
        this.startGeopoint = startGeopoint;
    }

    public GeoPoint getEndGeopoint() {
        return endGeopoint;
    }

    public void setEndGeopoint(GeoPoint endGeopoint) {
        this.endGeopoint = endGeopoint;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    @Override
    public String toString(){
        return "RiderLocation{ userEmail=" + userEmail +
                ", start_geopoint=" + startGeopoint +
                ", end_geopoint=" + endGeopoint
                +", timestamp=" + timestamp +
                ", status="+status+"}";
    }
}
