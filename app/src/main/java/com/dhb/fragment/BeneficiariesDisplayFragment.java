package com.dhb.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.AddEditBeneficiaryDetailsActivity;
import com.dhb.activity.OrderBookingActivity;
import com.dhb.activity.PaymentsActivity;
import com.dhb.adapter.BeneficiaryScreenSlidePagerAdapter;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BeneficiaryDetailsDao;
import com.dhb.dao.models.OrderDetailsDao;
import com.dhb.delegate.RefreshBeneficiariesSliderDelegate;
import com.dhb.models.api.request.OrderBookingRequestModel;
import com.dhb.models.data.BeneficiaryBarcodeDetailsModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.BeneficiaryLabAlertsModel;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;
import com.dhb.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.dhb.models.data.OrderBookingDetailsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.DeviceUtils;

import org.json.JSONException;

import java.util.ArrayList;

public class BeneficiariesDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "BENEFICIARIES_DISPLAY_FRAGMENT";
    private OrderBookingActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    private ViewPager vpBeneficiaries;
    private TextView txtAmtPayable,txtAddBeneficiary;
    private Button btnProceedPayment;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    private int totalAmount = 0;
    private int dotsCount;
    private ImageView[] dots;
    private BeneficiaryScreenSlidePagerAdapter beneficiaryScreenSlidePagerAdapter;
    private LinearLayout pagerIndicator;
    private BeneficiaryDetailsModel tempBeneficiaryDetailsModel = new BeneficiaryDetailsModel();
    private OrderDetailsModel tempOrderDetailsModel = new OrderDetailsModel();
    private DhbDao dhbDao;
    private OrderDetailsDao orderDetailsDao;
    private BeneficiaryDetailsDao beneficiaryDetailsDao;
    public BeneficiariesDisplayFragment() {
        // Required empty public constructor
    }

    public static BeneficiariesDisplayFragment newInstance(OrderVisitDetailsModel orderVisitDetailsModel) {
        BeneficiariesDisplayFragment fragment = new BeneficiariesDisplayFragment();
        Bundle args = new Bundle();
        args.putParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL,orderVisitDetailsModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (OrderBookingActivity) getActivity();
        dhbDao = new DhbDao(activity);
        orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
        beneficiaryDetailsDao = new BeneficiaryDetailsDao(dhbDao.getDb());
        appPreferenceManager = new AppPreferenceManager(activity);
        if (getArguments() != null) {
            this.orderVisitDetailsModel = getArguments().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_beneficiaries_display, container, false);
        initUI();
        initData();
        initListeners();
        return rootView;
    }

    private void initListeners() {
        txtAddBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Confirm Action")
                        .setMessage("Do you really want to add a new beneficiary?")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tempOrderDetailsModel.setOrderNo(DeviceUtils.randomString(8));
                        ArrayList<BeneficiaryDetailsModel> beneficiaries = new ArrayList<BeneficiaryDetailsModel>();

                        tempBeneficiaryDetailsModel = new BeneficiaryDetailsModel();
                        tempBeneficiaryDetailsModel.setOrderNo(tempOrderDetailsModel.getOrderNo());
                        tempBeneficiaryDetailsModel.setBenId((int)(Math.random()*999));
                        beneficiaryDetailsDao.insertOrUpdate(tempBeneficiaryDetailsModel);

                        beneficiaries.add(tempBeneficiaryDetailsModel);

                        tempOrderDetailsModel.setBenMaster(beneficiaries);
                        orderDetailsDao.insertOrUpdate(tempOrderDetailsModel);

                        Intent intentEdit = new Intent(activity, AddEditBeneficiaryDetailsActivity.class);
                        intentEdit.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, tempBeneficiaryDetailsModel);
                        intentEdit.putExtra(BundleConstants.ORDER_DETAILS_MODEL, tempOrderDetailsModel);
                        startActivityForResult(intentEdit, BundleConstants.ADD_EDIT_START);
                    }
                }).show();
            }
        });
        btnProceedPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel();
                ApiCallAsyncTask orderBookingAPIAsyncTask = new AsyncTaskForRequest(activity).getOrderBookingRequestAsyncTask(orderBookingRequestModel);
                orderBookingAPIAsyncTask.setApiCallAsyncTaskDelegate(new OrderBookingAPIAsyncTaskDelegateResult());
                if(isNetworkAvailable(activity)){
                    orderBookingAPIAsyncTask.execute(orderBookingAPIAsyncTask);
                }
                else{
                    Toast.makeText(activity,activity.getResources().getString(R.string.internet_connetion_error),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private OrderBookingRequestModel generateOrderBookingRequestModel() {
        OrderBookingRequestModel orderBookingRequestModel = new OrderBookingRequestModel();

        //SET Order Booking Details Model - START
        OrderBookingDetailsModel orderBookingDetailsModel = new OrderBookingDetailsModel();
        orderBookingDetailsModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        orderBookingDetailsModel.setVisitId(orderVisitDetailsModel.getVisitId());
        orderBookingDetailsModel.setOrddtl(orderDetailsDao.getModelsFromVisitId(orderVisitDetailsModel.getVisitId()));
        orderBookingRequestModel.setOrdbooking(orderBookingDetailsModel);
        //SET Order Booking Details Model - END

        //SET Order Details Models Array - START
        orderBookingRequestModel.setOrddtl(orderDetailsDao.getModelsFromVisitId(orderVisitDetailsModel.getVisitId()));
        //SET Order Details Models Array - END


        //SET BENEFICIARY Details Models Array - START
        ArrayList<BeneficiaryDetailsModel> benArr = new ArrayList<>();
        for (OrderDetailsModel orderDetailsModel:
                orderDetailsDao.getModelsFromVisitId(orderVisitDetailsModel.getVisitId())) {
            ArrayList<BeneficiaryDetailsModel> tempBenArr = new ArrayList<>();
            tempBenArr = beneficiaryDetailsDao.getModelsFromOrderNo(orderDetailsModel.getOrderNo());
            if(tempBenArr!=null) {
                benArr.addAll(tempBenArr);
            }
        }
        orderBookingRequestModel.setBendtl(benArr);
        //SET BENEFICIARY Details Models Array - END

        //SET BENEFICIARY Barcode Details Models Array - START
        ArrayList<BeneficiaryBarcodeDetailsModel> benBarcodeArr = new ArrayList<>();

        //SET BENEFICIARY Sample Types Details Models Array - START
        ArrayList<BeneficiarySampleTypeDetailsModel> benSTArr = new ArrayList<>();

        //SET BENEFICIARY Test Wise Clinical History Models Array - START
        ArrayList<BeneficiaryTestWiseClinicalHistoryModel> benCHArr = new ArrayList<>();

        //SET BENEFICIARY Lab Alerts Models Array - START
        ArrayList<BeneficiaryLabAlertsModel> benLAArr = new ArrayList<>();

        for (BeneficiaryDetailsModel beneficiaryDetailsModel:
             benArr) {
            if(beneficiaryDetailsModel.getBarcodedtl()!=null) {
                benBarcodeArr.addAll(beneficiaryDetailsModel.getBarcodedtl());
            }
            if(beneficiaryDetailsModel.getSampleType()!=null) {
                benSTArr.addAll(beneficiaryDetailsModel.getSampleType());
            }
            if(beneficiaryDetailsModel.getClHistory()!=null) {
                benCHArr.addAll(beneficiaryDetailsModel.getClHistory());
            }
            if(beneficiaryDetailsModel.getLabAlert()!=null) {
                benLAArr.addAll(beneficiaryDetailsModel.getLabAlert());
            }
        }
        orderBookingRequestModel.setBarcodedtl(benBarcodeArr);
        //SET BENEFICIARY Barcode Details Models Array - END

        orderBookingRequestModel.setSmpldtl(benSTArr);
        //SET BENEFICIARY Sample Type Details Models Array - END

        orderBookingRequestModel.setClHistory(benCHArr);
        //SET BENEFICIARY Test Wise Clinical History Models Array - END

        orderBookingRequestModel.setLabAlert(benLAArr);
        //SET BENEFICIARY Lab Alerts Models Array - END

        return orderBookingRequestModel;
    }

    private void initData() {
        for (OrderDetailsModel orderDetailsModel :
                orderVisitDetailsModel.getAllOrderdetails()) {
            totalAmount = totalAmount + orderDetailsModel.getAmountDue();
        }

        txtAmtPayable.setText(totalAmount+"");
        if(totalAmount==0){
            btnProceedPayment.setText("Submit Work Order");
        }
        else{
            btnProceedPayment.setText("Proceed for Payment");
        }
        ArrayList<BeneficiaryDetailsModel> beneficiariesArr = new ArrayList<>();
        for (OrderDetailsModel orderDetailsModel : orderVisitDetailsModel.getAllOrderdetails()) {
            Logger.error(orderDetailsModel.getBenMaster().size()+"");
            for (BeneficiaryDetailsModel beneficiaryDetailsModel :orderDetailsModel.getBenMaster()) {
                beneficiariesArr.add(beneficiaryDetailsModel);
            }
        }
        beneficiaryScreenSlidePagerAdapter = new BeneficiaryScreenSlidePagerAdapter(getFragmentManager(), activity, beneficiariesArr, orderVisitDetailsModel.getAllOrderdetails(), new RefreshBeneficiariesSliderDelegate() {
            @Override
            public void onRefreshActionCallbackReceived(OrderVisitDetailsModel orderVisitDetails) {
                orderVisitDetailsModel = orderVisitDetails;
                initData();
            }
        });
        vpBeneficiaries.setAdapter(beneficiaryScreenSlidePagerAdapter);
        vpBeneficiaries.setCurrentItem(0);
        vpBeneficiaries.setOnPageChangeListener(new BeneficiaryScreenPageChangeListener());
        setUiPageViewController();

        if(orderVisitDetailsModel.getAllOrderdetails().size()>0) {
            for (OrderDetailsModel orderDetailsModel :
                    orderVisitDetailsModel.getAllOrderdetails()) {
                tempOrderDetailsModel = orderDetailsModel;
                break;
            }
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        vpBeneficiaries = (ViewPager) rootView.findViewById(R.id.vp_beneficiaries);
        btnProceedPayment = (Button) rootView.findViewById(R.id.btn_proceed_payment);
        txtAddBeneficiary = (TextView) rootView.findViewById(R.id.title_add_beneficiary);
        txtAmtPayable = (TextView) rootView.findViewById(R.id.title_amt_payable);
        pagerIndicator = (LinearLayout) rootView.findViewById(R.id.viewPagerCountDots);
    }

    private void setUiPageViewController() {

        dotsCount = beneficiaryScreenSlidePagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(activity);
            dots[i].setImageDrawable(activity.getResources().getDrawable(R.drawable.non_selected_item_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pagerIndicator.addView(dots[i], params);
        }
        if(dots!=null && dots.length>0) {
            dots[0].setImageDrawable(activity.getResources().getDrawable(R.drawable.selected_item_dot));
        }
    }

    private class BeneficiaryScreenPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setImageDrawable(activity.getResources().getDrawable(R.drawable.non_selected_item_dot));
            }
            dots[position].setImageDrawable(activity.getResources().getDrawable(R.drawable.selected_item_dot));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BundleConstants.ADD_EDIT_START && resultCode == BundleConstants.ADD_EDIT_FINISH) {
            initData();
        }
        if(requestCode==BundleConstants.PAYMENTS_START && resultCode==BundleConstants.PAYMENTS_FINISH){
            boolean isPaymentSuccess = data.getBooleanExtra(BundleConstants.PAYMENT_STATUS,false);
            if(isPaymentSuccess) {
                OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel();
                ApiCallAsyncTask workOrderEntryRequestAsyncTask = new AsyncTaskForRequest(activity).getWorkOrderEntryRequestAsyncTask(orderBookingRequestModel);
                workOrderEntryRequestAsyncTask.setApiCallAsyncTaskDelegate(new WorkOrderEntryAsyncTaskDelegateResult());
                if (isNetworkAvailable(activity)) {
                    workOrderEntryRequestAsyncTask.execute(workOrderEntryRequestAsyncTask);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class OrderBookingAPIAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if(statusCode==200){
                if(btnProceedPayment.getText().equals("Proceed for Payment")) {
                    Intent intentPayments = new Intent(activity, PaymentsActivity.class);
                    intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT,totalAmount);
                    intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID,2);
                    intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO,orderVisitDetailsModel.getVisitId());
                    intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE,appPreferenceManager.getLoginResponseModel().getUserID());
                    startActivityForResult(intentPayments, BundleConstants.PAYMENTS_START);
                }
                else if(btnProceedPayment.getText().equals("Submit Work Order")){
                    OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel();
                    ApiCallAsyncTask workOrderEntryRequestAsyncTask = new AsyncTaskForRequest(activity).getWorkOrderEntryRequestAsyncTask(orderBookingRequestModel);
                    workOrderEntryRequestAsyncTask.setApiCallAsyncTaskDelegate(new WorkOrderEntryAsyncTaskDelegateResult());
                    if(isNetworkAvailable(activity)){
                        workOrderEntryRequestAsyncTask.execute(workOrderEntryRequestAsyncTask);
                    }
                    else{
                        Toast.makeText(activity,activity.getResources().getString(R.string.internet_connetion_error),Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else{
                Toast.makeText(activity,"Order Booking Failed",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity,"Order Booking Failed",Toast.LENGTH_SHORT).show();
        }
    }

    private class WorkOrderEntryAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if(statusCode==200){
                Toast.makeText(activity,"Work Order Entry Successful",Toast.LENGTH_SHORT).show();
                activity.finish();
            }
            else{
                Toast.makeText(activity,"Work Order Entry Failed",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity,"Work Order Entry Failed",Toast.LENGTH_SHORT).show();
        }
    }
}
