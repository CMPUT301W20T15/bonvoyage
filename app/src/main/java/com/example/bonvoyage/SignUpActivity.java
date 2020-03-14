package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    public FirebaseHandler firebaseHandler;

    String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private Button signUpConfirmButton;
    private EditText signUpFirstName, signUpLastName, signUpEmail, signUpPhoneNumber, signUpPassword;
    private CheckBox signUpUserIsDriver;
    private String userType = "riders";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sign_up);
        mAuth = FirebaseAuth.getInstance();

        User model = setUpUser();
        SignUpActivity view = new SignUpActivity();
        firebaseHandler = new FirebaseHandler();

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
    public User setUpUser(){
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
        if (userType.equals("rider")) {
            return new Rider(newFirstName, newLastName, newEmail, newPhoneNumber, newPassword);
        } else {
            return new Driver(newFirstName, newLastName, newEmail, newPhoneNumber, newPassword);
        }

    }

    /**
     * When the go button is pressed, it will change the existing activity into the login screen
     * activity
     * @param view  the current view of the activity
     */
    public void goToLoginScreen(View view) {
        Intent intent = new Intent(this, LoginSignupActivity.class);
        startActivity(intent);
    }

    /**
     * This function processes the user's inputted information (sign-up information) and creates a
     * map such that it will be used by other functions.
     * It later calls addNewUserToDatabase(user, newEmail) and createNewUser(newEmail, newPassword)
     * to use the transformed data.
     */
    public void createNewUserAccount(User user){
        if (user == null){
            return;
        }
        if (user.getFirstname().isEmpty() || user.getLastname().isEmpty() || user.getEmail().isEmpty() ||
                user.getPhonenumber().isEmpty() || user.getPassword().isEmpty()){
            Toast.makeText(SignUpActivity.this, "All subject fields must be filled in.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (user.getPhonenumber().length() > 11 || user.getPhonenumber().length() < 9){
            Toast.makeText(SignUpActivity.this, "Invalid Phone Number.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (user.getPassword().length() < 6 && user.getPassword().length() > 0){
            Toast.makeText(SignUpActivity.this, "Password length must be greater than 6.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // Create a new user with a first and last name, email, phone number, and password
        Map<String, Object> user_map = new HashMap<>();
        user_map.put("first_name", user.getFirstname());
        user_map.put("last_name", user.getLastname());
        user_map.put("email_address", user.getEmail());
        user_map.put("phone_number", user.getPhonenumber());
        user_map.put("password", user.getPassword());

        firebaseHandler.createNewUserToDatabase(user.getEmail(), user.getPassword(), mAuth, SignUpActivity.this, this); // Adds it to the Authentication of Firebase
        firebaseHandler.addNewUserToDatabase(user_map, user.getEmail(), userType); // Adds it to the Cloud Firestore of Firebase
    }

    public void displayToastMessage(Boolean success){
        if (success == null){
            return;
        } else if (success == true){
            Log.d(TAG, "createUserWithEmail:success");
            Toast.makeText(SignUpActivity.this, "Authentication Successsful.",
                    Toast.LENGTH_SHORT).show();
        } else if (success == false){
            Log.d(TAG, "createUserWithEmail:failure");
            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
