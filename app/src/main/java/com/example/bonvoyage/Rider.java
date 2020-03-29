package com.example.bonvoyage;

import com.google.android.gms.maps.model.LatLng;

public class Rider extends User {

    public Rider(String firstname, String lastname, String email, String phonenumber, String password, float wallet) {
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

}
