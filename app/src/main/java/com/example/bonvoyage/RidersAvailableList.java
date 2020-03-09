package com.example.bonvoyage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RidersAvailableList extends ArrayAdapter<String> {
    private ArrayList<String> riders;
    private Context context;

    public RidersAvailableList(Context context, List<String> riders) {
        super(context,0, riders);
        this.riders = (ArrayList<String>) riders;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.driver_list_item, parent, false);
        }

        String rider = riders.get(position);
        TextView riderUsername = view.findViewById(R.id.rider_username);
        riderUsername.setText(rider);

        return view;
    }
}

