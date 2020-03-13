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
        RiderRequests riderRequests = riderRequestsArrayList.get(position);
        TextView user_name = view.findViewById(R.id.contact_name);
        TextView driver_cost = view.findViewById(R.id.drive_cost);
        ImageButton expandBtn = (ImageButton)view.findViewById(R.id.expand_button);

        String rider_name = riderRequests.getFirstName() + " " +riderRequests.getLastName();
        float ride_amount = riderRequests.getCost();
        user_name.setText(rider_name);
        driver_cost.setText(Float.toString(ride_amount));
        expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HI", "HIIII");
            }
        });

        return view;
    }

}
