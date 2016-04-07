package com.rbonaventure.debug.sample;

import android.os.Build;
import android.view.View;

import com.rbonaventure.debug.DebugView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.android.api.Assertions.assertThat;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants   = BuildConfig.class,
        sdk         = Build.VERSION_CODES.LOLLIPOP)
public class DebugViewReleaseTest {

    MainActivity mActivity;
    DebugView mDebugView;

    @Before
    public void setup()  {
        mActivity = Robolectric.setupActivity(MainActivity.class);
        mDebugView = (DebugView) mActivity.findViewById(R.id.default_view);
    }

    @Test
    public void testInflateView(){
        // FIXME the view shouldn't be inflated in release mode
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView is null.")
                .isNotNull();
    }

    @Test
    public void testViewVisibility(){
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView is visible.")
                .isNotVisible();
    }

    @Test
    public void testSetVisibility(){
        mDebugView.setVisibility(View.VISIBLE);
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView's visibility changed.")
                .isGone();

        mDebugView.setVisibility(View.INVISIBLE);
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView's visibility changed.")
                .isGone();

        mDebugView.setVisibility(View.GONE);
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView's visibility changed.")
                .isGone();
    }

    @Test
    public void testViewDisabled(){
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView is enabled.")
                .isDisabled();
    }

    @Test
    public void testSetEnabled(){
        mDebugView.setEnabled(true);
        assertThat(mDebugView).isDisabled();
    }

}