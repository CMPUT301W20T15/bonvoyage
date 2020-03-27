package com.example.bonvoyage;

import android.content.Context;
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

public class RideRequestAdapter extends ArrayAdapter<RideRequest> {
    private ArrayList<RideRequest> rideRequestArrayList;
    private Context context;
    public RideRequestAdapter(Context context, ArrayList<RideRequest> rideRequestArrayList){
        super(context,0, rideRequestArrayList);
        this.rideRequestArrayList = rideRequestArrayList;
        this.context = context;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.rider_list_item,parent,false);
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
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                //PASS TO NEXT FRAGMENT HERE
                Log.d("TEST ACCEPT", "WORKING");
            }
        });
        return view;
    }

}
