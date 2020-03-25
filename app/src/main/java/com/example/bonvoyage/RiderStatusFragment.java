package com.example.bonvoyage;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.RatingBar;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class RiderStatusFragment extends Fragment {

    TextView title;

    View profile_preview;
    TextView profile_name;
    TextView profile_sub_header;


    View location_layout;
    TextView current_location;
    TextView destination_location;

    View contact_layout;
    Button textBtn;
    Button callBtn;
    Button emailBtn;


    View rating_layout;
    RatingBar driver_rating;

    TextView exitBtn;
    private RiderStatusListener statusListener;


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
        profile_preview = view.findViewById(R.id.rs_profile);
        profile_name = profile_preview.findViewById(R.id.rs_profile_name);
        profile_sub_header = profile_preview.findViewById(R.id.rs_profile_subheader);
        location_layout = view.findViewById(R.id.rs_location);
        current_location = location_layout.findViewById(R.id.startLocation);
        destination_location = location_layout.findViewById(R.id.endLocation);
        contact_layout = view.findViewById(R.id.rs_contact);
        callBtn = contact_layout.findViewById(R.id.rs_call_btn);
        textBtn = contact_layout.findViewById(R.id.rs_text_btn);
        emailBtn = contact_layout.findViewById(R.id.rs_email_btn);
        rating_layout = view.findViewById(R.id.rs_rate_driver);
        driver_rating = rating_layout.findViewById(R.id.rating);
        exitBtn = view.findViewById(R.id.rs_exitBtn);

        callBtn.setOnClickListener(new View.OnClickListener() {
        
            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + "4034026524"));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
            }
        });

        textBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Uri sms_uri = Uri.parse("smsto:4034026524");
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


        driver_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                driver_rating.getRating();
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

    private void addRiderStatusListener(){
        getView().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                statusListener.onCancelRide();
            }
        });
    }

    // This interface is used to interact with RiderMapActivity
    public interface RiderStatusListener {
        void onCancelRide();
    }
}
