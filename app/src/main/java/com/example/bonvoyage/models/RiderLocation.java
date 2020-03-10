package com.example.bonvoyage.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class RiderLocation {
    private GeoPoint start_geopoint;
    private GeoPoint end_geopoint;
    private @ServerTimestamp Date timestamp;
    private String userEmail;
    private String status;
    public RiderLocation(GeoPoint start_geopoint,GeoPoint end_geopoint, Date timestamp, String userEmail, String status){
        this.start_geopoint = start_geopoint;
        this.end_geopoint = end_geopoint;
        this.timestamp = timestamp;
        this.userEmail = userEmail;
        this.status = status;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    @Override
    public String toString(){
        return "RiderLocation{ userEmail=" + userEmail +
                ", start_geopoint=" + start_geopoint +
                ", end_geopoint=" + end_geopoint
                +", timestamp=" + timestamp +
                ", status="+status+"}";
    }
}
