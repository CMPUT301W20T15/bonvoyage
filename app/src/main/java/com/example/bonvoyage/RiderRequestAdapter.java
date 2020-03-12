package com.example.bonvoyage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bonvoyage.models.RiderRequest;

import java.util.ArrayList;

public class RiderRequestAdapter extends ArrayAdapter<RiderRequest> {
    private ArrayList<RiderRequest> riderRequestArrayList;
    private Context context;
    public RiderRequestAdapter(Context context, ArrayList<RiderRequest> riderRequestArrayList){
        super(context,0, riderRequestArrayList);
        this.riderRequestArrayList = riderRequestArrayList;
        this.context = context;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.rider_list_item,parent,false);
        }
        RiderRequest riderRequest = riderRequestArrayList.get(position);
        TextView user_name = view.findViewById(R.id.contact_name);
        String rider_name = riderRequest.getUserEmail();
        user_name.setText(rider_name);
        return view;
    }

}
