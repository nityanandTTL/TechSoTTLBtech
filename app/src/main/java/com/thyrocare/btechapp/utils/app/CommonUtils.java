package com.thyrocare.btechapp.utils.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.LoginActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.models.api.request.LatLongDataModel;
import com.thyrocare.btechapp.models.api.request.SevenDaysModel;
import com.thyrocare.btechapp.models.api.response.MessageModel;
import com.thyrocare.btechapp.models.data.SampleDropDetailsbyTSPLMEDetailsModel;
import com.thyrocare.btechapp.utils.api.Logger;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static com.thyrocare.btechapp.utils.app.BundleConstants.LOGOUT;

public class CommonUtils {

	/* public static ApiResponseModel getErrorReponseModel(String msg) {
         Gson gson = new Gson();
	     MessageModel errorModel = new MessageModel();

	     errorModel.setStatus("ERROR-BUSSINESS");
	     String messages = msg;
	     errorModel.setMessage(messages);

	     ApiResponseModel apiResponseModel = new ApiResponseModel();
	     apiResponseModel.setResponseData(gson.toJson(errorModel));
	     apiResponseModel.setStatusCode(-888);

	     return apiResponseModel;
	   }*/

    private static CommonUtils instance = null;
    private MessageModel messageModel;
    public static String TSP_NBT_Str = "tsp_nbt_list";

    protected CommonUtils() {
        // Exists only to defeat instantiation.
    }

    public static CommonUtils getInstance() {
        if (instance == null) {
            instance = new CommonUtils();
        }
        return instance;
    }


    public static boolean ValidateCovidorders(String testsCode) {
        if (testsCode != null) {
            return testsCode.toString().trim().contains("COVID-19") || testsCode.toString().trim().contains("COVID 19") || testsCode.toString().trim().contains("COVID19") || testsCode.toString().trim().contains("RTPCR");
        }
        return false;
    }


    public static String encodeImage(Bitmap bm) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();

