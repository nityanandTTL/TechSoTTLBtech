package com.dhb.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

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
                    Logger.error("THread is Executingsdsd ");
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

        appPreferenceManager = new AppPreferenceManager(getApplicationContext());
        if (appPreferenceManager.getLoginResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
            GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
            if (gpsTracker.canGetLocation() && !gpsTracker.isInternetAvailable()) {
                LocationModel lm = new LocationModel();
                lm.setLat(gpsTracker.getLatitude());
                lm.setLng(gpsTracker.getLongitude());
                lm.setTimestamp(Calendar.getInstance().getTimeInMillis());
                LocusPushLocationRequestModel lplrm = new LocusPushLocationRequestModel();
                lplrm.setLocation(lm);
                Logger.error("locationUpdateIntent Executed 4");
                locusPushLocationRequestAsyncTask = new AsyncTaskForRequest(getApplicationContext()).getLocusPushGeoLocationRequestAsyncTask(lplrm);
                locusPushLocationRequestAsyncTask.setApiCallAsyncTaskDelegate(new ApiCallAsyncTaskDelegate() {
                    @Override
                    public void apiCallResult(String json, int statusCode) throws JSONException {

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
}






