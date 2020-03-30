package com.example.bonvoyage;


import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRider {
    private Rider mockRider() {
        return new Rider("FooRider", "BarRider", "foobarRider@gmail.com",
                "17801231234", "foobarpassword", 100f);
    }
    @Test
    public void testFirstName(){
        Rider rider = mockRider();
        assertEquals("FooRider", rider.getFirstname());
    }
    @Test
    public void testLastName(){
        Rider rider = mockRider();
        assertEquals("BarRider", rider.getLastname());
    }
    @Test
    public void testEmailAddress(){
        Rider rider = mockRider();
        assertEquals("foobarRider@gmail.com", rider.getEmail());
    }
    @Test
    public void testPhoneNumber(){
        Rider rider = mockRider();
        assertEquals("17801231234", rider.getPhonenumber());
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
    public void testWallet(){
        Rider rider = mockRider();
        assertEquals(100, rider.getWallet());
    }
}
