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
    private FirebaseAuth mAuth;
    String TAG = "SignUpActivity";
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
    public void goToLoginScreen(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void createNewUserAccount(){
        final String newFirstName, newLastName, newEmail, newPhoneNumber, newPassword;

        signUpFirstName = findViewById(R.id.signUpFirstName);
        signUpLastName = findViewById(R.id.signUpLastName);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPhoneNumber = findViewById(R.id.signUpPhoneNumber);
        signUpPassword = findViewById(R.id.signUpPassword);

        newFirstName = signUpFirstName.getText().toString();
        newLastName = signUpLastName.getText().toString();
        newEmail = signUpEmail.getText().toString();
        newPhoneNumber = signUpPhoneNumber.getText().toString();
        newPassword = signUpPassword.getText().toString();

        createNewUser(newEmail, newPassword); // Adds it to the auth

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name, email, phone number, and password
        Map<String, Object> user = new HashMap<>();
        user.put("first_name", newFirstName);
        user.put("last_name", newLastName);
        user.put("email_address", newEmail);
        user.put("phone_number", newPhoneNumber);
        user.put("password", newPassword);

        // Checks to see if the user chose to sign up as a driver or not
        db.collection(userType)
                .document(newEmail)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document added with ID: " + newEmail);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void createNewUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, updalay a mete UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
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
