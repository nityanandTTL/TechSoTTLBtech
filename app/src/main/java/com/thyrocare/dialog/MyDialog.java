package com.thyrocare.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class MyDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AlertDialog.Builder builder = new AlertDialog.Builder(MyDialog.this);



                builder.setItems(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                //AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder.setTitle("Please  change the Date and Time Settings to automatic from Setting  ");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                        finish();
                    }
                });
        builder.setCancelable(false);
        builder.show();

            }



}
