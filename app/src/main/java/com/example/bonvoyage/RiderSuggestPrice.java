package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class RiderSuggestPrice extends AppCompatActivity {
    private double lat1, lat2, long1, long2;
    private TextView text_given;
    private EditText propose;
    private ImageView verify;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_add_price);
        text_given = findViewById(R.id.price_info);
        propose = findViewById(R.id.price_edit);
        verify = findViewById(R.id.approve);
        submit = findViewById(R.id.submit_req);
        // implement methods to fetch the saved start and end locations from previous activity
        // from dedicated Rider class

        // using dummy latitude and longitude to calculate

        // UofA to Edmonton City Centre
        // comment out later, replace these values
        LatLng start = new LatLng(53.5439,-113.5263);
        LatLng end = new LatLng(53.221401, -113.4923);

        double distance = calculateDistance(start, end);
        double cost = distance*1.50;
        text_given.setText(String.format(Locale.CANADA,"The suggested fare for your trip" +
                " is %.2f. You must enter at least this much in order to request" +
                " a driver",cost));

        propose.setHint(String.format(Locale.CANADA,"%.2f",cost));
        submit.setEnabled(false);
        submit.setBackgroundColor(Color.parseColor("#323868"));
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double propose_val = Double.parseDouble(propose.getText().toString());

                if(propose_val>=cost){
                    verify.setBackgroundColor(Color.parseColor("#00b01d"));
                    submit.setBackgroundColor(Color.parseColor("#1a237e"));
                    submit.setEnabled(true);
                }

                if(propose_val<cost)
                {
                    verify.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    submit.setEnabled(false);
                    submit.setBackgroundColor(Color.parseColor("#323868"));
                }
            }
        });

        // send submit on click to request activity

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(RiderSuggestPrice.this, "yep!! accepted", Toast.LENGTH_SHORT).show();

                }
            });


    }

    private double calculateDistance(LatLng start, LatLng end) {

        long1 = Math.toRadians(start.longitude);
        lat1 = Math.toRadians(start.latitude);
        lat2 = Math.toRadians(end.latitude);
        long2 = Math.toRadians(end.longitude);

        double dlong = long2 - long1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlong / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }

}
