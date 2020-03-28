package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;

public class ChangeUserProfile extends AppCompatActivity {

    ToggleButton tb;
    EditText change_email;
    EditText change_name;
    EditText change_number;
    private FirebaseHandler firebaseHandler;
    private  FirebaseFirestore db;
    private  FirebaseAuth mAuth;


    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userType = "riders";
    private FirebaseUser user;
    String TAG = "UserProfile";

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


        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (user != null){

            DocumentReference docRef = db.collection("riders").document(user.getEmail());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //DocumentSnapShot is the object that holds the fields of data
                    String first_name = documentSnapshot.getString("first_name");
                    String last_name = documentSnapshot.getString("last_name");
                    String email = documentSnapshot.getString("email_address");
                    String phone = documentSnapshot.getString("phone_number");
                    change_email.setText(email);
                    change_name.setText(String.format(first_name + " "+last_name));
                    change_number.setText(phone);
                    change_email.setFocusable(false);
                    change_name.setFocusable(false);
                    change_number.setFocusable(false);

                }
            });
        }else{
            Toast.makeText(ChangeUserProfile.this,"User does not exist", Toast.LENGTH_SHORT).show();
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
                    change_email.setFocusableInTouchMode(true);
                    change_name.setFocusableInTouchMode(true);
                    change_number.setFocusableInTouchMode(true);
                } else {
                    change_email.setFocusable(false);
                    change_name.setFocusable(false);
                    change_number.setFocusable(false);
                    String email = change_email.getText().toString();
                    String name = change_name.getText().toString();
                    String number = change_number.getText().toString();
                    String first_name = name.substring(0, name.indexOf(" "));
                    String last_name = name.substring(name.indexOf(" ")+1);
                    DocumentReference docRef = db.collection("riders").document(user.getEmail());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            //implement
                        }
                    });
                }
            }
        });

    }
}
