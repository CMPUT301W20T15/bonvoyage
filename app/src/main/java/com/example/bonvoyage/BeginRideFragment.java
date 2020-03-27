package com.example.bonvoyage;

import android.app.UiAutomation;
import android.content.Context;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class BeginRideFragment extends Fragment {

    private FirebaseFirestore db;
    private Bundle bundle;
    private HashMap requestInfo;
    private FirebaseHandler firebaseHandler;
    private BeginRideListener beginRideListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        beginRideListener = (BeginRideListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.driver_begin_ride, container, false);
        TextView rider_name = view.findViewById(R.id.beginRide_riderName);
        TextView location = view.findViewById(R.id.beginRide_location);
        TextView destination = view.findViewById(R.id.beginRide_destination);
        TextView cost = view.findViewById(R.id.beginRide_payment);
        Button beginBtn = view.findViewById(R.id.beginRide_beginRideBtn);
        Button cancelBtn = view.findViewById(R.id.beginRide_cancelRideBtn);
        db = FirebaseFirestore.getInstance();


        beginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = getArguments();
                requestInfo = (HashMap) bundle.getSerializable("HashMap");
                requestInfo.put("status", "inProgress");
                db = FirebaseFirestore.getInstance();
                db.collection("RiderRequests").document(requestInfo.get("rider_email").toString()).update("status", "inProgress");
                db.collection("RiderRequests").document(requestInfo.get("rider_email").toString()).set(requestInfo);
                bundle = new Bundle();
                bundle.putSerializable("HashMap", requestInfo);
                beginRideListener.onBeginRide(bundle);
            }
        });

        return view;
    }

    public interface BeginRideListener{
        void onBeginRide(Bundle request_info);
    }

}
