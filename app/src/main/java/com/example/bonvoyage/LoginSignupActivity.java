package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginSignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        ImageView bvlogo = findViewById(R.id.bvlogo);
        ImageView bgApp = findViewById(R.id.bgApp2);
        bgApp.animate().translationY(-1000).setDuration(1300).setStartDelay(2000);
        ImageView signInEmail =findViewById(R.id.signInEmailButton);
        ImageView SignInPhone = findViewById(R.id.signInPhoneButton);
        ImageView SignUp = findViewById(R.id.signUpButton);
        bvlogo.animate()
                .translationY(420)
                .translationX(400)
                .alpha(1.0f)
                .setDuration(2000)
                .setStartDelay(3000);
        signInEmail.animate()
                .translationY(-600)
                .alpha(1.0f)
                .setDuration(1000)
                .setStartDelay(4000);
        SignInPhone.animate()
                .translationY(-400)
                .alpha(1.0f)
                .setDuration(1000)
                .setStartDelay(4000);
        SignUp.animate()
                .translationY(-200)
                .alpha(1.0f)
                .setDuration(1000)
                .setStartDelay(4000);


    }
    public void signInEmailClicked(View view) {
        Intent intent = new Intent(this, SignInEmailActivity.class);
        startActivity(intent);
    }
    public void signInPhoneClicked(View view) {
        Intent intent = new Intent(this, SignInPhoneActivity.class);
        startActivity(intent);
    }
    public void signUpClicked(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }
}
