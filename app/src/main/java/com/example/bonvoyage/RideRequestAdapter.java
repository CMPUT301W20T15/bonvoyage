package com.example.bonvoyage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bonvoyage.models.RideRequest;

import java.util.ArrayList;

/**
 * RideRequestAdapter handles each listview items and contains the ride requests from riders, taken from firebase
 * to communicate with the driver.
 */
public class RideRequestAdapter extends ArrayAdapter<RideRequest> {
    private ArrayList<RideRequest> rideRequestArrayList;
    private Context context;
    public RideRequestAdapter(Context context, ArrayList<RideRequest> rideRequestArrayList){
        super(context,0, rideRequestArrayList);
        this.rideRequestArrayList = rideRequestArrayList;
        this.context = context;
    }

    /**
     * getView handles all the RideRequest objects contained in the array adpter and implements
     * access to the views for DriverMapActivity.
     * @param position
     * @param convertView
     * @param parent
     * @return View
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.driver_list_item,parent,false);
        }
        final RideRequest rideRequest = rideRequestArrayList.get(position);
        TextView name = view.findViewById(R.id.contact_name);
        TextView driverCost = view.findViewById(R.id.drive_cost);
        TextView userEmail = view.findViewById(R.id.rider_email);
        TextView userPhone = view.findViewById(R.id.rider_phone);
        Button acceptBtn = (Button)view.findViewById(R.id.driver_accept);

        String riderName = rideRequest.getFirstName() + " " + rideRequest.getLastName();
        String email = rideRequest.getUserEmail();
        String phone = rideRequest.getPhoneNumber();
        float rideCost = rideRequest.getCost();
        name.setText(riderName);
        driverCost.setText(Float.toString(rideCost));
        userEmail.setText(email);
        userPhone.setText(phone);

        //OnClickListener for each Accept Button for each of the RideRequests shown in the listview.
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PASS TO NEXT FRAGMENT HERE
                Intent intent = new Intent(context, DriverStatusActivity.class);
                context.startActivity(intent);
                Log.d("TEST ACCEPT", "WORKING");
            }
        });
        return view;
    }

}
