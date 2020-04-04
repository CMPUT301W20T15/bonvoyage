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
    public void testSetFirstName(){
        Driver driver = mockDriver();
        driver.setFirstname("Driver");
        assertEquals("Driver", driver.getFirstname());
    }
    @Test
    public void testLastName(){
        Driver driver = mockDriver();
        assertEquals("BarDriver", driver.getLastname());
    }
    @Test
    public void testSetLastName(){
        Driver driver = mockDriver();
        driver.setLastname("Bar");
        assertEquals("Bar", driver.getLastname());
    }
    @Test
    public void testEmailAddress(){
        Driver driver = mockDriver();
        assertEquals("foobarDriver@gmail.com", driver.getEmail());
    }
    @Test
    public void testSetEmailAddress(){
        Driver driver = mockDriver();
        driver.setEmail("IamDriver@gmail.com");
        assertEquals("IamDriver@gmail.com", driver.getEmail());
    }
    @Test
    public void testPhoneNumber(){
        Driver driver = mockDriver();
        assertEquals("17803214321", driver.getPhonenumber());
    }
    @Test
    public void testSetPhoneNumber(){
        Driver driver = mockDriver();
        driver.setPhonenumber("17804231234");
        assertEquals("17804231234", driver.getPhonenumber());
    }
    @Test
    public void testPassword(){
        Driver driver = mockDriver();
        assertEquals("foobarpassword", driver.getPassword());
    }
    @Test
    public void testSetPassword(){
        Driver driver = mockDriver();
        driver.setPassword("password123");
        assertEquals("password123", driver.getPassword());
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
    @Test
    public void testSetWallet(){
        Driver driver = mockDriver();
        driver.setWallet((float)20.01);
        assertEquals((float)20.01, driver.getWallet());
    }
}
