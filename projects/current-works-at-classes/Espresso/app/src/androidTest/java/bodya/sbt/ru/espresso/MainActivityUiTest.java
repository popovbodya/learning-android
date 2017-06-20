package bodya.sbt.ru.espresso;


import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityUiTest {
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testActionButton() {
        onView(withId(R.id.text_view_click)).check(matches(withText(R.string.hello_world)));
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.text_view_click)).check(matches(withText(R.string.hello_sber)));
    }

    @Test
    public void testActionButtonLong() {
        onView(withId(R.id.text_view_long_click)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button)).perform(longClick());
        onView(withId(R.id.text_view_long_click)).check(matches(isDisplayed()));
    }
}
