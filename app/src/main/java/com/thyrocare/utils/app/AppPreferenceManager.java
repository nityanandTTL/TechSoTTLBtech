package com.thyrocare.utils.app;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thyrocare.models.api.request.SetBtechAvailabilityAPIRequestModel;
import com.thyrocare.models.api.response.LoginResponseModel;
import com.thyrocare.models.api.response.SelfieUploadResponseModel;
import com.thyrocare.models.data.SlotModel;

import java.util.ArrayList;


public class AppPreferenceManager {

    private AppPreference appPreference;
    private String maskNumber = "maskNumber";
    private String AppVersion = "app_version";
    private String APISessionKey = "ApiSessionKey";
    private String userId = "userId";
    private String totalDistance = "totalDistance";
    private String latitude = "latitude";
    private String longitude = "longitude";
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
    private String isAChatRgister = "isAChatRgister";
    private String ChatPassword = "ChatPassword";
    private String scheduleCounter2 = "scheduleCounter2";
    private String scheduleDate2 = "scheduleDate2";

    private String scheduleCounter = "scheduleCounter";
    private String scheduleDate = "scheduleDate";

    private String loginRole = "loginRole";
    private String btechID = "btechID";
    private String userID = "userID";
    private String userNAME = "username";
    private String userChatWith = "userChatWith";
    private String UserDetailUserName = "userDetailUserName";
    private String UserDetailChatWith = "userDetailChatWith";


    //Neha G---------------------------------
    private String ShowTimeInNotificatn="";
    private int not_avail_tom;
    private int Day_aftr_tom;



    private int delay;
    private int DataInVisitModel;
    private  int NotifyOrderone ;
    private int OrderAccept;

//Neha G -------------------------------------

    public int getOrderAccept() {
        return OrderAccept;
    }

    public void setOrderAccept(int orderAccept) {
        OrderAccept = orderAccept;
    }

    public String getShowTimeInNotificatn() {
        return ShowTimeInNotificatn;
    }

    public void setShowTimeInNotificatn(String showTimeInNotificatn) {
        ShowTimeInNotificatn = showTimeInNotificatn;
    }

    public int getNot_avail_tom() {
        return not_avail_tom;
    }

    public void setNot_avail_tom(int not_avail_tom) {
        this.not_avail_tom = not_avail_tom;
    }

    public int getDay_aftr_tom() {
        return Day_aftr_tom;
    }

    public void setDay_aftr_tom(int day_aftr_tom) {
        Day_aftr_tom = day_aftr_tom;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDataInVisitModel() {
        return DataInVisitModel;
    }

    public void setDataInVisitModel(int dataInVisitModel) {
        DataInVisitModel = dataInVisitModel;
    }

    public int getNotifyOrderone() {
        return NotifyOrderone;
    }

    public void setNotifyOrderone(int notifyOrderone) {
        NotifyOrderone = notifyOrderone;
    }

    public String getUserDetailUserName() {
        return appPreference.getString(userNAME, "");
    }

    public void setUserDetailUserName(String userDetailUserName) {
        appPreference.putString(this.userNAME, userDetailUserName);
    }

    public String getUserDetailChatWith() {
        return appPreference.getString(userChatWith, "");
    }

    public void setUserDetailChatWith(String userDetailChatWith) {
        appPreference.putString(this.userChatWith, userDetailChatWith);
    }

    public String getUserID() {
        return appPreference.getString(userID, "");
    }

    public void setUserID(String userID) {
        appPreference.putString(this.userID, userID);
    }

    public String getBtechID() {
        return appPreference.getString(btechID, "");
    }

    public void setBtechID(String btechID) {
        appPreference.putString(this.btechID, btechID);
    }

    public String getLoginRole() {
        return appPreference.getString(loginRole, "");
    }

    public void setLoginRole(String loginRole) {
        appPreference.putString(this.loginRole, loginRole);
    }

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
    public boolean isChatRegister() {
        return appPreference.getBoolean(this.isAChatRgister, false);
    }

    public void setChatRegister(boolean value) {
        appPreference.putBoolean(this.isAChatRgister, value);
    }
    public String getScheduleCounter2() {
        return scheduleCounter2;
    }

    public void setScheduleCounter2(String scheduleCounter2) {
        this.scheduleCounter2 = scheduleCounter2;
    }

    public String getScheduleDate2() {
        return scheduleDate2;
    }

    public void setScheduleDate2(String scheduleDate2) {
        this.scheduleDate2 = scheduleDate2;
    }

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

    //jai
    public String getLatitude() {
        return appPreference.getString(latitude, "");
    }

    public void setLatitude(String latitude) {
        appPreference.putString(this.latitude, latitude);
    }

    public String getLongitude() {
        return appPreference.getString(longitude, "");
    }

    public void setLongitude(String longitude) {
        appPreference.putString(this.longitude, longitude);
    }

    //jai
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
    public String getChatPassword() {
        return appPreference.getString(ChatPassword, "");
    }

    public void setChatPassword(String password) {
        appPreference.putString(this.ChatPassword, password);
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