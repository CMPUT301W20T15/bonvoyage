package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SplashPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_splash_page);

//        getSupportActionBar().hide();
        RelativeLayout splash_screen = (RelativeLayout) findViewById(R.id.splash_page_layout);

        splash_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginPage(v);
            }
        });
    }

    public void goToLoginPage(View view){
        Intent intent = new Intent(this, LoginSignupActivity.class);
        startActivity(intent);
    }
}
