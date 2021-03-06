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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;

import static java.lang.Math.round;


public class RiderPricingFragment extends Fragment {

    private static String requestId = "hello@gmail.com";
    private EditText priceEdit;

        public void updatePrice() {
            Bundle bundle = this.getArguments();
            FirebaseHandler firebaseHandler = new FirebaseHandler();
            if (bundle != null) {
                DecimalFormat df = new DecimalFormat("#.00");

                HashMap tripData = (HashMap) bundle.getSerializable("HashMap");
                double inputCost = Double.parseDouble(priceEdit.getText().toString());
                float newCost = Float.parseFloat(df.format(inputCost));
                tripData.put("cost", newCost);
                firebaseHandler.addNewRideRequestToDatabase(tripData, firebaseHandler.getCurrentUser().getEmail());
            }
        }
    private static final String TAG = "RiderPricingFragment";
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

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public static String getRequestId() {
        return requestId;
    }
}
