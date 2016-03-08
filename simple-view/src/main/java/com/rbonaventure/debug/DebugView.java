package com.rbonaventure.debug;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Field;

public class DebugView extends TextView {

    private static final String TAG = "DebugView";

    public DebugView(Context context) {
        super(context);
    }

    public DebugView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        Object debuggable = getBuildConfigValue(context, "DEBUG");

        if (debuggable != null && (boolean) debuggable) {
            String versionName = "";
            int versionCode = 0;

            try {
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                versionName = info.versionName;
                versionCode = info.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                Log.d(TAG, e.getMessage());
            }

            String version = versionName + " (" + versionCode + ")";

            String text = context.getString(R.string.density);
            text += " ";
            text += version;

            setText(text);
            setBackgroundResource(R.drawable.border_radius);
        } else {
            setVisibility(GONE);
        }
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
}