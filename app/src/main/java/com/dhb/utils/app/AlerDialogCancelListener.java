package com.dhb.utils.app;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class AlerDialogCancelListener implements OnClickListener {

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();

    }

}
