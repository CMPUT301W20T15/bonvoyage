package com.example.bonvoyage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Locale;


public class RiderPricingFragment extends Fragment {


    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rider_add_price, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        priceEdit = view.findViewById(R.id.price_edit);
    }

    public void updatePrice() {
        Bundle bundle = this.getArguments();
        FirebaseHandler firebaseHandler = new FirebaseHandler();
        if (bundle != null) {
            HashMap tripData = (HashMap) bundle.getSerializable("HashMap");
            float newCost = Float.parseFloat(priceEdit.getText().toString());
            tripData.put("cost", newCost);
            firebaseHandler.addNewRideRequestToDatabase(tripData, requestId);


        }

}
