package com.example.bonvoyage.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bonvoyage.R;

public class RiderStatusDialog extends DialogFragment {

    private TextView title;
    private TextView driverName;
    private TextView driverVehicle;
    private TextView currentLocation;
    private TextView destination;
    private TextView amount;
    private Button callBtn;
    private Button textBtn;
    private ImageView priceIcon;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rider_request_status, container, false);
        title = view.findViewById(R.id.rider_status_title);
        driverName = view.findViewById(R.id.ds_driver_name);
        driverVehicle = view.findViewById(R.id.rs_car_type);
        currentLocation = view.findViewById((R.id.rs_location));
        destination = view.findViewById(R.id.rs_destination);
        amount = view.findViewById(R.id.rs_price);
        callBtn = view.findViewById(R.id.rs_call_btn);
        textBtn = view.findViewById(R.id.rs_text_btn);
        priceIcon = view.findViewById(R.id.rs_amount_icon);

        if(isAccepted() && !isInProgress()){
            title.setText("Your driver is on their way!");
        }

        else if(isAccepted() && isInProgress()){
            title.setText("Your ride is in progress!");
            amount.setVisibility(View.GONE);
            callBtn.setVisibility(View.GONE);
            textBtn.setVisibility(View.GONE);
            priceIcon.setVisibility(View.GONE);
        }


        return view;
    }


    //Not implemented yet. used for xml layout testing.
    public boolean isAccepted(){
        return true;
    }

    //not actual implementation. used for xml layout testing
    public boolean isInProgress(){
        return true;
    }











}
