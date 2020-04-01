package com.example.bonvoyage;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bonvoyage.models.RideRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class Rider extends User{

    public Rider(){
        super();
    }

    public Rider(String firstname, String lastname, String email, String phonenumber, String password,
                 float wallet) {
        super(firstname, lastname, email, phonenumber, password, wallet);
    }
    private LatLng currentLocation;
    private LatLng destinationLocation;

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    public LatLng getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(LatLng destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    private Rider(Parcel in){
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Rider> CREATOR = new Parcelable.Creator<Rider>() {
        @Override
        public Rider createFromParcel(Parcel in) {
            return new Rider(in);
        }

        @Override
        public Rider[] newArray(int size) {
            return new Rider[size];
        }
    };
}
