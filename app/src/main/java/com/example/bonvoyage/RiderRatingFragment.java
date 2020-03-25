package com.example.bonvoyage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RiderRatingFragment extends DialogFragment {

    View profile_preview;
    TextView profile_name;
    TextView profile_sub_header;

    View location_layout;
    TextView current_location;
    TextView destination_location;

    RatingBar rating;

    Button rating_complete_btn;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.rating_bar_layout, null);

        profile_preview = view.findViewById(R.id.rs_profile);
        profile_name = profile_preview.findViewById(R.id.rs_profile_name);
        profile_sub_header = profile_preview.findViewById(R.id.rs_profile_subheader);

        location_layout = view.findViewById(R.id.rs_location);
        current_location = location_layout.findViewById(R.id.startLocation);
        destination_location = location_layout.findViewById(R.id.endLocation);

        rating = view.findViewById(R.id.rating);

        rating_complete_btn = view.findViewById(R.id.rating_complete_btn);

        rating_complete_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //startActivity(new Intent(this, RiderMapActivity.class));
            }
        });

        // for the fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Rate your driver")
                .create();
    }
}
