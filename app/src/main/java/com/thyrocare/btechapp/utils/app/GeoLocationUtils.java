package com.thyrocare.btechapp.utils.app;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;

import androidx.appcompat.app.AlertDialog;

import com.thyrocare.btechapp.R;

/**
 * Created by Orion on 21/9/15.
 */
public class GeoLocationUtils {
    private static GeoLocationUtils instance;

    public static void showGPSDisabledAlertToUser(final Context context, final GeoLocationDialogDelegate geoLocDelegate) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(context.getResources().getString(R.string.gps_disabled))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.goto_settings),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton(context.getResources().getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        geoLocDelegate.onGeoLocDialogCancelPressed();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public static boolean getLocationSetting(Activity context) {
        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        return gps_enabled;
    }
}