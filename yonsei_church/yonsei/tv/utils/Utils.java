package yonsei_church.yonsei.tv.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

public class Utils {
    public static String getMdn(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
                return "";
            }
            String line1Number = telephonyManager.getLine1Number();
            return (line1Number == null || line1Number.indexOf("+82") != 0) ? line1Number : line1Number.replaceFirst("\\+82", "0");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDeviceId(Context context) {
        if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0) {
            return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        }
        return null;
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException unused) {
            return "0";
        }
    }

    public static boolean hasPermission(Context context, String str) {
        return context.getPackageManager().checkPermission(str, context.getPackageName()) == 0;
    }

    public static int convertDpToPixel(Context context, int i) {
        return Math.round(((float) i) * context.getResources().getDisplayMetrics().density);
    }
}
