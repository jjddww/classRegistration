package com.example.courseregistration.connection;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    public static final String PREFERENCES_NAME = "userPreference";
    private static final String DEFAULT_VALUE_STRING = "";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void setString(Context context, String key, String value) { //데이터 저장
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) { //로드
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);
        return value;
    }

    public static void removeKey(Context context, String key) { //저장데이터 삭제
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.commit();
    }
}
