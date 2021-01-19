package com.thyrocare.btechapp.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Action;
import android.text.TextUtils;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.SplashActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.NotificationClickActivity;

import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.dao.models.OrderDetailsDao;
import com.thyrocare.btechapp.models.api.request.ChatRequestModel;
import com.thyrocare.btechapp.models.data.AcceptOrderNotfiDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;



import com.thyrocare.btechapp.network.ResponseParser;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.widget.Toast.LENGTH_SHORT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

/**
 * Created by Orion on 5/26/2017.
 */

public class LocationUpdateService extends IntentService {
    private static final String TAG = LocationUpdateService.class.getSimpleName();
    public Handler mHandler;
    private static final String TAG_CHAT = "TAG_CHAT";
    private ArrayList<ChatRequestModel> chatRequestModels;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * Used to name the worker thread, important only for debugging.
     */
    AppPreferenceManager appPreferenceManager;


    //neha g--------------

    private boolean isFetchingOrders = false;
    int firtTime = 0;
    private OrderDetailsDao orderDetailsDao;
    private DhbDao dhbDao;
    Long time, currenttime;
    String finaltime = "";
    int hr = 0;
    int minnew = 0;
    ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels;
    private String todate = "";
    private ArrayList<AcceptOrderNotfiDetailsModel> materialDetailsModels;
    private Context activity;
    private Global global;

    //neha g ------------

