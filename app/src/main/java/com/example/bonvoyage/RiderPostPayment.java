package com.example.bonvoyage;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class RiderPostPayment extends AppCompatActivity {
    private FirebaseHandler firebaseHandler;

    protected void onCreateView(Bundle savedInstanceState) {
        Rider rider = firebaseHandler.getCurrentUser();

        float cost = firebaseHandler.getCostOfRideFromDatabase(RiderPricingFragment.getRequestId());

        firebaseHandler.riderTransaction(rider, cost);

        startActivity(new Intent(RiderPostPayment.this, RiderRatingFragment.class));
    }

}
