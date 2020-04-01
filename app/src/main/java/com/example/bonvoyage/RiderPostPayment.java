package com.example.bonvoyage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RiderPostPayment extends AppCompatActivity {
    private FirebaseHandler firebaseHandler;
    private FirebaseFirestore db;
    private static final String TAG = "RiderPostPayment";

    /**
     * Handles the call for rider transaction and send's the rider to rating fragment
     * @param savedInstanceState
     */
    protected void onCreateView(Bundle savedInstanceState) {
        FirebaseUser fb_rider = firebaseHandler.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("riders").document(fb_rider.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Rider rider = documentSnapshot.toObject(Rider.class);
                if (rider == null) {
                    Log.e(TAG,  "Failed to get rider");
                } else {
                    float cost = firebaseHandler.getCostOfRideFromDatabase(rider.getEmail());
                    firebaseHandler.riderTransaction(rider, cost);
                    startActivity(new Intent(RiderPostPayment.this, RiderRatingFragment.class));
                }

            }
        });
    }

}