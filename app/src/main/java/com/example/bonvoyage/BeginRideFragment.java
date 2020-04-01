package com.example.bonvoyage;

import android.app.UiAutomation;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BeginRideFragment extends Fragment {

    private final String TAG = "BeginRideFragment";
    private FirebaseFirestore db;
    private Bundle bundle;
    private HashMap requestInfo;
    private DriverStatusListener beginRideListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        beginRideListener = (DriverStatusListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.driver_begin_ride, container, false);
        TextView rider_name = view.findViewById(R.id.beginRide_riderName);
        TextView location = view.findViewById(R.id.beginRide_location);
        TextView destination = view.findViewById(R.id.beginRide_destination);
        TextView cost = view.findViewById(R.id.beginRide_payment);
        Button arriveBtn = view.findViewById(R.id.beginRide_arriveBtn);
        Button beginBtn = view.findViewById(R.id.beginRide_beginRideBtn);
        beginBtn.setVisibility(View.GONE);
        Button cancelBtn = view.findViewById(R.id.beginRide_cancelRideBtn);
        db = FirebaseFirestore.getInstance();
        bundle = getArguments();
        requestInfo = (HashMap) bundle.getSerializable("HashMap");
        DocumentReference requestRef = db.collection("RiderRequests").document(requestInfo.get("rider_email").toString());

        rider_name.setText(requestInfo.get("rider_name").toString());
        cost.setText(requestInfo.get("cost").toString());

        Geocoder geo = new Geocoder(BeginRideFragment.this.getContext());
        GeoPoint start = (GeoPoint) requestInfo.get("startGeopoint");
        GeoPoint end = (GeoPoint) requestInfo.get("endGeopoint");

        try {
            List<Address> startAddress = geo.getFromLocation(start.getLatitude(), start.getLongitude(), 1);
            String address = startAddress.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            List<Address> endAddress = geo.getFromLocation(end.getLatitude(), end.getLongitude(), 1);
            String endAddressLine = endAddress.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            location.setText(address);
            destination.setText(endAddressLine);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        arriveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginBtn.setVisibility(View.VISIBLE);
                arriveBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
                requestInfo.put("status", "atStartLocation");
                db = FirebaseFirestore.getInstance();
                db.collection("RiderRequests").document(requestInfo.get("rider_email").toString()).set(requestInfo);
            }
        });

        beginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestInfo.put("status", "inProgress");
                db = FirebaseFirestore.getInstance();
                db.collection("RiderRequests").document(requestInfo.get("rider_email").toString()).set(requestInfo);
                bundle = new Bundle();
                bundle.putSerializable("HashMap", requestInfo);
                beginRideListener.onBeginRide(bundle);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestInfo.put("status", "canceled");
                db = FirebaseFirestore.getInstance();
                db.collection("RiderRequests").document(requestInfo.get("rider_email").toString()).set(requestInfo);
                beginRideListener.onRideCanceled();

            }
        });


        requestRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.getString("status").equals("canceled")){

                }
            }
        });

        return view;
    }


}
