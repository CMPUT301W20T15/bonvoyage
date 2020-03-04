package com.example.bonvoyage;

import androidx.annotation.NonNull;
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

public class SignInPhoneActivity extends AppCompatActivity {
    private static final String TAG = "SignInPhoneActivity";
    //private FirebaseAuth mAuth;
    //private  FirebaseAuth.AuthStateListener mAuthListener;

    private EditText phoneEditText;
    private Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_phone);
        phoneEditText = findViewById(R.id.editTextPhone);
        submitBtn = (Button) findViewById(R.id.phoneContinue);
        /*
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d(TAG,"onAuthStateChanged:signed_in"+user.getUid());
                    toastMessage("Successfully signed in with: "+user.getEmail());
                }else {
                    Log.d(TAG,"onAuthStateChanged:signed_out");
                    toastMessage("Sucessfully signed out");
                }
            }
        };
        */

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phoneEditText.getText().toString();
                if (phone.isEmpty() || phone.length() < 11){
                    toastMessage("Valid Number is Required");
                }else{
                    String completePhone = "+" + phone;
                    Intent intent = new Intent(SignInPhoneActivity.this,SMSActivity.class);
                    intent.putExtra("phone",completePhone);
                    startActivity(intent);
                }
            }
        });
    }
    /*
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
    */
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
