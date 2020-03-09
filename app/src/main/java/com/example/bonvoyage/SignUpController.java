package com.example.bonvoyage;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


public class SignUpController {
    private String TAG = "SignUp";

    private User model;
    private SignUpActivity view;
    private Boolean successful;

    public SignUpController(User model, SignUpActivity view){
        this.model = model;
        this.view = view;
    }

    /**
     * This function takes in the map (a collection of the user's information) and a unique_id
     * (the user's email address). Then attempts to crate a new document of the user and stores
     * it onto the firebase database. If the user selects to sign up as a driver then their information
     * will be put into the driver collection of the cloud firestore. Likewise, if they sign-up as a
     * rider then their information will be put into the driver collection of the cloud firestore.
     * @param user      a map of the user's information containing:
     *                  first_name
     *                  last_name
     *                  email_address
     *                  phone_number
     *                  password
     * @param unique_id the unique document reference to be stored into the database (the user's email)
     */
    public void addNewUserToDatabase(Map user, final String unique_id, String userType){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
     * This function takes in the user's email and password and attempts to sign them up onto the
     * Authentication side of the Firebase. If it is successful, then the user will be able to
     * sign-in with their correct email and password. If it is unsuccessful, an exception will be
     * thrown.
     * @param email     a unique email of the user
     * @param password  a password of the user
     */
    public void createNewUser(String email, String password, FirebaseAuth mAuth) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, updalay a mete UI with the signed-in user's information
                            successful = true;
                        } else {
                            // If sign in fails, dispssage to the user.
                            successful = false;
                        }
                    }
                });
    }
    public void updateView(){
        view.displayToastMessage(successful);
    }
}
