package com.example.bonvoyage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DriverStatusActivity extends AppCompatActivity {

    TextView status;
    TextView name;
    TextView amount;
    TextView destination;
    Button completeBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_status_overlay);

        status = findViewById(R.id.ds_status_title);
        name = findViewById(R.id.ds_riderName);
        amount = findViewById(R.id.ds_amount);
        destination = findViewById(R.id.rs_location);
        completeBtn = findViewById(R.id.ds_complete_btn);

        completeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Update the status request
            }
        });

    }
}
