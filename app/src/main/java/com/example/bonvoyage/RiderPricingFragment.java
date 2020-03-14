package com.example.bonvoyage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.GeoPoint;

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
            EditText priceEdit = view.findViewById(R.id.price_edit);
//            EditText propose = view.findViewById(R.id.price_info);
            RiderMapActivity riderMapActivity = (RiderMapActivity)getActivity();
            GeoPoint start = riderMapActivity.startLocation;
            GeoPoint fin = riderMapActivity.endLocation;
//        double distance = calculateDistance(start, fin);
//            double cost = distance *1.5;
//            priceEdit.setHint(String.format(Locale.CANADA, "%.2f",cost));
//            priceEdit.setOnClickListener(v -> setPrice());
//        propose.setHint(String.format(Locale.CANADA,"%.2f",cost));
        }

//        private double calculateDistance(GeoPoint start, GeoPoint end) {
//            double long1 = Math.toRadians(start.getLongitude());
//            double lat1 = Math.toRadians(start.getLatitude());
//            double lat2 = Math.toRadians(end.getLatitude());
//            double long2 = Math.toRadians(end.getLongitude());
//
//            double dlong = long2 - long1;
//            double dlat = lat2 - lat1;
//            double a = Math.pow(Math.sin(dlat / 2), 2)
//                    + Math.cos(lat1) * Math.cos(lat2)
//                    * Math.pow(Math.sin(dlong / 2),2);
//
//            double c = 2 * Math.asin(Math.sqrt(a));
//
//            // Radius of earth in kilometers. Use 3956
//            // for miles
//            double r = 6371;
//
//            // calculate the result
//            return(c * r);
//        }



}
