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
public class SignInPhoneActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<SignInPhoneActivity> rule =
            new ActivityTestRule<>(SignInPhoneActivity.class, true, true);
    //Before
    /**
     * Gets activity running.
     * Make sure we are at the SignInPhoneActivity before beginning tests.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.assertCurrentActivity("SignInPhoneActivity", SignInPhoneActivity.class);
    }
    //Test
    /**
     * Checks that SignIn can be sucessfully completed.
     * @throws Exception
     */
    @Test
    public void activitySignInPhone() throws Exception{
        solo.enterText((EditText) solo.getView(R.id.editTextPhone), "11234567890");
        solo.clickOnView(solo.getView(R.id.phoneContinue));
        solo.waitForActivity(SMSActivity.class);
        solo.assertCurrentActivity("SMSActivity", SMSActivity.class);
    }
    /**
     * Checks that SignIn can be sucessfully completed.
     * @throws Exception
     */
    @Test
    public void activitySignInPhoneEmpty() throws Exception{
        solo.clickOnView(solo.getView(R.id.phoneContinue));
        assertTrue(solo.waitForText("Valid Number is Required", 1, 2000));
        solo.assertCurrentActivity("SignInPhoneActivity", SignInPhoneActivity.class);
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
