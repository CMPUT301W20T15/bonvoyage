package com.example.bonvoyage;

import android.os.Bundle;

public interface DriverStatusListener {
    void onRequestAccepted(Bundle requestInfo);
    void onBeginRide(Bundle requestInfo);
    void onRideComplete();
    void onRideCanceled();
}
