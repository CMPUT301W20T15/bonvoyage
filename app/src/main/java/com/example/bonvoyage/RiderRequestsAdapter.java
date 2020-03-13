package com.example.bonvoyage;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bonvoyage.models.RiderRequests;

import java.util.ArrayList;

public class RiderRequestsAdapter extends ArrayAdapter<RiderRequests> {
    private ArrayList<RiderRequests> riderRequestsArrayList;
    private Context context;
    public RiderRequestsAdapter(Context context, ArrayList<RiderRequests> riderRequestsArrayList){
        super(context,0, riderRequestsArrayList);
        this.riderRequestsArrayList = riderRequestsArrayList;
        this.context = context;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.rider_list_item,parent,false);
        }
        final RiderRequests riderRequests = riderRequestsArrayList.get(position);
        TextView name = view.findViewById(R.id.contact_name);
        TextView driverCost = view.findViewById(R.id.drive_cost);
        TextView userEmail = view.findViewById(R.id.rider_email);
        TextView userPhone = view.findViewById(R.id.rider_phone);
        Button acceptBtn = (Button)view.findViewById(R.id.driver_accept);

        String riderName = riderRequests.getFirstName() + " " +riderRequests.getLastName();
        String email = riderRequests.getUserEmail();
        String phone = riderRequests.getPhoneNumber();
        float rideCost = riderRequests.getCost();
        name.setText(riderName);
        driverCost.setText(Float.toString(rideCost));
        userEmail.setText(email);
        userPhone.setText(phone);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PASS TO NEXT FRAGMENT HERE
                Log.d("TEST ACCEPT", "WORKING");
            }
        });
        return view;
    }

}
