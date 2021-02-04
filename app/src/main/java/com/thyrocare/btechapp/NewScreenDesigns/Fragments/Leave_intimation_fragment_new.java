package com.thyrocare.btechapp.NewScreenDesigns.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Adapters.Leavehistory_Adapter;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.LeaveIntimation_SubmitModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.GetResponse_NatureLeaveModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_Leave_Applied_history_Model;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.DateUtil;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.SelectDatePickerDialogFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;



/**
 * A simple {@link Fragment} subclass.
 */
public class Leave_intimation_fragment_new extends Fragment {

    public static final String TAG_FRAGMENT = "LEAVE_INTIMATION";
    private Activity mActivity;
    private ConnectionDetector cd;
    private Button btn_apply_leaves_id, btn_applied_leaved_id, btn_submit_leave_id;
    private RadioGroup radio_group_id;
    private RadioButton radio_one_day_id, radio_more_day_id;
    private TextView tv_from_date_id, tv_to_day_id, tv_display_from_date_id;
    private ImageView img_from_date_id, img_to_day_id;
    private Spinner spinner_select_reason_id, spinner_select_nature_id;
    private EditText edt_remark_id;
    private String[] str_array,Cancel_ReasonArray;
    private DatePickerDialog datepicker_id;
    private String yy_mm_dd_str_show2, yy_mm_dd_str_show1;
    private AlertDialog.Builder alertDialog;
    private String todate, remarks;
    private LeaveIntimation_SubmitModel leaveIntimation_submitModel;
    private int  days = 1;
    String btech_id = "";
    private Global global;
    private RelativeLayout rl_applyforleave_id;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout ll_layout_from_date_id, ll_layout_to_date_id, ll_layout_remark_id, ll_showapplied_leaved, ll_spinner_nature_id, ll_datepicker_2_id, ll_datepicker_1_id;
    private RecyclerView rv_show_applied_leaves_id;
    ArrayList<GetResponse_NatureLeaveModel> getResponse_natureLeaveModel;
    private AppPreferenceManager appPreferenceManager;

    public Leave_intimation_fragment_new() {
        // Required empty public constructor
    }

