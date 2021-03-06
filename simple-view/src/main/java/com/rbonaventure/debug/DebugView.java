package com.rbonaventure.debug;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 *  DebugView implementation.
 *  Show a custom view to help you debug your application.
 *
 *  Created by rbonaventure on 3/7/2016.
 */
public class DebugView extends TextView implements View.OnClickListener {

    private static final String TAG = "DebugView";

    private boolean mInitialized = false;
    private AlertDialog mAlertDialog;

    public DebugView(Context context) {
        this(context, null);
    }

    public DebugView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(mInitialized)
            return;

        Object debuggable = getBuildConfigValue(context, "DEBUG");

        /**
         * Only show the view if the application is debuggable
         */
        if (debuggable != null && (boolean) debuggable) {
            initializeView(context);
        } else {
            setVisibility(GONE);
            setEnabled(false);
        }

        mInitialized = true;
    }

    /**
     * Initialize the view
     * @param context the context
     */
    private void initializeView(Context context) {
        initializeAlertDialog(context);
        initializeText(context);
        initializeBackground(context);
        setOnClickListener(this);
    }

    /**
     * Set the text of the AlertDialog
     * @param context the context
     */
    private void initializeAlertDialog(Context context) {
        Object flavor = getBuildConfigValue(context, "FLAVOR");
        Object buildType = getBuildConfigValue(context, "BUILD_TYPE");

        String mFlavor = flavor != null && !"".equals(flavor) ? (String) flavor : context.getString(R.string.no_flavor);
        String mBuildType = buildType != null ? (String) buildType : context.getString(R.string.no_build_type);
        String mDeviceInfo = DeviceInfo.getInstance(context).toString();

        String message = String.format(
                    context.getString(R.string.message_format),
                    mDeviceInfo,
                    mFlavor,
                    mBuildType);

        mAlertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.debug)
                .setMessage(message)
                .create();

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1){
            mAlertDialog.setIcon(R.drawable.ic_info_white);
        } else{
            mAlertDialog.setIcon(R.drawable.ic_info);
        }
    }

    /**
     * Set the text of the view
     * @param context the context
     */
    private void initializeText(Context context) {

        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String text = String.format(context.getString(R.string.text_format),
                    context.getString(R.string.density),
                    info.versionName,
                    info.versionCode);

            setText(text);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage());
        }

    }

    /**
     * Set the background of the view, and its color according to the android:textColor attribute
     * @param context the context
     */
    @SuppressLint("Deprecation")
    private void initializeBackground(Context context) {
        Drawable background;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            background = context.getDrawable(R.drawable.border_radius);
        } else {
            background = context.getResources().getDrawable(R.drawable.border_radius);
        }
        background.setColorFilter(new PorterDuffColorFilter(getCurrentTextColor(), PorterDuff.Mode.MULTIPLY));
        setBackgroundInternal(background);
    }

    /**
     * Gets a field from the project's BuildConfig. This is useful when, for example, flavors
     * are used at the project level to set custom fields.
     *
     * @param context   Used to find the correct file
     * @param fieldName The name of the field-to-access
     * @return The value of the field, or {@code null} if the field is not found.
     */
    private Object getBuildConfigValue(Context context, String fieldName) {
        try {
            Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");
            Field field = clazz.getField(fieldName);
            return field.get(null);
        } catch (ClassNotFoundException e) {
            Log.d(TAG, e.getMessage());
        } catch (NoSuchFieldException e) {
            Log.d(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.d(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * /!\ Disabled
     * Prevent the text from being changed
     * @param text ref android.R.styleable#TextView_text
     * @param type ref android.R.styleable#TextView_bufferType
     */
    @Override
    public void setText(CharSequence text, BufferType type) {
        if(!mInitialized) {
            super.setText(text, type);
        }
    }

    /**
     * Sets the text color for all the states (normal, selected,
     * focused) to be this color.
     * Also change the background color accordingly.
     * @param color the new text color
     */
    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        initializeBackground(getContext());
    }

    /**
     * Sets the text color.
     * Also change the background color accordingly.
     * @param colors the new text colors
     */
    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        initializeBackground(getContext());
    }

    /**
     * /!\ Disabled
     * Prevent errors to be set on the DebugView
     * @param error The text of the error
     * @param icon The icon to display
     */
    @Override
    public void setError(CharSequence error, Drawable icon) {
    }

    /**
     * /!\ Disabled
     * Prevent the background from being changed
     * @param background The Drawable to use as the background, or null to remove the background
     */
    @Override
    public void setBackground(Drawable background) {
    }

    /**
     * Set background from within the DebugView
     * @param background The Drawable to use as the background, or null to remove the background
     */
    private void setBackgroundInternal(Drawable background) {
        if(Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN) {
            super.setBackground(background);
        } else {
            super.setBackgroundDrawable(background);
        }
    }

    /**
     * /!\ Disabled
     * Prevent the visibility from being changed
     * @param visibility the visibility
     */
    @Override
    public void setVisibility(int visibility) {
        if(!mInitialized)
            super.setVisibility(visibility);
    }

    /**
     * /!\ Disabled
     * Prevent the view from being disabled in Debug mode
     *      and enabled in Release mode
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        if(!mInitialized)
            super.setEnabled(enabled);
    }

    /**
     * /!\ Disabled
     * Enforce layout params value
     * @param params The layout parameters for this view, cannot be null
     */
    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        super.setLayoutParams(params);
    }

    /**
     * Display an Alert Dialog showing the device information
     * @param v the view being touched
     */
    @Override
    public void onClick(View v) {
        mAlertDialog.show();
    }
}