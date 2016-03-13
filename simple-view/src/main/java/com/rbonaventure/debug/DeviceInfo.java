package com.rbonaventure.debug;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Created by rbonaventure on 3/11/2016.
 */
public class DeviceInfo {

    /**
     * The name of the device.
     */
    String mName = Build.DEVICE ;

    /**
     * The manufacturer of the device.
     */
    String mManufacturer = Build.MANUFACTURER;

    /**
     * The model of the device.
     */
    String mModel = Build.MODEL;

    /**
     * The brand of the device.
     */
    String mBrand = Build.BRAND;

    /**
     * The os version of the device.
     */
    String mVersion = Build.VERSION.RELEASE;

    /**
     * The resolution of the device.
     */
    String mResolution;

    /**
     * The density of the device (in dpi).
     */
    int mDensity;

    /**
     * The language of the device.
     */
    String mLanguage = Locale.getDefault().getLanguage();

    /**
     * The sdk level oft he device.
     */
    int mSdk;

    /**
     * String format of the toString method
     */
    private String mFormat;

    /**
     * Generate a set of information about the device.
     * @param context the context
     * @return the DeviceInfo object filled with in
     */
    public static DeviceInfo getInstance(Context context) {

        DeviceInfo deviceInfo = new DeviceInfo();

        deviceInfo.mFormat = context.getString(R.string.device_info_format);

        /**
         * Get a unique id for the device.
         */
        //deviceInfo.uniqueID = DeviceID.getID(context);

        /**
         * Get the sdk level of the device.
         */
        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {

            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                deviceInfo.mSdk = fieldValue;
            }
        }

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        windowManager.getDefaultDisplay().getMetrics(dm);

        /**
         * Get the resolution of the device.
         */
        deviceInfo.mResolution = dm.widthPixels + "x" + dm.heightPixels;

        /**
         * Get the density of the device.
         */
        deviceInfo.mDensity = dm.densityDpi;

        return deviceInfo;
    }

    /**
     * Get all the DeviceInfo as a String
     * @return the device information as a formatted String
     */
    public String toString() {
        return String.format(mFormat,
                format(mManufacturer),
                format(mBrand),
                format(mModel),
                mResolution,
                mDensity,
                mVersion,
                mSdk,
                mLanguage);
    }

    /**
     * Capitalize the first letter of a String
     * @param text the String to format
     * @return the formatted String
     */
    private String format(String text) {
        if(text != null && !text.isEmpty())
            return text.substring(0, 1).toUpperCase() + text.substring(1);
        else
            return "";
    }

}
