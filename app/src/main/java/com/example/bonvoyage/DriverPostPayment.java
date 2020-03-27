package com.example.bonvoyage;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DriverPostPayment extends AppCompatActivity {
    private FirebaseHandler firebaseHandler;
    private FirebaseFirestore db;

    protected void onCreateView(Bundle savedInstanceState) {
        FirebaseUser fb_driver = firebaseHandler.getCurrentUser();
        DocumentReference docRef = db.collection("drivers").document(fb_driver.getEmail());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Driver driver = documentSnapshot.toObject(Driver.class);
                float cost = firebaseHandler.getCostOfRideFromDatabase(RiderPricingFragment.getRequestId());
                firebaseHandler.driverTransaction(driver, cost);
                startActivity(new Intent(DriverPostPayment.this, DriverMapActivity.class));
            }
        });
    }
}
