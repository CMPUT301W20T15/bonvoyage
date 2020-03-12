package com.example.bonvoyage;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private User currentUser;
    ListView riderList;
    ArrayAdapter<String> riderAdapter;
    ArrayList<String> riderDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isServicesOK()){
            init();
        }
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        riderList = findViewById(R.id.rider_list);
        riderDataList = new ArrayList<>();
        riderDataList.add("user1") ;
        riderDataList.add("user2");

        riderAdapter = new RidersAvailableList(this, riderDataList);
        riderList.setAdapter(riderAdapter);

        currentUser = new Rider("jane", "doe", "abc", "780296664","abcd");


        if (currentUser.getUserType().equals("rider")) {
            ConstraintLayout riderView = findViewById(R.id.rider_layout);
            riderView.setVisibility(View.VISIBLE);
        }
        else if (currentUser.getUserType().equals("driver")) {
            ConstraintLayout driverView = findViewById(R.id.driver_layout);
            driverView.setVisibility(View.VISIBLE);
        }
    }

    private void init(){
        currentUser = new Rider("jane", "doe", "abc", "780296664","abcd");
        if (currentUser.getUserType().equals("rider")) {
            Intent intent = new Intent(MainActivity.this, RiderMapActivity.class);
            startActivity(intent);
        }
        else if (currentUser.getUserType().equals("driver")) {
            Intent intent = new Intent(MainActivity.this, DriverMapActivity.class);
            startActivity(intent);
        }

//        Button btnMap = (Button) findViewById(R.id.map_id);
//        btnMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,DriverMapActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    public boolean isServicesOK(){
        Log.d(TAG,"isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS){
            //everything is good can make map stuff
            Log.d(TAG,"isServicesOK: Google play services is working");
            return true;
        }else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG,"isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(this,"You can't make map requests",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // This needs to be replaced with an actual action
        if (id == R.id.menu_item_home) {
            Toast.makeText(MainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
