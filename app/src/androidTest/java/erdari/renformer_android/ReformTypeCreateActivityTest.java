package erdari.renformer_android;

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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;

/**
 * Interface tests for the ReformTypeCreateActivity.
 *
 * @author Ricard Pinilla Barnes
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ReformTypeCreateActivityTest {

    /**
     * Sets up the class to be tested.
     */
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(ReformTypeCreateActivity.class);

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
        onView(withId(R.id.reformNameBox)).check(matches(isDisplayed()));
        onView(withId(R.id.reformPriceBox)).check(matches(isDisplayed()));
        onView(withId(R.id.reformCreateButton)).check(matches(isDisplayed()));
        onView(withId(R.id.reformCreateButton)).check(matches(isClickable()));
    }

    /**
     * Tests whether the fields are able to get the keyboard imputs.
     *
     * @author Ricard Pinilla Barnes
     */
    @Test
    public void inputToCreateItemIsAllowed() {
        onView(withId(R.id.reformNameBox))
                .perform(typeText("Test reform type"), closeSoftKeyboard());
        onView(withId(R.id.reformPriceBox))
                .perform(typeText("10.5"), closeSoftKeyboard());
        onView(withId(R.id.reformCreateButton))
                .perform(click());
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
