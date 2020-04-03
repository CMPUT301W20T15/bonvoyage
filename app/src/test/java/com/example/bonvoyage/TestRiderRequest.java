package com.example.bonvoyage;

import android.os.Bundle;
import android.os.Parcel;

import com.example.bonvoyage.models.RideRequest;
import com.google.firebase.firestore.GeoPoint;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * UnitTests to check RideRequest (uses Parcelable).
 * RideRequest is the request sent to the driver once a rider confirms the terms of their ride.
 */
public class TestRiderRequest {
    private RideRequest mockRideRequest(){
        GeoPoint startLocation = new GeoPoint(53.5549,-113.5141);
        GeoPoint endLocation = new GeoPoint(53.5232,-113.5263);
        Date timestamp = Calendar.getInstance().getTime();
        String userEmail = "bob@gmail.com";
        String status = "available";
        String phoneNumber = "17801234567";
        String firstName = "Bob";
        String lastName = "Bobby";
        float cost = (float) 20.01;
        return new RideRequest(startLocation,endLocation,timestamp,userEmail,status,
                phoneNumber,firstName,lastName,cost);
    }
    @Test
    public void TestGeopoint() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals(new GeoPoint(53.5549,-113.5141),rideRequest.getStartGeopoint());
        assertEquals(new GeoPoint(53.5232,-113.5263),rideRequest.getEndGeopoint());
    }
    @Test
    public void TestSetStartGeopoint() {
        RideRequest rideRequest = mockRideRequest();
        rideRequest.setStartGeopoint(new GeoPoint(53.5550,-113.5131));
        assertEquals(new GeoPoint(53.5550,-113.5131),rideRequest.getStartGeopoint());
    }
    @Test
    public void TestSetEndGeopoint() {
        RideRequest rideRequest = mockRideRequest();
        rideRequest.setEndGeopoint((new GeoPoint(53.5550,-113.5131)));
        assertEquals(new GeoPoint(53.5550,-113.5131),rideRequest.getEndGeopoint());
    }
    @Test
    public void TestUserEmail() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals("bob@gmail.com",rideRequest.getUserEmail());
    }
    @Test
    public void TestSetUserEmail() {
        RideRequest rideRequest = mockRideRequest();
        rideRequest.setUserEmail("jenny@email.com");
        assertEquals("jenny@email.com",rideRequest.getUserEmail());
    }
    @Test
    public void TestStatus() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals("available",rideRequest.getStatus());
    }
    @Test
    public void TestSetStatus() {
        RideRequest rideRequest = mockRideRequest();
        rideRequest.setStatus("cancelled");
        assertEquals("cancelled",rideRequest.getStatus());
    }
    @Test
    public void TestPhoneNumber() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals("17801234567",rideRequest.getPhoneNumber());
    }
    @Test
    public void TestSetPhoneNumber() {
        RideRequest rideRequest = mockRideRequest();
        rideRequest.setPhoneNumber("+1234567890");
        assertEquals("+1234567890",rideRequest.getPhoneNumber());
    }
    @Test
    public void TestFirstName() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals("Bob",rideRequest.getFirstName());
    }
    @Test
    public void TestSetFirstName() {
        RideRequest rideRequest = mockRideRequest();
        rideRequest.setFirstName("Bobby");
        assertEquals("Bobby",rideRequest.getFirstName());
    }
    @Test
    public void TestLastName() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals("Bobby",rideRequest.getLastName());
    }
    @Test
    public void TestSetLastName() {
        RideRequest rideRequest = mockRideRequest();
        rideRequest.setLastName("Smith");
        assertEquals("Smith",rideRequest.getLastName());
    }
    @Test
    public void TestCost() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals((float)20.01,rideRequest.getCost());
    }
    @Test
    public void TestSetCost() {
        RideRequest rideRequest = mockRideRequest();
        rideRequest.setCost((float)10.01);
        assertEquals((float)10.01,rideRequest.getCost());
    }
    @Test
    public void TestGetFullName(){
        RideRequest rideRequest = mockRideRequest();
        assertEquals("Bob Bobby",rideRequest.getFullName());
    }
    @Test
    public void TestGetCostString(){
        RideRequest rideRequest = mockRideRequest();
        assertEquals("Cost: 20.01",rideRequest.getCostString());
    }
    @Test
    public void TestGetRideInformation(){
        RideRequest rideRequest = mockRideRequest();
        assertEquals("Bob Bobby\nCost: 20.01\nEmail: bob@gmail.com\nPhone: 17801234567",rideRequest.getRideInformation());
    }
    @org.junit.Test
    public void testParcelable() {
        RideRequest foo = mockRideRequest();
        Bundle bundle = new Bundle();
        bundle.putParcelable("foo", foo);
        RideRequest parceledFoo = bundle.getParcelable("foo");
        assertEquals(foo, foo);
    }
}
