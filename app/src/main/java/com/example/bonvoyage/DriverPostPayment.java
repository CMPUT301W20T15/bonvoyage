package com.example.bonvoyage;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DriverPostPayment extends AppCompatActivity {
    private FirebaseHandler firebaseHandler;
    float amount;

    protected void onCreateView(Bundle savedInstanceState) {

        Driver driver = firebaseHandler.getCurrentUser();

        float cost = firebaseHandler.getCostOfRideFromDatabase(RiderPricingFragment.getRequestId());

        firebaseHandler.driverTransaction(driver, cost);

        startActivity(new Intent(DriverPostPayment.this, DriverMapActivity.class));
    }

}
