package com.example.bonvoyage;

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

import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DriverStatusFragment extends Fragment {

    final String TAG = "DRIVER STATUS FRAGMENT";
    FirebaseHandler firebaseHandler;
    private HashMap tripData;
    private DriverStatusListener driverStatusListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        driverStatusListener = (DriverStatusListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_status_overlay, container, false);
        TextView status = view.findViewById(R.id.ds_status_title);
        TextView name = view.findViewById(R.id.ds_riderName);
        Bundle bundle = getArguments();
        this.tripData = (HashMap) bundle.getSerializable("HashMap");
        TextView amount = view.findViewById(R.id.ds_amount);
        TextView destination = view.findViewById(R.id.rs_location);
        Button completeBtn = view.findViewById(R.id.ds_complete_btn);


        amount.setText(tripData.get("cost").toString());

        Geocoder geo = new Geocoder(DriverStatusFragment.this.getContext());
        GeoPoint end = (GeoPoint) tripData.get("endGeopoint");


        /**
        try {
            List<Address> endAddress = geo.getFromLocation(end.getLatitude(), end.getLongitude(), 1);
            String address = endAddress.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            destination.setText(address);
        } catch (IOException e) {
            Log.d(TAG, "*****START ADDRESS*** NOT WOKRING");
        }
         */

        completeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                driverStatusListener.onRideComplete();
            }
        });

        return view;
    }

    public interface DriverStatusListener{
        void onRideComplete();
    }

}
