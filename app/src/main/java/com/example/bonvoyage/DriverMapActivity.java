package com.example.bonvoyage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bonvoyage.models.RideRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DriverMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, RideRequestAdapter.RequestListener, BeginRideFragment.BeginRideListener, DriverStatusFragment.DriverStatusListener {
    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //widgets
    private EditText mSearchText;
    private ImageView mGps;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private FirebaseFirestore mDatabase;

    private ListenerRegistration mRiderListEventListener;

    private ListView riderList;
    private ArrayAdapter<RideRequest> riderLocationArrayAdapter;
    private ArrayList<RideRequest> rideRequestArrayList = new ArrayList<>();
    private FirebaseHandler mFirebaseHandler;
    private Location currentLocation;
    private Driver currentDriver;
    private RelativeLayout mapContainer;
    private LinearLayout linearLayoutContainer;
    private BeginRideFragment beginRideFragment;
    private DriverStatusFragment driverStatusFragment;
    //private ArrayList<RideRequest> riderRequestList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);
        this.mapContainer = findViewById(R.id.map_container);
        this.linearLayoutContainer = findViewById(R.id.linear_map_containter);
        riderList = findViewById(R.id.rider_list_view);
        riderLocationArrayAdapter = new RideRequestAdapter(DriverMapActivity.this, rideRequestArrayList, this);
        riderList.setAdapter(riderLocationArrayAdapter);
        mFirebaseHandler = new FirebaseHandler();
        mSearchText = (EditText) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);
        mDatabase = FirebaseFirestore.getInstance();
        getLocationPermission();

        riderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RideRequest selectedRider = (RideRequest) adapterView.getItemAtPosition(i);
                riderLocationArrayAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        //mMap.setOnPolylineClickListener(DriverMapActivity.this);
        mMap.setOnInfoWindowClickListener(DriverMapActivity.this);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.driver_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }
    private void init(){
        Log.d(TAG, "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate(mMap,mSearchText);
                }

                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        hideSoftKeyboard();
    }

    private void geoLocate(GoogleMap mMap, EditText mSearchText){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(DriverMapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            mMap.clear();
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0),mMap);
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation = (Location) task.getResult();
                            MarkerOptions options = new MarkerOptions()
                                    .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                    .title("Current Location")
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                            mMap.addMarker(options);
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location",mMap);
                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(DriverMapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title, GoogleMap mMap){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.driver_map);
        mapFragment.getMapAsync(DriverMapActivity.this);
        getRiderLocations();

    }
    private void getRiderLocations(){
        /*
        riderRequestList = mFirebaseHandler.getAvailableRiderRequest();
        for (int i = 0; i < riderRequestList.size(); i++){
            RideRequest rider = riderRequestList.get(i);
            riderLocationArrayAdapter.add(rider);
            GeoPoint startGeopoint = rider.getStartGeopoint();
            Log.d(TAG,rider.toString());
            LatLng rider_position = new LatLng(startGeopoint.getLatitude(), startGeopoint.getLongitude());
            MarkerOptions options = new MarkerOptions()
                    .position(rider_position)
                    .title(rider.getUserEmail())
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            mMap.addMarker(options);
        }
        */
        CollectionReference riderRef = mDatabase
                .collection("RiderRequests");
        mRiderListEventListener = riderRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!= null){
                    Log.e(TAG, "onEventRiderLocations: list failed");
                    return;
                }
                //rideRequestArrayList.clear();
                //rideRequestArrayList = new ArrayList<>();
                if (queryDocumentSnapshots!= null){
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        RideRequest rider = doc.toObject(RideRequest.class);
                        //rideRequestArrayList.add(rider);
                        if (rider.getStatus().equals("available")){
                            riderLocationArrayAdapter.add(rider);
                            GeoPoint startGeopoint = rider.getStartGeopoint();
                            //GeoPoint endGeopoint = rider.getEndGeopoint();
                            Log.d(TAG,rider.toString());
                            LatLng rider_position = new LatLng(startGeopoint.getLatitude(), startGeopoint.getLongitude());
                            MarkerOptions options = new MarkerOptions()
                                    .position(rider_position)
                                    .title(rider.getCostString())
                                    .snippet(rider.getRideInformation())
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                            mMap.addMarker(options);
                        }
                    }
                    Log.d(TAG,"onEventRiderLocations: size is : "+ rideRequestArrayList.size());
                }
            }
        });

    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTitle().equals("Current Location")){
            marker.hideInfoWindow();
        }
        else{
            LatLng pickUpLocation = marker.getPosition();
            drawPolyline(pickUpLocation);
            final AlertDialog.Builder builder = new AlertDialog.Builder(DriverMapActivity.this);
            builder.setMessage(marker.getSnippet())
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }
    public void drawPolyline(LatLng latLng){
        LatLng start = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(start,latLng)
                .width(20).color(ContextCompat.getColor(DriverMapActivity.this,R.color.quantum_cyan)).geodesic(true));
        line.setClickable(true);
    }

    @Override
    public void onRequestAccepted(Bundle request_info) {
        beginRideFragment = new BeginRideFragment();
        beginRideFragment.setArguments(request_info);
        riderList.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , 0, 100);
        linearLayoutContainer.removeView(mapContainer);
        linearLayoutContainer.addView(mapContainer, params);
        getSupportFragmentManager().beginTransaction().add(R.id.driver_status_container, beginRideFragment).commit();
    }

    @Override
    public void onBeginRide(Bundle request_info) {
        driverStatusFragment = new DriverStatusFragment();
        driverStatusFragment.setArguments(request_info);
        getSupportFragmentManager().beginTransaction().remove(beginRideFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.driver_status_container,driverStatusFragment).commit();

    }

    @Override
    public void onRideComplete() {
        getSupportFragmentManager().beginTransaction().remove(driverStatusFragment).commit();

    }




    /*
    @Override
    public void onPolylineClick(Polyline polyline) {
        Log.d("POLYLINE","HIIII");
        polyline.setColor(ContextCompat.getColor(DriverMapActivity.this,R.color.quantum_cyan));
        polyline.setZIndex(1);
    }
     */
}
