package com.example.bonvoyage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DriverStatusFragment extends Fragment {


    TextView status;
    TextView name;
    TextView amount;
    TextView destination;
    Button completeBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_status_overlay, container, false);
        status = view.findViewById(R.id.ds_status_title);
        name = view.findViewById(R.id.ds_riderName);
        amount = view.findViewById(R.id.ds_amount);
        destination = view.findViewById(R.id.rs_location);
        completeBtn = view.findViewById(R.id.ds_complete_btn);

        completeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        return view;
    }
}
