package com.example.bonvoyage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * SMSActivity begins right after SignInPhoneActivity
 * Prompts the user to enter their SMS verification code to sucessfully sign into
 * their account.
 * Source code used: https://firebase.google.com/docs/auth/android/phone-auth.
 */
public class SMSActivity extends AppCompatActivity {
    //Instance variables
    private String smsID;
    private ProgressBar smsProgressBar;
    private EditText smsEditText;
    private Button submitSMSBtn;
    private static final String TAG = "SMSActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        smsEditText = (EditText) findViewById(R.id.editTextSMSCode);
        submitSMSBtn = (Button) findViewById(R.id.phoneSignIn);
        smsProgressBar = (ProgressBar) findViewById(R.id.smsProgressBar);
        String phone = getIntent().getStringExtra("phone");
        sendVerificationCode(phone);
        mAuth = FirebaseAuth.getInstance();

        submitSMSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String smsCode = smsEditText.getText().toString().trim();
                if (smsCode.isEmpty() || smsCode.length() < 6) {
                    toastMessage("Enter SMS code!");
                } else {
                    verifyCode(smsCode);
                }
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getPhoneNumber());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Sucessfully signed out");
                }
            }
        };
    }

    /**
     * verifyCode checks if the SMS code entered matches the one on their account.
     * @param smsCode
     */
    private void verifyCode(String smsCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(smsID, smsCode);
        signInWithCredential(credential);
    }

    /**
     * signInWithCredential facilitates Phone sign in, if the correct phone number and
     * verification code is entered the user will be signed in.
     * @param credential
     */
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            toastMessage("LOGIN WITH PHONE SUCESSFUL");
                        } else {
                            toastMessage(task.getException().getMessage());
                        }
                    }
                });
    }

    /**
     * sendVerifcationCode will send the SMS code to the user.
     * @param phone
     */
    private void sendVerificationCode(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                SMSActivity.this,
                mCallBack);
    }

    /**
     * PhoneAuthProvider from Google's Firebase to verify the phone number
     * and verifcation code.
     * Source:
     */
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            smsID = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                smsProgressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            toastMessage(e.getMessage() + "VERIFICATION FAILED");
        }
    };
    /**
     * toastMessage generates a toast message.
     * @param message
     */
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
