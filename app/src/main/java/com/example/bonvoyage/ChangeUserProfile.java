package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangeUserProfile extends AppCompatActivity {

    ToggleButton tb;
    EditText change_email;
    EditText change_name;
    EditText change_number;
    private FirebaseHandler firebaseHandler;
    private  FirebaseFirestore db;
    private  FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        new DrawerWrapper(this,this.getApplicationContext(),toolbar);

        Intent intent = getIntent();
        change_email = findViewById(R.id.change_email);
        change_name = findViewById(R.id.change_name);
        change_number = findViewById(R.id.change_phone);
        tb = findViewById(R.id.change_button);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser!=null){
            Toast.makeText(getApplicationContext(),"not null", Toast.LENGTH_SHORT).show();
            String name = firebaseUser.getDisplayName().toString();
            Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show();

        }
        else {
            Toast.makeText(getApplicationContext(), "probably null", Toast.LENGTH_SHORT).show();
        }
//        boolean answer = firebaseHandler.checkIfUserIsDriver(firebaseUser.getDisplayName());
//        String type_user;
//        String fetch_email, fetch_name, fetch_number;
//        DocumentReference docRef;
//        if(answer){
//            type_user ="drivers";
//        }
//        else
//        {
//            type_user = "riders";
//        }
//
//        docRef = db.collection(type_user).document(firebaseUser.getEmail());
//        fetch_email = docRef.toString();
//        docRef = db.collection(type_user).document(firebaseUser.getPhoneNumber());
//        fetch_number = docRef.toString();
//        docRef = db.collection(type_user).document(firebaseUser.getDisplayName());
//        fetch_name = docRef.toString();
//
//        change_email.setText(fetch_email);
//        change_name.setText(fetch_name);
//        change_number.setText(fetch_number);
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(),"Edit", Toast.LENGTH_SHORT).show();
                    //get values
                    // The toggle is enabled
                } else {
                    Toast.makeText(getApplicationContext(),"Save", Toast.LENGTH_SHORT).show();
                    // The toggle is disabled
                }
            }
        });

    }
}
