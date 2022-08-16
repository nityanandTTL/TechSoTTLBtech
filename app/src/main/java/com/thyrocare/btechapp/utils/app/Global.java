package com.thyrocare.btechapp.utils.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.appbar.AppBarLayout;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Global {

    public static String PE_BTech="3";
    public static String Prepaid = "PREPAID";
    private Context context;
    public String fontDefault = "OPENSANS-REGULAR_3.TTF";
    public String fontRegular = "OPENSANS-REGULAR_3.TTF";
    public String fontLight = "OPENSANS-LIGHT_3.TTF";
    public String fontBold = "OPENSANS-BOLD_2.TTF";
    public String fontSemiBold = "OPENSANS-SEMIBOLD_2.TTF";
    public String fontItalic = "OPENSANS-ITALIC_2.TTF";

    public String tableAarogyam = "Aarogyam";
    public String tableProfile = "Profile";
    public String tableTests = "Tests";
    public String tableOffer = "Offer";
    public String tableOfferCart = "OfferCart";
    public String tableThyronomicOfferCart = "ThyronomicOfferCart";

    public String tableAarogyamChilds = "Aarogyam_childs";
    public String tableProfileChilds = "Profile_childs";
    public String tableTestsChilds = "Tests_childs";
    public String tableOfferChilds = "Offer_childs";
    public String tableOfferCartChilds = "OfferCart_childs";
    public String tableThyronomicOfferCartChilds = "ThyronomicOfferCart_childs";

    public static int selectedPosition;
    public static int selectedRemarksID;
    public static boolean todisplaytimerforPosition =false;

    public String tableCart = "Cart";
    //Live ---------------------------

    ProgressDialog progressDialog;
    public static boolean callAPIEditOrder = false;
    public static boolean toDisplayTimerFlag = false;
    public static boolean TimerFlag = true;
    public static boolean displayedtimer=false;

    public Global(Context context) {
        this.context = context;
    }

    public static void showCustomStaticToast(Context context, String message) {

        if (context != null && !InputUtils.isNull(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean checkLogin(String s){
        return s != null && s.equalsIgnoreCase(Global.PE_BTech);
    }

    public static boolean getDays(Activity activity) {
        SharedPreferences preferences = null;
        preferences = activity.getSharedPreferences(AppConstants.RateCalDate, 0);
        if (InputUtils.isNull(preferences.getString("date", ""))) {
            return true;
        }
        Date storedDate = returnDate(preferences.getString("date", ""));
        Date currentDate = returnDate(getCurrentDate());
        return storedDate != null && storedDate.before(currentDate);
    }

    public static Date returnDate(String putDate) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            date = sdf.parse(putDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }
    public static String getCurrentDateandTime() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
    }


    public static void Storecurrenttime(Activity activity) {
        try {
            SharedPreferences.Editor editor = null;
            editor = activity.getSharedPreferences(AppConstants.RateCalDate, 0).edit();
            editor.putString("date", getCurrentDate());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void DsaResponseModel(){

    }


    public String convertNumberToPrice(String s) {
        Double price = Double.parseDouble(s);
        Locale locale = new Locale("en", "IN");
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        if (checkForApi14())
            symbols.setCurrencySymbol("\u20B9"); // Don't use null.
        else
            symbols.setCurrencySymbol("\u20A8"); // Don't use null.
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setMaximumFractionDigits(0);
        //MessageLogger.PrintMsg(formatter.format(price));
        s = formatter.format(price);
        return s;
    }

    public Boolean checkForApi11() {
        Boolean boolStatus = false;
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.HONEYCOMB) {
            boolStatus = true;
        } else {
            boolStatus = false;
        }
        return boolStatus;
    }

    public Boolean checkForApi14() {
        Boolean boolStatus = false;
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            boolStatus = true;
        } else {
            boolStatus = false;
        }
        return boolStatus;
    }

    public Boolean checkForApi21() {
        Boolean boolStatus = false;
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            boolStatus = true;
        } else {
            boolStatus = false;
        }
        return boolStatus;
    }

    public void showCustomToast(Activity activity, String message) {
        Context context = activity.getApplicationContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        RelativeLayout relItem = (RelativeLayout) toastRoot.findViewById(R.id.relItem);
        TextView txtToast = (TextView) toastRoot.findViewById(R.id.txtToast);

        relItem.getBackground().setAlpha(204);
        txtToast.setText(message);

        Toast toast = new Toast(context);
        toast.setView(toastRoot);
        //toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public void showCustomToast(Activity activity, String message, int Length) {
        Context context = activity.getApplicationContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        RelativeLayout relItem = (RelativeLayout) toastRoot.findViewById(R.id.relItem);
        TextView txtToast = (TextView) toastRoot.findViewById(R.id.txtToast);

        relItem.getBackground().setAlpha(204);
        txtToast.setText(message);

        Toast toast = new Toast(context);
        toast.setView(toastRoot);
        //toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Length);
        toast.show();
    }

    public void showcenterCustomToast(Activity activity, String message, int lengthLong) {
        Context context = activity.getApplicationContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        RelativeLayout relItem = (RelativeLayout) toastRoot.findViewById(R.id.relItem);
        TextView txtToast = (TextView) toastRoot.findViewById(R.id.txtToast);

        relItem.getBackground().setAlpha(204);
        txtToast.setText(message);

        Toast toast = new Toast(context);
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(lengthLong);
        toast.show();
    }


    public void showProgressDialog(Activity activity, String msg) {

        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(null);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        try {
            if (progressDialog != null && !progressDialog.isShowing())
                if (!activity.isFinishing()) {
                    progressDialog.show();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgressDialog(Context context, String msg) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(null);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        try {
            if (progressDialog != null && !progressDialog.isShowing())
                progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgressDialog(Activity activity, String msg, boolean IsCancelable) {

        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(null);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(IsCancelable);

        try {
            if (progressDialog != null && !progressDialog.isShowing())

                if (!activity.isFinishing()) {
                    progressDialog.show();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgressDialogg() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgressDialog(Activity activity) {

        try {
            if (activity != null && !activity.isFinishing() && progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String formatDate(String currentFormat, String outputFormat, String date) {

        SimpleDateFormat curFormater = new SimpleDateFormat(currentFormat);
        SimpleDateFormat postFormater = new SimpleDateFormat(outputFormat);
        //SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy");
        //SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(date);
            date = postFormater.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String toCamelCase(String inputString) {
        String result = "";
        if (inputString.length() == 0) {
            return result;
        }
        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            char previousChar = inputString.charAt(i - 1);
            if (previousChar == ' ') {
                char currentCharToUpperCase = Character.toUpperCase(currentChar);
                result = result + currentCharToUpperCase;
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
        }
        return result;
    }

    public static String toUpperCase(String inputString) {
        String result = "";
        for (int i = 0; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            char currentCharToUpperCase = Character.toUpperCase(currentChar);
            result = result + currentCharToUpperCase;
        }
        return result;
    }

    public static String toLowerCase(String inputString) {
        String result = "";
        for (int i = 0; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            char currentCharToLowerCase = Character.toLowerCase(currentChar);
            result = result + currentCharToLowerCase;
        }
        return result;
    }

    public static String toSentenceCase(String inputString) {
        String result = "";
        if (inputString.length() == 0) {
            return result;
        }
        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        boolean terminalCharacterEncountered = false;
        char[] terminalCharacters = {'.', '?', '!'};
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            if (terminalCharacterEncountered) {
                if (currentChar == ' ') {
                    result = result + currentChar;
                } else {
                    char currentCharToUpperCase = Character.toUpperCase(currentChar);
                    result = result + currentCharToUpperCase;
                    terminalCharacterEncountered = false;
                }
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
            for (int j = 0; j < terminalCharacters.length; j++) {
                if (currentChar == terminalCharacters[j]) {
                    terminalCharacterEncountered = true;
                    break;
                }
            }
        }
        return result;
    }

    public boolean checkValidation(String type, int length) {
        boolean result = false;
        if (type.equals("mobile")) {
            result = length != 10 ? false : true;
        } else if (type.equals("address")) {
            result = length <= 25 ? false : true;
        }

        return result;
    }

    public String checkJsonNullStringValue(JSONObject jsonObject, String key) {
        String value = "";
        try {
            value = jsonObject.isNull(key) ? "" : jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    public void showalert_OK(String message, Context context) {

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showAlert_OK_WithTitle(String message, Context context, String title) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        alertDialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void DisplayImagewithoutDefaultImage(Activity activity, String Url, ImageView imageView) {

        try {
            if (!InputUtils.isNull(Url)) {
                GlideUrl glideUrl = new GlideUrl(Url, new LazyHeaders.Builder()
                        .addHeader(Constants.HEADER_USER_AGENT, getHeaderValue(activity))
                        .build());

                Glide.with(activity)
                        .asBitmap()
                        .load(glideUrl)
                        //                .diskCacheStrategy(DiskCacheStrategy.NONE)
                        //                .skipMemoryCache(true)
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DisplayDeviceImages(Activity activity, String Url, ImageView imageView) {

        try {
            Glide.with(activity)
                    .load(Url.replace("\\", "/"))
                    .load(Url.replace("\n",""))
                    .placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void DisplayImagewithDefaultImage(Activity activity, String Url, ImageView imageView) {

        try {
            if (!InputUtils.isNull(Url)) {
                GlideUrl glideUrl = new GlideUrl(Url, new LazyHeaders.Builder()
                        .addHeader(Constants.HEADER_USER_AGENT, getHeaderValue(activity))
                        .build());

                Glide.with(activity)
                        .asBitmap()
                        .load(glideUrl)
                        .placeholder(R.drawable.app_logo).dontAnimate()
                        .error(R.drawable.app_logo)
                        //                .diskCacheStrategy(DiskCacheStrategy.NONE)
                        //                .skipMemoryCache(true)
                        .into(imageView);
            } else {
                Glide.with(activity)
                        .asBitmap()
                        .load("")
                        .placeholder(R.drawable.app_logo).dontAnimate()
                        .error(R.drawable.app_logo)
                        //                .diskCacheStrategy(DiskCacheStrategy.NONE)
                        //                .skipMemoryCache(true)
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void iconImage(Bitmap bitmap){

    }



    public void OpenImageDialog(String imgUrl, Activity mActivity, boolean isFromURL) {

        try {
            final Dialog openDialog = new Dialog(mActivity);
            openDialog.setContentView(R.layout.image_dialog);
            int width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.99);
            int height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.90);
            openDialog.getWindow().setLayout(width, height);
            openDialog.setTitle("");

            ImageView img_close = (ImageView) openDialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDialog.dismiss();
                }
            });

            ImageView imageview = (ImageView) openDialog.findViewById(R.id.imageview);
            if (isFromURL) {
                DisplayImagewithDefaultImage(mActivity, imgUrl.replace("\\", "/"), imageview);
            } else {

                DisplayDeviceImages(mActivity, imgUrl.replace("\\", "/"), imageview);
            }


            openDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OpenBase64ImageDialog(String base64Image, Activity mActivity) {

        try {
            final Dialog openDialog = new Dialog(mActivity);
            openDialog.setContentView(R.layout.image_dialog);
            int width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.99);
            int height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.90);
            openDialog.getWindow().setLayout(width, height);
            openDialog.setTitle("");

            ImageView img_close = (ImageView) openDialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDialog.dismiss();
                }
            });

            ImageView imageview = (ImageView) openDialog.findViewById(R.id.imageview);
            byte[] imageByteArray = Base64.decode(base64Image, Base64.DEFAULT);

            Glide.with(mActivity)
                    .asBitmap()
                    .load(imageByteArray)
                    .placeholder(R.drawable.app_logo).dontAnimate()
                    .error(R.drawable.app_logo)
                    .into(imageview);

            openDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getHeaderValue(Context pContext) {
        String header;
        header = "BtechApp/" + getUserCode(pContext) + getCurrentAppVersionName(pContext) + "(" + getCurrentVersionCode(pContext) + ")/" + getSerialnum(pContext);
        return header;
    }


    public static String getUserCode(Context pContext) {
        String user = "";
        String usercode = "";
        String userName = "";
        try {
            AppPreferenceManager appPreferenceManager = new AppPreferenceManager(pContext);
            usercode = appPreferenceManager.getLoginResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID()) ? appPreferenceManager.getLoginResponseModel().getUserID().trim().replace(" ", "") + "/" : "";
//            userName = !InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getName()) ? appPreferenceManager.getLoginResponseModel().getName().trim().replace(" ","")+"/" : "Default/" ;

            user = usercode + userName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public static int getCurrentVersionCode(Context pContext) {
        int currentAppVersion = 0;
        try {
            currentAppVersion = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentAppVersion;
    }

    public static String getCurrentAppVersionName(Context pContext) {
        String versionName = "";
        try {
            PackageInfo packageInfo = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getSerialnum(Context pContext) {
        String imeiNo = "";
        try {
            imeiNo = Settings.Secure.getString(pContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imeiNo;
    }


    public void EnterBarcodeManually(Activity mActivity, final OnBarcodeDialogSubmitClickListener onBarcodeDialogSubmitClickListener) {
        try {
            final Dialog openDialog = new Dialog(mActivity);
            openDialog.setContentView(R.layout.manual_barcode_entry);
            int width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.99);
            openDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            openDialog.setTitle("Manual Barcode Entry");

            ImageView img_close = (ImageView) openDialog.findViewById(R.id.img_close);
            final EditText edt_enterBarcode = (EditText) openDialog.findViewById(R.id.edt_enterBarcode);
            Button btn_submit = (Button) openDialog.findViewById(R.id.btn_submit);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDialog.dismiss();
                }
            });

            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog.dismiss();
                    if (onBarcodeDialogSubmitClickListener != null) {
                        onBarcodeDialogSubmitClickListener.onSubmitButtonClicked(edt_enterBarcode.getText().toString());
                    }
                }
            });

            openDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnBarcodeDialogSubmitClickListener {
        public void onSubmitButtonClicked(String barcode);
    }


    public static void appBar(Activity activity){
        AppBarLayout appBarLayout = activity.findViewById(R.id.appbarLayout);
        appBarLayout.setVisibility(View.GONE);
    }


    public static void cropImageFullScreenActivity(Activity activity, int flag) {
        if (flag == 1) {
            ImagePicker.Companion.with(activity).crop().compress(Constants.MaxImageSize)
                    .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight).start();
        } else if (flag == 0) {
            ImagePicker.Companion.with(activity).crop().cameraOnly().compress(Constants.MaxImageSize)
                    .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight).start();
        } else if (flag == 2) {
            ImagePicker.Companion.with(activity).crop().galleryOnly().compress(Constants.MaxImageSize)
                    .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight).start();
        }else if (flag == 3){
            ImagePicker.Companion.with(activity).cameraOnly().compress(Constants.MaxImageSize)
                    .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight).start();
        }
    }

    public static void cropImageFullScreenFragment(Fragment fragment, int flag) {

        if (flag == 1) {
            ImagePicker.Companion.with(fragment).crop().compress(Constants.MaxImageSize)
                    .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight).start();
        } else if (flag == 0) {
            ImagePicker.Companion.with(fragment).crop().compress(Constants.MaxImageSize)
                    .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight).cameraOnly().start();
        } else if (flag == 2) {
            ImagePicker.Companion.with(fragment).crop().compress(Constants.MaxImageSize)
                    .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight).galleryOnly().start();
        }
    }
}
