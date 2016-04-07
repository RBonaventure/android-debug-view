package com.rbonaventure.debug.sample;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.rbonaventure.debug.DebugView;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

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
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView is null.")
                .isNotNull();
    }

    @Test
    public void testViewVisibility(){
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView isn't visible.")
                .isVisible();
    }

    @Test
    public void testSetVisibility(){
        mDebugView.setVisibility(View.VISIBLE);
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView's visibility changed.")
                .isVisible();

        mDebugView.setVisibility(View.INVISIBLE);
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView's visibility changed.")
                .isVisible();

        mDebugView.setVisibility(View.GONE);
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView's visibility changed.")
                .isVisible();
    }

    @Test
    public void testViewEnabled(){
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView isn't enabled.")
                .isEnabled();
    }

    @Test
    public void testSetEnabled(){
        mDebugView.setEnabled(false);
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView has been disabled.")
                .isEnabled();
    }

    @Test
    public void testVersionName(){
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView's text doesn't contain the versionName.")
                .containsText(BuildConfig.VERSION_NAME);
    }

    @Test
    public void testVersionCode(){
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView's text doesn't contain the versionCode.")
                .containsText(Integer.toString(BuildConfig.VERSION_CODE));
    }

    @Test
    public void testSetText(){
        String expectedText = mDebugView.getText().toString();
        mDebugView.setText("test");
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView's text changed.")
                .hasText(expectedText);
    }

    @Test
    public void testSetBackground(){
        Drawable expectedBackground = mDebugView.getBackground();
        mDebugView.setBackground(null);
        assertThat(mDebugView)
                .overridingErrorMessage("The DebugView's Background changed.")
                .hasBackground(expectedBackground);
    }

    @Test
    public void testOnClickDialog(){
        mDebugView.performClick();
        AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();

        assertThat(alertDialog)
                .overridingErrorMessage("The AlertDialog isn't showing.")
                .isNotNull();

        alertDialog.dismiss();
    }

    @Test
    public void testDialogMessageContainsBuildType() {
        mDebugView.performClick();
        AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(alertDialog);
        String message = shadowAlertDialog.getMessage().toString();

        Assertions.assertThat(message)
                .overridingErrorMessage("The AlertDialog's text doesn't contain the app's build-type.")
                .contains("debug");

        alertDialog.dismiss();
    }

    /* FIXME add a build flavor to test this
    @Test
    public void testDialogMessageContainsFlavor() {
        mDebugView.performClick();
        AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(alertDialog);
        String message = shadowAlertDialog.getMessage().toString();

        Assertions.assertThat(message)
                .overridingErrorMessage("The AlertDialog's text doesn't contain the build's flavor.")
                .contains(BuildConfig.FLAVOR);

        alertDialog.dismiss();
    } */

    /* FIXME find a way to test the device information
    @Test
    public void testDialogMessageContainsDeviceInformation() {
        mDebugView.performClick();
        AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(alertDialog);
        String message = shadowAlertDialog.getMessage().toString();

        Assertions.assertThat(message)
                .overridingErrorMessage("The AlertDialog's text doesn't contain the device brand.")
                .contains(Build.BRAND);
        Assertions.assertThat(message)
                .overridingErrorMessage("The AlertDialog's text doesn't contain the device brand.")
                .contains(Build.MANUFACTURER);
        Assertions.assertThat(message)
                .overridingErrorMessage("The AlertDialog's text doesn't contain the device brand.")
                .contains(Build.MODEL);

        alertDialog.dismiss();
    }*/

}