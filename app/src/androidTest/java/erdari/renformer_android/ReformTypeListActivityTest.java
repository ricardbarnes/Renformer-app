package erdari.renformer_android;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import erdari.renformer_android.ui.user.admin.reformtype.ReformTypeCreateActivity;
import erdari.renformer_android.ui.user.admin.reformtype.ReformTypeListActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;

/**
 * Interface test for the ReformTypeListActivity.
 *
 * @author Ricard Pinilla Barnes
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ReformTypeListActivityTest {

    /**
     * Sets up the class to be tested.
     */
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(ReformTypeListActivity.class);

    /**
     * Sets up every needed element for the whole test.
     */
    @Before
    public void setUp() {
        Intents.init();
    }

    /**
     * Tests whether the static components of the screen are properly displayed.
     *
     * @author Ricard Pinilla Barnes
     */
    @Test
    public void screenStaticComponentsAreDisplayed() {
        onView(withId(R.id.coordinatorLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.app_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.backButtonView)).check(matches(isDisplayed()));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.fab)).check(matches(isDisplayed())); // Added FAB
    }

    @Test
    public void fabStartsReformTypeCreationActivity() {
        onView(withId(R.id.fab)).perform(click());
        mActivityRule.launchActivity(new Intent());
        intended(hasComponent(ReformTypeCreateActivity.class.getName()));
    }

    /**
     * Test whether pressing the back button (Home) destroys the activity.
     *
     * @author Ricard Pinilla Barnes
     */
    @Test
    public void clickBackButtonFinishesActivity() {
        Espresso.pressBackUnconditionally();
        assertTrue(mActivityRule.getActivity().isDestroyed());
    }

    /**
     * Tears down every common element set up in the @Before noted method.
     *
     * @author Ricard Pinilla Barnes
     */
    @After
    public void tearDown() {
        Intents.release();
    }

}
