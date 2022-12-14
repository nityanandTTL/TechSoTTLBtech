package com.thyrocare.btechapp.activity;

import android.Manifest;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;

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
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.OlcStartRequestModel;
import com.thyrocare.btechapp.models.data.BtechClientsModel;



import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.api.NetworkUtils;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.GPSTracker;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.fileutils.DataParser;

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

import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

/**
 * /ClientPickup
 */


public class OLCPickupDetailMapDisplayFragmentActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    private static final String TAG = OLCPickupDetailMapDisplayFragmentActivity.class.getSimpleName();
    public static final String TAG_FRAGMENT = OLCPickupDetailMapDisplayFragmentActivity.class.getSimpleName();
    private GoogleMap mMap;
    private ArrayList<LatLng> MarkerPoints;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Button btn_startNav, btn_arrived;

    private FragmentActivity activity;
    private BtechClientsModel btechClientsModel;

    private TextView txtName, txtAge, txtSrNo, txtAadharNo;
    private ImageView imgRelease, imgDistance;
    private TextView txtDistance,txtorder_no,txt_title;
    private TextView txtAddress;
    private AppPreferenceManager appPreferenceManager;
    private Geocoder geocoder;
    private List<Address> addresses;
    private ImageView title_aadhar_icon, title_distance_icon;
    private double destlat,destlong,currentlat,currentlong;
    private int Integertotaldiff;
    private boolean isStarted = false;
    private LinearLayout llCall;
    private Global global;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_map_display);
        activity = this;
        global = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        btechClientsModel = getIntent().getExtras().getParcelable(BundleConstants.BTECH_CLIENTS_MODEL);

        initUI();
        initData();
        setListeners();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        MarkerPoints = new ArrayList<>();

    }


    private void setListeners() {
        btn_arrived.setOnClickListener(this);
        btn_startNav.setOnClickListener(this);
        double totaldist = distFrom(currentlat,currentlong,destlat,destlong);

        Integertotaldiff = (int) totaldist;
//        Toast.makeText(getApplicationContext(),"totaldist"+Integertotaldiff,Toast.LENGTH_SHORT).show();
        btn_arrived.setVisibility(View.GONE);
    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("\n");
                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            MessageLogger.LogError("tag", e.getMessage());
        }

        return result.toString();
    }

    private void initUI() {
        txtorder_no=(TextView) findViewById(R.id.tv_orderno);
        txt_title=(TextView) findViewById(R.id.oderno_title);
        btn_arrived = (Button) findViewById(R.id.btn_arrived);
        btn_startNav = (Button) findViewById(R.id.btn_startNav);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtAge = (TextView) findViewById(R.id.txt_age);
        llCall = (LinearLayout) findViewById(R.id.ll_call);
        llCall.setVisibility(View.GONE);
        txtAge.setVisibility(View.INVISIBLE);
        txtAadharNo = (TextView) findViewById(R.id.txt_aadhar_no);
        txtAadharNo.setVisibility(View.INVISIBLE);
        txtSrNo = (TextView) findViewById(R.id.txt_sr_no);
        txtDistance = (TextView) findViewById(R.id.tv_distance);
        txtAddress = (TextView) findViewById(R.id.title_est_address);
        imgRelease = (ImageView) findViewById(R.id.img_release2);
        imgDistance = (ImageView) findViewById(R.id.title_distance_icon);
        title_aadhar_icon = (ImageView) findViewById(R.id.title_aadhar_icon);
        title_distance_icon = (ImageView) findViewById(R.id.title_distance_icon);

        txtorder_no.setVisibility(View.GONE);
        txt_title.setVisibility(View.GONE);
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
                    MessageLogger.LogError(TAG, "onMapReady: location : " + Double.toString(gpsTracker.getLatitude()) + "long " + gpsTracker.getLongitude());

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
                    MarkerPoints.add(currentLocation);
                    Logger.error("btechClientsModel lat" + btechClientsModel.getLatitude() + "long " + btechClientsModel.getLongitude());
                    destlat= Double.parseDouble(btechClientsModel.getLatitude());
                    destlong= Double.parseDouble(btechClientsModel.getLongitude());
//                    Toast.makeText(getApplicationContext(),"Destlat"+destlat+"",Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(),"Destlong"+destlong+"",Toast.LENGTH_SHORT).show();
                    LatLng destTempLocation = new LatLng(destlat, destlong);

                    String url = getUrl(currentLocation, destTempLocation);
                    MarkerOptions options = new MarkerOptions();
                    options.position(destTempLocation);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mMap.addMarker(options);
                    MessageLogger.LogDebug("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                    MessageLogger.LogError(TAG, "onMapReady: ");

                } else {
                    gpsTracker.showSettingsAlert();
                    Toast.makeText(activity, "Check Internet connection and gps settings", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        // Camp format
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

    @Override
    protected void onResume() {
     /*   double totaldist = distFrom(currentlat, currentlong, destlat, destlong);
        Integertotaldiff = (int) totaldist;
        if (Integertotaldiff > 100 || !isStarted) {
            btn_arrived.setVisibility(View.GONE);
            btn_startNav.setVisibility(View.VISIBLE);
        } else {
            btn_arrived.setVisibility(View.VISIBLE);
            btn_startNav.setVisibility(View.GONE);
        }*/
        super.onResume();
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

        currentlat=location.getLatitude();
        currentlong=location.getLongitude();

//        Toast.makeText(getApplicationContext(),latLng+"", Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),currentlat+"", Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),currentlong+"", Toast.LENGTH_SHORT).show();
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_arrived) {
            callOrderStatusChangeApi(3);
        } else if (v.getId() == R.id.btn_startNav) {
            callOrderStatusChangeApi(7);
        }
    }

    private void callOrderStatusChangeApi(int status) {
        OlcStartRequestModel olcStartRequestModel = new OlcStartRequestModel();
        olcStartRequestModel.setClientId("" + btechClientsModel.getClientId());
        olcStartRequestModel.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
        olcStartRequestModel.setType(status);
        if (NetworkUtils.isNetworkAvailable(activity)) {
            CallGetOlcStartRequestApi(olcStartRequestModel);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        //  txtAge.setText(hubBTechModel.getCutOffTime());
        txtName.setText(btechClientsModel.getName());
        txtDistance.setText("" + btechClientsModel.getDistance() + " Km");
        txtAddress.setText(btechClientsModel.getAddress() + " " + btechClientsModel.getPincode());
        imgRelease.setVisibility(View.INVISIBLE);
        txtDistance.setVisibility(View.VISIBLE);
        title_aadhar_icon.setVisibility(View.INVISIBLE);
        title_distance_icon.setVisibility(View.VISIBLE);
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
                MessageLogger.LogDebug("Background Task data", data.toString());
            } catch (Exception e) {
                MessageLogger.LogDebug("Background Task", e.toString());
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
            urlConnection.setRequestProperty(Constants.HEADER_USER_AGENT, Global.getHeaderValue(activity));
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
            MessageLogger.LogDebug("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            MessageLogger.LogDebug("Exception", e.toString());
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
                MessageLogger.LogDebug("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                MessageLogger.LogDebug("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                MessageLogger.LogDebug("ParserTask", "Executing routes");
                MessageLogger.LogDebug("ParserTask", routes.toString());

            } catch (Exception e) {
                MessageLogger.LogDebug("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
if(result!=null){
    // Traversing through all the routes
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

        MessageLogger.LogDebug("onPostExecute", "onPostExecute lineoptions decoded");

    }

    // Drawing polyline in the Google Map for the i-th route
    if (lineOptions != null) {
        mMap.addPolyline(lineOptions);
    } else {
        MessageLogger.LogDebug("onPostExecute", "without Polylines drawn");
    }
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

            // other 'case' lines to check for other permissions activity app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void CallGetOlcStartRequestApi(final OlcStartRequestModel olcStartRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallGetOlcStartRequestApi(olcStartRequestModel);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    if (olcStartRequestModel.getType() == 3){
                        Intent intentResult = new Intent();
                        intentResult.putExtra(BundleConstants.BTECH_CLIENTS_MODEL, btechClientsModel);
                        setResult(BundleConstants.BCMD_ARRIVED, intentResult);
                        finish();
                    }else{
                        btn_startNav.setVisibility(View.GONE);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+destlat+","+destlong));
                        startActivity(intent);
                        btn_arrived.setVisibility(View.VISIBLE);
                    }
                }else{
                    global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
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
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

   /* @Override
    protected void onPostResume() {

        btn_arrived.setVisibility(View.VISIBLE);
        super.onPostResume();
    }*/
}
