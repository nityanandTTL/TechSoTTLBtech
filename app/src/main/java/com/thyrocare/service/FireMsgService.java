package com.thyrocare.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.SplashScreenActivity;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.NotificationUtils;

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
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("onReceive","");

        if (remoteMessage == null)
            return;

// Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e("Error", "Notification Body: "+remoteMessage.getNotification().getBody());
        }

// Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e("Error", "Data Payload: "+remoteMessage.getData().toString());
            try {
                HashMap<String, String> notificationData = new HashMap<>();
                notificationData.putAll(remoteMessage.getData());
                handleDataMessage(notificationData);
            } catch (Exception e) {
                Log.e("Error", "Exception: "+e.getMessage());
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
//            String timestamp = json.get("timestamp");
            String timestamp = String.valueOf(System.currentTimeMillis());
            String NotificationID = json.get("Screen_Category");
            String Product_name = json.get("Product_name");
            String Order_ID = json.get("Order_ID");
            String userId = json.get("userid");
            String App_ID = json.get("App_ID");
            screenCategory = Integer.parseInt(json.get("Screen_Category"));

            Log.e(TAG, "Screen_category ----->" + screenCategory);

            if (!TextUtils.isEmpty(App_ID) && App_ID.equalsIgnoreCase(String.valueOf(AppConstants.BTECH_APP_ID))) {
                Intent resultIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                resultIntent.putExtra("isFromNotification", true);
                resultIntent.putExtra("screenCategory", screenCategory);

                  /* if (Screen_category == 0) {
                    resultIntent.putExtra("Screen_category", Screen_category);
                } else if (Screen_category > 0 && Screen_category <= 100) {
                    if (!TextUtils.isEmpty(Order_ID)) {
                        resultIntent.putExtra("TechsoID", Screen_category);
                        resultIntent.putExtra("Screen_category", Constants.SCR_1);
                        resultIntent.putExtra("Order_ID", Order_ID);
                    } else {
                        resultIntent.putExtra("Screen_category", 0);
                    }
                } else if (Screen_category > 100) {
                    int position = Screen_category - 100;
                    resultIntent.putExtra("Screen_category", position);
                    if (position == Constants.SCR_1) {
                        resultIntent.putExtra("Order_ID", Order_ID);
                    }
                } else {
                    resultIntent.putExtra("Screen_category", 0);
                }*/

                /*SharedPreferences prefs_user = getApplicationContext().getSharedPreferences("login_detail", 0);
                String loginMobileNumber = prefs_user.getString("mobile", "");*/
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
