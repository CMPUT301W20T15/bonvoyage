package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * SignInEmailActivity provides email sign in functionality for our app.
 * Once a user has used the email to create an account, they cannot create
 * another account with the same email. (Unique username implementation.
 * Source code: https://firebase.google.com/docs/auth/android/email-link-auth
 */
public class SignInEmailActivity extends AppCompatActivity {
    //Instance variables
    private static final String TAG = "SignInEmailActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmail, mPassword;
    private Button btnSignIn;
    private ProgressBar inProgress;
    private FirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sign_in);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        btnSignIn = (Button) findViewById(R.id.email_sign_in_button);
        inProgress = findViewById(R.id.progressBar);
        inProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        firebaseHandler = new FirebaseHandler();

        db = FirebaseFirestore.getInstance();

        /**
         * Checks whether the User is a rider or a driver, and directs them to their respective
         * home activities.
         */
        // TODO: SHOULD BE CHANGED IN LATER VERSION
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null){

                //TO CHECK IF THEY VERIFIED THEIR EMAIL
                //mAuth.getCurrentUser().isEmailVerified()
                if (true) {
                    // GO TO THE DRIVER HOME PAGE IS THEY ARE A DRIVER, LIKEWISE WITH A RIDER
                    Log.d(TAG,"onAuthStateChanged:signed_in"+user.getUid());
                    toastMessage("Successfully signed in with: "+user.getEmail());
                    DocumentReference docRef = db.collection("drivers").document(user.getEmail());
                    docRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // If user is a driver
                                Log.d(TAG, "Document exists!");
                                inProgress.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(this, DriverMapActivity.class);
                                startActivity(intent);
                            } else {
                                // If user is a rider
                                Log.d(TAG, "Document does not exist!");
                                inProgress.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(this, RiderMapActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    });
                }else{
                    // If the email is not verified
                    toastMessage("Please verify your email address.");
                    inProgress.setVisibility(View.INVISIBLE);
                }

            }else {
                Log.d(TAG,"onAuthStateChanged:signed_out");
                toastMessage("Successfully signed out");
            }
        };

        /**
         * If no email or password is entered (empty fields) prompts the user to fill out all fields
         * with a toast message.
         */
        btnSignIn.setOnClickListener(view -> {
            inProgress.setVisibility(View.VISIBLE);
            String email = mEmail.getText().toString();
            String pass = mPassword.getText().toString();
            // If the user entered a valid or invalid login information
            if (!email.equals("") && !pass.equals("")){
                firebaseHandler.loginUser(email, pass, SignInEmailActivity.this);
            }else {
                toastMessage("Fill in all fields");
                inProgress.setVisibility(View.INVISIBLE);
            }
        });
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
    /**
     * toastMessage generates a toast message.
     * @param message
     */
    public void toastMessage(String message){
        inProgress = findViewById(R.id.progressBar);
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        inProgress.setVisibility(View.INVISIBLE);
    }
}
