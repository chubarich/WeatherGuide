package com.example.julia.weatherguide.ui.main;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.julia.weatherguide.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LocationNotInitializedShowsTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void locationNotInitializedShowsTest() {
        onView(withText(R.string.city_not_picked_description)).check(
            matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        );

        onView(withText(R.string.empty_view_string)).check(
            matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        );
    }
}
