package com.example.bonvoyage;

import org.junit.Before;

import org.junit.Test;

import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.assertNull;

public class LoginActivityTest {
    private FirebaseHandler mFirebaseHandler;
    SignInEmailActivity mockedSignInEmailActivity;
    @Before
    public void before(){
        mFirebaseHandler = Mockito.mock(FirebaseHandler.class);
        mockedSignInEmailActivity = Mockito.mock(SignInEmailActivity.class);
    }
    @Test
    public void getSignedInUser(){
        mFirebaseHandler.loginUser("testride@gmail.com","testpassword",mockedSignInEmailActivity);
        assertNull(mFirebaseHandler.getCurrentUser());
    }

}
