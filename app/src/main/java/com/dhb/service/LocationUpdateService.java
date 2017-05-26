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
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.GPSTracker;

import org.json.JSONException;

import java.util.Calendar;

/**
 * Created by Vendor3 on 5/26/2017.
 */

public class LocationUpdateService extends IntentService{

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
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
        appPreferenceManager = new AppPreferenceManager(getApplicationContext());
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        if (gpsTracker.canGetLocation() && !gpsTracker.isInternetAvailable()) {
            LocationModel lm = new LocationModel();
            lm.setLat(gpsTracker.getLatitude());
            lm.setLng(gpsTracker.getLongitude());
            lm.setTimestamp(Calendar.getInstance().getTimeInMillis());
            LocusPushLocationRequestModel lplrm = new LocusPushLocationRequestModel();
            lplrm.setLocation(lm);
            locusPushLocationRequestAsyncTask = new AsyncTaskForRequest(getApplicationContext()).getLocusPushGeoLocationRequestAsyncTask(lplrm);
            locusPushLocationRequestAsyncTask.setApiCallAsyncTaskDelegate(new ApiCallAsyncTaskDelegate() {
                @Override
                public void apiCallResult(String json, int statusCode) throws JSONException {

                }

                @Override
                public void onApiCancelled() {

                }
            });
            locusPushLocationRequestAsyncTask.execute();
        }else {
            gpsTracker.showSettingsAlert();
            Toast.makeText(getApplicationContext(), "Check Internet connection and gps settings", Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }
}
