package com.dhb.utils.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertMessage {

    public static void showAlert(Activity activity, String title, String message, boolean isCancellable) {
        if (title.equals("")) {
            // CustomeAlertDialog.Builder builder1 = new
            // CustomeAlertDialog.Builder(activity,R.style.AppBaseThemeDialog);
            AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
            builder1.setMessage(message);
            builder1.setCancelable(isCancellable);
            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            // CustomeAlertDialog.Builder builder1 = new
            // CustomeAlertDialog.Builder(activity,R.style.AppBaseThemeDialog);
            AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
            builder1.setTitle(title);
            builder1.setMessage(message);
            builder1.setCancelable(isCancellable);
            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    public static void showAlertWithYesNo(Context context, String title, String message, boolean isCancellable) {
        if (title.equals("")) {
            // CustomeAlertDialog.Builder builder1 = new
            // CustomeAlertDialog.Builder(context,R.style.AppBaseThemeDialog);
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage(message);
            builder1.setCancelable(isCancellable);
            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                }
            });
            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setTitle(title);
            builder1.setMessage(message);
            builder1.setCancelable(isCancellable);
            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

}
