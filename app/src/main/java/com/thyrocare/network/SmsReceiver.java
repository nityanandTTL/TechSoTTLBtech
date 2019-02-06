package com.thyrocare.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.thyrocare.delegate.SmsListner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

/**
 * Created by e5209@thyrocare.com on 30/8/18.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListner mListener;
    Boolean b;
    String otp;
    String b1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            b = sender.endsWith("");  //Just to fetch otp sent from WNRCRP
            String messageBody = smsMessage.getMessageBody();
            Log.e(TAG, "onReceive msg: "+messageBody );
            try {
                otp = messageBody.replaceAll("[^0-9]", "");// here abcd contains otp which is in number format
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e(TAG, "onReceive: otp "+otp );
            Pattern p = Pattern.compile("(\\d{4})");// represents single character (\d{6})
            Matcher m = p.matcher(otp);
            if (m.find()) {
                b1 = m.group(1);
            }
            //Pass on the text to our listener.
            if (b == true) {
                try {
                    mListener.messageReceived(b1);  // attach value to interface object
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
            }
        }
    }

    public static void bindListener(SmsListner listener) {
        mListener = listener;
    }
}
