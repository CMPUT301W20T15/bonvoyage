package com.example.bonvoyage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private User currentUser;
    ListView riderList;
    ArrayAdapter<String> riderAdapter;
    ArrayList<String> riderDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        riderList = findViewById(R.id.rider_list);
        riderDataList = new ArrayList<>();
        riderDataList.add("user1") ;
        riderDataList.add("user2");

        riderAdapter = new RidersAvailableList(this, riderDataList);
        riderList.setAdapter(riderAdapter);

        currentUser = new User("test", "driver");

        if (currentUser.getUserType().equals("rider")) {
            ConstraintLayout riderView = findViewById(R.id.rider_layout);
            riderView.setVisibility(View.VISIBLE);
        }
        else if (currentUser.getUserType().equals("driver")) {
            ConstraintLayout driverView = findViewById(R.id.driver_layout);
            driverView.setVisibility(View.VISIBLE);
        }
    }
}
