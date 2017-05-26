package com.dhb.utils.app;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.dhb.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Pratik Ambhore on 21/9/15.
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
		} catch (Exception ex){
		}
		return gps_enabled;
	}
}