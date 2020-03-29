package com.example.bonvoyage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    public FirebaseHandler firebaseHandler;

    String TAG = "SignUpActivity";
    private CountryCodePicker ccp;
    private FirebaseAuth mAuth;
    private Button signUpConfirmButton;
    private EditText signUpFirstName, signUpLastName, signUpEmail, signUpPhoneNumber,
            signUpPassword, signUpRePassword;
    private CheckBox signUpUserIsDriver;
    private ProgressBar inProgress;
    private String userType = "riders";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sign_up);
        mAuth = FirebaseAuth.getInstance();
        firebaseHandler = new FirebaseHandler();
        inProgress = findViewById(R.id.progressBar);

        inProgress.setVisibility(View.INVISIBLE);

        signUpConfirmButton = findViewById(R.id.signUpConfirmButton);
        signUpConfirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                User user = setUpUser();
                createNewUserAccount(user);
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

    public void sendVerificationEmail(){
        mAuth.getCurrentUser()
                .sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Email sent.");
                        }else{
                            Log.d(TAG, "Failed to send email.");
                        }
                    }
                });
    }

    public User setUpUser(){

        final String newFirstName, newLastName, newEmail, newPhoneNumber, newPassword, newRePassword;
        final int newWallet;
        inProgress = findViewById(R.id.progressBar);
        inProgress.setVisibility(View.VISIBLE);

        // Find the objects that hold the user's inputted information
        ccp = (CountryCodePicker) findViewById(R.id.ccp_sign_up);
        signUpFirstName = findViewById(R.id.signUpFirstName);
        signUpLastName = findViewById(R.id.signUpLastName);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPhoneNumber = findViewById(R.id.signUpPhoneNumber);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpRePassword = findViewById(R.id.signUpRePassword);
        ccp.registerCarrierNumberEditText(signUpPhoneNumber);

        /* Transform the information into strings to be stored into the Cloud Firestore and
            the Authentication of Fire Base */
        newFirstName = signUpFirstName.getText().toString();
        newLastName = signUpLastName.getText().toString();
        newEmail = signUpEmail.getText().toString();
        newPhoneNumber = ccp.getFullNumberWithPlus();
        newPassword = signUpPassword.getText().toString();

        newRePassword = signUpRePassword.getText().toString();

        if (newPassword.equals(newRePassword)){
            newWallet = 100;
            if (userType.equals("rider")) {
                return new Rider(newFirstName, newLastName, newEmail, newPhoneNumber, newPassword, newWallet);
            } else {
                return new Driver(newFirstName, newLastName, newEmail, newPhoneNumber, newPassword, newWallet);
            }
        }else{
            Toast.makeText(SignUpActivity.this, "Passwords do not match.",
                    Toast.LENGTH_LONG).show();
            inProgress.setVisibility(View.INVISIBLE);
            return null;
        }

    }

    /**
     * This function processes the user's inputted information (sign-up information) and creates a
     * map such that it will be used by other functions.
     * It later calls addNewUserToDatabase(user, newEmail) and createNewUser(newEmail, newPassword)
     * to use the transformed data.
     */
    public void createNewUserAccount(User user){
        inProgress = findViewById(R.id.progressBar);
        if (user == null){
            inProgress.setVisibility(View.INVISIBLE);
            return;
        }
        if (user.getFirstname().isEmpty() || user.getLastname().isEmpty() || user.getEmail().isEmpty() ||
                user.getPhonenumber().isEmpty() || user.getPassword().isEmpty()){
            Toast.makeText(SignUpActivity.this, "All subject fields must be filled in.",
                    Toast.LENGTH_LONG).show();
            inProgress.setVisibility(View.INVISIBLE);
            return;
        }
        if (user.getPhonenumber().equals("")){
            Toast.makeText(SignUpActivity.this, "Invalid Phone Number.",
                    Toast.LENGTH_LONG).show();
            inProgress.setVisibility(View.INVISIBLE);
            return;
        }
        if (user.getPassword().length() < 6 && user.getPassword().length() > 0){
            Toast.makeText(SignUpActivity.this, "Password length must be greater than 6.",
                    Toast.LENGTH_LONG).show();
            inProgress.setVisibility(View.INVISIBLE);
            return;
        }

        // Create a new user with a first and last name, email, phone number, and password
        Map<String, Object> user_map = new HashMap<>();
        user_map.put("first_name", user.getFirstname());
        user_map.put("last_name", user.getLastname());
        user_map.put("email_address", user.getEmail());
        user_map.put("phone_number", user.getPhonenumber());
        user_map.put("password", user.getPassword());
        user_map.put("wallet", user.getWallet());

        // Adds it to the Authentication of Firebase
        firebaseHandler.createNewUserToDatabase(user.getEmail(), user.getPassword(), mAuth, SignUpActivity.this, this);
        // Adds it to the Cloud Firestore of Firebase
        firebaseHandler.addNewUserToDatabase(user_map, user.getEmail(), userType);
    }

    public void displayAuthToastMessage(Boolean success){
        inProgress = findViewById(R.id.progressBar);
        if (success == null){
            return;
        } else if (success == true){
            // If the user successfully created a new account, we send them a email verification
            sendVerificationEmail();
            Log.d(TAG, "createUserWithEmail:success");
            Toast.makeText(SignUpActivity.this, "Authentication Successsful. Check email for verification",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginSignupActivity.class);
            startActivity(intent);
            inProgress.setVisibility(View.INVISIBLE);
        } else if (success == false){
            // If the user fails to make a new account
            Log.d(TAG, "createUserWithEmail:failure");
            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
            inProgress.setVisibility(View.INVISIBLE);
        }
    }
}
