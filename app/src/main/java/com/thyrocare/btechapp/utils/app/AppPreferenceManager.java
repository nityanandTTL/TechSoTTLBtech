package com.thyrocare.btechapp.utils.app;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thyrocare.btechapp.models.api.request.SetBtechAvailabilityAPIRequestModel;
import com.thyrocare.btechapp.models.api.response.DSAProductsResponseModel;
import com.thyrocare.btechapp.models.api.response.FetchOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.api.response.LoginResponseModel;
import com.thyrocare.btechapp.models.api.response.NewBtechAvaliabilityResponseModel;
import com.thyrocare.btechapp.models.api.response.SelfieUploadResponseModel;
import com.thyrocare.btechapp.models.data.BrandTestMasterModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.models.data.SlotModel;

import java.util.ArrayList;
import java.util.Date;


public class AppPreferenceManager {

    public String ratecal = "ratecal";
    public String OTPFlagOneTime = "OTPFlag";
    public String Access_Token = "access_token";
    public String PE_Partner = "PE_Partner";
    public String PE_DSA = "PE_DSA";
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
    private String fetchOrderDetailsResponseModel = "fetchOrderDetailsResponseModel";
    private String orderVisitDetailsModel = "orderVisitDetailsModel";
    private String dsaProductResponse = "dsaProductResponse";
    private String btechAvailabilityResponseModel = "btechAvailabilityResponseModel";
    private String NEWBTECHAVALIABILITYRESPONSEMODEL = "NEWBTECHAVALIABILITYRESPONSEMODEL";
    private String selectedSlotsArr = "selectedSlotsArr";
    private String leaveFlag = "leaveFlag";
    private String leaveFromDate = "leaveFromDate";
    private String leaveToDate = "leaveToDate";
    private String cameFrom = "cameFrom";
    private String isAChatRgister = "isAChatRgister";
    private String ChatPassword = "ChatPassword";
    private String scheduleCounter2 = "scheduleCounter2";
    private String scheduleDate2 = "scheduleDate2";
    private String cashingTime = "cashingTime";
    private String CacheProduct = "CacheProduct";
    private String scheduleCounter = "scheduleCounter";
    private String scheduleDate = "scheduleDate";
    private String loginRole = "loginRole";
    private String btechID = "btechID";
    private String authToken = "authToken";
    private String userID = "userID";
    private String userNAME = "username";
    private String userChatWith = "userChatWith";
    private String UserDetailUserName = "userDetailUserName";
    private String UserDetailChatWith = "userDetailChatWith";
    private String isLoadSpotlightOnOrderd = "isLoadSpotlightOnOrderd";
    private String isLoadSpotlightOnOrderdHistory = "isLoadSpotlightOnOrderdHistory";
    private String isLoadSpotlightOnPager = "isLoadSpotlightOnPager";
    private String isLoadSpotlightOnHome = "isLoadSpotlightOnHome";
    //Neha G---------------------------------
    private String ShowTimeInNotificatn = "";
    private int not_avail_tom;
    private int Day_aftr_tom;


    private int delay;
    private int DataInVisitModel;
    private int NotifyOrderone;
    private int OrderAccept;
    private String selfieURl = "selfieURl";
    private String date = "date";
    private String OrderNo = "";

//Neha G -------------------------------------

    public AppPreferenceManager(Activity activity) {
        super();
        appPreference = AppPreference.getAppPreferences(activity);
    }

    public AppPreferenceManager(Context context) {
        appPreference = AppPreference.getAppPreferences(context);
    }

    public NewBtechAvaliabilityResponseModel getNEWBTECHAVALIABILITYRESPONSEMODEL() {
        String value = appPreference.getString(this.NEWBTECHAVALIABILITYRESPONSEMODEL, "");
        return new Gson().fromJson(value, NewBtechAvaliabilityResponseModel.class);
    }

    public void setNEWBTECHAVALIABILITYRESPONSEMODEL(NewBtechAvaliabilityResponseModel newbtechavaliabilityresponsemodel) {
        appPreference.putString(this.NEWBTECHAVALIABILITYRESPONSEMODEL, new Gson().toJson(newbtechavaliabilityresponsemodel));
    }

    public String getOrderNo() {
        return appPreference.getString(OrderNo, "");
    }

    public void setOrderNo(String orderNo) {
        appPreference.putString(this.OrderNo, orderNo);
    }

    public BrandTestMasterModel getTestsProfilePOPProfile() {
        String value = appPreference.getString(this.ratecal, "");
        return new Gson().fromJson(value, BrandTestMasterModel.class);
    }

    public void setTestsProfilePOPProfile(BrandTestMasterModel allTestsProfilePOPProfile) {
        appPreference.putString(this.ratecal, new Gson().toJson(allTestsProfilePOPProfile));
    }

    public String getBtechSelfie() {
        return appPreference.getString(selfieURl, "");
    }

    public void setBtechSelfie(String selfieURl) {
        appPreference.putString(this.selfieURl, selfieURl);
    }

    public boolean isPEPartner() {
        return appPreference.getBoolean(PE_Partner, false);
    }

