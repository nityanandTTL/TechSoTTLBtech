package com.thyrocare.btechapp.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;
import com.clevertap.android.sdk.pushnotification.NotificationInfo;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.LoginActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.SplashActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.InputUtils;
import com.thyrocare.btechapp.utils.app.NotificationUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by E4904 on 4/24/2017.
 */

public class FireMsgService extends FirebaseMessagingService {

    private static final String TAG = "FireMsgService";
    CleverTapAPI cleverTapAPI;
    private AppPreferenceManager appPreferenceManager;
    private NotificationUtils notificationUtils;
    private int Screen_category = 0;

    @Override
    public void onNewToken(@NonNull String tkn) {
        super.onNewToken(tkn);
        cleverTapAPI = CleverTapAPI.getDefaultInstance(getApplicationContext());
        cleverTapAPI.pushFcmRegistrationId(tkn, true);

        MessageLogger.LogDebug("FireIDServicechat", "Token [" + tkn + "]");

        storeRegIdInPref(tkn);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent("demokey");
        registrationComplete.putExtra("token", tkn);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Firebase_Pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Token", token);
        editor.putString("ServerToken", "no");
        editor.apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        MessageLogger.LogError("onReceive", "");

        if (remoteMessage == null)
            return;

// Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            MessageLogger.PrintMsg("Notification Body: " + remoteMessage.getNotification().getBody());
        }
// Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            MessageLogger.LogError("Error", "Data Payload: " + remoteMessage.getData().toString());
            Bundle extras = new Bundle();
            for (Map.Entry entry : remoteMessage.getData().entrySet()) {
                extras.putString(entry.getKey().toString(), entry.getValue().toString());
            }
            NotificationInfo notificationInfo = CleverTapAPI.getNotificationInfo(extras);
            if (notificationInfo.fromCleverTap) {
                CleverTapAPI.createNotification(getApplicationContext(), extras);
                CleverTapAPI cleverTapAPI = CleverTapAPI.getDefaultInstance(getApplicationContext());
                cleverTapAPI.setCTPushNotificationListener(new CTPushNotificationListener() {
                    @Override
                    public void onNotificationClickedPayloadReceived(HashMap<String, Object> payload) {
                        Constants.isFromCleverTap = true;
                       /* Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                        getApplication().startActivity(resultIntent);*/
                    }
                });
            } else {
                try {
                    HashMap<String, String> notificationData = new HashMap<>();
                    notificationData.putAll(remoteMessage.getData());
                    handleDataMessage(notificationData);
                } catch (Exception e) {
                    MessageLogger.LogError("Error", "Exception: " + e.getMessage());
                }
            }
        }


    }

    private void handleDataMessage(HashMap<String, String> json) {
        appPreferenceManager = new AppPreferenceManager(getApplicationContext());
        MessageLogger.PrintMsg("push json: " + json.toString());

        try {
            String title = json.get("title");
            String message = json.get("message");
            String onGoing = json.get("onGoing");
            String autoCancel = json.get("autoCancel");
            String bigText = json.get("bigText");
            String imageUrl = json.get("image");
//            String timestamp = json.get("timestamp");
            String timestamp = String.valueOf(System.currentTimeMillis());
            String notifyID = json.get("notifyID");
            String NotificationID = json.get("Screen_Category");
            String Product_name = json.get("Product_name");
            String Order_ID = json.get("Order_ID");
            String mobile = json.get("userid");
            String ScreenURL = json.get("ScreenURL");
            String AppID = json.get("App_ID");
            Screen_category = InputUtils.parseInt(json.get("Screen_Category"));
            MessageLogger.PrintMsg("title: " + title);
            MessageLogger.PrintMsg("message: " + message);
            MessageLogger.PrintMsg("onGoing: " + onGoing);
            MessageLogger.PrintMsg("autoCancel: " + autoCancel);
            MessageLogger.PrintMsg("bigText: " + bigText);
            MessageLogger.PrintMsg("imageUrl: " + imageUrl);
            MessageLogger.PrintMsg("timestamp: " + timestamp);
            MessageLogger.PrintMsg("Screen_category: " + Screen_category);
            MessageLogger.PrintMsg("ScreenURL: " + ScreenURL);
            MessageLogger.PrintMsg("Product_name: " + Product_name);
            MessageLogger.PrintMsg("Order_ID: " + Order_ID);


            if (InputUtils.CheckEqualIgnoreCase(AppID, Constants.StrAppID)) {
                if (Screen_category == Constants.LogoutID) {
                    LogUserOut();
                }
                InitaiteDataForNotification(notifyID, title, message, onGoing, autoCancel, bigText, imageUrl, timestamp, NotificationID, Product_name, Order_ID, mobile, ScreenURL);
            } else {
                MessageLogger.PrintMsg("This Notification is not for ThyroApp");
            }


        } catch (Exception e) {
            MessageLogger.PrintMsg("Exception: " + e.getMessage());
        }
    }

    private void LogUserOut() {
        try {
            appPreferenceManager.clearAllPreferences();
            try {
                new DhbDao(getApplicationContext()).deleteTablesonLogout();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent n = new Intent(getApplicationContext(), LoginActivity.class);
            n.setAction(Intent.ACTION_MAIN);
            n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            n.putExtra("ScreenCategory", 100);
            startActivity(n);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void InitaiteDataForNotification(String notifyID, String title, String message, String onGoing, String autoCancel, String bigText, String imageUrl, String timestamp, String notificationID, String product_name, String order_ID, String mobile, String ScreenURL) {

        Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        resultIntent.putExtra("isFromNotification", true);
        resultIntent.putExtra("notifyID", notifyID);
        resultIntent.putExtra("Screen_URL", ScreenURL);
        if (Screen_category == 0) {
            resultIntent.putExtra("screenCategory", Screen_category);
        } else if (Screen_category == 118) {
            resultIntent.putExtra("screenCategory", 118);
        } else if (Screen_category == Constants.LogoutID) {
            resultIntent.putExtra("screenCategory", Screen_category);
        } else {
            resultIntent.putExtra("screenCategory", 0);
        }

        AppPreferenceManager appPreferenceManager = new AppPreferenceManager(getApplicationContext());
        String loginMobileNumber = "" + appPreferenceManager.getLoginResponseModel().getUserID();
        if (Screen_category != Constants.LogoutID) {
            if (!InputUtils.isNull(mobile)) {
                if (InputUtils.CheckEqualIgnoreCase(mobile, loginMobileNumber)) {
                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent, notificationID, onGoing, autoCancel, bigText, Screen_category, product_name);
                    } else {
                        showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl, notificationID, onGoing, autoCancel, bigText, Screen_category, product_name);
                    }
                }
            } else {
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent, notificationID, onGoing, autoCancel, bigText, Screen_category, product_name);
                } else {
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl, notificationID, onGoing, autoCancel, bigText, Screen_category, product_name);
                }

            }
        }

    }


    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent, String notificationID, String onGoing, String autoCancel, String bigText, int Screen_category, String Product_name) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, notificationID, onGoing, autoCancel, bigText, Screen_category, Product_name);
    }

    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl, String notificationID, String onGoing, String autoCancel, String bigText, int Screen_category, String Product_name) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl, notificationID, onGoing, autoCancel, bigText, Screen_category, Product_name);
    }


}
