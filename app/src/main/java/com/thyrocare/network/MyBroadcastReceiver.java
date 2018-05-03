package com.thyrocare.network;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.NotificationCompat;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.ScheduleYourDaySecondIntentActivity;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;

import java.util.List;

/**
 * Created by e5321@thyrocare.com on 23/12/17.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    private NotificationManager mManager;
    AppPreferenceManager appPreferenceManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        isAppIsInBackground(context);
        appPreferenceManager = new AppPreferenceManager(context);

        boolean appInBack = isAppIsInBackground(context);

       /* if(!appInBack){
            System.out.println("Not is background");
        }else {*/
        mManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

        Intent snoozeIntent = new Intent(context, ScheduleYourDaySecondIntentActivity.class);
        Intent snoozeIntent2 = new Intent(context, HomeScreenActivity.class);//to be done here//
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        System.out.println("entered Broadcastreceiver");
        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("You've received new message.");
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.drawable.app_logo);


        mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo));
        android.support.v4.app.NotificationCompat.InboxStyle inboxStyle = new android.support.v4.app.NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("BTech");


        if (BundleConstants.OrderAccept == 2 && BundleConstants.delay == 0 || !appPreferenceManager.getShowTimeInNotificatn().equals("")) {

            String[] events = new String[6];
            events[0] = new String("Your order is scheule at " + BundleConstants.ShowTimeInNotificatn);
            events[1] = new String("Click on start, If you have already started please hurry!");
            System.out.println("15min");
            PendingIntent snoozePendingIntent =
                    PendingIntent.getActivity(context, 0, snoozeIntent2, 0);
            mBuilder.addAction(0, "Start",
                    snoozePendingIntent);
            for (int i = 0; i < events.length; i++) {
                inboxStyle.addLine(events[i]);
            }
        } else if (BundleConstants.Day_aftr_tom == 2 || appPreferenceManager.getDay_aftr_tom() == 2) {
            PendingIntent snoozePendingIntent =
                    PendingIntent.getActivity(context, 0, snoozeIntent, 0);
            String[] events = new String[6];
            events[0] = new String("You have still not given your availability for tomorrow");
            events[1] = new String(",please log in to do it immediately.");
            mBuilder.addAction(0, "Set Avaliablity",
                    snoozePendingIntent);
            System.out.println("acccept");

            for (int i = 0; i < events.length; i++) {
                inboxStyle.addLine(events[i]);
            }
        } /*else if (BundleConstants.DataInVisitModel == 2 || appPreferenceManager.getDataInVisitModel() == 2) {
                String[] events = new String[6];
                events[0] = new String("You have completed today's collection.");
                events[1] = new String("Ensure to complete HUB scan and submission of sample in HUB");
                System.out.println("acccept");
                for (int i = 0; i < events.length; i++) {
                    inboxStyle.addLine(events[i]);
                }
            } */ else if (BundleConstants.OrderAccept == 2 || appPreferenceManager.getOrderAccept() == 2 && appPreferenceManager.getDelay() != 0) {
            String[] events = new String[6];
            events[0] = new String("You order has been completed,");
            events[1] = new String("order served with delay of " + " " + BundleConstants.delay + "min .");
            events[2] = new String("Thank you.");
            System.out.println("acccept");
            for (int i = 0; i < events.length; i++) {
                inboxStyle.addLine(events[i]);
            }
        } /*else {
                String[] events = new String[6];
                events[0] = new String("You Have Orders for today");
                events[1] = new String("Kindly accept them");
                System.out.println("acccept");

                for (int i = 0; i < events.length; i++) {
                    inboxStyle.addLine(events[i]);
                }
            }*/
        mBuilder.setStyle(inboxStyle);
        mManager = (NotificationManager) context.getApplicationContext().getSystemService(context.getApplicationContext().NOTIFICATION_SERVICE);
        //Intent intent1 = new Intent(context.getApplicationContext(),MainActivity.class);
        // intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mManager.notify(1, mBuilder.build());

        // }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}