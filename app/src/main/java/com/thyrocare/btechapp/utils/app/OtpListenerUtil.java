package com.thyrocare.btechapp.utils.app;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.network.SmsBroadcastReceiver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpListenerUtil {

    public static SmsBroadcastReceiver smsBroadcastReceiver;

    public static Activity mActivity;
    public static int REQ_USER_CONSENT = 8591;
    private static boolean isBroardcastReceiverRegistered = false;

    public static void startSmsUserConsent(Activity activity) {
        mActivity = activity;
        SmsRetrieverClient client = SmsRetriever.getClient(mActivity);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                MessageLogger.PrintMsg("On Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                MessageLogger.PrintMsg("On OnFailure");
            }
        });
        if (!isBroardcastReceiverRegistered){
            registerBroadcastReceiver();
        }else{
            mActivity.unregisterReceiver(smsBroadcastReceiver);
            registerBroadcastReceiver();
        }
    }

    public static void registerBroadcastReceiver() {
        isBroardcastReceiverRegistered = true;
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.setOnBroadcastReceiverListener(new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {
                mActivity.startActivityForResult(intent, REQ_USER_CONSENT);
            }

            @Override
            public void onFailure() {

            }
        });
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        mActivity.registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    public static String getOtpFromMessag(String message) {

        // This will match any 6 digit number in the message
        String OTP = "";
        Pattern pattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()){
            OTP = matcher.group(0);
        }
        return  OTP;
    }

    public static void UnregisteredReceiver() {
        if (isBroardcastReceiverRegistered){
            isBroardcastReceiverRegistered = false;
            mActivity.unregisterReceiver(smsBroadcastReceiver);
        }
    }


}
