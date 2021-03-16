package com.thyrocare.btechapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mindorks.paracamera.Camera;
import com.thyrocare.btechapp.Controller.LeadChannelController;
import com.thyrocare.btechapp.NewScreenDesigns.Controllers.PostEmailValidationController;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPI_SingletonClass;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.models.api.request.LeadGenerationRequestModel;
import com.thyrocare.btechapp.models.api.response.LeadChannelRespModel;
import com.thyrocare.btechapp.models.api.response.LeadPurposeResponseModel;
import com.thyrocare.btechapp.models.api.response.LeadgenerationResponseModel;
import com.thyrocare.btechapp.models.api.response.TestBookingResponseModel;
import com.thyrocare.btechapp.models.data.BrandTestMasterModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;
import com.thyrocare.btechapp.utils.app.PermissionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.ApplicationController;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeadGenerationFragment extends Fragment {

    public static final String TAG = LeadGenerationFragment.class.getSimpleName();
    private Activity mActivity;
    private EditText edt_name, edt_mobile, edt_email, edt_setAddress, edt_pincode;
    private MultiAutoCompleteTextView edt_remarks;
    private Button btn_submit;
    private String name, mobile, email, address, pincode, remarks;
    private ImageView img_tick, img_tick2;
    private ConnectionDetector cd;
    private TextView tv_reset;
    private String type = "TEST";
    private RelativeLayout rel_upload_img, rel_upload_voice;
    private String AudioSavePathInDevice = null;
    private MediaRecorder mediaRecorder;
    private CustomDialogClass customDialogClass;
    private String lead_id = "";
    private TestBookingResponseModel testBookingResponseModel;
    private File imagefile;
    private File f_AudioSavePathInDevice;
    private Global globalClass;

    private Dialog CustomDialogforSuccess;
    private PackageManager pm;
    private int width, height;

    private Camera camera;
    private AppPreferenceManager appPreferenceManager;
    private Spinner spn_purpose;
    private LinearLayout lin_spnPurpose, lin_upload;
    private boolean isorderSelected = true;
    private String strSelectedPurpose = "";

    LinearLayout ll_channel;
    Spinner spr_channel, spr_from;
    TextView tv_selected_purpose;
    private LeadPurposeResponseModel leadmodel;


    public LeadGenerationFragment() {
        // Required empty public constructor
    }

    public static LeadGenerationFragment newInstance() {
        LeadGenerationFragment fragment = new LeadGenerationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        pm = mActivity.getPackageManager();
        globalClass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        setHasOptionsMenu(true);
        mActivity.setTitle("Lead Generation");


        try {
            HomeScreenActivity activity = (HomeScreenActivity) getActivity();
            activity.toolbar_image.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lead_generation, container, false);
        appPreferenceManager = new AppPreferenceManager(mActivity);
        initUI(v);
        initListners();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        if (displayMetrics != null) {
            try {
                height = displayMetrics.heightPixels;
                width = displayMetrics.widthPixels;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CallLeadPurposeAPI();
//        GetLeadChannel();

        return v;
    }


    private void GetLeadChannel() {
        if (cd.isConnectingToInternet()) {
            LeadChannelController leadChannelController = new LeadChannelController(LeadGenerationFragment.this);
            leadChannelController.GetLeadChannel();
        } else {
            globalClass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }
    }

    private void CallProductAPI() {
        if (UpdateProduct()) {
            if (cd.isConnectingToInternet()) {
                CallGetTechsoProductsAPI();
            } else {
                globalClass.showCustomToast(mActivity, CheckInternetConnectionMsg);
            }
        } else {
            SetProductstoAutoCompleteTextView();
        }
    }

    private boolean UpdateProduct() {
        long getCurrentMillis = System.currentTimeMillis();
        long getPreviouseMillis = appPreferenceManager.getCashingTime();
        long differ_millis = getCurrentMillis - getPreviouseMillis;
        int days = (int) (differ_millis / (1000 * 60 * 60 * 24));

        if (days >= 30) {
            return true;
        }
        return false;
    }

    private void initUI(View v) {
        edt_name = (EditText) v.findViewById(R.id.edt_name);
        edt_mobile = (EditText) v.findViewById(R.id.edt_mobile);
        edt_email = (EditText) v.findViewById(R.id.edt_email);
        edt_remarks = (MultiAutoCompleteTextView) v.findViewById(R.id.edt_remarks);
        edt_pincode = (EditText) v.findViewById(R.id.edt_pincode);
//        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        btn_submit = (Button) v.findViewById(R.id.btn_submit);
        img_tick = (ImageView) v.findViewById(R.id.img_tick);
        img_tick2 = (ImageView) v.findViewById(R.id.img_tick2);
        edt_setAddress = (EditText) v.findViewById(R.id.edt_setAddress);
        tv_reset = (TextView) v.findViewById(R.id.tv_reset);
        rel_upload_img = (RelativeLayout) v.findViewById(R.id.rel_upload_img);
        rel_upload_voice = (RelativeLayout) v.findViewById(R.id.rel_upload_voice);
        lin_spnPurpose = (LinearLayout) v.findViewById(R.id.lin_spnPurpose);
        lin_upload = (LinearLayout) v.findViewById(R.id.lin_upload);
        spn_purpose = (Spinner) v.findViewById(R.id.spn_purpose);

        ll_channel = v.findViewById(R.id.ll_channel);
        spr_channel = v.findViewById(R.id.spr_channel);

        tv_selected_purpose = v.findViewById(R.id.tv_selected_purpose);
        tv_selected_purpose.setVisibility(View.GONE);
        spr_channel.setVisibility(View.GONE);
        spr_from = v.findViewById(R.id.spr_from);
        spr_from.setVisibility(View.GONE);
    }


    private void initListners() {

        rel_upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtils.AsKPermissionForCamera(mActivity, new PermissionUtils.OnPermissionSuccessListener() {
                    @Override
                    public void onPermissionGranted() {
                        selectImage();
                    }
                });
            }
        });

        rel_upload_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customDialogClass != null) {
                    customDialogClass = null;
                }
                customDialogClass = new CustomDialogClass(mActivity);
                customDialogClass.show();
                customDialogClass.setCancelable(false);
                customDialogClass.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    if (edt_email.getText().toString().length() > 0) {
                        if (emailValidation()) {
                            if (cd.isConnectingToInternet()) {
                                CallEmailValidationApi(edt_email.getText().toString().trim());
                            } else {
                                globalClass.showCustomToast(mActivity, ConstantsMessages.CheckInternetConnectionMsg);
                            }
                        }
                    } else {
                        name = edt_name.getText().toString().toUpperCase().trim();
                        mobile = edt_mobile.getText().toString().trim();
                        email = edt_email.getText().toString().toLowerCase().trim();
                        address = edt_setAddress.getText().toString().trim();
                        remarks = edt_remarks.getText().toString().trim();
                        pincode = edt_pincode.getText().toString().trim();

                        if (checkRemarksValidation()) {
                            if (cd.isConnectingToInternet()) {
                                if (isorderSelected) {
                                    CallLeadGenerationAPI(name, mobile, email, address, pincode, remarks, type, imagefile, f_AudioSavePathInDevice);
                                } else {
                                    CallPurposeBasedLeadGenerationAPI(name, mobile, email, address, pincode, remarks, strSelectedPurpose);
                                }

                            } else {
                                globalClass.showCustomToast(mActivity, ConstantsMessages.CheckInternetConnectionMsg);
                            }
                        }
                    }
                }
            }
        });

        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFields();
            }
        });

    }

    private void CallLeadPurposeAPI() {
        PostAPI_SingletonClass.getInstance().CallGetLeadPurposeAPI(mActivity, true, new PostAPI_SingletonClass.CallGetLeadPurposeAPIListener() {
            @Override
            public void onSuccess(LeadPurposeResponseModel model) {

                if (model != null && StringUtils.CheckEqualIgnoreCase(model.getRespId(), Constants.RES00001) && model.getPurposeList() != null) {
                    leadmodel = model;
                    ArrayList<String> values = new ArrayList<>();
                    values.add("Select*");
                    for (int i = 0; i < model.getPurposeList().size(); i++) {
                        values.add(model.getPurposeList().get(i).getData());
                    }
                    SetPurposeDataToSpinner(values);
                } else {
                    lin_spnPurpose.setVisibility(View.GONE);
                }
                CallProductAPI();
            }

            @Override
            public void onFailure() {
                lin_spnPurpose.setVisibility(View.GONE);
                CallProductAPI();
            }
        });
    }

    private void SetPurposeDataToSpinner(List<String> purposeList) {
        lin_spnPurpose.setVisibility(View.VISIBLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, purposeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_purpose.setAdapter(adapter);
        spn_purpose.setSelection(0);


        spn_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (leadmodel != null) {
                    if (!InputUtils.isNull(leadmodel.getPurposeList())) {
                        if (spn_purpose.getSelectedItemPosition() == 0) {
//                            Toast.makeText(mActivity, "Kindly select Purpose", Toast.LENGTH_SHORT).show();
                            tv_selected_purpose.setVisibility(View.GONE);
                        } else {

                            for (int j = 0; j < leadmodel.getPurposeList().size(); j++) {
                                if (spn_purpose.getSelectedItem().toString().equalsIgnoreCase(leadmodel.getPurposeList().get(j).getData())) {
                                    if (!InputUtils.isNull(leadmodel.getPurposeList().get(j).getRemarks())) {
                                        tv_selected_purpose.setVisibility(View.VISIBLE);
                                        tv_selected_purpose.setText(leadmodel.getPurposeList().get(j).getRemarks());
                                    } else {
                                        tv_selected_purpose.setVisibility(View.GONE);
                                    }
                                }
                            }

                            if (spn_purpose.getSelectedItem().toString().equalsIgnoreCase("Order") || spn_purpose.getSelectedItem().toString().equalsIgnoreCase("Orders")) {
                                lin_upload.setVisibility(View.VISIBLE);
                                isorderSelected = true;
                            } else {
                                lin_upload.setVisibility(View.GONE);
                                isorderSelected = false;
                            }
                        }
                    }
                }


           /*     if (StringUtils.CheckEqualIgnoreCase(spn_purpose.getSelectedItem().toString(), "Order")) {
                    isorderSelected = true;
                } else {
                    isorderSelected = false;
                }
                strSelectedPurpose = spn_purpose.getSelectedItem().toString();
                tv_selected_purpose.setVisibility(View.VISIBLE);
                ShowandHideViews(isorderSelected);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ShowandHideViews(isorderSelected);
            }
        });
    }

    private void ShowandHideViews(boolean isorderSelected) {
        if (isorderSelected) {
            SetProductstoAutoCompleteTextView();
            ll_channel.setVisibility(View.GONE);
            lin_upload.setVisibility(View.VISIBLE);
        } else {
            edt_remarks.setAdapter(null);
            ll_channel.setVisibility(View.GONE);
            lin_upload.setVisibility(View.GONE);
        }
    }


    private boolean checkRemarksValidation() {
        if (edt_remarks.getText().toString().startsWith(",")) {
            globalClass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.remarks_val));
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith(".")) {
            globalClass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.remarks_val));
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith("-")) {
            globalClass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.remarks_val));
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith(" ")) {
            globalClass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.space_val_remarks));
            edt_remarks.requestFocus();
            return false;
        }
        return true;
    }

    public void getLeadChannel(LeadChannelRespModel leadChannelRespModel) {

        ArrayList<String> channelList = new ArrayList<>();
        ArrayList<String> fromList = new ArrayList<>();

        channelList.add("Select Channel*");
        fromList.add("Select From*");
        if (leadChannelRespModel != null) {
            ll_channel.setVisibility(View.VISIBLE);
            if (!InputUtils.isNull(leadChannelRespModel.getRespId()) && leadChannelRespModel.getRespId().equalsIgnoreCase(Constants.RES00001)) {
                if (!InputUtils.isNull(leadChannelRespModel.getChannelList())) {
                    for (int i = 0; i < leadChannelRespModel.getChannelList().size(); i++) {
                        if (!InputUtils.isNull(leadChannelRespModel.getChannelList().get(i).getData())) {
                            channelList.add(leadChannelRespModel.getChannelList().get(i).getData());
                        }
                    }
                }

                if (!InputUtils.isNull(leadChannelRespModel.getFromList())) {
                    for (int i = 0; i < leadChannelRespModel.getFromList().size(); i++) {
                        if (!InputUtils.isNull(leadChannelRespModel.getFromList().get(i).getData())) {
                            fromList.add(leadChannelRespModel.getFromList().get(i).getData());
                        }
                    }
                }

                ArrayAdapter<String> chanel = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, channelList);
                chanel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spr_channel.setAdapter(chanel);

                ArrayAdapter<String> from = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, fromList);
                from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spr_from.setAdapter(from);
            }
        } else {
            ll_channel.setVisibility(View.GONE);
        }


    }


    public class CustomDialogClass extends Dialog {

        public Activity activity;
        Boolean ButtonClicked = false;
        Button btn_discard, btn_save;
        ImageView img_record;
        TextView tv_record_title;
        ImageButton closeButton;

        public CustomDialogClass(Activity activity) {
            super(activity);
            this.activity = activity;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.audio_record_customdailog);

            initDialogUI();
            initDialogListners();
        }

        private void initDialogUI() {
            closeButton = (ImageButton) findViewById(R.id.ib_close);
            img_record = (ImageView) findViewById(R.id.img_record);
            btn_discard = (Button) findViewById(R.id.btn_discard);
            btn_save = (Button) findViewById(R.id.btn_save);
            tv_record_title = (TextView) findViewById(R.id.tv_record_title);

            if (f_AudioSavePathInDevice != null) {
                if (f_AudioSavePathInDevice.exists()) {
                    f_AudioSavePathInDevice.delete();
                }
                f_AudioSavePathInDevice = null;
            }
            btn_save.setEnabled(false);
            btn_save.setBackgroundResource(R.drawable.btn_disabled_bg);
            MessageLogger.info(mActivity, "Audio file initUI: " + f_AudioSavePathInDevice);
        }

        private void initDialogListners() {
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Dismiss the popup window
                    customDialogClass.cancel();
                    if (f_AudioSavePathInDevice != null) {
                        if (f_AudioSavePathInDevice.exists()) {
                            f_AudioSavePathInDevice.delete();
                        }
                        f_AudioSavePathInDevice = null;
                    }
                    MessageLogger.info(mActivity, "On cancel Audio file: " + f_AudioSavePathInDevice);
                    img_tick2.setVisibility(View.INVISIBLE);
                }
            });

            btn_discard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customDialogClass.cancel();
                    if (f_AudioSavePathInDevice != null) {
                        if (f_AudioSavePathInDevice.exists()) {
                            f_AudioSavePathInDevice.delete();
                        }
                        f_AudioSavePathInDevice = null;
                    }
                    MessageLogger.info(mActivity, "On discard Audio file: " + f_AudioSavePathInDevice);
                    img_tick2.setVisibility(View.INVISIBLE);
                }
            });

            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customDialogClass.cancel();
                    MessageLogger.info(mActivity, "On Save Audio file: " + f_AudioSavePathInDevice);
                    img_tick2.setVisibility(View.VISIBLE);
                }
            });

            img_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (!ButtonClicked) {
                            PermissionUtils.AsKPermissionToRecordAudio(mActivity, new PermissionUtils.OnPermissionSuccessListener() {
                                @Override
                                public void onPermissionGranted() {
                                    ButtonClicked = true;
                                    File sdCard = Environment.getExternalStorageDirectory();
                                    File directory = new File(sdCard.getAbsolutePath() + "/Uploads" + "/" + "Audio Records");
                                    if (!directory.isDirectory()) {
                                        directory.mkdirs();
                                    }
                                    if (InputUtils.CheckEqualCaseSensitive(type, "TEST")) {
                                        AudioSavePathInDevice = directory + "/" + lead_id + "_TestBooking.Mp3";
                                    } else {
                                        AudioSavePathInDevice = directory + "/" + edt_mobile.getText().toString() + "_ScanBooking.Mp3";
                                    }
                                    f_AudioSavePathInDevice = new File(AudioSavePathInDevice);
                                    MessageLogger.info(mActivity, "Audio File Path: " + AudioSavePathInDevice);
                                    MediaRecorderReady();
                                    try {
                                        mediaRecorder.prepare();
                                        mediaRecorder.start();
                                    } catch (IllegalStateException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    InputUtils.setTextToTextView(tv_record_title, getString(R.string.stop_record));
                                    closeButton.setClickable(false);
                                    img_record.setImageResource(R.drawable.ic_stop_record);
                                    btn_save.setEnabled(false);
                                    btn_save.setBackgroundResource(R.drawable.btn_disabled_bg);
                                    btn_discard.setEnabled(false);
                                    btn_discard.setBackgroundResource(R.drawable.btn_disabled_bg);
                                    globalClass.showCustomToast(mActivity, ConstantsMessages.Recordingstarted);
                                }
                            });

                        } else {
                            ButtonClicked = false;
                            try {
                                mediaRecorder.stop();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                            InputUtils.setTextToTextView(tv_record_title, getString(R.string.start_record));
                            closeButton.setClickable(true);
                            img_record.setImageResource(R.drawable.ic_start_record);
                            btn_save.setEnabled(true);
                            btn_save.setBackgroundResource(R.drawable.bg_bg1);
                            btn_discard.setEnabled(true);
                            btn_discard.setBackgroundResource(R.drawable.bg_bg1);
                            globalClass.showCustomToast(mActivity, ConstantsMessages.RecordingCompleted);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void clearAllFields() {
        edt_name.requestFocus();
        InputUtils.setTextToTextView(edt_name, "");
        InputUtils.setTextToTextView(edt_mobile, "");
        InputUtils.setTextToTextView(edt_email, "");
        InputUtils.setTextToTextView(edt_remarks, "");
        InputUtils.setTextToTextView(edt_setAddress, "");
        InputUtils.setTextToTextView(edt_pincode, "");
        InputUtils.setTextToTextView(tv_selected_purpose, "");
        tv_selected_purpose.setVisibility(View.GONE);
        edt_setAddress.setEnabled(true);

        img_tick.setVisibility(View.INVISIBLE);
        img_tick2.setVisibility(View.INVISIBLE);
        if (imagefile != null) {
            if (imagefile.exists()) {
                imagefile.delete();
            }
            imagefile = null;
        }
        if (f_AudioSavePathInDevice != null) {
            if (f_AudioSavePathInDevice.exists()) {
                f_AudioSavePathInDevice.delete();
            }
            f_AudioSavePathInDevice = null;
        }
        if (spr_channel != null && spr_channel.getAdapter() != null) {
            spr_channel.setSelection(0);
        }

        if (spr_from != null && spr_from.getAdapter() != null) {
            spr_from.setSelection(0);
        }

        if (spn_purpose != null && spn_purpose.getAdapter() != null) {
            spn_purpose.setSelection(0);
        }
        lin_upload.setVisibility(View.GONE);
    }

    private boolean checkValidation() {


        if (spn_purpose.getSelectedItemPosition() == 0) {
            globalClass.showCustomToast(mActivity, "Kindly Select Purpose");
            return false;
        }

        if (edt_name.getText().toString().isEmpty()) {
            globalClass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.str_enter_name));
            edt_name.requestFocus();
            return false;
        }
        if (edt_name.getText().toString().startsWith(" ")) {
            globalClass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.space_val_name));
            edt_name.requestFocus();
            return false;
        }
        if (edt_name.getText().toString().length() < 2) {
            globalClass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.valid_name));
            edt_name.requestFocus();
            return false;
        }
        if (edt_mobile.getText().toString().isEmpty()) {
            globalClass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.str_enter_mobile_number));
            edt_mobile.requestFocus();
            return false;
        }
        if (edt_mobile.getText().toString().length() < 10) {
            globalClass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.str_should_be_ten_digits));
            edt_mobile.requestFocus();
            return false;
        }


        if (!InputUtils.isNull(edt_pincode.getText().toString().trim()) && edt_pincode.getText().toString().length() < 6) {
            globalClass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.str_valid_pincode));
            edt_pincode.requestFocus();
            return false;
        }
        if (!edt_mobile.getText().toString().startsWith("9") && !edt_mobile.getText().toString().startsWith("8")
                && !edt_mobile.getText().toString().startsWith("7") && !edt_mobile.getText().toString().startsWith("6")) {
            globalClass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.str_valid_mobile_no));
            edt_mobile.requestFocus();
            return false;
        }
        /*if (edt_setcity.getText().toString().isEmpty()) {
            ConstantUtils.toastyError(context, getString(R.string.str_select_city), false);
            return false;
        }
        if (Utils.CheckEqualCaseSensitive(spinner_city.getSelectedItem(),"Select")) {
            ConstantUtils.toastyError(context, getString(R.string.str_select_city), false);
            spinner_city.requestFocus();
            return false;
        }*/
       /* try {
            if (!spn_purpose.getSelectedItem().toString().equalsIgnoreCase("Order")) {

                if (spr_channel.getSelectedItem().toString().equalsIgnoreCase("Select Channel*")) {

                    globalClass.showCustomToast(mActivity, "Select Channel");
                    return false;
                }

                if (spr_from.getSelectedItem().toString().equalsIgnoreCase("Select From*")) {

                    globalClass.showCustomToast(mActivity, "Select From");
                    return false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }*/

        return true;
    }

    private boolean emailValidation() {
        if (edt_email.getText().toString().startsWith(" ")) {
            globalClass.showCustomToast(mActivity, ConstantsMessages.EmailIDShouldNotStartWithSpace);
            edt_email.requestFocus();
            return false;
        }

        if (edt_email.getText().length() > 0 && !Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString()).matches()) {
            globalClass.showalert_OK("Enter valid Email Id", mActivity);
            edt_email.requestFocus();
            return false;
        }
        return true;
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Upload Image !");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    openCamera();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        buildCamera();

        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildCamera() {

        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("BtechApp/LeadsImages")
                .setName("img" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            String imageurl = "";
            try {
                img_tick.setVisibility(View.VISIBLE);
                imageurl = camera.getCameraBitmapPath();
                imagefile = new File(imageurl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void CallPurposeBasedLeadGenerationAPI(String name, final String mobile, String email, String address, String pincode, String remarks, String purpose) {

        LeadGenerationRequestModel model = new LeadGenerationRequestModel();
        model.setName(name);
        model.setMobile(mobile);
        model.setEmail(email);
        model.setAddress(address);
        model.setPincode(pincode);
        model.setRemarks(remarks);
        model.setPurpose(purpose);
        model.setAppName("Btech App");
        model.setEntryBy(appPreferenceManager.getLoginResponseModel().getMobile());
        /*model.setChannel(spr_channel.getSelectedItem().toString());
        model.setFrom(spr_from.getSelectedItem().toString());*/

        PostAPI_SingletonClass.getInstance().CallSubmitLeadGenerationAPI(mActivity, true, model, new PostAPI_SingletonClass.CallSubmitLeadAPIListener() {
            @Override
            public void onSuccess(LeadgenerationResponseModel model) {

                if (model != null && StringUtils.CheckEqualIgnoreCase(model.getRespId(), Constants.RES02024)) {
                    clearAllFields();
                    globalClass.showalert_OK(model.getResponse(), mActivity);
                } else {
                    if (model != null) {
                        globalClass.showCustomToast(mActivity, !StringUtils.isNull(model.getResponse()) ? model.getResponse() : ConstantsMessages.SOMETHING_WENT_WRONG);
                    } else {
                        globalClass.showCustomToast(mActivity, ConstantsMessages.SOMETHING_WENT_WRONG);
                    }
                }
            }

            @Override
            public void onFailure() {
                globalClass.showCustomToast(mActivity, ConstantsMessages.SOMETHING_WENT_WRONG);
            }
        });
    }

    private void CallLeadGenerationAPI(String name, String mobile1, String email1, String address, String pincode, String remarks1, String type, File imagefile, File f_AudioSavePathInDevice) {
        String refcode = appPreferenceManager.getLoginResponseModel().getMobile();
        if (remarks1.endsWith(",")) {
            remarks1 = StringUtils.removeLastCharacter(remarks1);
        }
        PostAPI_SingletonClass.getInstance().CallReferAFriendBookingAPI(mActivity, true, name, mobile1, email1, address, remarks1, type, imagefile, f_AudioSavePathInDevice,
                refcode, pincode, lead_id, new PostAPI_SingletonClass.CallReferAFriendBookingListener() {
                    @Override
                    public void onSuccess(TestBookingResponseModel model) {
                        if (InputUtils.CheckEqualIgnoreCase(model.getRES_ID(), "RES0000")) {
                            testBookingResponseModel = model;
                            OnLeadGenerationAPIresponseReceived();
                        } else if (InputUtils.CheckEqualIgnoreCase(model.getRES_ID(), "RES0063")) {
                            globalClass.showalert_OK(!InputUtils.isNull(model.getRESPONSE()) ? model.getRESPONSE() : ConstantsMessages.FailedToSubmitRequest, mActivity);

                        } else {
                            globalClass.showCustomToast(mActivity, ConstantsMessages.FailedToSubmitRequest);
                        }
                    }

                    @Override
                    public void onFailure() {
                        globalClass.showCustomToast(mActivity, ConstantsMessages.SomethingWentwrngMsg);
                    }
                });
    }

    private void OnLeadGenerationAPIresponseReceived() {
        if (InputUtils.CheckEqualCaseSensitive(testBookingResponseModel.getRES_ID(), "RES0000")) {
            globalClass.showCustomToast(mActivity, ConstantsMessages.TestBookingDoneSuccessfully);

            clearAllFields();

            CustomDialogforSuccess = new Dialog(mActivity);
            CustomDialogforSuccess.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            CustomDialogforSuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);
            CustomDialogforSuccess.setContentView(R.layout.lead_gen_success_dialog);
            CustomDialogforSuccess.setCancelable(false);

            TextView tv_name, tv_mobile, tv_email_id, tv_city, booking_ID, tv_remarks, note;
            ImageView img_whatsapp, img_mail, img_sms, btnclose;
            LinearLayout ll_city, ll_email_id, ll_remarks, ll_main;

            tv_name = (TextView) CustomDialogforSuccess.findViewById(R.id.tv_name);
            tv_mobile = (TextView) CustomDialogforSuccess.findViewById(R.id.tv_mobile);
            tv_email_id = (TextView) CustomDialogforSuccess.findViewById(R.id.tv_email_id);
            tv_city = (TextView) CustomDialogforSuccess.findViewById(R.id.tv_city);
            booking_ID = (TextView) CustomDialogforSuccess.findViewById(R.id.booking_ID);
            tv_remarks = (TextView) CustomDialogforSuccess.findViewById(R.id.tv_remarks);
            btnclose = (ImageView) CustomDialogforSuccess.findViewById(R.id.btnclose);
            img_whatsapp = (ImageView) CustomDialogforSuccess.findViewById(R.id.img_whatsapp);
            img_mail = (ImageView) CustomDialogforSuccess.findViewById(R.id.img_mail);
            img_sms = (ImageView) CustomDialogforSuccess.findViewById(R.id.img_sms);
            ll_city = (LinearLayout) CustomDialogforSuccess.findViewById(R.id.ll_city);
            ll_email_id = (LinearLayout) CustomDialogforSuccess.findViewById(R.id.ll_email_id);
            ll_remarks = (LinearLayout) CustomDialogforSuccess.findViewById(R.id.ll_remarks);
            ll_main = (LinearLayout) CustomDialogforSuccess.findViewById(R.id.ll_main);
            note = (TextView) CustomDialogforSuccess.findViewById(R.id.note);


            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width - 150, FrameLayout.LayoutParams.WRAP_CONTENT);
            ll_main.setLayoutParams(lp);

            btnclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onResume();
                    CustomDialogforSuccess.dismiss();
                }
            });
            InputUtils.setTextToTextView(booking_ID, testBookingResponseModel.getREF_ORDERID());
            InputUtils.setTextToTextView(tv_name, name);
            InputUtils.setTextToTextView(tv_mobile, mobile);

            if (email.isEmpty()) {
                ll_email_id.setVisibility(View.GONE);
            } else {
                InputUtils.setTextToTextView(tv_email_id, email);
            }

            if (address.isEmpty()) {
                ll_city.setVisibility(View.GONE);
            } else {
                InputUtils.setTextToTextView(tv_city, address);
            }

            if (remarks.isEmpty()) {
                ll_remarks.setVisibility(View.GONE);
            } else {
                InputUtils.setTextToTextView(tv_remarks, remarks);
            }

            InputUtils.setTextToTextView(note, name + " will be contacted for availing service.");

            img_whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isWhatsApp()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl(name, mobile, email, address, testBookingResponseModel.getREF_ORDERID(), type, remarks)));
                        startActivity(intent);
                    } else {
                        globalClass.showCustomToast(mActivity, ConstantsMessages.WhatsAppNotinstalled);
                    }
                }
            });

            img_mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailintent = new Intent(Intent.ACTION_SEND);
                    emailintent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    if (type.contains("TEST")) {
                        emailintent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.test_subject));
                        if (email.isEmpty() && address.isEmpty() && remarks.isEmpty()) {
                            emailintent.putExtra(Intent.EXTRA_TEXT, "Refer ID :- " + testBookingResponseModel.getREF_ORDERID() + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile);
                        } else {
                            emailintent.putExtra(Intent.EXTRA_TEXT, "Refer ID :- " + testBookingResponseModel.getREF_ORDERID() + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile + "\n" + "Email ID :- " + email + "\n" + "City :- " + address + "\n" + "Remarks :- " + remarks);
                        }
                    } else if (type.contains("SCAN")) {
                        emailintent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.scan_subject));
                        if (email.isEmpty() && address.isEmpty() && remarks.isEmpty()) {
                            emailintent.putExtra(Intent.EXTRA_TEXT, "Refer ID :- " + testBookingResponseModel.getREF_ORDERID() + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile);
                        } else {
                            emailintent.putExtra(Intent.EXTRA_TEXT, "Refer ID :- " + testBookingResponseModel.getREF_ORDERID() + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile + "\n" + "Email ID :- " + email + "\n" + "City :- " + address + "\n" + "Remarks :- " + remarks);
                        }
                    }
                    emailintent.setType("message/rfc822");
                    try {
                        startActivity(Intent.createChooser(emailintent, "Choose an Email client to which you want to share details:"));

                    } catch (Exception e) {
                        e.printStackTrace();
                        globalClass.showCustomToast(mActivity, ConstantsMessages.NoImageClientInstalled);
                    }
                }
            });

            img_sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("smsto:" + mobile);
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    if (type.contains("TEST")) {
                        if (email.isEmpty() && address.isEmpty() && remarks.isEmpty()) {
                            sendIntent.putExtra("sms_body", getString(R.string.test_subject) + "\n" + "Refer ID :- " + testBookingResponseModel.getREF_ORDERID() + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile);
                        } else {
                            sendIntent.putExtra("sms_body", getString(R.string.test_subject) + "\n" + "Refer ID :- " + testBookingResponseModel.getREF_ORDERID() + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile + "\n" + "Email ID :- " + email + "\n" + "City :- " + address + "\n" + "Remarks :- " + remarks);
                        }
                    } else if (type.contains("SCAN")) {
                        if (email.isEmpty() && address.isEmpty() && remarks.isEmpty()) {
                            sendIntent.putExtra("sms_body", getString(R.string.scan_subject) + "\n" + "Refer ID :- " + testBookingResponseModel.getREF_ORDERID() + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile);
                        } else {
                            sendIntent.putExtra("sms_body", getString(R.string.scan_subject) + "\n" + "Refer ID :- " + testBookingResponseModel.getREF_ORDERID() + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile + "\n" + "Email ID :- " + email + "\n" + "City :- " + address + "\n" + "Remarks :- " + remarks);
                        }
                    }
                    startActivity(sendIntent);
                }
            });

            CustomDialogforSuccess.show();


        } else if (testBookingResponseModel.getRESPONSE().contains("MOBILE NUMBER. IS ALREADY HAS BEEN USED")) {
            globalClass.showalert_OK(ConstantsMessages.Mobilerefferedmorethan10timesMsg, mActivity);
        } else {
            globalClass.showalert_OK(!InputUtils.isNull(testBookingResponseModel.getRESPONSE()) ? testBookingResponseModel.getRESPONSE() : ConstantsMessages.SomethingWentwrngMsg, mActivity);
        }
    }

    private boolean isWhatsApp() {
        try {
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            globalClass.showCustomToast(mActivity, ConstantsMessages.WhatsAppNotinstalled);
        }
        return false;
    }

    public static String whatsappUrl(String name, String mobile, String email_id, String city, String booking_id, String type, String remarks) {

        final String BASE_URL = "https://api.whatsapp.com/";
        final String WHATSAPP_PHONE_NUMBER = "91" + mobile;    //'62' is country code for Indonesia
        final String PARAM_PHONE_NUMBER = "phone";
        final String PARAM_TEXT = "text";
        String TEXT_VALUE = null;
        if (type.contains("TEST")) {
            if (email_id.isEmpty() && city.isEmpty() && remarks.isEmpty()) {
                TEXT_VALUE = "Test details" + "\n" + "Refer ID :- " + booking_id + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile;
            } else {
                TEXT_VALUE = "Test details" + "\n" + "Refer ID :- " + booking_id + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile + "\n" + "Email ID :- " + email_id + "\n" + "City :- " + city + "\n" + "Remarks :- " + remarks;
            }
        } else if (type.contains("SCAN")) {
            if (email_id.isEmpty() && city.isEmpty() && remarks.isEmpty()) {
                TEXT_VALUE = "PET scan details" + "\n" + "Refer ID :- " + booking_id + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile;
            } else {
                TEXT_VALUE = "PET scan details" + "\n" + "Refer ID :- " + booking_id + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile + "\n" + "Email ID :- " + email_id + "\n" + "City :- " + city + "\n" + "Remarks :- " + remarks;
            }
        }
        String newUrl = BASE_URL + "send";
        Uri builtUri = Uri.parse(newUrl).buildUpon()
                .appendQueryParameter(PARAM_PHONE_NUMBER, WHATSAPP_PHONE_NUMBER)
                .appendQueryParameter(PARAM_TEXT, TEXT_VALUE)
                .build();

        return buildUrl(builtUri).toString();
    }

    public static URL buildUrl(Uri myUri) {
        URL finalUrl = null;
        try {
            finalUrl = new URL(myUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return finalUrl;
    }

    private void CallEmailValidationApi(String EmailID) {

        try {
            if (ApplicationController.PostEmailValidationController != null) {
                ApplicationController.PostEmailValidationController = null;
            }

            ApplicationController.PostEmailValidationController = new PostEmailValidationController(mActivity);
            ApplicationController.PostEmailValidationController.CallEmailValdationAPI(EmailID);
            ApplicationController.PostEmailValidationController.setOnResponseListener(new PostEmailValidationController.OnResponseListener() {
                @Override
                public void onSuccess(boolean isEmailValid) {
                    isEmailValidate(isEmailValid);
                }

                @Override
                public void onfailure(String msg) {
                    globalClass.showCustomToast(mActivity, ConstantsMessages.SomethingWentwrngMsg);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void isEmailValidate(boolean isEmailValid) {

        if (checkRemarksValidation()) {
            if (!isEmailValid) {
                globalClass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.invalid_email));
                edt_email.requestFocus();
            } else {
                name = edt_name.getText().toString().toUpperCase().trim();
                mobile = edt_mobile.getText().toString().trim();
                email = edt_email.getText().toString().toLowerCase().trim();
                address = edt_setAddress.getText().toString().trim();
                pincode = edt_pincode.getText().toString().trim();
                remarks = edt_remarks.getText().toString().trim();

                if (cd.isConnectingToInternet()) {
                    if (isorderSelected) {
                        CallLeadGenerationAPI(name, mobile, email, address, pincode, remarks, type, imagefile, f_AudioSavePathInDevice);
                    } else {
                        CallPurposeBasedLeadGenerationAPI(name, mobile, email, address, pincode, remarks, strSelectedPurpose);
                    }
                } else {
                    globalClass.showCustomToast(mActivity, ConstantsMessages.CheckInternetConnectionMsg);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        lead_id = generateBookingID();
    }

    public static String generateBookingID() {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder random = new StringBuilder();
        Random rnd = new Random();
        while (random.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * alphanumeric.length());
            random.append(alphanumeric.charAt(index));
        }
        String generatedstr = random.toString();
        return generatedstr;
    }

    private void CallGetTechsoProductsAPI() {

        try {
            GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<BrandTestMasterModel> responseCall = apiInterface.CallGetTechsoPRoductsAPI("Bearer " + appPreferenceManager.getLoginResponseModel().getAccess_token());
            globalClass.showProgressDialog(mActivity, "Fetching products. Please wait..");
            responseCall.enqueue(new Callback<BrandTestMasterModel>() {
                @Override
                public void onResponse(Call<BrandTestMasterModel> call, retrofit2.Response<BrandTestMasterModel> response) {
                    globalClass.hideProgressDialog(mActivity);

                    if (response.isSuccessful() && response.body() != null) {
                        BrandTestMasterModel brandTestMasterModel = response.body();
                        ArrayList<String> productsNameArray = getProductsNameArray(brandTestMasterModel);
                        Gson gson22 = new Gson();
                        String json22 = gson22.toJson(productsNameArray);
                        appPreferenceManager.setCacheProduct(json22);
                        appPreferenceManager.setCashingTime(System.currentTimeMillis());

                        SetProductstoAutoCompleteTextView();

                    } else {
                        globalClass.showcenterCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<BrandTestMasterModel> call, Throwable t) {
                    globalClass.hideProgressDialog(mActivity);
                    globalClass.showcenterCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetProductstoAutoCompleteTextView() {

        if (!InputUtils.isNull(appPreferenceManager.getCacheProduct())) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> productsNameArray = gson.fromJson(appPreferenceManager.getCacheProduct(), type);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_dropdown_item_1line, productsNameArray);
            edt_remarks.setAdapter(adapter);
            edt_remarks.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        }


    }

    private ArrayList<String> getProductsNameArray(BrandTestMasterModel brandTestMasterModel) {
        ArrayList<String> FinalProductnameAryList = new ArrayList<>();
        if (brandTestMasterModel != null && brandTestMasterModel.getTstratemaster() != null && brandTestMasterModel.getTstratemaster().size() > 0) {
            for (int i = 0; i < brandTestMasterModel.getTstratemaster().size(); i++) {
                if (InputUtils.CheckEqualIgnoreCase(brandTestMasterModel.getTstratemaster().get(i).getTestType(), "TEST")) {
                    if (!InputUtils.isNull(brandTestMasterModel.getTstratemaster().get(i).getDescription())) {
                        FinalProductnameAryList.add(brandTestMasterModel.getTstratemaster().get(i).getDescription());
                    }
                } else if (!InputUtils.isNull(brandTestMasterModel.getTstratemaster().get(i).getTestCode())) {
                    FinalProductnameAryList.add(brandTestMasterModel.getTstratemaster().get(i).getTestCode());
                }
            }
        }

        return FinalProductnameAryList;
    }

}
