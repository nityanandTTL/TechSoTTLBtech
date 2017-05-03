package com.dhb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.dhb.R;
import com.dhb.dao.CreateOrUpgradeDbTask;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.app.AppConstants;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.InputUtils;


public class SplashScreenActivity extends AbstractActivity {

    private Activity activity;
    private AppPreferenceManager appPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goAhead();
            }
        }, AppConstants.SPLASH_SCREEN_TIMEOUT);
    }

    private void goAhead() {
        new CreateOrUpgradeDbTask(new DhbDbDelegate(), getApplicationContext()).execute();
    }

    private class DhbDbDelegate implements CreateOrUpgradeDbTask.DbTaskDelegate {
        @Override
        public void dbTaskCompletedWithResult(Boolean result) {
            if(InputUtils.isNull(appPreferenceManager.getAPISessionKey()))
                switchToActivity(activity,LoginScreenActivity.class,new Bundle());
            else
                switchToActivity(activity,HomeScreenActivity.class,new Bundle());
        }
    }
}
