package com.thyrocare.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;

/**
 * Created by E4904 on 4/24/2017.
 */

public class FireMsgService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("onReceive","");
        sendNotification("Thyrocare","You have new notification");
       // sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));
    }

    private void sendNotification(String messageTitle,String messageBody) {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* request code */, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500,500,500,500,500};

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.bubble_left_gray)
                .setContentTitle("FInalChat")
                .setContentText("New Message Arrived ")
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE,1,1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
