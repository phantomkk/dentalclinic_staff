package com.dentalclinic.capstone.admin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.models.User;
import com.dentalclinic.capstone.admin.model.FingerAuthObj;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import okhttp3.ResponseBody;


/**
 * Created by lucky on 14-Sep-17.
 */

public class Utils {

    private static final String PREF_NAME = "ACCOUNT";
    private static final String STAFF_KEY = "STAFF_KEY";
    private static final String FINGER_AUTH_KEY = "FINGER_AUTH_KEY";
    private static final String PATIENTS_KEY = "PATIENTS_KEY";
    public static final String CURRENT_UNIT = "đ";
    public static final String STATUS_DONE = "Hoàn Thành";
    public static final String STATUS_NOT_DONE = "Hoàn Thành";
    public static final String PATTERN_DATE = "dd-MM-yyyy";
    public static final int DENTIST = 2;
    public static final int RECEIPTION = 3;
    public static String LIST_TREATMENT = "LIST_TREATMENT";
    public static String LIST_PAYMENT = "LIST_PAYMENT";
    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86");
    }
    public static String linkServer = "http://150.95.104.237/";

    public static void saveStaffInSharePref(Context context, Staff user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(STAFF_KEY, gson.toJson(user));
        editor.apply();
    }

    public static Staff getStaffInSharePref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonUser = sharedPreferences.getString(STAFF_KEY, null);
        Gson gson = new Gson();
        Staff u = gson.fromJson(jsonUser, Staff.class);
        return u;
    }

    public static void saveFingerAuthInSharePref(Context context, FingerAuthObj fingerAuthObj) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(FINGER_AUTH_KEY, gson.toJson(fingerAuthObj));
        editor.apply();
    }

    public static FingerAuthObj getFingerAuthInSharePref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonUser = sharedPreferences.getString(FINGER_AUTH_KEY, null);
        Gson gson = new Gson();
        FingerAuthObj fingerAuthObj = gson.fromJson(jsonUser, FingerAuthObj.class);
        return fingerAuthObj;
    }
    public static String getErrorMsg(ResponseBody responseBody) {
        try {
            if (responseBody != null) {
                JSONObject errorObject = new JSONObject(responseBody.string());
                if (errorObject != null) {
                    return errorObject.getString("error");
                } else {
                    Log.d(AppConst.DEBUG_TAG, "ResponseBody or ResponseBody.getErrorMsg() null");
                    return "";
                }
            } else {
                Log.d(AppConst.DEBUG_TAG, "ResponseBody or ResponseBody.string() null");
                return "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String upperCaseFirst(String value) {

        // Convert String to char array.
        char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);
        // Return string.
        return new String(array);
    }

    /// Tên thuốc: hello .......... 30 viên
    public static String getMedicineLine(String medicineName, int quantity, int numDot) {
        String dots = "";
        for (int i = 0; i < numDot - medicineName.length() - Integer.toString(quantity).length(); i++) {
            dots += ".";
        }
        return medicineName + " " + dots + " " + quantity + " viên";
    }

    public static String formatMoney(Long money) {
        NumberFormat formatter = new DecimalFormat("#,###");
//        double myNumber = 1000000;
        String formattedNumber = formatter.format(money);
//            return (String.format("%,d", money)).replace(',', '.');
//            return money.toString();
        return  formattedNumber;
    }

    public static <T> T parseJson(String source, Class<T> c) throws JsonSyntaxException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(source, c);
    }


    public static void setVNLocale(Context context) {
        Resources res = context.getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("vi_VN")); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }

    public static boolean isDentist(Context context){
        boolean rs = false;
        Staff staff = CoreManager.getStaff(context);
        if(staff.getRole() ==DENTIST){
            return true;
        }
        return rs;
    }
    public static boolean isRceiption(Context context){
        boolean rs = false;
        Staff staff = CoreManager.getStaff(context);
        if(staff.getRole()==RECEIPTION){
            return true;
        }
        return rs;
    }

    public static void subscribeReloadClinicAppointment() {
        FirebaseMessaging.getInstance().subscribeToTopic(AppConst.TOPIC_RELOAD_APPOINTMENT);
    }

    public static void unsubscribeReloadClinicAppointment() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(AppConst.TOPIC_RELOAD_APPOINTMENT);
    }
}
