package com.thyrocare.btechapp.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;

/**
 * Created by E5233 on 9/22/2017.
 */

public class Test extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MessageLogger.PrintMsg("Pooja >> onCreate Test");
    }

    @Override
    protected void onStart() {
        super.onStart();
        MessageLogger.PrintMsg("Pooja >> onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MessageLogger.PrintMsg("Pooja >> onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
