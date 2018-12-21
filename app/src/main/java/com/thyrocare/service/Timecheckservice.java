package com.thyrocare.service;

import android.app.IntentService;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.thyrocare.dialog.MyDialog;
import com.thyrocare.utils.api.Logger;

import static android.content.ContentValues.TAG;

/**
 * Created by E4904 on 9/20/2017.
 */

public class Timecheckservice extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public Timecheckservice() {
        super("LocationUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
      Thread  t = new Thread(new Runnable() {
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

    @Override
    public void onDestroy() {
        stopSelf();

    }

    private void startJob() {

        try {

            if (Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
                Log.e(TAG, "run: condition true");
            } else {

                Intent i = new Intent(Timecheckservice.this, MyDialog.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                Log.v("PeriodicTimerService", "Awake");


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
