package com.thyrocare.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.thyrocare.R;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.dao.models.OrderDetailsDao;
import com.thyrocare.models.api.request.CallPatchRequestModel;
import com.thyrocare.models.api.request.OrderAllocationTrackLocationRequestModel;
import com.thyrocare.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.models.data.OrderVisitDetailsModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.service.TrackerService;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.api.NetworkUtils;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.GPSTracker;
import com.thyrocare.utils.app.VenuPuntureUtils;
import com.thyrocare.utils.fileutils.DataParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.thyrocare.utils.api.NetworkUtils.isNetworkAvailable;


public class VisitOrderDetailMapDisplayFragmentActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    //started and arrieved
    public static final String TAG_FRAGMENT = VisitOrderDetailMapDisplayFragmentActivity.class.getSimpleName();
    private GoogleMap mMap;
    private ArrayList<LatLng> MarkerPoints;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Button btn_startNav, btn_arrived;
    private FragmentActivity activity;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    private TextView txtName, txtAge, txtSrNo, txtAadharNo;
    private ImageView imgRelease, imgDistance;
    private TextView txtDistance;
    private TextView txtAddress, tv_orderno, oderno_title;

    private LinearLayout llCall;
    private double destlat, destlong, currentlat, currentlong;
    private int Integertotaldiff;
    private boolean isStarted = false;
    private AppPreferenceManager appPreferenceManager;
    private String MaskedPhoneNumber = "";
    private DhbDao dhbDao;
    private Intent FirebaselocationUpdateIntent;
    private OrderDetailsDao orderDetailsDao;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_map_display);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        gpsTracker = new GPSTracker(activity);
        dhbDao = new DhbDao(activity);
        orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        orderVisitDetailsModel = bundle.getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
        initUI();
        initData();
        setListeners();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Initializing
        MarkerPoints = new ArrayList<>();
        FirebaselocationUpdateIntent = new Intent(this, TrackerService.class);


    }

    private void initData() {
        txtAge.setText(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getAge() + " Y | " + orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getGender());
        txtName.setText(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getName());
        txtSrNo.setText("1");//orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getBenId() + "");
        txtDistance.setText(orderVisitDetailsModel.getDistance() + "");
        txtDistance.setVisibility(View.VISIBLE);
        imgDistance.setVisibility(View.VISIBLE);
        imgRelease.setVisibility(View.INVISIBLE);
        tv_orderno.setVisibility(View.INVISIBLE);
        oderno_title.setVisibility(View.INVISIBLE);
        txtAddress.setText(orderVisitDetailsModel.getAllOrderdetails().get(0).getAddress());

        //Abhi Call hide ad unhide
        if (orderVisitDetailsModel.getAllOrderdetails().get(0).isDirectVisit()) {
            llCall.setVisibility(View.GONE);
        } else {
            llCall.setVisibility(View.VISIBLE);
        }
    }

    private void startTrackerService() {


        /*if (DeviceUtils.isMyServiceRunning(TrackerService.class, activity)) {
        } else {*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(FirebaselocationUpdateIntent);
        } else {
            activity.startService(FirebaselocationUpdateIntent);
        }
//            startService(FirebaselocationUpdateIntent);
        //  }
    }

    private void setListeners() {
        btn_arrived.setOnClickListener(this);
        btn_startNav.setOnClickListener(this);

        // double totaldist = distFrom(currentlat, currentlong, destlat, destlong);

        //Integertotaldiff = (int) totaldist;
        llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile()));
                activity.startActivity(intent);*/


                CallPatchRequestModel callPatchRequestModel = new CallPatchRequestModel();
                callPatchRequestModel.setSrcnumber(appPreferenceManager.getLoginResponseModel().getUserID());
                callPatchRequestModel.setDestNumber(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                callPatchRequestModel.setVisitID(orderVisitDetailsModel.getVisitId());
                ApiCallAsyncTask callPatchRequestAsyncTask = new AsyncTaskForRequest(activity).getCallPatchRequestAsyncTask(callPatchRequestModel);
                callPatchRequestAsyncTask.setApiCallAsyncTaskDelegate(new CallPatchRequestAsyncTaskDelegateResult());
                callPatchRequestAsyncTask.execute(callPatchRequestAsyncTask);
            }
        });
