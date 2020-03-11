package com.example.bonvoyage;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SignUpActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<LoginSignupActivity> rule =
            new ActivityTestRule<>(LoginSignupActivity.class, true, true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        solo.assertCurrentActivity("Wrong Activity", LoginSignupActivity.class);
        solo.clickOnButton("SIGN UP");
    }

    @Test
    public void checkActivitySwitched(){
        solo.assertCurrentActivity("Activity not switched.", SignUpActivity.class);
    }

    @Test
    public void checkBlankSpace(){
        solo.enterText((EditText) solo.getView(R.id.signUpFirstName), "");
        solo.clickOnButton("CONFIRM REGISTRATION");
        assertTrue(solo.waitForText("All subject fields must be filled in.", 1, 2000));
    }

    @Test
    public void checkValidPhoneNumber(){
        solo.enterText((EditText) solo.getView(R.id.signUpFirstName), "a");
        solo.enterText((EditText) solo.getView(R.id.signUpLastName), "a");
        solo.enterText((EditText) solo.getView(R.id.signUpEmail), "a");
        solo.enterText((EditText) solo.getView(R.id.signUpPassword), "aaaaaaa");

        solo.enterText((EditText) solo.getView(R.id.signUpPhoneNumber), "123");
        solo.clickOnButton("CONFIRM REGISTRATION");
        assertTrue(solo.waitForText("Invalid Phone Number.", 1, 2000));

        solo.enterText((EditText) solo.getView(R.id.signUpPhoneNumber), "78012344321234");
        solo.clickOnButton("CONFIRM REGISTRATION");
        assertTrue(solo.waitForText("Invalid Phone Number.", 1, 2000));
    }

    @Test
    public void checkValidPassword(){
        solo.enterText((EditText) solo.getView(R.id.signUpFirstName), "a");
        solo.enterText((EditText) solo.getView(R.id.signUpLastName), "a");
        solo.enterText((EditText) solo.getView(R.id.signUpEmail), "a");
        solo.enterText((EditText) solo.getView(R.id.signUpPhoneNumber), "7804445555");

        solo.enterText((EditText) solo.getView(R.id.signUpPassword), "test");
        solo.clickOnButton("CONFIRM REGISTRATION");
        assertTrue(solo.waitForText("Password length must be greater than 6.", 1, 2000));
    }

    @Test
    public void checkBackButton(){
        solo.clickOnButton("GO BACK TO LOGIN SCREEN");
        solo.assertCurrentActivity("Back Button not working", LoginSignupActivity.class);

    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
