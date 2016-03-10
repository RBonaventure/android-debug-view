package com.rbonaventure.debug;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 *  DebugView implementation.
 *  Show a custom view to help you debug your application.
 */
public class DebugView extends TextView {

    private static final String TAG = "DebugView";

    private boolean mTextInitialized = false;
    private boolean mBackgroundInitialized = false;

    public DebugView(Context context) {
        this(context, null);
    }

    public DebugView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Object debuggable = getBuildConfigValue(context, "DEBUG");

        /**
         * Only show the view if the application is debuggable
         */
        if (debuggable != null && (boolean) debuggable) {
            initializeView(context);
        } else {
            setVisibility(GONE);
        }

    }

    /**
     * Initialize the view
     * @param context the context
     */
    private void initializeView(Context context) {
        initializeText(context);
        initializeBackground(context);
    }

    /**
     * Set the text
     * @param context the context
     */
    private void initializeText(Context context) {
        String versionName;
        int versionCode;

        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
            versionCode = info.versionCode;
            String text = String.format(context.getString(R.string.format),
                    context.getString(R.string.density),
                    versionName,
                    versionCode);

            setText(text);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage());
        }

        mTextInitialized = true;
    }

    /**
     * Set the background, and its color according to the android:textColor attribute
     * @param context the context
     */
    private void initializeBackground(Context context) {
        Drawable background;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            background = context.getDrawable(R.drawable.border_radius);
        } else {
            background = context.getResources().getDrawable(R.drawable.border_radius);
        }
        background.setColorFilter(new PorterDuffColorFilter(getCurrentTextColor(), PorterDuff.Mode.MULTIPLY));
        if(Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(background);
        } else {
            setBackgroundDrawable(background);
        }

        mBackgroundInitialized = true;
    }

    /**
     * Gets a field from the project's BuildConfig. This is useful when, for example, flavors
     * are used at the project level to set custom fields.
     *
     * @param context   Used to find the correct file
     * @param fieldName The name of the field-to-access
     * @return The value of the field, or {@code null} if the field is not found.
     */
    public static Object getBuildConfigValue(Context context, String fieldName) {
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
     * Prevent the text from being changed
     * text ref android.R.styleable#TextView_text
     * type ref android.R.styleable#TextView_bufferType
     */
    @Override
    public void setText(CharSequence text, BufferType type) {
        if(!mTextInitialized) {
            super.setText(text, type);
        }
    }

    /**
     * Prevent the background from being changed
     * @param background The Drawable to use as the background, or null to remove the background
     */
    @Override
    public void setBackground(Drawable background) {
        if(!mBackgroundInitialized) {
            super.setBackground(background);
        }
    }
    /**
     * Enforce layout params value
     * @param params The layout parameters for this view, cannot be null
     */
    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        super.setLayoutParams(params);
    }

}