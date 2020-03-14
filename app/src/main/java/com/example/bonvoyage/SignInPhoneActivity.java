package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * SignInPhoneActivity implements phone number authentication sign in.
 * Source code used: https://firebase.google.com/docs/auth/android/phone-auth.
 */
public class SignInPhoneActivity extends AppCompatActivity {
    //Instance variables
    private static final String TAG = "SignInPhoneActivity";
    private EditText phoneEditText;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_phone);
        phoneEditText = findViewById(R.id.editTextPhone);
        submitBtn = (Button) findViewById(R.id.phoneContinue);

        /**
         * If the phone number submitted is invalid/empty, then a toast will appear to
         * prompt the user to enter a valid phone number.
         * Once a correct phoneNumber is implemented, it redirects to a SMSActivity where
         * the user is prompted to enter their SMS verification code.
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

    /**
     * toastMessage generates a toast message.
     * @param message
     */
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
