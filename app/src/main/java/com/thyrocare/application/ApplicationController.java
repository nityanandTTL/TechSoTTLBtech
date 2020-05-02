package com.thyrocare.application;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.thyrocare.Controller.ClientEntryController;
import com.thyrocare.Controller.DeviceLogOutController;
import com.thyrocare.Controller.GetAvailableStockController;
import com.thyrocare.Controller.NotificationMappingController;
import com.thyrocare.Controller.TSPLMESampleDropController;
import com.thyrocare.Controller.TrackUserActivityController;
import com.thyrocare.Controller.UpdateStockController;
import com.thyrocare.dao.DbHelper;
import com.thyrocare.utils.app.AppPreferenceManager;

import java.util.Stack;

import io.fabric.sdk.android.Fabric;

public class ApplicationController extends MultiDexApplication {

	public static ApplicationController applicationController;
	public static Stack<Activity> ACTIVITY_STACK = new Stack<Activity>();
	public static boolean IS_PAUSED = true;
	public static String pushToken="";
	public static Handler handler;
	public static Runnable timerRunnable;
//	private File cacheDir;
	public static String selectedService;
	public static NotificationMappingController notificationMappingController;
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

	}

	@Override
	public void onCreate() {

		super.onCreate();
		Fabric.with(this, new Crashlytics());

		applicationController = this;
		/*
		if (!cacheDir.exists()){
			cacheDir.mkdirs();
		}*/

		DbHelper.init(applicationController);
		appPreferenceManager=new AppPreferenceManager(getApplicationContext());

		handler=new Handler();

		player = new MediaPlayer();

		/*timerRunnable=new Runnable() {
			@Override
			public void run() {

				if (!appPreferenceManager.getAPISessionKey().trim().isEmpty()){

					long lMin = appPreferenceManager.getLastApiTiming() / 60000;
					long nMin = Calendar.getInstance().getTimeInMillis() / 60000;

					if (lMin - nMin <= 0){

						Logger.debug("Called Beep Function");
						//Start Beep;
//						Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//						long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
//						v.vibrate(pattern, -1);
//
//						playBeep();

						if (ApplicationController.beepCouner < MAX_BEEP_COUNT){
							sendBroadcast(new Intent("com.thyrocare.restart_beep_service"));
						}
					}
				}
			}

		};*/

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

	public static void clearActivityStack() {

		for (Activity activity : ACTIVITY_STACK){
			activity.finish();
		}

	}

	public static String getSelectedService() {
		return selectedService;
	}

	public static void setSelectedService(String selectedService) {
		ApplicationController.selectedService = selectedService;
	}

	public void playBeep() {
		try {
			if (player.isPlaying()){
				player.stop();
				player.release();
				player = new MediaPlayer();
			}

			AssetFileDescriptor descriptor = getAssets().openFd("beep.mp3");
			player.reset();
			player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
				        Log.i("Completion Listener", "Song Complete");
				        mp.stop();
				        mp.reset();
				        /* mp.setDataSource([nextElement]);
				           mp.prepare();
				           mp.start();*/
				}
			});
			player.prepare();
			player.setVolume(1f, 1f);
			player.setLooping(false);
			player.start();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}