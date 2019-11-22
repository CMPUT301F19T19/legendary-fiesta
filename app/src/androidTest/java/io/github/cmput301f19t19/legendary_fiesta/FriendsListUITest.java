package io.github.cmput301f19t19.legendary_fiesta;

import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import io.github.cmput301f19t19.legendary_fiesta.ui.FragmentEmptyClass;
import io.github.cmput301f19t19.legendary_fiesta.ui.FriendsFragment;

import static org.awaitility.Awaitility.await;

@RunWith(AndroidJUnit4.class)
public class FriendsListUITest {

    private FriendsFragment fragment;
    private User user;

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

        fragment = new FriendsFragment();
        fragment.setArguments(bundle);

        mainActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(1, fragment, null).commit();
    }

}
