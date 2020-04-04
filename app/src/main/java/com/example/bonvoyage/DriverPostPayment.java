package com.example.bonvoyage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DriverPostPayment extends AppCompatActivity {
    private FirebaseHandler firebaseHandler;
    private FirebaseFirestore db;

    /**
     * Handles the call for the driver transaction and sends the driver back to the driver home page
     * @param savedInstanceState
     */
    protected void onCreateView(Bundle savedInstanceState) {
        FirebaseUser fb_driver = firebaseHandler.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("drivers").document(fb_driver.getEmail());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Driver driver = documentSnapshot.toObject(Driver.class);
                float cost = firebaseHandler.getCostOfRideFromDatabase(RiderPricingFragment.getRequestId());
                firebaseHandler.driverTransaction(driver, cost);
                toastMessage("Payment Processed!");
                startActivity(new Intent(DriverPostPayment.this, DriverMapActivity.class));
            }
        });
    }

    /**
     * toastMessage generates a toast message.
     * @param message
     */
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}