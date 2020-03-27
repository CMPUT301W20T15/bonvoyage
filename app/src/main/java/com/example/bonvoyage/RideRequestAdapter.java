package com.example.bonvoyage;

import android.content.Context;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RideRequestAdapter extends ArrayAdapter<RideRequest> {
    final String TAG = "RideRequestAdapter";
    private ArrayList<RideRequest> rideRequestArrayList;
    private Context context;
    private FirebaseFirestore db;
    private RequestListener requestListener;
    FirebaseHandler firebaseHandler = new FirebaseHandler();
    public RideRequestAdapter(Context context, ArrayList<RideRequest> rideRequestArrayList, RequestListener listener){
        super(context,0, rideRequestArrayList);
        this.rideRequestArrayList = rideRequestArrayList;
        this.context = context;
        this.requestListener = listener;
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
                db = FirebaseFirestore.getInstance();
                FirebaseUser driver = firebaseHandler.getCurrentUser();
                Toast.makeText(getContext(), driver.getEmail(), Toast.LENGTH_LONG).show();
                DocumentReference driverRef = db.collection("drivers").document(driver.getEmail());


                HashMap<String, Object> tripInformation = new HashMap<>();
                driverRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            if(documentSnapshot.getString("first_name") != null){
                                Log.d(TAG, documentSnapshot.getString("first_name"));
                            }
                            tripInformation.put("driver_name", documentSnapshot.getString("first_name") + documentSnapshot.getString("last_name"));
                            tripInformation.put("phone_number", documentSnapshot.getString("phone_number"));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                });


                tripInformation.put("rider_email", rideRequest.getUserEmail());
                tripInformation.put("rider_name", rideRequest.getFullName());
                tripInformation.put("driver_email", driver.getEmail());
                tripInformation.put("startGeopoint", rideRequest.getStartGeopoint());
                GeoPoint x = new GeoPoint(45,100 );
                tripInformation.put("endGeopoint",x);
                tripInformation.put("timestamp", rideRequest.getTimestamp());
                tripInformation.put("cost", rideRequest.getCost());
                tripInformation.put("status", "accepted");
                db = FirebaseFirestore.getInstance();
                db.collection("RiderRequest").document(rideRequest.getUserEmail()).set(tripInformation);
                Bundle rideInfo = new Bundle();
                rideInfo.putSerializable("HashMap", tripInformation);
                requestListener.onRequestAccepted(rideInfo);


                Log.d("TEST ACCEPT", "WORKING");
            }
        });
        return view;
    }


    public interface RequestListener{
        void onRequestAccepted(Bundle request_info);
    }


}
