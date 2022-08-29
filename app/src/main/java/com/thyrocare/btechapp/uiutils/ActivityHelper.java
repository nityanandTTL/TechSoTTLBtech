package com.thyrocare.btechapp.uiutils;


import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.thyrocare.btechapp.utils.app.AppConstants;


public interface ActivityHelper extends AppConstants {

    void hideKeyboard(View view);

    Typeface createTypeFace(String fontName);

    Drawable createRepeatableDrawable(int imageId);

    boolean isNetworkAvailable(Activity activity);

    void switchToActivity(Activity current, Class<? extends Activity> otherActivityClass, Bundle extras);

    void goToActivity(Activity current, Class<? extends Activity> otherActivityClass, Bundle extras);

    void initUI();

}
