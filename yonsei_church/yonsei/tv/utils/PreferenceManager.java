package yonsei_church.yonsei.tv.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;

public class PreferenceManager {
    private static PreferenceManager instance;
    private Context mContext;
    private SharedPreferences mPreferences;

    private PreferenceManager(Context context) {
        this.mContext = context;
        this.mPreferences = context.getSharedPreferences("YONSEI", 0);
    }

    public static PreferenceManager getInstance(Context context) {
        if (context != null && (instance == null || !instance.mContext.equals(context.getApplicationContext()))) {
            instance = new PreferenceManager(context.getApplicationContext());
        }
        return instance;
    }

    public static PreferenceManager setInstance(Context context) {
        instance = new PreferenceManager(context.getApplicationContext());
        return instance;
    }

    public void putString(String str, String str2) {
        this.mPreferences.edit().putString(str, str2).apply();
    }

    public void putListString(String str, ArrayList<String> arrayList) {
        this.mPreferences.edit().putString(str, TextUtils.join("‚‗‚", (String[]) arrayList.toArray(new String[arrayList.size()]))).apply();
    }

    public void putBoolean(String str, boolean z) {
        this.mPreferences.edit().putBoolean(str, z).apply();
    }

    public void putInt(String str, int i) {
        this.mPreferences.edit().putInt(str, i).apply();
    }

    public void putFloat(String str, float f) {
        this.mPreferences.edit().putFloat(str, f).apply();
    }

    public void putLong(String str, long j) {
        this.mPreferences.edit().putLong(str, j).apply();
    }

    public void put(Bundle bundle) {
        SharedPreferences.Editor edit = this.mPreferences.edit();
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            if (obj instanceof Boolean) {
                edit.putBoolean(str, bundle.getBoolean(str));
            } else if (obj instanceof Integer) {
                edit.putInt(str, bundle.getInt(str));
            } else if (obj instanceof String) {
                edit.putString(str, bundle.getString(str));
            }
        }
        edit.apply();
    }

    public SharedPreferences getSharedPreferences() {
        return this.mPreferences;
    }

    public String getString(String str, String str2) {
        return this.mPreferences.getString(str, str2);
    }

    public ArrayList<String> getListString(String str) {
        return new ArrayList<>(Arrays.asList(TextUtils.split(this.mPreferences.getString(str, ""), "‚‗‚")));
    }

    public boolean getBoolean(String str, boolean z) {
        return this.mPreferences.getBoolean(str, z);
    }

    public int getInt(String str, int i) {
        return this.mPreferences.getInt(str, i);
    }

    public float getFloat(String str, float f) {
        return this.mPreferences.getFloat(str, f);
    }

    public long getLong(String str, long j) {
        return this.mPreferences.getLong(str, j);
    }

    public void remove(String str) {
        if (this.mPreferences != null) {
            this.mPreferences.edit().remove(str).apply();
        }
    }

    public void clear() {
        if (this.mPreferences != null) {
            this.mPreferences.edit().clear().apply();
        }
    }
}
