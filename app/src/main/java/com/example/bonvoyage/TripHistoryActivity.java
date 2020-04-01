package com.example.bonvoyage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bonvoyage.models.RideRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TripHistoryActivity extends AppCompatActivity{
    private ListView tripHistory;
    private ArrayAdapter<DocumentSnapshot> tripHistoryArrayAdapter;
    private ArrayList<DocumentSnapshot> tripHistoryArrayList;
    private FirebaseFirestore db;
    private FirebaseUser user;
    String userType = "rider_email";
    String TAG = "TripHistoryActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_history_layout);

        tripHistory = findViewById(R.id.list_trips);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        tripHistoryArrayList = new ArrayList<>();

        DocumentReference userRef = db.collection("drivers").document(user.getEmail());
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // If user is a driver
                    userType = "driver_email";
                } else {
                    // If user is a rider
                    userType = "rider_email";
                }
            }
        });
        Log.d(TAG, userType + " " + user.getEmail());
        tripHistoryArrayList.clear();
        db.collection("CompletedRiderRequests")
                .whereEqualTo(userType, user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                tripHistoryArrayList.add(document);
                            }
                        }else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        tripHistoryArrayAdapter = new TripHistoryAdapter(TripHistoryActivity.this, tripHistoryArrayList);
        tripHistory.setAdapter(tripHistoryArrayAdapter);

        final CollectionReference collectionReference = db.collection("CompletedRiderRequests");
        collectionReference
                .whereEqualTo(userType, user.getEmail())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    tripHistoryArrayList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        tripHistoryArrayList.add(doc);
                    }
                    tripHistoryArrayAdapter.notifyDataSetChanged();
                }
            });
    }
}
