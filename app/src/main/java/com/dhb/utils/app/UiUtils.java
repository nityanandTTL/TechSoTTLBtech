package com.dhb.utils.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Stack;

public class UiUtils {

	private static UiUtils instance = null;

	protected UiUtils() {
		// Exists only to defeat instantiation.
	}

	public static UiUtils getInstance() {
		if (instance == null){
			instance = new UiUtils();
		}
		return instance;
	}

	public void switchToActivity(Activity current, Class<? extends Activity> otherActivityClass, Bundle extras) {
		Intent intent = new Intent(current, otherActivityClass);
		if (extras != null){
			intent.putExtras(extras);
		}
		current.startActivity(intent);
		current.finish();
	}

	public void goToActivity(Activity current, Class<? extends Activity> otherActivityClass, Bundle extras) {
		Intent intent = new Intent(current, otherActivityClass);
		if (extras != null){
			intent.putExtras(extras);
		}
		current.startActivity(intent);
	}

	public Typeface createTypeFace(Context context, String fontName) {
		Typeface tf = Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/" + fontName);
		return tf;
	}

	public void clearActivityStack(Stack<Activity> activityStack) {
		for (Activity activity : activityStack){
			activity.finish();
		}
		activityStack.clear();
	}

	public static int dpToPx(Context context, int dp) {
		DisplayMetrics displayMetrics = context.getApplicationContext()
		                                .getResources().getDisplayMetrics();
		int px = Math.round(dp
		                    * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}

	public static void setupKeyboard(View view, final Activity activity) {

		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)){

			view.setOnTouchListener(new View.OnTouchListener() {

			                                @Override
			                                public boolean onTouch(View v, android.view.MotionEvent event) {
			                                        // TODO Auto-generated method stub
			                                        hideSoftKeyboard(activity);
			                                        return false;
							}
						});
		}

		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup){

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++){

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupKeyboard(innerView, activity);
			}
		}
	}

	public static void hideSoftKeyboard(Activity activity) {

		InputMethodManager inputMethodManager = (InputMethodManager) activity
		                                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (inputMethodManager.isAcceptingText()){
			try {
				inputMethodManager.hideSoftInputFromWindow(activity
				                                           .getCurrentFocus().getWindowToken(), 0);
			} catch (Exception e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}