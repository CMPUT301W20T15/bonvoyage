package com.example.bonvoyage;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;


public class DriverMapActivity extends MapActivity {

    private static final String TAG = "DriverMapActivity";
    private EditText inputSearch;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);

//        ConstraintLayout driverView = findViewById(R.id.driver_layout);
//        driverView.setVisibility(View.VISIBLE);

        inputSearch = findViewById(R.id.endLocation);
        ImageView magnifyingIcon = findViewById(R.id.ic_magnify);
        magnifyingIcon.setVisibility(View.VISIBLE);
        inputSearch.setVisibility(View.VISIBLE);
        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate(inputSearch);
                }

                return false;
            }
        });
    }
}
