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
    public void testViewVisibility(){
        assertThat(mDebugView).isNotVisible();
    }

    @Test
    public void testSetVisibility(){
        mDebugView.setVisibility(View.VISIBLE);
        assertThat(mDebugView).isGone();
        mDebugView.setVisibility(View.INVISIBLE);
        assertThat(mDebugView).isGone();
        mDebugView.setVisibility(View.GONE);
        assertThat(mDebugView).isGone();
    }

    @Test
    public void testViewDisabled(){
        assertThat(mDebugView).isDisabled();
    }

    @Test
    public void testSetEnabled(){
        mDebugView.setEnabled(true);
        assertThat(mDebugView).isDisabled();
    }

}