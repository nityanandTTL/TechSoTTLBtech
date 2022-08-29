package com.thyrocare.btechapp.fragment;

/**
 * Created by Orion on 6/14/2017.
 */

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.adapter.OrderServedDisplayDetailsAdapter;
import com.thyrocare.btechapp.delegate.OrderServedDisplayDetailsAdapterClickedDelegate;
import com.thyrocare.btechapp.models.api.response.OrderServedResponseModel;
import com.thyrocare.btechapp.models.data.BtechOrderModel;


import com.thyrocare.btechapp.uiutils.AbstractFragment;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;


/**
 * Created by Orion on 5/2/2017.<br/>
 * for getting orders<br/>
 */

public class OrderServedFragment extends AbstractFragment {


    public static final String TAG_FRAGMENT = "ORDER_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    RecyclerView recycler_view;
    private View rootView;
    private TextView tv_date, no_orders;
    private String todaysDate;
    private ArrayList<BtechOrderModel> btechOrderModels = new ArrayList<>();
    private OrderServedDisplayDetailsAdapter orderServedDisplayDetailsAdapter;
    private int mYear, mMonth, mDay;
    private ImageView img_search;
    private String todate = "";
    private String MaskedPhoneNumber = "";
    private Global global;

    public OrderServedFragment() {
        // Required empty public constructor
    }

    public static OrderServedFragment newInstance() {
        OrderServedFragment fragment = new OrderServedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Orders Served");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        global = new Global(activity);
        try {
            if (activity != null) {
                if (activity.toolbarHome != null) {
                    activity.toolbarHome.setTitle("Orders served");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        appPreferenceManager = new AppPreferenceManager(activity);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_order_served, container, false);
        Calendar calendar = Calendar.getInstance();
        Date jd = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        todaysDate = simpleDateFormat.format(jd);
        todate = sdf.format(jd);
        initUI();

        setListners();
        initData(todaysDate);
        return rootView;
    }

    @Override
    public void initUI() {
        super.initUI();
        recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        tv_date = (TextView) rootView.findViewById(R.id.tv_date);
        tv_date.setText("" + todate);
        img_search = (ImageView) rootView.findViewById(R.id.img_search);
        no_orders = (TextView) rootView.findViewById(R.id.no_orders);
    }

    private void setListners() {


        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                tv_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                todaysDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                //changes_20june2017
                                initData(todaysDate);
                                //changes_20june2017
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //changes_20june2017
                //initData(todaysDate);
                //changes_20june2017
            }
        });
    }


    private void initData(String date) {

        if (isNetworkAvailable(activity)) {
            CallGetOrderServedDetailsDisplayApi(date);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void CallGetOrderServedDetailsDisplayApi(String date) {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<OrderServedResponseModel> responseCall = apiInterface.CallGetOrderServedDetailsDisplayApi(appPreferenceManager.getLoginResponseModel().getUserID(), date);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<OrderServedResponseModel>() {
            @Override
            public void onResponse(Call<OrderServedResponseModel> call, retrofit2.Response<OrderServedResponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    OrderServedResponseModel orderServedResponseModel = response.body();
                    if (orderServedResponseModel != null && orderServedResponseModel.getBtchOrd().size() > 0) {
                        btechOrderModels = orderServedResponseModel.getBtchOrd();
                        no_orders.setVisibility(View.GONE);
                    } else {
                        btechOrderModels = new ArrayList<>();
                        no_orders.setVisibility(View.VISIBLE);
                    }
                    prepareRecyclerView();
                } else {
                    Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderServedResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private void prepareRecyclerView() {
        orderServedDisplayDetailsAdapter = new OrderServedDisplayDetailsAdapter(btechOrderModels, activity, new OrderServedDisplayDetailsAdapterClickedDelegate() {
            @Override
            public void onCallCustomer(String customerMobile, String orderNo) {


                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + customerMobile));
                activity.startActivity(intent);


            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(orderServedDisplayDetailsAdapter);

    }

}




