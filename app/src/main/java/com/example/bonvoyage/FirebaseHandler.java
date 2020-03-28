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
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class handles the firebase cloud service that is updated live with the applciation
 */
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
    public FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * If the user exists, the user is logged on to the app
     * @param email     the user's email
     * @param password  the user's password
     * @param activity  the activity to display a toast
     */

    public void loginUser(String email, String password, SignInEmailActivity activity) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            activity.toastMessage("Authentication failed.");
                        }
                    }
                });
    }

    /**
     * Communicates with the firestore to make a transaction between rider and driver
     * @param rider         a rider that will pay the rider
     * @param driver        a driver that will get paid
     * @param payment_fee   the amount that the rider agreed to pay
     */
    public void userTransaction(Rider rider, Driver driver, float payment_fee){
        // Adding money to the driver's wallet
        DocumentReference driverRef = db.collection("drivers").document(driver.getEmail());
        driver.addMoneyToWallet(payment_fee);
        driverRef
                .update("wallet", driver.getWallet())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Updated driver wallet!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Failed updating driver wallet", e);
                    }
                });

        // Removing money from the rider's wallet
        DocumentReference riderRef = db.collection("riders").document(rider.getEmail());
        rider.takeMoneyFromWallet(payment_fee);
        riderRef
                .update("wallet", rider.getWallet())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Updated rider wallet!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Failed updating rider wallet", e);
                    }
                });

    }

    /**
     * TODO needs to be worked on for offline interaction
     */
    public void getOfflineRideRequest(){
        db = FirebaseFirestore.getInstance();
        db.collection("RiderRequests")
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen error", e);
                            return;
                        }
                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {
                                Log.d(TAG, "New ride request:" + change.getDocument().getData());
                            }
                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });
    }

    /**
     * Checks if the user is a driver or not a driver
     * @param email     email of the user
     * @return          true if they are a driver, false if they are not a driver
     */
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

    /**
     * Gets the available riderrequests
     * @return      the list of available rider requests
     */
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
    /**
     * Adds a new rider request to the firestore database in realtime
     * @param request_details   the details (location, rider, fee, etc.) of the ride request
     * @param unique_id         the rider's email
     */
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

    /**
     * Adds a new user to the database (in sync with authentication)
     * @param user          a map of the user's information
     * @param unique_id     the user's email
     * @param userType      the type of user they are (driver or rider)
     */
    public void addNewUserToDatabase(Map user, final String unique_id, String userType){
        Log.d(TAG, "THIS");

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

    /**
     * Creates a new user in the authentication of the data base
     * @param email
     * @param password
     * @param mAuth
     * @param view
     * @param signUpActivity
     */
    public void createNewUserToDatabase(String email, String password, FirebaseAuth mAuth,
                                        SignUpActivity view, final SignUpActivity signUpActivity) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, updalay a mete UI with the signed-in user's information
                            signUpActivity.displayAuthToastMessage(true);
                        } else {
                            // If sign in fails, dispssage to the user.
                            signUpActivity.displayAuthToastMessage(false);
                        }
                    }
                });
    }
}