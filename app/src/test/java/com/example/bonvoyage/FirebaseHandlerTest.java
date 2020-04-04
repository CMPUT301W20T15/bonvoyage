package com.example.bonvoyage;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;

import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;


import static junit.framework.TestCase.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FirebaseHandlerTest {
    private FirebaseHandler mFirebaseHandler;
    private static FirebaseHandler instance = null;
    private String TAG = "Firebase";
    private static FirebaseFirestore db;
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Driver driver;
    @Before
    public void before(){
        mFirebaseHandler = Mockito.mock(FirebaseHandler.class);
        driver = Mockito.mock(Driver.class);
        driver.setEmail("testrider@gmail.com");
    }
    @Test
    public void TestGetDriverRef(){
        assertNull(mFirebaseHandler.getDriverRef("testdriver@gmail.com"));
    }
    @Test
    public void TestWalletTransaction(){
        mFirebaseHandler.driverTransaction(driver,(float)0);
        assertEquals((float)0,mFirebaseHandler.getRiderWallet("testrider@gmail.com"));
    }
    @Test
    public void TestWalletUpdate(){
        mFirebaseHandler.updateRiderWallet("testrider@gmail.com",(float)0);
        assertEquals((float)0,mFirebaseHandler.getRiderWallet("testrider@gmail.com"));
    }


}
