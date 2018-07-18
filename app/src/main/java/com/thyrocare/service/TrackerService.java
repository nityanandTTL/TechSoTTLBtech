package com.thyrocare.service;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    private LocationManager mLocationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        request = LocationRequest.create();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // buildNotification();
        final Handler handler1 = new Handler();
        final int delay = 5000; //milliseconds

        handler1.postDelayed(new Runnable() {
            public void run() {
                //do something
                Log.e(TAG, "run: ");
                try {
                    if (appPreferenceManager.getLoginResponseModel() != null) {
                        if (appPreferenceManager.getLoginResponseModel().getUserID() != null) {
                            loginToFirebase();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                handler1.postDelayed(this, delay);
            }
        }, delay);


        mGoogleApiClient.connect();
        appPreferenceManager = new AppPreferenceManager(getApplicationContext());
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
                .setSmallIcon(R.drawable.ic_home_black);
        startForeground(1, builder.build());
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
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
                    requestLocationUpdates();
                } else {
                    Log.d(TAG, "onComplete: " + task.getException().toString());
                    Log.d(TAG, "firebase auth failed");
                }
            }
        });
    }

    private void requestLocationUpdates() {
        Log.e(TAG, "requestLocationUpdates: ");
        request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        Log.e(TAG, "requestLocationUpdates: 22");

       /* FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);*/
        // FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi(this);
//        final String path = getString(R.string.firebase_path) + "/" + appPreferenceManager.getLoginResponseModel().getUserID();
        //  final String path = getString(R.string.firebase_path) + "/" + getString(R.string.transport_id);


        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {

           /* GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
            location1 = new Location("");
            location1.setLatitude(gpsTracker.getLatitude());
            location1.setLongitude(gpsTracker.getLongitude());
            Log.e(TAG, "requestLocationUpdates: lat long " + location1);

            final String path = getString(R.string.firebase_path) + "/" + appPreferenceManager.getLoginResponseModel().getUserName() + "-" + appPreferenceManager.getLoginResponseModel().getUserID();


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);

            if (location1 != null) {

                //   Toast.makeText(this, "last known " + location1, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "location update " + location1);
                ref.setValue(location1);
            }*/


            // Request location updates and when an update is
            // received, store the location in Firebase

            try {
                GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                location1 = new Location("");
                location1.setLatitude(gpsTracker.getLatitude());
                location1.setLongitude(gpsTracker.getLongitude());

                final String path = getString(R.string.firebase_path) + "/" + rplchar(appPreferenceManager.getLoginResponseModel().getUserName().toString().trim()) + "-" + appPreferenceManager.getLoginResponseModel().getUserID();


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);

                if (location1 != null) {

                    //   Toast.makeText(this, "last known " + location1, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "location update " + location1);
                    ref.setValue(location1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public String rplchar(String s){
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

            final String path = getString(R.string.firebase_path) + "/" + appPreferenceManager.getLoginResponseModel().getUserName() + "-" + appPreferenceManager.getLoginResponseModel().getUserID();


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
            if (location != null) {
                //   Toast.makeText(this, "" + location, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "location update " + location);
                ref.setValue(location);
            }





      /*  fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, request, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        });*/

            Log.e(TAG, "onLocationChanged: " + location.toString());
            //   }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}