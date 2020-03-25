package com.example.bonvoyage;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

///**
// * Instrumented test, which will execute on an Android device.
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//@RunWith(AndroidJUnit4.class)
//public class ExampleInstrumentedTest {
//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//
//        assertEquals("com.example.bonvoyage", appContext.getPackageName());
//    }
//}
import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
public class ExampleTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.example.ExampleActivty";

    private static Class<?> launcherActivityClass;
    static{
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public ExampleTestTest() throws ClassNotFoundException {
        super(launcherActivityClass);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testRun() {
        //Wait for activity: 'com.example.ExampleActivty'
        solo.waitForActivity("ExampleActivty", 2000);
        //Clear the EditText editText1
        solo.clearEditText((android.widget.EditText) solo.getView("editText1"));
        solo.enterText((android.widget.EditText) solo.getView("editText1"), "This is an example text");
    }
}