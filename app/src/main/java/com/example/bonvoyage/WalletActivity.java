package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WalletActivity extends AppCompatActivity {
    FirebaseUser user;
    FirebaseFirestore db;
    String userType = "riders";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_activity);
        TextView money_val = findViewById(R.id.wallet_val);
        Toolbar toolbar = findViewById(R.id.toolbar);
        new DrawerWrapper(this,this.getApplicationContext(),toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();


        DocumentReference userRef = db.collection("drivers").document(user.getEmail());
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // If user is a driver
                    userType = "drivers";
                } else {
                    // If user is a rider
                    userType = "riders";
                }
                if (user != null){
                    DocumentReference docRef = db.collection(userType).document(user.getEmail());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            //DocumentSnapShot is the object that holds the fields of data
                            float money = documentSnapshot.getLong("wallet");
                            money_val.setText("$"+ String.valueOf(money));
                        }
                    });
                }else{
                    Toast.makeText(WalletActivity.this,"User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}