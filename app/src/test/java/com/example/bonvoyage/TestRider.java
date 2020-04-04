package com.example.bonvoyage;


import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRider {
    private Rider mockRider() {
        return new Rider("FooRider", "BarRider", "foobarRider@gmail.com",
                "17801231234", "foobarpassword", 100f);
    }
    @Test
    public void testLatLng(){
        Rider rider = mockRider();
        assertEquals("FooRider", rider.getFirstname());
    }
    @Test
    public void testFirstName(){
        Rider rider = mockRider();
        assertEquals("FooRider", rider.getFirstname());
    }
    @Test
    public void testSetFirstName(){
        Rider rider = mockRider();
        rider.setFirstname("Foo");
        assertEquals("Foo", rider.getFirstname());
    }
    @Test
    public void testLastName(){
        Rider rider = mockRider();
        assertEquals("BarRider", rider.getLastname());
    }
    @Test
    public void testSetLastName(){
        Rider rider = mockRider();
        rider.setLastname("Rider");
        assertEquals("Rider", rider.getLastname());
    }
    @Test
    public void testEmailAddress(){
        Rider rider = mockRider();
        assertEquals("foobarRider@gmail.com", rider.getEmail());
    }
    @Test
    public void testSetEmailAddress(){
        Rider rider = mockRider();
        rider.setEmail("rider@gmail.com");
        assertEquals("rider@gmail.com", rider.getEmail());
    }
    @Test
    public void testPhoneNumber(){
        Rider rider = mockRider();
        assertEquals("17801231234", rider.getPhonenumber());
    }
    @Test
    public void testSetPhoneNumber(){
        Rider rider = mockRider();
        rider.setPhonenumber("1234567890");
        assertEquals("1234567890", rider.getPhonenumber());
    }
    @Test
    public void testPassword(){
        Rider rider = mockRider();
        assertEquals("foobarpassword", rider.getPassword());
    }
    @Test
    public void testPasswordLen() {
        Rider rider = mockRider();
        assertEquals(14, rider.getPassword().length());
    }
    @Test
    public void testSetPassword(){
        Rider rider = mockRider();
        rider.setPassword("foo12345");
        assertEquals("foo12345", rider.getPassword());
    }
    @Test
    public void testWallet(){
        Rider rider = mockRider();
        assertEquals(100, rider.getWallet());
    }
    @Test
    public void testSetWallet(){
        Rider rider = mockRider();
        rider.setWallet((float)10.01);
        assertEquals((float)10.01, rider.getWallet());
    }
}
