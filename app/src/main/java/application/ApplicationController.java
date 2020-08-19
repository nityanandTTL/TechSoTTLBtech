package application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Handler;
import androidx.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.thyrocare.btechapp.Controller.ClientEntryController;
import com.thyrocare.btechapp.Controller.DeviceLogOutController;
import com.thyrocare.btechapp.Controller.GetAvailableStockController;
import com.thyrocare.btechapp.Controller.NotificationMappingController;
import com.thyrocare.btechapp.Controller.SendLatLongforOrderController;
import com.thyrocare.btechapp.Controller.TSPLMESampleDropController;
import com.thyrocare.btechapp.Controller.UpdateStockController;
import com.thyrocare.btechapp.NewScreenDesigns.Controllers.GetAcessTokenAndOTPAPIController;
import com.thyrocare.btechapp.NewScreenDesigns.Controllers.PostEmailValidationController;
import com.thyrocare.btechapp.NewScreenDesigns.Controllers.TrackUserActivityController;
import com.thyrocare.btechapp.dao.DbHelper;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;

import java.util.Stack;

import io.fabric.sdk.android.Fabric;

public class ApplicationController extends Application {

	public static ApplicationController applicationController;
	public static Stack<Activity> ACTIVITY_STACK = new Stack<Activity>();
	public static boolean IS_PAUSED = true;
	public static String pushToken="";
	public static Handler handler;
	public static Runnable timerRunnable;
//	private File cacheDir;
	public static String selectedService;
	public static NotificationMappingController notificationMappingController;
	public static SendLatLongforOrderController sendLatLongforOrderController;
    private AppPreferenceManager appPreferenceManager;
	MediaPlayer player;
	public static int beepCouner = 0;
	public static final int BEEP_COUNTER_15 = 15*60*1000;
	public static final int BEEP_COUNTER_5 = 5*60*1000;
	// after 15 min there will be two beep events 20 mins and 25 mins before completing 30 min inactive time.
	public static final int MAX_BEEP_COUNT = 2;
	public static DeviceLogOutController mDeviceLogOutController;
	public static TSPLMESampleDropController mTSPLMESampleDropController;
	public static ClientEntryController clientEntryController;
	public static UpdateStockController updateStockController;
	public static GetAvailableStockController getAvailableStockController;
	public static TrackUserActivityController trackUserActivityController;
	public static PostEmailValidationController PostEmailValidationController;
	public static GetAcessTokenAndOTPAPIController getAcessTokenAndOTPAPIController;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

	}

	@Override
	public void onCreate() {

		super.onCreate();
		Fabric.with(this, new Crashlytics());

		applicationController = this;

		DbHelper.init(applicationController);
		appPreferenceManager=new AppPreferenceManager(getApplicationContext());

		handler=new Handler();

		player = new MediaPlayer();


	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {

		super.onTerminate();

	}
}