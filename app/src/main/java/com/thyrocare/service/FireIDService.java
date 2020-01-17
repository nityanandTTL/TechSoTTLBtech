package com.thyrocare.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by E4904 on 4/24/2017.
 */

public class FireIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String tkn = FirebaseInstanceId.getInstance().getToken();
        Log.d("FireIDServicechat","Token ["+tkn+"]");

        storeRegIdInPref(tkn);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent("demokey");
        registrationComplete.putExtra("token", tkn);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Firebase Pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Token", token);
        editor.putString("ServerToken", "no");
        editor.apply();
    }
}