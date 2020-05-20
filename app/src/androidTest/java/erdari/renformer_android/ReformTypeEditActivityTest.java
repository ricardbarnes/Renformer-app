package erdari.renformer_android;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import erdari.renformer_android.ui.user.admin.reformtype.ReformTypeEditActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Interface tests for the ReformTypeCreateActivity.
 *
 * @author Ricard Pinilla Barnes
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ReformTypeEditActivityTest {

    /**
     * Sets up the class to be tested.
     */
    @Rule
    public ActivityTestRule mActivityRule =
            new ActivityTestRule<>(ReformTypeEditActivity.class);

    /**
     * Tests whether the static components of the screen are properly displayed.
     *
     * @author Ricard Pinilla Barnes
     */
    @Test
    public void screenStaticComponentsAreDisplayed() {
        onView(withId(R.id.userIdLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.nameLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.nameBox)).check(matches(isClickable()));
        onView(withId(R.id.meterPriceBox)).check(matches(isDisplayed()));
        onView(withId(R.id.priceLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonEdit)).check(matches(isDisplayed()));
        onView(withId(R.id.deleteText)).check(matches(isDisplayed()));
    }

}
