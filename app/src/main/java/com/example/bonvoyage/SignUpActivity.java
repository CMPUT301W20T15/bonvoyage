package com.example.bonvoyage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private Button signUpConfirmButton, signUpBackButton;
    private EditText signUpFirstName, signUpLastName, signUpEmail, signUpPhoneNumber, signUpPassword;
    private CheckBox signUpUserIsDriver;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        signUpConfirmButton = findViewById(R.id.signUpConfirmButton);
        signUpConfirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                createNewUserAccount();
            }
        });

        signUpBackButton = findViewById(R.id.signUpBackButton);
        signUpBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginScreen(v);
            }
        });

        signUpUserIsDriver = findViewById(R.id.signUpUserIsDriver);
        signUpUserIsDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()){
                    userType = "drivers";
                }else {
                    userType = "riders";
                }
            }
        });
    }

    /**
     * When the go button is pressed, it will change the existing activity into the login screen
     * activity
     * @param view  the current view of the activity
     */
    public void goToLoginScreen(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * This function processes the user's inputted information (sign-up information) and creates a
     * map such that it will be used by other functions.
     * It later calls addNewUserToDatabase(user, newEmail) and createNewUser(newEmail, newPassword)
     * to use the transformed data.
     */
    public void createNewUserAccount(){
        final String newFirstName, newLastName, newEmail, newPhoneNumber, newPassword;

        // Find the objects that hold the user's inputted information
        signUpFirstName = findViewById(R.id.signUpFirstName);
        signUpLastName = findViewById(R.id.signUpLastName);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPhoneNumber = findViewById(R.id.signUpPhoneNumber);
        signUpPassword = findViewById(R.id.signUpPassword);

        /* Transform the information into strings to be stored into the Cloud Firestore and
            the Authentication of Fire Base */
        newFirstName = signUpFirstName.getText().toString();
        newLastName = signUpLastName.getText().toString();
        newEmail = signUpEmail.getText().toString();
        newPhoneNumber = signUpPhoneNumber.getText().toString();
        newPassword = signUpPassword.getText().toString();

        if (newFirstName.isEmpty() || newLastName.isEmpty() || newEmail.isEmpty() ||
                newPhoneNumber.isEmpty() || newPassword.isEmpty()){
            Toast.makeText(SignUpActivity.this, "All subject fields must be filled in.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (newPhoneNumber.length() < 11){
            Toast.makeText(SignUpActivity.this, "Invalid Phone Number (###) ###-####.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a new user with a first and last name, email, phone number, and password
        Map<String, Object> user = new HashMap<>();
        user.put("first_name", newFirstName);
        user.put("last_name", newLastName);
        user.put("email_address", newEmail);
        user.put("phone_number", newPhoneNumber);
        user.put("password", newPassword);

        createNewUser(newEmail, newPassword); // Adds it to the Authentication of Firebase
        addNewUserToDatabase(user, newEmail); // Adds it to the Cloud Firestore of Firebase
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
    public void addNewUserToDatabase(Map user, final String unique_id){
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
    public void createNewUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, updalay a mete UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignUpActivity.this, "Authentication Successsful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                            // If sign in fails, dispssage to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
