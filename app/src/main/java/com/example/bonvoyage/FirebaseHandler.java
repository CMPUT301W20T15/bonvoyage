package com.example.bonvoyage;

import android.util.Log;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;

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

    public DocumentReference getDriverRef(String id){
        db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("drivers").document(id);
        return ref;
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
                            db = FirebaseFirestore.getInstance();
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
     * @param driver        a driver that will get paid
     * @param payment_fee   the amount that the rider agreed to pay
     */
    public void driverTransaction(Driver driver, float payment_fee){
        // Adding money to the driver's wallet
        db = FirebaseFirestore.getInstance();
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

    }

    /**
     * Communicates with the firestore to make a transaction between rider and driver
     * @param rider         a rider that will pay the rider
     * @param payment_fee   the amount that the rider agreed to pay
     */
    public void riderTransaction(Rider rider, float payment_fee){
        // Removing money from the rider's wallet
        db = FirebaseFirestore.getInstance();
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

    public void deleteRideRequest(final String unique_id){
        db = FirebaseFirestore.getInstance();
        db.collection("RiderRequests").document(unique_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Document  with ID: " + unique_id);
            }
        });
    }

    /**
     * Gets the cost of ride
     * @param unique_id     the unique id of the ride in question
     * @return              the cost of the ride in question
     */
    public float getCostOfRideFromDatabase(final String unique_id) {
        db = FirebaseFirestore.getInstance();
        final float[] cost = {0};
        DocumentReference docRef = db.collection("RiderRequests")
                .document(unique_id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.e(TAG,  "Unable to find cost of ride");
                    return;
                }
                if (documentSnapshot != null){
                    cost[0] = documentSnapshot.toObject(RideRequest.class).getCost();
                }
            }
        });
        return cost[0];
    }

    public float getRiderWallet(final String id){
        db = FirebaseFirestore.getInstance();
        final float[] cost = {0};
        DocumentReference docRef = db.collection("riders")
                .document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.e(TAG,  "Unable to find cost of ride");
                    return;
                }
                if (documentSnapshot != null){
                    String wallet = documentSnapshot.getString("wallet");
                    cost[0] = Float.parseFloat(wallet);
                }
            }
        });
        return cost[0];
    }

    public void updateRiderWallet(final String id, float newWallet){
        db = FirebaseFirestore.getInstance();
        db.collection("riders").document(id).update("wallet", newWallet);
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


}