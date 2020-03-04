package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void signInClicked(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
    public void signUpClicked(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }
}
