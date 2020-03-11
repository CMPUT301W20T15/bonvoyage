package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignInPhoneActivity extends AppCompatActivity {
    private static final String TAG = "SignInPhoneActivity";

    private EditText phoneEditText;
    private Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_phone);
        phoneEditText = findViewById(R.id.editTextPhone);
        submitBtn = (Button) findViewById(R.id.phoneContinue);

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
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
