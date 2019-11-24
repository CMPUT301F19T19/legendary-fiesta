package io.github.cmput301f19t19.legendary_fiesta;

import android.os.Bundle;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.cmput301f19t19.legendary_fiesta.ui.FragmentEmptyClass;
import io.github.cmput301f19t19.legendary_fiesta.ui.ProfileFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ProfileUITest {

    private ProfileFragment fragment;
    private User user; //keep reference of test user

    @Rule
    public ActivityTestRule<FragmentEmptyClass> mainActivityRule = new ActivityTestRule<>(FragmentEmptyClass.class);

    @Before
    public void init(){
        //wait for fragmentEmptyClass to finish retrieving test user from database
        await().until(()-> mainActivityRule.getActivity().getTestUser() != null);

        user = mainActivityRule.getActivity().getTestUser(); //get the test user;
        //put the user in a bundle and pass it to fragment object
        Bundle bundle = new Bundle();
        bundle.putParcelable("USER_PROFILE", user);

        fragment = new ProfileFragment();
        fragment.setArguments(bundle);

        mainActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(1, fragment, null).commit();
    }

    @Test
    public void LaunchFragmentTest(){
        //check if Profile fragment is launched correctly
        onView(withId(R.id.user_profile)).check(matches(isDisplayed()));
    }

    @Test
    public void UsernameTextTest(){
        //clear default username
        onView(withId(R.id.usernameEditText)).check(matches(isDisplayed())).perform(clearText());

        //basic text input
        onView(withId(R.id.usernameEditText)).check(matches(isDisplayed())).perform(typeText("Johnathan"));
        onView(withText("Johnathan")).check(matches(isDisplayed()));

        //clear entry
        onView(withId(R.id.usernameEditText)).check(matches(isDisplayed())).perform(clearText());

        //text with more than 15 chars
        onView(withId(R.id.usernameEditText)).perform(typeText("12345678901234567"));
        onView(withText("123456789012345")).check(matches(isDisplayed()));
    }

    @Test
    public void BirthDateTest(){
        onView(withId(R.id.birthEditText)).check(matches(isDisplayed())).perform(click());

        onView(withText("OK")).check(matches(isDisplayed())).perform(click());

        //check format
        EditText birthET = fragment.getView().findViewById(R.id.birthEditText);
        String birthText = birthET.getText().toString();

        //look for three '/' in dateText
        int slashCount = 0;
        for(int i = 0; i < birthText.length(); i++){
            if(birthText.charAt(i) == '/')
                slashCount++;
        }

        assertEquals(slashCount, 2);
    }

    @Test
    public void BioTextTest(){
        //clear text
        onView(withId(R.id.bioEditText)).check(matches(isDisplayed())).perform(clearText());

        //type more than 100 chars
        onView(withId(R.id.bioEditText)).perform(typeText("qVoUAdYfyioEM1iwGkxLSdySVRRNboK22ezDWFzSwtqQbABhyISTLDIwBAWO6eAxpO5sRjqoOOOGpNtw4HaAWaL2pucCeOrlXbHXc12312312"));

        //check if the first 100 char is there
        onView(withText("qVoUAdYfyioEM1iwGkxLSdySVRRNboK22ezDWFzSwtqQbABhyISTLDIwBAWO6eAxpO5sRjqoOOOGpNtw4HaAWaL2pucCeOrlXbHX")).check(matches(isDisplayed()));
    }

}
