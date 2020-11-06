package com.thyrocare.btechapp.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.SplashActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.NotificationUtils;

import java.util.HashMap;

/**
 * Created by E4904 on 4/24/2017.
 */

public class FireMsgService extends FirebaseMessagingService {

    private static final String TAG = "FireMsgService";
    private int screenCategory = 0;
    private AppPreferenceManager appPreferenceManager;
    private NotificationUtils notificationUtils;

    @Override
    public void onNewToken(@NonNull String tkn) {
        super.onNewToken(tkn);

        MessageLogger.LogDebug("FireIDServicechat","Token ["+tkn+"]");

        storeRegIdInPref(tkn);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent("demokey");
        registrationComplete.putExtra("token", tkn);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Firebase Pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Token", token);
        editor.putString("ServerToken", "no");
        editor.apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        MessageLogger.LogError("onReceive","");

        if (remoteMessage == null)
            return;

// Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            MessageLogger.LogError("Error", "Notification Body: "+remoteMessage.getNotification().getBody());
        }

// Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            MessageLogger.LogError("Error", "Data Payload: "+remoteMessage.getData().toString());
            try {
                HashMap<String, String> notificationData = new HashMap<>();
                notificationData.putAll(remoteMessage.getData());
                handleDataMessage(notificationData);
            } catch (Exception e) {
                MessageLogger.LogError("Error", "Exception: "+e.getMessage());
            }
        }


    }

    private void handleDataMessage(HashMap<String, String> json) {
        try {
            String title = json.get("title");
            String message = json.get("message");
            String onGoing = json.get("onGoing");
            String autoCancel = json.get("autoCancel");
            String bigText = json.get("bigText");
            String imageUrl = json.get("image");
            String timestamp = String.valueOf(System.currentTimeMillis());
            String NotificationID = json.get("Screen_Category");
            String Product_name = json.get("Product_name");
            String userId = json.get("userid");
            String App_ID = json.get("App_ID");
            screenCategory = Integer.parseInt(json.get("Screen_Category"));

            MessageLogger.LogError(TAG, "Screen_category ----->" + screenCategory);

            if (!TextUtils.isEmpty(App_ID) && App_ID.equalsIgnoreCase(String.valueOf(AppConstants.BTECH_APP_ID))) {
                Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                resultIntent.putExtra("isFromNotification", true);
                resultIntent.putExtra("screenCategory", screenCategory);


                appPreferenceManager = new AppPreferenceManager(this);
                if (appPreferenceManager.getAPISessionKey() != null) {
                    String userid = appPreferenceManager.getBtechID();
                    if (!TextUtils.isEmpty(userId)) {
                        if (userId.equalsIgnoreCase(userid)) {
                            if (TextUtils.isEmpty(imageUrl)) {
                                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent, NotificationID, onGoing, autoCancel, bigText, screenCategory, Product_name);
                            } else {
                                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl, NotificationID, onGoing, autoCancel, bigText, screenCategory, Product_name);
                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent, NotificationID, onGoing, autoCancel, bigText, screenCategory, Product_name);
                        } else {
                            showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl, NotificationID, onGoing, autoCancel, bigText, screenCategory, Product_name);
                        }
                    }
                }
            }
        } catch (Exception e) {
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
