package com.example.bonvoyage;

import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class RiderMapActivity extends MapActivity {

    private static final String TAG = "RiderMapActivity";
    private EditText destinationLocationBox;
    private TextView currentLocationBox;
    private Button continueButton;
    String first_name = "";
    String last_name = "";
    FragmentManager fm = getSupportFragmentManager();
    RiderPricingFragment pricingFragment;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        pricingFragment = (RiderPricingFragment)
                getSupportFragmentManager().findFragmentById(R.id.rider_add_price);
        pricingFragment.getView().setVisibility(View.GONE);
        ConstraintLayout riderView = findViewById(R.id.rider_layout);
        riderView.setVisibility(View.VISIBLE);

        currentLocationBox = findViewById(R.id.startLocation);
        currentLocationBox.setOnClickListener(v -> setCurrentLocation());

        continueButton = findViewById(R.id.continueButton);
        continueButton.setVisibility(View.GONE);
        continueButton.setOnClickListener(v -> continueToPayment());
        continueButton.setEnabled(false);
        destinationLocationBox = findViewById(R.id.endLocation);
        destinationLocationBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    Address address = geoLocate(destinationLocationBox);
                    if (address != null) {
                        continueButton.setEnabled(true);
                        continueButton.setVisibility(View.VISIBLE);
                        endLocation = new GeoPoint(address.getLatitude(), address.getLongitude());
                    }
                }
                return false;
            }
        });
    }

    private void setCurrentLocation() {
        Log.d(TAG, "Updated current location");
        getDeviceLocation();
    }

    private void continueToPayment() {
        double distance = calculateDistance(startLocation, endLocation);
        EditText priceEdit = findViewById(R.id.price_edit);

        double cost = distance * 2.5;
        priceEdit.setText(String.format(Locale.CANADA, "%.2f",cost));

        String priceText = String.format(getString(R.string.suggested_payment), cost);

        TextView priceInfo = findViewById(R.id.price_info);
        priceInfo.setText(priceText);

        createPricingFragment();
        
        Log.d(TAG, "Start location: " + startLocation.getLatitude()
                + ", " + startLocation.getLongitude());
        Log.d(TAG, "Destination location: " + endLocation.getLatitude()
                + ", " + endLocation.getLongitude());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        HashMap<String, Object> tripInformation = new HashMap<>();


        FirebaseUser user = firebaseHandler.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("riders").document("testrider@gmail.com");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (!Objects.requireNonNull(documentSnapshot.getString("first_name")).isEmpty()) {
                        first_name = documentSnapshot.getString("first_name");
                    }
                    if (!Objects.requireNonNull(documentSnapshot.getString("last_name")).isEmpty()) {
                        last_name = documentSnapshot.getString("last_name");
                    }
                } else {
                    Log.d(TAG, "No user found with that email");
                }
            }
        });

        tripInformation.put("cost", cost);
        tripInformation.put("endGeopoint", endLocation);
        tripInformation.put("startGeopoint", startLocation);
        tripInformation.put("firstName", first_name);
        tripInformation.put("lastName", last_name);
        tripInformation.put("phoneNumber", user.getPhoneNumber());
        tripInformation.put("email", user.getEmail());
        tripInformation.put("status", "available");
        tripInformation.put("timestamp", timestamp);
        Bundle rideInfo = new Bundle();
        rideInfo.putSerializable("HashMap",tripInformation);
        pricingFragment.setArguments(rideInfo);
        continueButton.setOnClickListener(v -> pricingFragment.updatePrice());
    }


    void createPricingFragment() {
        currentLocationBox.setOnClickListener(null);
        destinationLocationBox.setOnClickListener(null);
        destinationLocationBox.setEnabled(false);

        pricingFragment.getView().setVisibility(View.VISIBLE);


    }
    private double calculateDistance(GeoPoint start, GeoPoint end) {
        Log.d(TAG, "Destination location: " + endLocation.getLatitude()
                + ", " + endLocation.getLongitude());
        double long1 = Math.toRadians(start.getLongitude());
        double lat1 = Math.toRadians(start.getLatitude());
        double lat2 = Math.toRadians(end.getLatitude());
        double long2 = Math.toRadians(end.getLongitude());

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