            return Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encodeImage(byte[] b) {
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap decodeImage(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static byte[] decodedImageBytes(String encodedImage) {
        return Base64.decode(encodedImage, Base64.DEFAULT);
    }

    public static float dpTopx(float dp, Context context) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
        return px;
    }

    public static float getPxFromDp(float dp, Context context) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp,
                context.getResources().getDisplayMetrics());
        return px;
    }

    public static File createDirectory(Context activity) {
        File directoryFile = new File(activity.getFilesDir().getAbsolutePath());
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        return directoryFile;
    }

    public static void HideViewIfStringisEmpty(TextView textView, String strstring) {

        if (textView != null ){
            if (!StringUtils.isNull(strstring)){
                textView.setVisibility(View.VISIBLE);
                textView.setText(strstring);
            }else{
                textView.setVisibility(View.GONE);
            }
        }
    }

    public String getErrorJson(String msg) {
        Gson gson = new Gson();
        MessageModel errorModel = new MessageModel();

        messageModel = new MessageModel();
        MessageModel.FieldError f = new MessageModel.FieldError();
        f.setField("InterNet");
        f.setMessage(msg);

        MessageModel.FieldError[] messages = new MessageModel.FieldError[]{f};

        errorModel.setType("ERROR");
        errorModel.setStatusCode(400);
        errorModel.setMessages(messages);
        Logger.debug(gson.toJson(errorModel));

        return gson.toJson(errorModel);
    }

    public void openAppOnMarket(Activity activity) {
        final String appPackageName = activity.getPackageName();
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static String getAppVersion(Activity activity) {
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Bitmap watermarkImage(Bitmap image, String[] lines) {
        Bitmap.Config config = image.getConfig();
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }

        Bitmap newBitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
        Canvas mCanvas = new Canvas(newBitmap);
        mCanvas.drawBitmap(image, 0, 0, null);

        Paint mPaint = new Paint();

        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.parseColor("#FF444444"));
        paintText.setTextSize((float) (image.getWidth() * 0.03));
        paintText.setStyle(Paint.Style.FILL);

//		int xCo = (int) (image.getWidth() * 0.70);
        int xCo = 5;
        int yCo = 5;

        for (int index = 0; index < lines.length; index++) {
            String currentLine = lines[index];
            Rect rectText = new Rect();
            paintText.getTextBounds(currentLine, 0, currentLine.length(), rectText);
            yCo = yCo + rectText.height() + 5;
            if (index == 2) {
                yCo = yCo + 5;
            }
            mCanvas.drawText(currentLine, xCo, yCo, paintText);
        }

        return newBitmap;

    }

    public static void exportDB(Activity homeScreenActivity) {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + homeScreenActivity.getPackageName()
                + "/databases/dhb_db";
        String backupDBPath = "dhb_db.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir, context);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir, Context context) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]), context);
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static void CallLogOutFromDevice(Context mContext, Activity mActivity, AppPreferenceManager appPreferenceManager, DhbDao dhbDao) {
        try {
            TastyToast.makeText(mContext, "Authorization failed, need to Login again...", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
             new LogUserActivityTagging(mActivity, LOGOUT,"");
                    appPreferenceManager.clearAllPreferences();
            dhbDao.deleteTablesonLogout();
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mActivity.startActivity(homeIntent);
            // stopService(TImeCheckerIntent);
               /* finish();
                finishAffinity();*/

            Intent n = new Intent(mContext, LoginActivity.class);
            n.setAction(Intent.ACTION_MAIN);
            n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(n);
            mActivity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int getNotificationIcon(){

        return R.mipmap.app_nlogo;
    }

    public static String getSampleCount(SampleDropDetailsbyTSPLMEDetailsModel sampleDropDetailsModel) {
        String cnt = "";

        if (sampleDropDetailsModel != null) {
            if (sampleDropDetailsModel.getBarcodeList() != null) {
                if (sampleDropDetailsModel.getBarcodeList().size() != 0) {
                    int smpcnt = 0;
                    for (int i = 0; i < sampleDropDetailsModel.getBarcodeList().size(); i++) {
                        smpcnt = smpcnt + sampleDropDetailsModel.getBarcodeList().get(i).getSampleCount();
                    }

                    cnt = "" + smpcnt;
                }
            }
        }

        return cnt;
    }


    public static boolean isValidForEditing(String tests) {

        if (tests.equalsIgnoreCase(AppConstants.PPBS)
                || tests.equalsIgnoreCase(AppConstants.INSPP)
                || tests.equalsIgnoreCase(AppConstants.RBS)
                || tests.equalsIgnoreCase(AppConstants.PPBS + "," + AppConstants.INSPP)
                || tests.equalsIgnoreCase(AppConstants.PPBS + "," + AppConstants.RBS)
                || tests.equalsIgnoreCase(AppConstants.PPBS + "," + AppConstants.RBS + "," + AppConstants.INSPP)
                || tests.equalsIgnoreCase(AppConstants.PPBS + "," + AppConstants.INSPP + "," + AppConstants.RBS)

                || tests.equalsIgnoreCase(AppConstants.RBS + "," + AppConstants.PPBS)
                || tests.equalsIgnoreCase(AppConstants.RBS + "," + AppConstants.INSPP)
                || tests.equalsIgnoreCase(AppConstants.RBS + "," + AppConstants.PPBS + "," + AppConstants.INSPP)
                || tests.equalsIgnoreCase(AppConstants.RBS + "," + AppConstants.INSPP + "," + AppConstants.PPBS)

                || tests.equalsIgnoreCase(AppConstants.INSPP + "," + AppConstants.PPBS)
                || tests.equalsIgnoreCase(AppConstants.INSPP + "," + AppConstants.RBS)
                || tests.equalsIgnoreCase(AppConstants.INSPP + "," + AppConstants.PPBS + "," + AppConstants.RBS)
                || tests.equalsIgnoreCase(AppConstants.INSPP + "," + AppConstants.RBS + "," + AppConstants.PPBS)
        ) {
            return true;
        }


        return false;
    }

    public static LatLongDataModel getCurrentLatLong(Activity mActivity) {
        LatLongDataModel latLong = new LatLongDataModel();
        String lat = "0.0", longi = "0.0";
        try {
            GPSTracker gpsTracker = new GPSTracker(mActivity);
            if (gpsTracker.canGetLocation()) {
//                lat = new DecimalFormat("####.##########").format(gpsTracker.getLatitude());
                lat = String.valueOf(gpsTracker.getLatitude());
//                longi = new DecimalFormat("####.##########").format(gpsTracker.getLongitude());
                longi = String.valueOf(gpsTracker.getLongitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        latLong.setmLatitude(lat);
        latLong.setmLongitude(longi);
        return latLong;
    }



    /*public static String getIPAddress(Activity activity) {
        WifiManager wm = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        return wm != null && wm.getConnectionInfo() != null ? Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress()) : "";
    }*/

    /*public static String getMACAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }*/

    public static String getMACAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : networkInterfaces) {
                List<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress inetAddress : inetAddresses) {
                    if (!inetAddress.isLoopbackAddress()) {
                        String sAddr = inetAddress.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                // drop ip6 port suffix
                                int delim = sAddr.indexOf('%');
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }


    public static ArrayList<SevenDaysModel> getSevenDays() {
        SimpleDateFormat sdfday = new SimpleDateFormat("EEE");
        SimpleDateFormat sdfdate = new SimpleDateFormat("dd");
        SimpleDateFormat sdfmonth = new SimpleDateFormat("MMM");
        SimpleDateFormat sdffulldate = new SimpleDateFormat("yyyy-MM-dd");
        String day = "", date = "", month = "", fulldate = "";
        ArrayList<SevenDaysModel> arrayList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, i);
            day = sdfday.format(calendar.getTime());
            date = sdfdate.format(calendar.getTime());
            month = sdfmonth.format(calendar.getTime());
            fulldate = sdffulldate.format(calendar.getTime());
            SevenDaysModel sevenDaysModel = new SevenDaysModel();
            sevenDaysModel.setDate(date);
            sevenDaysModel.setFulldate(fulldate);
            sevenDaysModel.setDay(day);
            sevenDaysModel.setMonth(month);
            arrayList.add(sevenDaysModel);
        }
        return arrayList;
    }
}
