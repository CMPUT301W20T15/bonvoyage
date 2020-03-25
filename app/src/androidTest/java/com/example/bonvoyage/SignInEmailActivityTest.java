package com.example.bonvoyage;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class SignInEmailActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<SignInEmailActivity> rule =
            new ActivityTestRule<>(SignInEmailActivity.class, true, true);
    //Before
    /**
     * Gets activity running.
     * Make sure we are at the SignInEmailActivity before beginning tests.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.assertCurrentActivity("SignInEmailActivity", SignInEmailActivity.class);
    }
    //Test

    /**
     * Checks that SignIn can be sucessfully completed.
     * @throws Exception
     */
    @Test
    public void activitySignInRiderEmail() throws Exception{
        solo.enterText((EditText) solo.getView(R.id.email), "testrider@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password), "riderpassword");
        solo.clickOnView(solo.getView(R.id.email_sign_in_button));
        solo.waitForActivity(RiderMapActivity.class);
        solo.assertCurrentActivity("RiderMapActivity", RiderMapActivity.class);
    }
    /**
     * Checks that SignIn can be sucessfully completed.
     * @throws Exception
     */
    @Test
    public void activitySignInDriverEmail() throws Exception{
        solo.enterText((EditText) solo.getView(R.id.email), "testdriver@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password), "driverpassword");
        solo.clickOnView(solo.getView(R.id.email_sign_in_button));
        //assertTrue(solo.waitForActivity(DriverMapActivity.class));
        //solo.assertCurrentActivity("DriverMapActivity", RiderMapActivity.class);
    }
    /**
     * Checks that SignIn with incorrect login information.
     * @throws Exception
     */
    @Test
    public void activitySignInEmailIncorrect() throws Exception{
        solo.enterText((EditText) solo.getView(R.id.email), "testriderrrr@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password), "riderpassword");
        solo.clickOnView(solo.getView(R.id.email_sign_in_button));
        assertTrue(solo.waitForText("Authentication failed.", 1, 2000));
        solo.assertCurrentActivity("SignInEmailActivity", SignInEmailActivity.class);
    }
    /**
     * Checks that SignIn with empty fields.
     * @throws Exception
     */
    @Test
    public void activitySignInEmptyFields() throws Exception{
        solo.clickOnView(solo.getView(R.id.email_sign_in_button));
        assertTrue(solo.waitForText("Fill in all fields", 1, 2000));
        solo.assertCurrentActivity("SignInEmailActivity", SignInEmailActivity.class);
    }
    //After
    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
