package com.thyrocare.utils.app;

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

import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.R;
import com.thyrocare.activity.LoginScreenActivity;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.models.api.response.MessageModel;
import com.thyrocare.utils.api.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

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
            appPreferenceManager.clearAllPreferences();
            dhbDao.deleteTablesonLogout();
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mActivity.startActivity(homeIntent);
            // stopService(TImeCheckerIntent);
               /* finish();
                finishAffinity();*/

            Intent n = new Intent(mContext, LoginScreenActivity.class);
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
}