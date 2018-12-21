package com.thyrocare.uiutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.thyrocare.utils.api.NetworkUtils;
import com.thyrocare.utils.app.InputUtils;
import com.thyrocare.utils.app.UiUtils;


public class ActivityHelperImpl implements ActivityHelper {

    private Context activity;

    public ActivityHelperImpl(Context activity) {

        this.activity = activity;
    }

    @Override
    public void hideKeyboard(View view) {

        InputUtils.hideKeyboard(activity, view);
    }

    @Override
    public Typeface createTypeFace(String fontName) {

        return UiUtils.getInstance().createTypeFace(activity, fontName);
    }

    @Override
    public Drawable createRepeatableDrawable(int imageId) {
        return null;
    }

    @Override
    public boolean isNetworkAvailable(Activity activity) {

        return NetworkUtils.isNetworkAvailable(activity);
    }

    @Override
    public void switchToActivity(Activity current, Class<? extends Activity> otherActivityClass, Bundle extras) {

        UiUtils.getInstance().switchToActivity(current, otherActivityClass, extras);

    }

    @Override
    public void goToActivity(Activity current, Class<? extends Activity> otherActivityClass, Bundle extras) {

        UiUtils.getInstance().goToActivity(current, otherActivityClass, extras);
    }

    @Override
    public void initUI() {

    }

}
