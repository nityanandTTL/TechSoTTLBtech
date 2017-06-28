package com.dhb.utils.app;

import android.app.Activity;
import android.content.Context;

import com.dhb.models.api.request.SetBtechAvailabilityAPIRequestModel;
import com.dhb.models.api.response.LoginResponseModel;
import com.dhb.models.api.response.SelfieUploadResponseModel;
import com.dhb.models.data.SlotModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class AppPreferenceManager {

    private AppPreference appPreference;
    private String maskNumber = "maskNumber";
    private String AppVersion = "app_version";
    private String APISessionKey = "ApiSessionKey";
    private String userId = "userId";
    private String totalDistance = "totalDistance";
    private String totalEarnings = "totalEarnings";
    private String totalKits = "totalKits";
    private String loginTime = "loginTime";
    private String isAfterLogin = "isAfterLogin";
    private String isAppInBackground = "isAppInBackground";
    private String are_terms_and_conditions_accepted = "are_terms_and_conditions_accepted";
    private String selfieResponse = "selfieResponse";
    private String loginResponse = "loginResponse";
    private String btechAvailabilityResponseModel = "btechAvailabilityResponseModel";
    private String selectedSlotsArr = "selectedSlotsArr";
    private String leaveFlag = "leaveFlag";
    private String leaveFromDate = "leaveFromDate";
    private String leaveToDate = "leaveToDate";
    private String cameFrom = "cameFrom";

    //changes_5june2017--START
    private String scheduleCounter = "scheduleCounter";
    private String scheduleDate = "scheduleDate";

    public String getMaskNumber() {
        return appPreference.getString(maskNumber, "");
    }

    public void setMaskNumber(String maskNumber) {
        appPreference.putString(this.maskNumber, maskNumber);
    }

    public String getScheduleDate() {
        return appPreference.getString(scheduleDate, "");
    }

    public void setScheduleDate(String scheduleDate) {
        appPreference.putString(this.scheduleDate, scheduleDate);
    }

    public String getScheduleCounter() {
        return appPreference.getString(scheduleCounter, "");
    }

    public void setScheduleCounter(String scheduleCounter) {
        appPreference.putString(this.scheduleCounter, scheduleCounter);
    }
    //changes_5june2017--END

    public AppPreferenceManager(Activity activity) {
        super();
        appPreference = AppPreference.getAppPreferences(activity);
    }

    public AppPreferenceManager(Context context) {
        appPreference = AppPreference.getAppPreferences(context);
    }

    public String getAppVersion() {
        return appPreference.getString(AppVersion, "");
    }

    public void setAppVersion(String appVersion) {
        appPreference.putString(this.AppVersion, appVersion);
    }

    public String getAPISessionKey() {
        return appPreference.getString(APISessionKey, "");
    }

    public void setAPISessionKey(String aPISessionKey) {
        appPreference.putString(this.APISessionKey, aPISessionKey);
    }

    public LoginResponseModel getLoginResponseModel() {
        String value = appPreference.getString(this.loginResponse, "");
        return new Gson().fromJson(value, LoginResponseModel.class);
    }

    public void setLoginResponseModel(LoginResponseModel loginResponseModel) {
        appPreference.putString(this.loginResponse, new Gson().toJson(loginResponseModel));
    }

    public SelfieUploadResponseModel getSelfieResponseModel() {
        String value = appPreference.getString(this.selfieResponse, "");
        return new Gson().fromJson(value, SelfieUploadResponseModel.class);
    }

    public void setSelfieResponseModel(SelfieUploadResponseModel selfieResponseModel) {
        appPreference.putString(this.selfieResponse, new Gson().toJson(selfieResponseModel));
    }

    public SetBtechAvailabilityAPIRequestModel getBtechAvailabilityAPIRequestModel() {
        String value = appPreference.getString(this.btechAvailabilityResponseModel, "");
        return new Gson().fromJson(value, SetBtechAvailabilityAPIRequestModel.class);
    }

    public void setBtechAvailabilityResponseModel(SetBtechAvailabilityAPIRequestModel setBtechAvailabilityAPIRequestModel) {
        appPreference.putString(this.btechAvailabilityResponseModel, new Gson().toJson(setBtechAvailabilityAPIRequestModel));
    }

    public ArrayList<SlotModel> getSelectedSlotsArr() {
        String value = appPreference.getString(this.selectedSlotsArr, "");
        TypeToken<ArrayList<SlotModel>> tokenSlots = new TypeToken<ArrayList<SlotModel>>() {
        };
        return new Gson().fromJson(value, tokenSlots.getType());
    }

    public void setSelectedSlotsArr(ArrayList<SlotModel> slotsArr) {
        appPreference.putString(this.selectedSlotsArr, new Gson().toJson(slotsArr));
    }

    public AppPreference getAppPreference() {
        return appPreference;
    }

    public void setAppPreference(AppPreference appPreference) {
        this.appPreference = appPreference;
    }

    public String getLoginTime() {
        return appPreference.getString(loginTime, "");
    }

    public void setLoginTime(String loginTime) {
        appPreference.putString(this.loginTime, loginTime);
    }


    public String getTotalDistance() {
        return appPreference.getString(totalDistance, "");
    }

    public void setTotalDistance(String totalDistance) {
        appPreference.putString(this.totalDistance, totalDistance);
    }

    public String getTotalEarnings() {
        return appPreference.getString(totalEarnings, "");
    }

    public void setTotalEarnings(String totalEarnings) {
        appPreference.putString(this.totalEarnings, totalEarnings);
    }

    public String getTotalKits() {
        return appPreference.getString(totalKits, "");
    }

    public void setTotalKits(String totalKits) {
        appPreference.putString(this.totalKits, totalKits);
    }


    public String getLeaveFromDate() {
        return appPreference.getString(leaveFromDate, "");
    }

    public void setLeaveFromDate(String leaveFromDate) {
        appPreference.putString(this.leaveFromDate, leaveFromDate);
    }

    public String getLeaveToDate() {
        return appPreference.getString(leaveToDate, "");
    }

    public void setLeaveToDate(String leaveToDate) {
        appPreference.putString(this.leaveToDate, leaveToDate);
    }

    public int getLeaveFlag() {
        return appPreference.getInt(leaveFlag, 0);
    }

    public void setLeaveFlag(int leaveFlag) {
        appPreference.putInt(this.leaveFlag, leaveFlag);
    }


    public int getCameFrom() {
        return appPreference.getInt(cameFrom, 0);
    }

    public void setCameFrom(int cameFrom) {
        appPreference.putInt(this.cameFrom, cameFrom);
    }

    public void clearAllPreferences() {
        boolean termsAndConditionsAccepted = appPreference.getBoolean(this.are_terms_and_conditions_accepted, false);
        appPreference.clearPreferences();
        appPreference.putBoolean(this.are_terms_and_conditions_accepted, termsAndConditionsAccepted);
    }

    public boolean isAfterLogin() {
        return appPreference.getBoolean(this.isAfterLogin, false);
    }

    public void setIsAfterLogin(boolean value) {
        appPreference.putBoolean(this.isAfterLogin, value);
    }

    public boolean isAppInBackground() {
        return appPreference.getBoolean(this.isAppInBackground, false);
    }

    public void setIsAppInBackground(boolean isAppInBackground) {
        appPreference.putBoolean(this.isAppInBackground, isAppInBackground);
    }

    public boolean areTermsAndConditionsAccepted() {
        return appPreference.getBoolean(this.are_terms_and_conditions_accepted, false);
    }

    public void setAreTermsAndConditionsAccepted(boolean areTermsAndConditionsAccepted) {
        appPreference.putBoolean(this.are_terms_and_conditions_accepted, areTermsAndConditionsAccepted);
    }
}