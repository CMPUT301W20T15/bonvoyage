package com.example.bonvoyage.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class RiderLocation {
    private GeoPoint start_geopoint;
    private GeoPoint end_geopoint;
    private @ServerTimestamp Date timestamp;
    private String user_email;
    private String status;
    private String first_name;

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
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

    private String last_name;
    private String phone_number;
    public RiderLocation(GeoPoint start_geopoint,GeoPoint end_geopoint, Date timestamp, String user_email, String status,
                         String first_name, String last_name, String phone_number){
        this.start_geopoint = start_geopoint;
        this.end_geopoint = end_geopoint;
        this.timestamp = timestamp;
        this.user_email = user_email;
        this.status = status;
        this.phone_number = phone_number;
        this.first_name = first_name;
        this.last_name = last_name;
    }
    public RiderLocation(){

    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GeoPoint getStart_geopoint() {
        return start_geopoint;
    }

    public void setStart_geopoint(GeoPoint start_geopoint) {
        this.start_geopoint = start_geopoint;
    }

    public GeoPoint getEnd_geopoint() {
        return end_geopoint;
    }

    public void setEnd_geopoint(GeoPoint end_geopoint) {
        this.end_geopoint = end_geopoint;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString(){
        return "RiderLocation{ userEmail=" + user_email +
                ", start_geopoint=" + start_geopoint +
                ", end_geopoint=" + end_geopoint
                +", timestamp=" + timestamp +
                ", status="+status+"}";
    }
}
