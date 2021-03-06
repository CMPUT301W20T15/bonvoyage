package com.example.bonvoyage;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RiderStatusActivity extends AppCompatActivity {
    TextView title;

    View profile_preview;
    TextView profile_name;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_status_overlay);
        View profile_preview = findViewById(R.id.rs_profile);
        TextView profile_name = profile_preview.findViewById(R.id.rs_profile_name);
        View location_layout = findViewById(R.id.rs_location);
        TextView current_location = location_layout.findViewById(R.id.startLocation);
        TextView destination_location = location_layout.findViewById(R.id.endLocation);
        View contact_layout = findViewById(R.id.rs_contact);
        Button callBtn = contact_layout.findViewById(R.id.rs_call_btn);
        Button textBtn = contact_layout.findViewById(R.id.rs_text_btn);
        Button emailBtn = contact_layout.findViewById(R.id.rs_email_btn);
        Button exitBtn = findViewById(R.id.rs_exitBtn);

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



    }
}
