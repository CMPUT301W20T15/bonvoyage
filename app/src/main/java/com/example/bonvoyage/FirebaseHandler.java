package com.example.bonvoyage;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bonvoyage.models.RideRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected  FirebaseHandler(){
    }

    public static FirebaseHandler getInstance(){
        if (instance == null){
            instance = new  FirebaseHandler();
        }
        return instance;
    }


    public void loginUser(String email, String password, SignInEmailActivity activity) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        activity.toastMessage("Authentication failed.");
                    }
                });
    }

    public FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Boolean checkIfUserIsDriver(String email){
        final Boolean[] driver = {false};
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("drivers").document(email);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "Document exists!");
                    driver[0] = true;
                } else {
                    Log.d(TAG, "Document does not exist!");
                    driver[0] = false;
                }
            } else {
                Log.d(TAG, "Failed with: ", task.getException());
            }
        });
        return driver[0];
    }

    public ArrayList<RideRequest> getAvailableRiderRequest(){
        final ArrayList<RideRequest> riderRequestList = new ArrayList<>();
        ListenerRegistration riderRequestRefListener;
        db = FirebaseFirestore.getInstance();

        CollectionReference riderRequestRef = db.collection("RiderRequests");
        riderRequestRefListener = riderRequestRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.e(TAG,  "onEventRideRequests: list failed");
                    return;
                }
                if (queryDocumentSnapshots != null){
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        RideRequest rider_detail = doc.toObject(RideRequest.class);
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
        db.collection("RiderRequests")
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