    public static Leave_intimation_fragment_new newInstance() {
        Leave_intimation_fragment_new fragment = new Leave_intimation_fragment_new();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        cd = new ConnectionDetector(mActivity);
        global = new Global(mActivity);
        alertDialog = new AlertDialog.Builder(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);
        btech_id = appPreferenceManager.getLoginResponseModel().getUserID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leave_intimation_modified_new, container, false);
        HomeScreenActivity activity = (HomeScreenActivity) getActivity();
        if (activity.toolbarHome != null) {
            activity.toolbarHome.setTitle("Leave Intimation");
        }
        init(view);
        listeners();
        callapi_forspinner();
        return view;
    }

    private void init(View view) {
        btn_applied_leaved_id = view.findViewById(R.id.btn_applied_leaved_id);
        btn_apply_leaves_id = view.findViewById(R.id.btn_apply_leaves_id);
        btn_submit_leave_id = view.findViewById(R.id.btn_submit_leave_id);

        radio_group_id = view.findViewById(R.id.radio_group_id);
        radio_one_day_id = view.findViewById(R.id.radio_one_day_id);
        radio_more_day_id = view.findViewById(R.id.radio_more_day_id);

        tv_from_date_id = view.findViewById(R.id.tv_from_date_id);
        tv_to_day_id = view.findViewById(R.id.tv_to_day_id);
        tv_display_from_date_id = view.findViewById(R.id.tv_display_from_date_id);

        img_from_date_id = view.findViewById(R.id.img_from_date_id);
        img_to_day_id = view.findViewById(R.id.img_to_day_id);

        spinner_select_reason_id = view.findViewById(R.id.spinner_select_reason_id);
        spinner_select_nature_id = view.findViewById(R.id.spinner_select_nature_id);

        edt_remark_id = view.findViewById(R.id.edt_remark_id);

        ll_layout_from_date_id = view.findViewById(R.id.ll_layout_from_date_id);
        ll_layout_to_date_id = view.findViewById(R.id.ll_layout_to_date_id);
        ll_layout_remark_id = view.findViewById(R.id.ll_layout_remark_id);
        ll_showapplied_leaved = view.findViewById(R.id.ll_showapplied_leaved);
        ll_spinner_nature_id = view.findViewById(R.id.ll_spinner_nature_id);
        ll_datepicker_2_id = view.findViewById(R.id.ll_datepicker_2_id);
        ll_datepicker_1_id = view.findViewById(R.id.ll_datepicker_1_id);

        rl_applyforleave_id = view.findViewById(R.id.rl_applyforleave_id);

        rv_show_applied_leaves_id = view.findViewById(R.id.rv_show_applied_leaves_id);
        layoutManager = new LinearLayoutManager(getActivity());
        rv_show_applied_leaves_id.setLayoutManager(layoutManager);

        str_array = getActivity().getResources().getStringArray(R.array.Reason_mode);
        Cancel_ReasonArray = getActivity().getResources().getStringArray(R.array.Cancel_Reason_mode);
        radio_one_day_id.setSelected(true);
        radio_one_day_id.setChecked(true);

    }

    private void listeners() {

        ll_datepicker_1_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepicker1();
            }
        });
        ll_datepicker_2_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepicker2();
            }
        });

        btn_apply_leaves_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_applyforleave_id.setVisibility(View.VISIBLE);
                ll_showapplied_leaved.setVisibility(View.GONE);
                btn_applied_leaved_id.setBackground(getContext().getResources().getDrawable(R.drawable.border_checkout_));
                btn_apply_leaves_id.setBackground(getContext().getResources().getDrawable(R.drawable.background_btn_orange_));

                btn_applied_leaved_id.setTextColor(getResources().getColor(R.color.black));
                btn_apply_leaves_id.setTextColor(getResources().getColor(R.color.white));
            }
        });
        btn_applied_leaved_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_applyforleave_id.setVisibility(View.GONE);
                callapi_forleaveapplied();
                ll_showapplied_leaved.setVisibility(View.VISIBLE);
                btn_applied_leaved_id.setBackground(getContext().getResources().getDrawable(R.drawable.background_btn_orange_));
                btn_apply_leaves_id.setBackground(getContext().getResources().getDrawable(R.drawable.border_checkout_));
                btn_applied_leaved_id.setTextColor(getResources().getColor(R.color.white));
                btn_apply_leaves_id.setTextColor(getResources().getColor(R.color.black));
            }
        });
        btn_submit_leave_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    callapi_leaveintimation();
                }
            }
        });


        setReasonSpinner(str_array);



        radio_group_id.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radio_one_day_id) {
                    tv_display_from_date_id.setText("Date");
                    if (ll_layout_to_date_id.getVisibility() == View.VISIBLE) {
                        ll_layout_to_date_id.setVisibility(View.GONE);
                    }
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_more_day_id) {
                    tv_display_from_date_id.setText("From date");
                    if (ll_layout_to_date_id.getVisibility() == View.GONE) {
                        ll_layout_to_date_id.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }



    private void callapi_forspinner() {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<GetResponse_NatureLeaveModel>> getResponse_natureLeaveModelCall = apiInterface.Getnature_forleaveintimation();
        global.showProgressDialog(mActivity,  "Please wait..", false);
        getResponse_natureLeaveModelCall.enqueue(new Callback<ArrayList<GetResponse_NatureLeaveModel>>() {
            @Override
            public void onResponse(Call<ArrayList<GetResponse_NatureLeaveModel>> call, Response<ArrayList<GetResponse_NatureLeaveModel>> response) {
                global.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {

                    getResponse_natureLeaveModel = null;
                    getResponse_natureLeaveModel = new ArrayList<>();
                    GetResponse_NatureLeaveModel model = new GetResponse_NatureLeaveModel();
                    model.setId(0);
                    model.setNature("--Select--");
                    getResponse_natureLeaveModel.add(model);
                    getResponse_natureLeaveModel.addAll(response.body());
                    if (getResponse_natureLeaveModel.size() > 0) {
                        SetNatureSpinner();
                    } else {
                        global.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_LONG);
                    }
                } else {
                    global.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetResponse_NatureLeaveModel>> call, Throwable t) {
                global.hideProgressDialog(mActivity);
                global.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_LONG);

            }
        });

    }

    private void SetNatureSpinner() {
        ArrayAdapter spinneradapter = new ArrayAdapter(mActivity, android.R.layout.simple_spinner_item, getResponse_natureLeaveModel);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_select_nature_id.setAdapter(spinneradapter);
        spinner_select_nature_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (getResponse_natureLeaveModel.get(position).getNature().equalsIgnoreCase("Cancel")){
                    setReasonSpinner(Cancel_ReasonArray);
                }else{
                    setReasonSpinner(str_array);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setReasonSpinner(String[] ReasonArry) {

        ArrayAdapter spinneradapter = new ArrayAdapter(mActivity, android.R.layout.simple_spinner_item, ReasonArry);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_select_reason_id.setAdapter(spinneradapter);
        spinner_select_reason_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (str_array[i].equalsIgnoreCase("others")) {
                    ll_layout_remark_id.setVisibility(View.VISIBLE);
                } else {
                    ll_layout_remark_id.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void callapi_forleaveapplied() {

        global.showProgressDialog(mActivity,  "Please wait..", false);
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<Get_Leave_Applied_history_Model>> leave_applied_history_call = apiInterface.GetBetch_leavehistory(btech_id);
        leave_applied_history_call.enqueue(new Callback<ArrayList<Get_Leave_Applied_history_Model>>() {
            @Override
            public void onResponse(Call<ArrayList<Get_Leave_Applied_history_Model>> call, Response<ArrayList<Get_Leave_Applied_history_Model>> response) {
                global.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Get_Leave_Applied_history_Model> getLeave_applied_history_model = response.body();
                    if (getLeave_applied_history_model.size() > 0) {
                        rv_show_applied_leaves_id.setVisibility(View.VISIBLE);
                        rv_show_applied_leaves_id.setAdapter(new Leavehistory_Adapter(mActivity, getLeave_applied_history_model));
                    } else {
                        rv_show_applied_leaves_id.setVisibility(View.GONE);
                        global.showcenterCustomToast(mActivity,"Not Records found", Toast.LENGTH_LONG);
                    }
                } else {
                    global.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Get_Leave_Applied_history_Model>> call, Throwable t) {
                global.showCustomToast(mActivity,SOMETHING_WENT_WRONG, Toast.LENGTH_LONG);
                global.hideProgressDialog(mActivity);


            }
        });

    }

    private boolean validation() {
        if (spinner_select_nature_id.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
            global.showCustomToast(getActivity(), getResources().getString(R.string.error_nature), Toast.LENGTH_LONG);
            return false;
        }
        if (radio_group_id.getCheckedRadioButtonId() == R.id.radio_one_day_id) {
            if (TextUtils.isEmpty(tv_from_date_id.getText().toString())) {
                global.showCustomToast(getActivity(), getResources().getString(R.string.Select_date), Toast.LENGTH_LONG);
                return false;
            }
        }
        if (radio_group_id.getCheckedRadioButtonId() == R.id.radio_more_day_id) {
            if (TextUtils.isEmpty(tv_from_date_id.getText().toString()) || TextUtils.isEmpty(tv_to_day_id.getText().toString())) {
                global.showCustomToast(getActivity(), getResources().getString(R.string.Select_both_date), Toast.LENGTH_LONG);
                return false;
            } else if (!TextUtils.isEmpty(tv_from_date_id.getText().toString()) && !TextUtils.isEmpty(tv_to_day_id.getText().toString())) {

                try {
                    Date d_from_date = DateUtil.dateFromString(yy_mm_dd_str_show1, "yyyy-MM-dd");
                    Date d_to_date = DateUtil.dateFromString(yy_mm_dd_str_show2, "yyyy-MM-dd");

                    if (d_from_date.equals(d_to_date)) {
                        tv_from_date_id.setText("");
                        tv_to_day_id.setText("");
                        global.showCustomToast(getActivity(), getResources().getString(R.string.error_samedate), Toast.LENGTH_LONG);
                        return false;
                    }
                    if (d_from_date.after(d_to_date)) {
                        tv_from_date_id.setText("");
                        tv_to_day_id.setText("");
                        global.showCustomToast(getActivity(), getResources().getString(R.string.error_FromDatebigger), Toast.LENGTH_LONG);
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (spinner_select_reason_id.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
            global.showCustomToast(getActivity(), getResources().getString(R.string.error_reason), Toast.LENGTH_LONG);
            return false;
        }
        if (spinner_select_reason_id.getSelectedItem().toString().equalsIgnoreCase("Others")) {
            if (TextUtils.isEmpty(edt_remark_id.getText().toString())) {
                global.showCustomToast(getActivity(), getResources().getString(R.string.error_remark), Toast.LENGTH_LONG);
                return false;
            }
        }

        return true;
    }

    private void callapi_leaveintimation() {

        leaveIntimation_submitModel = new LeaveIntimation_SubmitModel();
        leaveIntimation_submitModel.setBtechId(Integer.parseInt(btech_id));
        leaveIntimation_submitModel.setEnteredBy(Integer.parseInt(btech_id));
        leaveIntimation_submitModel.setNature(getResponse_natureLeaveModel.get(spinner_select_nature_id.getSelectedItemPosition()).getId());
        if (str_array[spinner_select_reason_id.getSelectedItemPosition()].equalsIgnoreCase("others")) {
            leaveIntimation_submitModel.setRemarks(edt_remark_id.getText().toString().trim());
        } else {
            leaveIntimation_submitModel.setRemarks(spinner_select_reason_id.getSelectedItem().toString());
        }

        leaveIntimation_submitModel.setLeaveType(getResponse_natureLeaveModel.get(spinner_select_nature_id.getSelectedItemPosition()).getNature());

        if (radio_group_id.getCheckedRadioButtonId() == R.id.radio_more_day_id) {
            try {
                days = Integer.parseInt(total_date_show());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            leaveIntimation_submitModel.setDays(days);
            leaveIntimation_submitModel.setTodate(yy_mm_dd_str_show2);
        } else {
            leaveIntimation_submitModel.setDays(1);
            leaveIntimation_submitModel.setTodate(yy_mm_dd_str_show1);
        }

        leaveIntimation_submitModel.setFromdate(yy_mm_dd_str_show1);


        global.showProgressDialog(mActivity, "Please Wait..", false);
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<CommonResponseModel> call_leaveapi = apiInterface.submitresponse(leaveIntimation_submitModel);
        call_leaveapi.enqueue(new Callback<CommonResponseModel>() {
            @Override
            public void onResponse(Call<CommonResponseModel> call, Response<CommonResponseModel> response) {
                global.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    CommonResponseModel responseModel = response.body();
                    if (responseModel.getRES_ID().equalsIgnoreCase("RES0000")) {

                        if (getResponse_natureLeaveModel.get(spinner_select_nature_id.getSelectedItemPosition()).getNature().equalsIgnoreCase("Cancel")){
                            global.showCustomToast(mActivity, "Leave Cancelled Successfully", Toast.LENGTH_LONG);
                        }else{
                            global.showCustomToast(mActivity, "Leave Applied Successfully", Toast.LENGTH_LONG);
                        }
                        resetAlldata();
                    } else {
                        global.showCustomToast(mActivity, !StringUtils.isNull(responseModel.getRESPONSE1()) ? responseModel.getRESPONSE1() : SomethingWentwrngMsg, Toast.LENGTH_LONG);
                    }
                } else {
                    global.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<CommonResponseModel> call, Throwable t) {
                global.hideProgressDialog(mActivity);
                global.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_LONG);
            }
        });

    }

    private void showDatepicker1() {

        Calendar mindate = Calendar.getInstance();
        mindate.add(Calendar.DAY_OF_MONTH, 1);
        mindate.add(Calendar.MONTH, -3);
        Calendar maxdate = Calendar.getInstance();
        maxdate.add(Calendar.MONTH, 6);
        SelectDatePickerDialogFragment datePickerDialogFragment = new SelectDatePickerDialogFragment(mActivity, "Select Date", mindate.getTimeInMillis(), maxdate.getTimeInMillis(), "dd-MM-yyyy");
        datePickerDialogFragment.setDateSelectedListener(new SelectDatePickerDialogFragment.OnDateSelectedListener() {
            @Override
            public void onDateSelected(String strSelectedDate, Date SelectedDate) {
                tv_from_date_id.setText(strSelectedDate);
                yy_mm_dd_str_show1 = DateUtil.Req_Date_Req(strSelectedDate, "dd-MM-yyyy", "yyyy-MM-dd");

            }
        });
        datePickerDialogFragment.show(getFragmentManager(), "DatePicker");
    }

    private void showDatepicker2() {

        Calendar mindate = Calendar.getInstance();
        mindate.add(Calendar.DAY_OF_MONTH, 1);
        mindate.add(Calendar.MONTH, -3);
        Calendar maxdate = Calendar.getInstance();
        maxdate.add(Calendar.MONTH, 6);
        SelectDatePickerDialogFragment datePickerDialogFragment = new SelectDatePickerDialogFragment(mActivity, "Select Date", mindate.getTimeInMillis(), maxdate.getTimeInMillis(), "dd-MM-yyyy");
        datePickerDialogFragment.setDateSelectedListener(new SelectDatePickerDialogFragment.OnDateSelectedListener() {
            @Override
            public void onDateSelected(String strSelectedDate, Date SelectedDate) {
                tv_to_day_id.setText(strSelectedDate);
                yy_mm_dd_str_show2 = DateUtil.Req_Date_Req(strSelectedDate, "dd-MM-yyyy", "yyyy-MM-dd");

            }
        });
        datePickerDialogFragment.show(getFragmentManager(), "DatePicker");
    }


    private float getDate_diff(String from_date, String to_date) {

        Date d_from_date = DateUtil.dateFromString(from_date, "yyyy-MM-dd");
        Date d_to_date = DateUtil.dateFromString(to_date, "yyyy-MM-dd");
        long diff = Math.abs(d_from_date.getTime() - d_to_date.getTime());
        float duration_float = TimeUnit.MILLISECONDS.toDays(diff);
        return (float) (duration_float + 1.0);


    }

    private String total_date_show() {
        if (yy_mm_dd_str_show2 != null && yy_mm_dd_str_show1 != null) {
            if (!yy_mm_dd_str_show2.isEmpty() && !yy_mm_dd_str_show1.isEmpty()) {
                Date d_from_date = DateUtil.dateFromString(yy_mm_dd_str_show1, "yyyy-MM-dd");
                Date d_to_date = DateUtil.dateFromString(yy_mm_dd_str_show2, "yyyy-MM-dd");
                if (d_from_date.getTime() != d_to_date.getTime()) {
                    if (d_from_date.before(d_to_date)) {
                        float diff = getDate_diff(yy_mm_dd_str_show1, yy_mm_dd_str_show2);
                        if (diff >= 1.0) {
                            int i = Math.round(diff);
                            String number = "" + i + "";
                            return number;

                        } else {
                            String str_diff = "" + diff + "";
                            return str_diff;
                        }
                    } else {
                        global.showalert_OK("Please select valid dates.", mActivity);
                        resetdata();
                        return "0";
                    }
                } else {
                    global.showalert_OK("Sorry you cant select same dates.", mActivity);
                    resetdata();
                    return "0";
                }
            }
        }

        return "";
    }

    private void resetdata() {
        tv_from_date_id.setText("");
        tv_to_day_id.setText("");
        yy_mm_dd_str_show2 = "";
        yy_mm_dd_str_show1 = "";
    }

    private void resetAlldata() {
        resetdata();
        spinner_select_reason_id.setSelection(0);
        spinner_select_nature_id.setSelection(0);
        radio_one_day_id.setChecked(true);
        radio_one_day_id.setSelected(true);
        edt_remark_id.setText("");
    }
}