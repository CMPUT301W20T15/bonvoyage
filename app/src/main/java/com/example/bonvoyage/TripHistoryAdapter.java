package com.example.bonvoyage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bonvoyage.R;
import com.example.bonvoyage.models.RideRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TripHistoryAdapter extends ArrayAdapter<DocumentSnapshot> {
    private ArrayList<DocumentSnapshot> tripHistoryArrayList;
    private Context context;

    public TripHistoryAdapter(Context context, ArrayList<DocumentSnapshot> tripHistoryArrayList){
        super(context, 0, tripHistoryArrayList);
        this.tripHistoryArrayList = tripHistoryArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.trip_history_item, parent, false);
        }

        final DocumentSnapshot docSnap = tripHistoryArrayList.get(position);

        TextView driver_name = view.findViewById(R.id.driver_name);
        TextView driver_email = view.findViewById(R.id.driver_email);
        TextView rider_name = view.findViewById(R.id.rider_name);
        TextView rider_email = view.findViewById(R.id.rider_email);
        TextView start_loc = view.findViewById(R.id.start_location);
        TextView end_loc = view.findViewById(R.id.end_location);
        TextView cost = view.findViewById(R.id.trip_cost);
        TextView time_stamp = view.findViewById(R.id.time_stamp);

        driver_name.setText(docSnap.getString("driver_name"));
        driver_email.setText(docSnap.getString("driver_email"));
        rider_name.setText(docSnap.getString("rider_name"));
        rider_email.setText(docSnap.getString("rider_email"));
        //https://stackoverflow.com/questions/53799346/how-to-convert-geopoint-in-firestore-to-latlng
        GeoPoint startGeopoint = docSnap.getGeoPoint("startGeopoint");
        double start_lat = startGeopoint.getLatitude();
        double start_lng = startGeopoint.getLongitude ();
        LatLng start_latLng = new LatLng(start_lat, start_lng);
        start_loc.setText("Start: " + start_latLng.toString());
        GeoPoint endgeoPoint = docSnap.getGeoPoint("endGeopoint");
        double end_lat = endgeoPoint.getLatitude();
        double end_lng = endgeoPoint.getLongitude ();
        LatLng end_latLng = new LatLng(end_lat, end_lng);
        end_loc.setText("End: " + end_latLng.toString());
        cost.setText("$" + String.valueOf(docSnap.getLong("cost")));
        time_stamp.setText(String.valueOf(docSnap.getDate("timestamp")));

        return view;
    }
}
