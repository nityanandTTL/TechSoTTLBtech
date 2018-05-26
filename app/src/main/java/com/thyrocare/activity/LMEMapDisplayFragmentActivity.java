package com.thyrocare.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.thyrocare.Controller.TSPLMESampleDropController;
import com.thyrocare.R;
import com.thyrocare.application.ApplicationController;
import com.thyrocare.models.data.HUBBTechModel;
import com.thyrocare.models.data.SampleDropDetailsbyTSPLMEDetailsModel;
import com.thyrocare.models.data.ScannedMasterBarcodebyLMEPOSTDATAModel;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.GPSTracker;
import com.thyrocare.utils.fileutils.DataParser;

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


public class LMEMapDisplayFragmentActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    private static final String TAG = LMEMapDisplayFragmentActivity.class.getSimpleName();
    public static final String TAG_FRAGMENT = LMEMapDisplayFragmentActivity.class.getSimpleName();
    private GoogleMap mMap;
    private ArrayList<LatLng> MarkerPoints;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private ArrayList<HUBBTechModel> hubbTechModels;
    private LocationRequest mLocationRequest;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Button btn_startaccept;

    private FragmentActivity activity;
    private SampleDropDetailsbyTSPLMEDetailsModel mSampleDropDetailsbyTSPLMEDetailsModel;
    TextView txt_code, txt_cnt, txt_name, txt_address;

    private AppPreferenceManager appPreferenceManager;
    private Geocoder geocoder;
    private List<Address> addresses;
    private double destlat = 0.0, destlong = 0.0, currentlat, currentlong;
    private int Integertotaldiff;
    private boolean isStarted = false;
    private LatLng mAddressLatLong;
    LMEMapDisplayFragmentActivity mLMEMapDisplayFragmentActivity;
    LinearLayout ll_scan_master_barcode;
    private IntentIntegrator intentIntegrator;
    private String master_scanned_barcode = "";
    TextView scanned_barcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmeactivity_navigation_map_display);
        activity = this;
        mLMEMapDisplayFragmentActivity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        mSampleDropDetailsbyTSPLMEDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.LME_ORDER_MODEL);

        initUI();
        initData();
        setListeners();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Initializing
        MarkerPoints = new ArrayList<>();

    }


    private void setListeners() {
        btn_startaccept.setOnClickListener(this);
        double totaldist = distFrom(currentlat, currentlong, destlat, destlong);

        Integertotaldiff = (int) totaldist;
    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                //   Toast.makeText(getApplicationContext(),"Address:"+addresses+"",Toast.LENGTH_SHORT).show();
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("\n");
                result.append(address.getCountryName());

            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    private void initUI() {

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_code = (TextView) findViewById(R.id.txt_code);
        txt_cnt = (TextView) findViewById(R.id.txt_cnt);

        ll_scan_master_barcode = (LinearLayout) findViewById(R.id.ll_scan_master_barcode);
        ll_scan_master_barcode.setOnClickListener(this);

        btn_startaccept = (Button) findViewById(R.id.btn_startaccept);
        scanned_barcode = (TextView) findViewById(R.id.scanned_barcode);

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
                GPSTracker gpsTracker = new GPSTracker(activity);
                if (gpsTracker.canGetLocation() && !gpsTracker.isInternetAvailable()) {
                    Log.e(TAG, "onMapReady: location : " + Double.toString(gpsTracker.getLatitude()) + "long " + gpsTracker.getLongitude());
                    final LatLng currentLocation = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                    try {
                        mAddressLatLong = getLocationFromAddress(mSampleDropDetailsbyTSPLMEDetailsModel.getAddress() + "-" + mSampleDropDetailsbyTSPLMEDetailsModel.getPincode());
                        if (mAddressLatLong != null) {
                            destlat = Double.parseDouble("" + mAddressLatLong.latitude);
                            destlong = Double.parseDouble("" + mAddressLatLong.longitude);
                        } else {
                            destlat = Double.parseDouble("0.0");
                            destlong = Double.parseDouble("0.0");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        destlat = Double.parseDouble("0.0");
                        destlong = Double.parseDouble("0.0");
                    }
                    MarkerPoints.add(currentLocation);

                    LatLng destTempLocation = new LatLng(destlat, destlong);
                    MarkerOptions options = new MarkerOptions();
                    options.position(destTempLocation);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mMap.addMarker(options);
                    // mMap.addMarker(new MarkerOptions().position(destTempLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_24dp)));
                    LatLng dest = destTempLocation;
                    LatLng origin = currentLocation;
                    String url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                    Log.e(TAG, "onMapReady: ");

                } else {
                    gpsTracker.showSettingsAlert();
                    Toast.makeText(activity, "Check Internet connection and gps settings", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(activity);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;

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
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        currentlat = location.getLatitude();
        currentlong = location.getLongitude();

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean validate() {
        if (master_scanned_barcode.toString().trim().length() != 8 && master_scanned_barcode.toString().trim().length() >= 1) {
            scanned_barcode.setText("");
            Toast.makeText(activity, "Invalid master barcode", Toast.LENGTH_SHORT).show();
            return false;
        } else if (master_scanned_barcode.equals("")) {
            CallDialog_a();
            return false;
        }
        return true;
    }

    private void CallDialog_a() {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

        alertDialog.setMessage("Accept without master Barcode?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        StartPostScannedMasterBarcodebyLME(mSampleDropDetailsbyTSPLMEDetailsModel);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_startaccept) {
            if (validate()) {
                StartPostScannedMasterBarcodebyLME(mSampleDropDetailsbyTSPLMEDetailsModel);
            }
        } else if (v.getId() == R.id.ll_scan_master_barcode) {
            scanFromFragment();
        }
    }

    public void scanFromFragment() {
        intentIntegrator = new IntentIntegrator(activity) {
            @Override
            protected void startActivityForResult(Intent intent, int code) {
                mLMEMapDisplayFragmentActivity.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
            }
        };
        intentIntegrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null && scanningResult.getContents() != null) {
            if (scanningResult.getContents().startsWith("0") || scanningResult.getContents().startsWith("$")) {
                Toast.makeText(activity, "Invalid Barcode", Toast.LENGTH_SHORT).show();
            } else {
                master_scanned_barcode = scanningResult.getContents();
                if (master_scanned_barcode.toString().trim().length() != 8) {
                    master_scanned_barcode = "";
                    Toast.makeText(activity, "Invalid master barcode", Toast.LENGTH_SHORT).show();
                } else {
                    scanned_barcode.setText("" + master_scanned_barcode.toString().trim());
                    Toast.makeText(activity, "Master barcode scanned successfully", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Logger.error("Cancelled from fragment");
        }
    }

    private void StartPostScannedMasterBarcodebyLME(SampleDropDetailsbyTSPLMEDetailsModel sampleDropDetailsbyTSPLMEDetailsModel) {
        ScannedMasterBarcodebyLMEPOSTDATAModel n = null;
        try {
            GPSTracker gpsTracker = new GPSTracker(activity);
            n = new ScannedMasterBarcodebyLMEPOSTDATAModel();
            n.setMasterBarcode("" + master_scanned_barcode.toString().trim());
            n.setSampleDropIds("" + sampleDropDetailsbyTSPLMEDetailsModel.getSampleDropId());
            n.setStatus("3");
            n.setLatitude(String.valueOf(gpsTracker.getLatitude()));
            n.setLongitude(String.valueOf(gpsTracker.getLongitude()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ApplicationController.mTSPLMESampleDropController != null) {
            ApplicationController.mTSPLMESampleDropController = null;
        }

        ApplicationController.mTSPLMESampleDropController = new TSPLMESampleDropController(activity, mLMEMapDisplayFragmentActivity);
        ApplicationController.mTSPLMESampleDropController.CallPostScannedMasterBarcodebyLME(n);
    }

    private void initData() {
        txt_code.setText("" + mSampleDropDetailsbyTSPLMEDetailsModel.getSourceCode());
        txt_cnt.setText("" + mSampleDropDetailsbyTSPLMEDetailsModel.getSampleCount());
        txt_name.setText("" + mSampleDropDetailsbyTSPLMEDetailsModel.getName());
        txt_address.setText("" + mSampleDropDetailsbyTSPLMEDetailsModel.getAddress() + "-" + mSampleDropDetailsbyTSPLMEDetailsModel.getPincode());
    }

    public void EndButtonClickedSuccess() {
        Intent intentResult = new Intent();
        intentResult.putExtra(BundleConstants.HUB_BTECH_MODEL, mSampleDropDetailsbyTSPLMEDetailsModel);
        setResult(BundleConstants.HMD_ARRIVED, intentResult);
        finish();
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

            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
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
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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

                    // Permission denied, Disable the functionality that depends on activity permission.
                    Toast.makeText(activity, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
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
        return dist;
    }
}
