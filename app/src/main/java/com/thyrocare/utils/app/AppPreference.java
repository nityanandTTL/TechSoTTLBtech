package com.thyrocare.utils.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.thyrocare.R;
import com.thyrocare.utils.api.Logger;
import com.google.gson.Gson;

public class AppPreference {

    private static AppPreference appPreferences;

    private Context applicationContext;
    private Gson gson;
    private SharedPreferences sharedPreferences;

    private AppPreference(Context applicationContext) {

        this.applicationContext = applicationContext;
        Logger.debug("applicationContext: " + applicationContext);
        Resources res = this.applicationContext.getResources();
        // Logger.debug("res: "+res);
        gson = new Gson();
        String preferencesName = res.getString(R.string.app_name);
        sharedPreferences = this.applicationContext.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }

    public static AppPreference getAppPreferences(Context applicationContext) {

        if (appPreferences != null) {
            return appPreferences;
        }
        appPreferences = new AppPreference(applicationContext);
        return appPreferences;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }


    public void clearPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    /*public void putVersionInfo(String key, VersionInfo versionInfo){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(versionInfo);
        editor.putString(key, json);
        editor.commit();
    }

    public VersionInfo getVersionInfo(String key){
        String json = sharedPreferences.getString(key,"");
        VersionInfo versionInfo =  gson.fromJson(json,VersionInfo.class);
        return versionInfo;
    }

    public void putThyrocareLoginInfoModel(String key, ThyrocareLoginInfoModel thyrocareLoginInfoModel){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(thyrocareLoginInfoModel);
        editor.putString(key, json);
        editor.commit();
    }

    public ThyrocareLoginInfoModel getThyrocareLoginInfoModel(String key){
        String json = sharedPreferences.getString(key,"");
        ThyrocareLoginInfoModel thyrocareLoginInfoModel =  gson.fromJson(json,ThyrocareLoginInfoModel.class);
        return thyrocareLoginInfoModel;
    }


    public void putWOERequest(String key, DHBThyrocareWindUpModel wOERequst){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(wOERequst);
        editor.putString(key, json);
        editor.commit();
    }

    public DHBThyrocareWindUpModel getWOERequest(String key){
        String json = sharedPreferences.getString(key,"");
        DHBThyrocareWindUpModel dHBWorkOrderEntryAPIResponseModel =  gson.fromJson(json,DHBThyrocareWindUpModel.class);
        return dHBWorkOrderEntryAPIResponseModel;
    }
*/
}
