package com.example.bonvoyage;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.RatingBar;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.vision.L.TAG;

public class RiderStatusFragment extends Fragment {
    private static final String TAG = "RiderStatusFragment";
    private TextView current_location;
    private TextView destination_location;
    private RiderStatusListener statusListener;
    private FirebaseFirestore db;
    private HashMap tripData;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        statusListener = (RiderStatusListener) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addRiderStatusListener();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rider_status_overlay, container, false);
        TextView title = view.findViewById(R.id.rs_title);
        title.setText("Trip requested");

        View profile_preview = view.findViewById(R.id.rs_profile);
        profile_preview.setVisibility(View.GONE);
        TextView profile_name = profile_preview.findViewById(R.id.rs_profile_name);
        View location_layout = view.findViewById(R.id.rs_location);
        current_location = location_layout.findViewById(R.id.startLocation);
        destination_location = location_layout.findViewById(R.id.endLocation);
        View contact_layout = view.findViewById(R.id.rs_contact);
        contact_layout.setVisibility(View.GONE);
        Button callBtn = contact_layout.findViewById(R.id.rs_call_btn);
        Button textBtn = contact_layout.findViewById(R.id.rs_text_btn);
        Button emailBtn = contact_layout.findViewById(R.id.rs_email_btn);
        Button exitBtn = view.findViewById(R.id.rs_exitBtn);

        Bundle bundle = getArguments();
        tripData = (HashMap) bundle.getSerializable("HashMap");
        db = FirebaseFirestore.getInstance();
        DocumentReference statusRef = db.collection("RiderRequests").document(tripData.get("userEmail").toString());
        setAddresses();

        statusRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.get("status").toString().equals("accepted")){
                    title.setText("Ride request accepted, Rider is on their way!");
                    profile_preview.setVisibility(View.VISIBLE);
                    profile_name.setVisibility(View.VISIBLE);
                    profile_name.setText(documentSnapshot.get("driver_name").toString());
                    contact_layout.setVisibility(View.VISIBLE);
                    callBtn.setVisibility(View.VISIBLE);
                    textBtn.setVisibility(View.VISIBLE);
                }

                else if(documentSnapshot.getString("status").equals("atStartLocation")){
                    title.setText("Rider is here!");
                    exitBtn.setVisibility(View.GONE);
                }

                else if(documentSnapshot.getString("status").equals("inProgress")){
                    title.setText("Ride in progress");
                    contact_layout.setVisibility(View.GONE);
                    exitBtn.setVisibility(View.GONE);

                }
                else if(documentSnapshot.getString("status").equals("canceled")){
                    statusListener.onCancelRide();
                }
                else if (documentSnapshot.getString("status").equals("complete")){
                    db = FirebaseFirestore.getInstance();
                    db.collection("RiderRequests").document(tripData.get("email").toString()).delete();
                    getActivity().startActivity(new Intent(getActivity(), RiderPaymentFragment.class));     // Call rider payment fragment to display for QR Code
                    statusListener.onRideComplete();
                }
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
        
            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + tripData.get("driver_phone").toString()));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
            }
        });

        textBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Uri sms_uri = Uri.parse("smsto:"+ tripData.get("driver_phone").toString());
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                sms_intent.putExtra("sms_body", "Hello");
                startActivity(sms_intent);
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Uri email_uri = Uri.parse("mailto:testdriver@gmail.com");
                Intent email_intent = new Intent(Intent.ACTION_SENDTO, email_uri);
                email_intent.putExtra("mail_body", "Hello");
                startActivity(email_intent);
            }
        });


        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusListener.onCancelRide();
            }
        });

        return view;
    }

    private void setAddresses(){
        Geocoder geo = new Geocoder(RiderStatusFragment.this.getContext());
        GeoPoint start = (GeoPoint) tripData.get("startGeopoint");
        GeoPoint end = (GeoPoint) tripData.get("endGeopoint");
        try {
            List<Address> startAddress = geo.getFromLocation(start.getLatitude(), start.getLongitude(), 1);
            String address = startAddress.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            List<Address> endAddress = geo.getFromLocation(end.getLatitude(), end.getLongitude(), 1);
            String endAddressLine = endAddress.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            current_location.setText(address);
            destination_location.setText(endAddressLine);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }


    private void addRiderStatusListener(){
        getView().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                statusListener.onCancelRide();
            }
        });
    }

    // This interface is used to interact with RiderMapActivity
}
