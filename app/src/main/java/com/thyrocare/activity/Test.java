package com.thyrocare.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by E5233 on 9/22/2017.
 */

public class Test extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Pooja >> onCreate Test");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("Pooja >> onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("Pooja >> onStart");
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
