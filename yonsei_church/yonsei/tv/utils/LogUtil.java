package yonsei_church.yonsei.tv.utils;

import android.util.Log;

public class LogUtil {
    public static String TAG = "Yonsei_Church";
    public static boolean outputLog = false;

    public static void d(String str) {
        if (outputLog) {
            Log.d(TAG, debugInfo() + str);
        }
    }

    public static void i(String str) {
        if (outputLog) {
            Log.i(TAG, debugInfo() + str);
        }
    }

    public static void e(String str) {
        if (outputLog) {
            Log.e(TAG, debugInfo() + str);
        }
    }

    public static void w(String str) {
        if (outputLog) {
            Log.w(TAG, debugInfo() + str);
        }
    }

    public static void e(Exception exc) {
        if (outputLog) {
            Log.e(TAG, debugInfo() + exc.toString());
        }
    }

    public static void w(Exception exc) {
        if (outputLog) {
            Log.w(TAG, debugInfo() + exc.toString());
        }
    }

    private static String debugInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String className = stackTrace[4].getClassName();
        int lineNumber = stackTrace[4].getLineNumber();
        Thread currentThread = Thread.currentThread();
        return "[" + currentThread.getName() + "][" + className + ":" + lineNumber + "]";
    }
}
