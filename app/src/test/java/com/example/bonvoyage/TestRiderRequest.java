package com.example.bonvoyage;

import android.os.Bundle;

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
    //Test comment
    @Test
    public void TestGeopoint() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals(new GeoPoint(53.5549,-113.5141),rideRequest.getStartGeopoint());
        assertEquals(new GeoPoint(53.5232,-113.5263),rideRequest.getEndGeopoint());
    }
    @Test
    public void TestUserEmail() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals("bob@gmail.com",rideRequest.getUserEmail());
    }
    @Test
    public void TestStatus() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals("available",rideRequest.getStatus());
    }
    @Test
    public void TestPhoneNumber() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals("17801234567",rideRequest.getPhoneNumber());
    }
    @Test
    public void TestFirstName() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals("Bob",rideRequest.getFirstName());
    }
    @Test
    public void TestLastName() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals("Bobby",rideRequest.getLastName());
    }
    @Test
    public void TestCost() {
        RideRequest rideRequest = mockRideRequest();
        assertEquals((float)20.01,rideRequest.getCost());
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
