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

/**
 * DriverMapActivity extends MapActvity --> specialization for the Driver user class.
 * DriverMapActivity implements the homescreen for the driver user.
 */
public class DriverMapActivity extends MapActivity implements RideRequestAdapter.RideRequestAdapterListener {
    private static final String TAG = "DriverMapActivity";
    private EditText inputSearch;

    //Instances
    private ListenerRegistration mRiderListEventListener;
    private ListView riderList;
    private ArrayAdapter<RideRequest> riderLocationArrayAdapter;
    private ArrayList<RideRequest> rideRequestArrayList = new ArrayList<>();
    private FirebaseFirestore mDatabase;
    private DriverStatusFragment driverStatusFragment;

    /**
     * onMapReady sets the Map View, along with the associated Listview and EditText.
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        setContentView(R.layout.activity_driver_map);
        riderList = findViewById(R.id.rider_list_view);
        riderLocationArrayAdapter = new RideRequestAdapter(DriverMapActivity.this, rideRequestArrayList);
        riderList.setAdapter(riderLocationArrayAdapter);
        mDatabase = FirebaseFirestore.getInstance();

//        getRideRequests(mRiderListEventListener,mDatabase,riderLocationArrayAdapter, rideRequestArrayList);
        inputSearch = findViewById(R.id.endLocation);
//        ImageView magnifyingIcon = findViewById(R.id.ic_magnify);
//        magnifyingIcon.setVisibility(View.VISIBLE);
//        inputSearch.setVisibility(View.VISIBLE);
        /**
         * Allows driver to search for a different location.
         */
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

    @Override
    public void onRideSelected() {
        driverStatusFragment = new DriverStatusFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.driver_status_container, driverStatusFragment, "DRIVER STATUS").commit();
    }
}
