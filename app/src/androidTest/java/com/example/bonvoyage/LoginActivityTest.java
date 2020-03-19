package com.example.bonvoyage;
import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
public class LoginActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<LoginSignupActivity> rule =
            new ActivityTestRule<>(LoginSignupActivity.class, true, true);
    //Before
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.assertCurrentActivity("LoginActivityTest", LoginSignupActivity.class);
    }
    //Test
    /**
     * Checks that the intent can sucessfully switched to SignInEmailActivity,
     * if "SIGN IN WITH EMAIL" button is clicked.
     * @throws Exception
     */
    @Test
    public void activitySwitchEmail() throws Exception{
        solo.clickOnButton("SIGN IN WITH EMAIL");
        solo.assertCurrentActivity("Not in SignInEmailActivity", SignInEmailActivity.class);
    }
    /**
     * Checks that the intent can sucessfully switched to SignInPhoneActivity,
     * if "SIGN IN WITH PHONE" button is clicked.
     * @throws Exception
     */
    @Test
    public void activitySwitchPhone() throws Exception{
        solo.clickOnButton("SIGN IN WITH PHONE");
        solo.assertCurrentActivity("Not in SignInPhoneActivity", SignInPhoneActivity.class);
    }
    @Test
    /**
     * Checks that the intent can sucessfully switched to SignUpActivity,
     * if "SIGN UP AS A NEW USER" button is clicked.
     * @throws Exception
     */
    public void activitySwitchSignUp() throws Exception{
        solo.clickOnButton("SIGN UP AS A NEW USER");
        solo.assertCurrentActivity("Not in SignInEmailActivity", SignUpActivity.class);
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
