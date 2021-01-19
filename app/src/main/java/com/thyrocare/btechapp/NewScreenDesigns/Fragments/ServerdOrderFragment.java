package com.thyrocare.btechapp.NewScreenDesigns.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.ServedOrdersListAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.ServedOrderResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.DateUtil;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.SelectDatePickerDialogFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServerdOrderFragment extends Fragment {

    public static final String TAG_FRAGMENT = "SERVED_ORDER_FRAGMENT";
    Activity mActivity;
    ConnectionDetector cd;
    Global globalclass;
    private RelativeLayout rel_main,rel_dateSelector;
    private TextView tv_selectedDate,tv_noDatafound;
    private EditText edt_Search;
    private RecyclerView recycle_servedOrders;
    ArrayList<ServedOrderResponseModel.btchOrd> ServedOrderArylist;
    private AppPreferenceManager appPreferenceManager;


    public ServerdOrderFragment() {
        // Required empty public constructor
    }

    public static ServerdOrderFragment newInstance() {
        ServerdOrderFragment fragment = new ServerdOrderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        appPreferenceManager = new AppPreferenceManager(mActivity);
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);

        try {
            HomeScreenActivity activity = (HomeScreenActivity) getActivity();
            activity.setTitle("Orders Served");
            activity.toolbar_image.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_serverd_order, container, false);

        InitUI(v);
        InitListeners();
        InitData();

        return v;
    }

    private void InitUI(View v) {

        rel_main = (RelativeLayout) v.findViewById(R.id.rel_main);
        rel_dateSelector = (RelativeLayout) v.findViewById(R.id.rel_dateSelector);

        tv_selectedDate = (TextView) v.findViewById(R.id.tv_selectedDate);
        tv_noDatafound = (TextView) v.findViewById(R.id.tv_noDatafound);
        edt_Search = (EditText) v.findViewById(R.id.edt_Search);

        recycle_servedOrders = (RecyclerView) v.findViewById(R.id.recycle_servedOrders);


    }

    private void InitListeners() {

        edt_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                if (ServedOrderArylist != null && ServedOrderArylist.size() > 0) {
                    if (!StringUtils.isNull(charSequence.toString())) {
                        ArrayList<ServedOrderResponseModel.btchOrd> filterarraylist = new ArrayList<>();
                        for (int i = 0; i < ServedOrderArylist.size(); i++) {
                            if ((!StringUtils.isNull(ServedOrderArylist.get(i).getOrderNo()) && ServedOrderArylist.get(i).getOrderNo().toUpperCase().contains(charSequence.toString().toUpperCase())) || (!StringUtils.isNull(ServedOrderArylist.get(i).getOrderBy()) && ServedOrderArylist.get(i).getOrderBy().toUpperCase().contains(charSequence.toString().toUpperCase()))) {
                                filterarraylist.add(ServedOrderArylist.get(i));
                            }
                        }
                        if (filterarraylist.size() > 0){
                            DisplayServedOrderList(filterarraylist);
                        }else{
                            recycle_servedOrders.setVisibility(View.GONE);
                            tv_noDatafound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        DisplayServedOrderList(ServedOrderArylist);
                    }
                } else {
                    recycle_servedOrders.setVisibility(View.GONE);
                    tv_noDatafound.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rel_dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectDatePickerDialogFragment datePickerDialogFragment = new SelectDatePickerDialogFragment(mActivity,"Select Date",0,System.currentTimeMillis(),"yyyy-MM-dd");
                datePickerDialogFragment.setDateSelectedListener(new SelectDatePickerDialogFragment.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(String strSelectedDate, Date SelectedDate) {
                        String CurrentDateToDisplay = DateUtil.getDateFromLong(SelectedDate.getTime(),"dd-MM-yyyy");
                        tv_selectedDate.setText(CurrentDateToDisplay);
                        if (cd.isConnectingToInternet()){
                            callServedOrderAPI(appPreferenceManager.getLoginResponseModel().getUserID(),strSelectedDate);
                        }else{
                            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);
                        }

                    }
                });
                datePickerDialogFragment.show(getFragmentManager(), "DatePicker");
            }
        });
    }

    private void InitData() {

        String CurrentDateToDisplay = DateUtil.getDateFromLong(System.currentTimeMillis(),"dd-MM-yyyy");
        tv_selectedDate.setText(CurrentDateToDisplay);

        String CurrentDate = DateUtil.getDateFromLong(System.currentTimeMillis(),"yyyy-MM-dd");
        if (cd.isConnectingToInternet()){
            callServedOrderAPI(appPreferenceManager.getLoginResponseModel().getUserID(),CurrentDate);
        }else{
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg,Toast.LENGTH_LONG);
        }

    }

    private void callServedOrderAPI(String BtechID,String Date) {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ServedOrderResponseModel> responseCall = apiInterface.CallServedOrderAPI(BtechID,Date);
        globalclass.showProgressDialog(mActivity,"Please wait..",false);


        responseCall.enqueue(new Callback<ServedOrderResponseModel>() {
            @Override
            public void onResponse(Call<ServedOrderResponseModel> call, Response<ServedOrderResponseModel> response) {
                globalclass.hideProgressDialog(mActivity);

                if (response.isSuccessful() && response.body() != null){
                    ServedOrderResponseModel model = response.body();
                    if (!StringUtils.isNull(model.getResponse()) && model.getResponse().equalsIgnoreCase("SUCCESS") && model.getBtchOrd() != null && model.getBtchOrd().size() > 0){
                        ServedOrderArylist = null;
                        ServedOrderArylist = new ArrayList<>();
                        ServedOrderArylist = model.getBtchOrd();
                        DisplayServedOrderList(ServedOrderArylist);
                    }else{

                        recycle_servedOrders.setVisibility(View.GONE);
                        tv_noDatafound.setVisibility(View.VISIBLE);
                    }

                }else{
                    recycle_servedOrders.setVisibility(View.GONE);
                    tv_noDatafound.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onFailure(Call<ServedOrderResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                recycle_servedOrders.setVisibility(View.GONE);
                tv_noDatafound.setVisibility(View.VISIBLE);

            }
        });

    }

    private void DisplayServedOrderList(ArrayList<ServedOrderResponseModel.btchOrd> ServedOrderList) {

        recycle_servedOrders.setVisibility(View.VISIBLE);
        tv_noDatafound.setVisibility(View.GONE);
        ServedOrdersListAdapter servedOrdersListAdapter = new ServedOrdersListAdapter(mActivity,ServedOrderList);
        servedOrdersListAdapter.setOnItemClickListener(new ServedOrdersListAdapter.OnItemClickListener() {
            @Override
            public void onCallbuttonClicked(final String mobilenumber) {

                TedPermission.with(mActivity)
                        .setPermissions(Manifest.permission.CALL_PHONE)
                        .setRationaleMessage("We need permission to make call from your device.")
                        .setRationaleConfirmText("OK")
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > Permission > Telephone")
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + mobilenumber));
                                startActivity(callIntent);
                            }

                            @Override
                            public void onPermissionDenied(List<String> deniedPermissions) {
                                Toast.makeText(mActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .check();
            }

            @Override
            public void onReceiptDownloadClicked(String Orderno) {

                if (cd.isConnectingToInternet()){
                    callSendreceiptAPI(Orderno);
                }else{
                    globalclass.showCustomToast(mActivity,CheckInternetConnectionMsg,Toast.LENGTH_LONG);
                }

            }
        });

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recycle_servedOrders.setLayoutManager(mLayoutManager);
//                    recycle_servedOrders.setItemAnimator(new DefaultItemAnimator());
//                    recycle_servedOrders.addItemDecoration(new DividerItemDecoration(MyAddressActivity.this, android.support.v7.widget.LinearLayoutManager.VERTICAL));
        recycle_servedOrders.setHasFixedSize(true);
        recycle_servedOrders.setAdapter(servedOrdersListAdapter);

    }

    private void callSendreceiptAPI(String OrderNo) {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<String> responseCall = apiInterface.CallSendreceiptAPI(OrderNo);
        globalclass.showProgressDialog(mActivity,"Please wait..",false);

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog(mActivity);

                if (response.isSuccessful() && response.body() != null){
                    String body = response.body();
                    if (!StringUtils.isNull(body) && (body.equalsIgnoreCase("1") || body.equalsIgnoreCase("1\n"))) {
                        globalclass.showCustomToast(mActivity,"E-Receipt sent Successfully",Toast.LENGTH_LONG);
                    } else {
                        globalclass.showCustomToast(mActivity,"Failed to send E-Receipt. Please try after sometime.",Toast.LENGTH_LONG);
                    }
                }else{
                    globalclass.showCustomToast(mActivity,SomethingWentwrngMsg,Toast.LENGTH_LONG);
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity,SomethingWentwrngMsg,Toast.LENGTH_LONG);

            }
        });

    }

}

