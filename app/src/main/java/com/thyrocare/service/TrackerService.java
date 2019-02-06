package com.thyrocare.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thyrocare.R;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.GPSTracker;

public class TrackerService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = TrackerService.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest request;
    private AppPreferenceManager appPreferenceManager;
    private Location location1;
    private Location mLastLocation;
    public static Handler handler1;
    private LocationManager mLocationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
          handler1 = new Handler();
        request = LocationRequest.create();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            buildNotification();
        }


        appPreferenceManager = new AppPreferenceManager(getApplicationContext());
        mGoogleApiClient.connect();
        if (appPreferenceManager.getLoginResponseModel() != null) {
            if (appPreferenceManager.getLoginResponseModel().getUserID() != null) {
                loginToFirebase();
            }
        }
    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.app_logo);
        startForeground(1, builder.build());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        String NOTIFICATION_CHANNEL_ID = "com.thyrocare.btech.dev";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.app_logo)
                .setContentIntent(broadcastIntent)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    public  BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            stopSelf();
//            unregisterReceiver(stopReceiver);

        }
    };


    private void loginToFirebase() {
        // Authenticate with Firebase, and request location updates
        String email = getString(R.string.firebase_email);
        String password = getString(R.string.firebase_password);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "firebase auth success");
                    requestLocationUpdates1();
                } else {
                    Log.d(TAG, "onComplete: " + task.getException().toString());
                    Log.d(TAG, "firebase auth failed");
                }
            }
        });
    }


    @SuppressLint("RestrictedApi")
    private void requestLocationUpdates1() {

        LocationRequest request = new LocationRequest();
        request.setInterval(5000);
        request.setFastestInterval(2000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
//        final String path = getString(R.string.firebase_path) + "/" + rplchar(appPreferenceManager.getLoginResponseModel().getUserName().trim()) + "-" + appPreferenceManager.getLoginResponseModel().getUserID();
     final String path = getString(R.string.firebase_path) + "/" +appPreferenceManager.getLoginResponseModel().getUserID() + "/" + getString(R.string.BTECH_LOCATION);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    System.out.println("path : "+path);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    Location location = locationResult.getLastLocation();
                    try {
                        if (location != null) {

                            ref.setValue(location);
                            Log.d(TAG, "location update " + location);

                        /* GeoFire geoFire=new GeoFire(ref);

                         // geoFire.setLocation(getString(R.string.firebase_path) + "/" + rplchar(appPreferenceManager.getLoginResponseModel().getUserName().toString().trim()) + "-" + appPreferenceManager.getLoginResponseModel().getUserID(),new GeoLocation(gpsTracker.getLatitude(),gpsTracker.getLongitude()));
                         geoFire.setLocation(getString(R.string.firebase_path) + "/" + rplchar(appPreferenceManager.getLoginResponseModel().getUserName().trim()) + "-" + appPreferenceManager.getLoginResponseModel().getUserID(), new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                             @Override
                             public void onComplete(String key, DatabaseError error) {

                             }
                         });*/

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        }

    }

    public String rplchar(String s) {
        String result = "";

        result = s.replaceAll("[^a-zA-Z0-9]", " ");

        return result;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {/*
        fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, request, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        });*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            // if (appPreferenceManager.isAfterLogin()) {

            /*final String path = getString(R.string.firebase_path) + "/" + appPreferenceManager.getLoginResponseModel().getUserName() + "-" + appPreferenceManager.getLoginResponseModel().getUserID();


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
            if (location != null) {
                //   Toast.makeText(this, "" + location, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "location update " + location);
                ref.setValue(location);
            }*/

            Log.e(TAG, "onLocationChanged: " + location.toString());
            //loginToFirebase1();
            //   }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(stopReceiver);
    }
}