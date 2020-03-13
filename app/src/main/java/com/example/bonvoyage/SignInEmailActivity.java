package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInEmailActivity extends AppCompatActivity {
    private static final String TAG = "SignInEmailActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText mEmail, mPassword;
    private Button btnSignIn, btnSignOut;
    private Button backToLoginScreen;

    private FirebaseHandler firebaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sign_in);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        btnSignIn = (Button) findViewById(R.id.email_sign_in_button);
        //btnSignOut = (Button) findViewById(R.id.email_sign_out_button);

        // FOR TESTINGS
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        firebaseHandler = new FirebaseHandler();

        // SHOULD BE CHANGED
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null){
                Log.d(TAG,"onAuthStateChanged:signed_in"+user.getUid());
                toastMessage("Successfully signed in with: "+user.getEmail());
                // GO TO THE DRIVER HOME PAGE IS THEY ARE A DRIVER, LIKEWISE WITH A RIDER
                if (firebaseHandler.checkIfUserIsDriver(user.getEmail())){
                    Intent intent = new Intent(this, DriverMapActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(this, RiderMapActivity.class);
                    startActivity(intent);
                }

            }else {
                Log.d(TAG,"onAuthStateChanged:signed_out");
                toastMessage("Successfully signed out");
            }
        };

        btnSignIn.setOnClickListener(view -> {
            String email = mEmail.getText().toString();
            String pass = mPassword.getText().toString();
            if (!email.equals("") && !pass.equals("")){
                firebaseHandler.loginUser(email, pass, SignInEmailActivity.this);
            }else {
                toastMessage("Fill in all fields");
            }
        });

        /* FOR SIGNING OUT
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                toastMessage("Signing out...");
            }
        });
        */

        /* FOR GOING BACK TO LOGIN SCREEN
        backToLoginScreen = findViewById(R.id.BackButton);
        backToLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginScreen(v);
            }
        });
        */
    }

    public void goToLoginScreen(View view){
        Intent intent = new Intent(this, LoginSignupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
