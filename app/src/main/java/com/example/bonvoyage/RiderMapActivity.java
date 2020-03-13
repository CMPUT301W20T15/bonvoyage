package com.example.bonvoyage;

import android.content.Intent;
import android.location.Address;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiderMapActivity extends MapActivity {

    private static final String TAG = "RiderMapActivity";
    private EditText destinationLocationBox;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);

        ConstraintLayout riderView = findViewById(R.id.rider_layout);
        riderView.setVisibility(View.VISIBLE);

        TextView currentLocationBox = findViewById(R.id.startLocation);
        currentLocationBox.setOnClickListener(v -> setCurrentLocation());

        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(v -> continueToPayment());
        destinationLocationBox = findViewById(R.id.endLocation);
        destinationLocationBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    Address address = geoLocate(destinationLocationBox);
                    endLocation = new GeoPoint(address.getLatitude(), address.getLatitude());
                }

                return false;
            }
        });
    }

    private void setCurrentLocation() {
        Log.d(TAG, "Updated current location");
        getDeviceLocation();

    }

    private void setDestinationLocation() {
        Log.d(TAG, "Updated destination location");
        getDeviceLocation();
        // TODO: update in database
        // TODO: draw line
        // TODO: calculate destination amount
    }

    private void continueToPayment() {
        Log.d(TAG, "Start location: " + startLocation.getLatitude()
                + ", " + startLocation.getLongitude());
        Log.d(TAG, "Destination location: " + endLocation.getLatitude()
                + ", " + endLocation.getLongitude());
        Map<String, Object> tripInformation = new HashMap<>();
        tripInformation.put("cost", 10.00);
        tripInformation.put("endGeopoint", endLocation);
        tripInformation.put("startGeopoint", startLocation);
        tripInformation.put("firstName", "Test");
        tripInformation.put("lastName", "User");
        tripInformation.put("phoneNumber", "17801234567");
        tripInformation.put("status", "available");
//        tripInformation.put("timestamp", );
        firebaseHandler.addNewRideRequestToDatabase(tripInformation, "bob@gmail.com");

//        Intent intent = new Intent(RiderMapActivity.this, RiderPaymentFragment.class);
//        startActivity(intent);
    }




}
