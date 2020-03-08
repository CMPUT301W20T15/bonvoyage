package com.example.bonvoyage;

import com.google.android.gms.maps.model.LatLng;

public class Rider {
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
