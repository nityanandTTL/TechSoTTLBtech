package com.thyrocare.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.NotificationClickActivity;
import com.thyrocare.activity.SplashScreenActivity;
import com.thyrocare.activity.Users;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.dao.models.OrderDetailsDao;
import com.thyrocare.models.api.request.ChatRequestModel;
import com.thyrocare.models.api.request.LocusPushLocationRequestModel;
import com.thyrocare.models.api.request.TrackBtechLocationRequestModel;
import com.thyrocare.models.api.response.FetchOrderDetailsResponseModel;
import com.thyrocare.models.data.AcceptOrderNotfiDetailsModel;
import com.thyrocare.models.data.LocationModel;
import com.thyrocare.models.data.OrderVisitDetailsModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.GPSTracker;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.thyrocare.utils.api.NetworkUtils.isNetworkAvailable;

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
    ApiCallAsyncTask locusPushLocationRequestAsyncTask;


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
            appPreferenceManager = new AppPreferenceManager(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // As per Ganesh Sir Instruction
        /*Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    startJob();
                    Logger.error("Thread is Executing ");
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t.start();*/

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

        /* Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {


                    VisitOderDEtails1(); //todo neha
                    Logger.error("Thread is Executing 4");
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t4.start();*/
        return START_STICKY;
    }

    private void startJob() {


        Logger.error("wowowow0");

        {

            if (Looper.myLooper() == null) {
                Logger.error("Lopper Exist ");
                Looper.myLooper().prepare();

            }

            if (Looper.myLooper() != null) {
//                fetchchat();
                Logger.error("wowowow0.1");
                appPreferenceManager = new AppPreferenceManager(getApplicationContext());
/*
                if (!appPreferenceManager.getUserDetailUserName().equals("") && !appPreferenceManager.getUserDetailChatWith().equals("")) {
                    Logger.error("uname " + appPreferenceManager.getUserDetailUserName());
                    Logger.error("chatWith " + appPreferenceManager.getUserDetailChatWith());

                }*/

                if (appPreferenceManager.getLoginResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
                    Logger.error("wowowow0.2");
                    GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                    Logger.error("wowowow0.3");
                   /* if (gpsTracker.canGetLocation() && !gpsTracker.isInternetAvailable()) {*/
                    Logger.error("wowowow1");

                    LocationModel lm = new LocationModel();
                    Logger.error("wowowow2");
                    lm.setLat(gpsTracker.getLatitude());
                    lm.setLng(gpsTracker.getLongitude());

                    appPreferenceManager.setLatitude(String.valueOf(gpsTracker.getLatitude()));
                    appPreferenceManager.setLongitude(String.valueOf(gpsTracker.getLongitude()));
                    lm.setTimestamp(Calendar.getInstance().getTimeInMillis());
                    Logger.error("wowowow3");
                    Logger.error("API" + lm.getLat() + "");
                    Logger.error("API" + lm.getLng() + "");
                    Logger.error("API" + lm.getTimestamp() + "");
                    Logger.error("wowowow4");

                    LocusPushLocationRequestModel lplrm = new LocusPushLocationRequestModel();
                    lplrm.setLocation(lm);
                    Logger.error("locationUpdateIntent Executed 4");
                    AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(this);
                    Logger.error("wowowow5");

                    TrackBtechLocationRequestModel trackBtechLocationRequestModel = new TrackBtechLocationRequestModel();
                    trackBtechLocationRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                    trackBtechLocationRequestModel.setLatitude(gpsTracker.getLatitude());
                    trackBtechLocationRequestModel.setLongitude(gpsTracker.getLongitude());

                    ApiCallAsyncTask Location = asyncTaskForRequest.getTrackBtechLocationRequestAsyncTask(trackBtechLocationRequestModel);
                    // ApiCallAsyncTask Location = asyncTaskForRequest.getLocusPushGeoLocationRequestAsyncTask(lplrm);


                    Location.execute(Location);

                    Logger.error("wowowow6");

                    Location.setApiCallAsyncTaskDelegate(new ApiCallAsyncTaskDelegate() {
                        @Override
                        public void apiCallResult(String json, int statusCode) throws JSONException {
                            if (statusCode == 200) {
                                Logger.error("APICALLED SUCCESFULLY LOCUS");
/*                                Looper.myLooper().quit();*/
                            } else {
                                // Toast.makeText(getApplicationContext(), "Locus Failed ", Toast.LENGTH_SHORT).show();
                            }
                        }


                        @Override
                        public void onApiCancelled() {

                        }
                    });
                    //locusPushLocationRequestAsyncTask.execute();
                }/* else {
//            gpsTracker.showSettingsAlert();
                Toast.makeText(getApplicationContext(), "Check Internet connection and gps settings", Toast.LENGTH_SHORT).show();
            }*/
            }
        }
        mHandler = new Handler();


    }

    private void fetchchat() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(getApplicationContext());
        ApiCallAsyncTask fetchMaterialINVApiAsyncTask = asyncTaskForRequest.getChatAPi();
        fetchMaterialINVApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchchatAsyncTaskDelegateResult());
        if (isNetworkAvailable(getApplicationContext())) {
            fetchMaterialINVApiAsyncTask.execute(fetchMaterialINVApiAsyncTask);
        } else {
            Toast.makeText(getApplicationContext(), R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }


    private class FetchchatAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.error("test json123" + json);
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(getApplicationContext());

                chatRequestModels = responseParser.getchatMasterResponse(json, statusCode);
                Logger.error("test json " + json);
                JSONArray jsonArray = new JSONArray(json);
                //for (int i = 0; i < jsonArray.length(); i++) {
                int Counter = jsonArray.length() - 1;
                Logger.error("Counter" + Counter);
                String flag = jsonArray.getJSONObject(Counter).getString("code");
                String name = jsonArray.getJSONObject(Counter).getString("lastName");
                String from = jsonArray.getJSONObject(Counter).getString("firstName");
                Logger.error("code " + flag);
                Logger.error("name " + name);

                if (flag.equals("1")) {
                    Logger.error("notification");
                    if (name.equalsIgnoreCase("" + appPreferenceManager.getLoginResponseModel().getUserName())) {
                        Logger.error("notification send kaara");

                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                                0 /* request code */, new Intent(getApplicationContext(), Users.class).putExtra("Comeform", from).putExtra("comeFrom", "service"), PendingIntent.FLAG_UPDATE_CURRENT);


                        sendNotify(pendingIntent, jsonArray, Counter);
//                        Chatpostapi(jsonArray);
                    }

                }
                //  }

            }

        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }


    }

    private void Chatpostapi(JSONArray jsonArray) {
        ChatRequestModel chatRequestModel = new ChatRequestModel();
        try {
            chatRequestModel.setFirstName(jsonArray.getJSONObject(jsonArray.length() - 1).getString("firstName"));
            chatRequestModel.setLastName(jsonArray.getJSONObject(jsonArray.length() - 1).getString("lastName"));
            chatRequestModel.setPhone(jsonArray.getJSONObject(jsonArray.length() - 1).getString("phone"));
            chatRequestModel.setEmail(jsonArray.getJSONObject(jsonArray.length() - 1).getString("email"));
            chatRequestModel.setCode("0");
            chatRequestModel.setPassword("Null");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Logger.error("model " + chatRequestModel.toString());
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(getApplicationContext());
        Logger.error("test1 ");
        ApiCallAsyncTask setchatDetailApiAsyncTask = asyncTaskForRequest.ChatPostapi(chatRequestModel);

        setchatDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new setApplyLeaveDetailsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(getApplicationContext())) {
            setchatDetailApiAsyncTask.execute(setchatDetailApiAsyncTask);
        } else {
            Toast.makeText(getApplicationContext(), R.string.internet_connetion_error, LENGTH_SHORT).show();
        }
    }

    private void sendNotify(PendingIntent pendingIntent, JSONArray jsonArray, int i) {
        String message = null;
        String from = null;
        try {
            message = jsonArray.getJSONObject(i).getString("phone");
            from = jsonArray.getJSONObject(i).getString("firstName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        long[] pattern = {500, 500, 500, 500, 500};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("Oms App Notification")
                .setContentText("" + message + "from " + from)
                .setAutoCancel(true).setVibrate(pattern)
                .setLights(Color.BLUE, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    //set Deleagte
    private class setApplyLeaveDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_CHAT + "--apiCallResult: ");
            if (statusCode == 200) {
                Toast.makeText(getApplicationContext(), "" + json, LENGTH_SHORT).show();


            } else {

                Logger.debug(TAG_CHAT + "--apiCallfailed: ");
            }
        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_CHAT + "onApiCancelled: ");
            Toast.makeText(getApplicationContext(), R.string.network_error, LENGTH_SHORT).show();
        }


    }

    //todo neha

    class FetchOrderDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

            if (statusCode == 200) {
                System.out.println("statuscode" + statusCode);

                //jai
                JSONObject jsonObject = new JSONObject(json);

                ResponseParser responseParser = new ResponseParser(getApplicationContext());
                FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel = new FetchOrderDetailsResponseModel();
                fetchOrderDetailsResponseModel = responseParser.getFetchOrderDetailsResponseModel(json, statusCode);
                System.out.println("fetchorderdetailsmodel" + fetchOrderDetailsResponseModel.toString());
                System.out.println("fetchorderdetailsmodelordervisit" + fetchOrderDetailsResponseModel.getOrderVisitDetails().size());

                mHandler = new Handler();

                isFetchingOrders = false;
                initData();
            } else {
                firtTime = 0;

            }
        }

        @Override
        public void onApiCancelled() {
            isFetchingOrders = false;
        }

    }
    //todo neha

    //todo neha
    private void VisitOderDEtails1() {
        if (Looper.myLooper() == null) {
            Looper.myLooper().prepare();
        }

        if (Looper.myLooper() != null) {
            appPreferenceManager = new AppPreferenceManager(getApplicationContext());

           /* if (!appPreferenceManager.getUserDetailUserName().equals("") && !appPreferenceManager.getUserDetailChatWith().equals("")) {
                Logger.error("uname1 " + appPreferenceManager.getUserDetailUserName());

            }*/
            if (appPreferenceManager.getLoginResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
                AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(getApplicationContext());
                ApiCallAsyncTask fetchOrderDetailApiAsyncTask = asyncTaskForRequest.getFetchOrderDetailsRequestAsyncTask(false);
                fetchOrderDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchOrderDetailsApiAsyncTaskDelegateResult());
                if (!isFetchingOrders) {
                    isFetchingOrders = true;
                    fetchOrderDetailApiAsyncTask.execute(fetchOrderDetailApiAsyncTask);
                } else {
                    dhbDao = new DhbDao(getApplicationContext());
                    orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
                    TastyToast.makeText(getApplicationContext(), getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    initData();
                }
            }
        }
    }

    private void initData() {
        dhbDao = new DhbDao(getApplicationContext());
        orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
        orderDetailsResponseModels = orderDetailsDao.getAllModels();
        int currenthour = new Time(System.currentTimeMillis()).getHours();
        int currentmin = new Time(System.currentTimeMillis()).getMinutes();
        if (currenthour == 16 && currentmin == 00) {
            startAlertn("Please update your material stock as on day to avoid any inconvenience in serving the orders.");
        }
        if (currenthour == 12 && currentmin == 00 || currenthour == 14 && currentmin == 0 || currenthour == 17 && currentmin == 00) {
            startAlertn("Please clear the outstanding to avoid suspension, Update bank receipt in the App. Ignore if already done.");
        }
        if (currenthour == 18 && currentmin == 00 && orderDetailsResponseModels.size() == 0) {
            appPreferenceManager.setDataInVisitModel(2);
            BundleConstants.DataInVisitModel = 2;
            startAlertn("You have completed today's collection.\nEnsure to complete HUB scan and submission of sample in HUB");//TODO neha
        }
    }

    //todo neha
    public void startAlertn(String msg) {
        int mNotificationId = 002;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext());
        Notification notification = mBuilder.setSmallIcon(R.drawable.app_logo).setTicker("Btech App").setWhen(0)
                .setAutoCancel(true)
                .setContentTitle("Btech App")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.app_logo))
                .setContentText(msg).build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationId, notification);
    }

    //todo neha
    //tejas t----------------------------------------------------------------------

    class BtechBlockDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

            if (statusCode == 200) {
                System.out.println("statuscode" + statusCode);

                if (json != null || !TextUtils.isEmpty(json)) {
                    JSONObject jsonObject = new JSONObject(json);
                    int Status = jsonObject.getInt("Status");

                    boolean isAppInBackground = isAppIsInBackground(getApplicationContext());

                    if (Status == 1) {
                        Log.e(TAG, "CBT/NBT is Active");
                    } else {
                        Log.e(TAG, "CBT/NBT is NOT Active");
                        if (!isAppInBackground) {

                            Log.e(TAG, "APP is running, hence no notification needs to be send");
                        } else {
                            startBlockBtechnotification();
                        }
                    }
                } else {
                    Log.e(TAG, "No data Found, Btech Block Status");
                }
            }
        }

        @Override
        public void onApiCancelled() {
        }
    }

    class AcceptOrderNotificationApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                System.out.println("statuscode" + statusCode);
                if (json != null && !TextUtils.isEmpty(json) && !json.trim().equalsIgnoreCase("[]")) {
                    JSONArray jsonArray = new JSONArray(json);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String data1 = jsonObject.getString("VisitId");

                    boolean isAppInBackground = isAppIsInBackground(getApplicationContext());

                    ResponseParser responseParser = new ResponseParser(getApplicationContext());
                    materialDetailsModels = new ArrayList<>();

                    materialDetailsModels = responseParser.getAcceptOrderNotfiResponseModel(json, statusCode);
                    if (jsonObject != null || jsonObject.equals("")) {
                        Log.e(TAG, "CBT/NBT Has Orders Assigned Under him/her...");
                        if (!isAppInBackground) {
                            Log.e(TAG, "APP is running, hence no notification needs to be send");
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
                        Log.e(TAG, "CBT/NBT Has NO Orders Assigned Under him/her...");
                    }
                } else {
                    Log.e(TAG, "CBT/NBT Has NO Orders Assigned Under him/her...");
                }
            } else {
                Log.e(TAG, "CBT/NBT Has NO Orders Assigned Under him/her...");
            }
        }

        @Override
        public void onApiCancelled() {
        }
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
                        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(getApplicationContext());
                        ApiCallAsyncTask BtechBlockDetailsApiAsyncTask = asyncTaskForRequest.getBtechBlockDetails();
                        BtechBlockDetailsApiAsyncTask.setApiCallAsyncTaskDelegate(new BtechBlockDetailsApiAsyncTaskDelegateResult());
                        BtechBlockDetailsApiAsyncTask.execute();
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
                    if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.BTECH_ROLE_ID) || appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.NBT_ROLE_ID)) {
                        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(getApplicationContext());
                        ApiCallAsyncTask AcceptOrderNotificationApiAsyncTask = asyncTaskForRequest.getAcceptOrderNotification();
                        AcceptOrderNotificationApiAsyncTask.setApiCallAsyncTaskDelegate(new AcceptOrderNotificationApiAsyncTaskDelegateResult());
                        AcceptOrderNotificationApiAsyncTask.execute();
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
        Intent intent = new Intent(this, SplashScreenActivity.class);
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
        Notification notification = mBuilder.setSmallIcon(R.drawable.app_logo).setTicker("Btech App").setWhen(0)
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

    public void startAcceptordernotification() {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        int mNotificationId = 001;

        CharSequence msg = "NEW ORDER PENDING FOR YOUR ACCEPTANCE, DO IT IMMEDIATELY TO AVOID ANY INCONVENIENCE TO THE PATIENT.";
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext());
        Notification notification = mBuilder.setSmallIcon(R.drawable.app_logo).setTicker("Btech App").setWhen(0)
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

    private void sendNotification(String OrderNo, int i) {
        try {
            Intent intent = new Intent(this, SplashScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, i /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Intent secondActivityIntent = new Intent(this, HomeScreenActivity.class);
            secondActivityIntent.putExtra(BundleConstants.VISIT_ID, "" + OrderNo);
            secondActivityIntent.putExtra(BundleConstants.FlagAcceptReject, "1");
            secondActivityIntent.putExtra("LEAVEINTIMATION", "0");
            PendingIntent secondActivityPendingIntent = PendingIntent.getActivity(this, i, secondActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent thirdActivityIntent = new Intent(this, HomeScreenActivity.class);
            thirdActivityIntent.putExtra(BundleConstants.VISIT_ID, "" + OrderNo);
            thirdActivityIntent.putExtra(BundleConstants.FlagAcceptReject, "0");
            thirdActivityIntent.putExtra("LEAVEINTIMATION", "0");
            PendingIntent thirdActivityPendingIntent = PendingIntent.getActivity(this, i, thirdActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.app_logo)
                    .setContentTitle(OrderNo)
                    .setContentText("Order is Assigned, Please Accept or Reject the Order")
                    .addAction(R.drawable.app_logo, "Yes", secondActivityPendingIntent)
                    .addAction(R.drawable.app_logo1, "No", thirdActivityPendingIntent)
                    .setAutoCancel(true)
                    .setOngoing(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(i /* ID of notification */, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    .setSmallIcon(R.drawable.app_logo)
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







