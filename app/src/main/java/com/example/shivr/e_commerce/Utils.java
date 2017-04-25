package com.example.shivr.e_commerce;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;

/**
 * Created by shivr on 4/3/2017.
 */

public class Utils {
    private static String sharedPrefKey = "sharedPrefKey";
    private static String signInInfo = "signInInfo";
    public static String signedInBoolKey = "signedIn";
    public static String userSelectedLocationEnable = "userSelectLocationEnable";
    public static String geoFencesSet = "getFencesSet";

    public static SharedPreferences getSharedPrefs(Context context){
        return context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE);
    }

    public static void insertSharedPrefsBool(String key, boolean signedIn, SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, signedIn);
        editor.apply();
    }

    public static boolean checkBooleanSharedPrefs(SharedPreferences sharedPreferences, String key){
        return sharedPreferences.getBoolean(key, false);
    }

    public static void saveUserInfo(GoogleSignInAccount googleSignInAccount, SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(googleSignInAccount);
        editor.putString(signInInfo, json);
        editor.apply();
    }

    public static GoogleSignInAccount getUserInfo(SharedPreferences sharedPreferences){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(signInInfo, "");
        if (!json.equals(""))
            return gson.fromJson(json, GoogleSignInAccount.class);
        return null;
    }

    public static String getDecimalFormatForPrice(Double price){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return decimalFormat.format(price);
    }
}
