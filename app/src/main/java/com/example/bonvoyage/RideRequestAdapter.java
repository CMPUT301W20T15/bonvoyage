package com.example.bonvoyage;

import android.content.Context;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bonvoyage.models.RideRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RideRequestAdapter extends ArrayAdapter<RideRequest> {
    final String TAG = "RideRequestAdapter";
    private ArrayList<RideRequest> rideRequestArrayList;
    private Context context;
    private FirebaseFirestore db;
    private RequestListener requestListener;
    private FirebaseHandler firebaseHandler = new FirebaseHandler();

    public RideRequestAdapter(Context context, ArrayList<RideRequest> rideRequestArrayList, RequestListener listener){
        super(context,0, rideRequestArrayList);
        this.rideRequestArrayList = rideRequestArrayList;
        this.context = context;
        this.requestListener = listener;
        this.db = FirebaseFirestore.getInstance();
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.rider_list_item,parent,false);
        }
        final RideRequest rideRequest = rideRequestArrayList.get(position);
        TextView name = view.findViewById(R.id.contact_name);
        TextView driverCost = view.findViewById(R.id.drive_cost);
        TextView userEmail = view.findViewById(R.id.rider_email);
        TextView userPhone = view.findViewById(R.id.rider_phone);
        Button acceptBtn = (Button)view.findViewById(R.id.driver_accept);

        String riderName = rideRequest.getFirstName() + " " + rideRequest.getLastName();
        String email = rideRequest.getUserEmail();
        String phone = rideRequest.getPhoneNumber();
        float rideCost = rideRequest.getCost();
        name.setText(riderName);
        driverCost.setText(Float.toString(rideCost));
        userEmail.setText(email);
        userPhone.setText(phone);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDriverToRequest(rideRequest);
            }
        });

        return view;
    }


    public void addDriverToRequest(RideRequest rideRequest){
        String driverEmail = firebaseHandler.getCurrentUser().getEmail();
        DocumentReference ref = firebaseHandler.getDriverRef(driverEmail);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d("*****DOC*****", "document snapshot data: " + document.getData());
                        HashMap<String, Object> tripInformation = new HashMap<>();
                        tripInformation.put("rider_email", rideRequest.getUserEmail());
                        tripInformation.put("rider_name", rideRequest.getFirstName() + " " + rideRequest.getLastName());
                        tripInformation.put("driver_email", document.getString("email_address"));
                        tripInformation.put("driver_name", document.getString("first_name") + document.getString("last_name"));
                        tripInformation.put("startGeopoint", rideRequest.getStartGeopoint());
                        tripInformation.put("endGeopoint",rideRequest.getEndGeopoint());
                        tripInformation.put("timestamp", rideRequest.getTimestamp());
                        tripInformation.put("cost", rideRequest.getCost());
                        tripInformation.put("status", "accepted");
                        String riderEmail = Objects.requireNonNull(tripInformation.get("rider_email")).toString();
                        db = FirebaseFirestore.getInstance();
                        db.collection("RiderRequests").document(riderEmail).set(tripInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Bundle rideInfo = new Bundle();
                                rideInfo.putSerializable("HashMap", tripInformation);
                                requestListener.onRequestAccepted(rideInfo);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        });

                    }
                }else{
                    Log.d("*****DOC*****", task.getException().getMessage());
                }
            }
        });
    }


    public interface RequestListener{
        void onRequestAccepted(Bundle request_info);
    }


}
