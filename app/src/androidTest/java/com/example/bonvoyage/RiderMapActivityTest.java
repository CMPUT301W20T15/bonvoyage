package com.example.bonvoyage;

import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.test.espresso.action.KeyEventAction;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.GoogleMap;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class RiderMapActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<RiderMapActivity> rule =
            new ActivityTestRule<>(RiderMapActivity.class, true, true);

    //Before
    /**
     * Gets activity running.
     * Make sure we are at the SignInEmailActivity before beginning tests.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.assertCurrentActivity("Not RiderMap", RiderMapActivity.class);
    }

    @Test
    public void mapOnReadyTest() throws Exception{
        solo.assertCurrentActivity("Not RiderMap", RiderMapActivity.class);
        solo.waitForView(R.id.rider_layout);
        solo.waitForFragmentById(R.id.map_rider);

    }

//    @Test
//    public void setCurrentLocationTest() throws Exception{
//        solo.assertCurrentActivity("Not RiderMap", RiderMapActivity.class);
//    }

    @Test
    public void destinationLocationBoxTest() throws Exception{
        solo.assertCurrentActivity("Not RiderMap", RiderMapActivity.class);
        solo.waitForView(R.id.endLocation);
        solo.clickOnEditText(1);
        solo.enterText((EditText) solo.getView(R.id.endLocation),"Charleston Campus");
        solo.pressSoftKeyboardDoneButton();
        assertTrue(solo.waitForText("Charleston Campus", 1, 2000));

    }

    @Test
    public void markerDropAtDestinationTest() throws Exception{
        solo.assertCurrentActivity("Not RiderMap", RiderMapActivity.class);
        solo.waitForView(R.id.endLocation);
        solo.clickOnEditText(1);
        solo.enterText((EditText) solo.getView(R.id.endLocation),"Charleston Campus");
        solo.pressSoftKeyboardGoButton();
        assertTrue(solo.waitForText("Charleston Campus", 1, 2000));
        solo.waitForView(R.id.continueButton);
        solo.clickOnButton("CONTINUE");
        solo.waitForFragmentById(R.id.rider_add_price);
    }
}