    public LocationUpdateService() {
        super("LocationUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        try {
            activity = getApplicationContext();
            global = new Global(activity);
            appPreferenceManager = new AppPreferenceManager(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    BlockBtechNotification(); //todo tejas
                    Logger.error("Thread is Executing 2 ");
                    try {
                        Thread.sleep(4 * 60 * 60 * 1000);
//                        Thread.sleep(20 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t2.start();

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    AcceptOrderNotification(); //todo tejas
                    Logger.error("Thread is Executing 3 ");
                    try {
                        Thread.sleep(30 * 60 * 1000);
//                        Thread.sleep(1 * 60 * 1000);
                        //  Thread.sleep(30 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t3.start();

        return START_STICKY;
    }










    //todo neha
    //tejas t----------------------------------------------------------------------

    private void CallGetBtechBlockApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<String> responseCall = apiInterface.CallGetBtechBlockApi(appPreferenceManager.getLoginResponseModel().getUserID());
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!StringUtils.isNull(response.body())) {
                        int Status = 0;
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            Status = jsonObject.getInt("Status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        boolean isAppInBackground = isAppIsInBackground(getApplicationContext());
                        if (Status == 1) {
                            MessageLogger.LogError(TAG, "CBT/NBT is Active");
                        } else {
                            MessageLogger.LogError(TAG, "CBT/NBT is NOT Active");
                            if (!isAppInBackground) {

                                MessageLogger.LogError(TAG, "APP is running, hence no notification needs to be send");
                            } else {
                                startBlockBtechnotification();
                            }
                        }
                    } else {
                        MessageLogger.LogError(TAG, "No data Found, Btech Block Status");
                    }
                } else {
                    Toast.makeText(activity, SomethingWentwrngMsg, LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(activity, SomethingWentwrngMsg, LENGTH_SHORT).show();
            }
        });
    }

    private void CallgetAcceptOrderNotificationApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<String> responseCall = apiInterface.CallgetAcceptOrderNotificationApi(appPreferenceManager.getLoginResponseModel().getUserID());
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!TextUtils.isEmpty(response.body()) && !response.body().trim().equalsIgnoreCase("[]")) {
                        JSONObject jsonObject = null;
                        try {
                            JSONArray jsonArray = new JSONArray(response.body());
                            jsonObject = jsonArray.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        boolean isAppInBackground = isAppIsInBackground(getApplicationContext());

                        ResponseParser responseParser = new ResponseParser(getApplicationContext());
                        materialDetailsModels = new ArrayList<>();

                        materialDetailsModels = responseParser.getAcceptOrderNotfiResponseModel(response.body(), response.code());
                        if (jsonObject != null || jsonObject.equals("")) {
                            MessageLogger.LogError(TAG, "CBT/NBT Has Orders Assigned Under him/her...");
                            if (!isAppInBackground) {
                                MessageLogger.LogError(TAG, "APP is running, hence no notification needs to be send");
                            } else {
                                try {
                                    NotificationManager notificationManager;
                                    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancelAll();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

//                            startAcceptordernotification();
                                if (materialDetailsModels != null && materialDetailsModels.size() > 0) {
                                    if (materialDetailsModels.size() != 0) {
                                        for (int i = 0; i < materialDetailsModels.size(); i++) {
//                                        sendNotification(materialDetailsModels.get(i).getVisitId(), i);
                                            sendNotification_n(materialDetailsModels.get(i), i);
                                        }
                                    }
                                }
                            }
                        } else {
                            MessageLogger.LogError(TAG, "CBT/NBT Has NO Orders Assigned Under him/her...");
                        }
                    } else {
                        MessageLogger.LogError(TAG, "CBT/NBT Has NO Orders Assigned Under him/her...");
                    }
                } else {
                    Toast.makeText(activity, SomethingWentwrngMsg, LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(activity, SomethingWentwrngMsg, LENGTH_SHORT).show();
            }
        });
    }


    //todo neha
    private void BlockBtechNotification() {
        try {
            if (Looper.myLooper() == null) {
                Logger.error("Lopper Exist ");
                Looper.myLooper().prepare();
            }

            if (Looper.myLooper() != null) {
                if (appPreferenceManager.getLoginResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
//                    Logger.error("uname1 " + appPreferenceManager.getUserDetailUserName());
                    if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.BTECH_ROLE_ID) || appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.NBT_ROLE_ID)) {
                        CallGetBtechBlockApi();
                    }
                } else {
                    Logger.error("Not login, please login ");
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        mHandler = new Handler();
    }

    private void AcceptOrderNotification() {
        try {
            if (Looper.myLooper() == null) {
                Logger.error("Lopper Exist ");
                Looper.myLooper().prepare();
            }

            if (Looper.myLooper() != null) {
                if (appPreferenceManager.getLoginResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
//                    Logger.error("uname1 " + appPreferenceManager.getUserDetailUserName());

                    if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.BTECH_ROLE_ID) || appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.NBT_ROLE_ID)|| appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.TSP_ROLE_ID)) {
                        CallgetAcceptOrderNotificationApi();
                    }
                } else {
                    Logger.error("Not login, please login ");
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        mHandler = new Handler();

    }

    public void startBlockBtechnotification() {
        Intent intent = new Intent(this, SplashActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        int mNotificationId = 001;

        CharSequence msg = "YOUR ACCESS HAS BEEN BLOCKED DUE TO OPERATIONAL REASONS. PLEASE CONTACT YOUR SUPERVISOR.";
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext());
        Notification notification = mBuilder.setSmallIcon(CommonUtils.getNotificationIcon()).setTicker("Btech App").setWhen(0)
                .setAutoCancel(true)
                .setContentTitle("Btech App")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentIntent(resultPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.app_logo))
                .setContentText(msg).build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationId, notification);

    }


    private void sendNotification_n(AcceptOrderNotfiDetailsModel AcceptOrderModel, int i) {
        try {
            NotificationManager notificationManager;
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Intent yesIntent = getNotificationIntent();
            yesIntent.setAction(BundleConstants.YES_ACTION);
            yesIntent.putExtra(BundleConstants.VISIT_ID, "" + AcceptOrderModel.getVisitId());
            yesIntent.putExtra(BundleConstants.YESNO_ID, "" + i);
            yesIntent.putExtra(BundleConstants.ORDER_SLOTID, "" + AcceptOrderModel.getSlotId());

            Intent maybeIntent = getNotificationIntent();
            maybeIntent.setAction(BundleConstants.NO_ACTION);
            maybeIntent.putExtra(BundleConstants.VISIT_ID, "" + AcceptOrderModel.getVisitId());
            maybeIntent.putExtra(BundleConstants.YESNO_ID, "" + i);
            maybeIntent.putExtra(BundleConstants.ORDER_SLOTID, "" + AcceptOrderModel.getSlotId());

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle("" + AcceptOrderModel.getVisitId());
            bigTextStyle.bigText("Order is Assigned, Please Accept or Reject the Order");

            Notification notification = new NotificationCompat.Builder(getApplicationContext())
//                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, getNotificationIntent(), PendingIntent.FLAG_UPDATE_CURRENT))
                    .setSmallIcon(CommonUtils.getNotificationIcon())
//                    .setTicker("Action Buttons Notification Received")
                    .setContentTitle("" + AcceptOrderModel.getVisitId())
                    .setContentText("Order is Assigned, Please Accept or Reject the Order")
                    .setWhen(System.currentTimeMillis())
                    .setStyle(bigTextStyle)
                    .setAutoCancel(false)
                    .setOngoing(false)
                    .addAction(new Action(
                            R.mipmap.ic_thumb_up_black_36dp,
                            getString(R.string.yes),
                            PendingIntent.getActivity(this, i, yesIntent, PendingIntent.FLAG_ONE_SHOT)))
                    .addAction(new Action(
                            R.mipmap.ic_thumb_down_black_36dp,
                            getString(R.string.no),
                            PendingIntent.getActivity(this, i, maybeIntent, PendingIntent.FLAG_ONE_SHOT)))
                    .build();

            notificationManager.notify(i, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Intent getNotificationIntent() {
        Intent intent = new Intent(getApplicationContext(), NotificationClickActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
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







