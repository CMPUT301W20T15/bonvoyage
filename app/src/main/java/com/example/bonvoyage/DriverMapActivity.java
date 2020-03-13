package com.example.bonvoyage;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bonvoyage.models.RideRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;


public class DriverMapActivity extends MapActivity {

    private static final String TAG = "DriverMapActivity";
    private EditText inputSearch;

    private ListenerRegistration mRiderListEventListener;

    private ListView riderList;
    private ArrayAdapter<RideRequest> riderLocationArrayAdapter;
    private ArrayList<RideRequest> rideRequestArrayList = new ArrayList<>();
    private FirebaseFirestore mDatabase;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        setContentView(R.layout.activity_driver_map);
        riderList = findViewById(R.id.rider_list_view);
        riderLocationArrayAdapter = new RideRequestAdapter(DriverMapActivity.this, rideRequestArrayList);
        riderList.setAdapter(riderLocationArrayAdapter);
        mDatabase = FirebaseFirestore.getInstance();

        getRideRequests(mRiderListEventListener,mDatabase,riderLocationArrayAdapter, rideRequestArrayList);
        inputSearch = findViewById(R.id.endLocation);
//        ImageView magnifyingIcon = findViewById(R.id.ic_magnify);
//        magnifyingIcon.setVisibility(View.VISIBLE);
//        inputSearch.setVisibility(View.VISIBLE);
        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate(inputSearch);
                }

                return false;
            }
        });
    }
}
