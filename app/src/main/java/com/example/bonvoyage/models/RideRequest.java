package com.example.bonvoyage.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RideRequest implements Parcelable {
    private GeoPoint startGeopoint;
    private GeoPoint endGeopoint;
    private @ServerTimestamp Date timestamp;
    private String userEmail;
    private String status;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private float cost;

    public RideRequest(GeoPoint startGeopoint, GeoPoint endGeopoint, Date timestamp, String userEmail, String status, String phoneNumber, String firstName, String lastName, float cost){
        this.startGeopoint = startGeopoint;
        this.endGeopoint = endGeopoint;
        this.timestamp = timestamp;
        this.userEmail = userEmail;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cost = cost;
        this.status = status;
    }



    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public RideRequest(){

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public String toString(){
        return "RideRequest{ userEmail=" + userEmail +
                ", startGeopoint=" + startGeopoint +
                ", endGeopoint=" + endGeopoint
                +", timestamp=" + timestamp +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", phoneNumber=" + phoneNumber +
                ", status="+status+"}";
    }

    // parcelling information

    public static final Creator<RideRequest> CREATOR = new Creator<RideRequest>() {
        @Override
        public RideRequest createFromParcel(Parcel in) {
            return new RideRequest(in);
        }

        @Override
        public RideRequest[] newArray(int size) {
            return new RideRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userEmail);
        dest.writeDouble(this.startGeopoint.getLatitude());
        dest.writeDouble(this.startGeopoint.getLongitude());
        dest.writeDouble(this.endGeopoint.getLatitude());
        dest.writeDouble(this.endGeopoint.getLongitude());
        dest.writeLong(timestamp.getTime());
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.phoneNumber);
        dest.writeFloat(this.cost);
        dest.writeString(this.status);

    }

    protected RideRequest(Parcel in) {
        userEmail = in.readString();
        Double startGeopointLatitude = in.readDouble();
        Double startGeopointLongitude = in.readDouble();
        Double endGeopointLatitude = in.readDouble();
        Double endGeopointLongitude = in.readDouble();
        timestamp = new Date(in.readLong());
        firstName = in.readString();
        lastName = in.readString();
        phoneNumber = in.readString();
        cost = in.readFloat();
        status = in.readString();
        startGeopoint = new GeoPoint(startGeopointLatitude,startGeopointLongitude);
        endGeopoint = new GeoPoint(endGeopointLatitude, endGeopointLongitude);
    }


}
