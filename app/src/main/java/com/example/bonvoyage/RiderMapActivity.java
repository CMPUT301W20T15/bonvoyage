package com.example.bonvoyage;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;


public class RiderMapActivity extends FragmentActivity implements
        OnMapReadyCallback {

    private static final int REQUEST_CODE = 10;
    private GoogleMap mMap;
    private EditText findLocation;
    private TextView destinationLocation;

    private Rider rider;
    private Location current;
    private LatLng end;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private MarkerOptions startMarker;
    private MarkerOptions endMarker;
    private Button submit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startMarker = new MarkerOptions();
        // map the mapFragment to the map element in the xml
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_rider);
        mapFragment.getMapAsync(RiderMapActivity.this);

        findLocation = findViewById(R.id.current_location);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        findLocation.setOnClickListener(v -> {
            // if editText was clicked, check if permissions are granted
            // if permissions granted, check if location is enabled
            // request the location from the system
            checkPermissionsForApp();
            if (mMap.isMyLocationEnabled()) {
                isLocationEnabled();

                requestLocationUpdates();
            }
            if (current != null) {
                moveMap(mMap, current, startMarker);
                findLocation.setText("Current Location");
            }

        });

        if (!Places.isInitialized()) {
            Places.initialize(RiderMapActivity.this, getString(R.string.google_maps_API_key));
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(RiderMapActivity.this);


        destinationLocation = findViewById(R.id.add_destination);

        destinationLocation.setOnClickListener(v -> {
            AutocompleteSupportFragment autocompleteFragment =
                    (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE);
        });

        submit = findViewById(R.id.continue_req);
        //submit button code
        submit.setOnClickListener(v -> {
            if(end!=null && current!=null){
                rider.setCurrentLocation(new LatLng(current.getLatitude(), current.getLongitude()));
                rider.setDestinationLocation(end);
                drawPath();
            }
            Intent intent = new Intent(this, RiderSuggestPrice.class);
            startActivity(intent);
        });
    }

    private void drawPath() {
        LatLng currentLocation = new LatLng(current.getLatitude(), current.getLongitude());
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(currentLocation,end)
                .width(6)
                .color(Color.parseColor("#9da1c7")));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                end = place.getLatLng();
                Location endLocation = new Location(LocationManager.GPS_PROVIDER);
                endLocation.setLatitude(end.latitude);
                endLocation.setLongitude(end.longitude);
                endMarker = new MarkerOptions();
                moveMap(mMap,endLocation,endMarker);
                destinationLocation.setText(place.getAddress());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.

                destinationLocation.setText("Could not fetch place");
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }


    }
    // code from https://stackoverflow.com/questions/10311834/how-to-check-if-location-services-are-enabled
    // lenik https://stackoverflow.com/users/1329296/lenik
    public void isLocationEnabled()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            LocationManager lm = (LocationManager) RiderMapActivity.this.getSystemService(Context.LOCATION_SERVICE);
            if(!lm.isLocationEnabled()){
                AlertDialog alert = new AlertDialog.Builder(RiderMapActivity.this)
                        .setTitle("Location Not Active")  // GPS not found
                        .setMessage("Turn on Location?") // Want to enable?
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                RiderMapActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        } else {
            // This is Deprecated in API 28
            int mode = Settings.Secure.getInt(RiderMapActivity.this.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
//             (mode != Settings.Secure.LOCATION_MODE_OFF);
            return;

        }
    }


    // code from https://stackoverflow.com/questions/46552087/fusedlocationproviderclient-when-and-how-to-stop-looper
    // Daniel Nugent https://stackoverflow.com/users/4409409/daniel-nugent
    @Override
    public void onResume() {
        super.onResume();
        if (mFusedLocationClient != null) {
            requestLocationUpdates();
        }
    }

    public void requestLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    current = location;
                }
            };

        };
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }


    private void checkPermissionsForApp() {
        // checks if permissions are granted or not

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE);


        } else {
            mMap.setMyLocationEnabled(true);
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                        if (location != null) {
                            current = location;
                        }
                    });
                } else {
                    Toast.makeText(this, "Please turn on your location", Toast.LENGTH_SHORT).show();
                    checkPermissionsForApp();
                }
                break;
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // customize the state of the map when the app first runs
        mMap = googleMap;
        styleMap();
        Location dummyLocation = new Location(LocationManager.GPS_PROVIDER);
        dummyLocation.setLongitude(-113.5263);
        dummyLocation.setLatitude(53.5232);
        moveMap(mMap,dummyLocation,startMarker);
        Location dummEndLoc = new Location(LocationManager.GPS_PROVIDER);
        dummEndLoc.setLatitude(53.516698);
        dummEndLoc.setLongitude(-113.505075);
        endMarker = new MarkerOptions();
        moveMap(mMap, dummEndLoc, endMarker);
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(53.5232, -113.5263), new LatLng(53.516698, -113.505075))
                .width(6)
                .color(Color.parseColor("#9da1c7")));
        mMap.setMinZoomPreference(14);


    }

    public void moveMap(GoogleMap mMap, Location current, MarkerOptions marker) {
        // moveMap changes focus of the map and adds a marker according to coordinate
        LatLng currentLocation = new LatLng(current.getLatitude(), current.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.setMinZoomPreference(17);
        marker.position(currentLocation);
        mMap.addMarker(marker);

    }
    public void styleMap(){
        boolean success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));
        try{
            if (!success) {
                Log.e("TAG", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("TAG", "Can't find style. Error: ", e);
        }
    }
}
