package com.example.bonvoyage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RiderHome extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location origin;
    private Marker startMarker;
    private Marker endMarker;

    EditText currentLocation;
    EditText destinationLocation;
    Location temp = new Location(LocationManager.GPS_PROVIDER);
    private FusedLocationProviderClient mFusedLocationClient;

    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;


    SupportMapFragment mapFragment;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_home);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(RiderHome.this);

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        currentLocation = findViewById(R.id.current_location);
        destinationLocation = findViewById(R.id.add_destination);

        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        PlacesClient placesClient = Places.createClient(RiderHome.this);

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG);

                Intent autocompleteInt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fields).build(RiderHome.this);
                startActivityForResult(autocompleteInt, 10);
            }
        });
        destinationLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLocation==null)
                {
                    printToast("select origin first!");
                }
                else {
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG);

                    Intent autocompleteInt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                            fields).build(RiderHome.this);
                    startActivityForResult(autocompleteInt, 10);
                }
            }
        });
    }

    private void printToast(String s) {
        Toast.makeText(RiderHome.this,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            LatLng dest = place.getLatLng();

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng home = new LatLng(53.5232,-113.5263);
        boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));
    try{
        if (!success) {
            Log.e("TAG", "Style parsing failed.");
        }
    } catch (Resources.NotFoundException e) {
        Log.e("TAG", "Can't find style. Error: ", e);
    }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(home));
        googleMap.setMinZoomPreference(17);

    }
}