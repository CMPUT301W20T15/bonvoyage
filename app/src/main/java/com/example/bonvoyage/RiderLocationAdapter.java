package com.example.bonvoyage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bonvoyage.models.RiderLocation;

import java.util.ArrayList;

public class RiderLocationAdapter extends ArrayAdapter<RiderLocation> {
    private ArrayList<RiderLocation> riderLocationArrayList;
    private Context context;
    public RiderLocationAdapter(Context context, ArrayList<RiderLocation> riderLocationArrayList){
        super(context,0,riderLocationArrayList);
        this.riderLocationArrayList = riderLocationArrayList;
        this.context = context;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.rider_list_item,parent,false);
        }
        RiderLocation riderLocation = riderLocationArrayList.get(position);
        TextView user_name = view.findViewById(R.id.contact_name);
        String rider_name = riderLocation.getFirst_name() + " " + riderLocation.getLast_name();
        user_name.setText(rider_name);
        return view;
    }

}
