package com.thyrocare.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.thyrocare.R;
import com.thyrocare.adapter.SpinnerAdapter;
import com.thyrocare.dao.utils.ConnectionDetector;
import com.thyrocare.network.AbstractApiModel;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.Global;
import com.thyrocare.utils.app.Validator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class FeedbackFragment extends Fragment {

    public static final String TAG_FRAGMENT = FeedbackFragment.class.getSimpleName();
    private Activity mActivity;
    private ImageView imgHappy, imgNeutral, imgSad;
    private TextView txtHappy, txtNeutral, txtSad;
    private Spinner spinType;
    private EditText edtName, edtEmail, edtMobile, edtQuery;
    private ScrollView mScrollView;
    private Button btnSubmit;

    private ArrayList<String> arrFeedback, arrComplain;
    private SpinnerAdapter adapterString;
    private String strType="", strEmotionsType="", strRating="", strMobile="", strEmail="", strName="", strQuery="";

    private AlertDialog.Builder alertDialogBuilder;
    private Global globalClass;
    private ConnectionDetector cd;
    private SharedPreferences pref, prefs_user;
    private String deviceInfo="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }
    public static FeedbackFragment newInstance() {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        imgHappy=(ImageView) view.findViewById(R.id.imgHappy);
        imgNeutral=(ImageView) view.findViewById(R.id.imgNeutral);
        imgSad=(ImageView) view.findViewById(R.id.imgSad);
        txtHappy=(TextView) view.findViewById(R.id.txtHappy);
        txtNeutral=(TextView) view.findViewById(R.id.txtNeutral);
        txtSad=(TextView) view.findViewById(R.id.txtSad);
        spinType=(Spinner) view.findViewById(R.id.spinType);
        edtName=(EditText) view.findViewById(R.id.edtName);
        edtEmail=(EditText) view.findViewById(R.id.edtEmail);
        edtMobile=(EditText) view.findViewById(R.id.edtMobile);
        edtQuery=(EditText) view.findViewById(R.id.edtQuery);
        btnSubmit=(Button) view.findViewById(R.id.btnSubmit);
        mScrollView=(ScrollView) view.findViewById(R.id.scrollView5);

        globalClass=new Global(mActivity);
        globalClass.setLoadingGIF(mActivity);
        cd=new ConnectionDetector(mActivity);
        pref=mActivity.getSharedPreferences("domain", 0);
        prefs_user = mActivity.getSharedPreferences("login_detail", 0);
        mScrollView.setVisibility(View.INVISIBLE);

        setDeviceInfo();

        if (cd.isConnectingToInternet()){
            new AsyncLoadOptionApi().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            globalClass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.plz_chk_internet));
        }

        if (!prefs_user.getString("email", "").equals("")){
            strMobile=prefs_user.getString("mobile", "");
            strEmail=prefs_user.getString("email", "");

            edtName.setText(prefs_user.getString("name", ""));
            edtMobile.setText(strMobile);
            edtEmail.setText(strEmail);

            edtName.setVisibility(View.GONE);
            edtMobile.setVisibility(View.GONE);
            edtEmail.setVisibility(View.GONE);
        }

        imgHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtHappy.setTextColor(mActivity.getResources().getColor(R.color.highlight_color));
                txtNeutral.setTextColor(mActivity.getResources().getColor(R.color.tertiary_color));
                txtSad.setTextColor(mActivity.getResources().getColor(R.color.tertiary_color));

                strRating="HAPPY";
                strEmotionsType=":-)";
            }
        });

        imgNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtHappy.setTextColor(mActivity.getResources().getColor(R.color.tertiary_color));
                txtNeutral.setTextColor(mActivity.getResources().getColor(R.color.highlight_color));
                txtSad.setTextColor(mActivity.getResources().getColor(R.color.tertiary_color));

                strRating="NEUTRAL";
                strEmotionsType=":-|";
            }
        });

        imgSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtHappy.setTextColor(mActivity.getResources().getColor(R.color.tertiary_color));
                txtNeutral.setTextColor(mActivity.getResources().getColor(R.color.tertiary_color));
                txtSad.setTextColor(mActivity.getResources().getColor(R.color.highlight_color));

                strRating = "SAD";
                strEmotionsType = ":-(";
            }
        });

       /* radGrpType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radFeedback:
                        setFeedbackSpinner();
                        break;
                    case R.id.radComplaint:
                        setComplainSpinner();
                        break;
                }
            }
        });*/

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName=edtName.getText().toString();
                strMobile=edtMobile.getText().toString();
                strEmail=edtEmail.getText().toString();
                strQuery=edtQuery.getText().toString();
                alertDialogBuilder = new AlertDialog.Builder(mActivity);
                if (strName.equals("") || strEmail.equals("") || strMobile.equals("") || strQuery.equals("")){
                    alertDialogBuilder
                            .setMessage("All fields are mandatory")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else if (spinType.getSelectedItemPosition()==0) {
                    alertDialogBuilder
                            .setMessage("Please select feedback category.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else if (strRating.equals("")) {
                    alertDialogBuilder
                            .setMessage("Please select your rating")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else if (strMobile.length()!=10){
                    alertDialogBuilder
                            .setMessage("Mobile number should be of 10 digits")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else if (strMobile.startsWith("5")||strMobile.startsWith("4")||strMobile.startsWith("3")||strMobile.startsWith("2")||strMobile.startsWith("1")||strMobile.startsWith("0")){
                    alertDialogBuilder
                            .setMessage("Mobile number should start with 6,7,8 or 9")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else  if (!Validator.isValidEmail(strEmail.toString())) {
                    alertDialogBuilder
                            .setMessage("Please enter valid Email ID")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else if (strQuery.length()<20){
                    alertDialogBuilder
                            .setMessage("Query should be atleast of 20 characters")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else if (strQuery.length()>250){
                    alertDialogBuilder
                            .setMessage("Query should be maximum of 250 characters")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{
                    if (cd.isConnectingToInternet())
                        new AsyncTaskPost().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    else
                        globalClass.showCustomToast(mActivity, getResources().getString(R.string.plz_chk_internet));
                }
            }
        });

        return view;
    }

    private void setDeviceInfo() {
        String myDeviceModel = "";
        try {
            myDeviceModel = android.os.Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
            myDeviceModel = "";
        }

        String myDevice = "";
        try {
            myDevice = android.os.Build.DEVICE;
        } catch (Exception e) {
            e.printStackTrace();
            myDevice = "";
        }

        String myDevicePRODUCT = "";
        try {
            myDevicePRODUCT = android.os.Build.PRODUCT;
        } catch (Exception e) {
            e.printStackTrace();
            myDevicePRODUCT = "";
        }

        String myDeviceVersion = "";
        try {
            myDeviceVersion = android.os.Build.VERSION.SDK;
        } catch (Exception e) {
            e.printStackTrace();
            myDeviceVersion = "";
        }

        deviceInfo = myDeviceModel+" "+ myDevice + " "+myDevicePRODUCT+" "+myDeviceVersion;

        System.out.println("Nitya >> "+deviceInfo);
    }

    public class AsyncLoadOptionApi extends AsyncTask<Void, Void, ArrayList<String>> {
        String strData="";
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            globalClass.showProgressDialog();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                try {
                    HttpClient client = new DefaultHttpClient();
//                    String getURL = pref.getString("domain", "") + "MASTER.svc/"+pref.getString("api_key","")+"/FEEDBACK/getlist";
                    String getURL = AbstractApiModel.API_VERSION+"/MASTER.svc/"+ AppConstants.API_KEY+"/FEEDBACK/getlist";
                    System.out.println(getURL);
                    HttpGet httpGet = new HttpGet(getURL);
                    httpGet.setHeader("Content-Type", "application/json");
                    httpGet.setHeader("Accept", "application/json");
                    HttpResponse response = client.execute(httpGet);
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        strData= EntityUtils.toString(resEntity);
                        Log.e("Response", strData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                arrFeedback=new ArrayList<>();
                arrFeedback.add("-Select Feedback Category-");
                JSONObject json = new JSONObject(strData);
                JSONArray jsonArray=json.getJSONArray("MASTER");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jObject=jsonArray.getJSONObject(i);
                    arrFeedback.add(jObject.getString("VALUE"));
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

            return arrFeedback;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            setFeedbackSpinner();
            mScrollView.setVisibility(View.VISIBLE);
            globalClass.hideProgressDialog();
        }
    }

    private void setFeedbackSpinner(){
        Resources res = mActivity.getResources();
       /* arrFeedback=new ArrayList<>();
        String[] array=getResources().getStringArray(R.array.feedbacktype_array);
        for (int i=0; i<array.length; i++){
            arrFeedback.add(array[i]);
        }*/
        adapterString=new SpinnerAdapter(mActivity, R.layout.spinner_items, arrFeedback, res);
        spinType.setAdapter(adapterString);
        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                strType = (String) spinType.getSelectedItem();
                //System.out.println("strType: " + strType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }




    class AsyncTaskPost extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //globalClass=new Global(mActivity);
            globalClass.setLoadingGIF(mActivity);
            globalClass.showProgressDialog();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

//            String strUrl=pref.getString("domain", "")+"MASTER.svc/feedback";
            String strUrl=AbstractApiModel.API_VERSION+"/MASTER.svc/feedback";

            JSONObject jobj = new JSONObject();
            try {
                jobj.put("api_key", AppConstants.API_KEY);
                jobj.put("display_type", "FEEDBACK");
                jobj.put("purpose", strType);
                jobj.put("name", strName);
                jobj.put("email", strEmail);
                jobj.put("mobile", strMobile);
                jobj.put("feedback", strQuery+ " - Btech app "+deviceInfo);
                jobj.put("emotion_text", strEmotionsType);
                jobj.put("rating", strRating);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(strUrl);

            InputStream inputStream = null;
            String result = "";
            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strUrl);
                String strJson = "";
                strJson = jobj.toString();
                System.out.println("Sending data: "+strJson);
                StringEntity se = new StringEntity(strJson);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                if(inputStream != null){
                    result = convertInputStreamToString(inputStream);
                    System.out.println("Response : "+ result);
                }

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            JSONObject json = null;
            try {

                json = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            globalClass.hideProgressDialog();
            alertDialogBuilder = new AlertDialog.Builder(mActivity);
            try{
                String strResponse=result.getString("RESPONSE");
                if (strResponse.equals("SUCCESS")){
                    alertDialogBuilder
                            .setMessage("Thank you for your valuable information!")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    if (!prefs_user.getString("email", "").equals("")){
                                        strMobile=prefs_user.getString("mobile", "");
                                        strEmail=prefs_user.getString("email", "");

                                        edtName.setText(prefs_user.getString("name", ""));
                                        edtMobile.setText(strMobile);
                                        edtEmail.setText(strEmail);
                                    }else{
                                        edtName.setText("");
                                        edtEmail.setText("");
                                        edtMobile.setText("");
                                    }
                                    txtHappy.setTextColor(mActivity.getResources().getColor(R.color.tertiary_color));
                                    txtNeutral.setTextColor(mActivity.getResources().getColor(R.color.tertiary_color));
                                    txtSad.setTextColor(mActivity.getResources().getColor(R.color.tertiary_color));
                                    strRating="";
                                    strEmotionsType="";

                                    edtQuery.setText("");
                                    spinType.setSelection(0);
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{
                    alertDialogBuilder
                            .setMessage(strResponse)
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Feedback");
    }
}