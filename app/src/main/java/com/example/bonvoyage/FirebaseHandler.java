package com.example.bonvoyage;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseHandler {
    private static FirebaseHandler instance = null;
    private String TAG = "Firebase";
    private static FirebaseFirestore db;

    public static FirebaseHandler getInstance(){
        if (instance == null){
            instance = new  FirebaseHandler();
        }
        return instance;
    }

    public ArrayList<RiderLocation> getAvailableRiderRequest(){
        final ArrayList<RiderLocation> riderRequestList = new ArrayList<>();
        ListenerRegistration riderRequestRefListener;

        CollectionReference riderRequestRef = db.collection("Rider Location");
        riderRequestRefListener = riderRequestRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.e(TAG,  "onEventRiderLocations: list failed");
                    return;
                }
                if (queryDocumentSnapshots != null){
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        RiderLocation rider_detail = doc.toObject(RiderLocation.class);
                        if (rider_detail.getStatus() == "available")
                        {
                            riderRequestList.add(rider_detail);
                        }
                    }
                }
            }
        });

        return riderRequestList;
    }

    public void addNewRideRequestToDatabase(Map request_details, final String unique_id){
        db = FirebaseFirestore.getInstance();
        db.collection("Rider Location")
                .document(unique_id)
                .set(request_details)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document added with ID: " + unique_id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void addNewUserToDatabase(Map user, final String unique_id, String userType){
        db = FirebaseFirestore.getInstance();
        db.collection(userType)
                .document(unique_id)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document added with ID: " + unique_id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    public void createNewUserToDatabase(String email, String password, FirebaseAuth mAuth,
                                        SignUpActivity view, final SignUpActivity signUpActivity) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, updalay a mete UI with the signed-in user's information
                            signUpActivity.displayToastMessage(true);
                        } else {
                            // If sign in fails, dispssage to the user.
                            signUpActivity.displayToastMessage(false);
                        }
                    }
                });
    }
}