    public void setPE_Partner(boolean PE_Partner) {
        appPreference.putBoolean(this.PE_Partner, PE_Partner);
    }

    public boolean PEDSAOrder() {
        return appPreference.getBoolean(PE_DSA, false);
    }

    public void setPE_DSA(boolean PE_DSA) {
        appPreference.putBoolean(this.PE_DSA, PE_DSA);
    }


    /*public NewBtechAvaliabilityResponseModel getNEWBTECHAVALIABILITYRESPONSEMODEL() {
        String value = appPreference.getString(this.NEWBTECHAVALIABILITYRESPONSEMODEL, "");
        return new Gson().fromJson(value, NewBtechAvaliabilityResponseModel.class);
    }

    public void setNEWBTECHAVALIABILITYRESPONSEMODEL(NewBtechAvaliabilityResponseModel newbtechavaliabilityresponsemodel) {
        appPreference.putString(this.NEWBTECHAVALIABILITYRESPONSEMODEL, new Gson().toJson(newbtechavaliabilityresponsemodel));
    }*/

    public String get7days() {
        return appPreference.getString(date, "");
    }

    public void set7date(String date) {
        appPreference.putString(this.date, date);
    }

    public String getCashingTime() {
        return appPreference.getString(cashingTime, "");
    }

    public void setCashingTime(String cashingTime) {
        appPreference.putString(this.cashingTime, cashingTime);
    }

    public String getOTPOnetime() {
        return appPreference.getString(OTPFlagOneTime, "");
    }

    public void setOTPFlagOneTime(String OTpflag) {
        appPreference.putString(this.OTPFlagOneTime, OTpflag);
    }

    public String getCacheProduct() {
        return appPreference.getString(CacheProduct, "");
    }

    public void setCacheProduct(String myObject) {
        appPreference.putString(this.CacheProduct, myObject);
    }

    public int getOrderAccept() {
        return OrderAccept;
    }

    public void setOrderAccept(int orderAccept) {
        OrderAccept = orderAccept;
    }

    public String getAuthToken() {
        return appPreference.getString(authToken, "");
    }

    public void setAuthToken(String btechID) {
        appPreference.putString(this.authToken, btechID);
    }

    public String getAccess_Token() {
        return appPreference.getString(Access_Token, "");
    }

    public void setAccess_Token(String access_Token) {
        appPreference.putString(this.Access_Token, access_Token);
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

    public com.thyrocare.btechapp.models.api.response.FetchOrderDetailsResponseModel getfetchOrderDetailsResponseModel() {
        String value = appPreference.getString(this.fetchOrderDetailsResponseModel, "");
        return new Gson().fromJson(value, FetchOrderDetailsResponseModel.class);
    }

    public void setfetchOrderDetailsResponseModel(com.thyrocare.btechapp.models.api.response.FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel) {
        appPreference.putString(this.fetchOrderDetailsResponseModel, new Gson().toJson(fetchOrderDetailsResponseModel));
    }

    public OrderVisitDetailsModel getorderVisitDetailsModel() {
        String value = appPreference.getString(this.orderVisitDetailsModel, "");
        return new Gson().fromJson(value, OrderVisitDetailsModel.class);
    }

    public void setorderVisitDetailsModel(OrderVisitDetailsModel orderVisitDetailsModel) {
        appPreference.putString(this.orderVisitDetailsModel, new Gson().toJson(orderVisitDetailsModel));
    }

    public DSAProductsResponseModel getDSAProductResponseModel() {
        String value = appPreference.getString(this.dsaProductResponse, "");
        return new Gson().fromJson(value, DSAProductsResponseModel.class);
    }

    public void setDSAProductResponseModel(DSAProductsResponseModel dsaProductsResponseModel) {
        appPreference.putString(this.dsaProductResponse, new Gson().toJson(dsaProductsResponseModel));
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

    public boolean isLoadSpotlightOnPager() {
        return appPreference.getBoolean(this.isLoadSpotlightOnPager, false);
    }

    public void setLoadSpotlightOnPager(boolean value) {
        appPreference.putBoolean(this.isLoadSpotlightOnPager, value);
    }

    public boolean isLoadSpotlightOnHome() {
        return appPreference.getBoolean(this.isLoadSpotlightOnHome, false);
    }

    public void setLoadSpotlightOnHome(boolean value) {
        appPreference.putBoolean(this.isLoadSpotlightOnHome, value);
    }

    public boolean isLoadSpotlightOnOrderdHistory() {
        return appPreference.getBoolean(this.isLoadSpotlightOnOrderdHistory, false);
    }

    public void setLoadSpotlightOnOrderdHistory(boolean value) {
        appPreference.putBoolean(this.isLoadSpotlightOnOrderdHistory, value);
    }

    public boolean isLoadSpotlightOnOrderd() {
        return appPreference.getBoolean(this.isLoadSpotlightOnOrderd, false);
    }

    public void setLoadSpotlightOnOrderd(boolean value) {
        appPreference.putBoolean(this.isLoadSpotlightOnOrderd, value);
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