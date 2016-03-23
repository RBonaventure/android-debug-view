package com.rbonaventure.debug.sample;

import android.graphics.drawable.Drawable;
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
public class DebugViewDebugTest {

    MainActivity mActivity;
    DebugView mDebugView;

    @Before
    public void setup()  {
        mActivity = Robolectric.setupActivity(MainActivity.class);
        mDebugView = (DebugView) mActivity.findViewById(R.id.default_view);
    }

    @Test
    public void testInflateView(){
        assertThat(mDebugView).isNotNull();
    }

    @Test
    public void testViewVisibility(){
        assertThat(mDebugView).isVisible();
    }

    @Test
    public void testSetVisibility(){
        mDebugView.setVisibility(View.VISIBLE);
        assertThat(mDebugView).isVisible();
        mDebugView.setVisibility(View.INVISIBLE);
        assertThat(mDebugView).isVisible();
        mDebugView.setVisibility(View.GONE);
        assertThat(mDebugView).isVisible();
    }

    @Test
    public void testViewEnabled(){
        assertThat(mDebugView).isEnabled();
    }

    @Test
    public void testSetEnabled(){
        mDebugView.setEnabled(false);
        assertThat(mDebugView).isEnabled();
    }

    @Test
    public void testVersionName(){
        assertThat(mDebugView).containsText(BuildConfig.VERSION_NAME);
    }

    @Test
    public void testVersionCode(){
        assertThat(mDebugView).containsText(Integer.toString(BuildConfig.VERSION_CODE));
    }

    @Test
    public void testSetText(){
        String expectedText = mDebugView.getText().toString();
        mDebugView.setText("test");
        assertThat(mDebugView).hasText(expectedText);
    }

    @Test
    public void testSetBackground(){
        Drawable expectedBackground = mDebugView.getBackground();
        mDebugView.setBackground(null);
        assertThat(mDebugView).hasBackground(expectedBackground);
    }
/*

    @Test
    public void testOnClickDialog(){
        mDebugView.performClick();
        AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        assertThat(alertDialog).isNotNull();
    }

    @Test
    public void testDialogTitle(){
        mDebugView.performClick();
        AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        assertThat(alertDialog).isNotNull();
        ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(alertDialog);
        Assert.assertEquals(shadowAlertDialog.getTitle(), "Debug");
    }
*/

}