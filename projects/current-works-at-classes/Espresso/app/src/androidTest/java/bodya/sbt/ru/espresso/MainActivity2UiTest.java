package bodya.sbt.ru.espresso;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivity2UiTest {
    @Rule
    public ActivityTestRule<Main2Activity> rule = new ActivityTestRule<>(Main2Activity.class);

    @Test
    public void testResultButton() {
        onView(withId(R.id.result_text_view)).check(matches(withText("")));
        onView(withId(R.id.first_edit_text)).perform(typeText("Hello "));
        onView(withId(R.id.second_edit_text)).perform(typeText("Sberbank"));
        onView(withId(R.id.result_button)).perform(click());
        onView(withId(R.id.result_text_view)).check(matches(withText(not(""))));
        onView(withId(R.id.result_text_view)).check(matches(withText("Hello Sberbank")));
    }
}