//        Toast.makeText(getApplicationContext(),"totaldist"+Integertotaldiff,Toast.LENGTH_SHORT).show();
    }

    private void initUI() {
        llCall = (LinearLayout) findViewById(R.id.ll_call);
        btn_arrived = (Button) findViewById(R.id.btn_arrived);
        btn_startNav = (Button) findViewById(R.id.btn_startNav);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtAge = (TextView) findViewById(R.id.txt_age);
        txtAadharNo = (TextView) findViewById(R.id.txt_aadhar_no);
        txtSrNo = (TextView) findViewById(R.id.txt_sr_no);
        txtDistance = (TextView) findViewById(R.id.tv_distance);
        txtDistance.setMaxEms(2);
        txtAddress = (TextView) findViewById(R.id.title_est_address);
        imgRelease = (ImageView) findViewById(R.id.img_release2);
        imgDistance = (ImageView) findViewById(R.id.title_distance_icon);
        tv_orderno = (TextView) findViewById(R.id.tv_orderno);
        oderno_title = (TextView) findViewById(R.id.oderno_title);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) activity.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
//===========================

                if (gpsTracker.canGetLocation() && !gpsTracker.isInternetAvailable()) {
                    Log.e(TAG_FRAGMENT, "onMapReady: location : " + Double.toString(gpsTracker.getLatitude()) + "long " + gpsTracker.getLongitude());
                    final LatLng currentLocation = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                    //     mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in Sydney"));
                    //    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    } else {
                        MarkerPoints.add(currentLocation);
                        Logger.error("orderVisitDetailsModel lat" + orderVisitDetailsModel.getAllOrderdetails().get(0).getLatitude() + "long " + orderVisitDetailsModel.getAllOrderdetails().get(0).getLongitude());
                        destlat = Double.parseDouble(orderVisitDetailsModel.getAllOrderdetails().get(0).getLatitude());
                        destlong = Double.parseDouble(orderVisitDetailsModel.getAllOrderdetails().get(0).getLongitude());
                        LatLng destTempLocation = new LatLng(destlat, destlong);
                        //                    Toast.makeText(getApplicationContext(),"Destlat"+destlat+"",Toast.LENGTH_SHORT).show();
                        //                    Toast.makeText(getApplicationContext(),"Destlong"+destlong+"",Toast.LENGTH_SHORT).show();
                        MarkerOptions options = new MarkerOptions();
                        options.position(destTempLocation);
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        mMap.addMarker(options);
                        // mMap.addMarker(new MarkerOptions().position(destTempLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_24dp)));
                        LatLng dest = destTempLocation;
                        LatLng origin = currentLocation;
                        String url = getUrl(origin, dest);
                        Logger.error("tyanche " + destlat + "long " + destlong);
                        Logger.error("tyanche1 " + gpsTracker.getLatitude() + "long " + gpsTracker.getLongitude());
                        //String distanceKm=""+distFrom(gpsTracker.getLatitude(),gpsTracker.getLongitude(),destlat,destlong);
                        if (gpsTracker.getLatitude() == 0 || gpsTracker.getLongitude() == 0 || destlat == 0 || destlong == 0) {
                            txtDistance.setText("0 km");
                        } else {
                            txtDistance.setText("" + distFrom(gpsTracker.getLatitude(), gpsTracker.getLongitude(), destlat, destlong));
                        }

                        Log.d("onMapClick", url.toString());
                        FetchUrl FetchUrl = new FetchUrl();

                        // Start downloading json data from Google Directions API
                        FetchUrl.execute(url);
                        //move map camera
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                        Log.e(TAG_FRAGMENT, "onMapReady: ");
                        //    moveToCurrentLocation(currentLocation);
                        //=====================
                    /*    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng point) {
                                // Already two locations
                                if (MarkerPoints.size() > 1) {
                                    MarkerPoints.clear();
                                    mMap.clear();
                                }
                                //1 to create current
                                MarkerPoints.add(point);
                                MarkerOptions options = new MarkerOptions();
                                options.position(point);

                                *//**
                         * For the start location, the color of marker is GREEN and
                         * for the end location, the color of marker is RED.*//*

                                if (MarkerPoints.size() == 1) {
                                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                } else if (MarkerPoints.size() == 2) {
                                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                }


                                // Add new marker to the Google Map Android API V2
                                mMap.addMarker(options);

                                // Checks, whether start and end locations are captured
                                if (MarkerPoints.size() >= 2) {
                                    LatLng origin =currentLocation; MarkerPoints.get(0);

                                    LatLng destTempLocation = new LatLng(19.9975, 73.7898);
                                    LatLng dest = destTempLocation;MarkerPoints.get(1);
    //                                title_distance.setText("" + distance(MarkerPoints.get(0).latitude, MarkerPoints.get(0).longitude, MarkerPoints.get(1).latitude, MarkerPoints.get(1).longitude));
                                    Log.e(TAG_FRAGMENT, "onMapClick: distance " + "" + distance(MarkerPoints.get(0).latitude, MarkerPoints.get(0).longitude, MarkerPoints.get(1).latitude, MarkerPoints.get(1).longitude));
                                    // Getting URL to the Google Directions API
                                    String url = getUrl(origin, dest);
                                    Log.d("onMapClick", url.toString());
                                    FetchUrl FetchUrl = new FetchUrl();

                                    // Start downloading json data from Google Directions API
                                    FetchUrl.execute(url);
                                    //move map camera
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                                }

                            }

                        });*/
                    }
                } else {
                    gpsTracker.showSettingsAlert();
                    Toast.makeText(activity, "Check Internet connection and gps settings", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_arrived.setVisibility(View.GONE);
    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("\n");
                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    private void movingCurrentLocation(GPSTracker gpsTracker, LatLng currentLocation) {

        /*Marker marker =*/
        mMap.addMarker(new MarkerOptions()
                .flat(true)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_menu_camera))
                .anchor(0.5f, 0.5f)
                .position(
                        new LatLng(gpsTracker.getLatitude(), gpsTracker
                                .getLongitude())));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // activity thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        try {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            if (mMap != null) {
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                currentlat = location.getLatitude();
                currentlong = location.getLongitude();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                if (mGoogleApiClient != null) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*  @Override
      protected void onResume() {
          *//*double totaldist = distFrom(currentlat, currentlong, destlat, destlong);
        Integertotaldiff = (int) totaldist;
        if (Integertotaldiff > 100 || !isStarted) {
            btn_arrived.setVisibility(View.GONE);
            btn_startNav.setVisibility(View.VISIBLE);
        } else {
            btn_arrived.setVisibility(View.VISIBLE);
            btn_startNav.setVisibility(View.GONE);
        }*//*
        super.onResume();
    }*/
    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG_FRAGMENT, "received stop broadcast");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            //  stopSelf();
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_arrived) {

            // registerReceiver(stopReceiver, new IntentFilter("stop"));

           /*
            if (DeviceUtils.isMyServiceRunning(TrackerService.class, activity)) {
                Log.e(TAG_FRAGMENT, "onClick1: " );

            } else {
                Log.e(TAG_FRAGMENT, "onClick2: " );

            }
*/


            AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

            alertDialog.setMessage("Do you want to confirm?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            try {
//                                TrackerService.handler1.removeMessages(0);
                                VenuPuntureUtils.ClearVenupumtureTempGlobalArry();
                                activity.stopService(new Intent(getApplicationContext(), TrackerService.class));
                                callOrderStatusChangeApi(3);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
            alertDialog.show();


        } else if (v.getId() == R.id.btn_startNav) {

            if (gpsTracker.isGPSon() /*&& !gpsTracker.isInternetAvailable()*/) {
                startTrackerService();
                callOrderStatusChangeApi(7);
                llCall.setVisibility(View.VISIBLE);
            } else {
                gpsTracker.showSettingsAlert();
                Toast.makeText(activity, "Check Internet connection and gps settings", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void callOrderStatusChangeApi(int status) {

        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();

        orderStatusChangeRequestModel.setId("" + orderVisitDetailsModel.getSlotId());
        orderStatusChangeRequestModel.setStatus(status);
        ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderStatusChangeRequestAsyncTask(orderStatusChangeRequestModel);
        if (status == 3) {
            orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderStatusChangetoArrivedApiAsyncTaskDelegateResult());
        } else {
            orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderStatusChangetoStartApiAsyncTaskDelegateResult());
        }

        if (NetworkUtils.isNetworkAvailable(activity)) {
            orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class OrderStatusChangetoArrivedApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.error(json);
            if (statusCode == 204 || statusCode == 200) {
                for (int i = 0; orderVisitDetailsModel.getAllOrderdetails() != null && i < orderVisitDetailsModel.getAllOrderdetails().size(); i++) {
                    orderVisitDetailsModel.getAllOrderdetails().get(i).setStatus("ARRIVED");
                    orderDetailsDao.insertOrUpdate(orderVisitDetailsModel.getAllOrderdetails().get(i));
                }
                Toast.makeText(activity, "Arrived Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
                setResult(BundleConstants.VOMD_ARRIVED, intent);
                SendinglatlongOrderAllocation(3);

                finish();
            } else {
                if (AppConstants.IS_DEBUG)
                    Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; result != null && i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; path != null && j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0) {
                    boolean allGranted = true;
                    for (int result : grantResults
                    ) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            allGranted = false;
                            break;
                        }
                    }
                    if (allGranted) {
                        // permission was granted. Do the
                        // contacts-related task you need to do.
                        if (ContextCompat.checkSelfPermission(activity,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                            if (mGoogleApiClient == null) {
                                buildGoogleApiClient();
                            }
                            mMap.setMyLocationEnabled(true);
                        }
                    } else {
                        Toast.makeText(activity, "permission denied", Toast.LENGTH_LONG).show();
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on activity permission.
                    Toast.makeText(activity, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            case AppConstants.APP_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + MaskedPhoneNumber));
                    startActivity(intent);
                } else {

                    // Permission denied, Disable the functionality that depends on activity permission.
                    Toast.makeText(activity, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions activity app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);
        float distkm = (float) (dist / 1000);

        return distkm;
    }

    private class OrderStatusChangetoStartApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 204 || statusCode == 200) {
                isStarted = true;
                /*if (Integertotaldiff > 100) {
                    btn_arrived.setVisibility(View.GONE);
                    btn_startNav.setVisibility(View.VISIBLE);
                } else {
                    btn_arrived.setVisibility(View.VISIBLE);
                    btn_startNav.setVisibility(View.GONE);
                }*/
                btn_arrived.setVisibility(View.VISIBLE);
                btn_startNav.setVisibility(View.GONE);
                for (int i = 0; orderVisitDetailsModel.getAllOrderdetails() != null && i < orderVisitDetailsModel.getAllOrderdetails().size(); i++) {
                    orderVisitDetailsModel.getAllOrderdetails().get(i).setStatus("STARTED");
                    orderDetailsDao.insertOrUpdate(orderVisitDetailsModel.getAllOrderdetails().get(i));
                }
                Toast.makeText(activity, "Started Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        //   Uri.parse("google.navigation:q=an+panchavati+nashik"));
                        Uri.parse("google.navigation:q=" + destlat + "," + destlong));
                SendinglatlongOrderAllocation(7);
                startActivity(intent);
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private void SendinglatlongOrderAllocation(int status) {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        OrderAllocationTrackLocationRequestModel orderAllocationTrackLocationRequestModel = new OrderAllocationTrackLocationRequestModel();

        orderAllocationTrackLocationRequestModel.setVisitId(orderVisitDetailsModel.getVisitId());
        orderAllocationTrackLocationRequestModel.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
        orderAllocationTrackLocationRequestModel.setStatus(status);
        //Latlong added
        GPSTracker gpsTracker = new GPSTracker(activity);
        if (gpsTracker.canGetLocation()) {
            orderAllocationTrackLocationRequestModel.setLatitude(String.valueOf(gpsTracker.getLatitude()));
            orderAllocationTrackLocationRequestModel.setLongitude(String.valueOf(gpsTracker.getLongitude()));
        }

        ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderAllocationpost(orderAllocationTrackLocationRequestModel);
        orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderAllocationTrackLocationiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);

        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class OrderAllocationTrackLocationiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Logger.error("" + json);
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private class CallPatchRequestAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    MaskedPhoneNumber = json;
                    intent.setData(Uri.parse("tel:" + MaskedPhoneNumber));
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{
                                        Manifest.permission.CALL_PHONE},
                                AppConstants.APP_PERMISSIONS);
                    } else {
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }
}
