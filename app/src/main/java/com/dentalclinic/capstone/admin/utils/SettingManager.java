package com.dentalclinic.capstone.admin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dentalclinic.capstone.admin.models.Setting;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

public class SettingManager {
    public static final String PREF_NAME = "SETTING";
    public static final String CUSTOM_SETTING_KEY = "CUSTOM_SETTING_KEY";
    public static final String PROMOTE = "YES";
    public static final String VIBRATE = "YES";

    public static void initSetting(Context context) {
        Setting setting = getSettingPref(context);
        if (setting == null) {
            setting = new Setting();
            setting.setPromote(true);
            setting.setVibrate(true);
            saveSetting(context, setting);
            Log.d(AppConst.DEBUG_TAG, "SAVE CUSTOM SETTING SUCCESS");
        }
        if (setting.isPromote()) {
            FirebaseMessaging.getInstance().subscribeToTopic(AppConst.TOPIC_PROMOTION);
        }else{
            FirebaseMessaging.getInstance().unsubscribeFromTopic(AppConst.TOPIC_PROMOTION);
        }
    }

    public static void saveSetting(Context context, Setting setting) {
        if (setting.isPromote()) {
            FirebaseMessaging.getInstance().subscribeToTopic(AppConst.TOPIC_PROMOTION);
        }else{
            FirebaseMessaging.getInstance().unsubscribeFromTopic(AppConst.TOPIC_PROMOTION);
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(CUSTOM_SETTING_KEY, gson.toJson(setting));
        editor.apply();
    }

    public static Setting getSettingPref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonStirng = sharedPreferences.getString(CUSTOM_SETTING_KEY, null);
        Gson gson = new Gson();
        Setting setting = gson.fromJson(jsonStirng, Setting.class);
        return setting;
    }
}
