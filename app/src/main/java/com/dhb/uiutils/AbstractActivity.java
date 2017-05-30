package com.dhb.uiutils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.dhb.R;
import com.dhb.application.ApplicationController;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbstractActivity extends AppCompatActivity implements ActivityHelper {

	private ActivityHelper ah = new ActivityHelperImpl(this);
	protected AppPreferenceManager appPreferenceManager;
	public Typeface fontOpenRobotoRegular;
	public Typeface fontOpenRobotoMedium;
	public Typeface fontOpenRobotoLight;
	public Typeface fontArialBold;
	String screenId = "";
	private ProgressDialog progressDialog;
	private double PIC_WIDTH = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ApplicationController.ACTIVITY_STACK.add(this);
		appPreferenceManager = new AppPreferenceManager(this);
		appPreferenceManager.setIsAppInBackground(false);
		setFont();
		super.onCreate(savedInstanceState);


	}
	public int getScale(){
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth();
		Double val = new Double(width)/new Double(PIC_WIDTH);
		val = val * 100d;
		return val.intValue();
	}
	@Override
	protected void onStart() {
		super.onStart();

	}

	private void setFont() {
		fontOpenRobotoRegular = Typeface.createFromAsset(getAssets(), "fonts/roboto-regular.ttf");
		fontOpenRobotoMedium = Typeface.createFromAsset(getAssets(), "fonts/roboto-medium.ttf");
		fontOpenRobotoLight = Typeface.createFromAsset(getAssets(), "fonts/roboto-light.ttf");
		fontArialBold= Typeface.createFromAsset(getAssets(), "fonts/arialbd.ttf");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		appPreferenceManager.setIsAppInBackground(true);

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (appPreferenceManager.isAppInBackground()){
			appPreferenceManager.setIsAppInBackground(false);
		}

	}

	@Override
	protected void onPause() {
		ApplicationController.IS_PAUSED = true;
		appPreferenceManager.setIsAppInBackground(false);
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		ApplicationController.ACTIVITY_STACK.remove(this);

		super.onDestroy();
	}

	@Override
	public void hideKeyboard(View view) {

		ah.hideKeyboard(view);

	}

	/* For creating required typeface */
	@Override
	public Typeface createTypeFace(String fontName) {

		return ah.createTypeFace(fontName);

	}

	@Override
	public Drawable createRepeatableDrawable(int imageId) {
		return ah.createRepeatableDrawable(imageId);
	}

	/* For taking network state */
	@Override
	public boolean isNetworkAvailable(Activity activity) {
		return ah.isNetworkAvailable(activity);
	}

	/* Switch to some activity with bundle values and kill current activity */
	@Override
	public void switchToActivity(Activity current,
	                             Class<? extends Activity> otherActivityClass, Bundle extras) {

		ah.switchToActivity(current, otherActivityClass, extras);

	}

	/* Go to some activity with bundle values */
	@Override
	public void goToActivity(Activity current,
	                         Class<? extends Activity> otherActivityClass, Bundle extras) {

		ah.goToActivity(current, otherActivityClass, extras);
	}

	@Override
	public void initUI() {

	}

	// validate First Name

	public static boolean validateFName(String firstName) {

		return firstName.matches("[a-zA-Z-']*");
	}

	// validate last name
	public static boolean validateLName(String lastName) {

		return lastName.matches("[a-zA-Z'-]*");
	}

	public static boolean validatePhoneNumber(CharSequence target) {
		Pattern digitPattern = Pattern.compile("[0-9+-]*");

		return !TextUtils.isEmpty(target)
		       && digitPattern.matcher(target)
		       .matches();
	}

	public static boolean validateDigit(CharSequence target) {
		Pattern digitPattern = Pattern.compile("[0-9]*");

		return !TextUtils.isEmpty(target)
		       && digitPattern.matcher(target)
		       .matches();
	}

	public static boolean isValidEmail(String inputEmail) {
		final String emailRegExp = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
		Pattern patternObj = Pattern.compile(emailRegExp);

		Matcher matcherObj = patternObj.matcher(inputEmail);
		if (matcherObj.matches()){
			return true;
		} else {
			return false;
		}
	}

	public static boolean isAlphaNumeric(String s) {
		String pattern = "^[a-zA-Z0-9]*$";
		if (s.matches(pattern)){
			return true;
		}
		return false;
	}

	public static String getDeviceSecureAndroidId(Activity activity) {

		String strAndroidID = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
		return strAndroidID;
	}

	public static void forceHideKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		//Find the currently focused view, so we can grab the correct window token from it.
		View view = activity.getCurrentFocus();
		//If no view currently has focus, create a new one, just so we can grab a window token from it
		if (view == null){
			view = new View(activity);
		}
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public void showProgressDialog(Activity activity,String message) {
		Logger.debug("Progress dialog open");
		progressDialog = new ProgressDialog(activity);
		progressDialog.setTitle("Please wait");
		progressDialog.setMessage(message);
//		progressDialog.setMessage("Fetching data...");
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.payment_progress_circle));
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}

	public void closeProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}

	public void pushFragments(Fragment fragment, boolean shouldAnimate,
							  boolean shouldAdd, String destinationFragmetTag, int frameLayoutContainerId, String CurrentFragmentTag) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		if (shouldAnimate){
			// ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
			// R.animator.fragment_slide_left_exit,
			// R.animator.fragment_slide_right_enter,
			// R.animator.fragment_slide_right_exit);
		}

		ft.replace(frameLayoutContainerId, fragment, CurrentFragmentTag);

		//ft.add(R.id.fr_layout_container, fragment, TAG_FRAGMENT);

		if (shouldAdd){
			/*
			 * here you can create named backstack for realize another logic.
			   <<<<<<< HEAD
			                                                                                                                                                                                                                                                                                                                                                                                                   <<<<<<< HEAD
			                                                                                                                                                                                                                                                                                                                                                                                                           /*
			 * here you can create named backstack for realize another logic.
			                                                                                                                                                                                                                                                                                                                                                                                                   >>>>>>> bf799f243c1bd10ee4fb953d6481aa806925783f
			                                                                                                                                                                                                                                                                                                                                                                                                   >>>>>>> 7054f2ddd15b92e9724794839f298ccd266af5f2
			   =======
			                                                                                                                                                                                                                                                                                                                                                                                                                                                   <<<<<<< HEAD
			                                                                                                                                                                                                                                                                                                                                                                                                                                                           /*
			 * here you can create named backstack for realize another logic.
			                                                                                                                                                                                                                                                                                                                                                                                                                                                   >>>>>>> bf799f243c1bd10ee4fb953d6481aa806925783f
			                                                                                                                                                                                                                                                                                                                                                                                                                                                   >>>>>>> 7054f2ddd15b92e9724794839f298ccd266af5f2
			   >>>>>>> 596a4e066e37214dd935f8db9f0f637d7af457c3
			 * ft.addToBackStack("name of your backstack");
			 */
			ft.addToBackStack(destinationFragmetTag);
		} else {
			/*
			 * and remove named backstack:
			 * manager.popBackStack("name of your backstack",
			 * FragmentManager.POP_BACK_STACK_INCLUSIVE); or remove whole:
			 * manager.popBackStack(null,
			 * FragmentManager.POP_BACK_STACK_INCLUSIVE);
			 */
			manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}

		ft.commit();
	}

}