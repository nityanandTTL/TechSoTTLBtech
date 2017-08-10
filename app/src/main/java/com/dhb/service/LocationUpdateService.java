package com.dhb.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.dao.DhbDao;
import com.dhb.models.api.request.LocusPushLocationRequestModel;
import com.dhb.models.data.LocationModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.GPSTracker;
import com.dhb.utils.app.InputUtils;

import org.json.JSONException;

import java.util.Calendar;

/**
 * Created by Orion on 5/26/2017.
 */

public class LocationUpdateService extends IntentService {
    public Handler mHandler;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * Used to name the worker thread, important only for debugging.
     */
    AppPreferenceManager appPreferenceManager;
    ApiCallAsyncTask locusPushLocationRequestAsyncTask;

    public LocationUpdateService() {
        super("LocationUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        Thread t = new Thread(new Runnable() {
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
        t.start();
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

                Logger.error("wowowow0.1");
                appPreferenceManager = new AppPreferenceManager(getApplicationContext());
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

                    ApiCallAsyncTask Location = asyncTaskForRequest.getLocusPushGeoLocationRequestAsyncTask(lplrm);

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
}







