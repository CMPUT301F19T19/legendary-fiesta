package io.github.cmput301f19t19.legendary_fiesta;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.cmput301f19t19.legendary_fiesta.ui.AddPostFragment;
import io.github.cmput301f19t19.legendary_fiesta.ui.FragmentEmptyClass;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AddPostTest {

    private AddPostFragment fragment;

    @Rule
    public ActivityTestRule<FragmentEmptyClass> mainActivityRule = new ActivityTestRule<>(FragmentEmptyClass.class);

    //Launch AddPostFragment before every test
    @Before
    public void init(){
        fragment = new AddPostFragment();
        mainActivityRule.getActivity().getSupportFragmentManager().beginTransaction().add(1, fragment, null)
                .commit();
    }

    @Test
    public void LaunchFragmentTest(){
        onView(withId(R.id.emotionRadioGroup)).check(matches(isDisplayed()));
    }

}
