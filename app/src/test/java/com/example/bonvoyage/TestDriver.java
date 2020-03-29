package com.example.bonvoyage;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDriver {
    private Driver mockDriver() {
        return new Driver("FooDriver", "BarDriver", "foobarDriver@gmail.com",
                "17803214321", "foobarpassword", 1000f);
    }
    @Test
    public void testFirstName(){
        Driver driver = mockDriver();
        assertEquals("FooDriver", driver.getFirstname());
    }
    @Test
    public void testLastName(){
        Driver driver = mockDriver();
        assertEquals("BarDriver", driver.getLastname());
    }
    @Test
    public void testEmailAddress(){
        Driver driver = mockDriver();
        assertEquals("foobarDriver@gmail.com", driver.getEmail());
    }
    @Test
    public void testPhoneNumber(){
        Driver driver = mockDriver();
        assertEquals("17803214321", driver.getPhonenumber());
    }
    @Test
    public void testPassword(){
        Driver driver = mockDriver();
        assertEquals("foobarpassword", driver.getPassword());
    }
    @Test
    public void testPasswordLen() {
        Driver driver = mockDriver();
        assertEquals(14, driver.getPassword().length());
    }
    @Test
    public void testWallet(){
        Driver driver = mockDriver();
        assertEquals(1000, driver.getWallet());
    }
}
