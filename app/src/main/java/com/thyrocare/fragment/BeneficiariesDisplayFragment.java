package com.thyrocare.fragment;


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

import com.thyrocare.R;
import com.thyrocare.activity.AddEditBeneficiaryDetailsActivity;
import com.thyrocare.activity.OrderBookingActivity;
import com.thyrocare.activity.PaymentsActivity;
import com.thyrocare.adapter.BeneficiaryScreenSlidePagerAdapter;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.dao.models.BeneficiaryDetailsDao;
import com.thyrocare.dao.models.OrderDetailsDao;
import com.thyrocare.delegate.RefreshBeneficiariesSliderDelegate;
import com.thyrocare.models.api.request.CartAPIRequestModel;
import com.thyrocare.models.api.request.OrderBookingRequestModel;
import com.thyrocare.models.api.response.CartAPIResponseModel;
import com.thyrocare.models.api.response.OrderBookingResponseBeneficiaryModel;
import com.thyrocare.models.api.response.OrderBookingResponseOrderModel;
import com.thyrocare.models.api.response.OrderBookingResponseVisitModel;
import com.thyrocare.models.data.BeneficiaryBarcodeDetailsModel;
import com.thyrocare.models.data.BeneficiaryDetailsModel;
import com.thyrocare.models.data.BeneficiaryLabAlertsModel;
import com.thyrocare.models.data.BeneficiarySampleTypeDetailsModel;
import com.thyrocare.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.thyrocare.models.data.CartRequestBeneficiaryModel;
import com.thyrocare.models.data.CartRequestOrderModel;
import com.thyrocare.models.data.OrderBookingDetailsModel;
import com.thyrocare.models.data.OrderDetailsModel;
import com.thyrocare.models.data.OrderVisitDetailsModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import java.util.ArrayList;

public class BeneficiariesDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "BENEFICIARIES_DISPLAY_FRAGMENT";
    private OrderBookingActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    private ViewPager vpBeneficiaries;
    private TextView txtAmtPayable;
    private Button btnProceedPayment;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    private int totalAmountPayable = 0;
    private int dotsCount;
    private ImageView[] dots;
    private BeneficiaryScreenSlidePagerAdapter beneficiaryScreenSlidePagerAdapter;
    private LinearLayout pagerIndicator;
    private BeneficiaryDetailsModel tempBeneficiaryDetailsModel = new BeneficiaryDetailsModel();
//    private OrderDetailsModel tempOrderDetailsModel = new OrderDetailsModel();
    private DhbDao dhbDao;
    private OrderDetailsDao orderDetailsDao;
    private BeneficiaryDetailsDao beneficiaryDetailsDao;
    private int PaymentMode;
    private OrderBookingResponseVisitModel orderBookingResponseVisitModel = new OrderBookingResponseVisitModel();
    private ArrayList<OrderBookingResponseBeneficiaryModel> orderBookingResponseBeneficiaryModelArr = new ArrayList<>();
    private LinearLayout llAddBeneficiary;

    //changes_17june2017
    //private TextView title_add_beneficiary;
    //changes_17june2017


    public BeneficiariesDisplayFragment() {
        // Required empty public constructor
    }

    public static BeneficiariesDisplayFragment newInstance(OrderVisitDetailsModel orderVisitDetailsModel) {
        BeneficiariesDisplayFragment fragment = new BeneficiariesDisplayFragment();
        Bundle args = new Bundle();
        args.putParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
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
        llAddBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   Toast.makeText(getActivity(),"Feature Coming Soon Stay tuned...... ",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Confirm Action")
                        .setMessage("Do you want to add a new beneficiary?")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*Logger.debug("orderVisitDetailsModel 1 :" + new Gson().toJson(orderVisitDetailsModel));
                        tempOrderDetailsModel.setOrderNo("TEMP_"+DeviceUtils.randomString(8));
                        tempOrderDetailsModel.setAddBen(true);
                        tempOrderDetailsModel.setTestEdit(false);
                        tempOrderDetailsModel.setAmountDue(0);
                        tempOrderDetailsModel.setAmountPayable(0);
                        Logger.debug("tempOrderDetailsModel:" + new Gson().toJson(tempOrderDetailsModel));
                        Logger.debug("orderVisitDetailsModel 2 :" + new Gson().toJson(orderVisitDetailsModel));
                        ArrayList<BeneficiaryDetailsModel> beneficiaries = new ArrayList<BeneficiaryDetailsModel>();

                        */tempBeneficiaryDetailsModel = new BeneficiaryDetailsModel();

                        //**********************


                        tempBeneficiaryDetailsModel.setFasting("");
                        tempBeneficiaryDetailsModel.setAddBen(true);
                        tempBeneficiaryDetailsModel.setTestEdit(false);

                        //**************************
                        tempBeneficiaryDetailsModel.setOrderNo(orderVisitDetailsModel.getAllOrderdetails().get(0).getOrderNo());
                        tempBeneficiaryDetailsModel.setBenId((int) (Math.random() * 999));
                        beneficiaryDetailsDao.insertOrUpdate(tempBeneficiaryDetailsModel);
                        /*
                        beneficiaries.add(tempBeneficiaryDetailsModel);

                        tempOrderDetailsModel.setBenMaster(beneficiaries);
                        orderDetailsDao.insertOrUpdate(tempOrderDetailsModel);*/

                        OrderDetailsModel orderDetailsModel = new OrderDetailsModel();
                        orderDetailsModel = orderVisitDetailsModel.getAllOrderdetails().get(0);
                        orderDetailsModel.setAddBen(true);
                        orderDetailsModel.setTestEdit(false);
                        orderDetailsDao.insertOrUpdate(orderDetailsModel);

                        orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().add(tempBeneficiaryDetailsModel);
                        orderVisitDetailsModel.getAllOrderdetails().get(0).setAddBen(true);
                        orderVisitDetailsModel.getAllOrderdetails().get(0).setTestEdit(false);
                        Intent intentEdit = new Intent(activity, AddEditBeneficiaryDetailsActivity.class);
                        intentEdit.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, tempBeneficiaryDetailsModel);
                        intentEdit.putExtra(BundleConstants.ORDER_DETAILS_MODEL, orderVisitDetailsModel.getAllOrderdetails().get(0));
                        intentEdit.putExtra(BundleConstants.IS_BENEFICIARY_ADD, true);
                        intentEdit.putExtra(BundleConstants.IS_BENEFICIARY_EDIT, false);
                        startActivityForResult(intentEdit, BundleConstants.ADD_START);
                    }
                }).show();
            }
        });
        btnProceedPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //changes_17june2017
               /* if (title_add_beneficiary.getText().equals("Next Beneficiary")) {
                    title_add_beneficiary.setError("Proceed to next beneficiary...");
                    //Toast.makeText(activity, "next ben.", Toast.LENGTH_SHORT).show();
                } else {
                    title_add_beneficiary.setError("test run");
                    //Toast.makeText(activity, "add ben.", Toast.LENGTH_SHORT).show();
                }*/
                //changes_17june2017

                OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("Button_proceed_payment");
                if (validate(orderBookingRequestModel)) {
                    ApiCallAsyncTask orderBookingAPIAsyncTask = new AsyncTaskForRequest(activity).getOrderBookingRequestAsyncTask(orderBookingRequestModel);
                    orderBookingAPIAsyncTask.setApiCallAsyncTaskDelegate(new OrderBookingAPIAsyncTaskDelegateResult());
                    if (isNetworkAvailable(activity)) {
                        orderBookingAPIAsyncTask.execute(orderBookingAPIAsyncTask);
                    } else {
                        Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validate(OrderBookingRequestModel orderBookingRequestModel) {
        for (BeneficiaryBarcodeDetailsModel barcodesModel :
                orderBookingRequestModel.getBarcodedtl()) {
            if (InputUtils.isNull(barcodesModel.getBarcode())) {
                for (BeneficiaryDetailsModel bdm : orderBookingRequestModel.getBendtl()) {
                    if (barcodesModel.getBenId() == bdm.getBenId()) {
                        Toast.makeText(activity, "Please scan all barcodes for " + bdm.getName(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
        }
        for (BeneficiaryDetailsModel bdm : orderBookingRequestModel.getBendtl()) {
            if (InputUtils.isNull(bdm.getTests())) {
                Toast.makeText(activity, "Please select atleast one test for " + bdm.getName(), Toast.LENGTH_SHORT).show();
                return false;
            }
            if (bdm.getVenepuncture() == null) {
                Toast.makeText(activity, "Please capture venepuncture image for " + bdm.getName(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private OrderBookingRequestModel generateOrderBookingRequestModel(String from) {
        OrderBookingRequestModel orderBookingRequestModel = new OrderBookingRequestModel();

        //SET Order Booking Details Model - START
        OrderBookingDetailsModel orderBookingDetailsModel = new OrderBookingDetailsModel();
        orderBookingDetailsModel.setPaymentMode(PaymentMode);
        orderBookingDetailsModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        orderBookingDetailsModel.setVisitId(orderVisitDetailsModel.getVisitId());
        ArrayList<OrderDetailsModel> ordtl = new ArrayList<>();
        ordtl = orderDetailsDao.getModelsFromVisitId(orderVisitDetailsModel.getVisitId());
        if (from.equals("Button_proceed_payment")) {
            for (int i = 0; i < ordtl.size(); i++) {
                ordtl.get(i).setAddBen(false);

            }
        }
        orderBookingDetailsModel.setOrddtl(ordtl);
        orderBookingRequestModel.setOrdbooking(orderBookingDetailsModel);
        //SET Order Booking Details Model - END

        //SET Order Details Models Array - START
        orderBookingRequestModel.setOrddtl(ordtl);
        //SET Order Details Models Array - END


        //SET BENEFICIARY Details Models Array - START
        ArrayList<BeneficiaryDetailsModel> benArr = new ArrayList<>();

        for (OrderDetailsModel orderDetailsModel :
                orderDetailsDao.getModelsFromVisitId(orderVisitDetailsModel.getVisitId())) {
            ArrayList<BeneficiaryDetailsModel> tempBenArr = new ArrayList<>();
            tempBenArr = beneficiaryDetailsDao.getModelsFromOrderNo(orderDetailsModel.getOrderNo());
            if (tempBenArr != null) {
                benArr.addAll(tempBenArr);
            }
        }

        if (from.equals("Button_proceed_payment")) {
            for (int i = 0; i < benArr.size(); i++) {
                benArr.get(i).setAddBen(false);
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

        for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                benArr) {

            if (beneficiaryDetailsModel.getBarcodedtl() != null) {
                benBarcodeArr.addAll(beneficiaryDetailsModel.getBarcodedtl());
            }
            if (beneficiaryDetailsModel.getSampleType() != null) {
                benSTArr.addAll(beneficiaryDetailsModel.getSampleType());
            }
            if (beneficiaryDetailsModel.getClHistory() != null) {
                benCHArr.addAll(beneficiaryDetailsModel.getClHistory());
            }
            if (beneficiaryDetailsModel.getLabAlert() != null) {
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

//        tempBeneficiaryDetailsModel =


        // Here #############################################################################################
        vpBeneficiaries.removeAllViews();
        vpBeneficiaries.clearOnPageChangeListeners();
        totalAmountPayable = 0;
        Logger.debug(orderVisitDetailsModel.getVisitId());
        orderVisitDetailsModel = orderDetailsDao.getOrderVisitModel(orderVisitDetailsModel.getVisitId());
        for (OrderDetailsModel orderDetailsModel :orderVisitDetailsModel.getAllOrderdetails()) {
            totalAmountPayable = totalAmountPayable + orderDetailsModel.getAmountPayable();
        }

        txtAmtPayable.setText("" + totalAmountPayable + "/-");
        if (totalAmountPayable == 0) {
            btnProceedPayment.setText("Submit Work Order");
        } else {
            btnProceedPayment.setText("Proceed for Payment");
        }
        ArrayList<BeneficiaryDetailsModel> beneficiariesArr = new ArrayList<>();
        for (OrderDetailsModel orderDetailsModel : orderVisitDetailsModel.getAllOrderdetails()) {
            Logger.error(orderDetailsModel.getBenMaster().size() + "");
            for (BeneficiaryDetailsModel beneficiaryDetailsModel : orderDetailsModel.getBenMaster()) {
                beneficiariesArr.add(beneficiaryDetailsModel);
            }
        }
        if (beneficiariesArr.size() == 0) {
            Toast.makeText(activity, "No beneficiaries found", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
        beneficiaryScreenSlidePagerAdapter = new BeneficiaryScreenSlidePagerAdapter(getFragmentManager(), activity, beneficiariesArr, orderVisitDetailsModel.getAllOrderdetails(), new RefreshBeneficiariesSliderDelegate() {
            @Override
            public void onRefreshActionCallbackReceived(OrderVisitDetailsModel orderVisitDetails) {
                CartAPIRequestModel cartAPIRequestModel = new CartAPIRequestModel();
                ArrayList<CartRequestOrderModel> ordersArr = new ArrayList<>();
                ArrayList<CartRequestBeneficiaryModel> beneficiariesArr = new ArrayList<>();
                cartAPIRequestModel.setVisitId(orderVisitDetails.getVisitId());
                for (OrderDetailsModel order :
                        orderVisitDetails.getAllOrderdetails()) {
                    CartRequestOrderModel crom = new CartRequestOrderModel();
                    crom.setOrderNo(order.getOrderNo());
                    crom.setHC(order.getReportHC());
                    crom.setBrandId(order.getBrandId() + "");
                    ordersArr.add(crom);
                    for (BeneficiaryDetailsModel ben : order.getBenMaster()) {
                        CartRequestBeneficiaryModel crbm = new CartRequestBeneficiaryModel();
                        crbm.setOrderNo(order.getOrderNo());
                        crbm.setAddben(ben.isAddBen());
                        crbm.setTestEdit(ben.isTestEdit());
                        if (!InputUtils.isNull(ben.getProjId())) {
                            crbm.setTests(ben.getProjId() + "," + ben.getTestsCode());
                            crbm.setProjId(ben.getProjId());
                            crbm.setBenId(String.valueOf(ben.getBenId()));
                        } else {
                            crbm.setTests(ben.getTestsCode());
                        }
                        beneficiariesArr.add(crbm);
                    }
                }
                cartAPIRequestModel.setOrders(ordersArr);
                cartAPIRequestModel.setBeneficiaries(beneficiariesArr);

                ApiCallAsyncTask cartAPIAsyncTaskRequest = new AsyncTaskForRequest(activity).getCartRequestAsyncTask(cartAPIRequestModel);
                cartAPIAsyncTaskRequest.setApiCallAsyncTaskDelegate(new CartAPIAsyncTaskDelegateResult());
                if (isNetworkAvailable(activity)) {
                    orderVisitDetailsModel = orderVisitDetails;
                    cartAPIAsyncTaskRequest.execute(cartAPIAsyncTaskRequest);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
        vpBeneficiaries.setAdapter(beneficiaryScreenSlidePagerAdapter);
        vpBeneficiaries.setCurrentItem(0);
        vpBeneficiaries.setOnPageChangeListener(new BeneficiaryScreenPageChangeListener());
        setUiPageViewController();

        /*if (orderVisitDetailsModel.getAllOrderdetails().size() > 0) {
            for (OrderDetailsModel orderDetailsModel :
                    orderVisitDetailsModel.getAllOrderdetails()) {
                tempOrderDetailsModel = orderDetailsModel;
                break;
            }
        }*/
    }

    @Override
    public void initUI() {
        super.initUI();
        vpBeneficiaries = (ViewPager) rootView.findViewById(R.id.vp_beneficiaries);
        btnProceedPayment = (Button) rootView.findViewById(R.id.btn_proceed_payment);
        llAddBeneficiary = (LinearLayout) rootView.findViewById(R.id.ll_add_beneficiary);
        txtAmtPayable = (TextView) rootView.findViewById(R.id.title_amt_payable);
        pagerIndicator = (LinearLayout) rootView.findViewById(R.id.viewPagerCountDots);

        //changes_17june2017
        //title_add_beneficiary = (TextView) rootView.findViewById(R.id.title_add_beneficiary);
        //changes_17june2017
    }

    private void setUiPageViewController() {
        pagerIndicator.removeAllViews();
        dotsCount = beneficiaryScreenSlidePagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        Logger.debug("dots_count" + String.valueOf(dotsCount));

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
        if (dots != null && dots.length > 0) {
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

                Logger.debug("dots_counter" + dotsCount);

                //changes_17june2017
               /* if ((i + 1) < dotsCount) {
                    title_add_beneficiary.setText("Next Beneficiary");
                } else {
                    title_add_beneficiary.setText("Add Beneficiary");
                }*/
                //changes_17june2017
            }
            dots[position].setImageDrawable(activity.getResources().getDrawable(R.drawable.selected_item_dot));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BundleConstants.ADD_START && resultCode == BundleConstants.ADD_FINISH) {
            tempBeneficiaryDetailsModel = data.getExtras().getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
            OrderDetailsModel orderDetailsModel = data.getExtras().getParcelable(BundleConstants.ORDER_DETAILS_MODEL);

            OrderVisitDetailsModel orderVisitDetails = orderDetailsDao.getOrderVisitModel(orderDetailsModel.getVisitId());

            CartAPIRequestModel cartAPIRequestModel = new CartAPIRequestModel();
            ArrayList<CartRequestOrderModel> ordersArr = new ArrayList<>();
            ArrayList<CartRequestBeneficiaryModel> beneficiariesArr = new ArrayList<>();
            cartAPIRequestModel.setVisitId(orderVisitDetails.getVisitId());
            for (OrderDetailsModel order :
                    orderVisitDetails.getAllOrderdetails()) {
                CartRequestOrderModel crom = new CartRequestOrderModel();
                crom.setOrderNo(order.getOrderNo());
                crom.setHC(order.getReportHC());
                crom.setBrandId(order.getBrandId() + "");
                ordersArr.add(crom);
                for (BeneficiaryDetailsModel ben : order.getBenMaster()) {
                    CartRequestBeneficiaryModel crbm = new CartRequestBeneficiaryModel();
                    crbm.setOrderNo(order.getOrderNo());
                    crbm.setAddben(ben.isAddBen());
                    crbm.setBenId(ben.getBenId()+"");
                    crbm.setTestEdit(ben.isTestEdit());
                    if (!InputUtils.isNull(ben.getProjId())) {
                        crbm.setTests(ben.getProjId() + "," + ben.getTestsCode());
                        crbm.setProjId(ben.getProjId());
                    } else {
                        crbm.setProjId("");
                        crbm.setTests(ben.getTestsCode());
                    }
                    beneficiariesArr.add(crbm);
                }
            }
            cartAPIRequestModel.setOrders(ordersArr);
            cartAPIRequestModel.setBeneficiaries(beneficiariesArr);

            ApiCallAsyncTask cartAPIAsyncTaskRequest = new AsyncTaskForRequest(activity).getCartRequestAsyncTask(cartAPIRequestModel);
            cartAPIAsyncTaskRequest.setApiCallAsyncTaskDelegate(new CartAPIAsyncTaskDelegateResult());
            if (isNetworkAvailable(activity)) {
                orderVisitDetailsModel = orderVisitDetails;
                cartAPIAsyncTaskRequest.execute(cartAPIAsyncTaskRequest);
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == BundleConstants.PAYMENTS_START && resultCode == BundleConstants.PAYMENTS_FINISH) {
            boolean isPaymentSuccess = data.getBooleanExtra(BundleConstants.PAYMENT_STATUS, false);
            if (isPaymentSuccess) {
                OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_digital");
                orderBookingRequestModel = fixForAddBeneficiary(orderBookingRequestModel);
                ApiCallAsyncTask workOrderEntryRequestAsyncTask = new AsyncTaskForRequest(activity).getWorkOrderEntryRequestAsyncTask(orderBookingRequestModel);
                workOrderEntryRequestAsyncTask.setApiCallAsyncTaskDelegate(new WorkOrderEntryAsyncTaskDelegateResult());
                if (isNetworkAvailable(activity)) {
                    workOrderEntryRequestAsyncTask.execute(workOrderEntryRequestAsyncTask);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "Payment Failed", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private OrderBookingRequestModel fixForAddBeneficiary(OrderBookingRequestModel orderBookingRequestModel) {
        //Update Visit ID in OrdBooking Model
        if (orderBookingRequestModel.getOrdbooking().getVisitId().equals(orderBookingResponseVisitModel.getOldVisitId())) {
            orderBookingRequestModel.getOrdbooking().setVisitId(orderBookingResponseVisitModel.getNewVisitId());
        }
        //Update Order No and Visit ID in Order Dtl Arr
        for (int i = 0; i < orderBookingRequestModel.getOrddtl().size(); i++) {
            if (orderBookingRequestModel.getOrddtl().get(i).getVisitId().equals(orderBookingResponseVisitModel.getOldVisitId())) {
                orderBookingRequestModel.getOrddtl().get(i).setVisitId(orderBookingResponseVisitModel.getNewVisitId());
            }
            for (int j = 0; j < orderBookingResponseVisitModel.getOrderids().size(); j++) {
                if (orderBookingRequestModel.getOrddtl().get(i).getOrderNo().equals(orderBookingResponseVisitModel.getOrderids().get(j).getOldOrderId())) {
                    orderBookingRequestModel.getOrddtl().get(i).setOrderNo(orderBookingResponseVisitModel.getOrderids().get(j).getNewOrderId());
                }
            }
        }
        //Update Order No and BenId in Bendtl Arr
        for (int i = 0; i < orderBookingRequestModel.getBendtl().size(); i++) {
            for (int j = 0; j < orderBookingResponseVisitModel.getOrderids().size(); j++) {
                if (orderBookingRequestModel.getBendtl().get(i).getOrderNo().equals(orderBookingResponseVisitModel.getOrderids().get(j).getOldOrderId())) {
                    orderBookingRequestModel.getBendtl().get(i).setOrderNo(orderBookingResponseVisitModel.getOrderids().get(j).getNewOrderId());
                }
            }
            for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                if ((orderBookingRequestModel.getBendtl().get(i).getBenId() + "").equals(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                    orderBookingRequestModel.getBendtl().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                }
            }
        }
        //Update orderNo and BenId in BarcodeDtl Arr
        for (int i = 0; i < orderBookingRequestModel.getBarcodedtl().size(); i++) {
            for (int j = 0; j < orderBookingResponseVisitModel.getOrderids().size(); j++) {
                if (orderBookingRequestModel.getBarcodedtl().get(i).getOrderNo().equals(orderBookingResponseVisitModel.getOrderids().get(j).getOldOrderId())) {
                    orderBookingRequestModel.getBarcodedtl().get(i).setOrderNo(orderBookingResponseVisitModel.getOrderids().get(j).getNewOrderId());
                }
            }
            for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                if ((orderBookingRequestModel.getBarcodedtl().get(i).getBenId() + "").equals(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                    orderBookingRequestModel.getBarcodedtl().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                }
            }
        }
        //Update BenId in SmplDtl Arr
        for (int i = 0; i < orderBookingRequestModel.getSmpldtl().size(); i++) {
            for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                if ((orderBookingRequestModel.getSmpldtl().get(i).getBenId() + "").equals(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                    orderBookingRequestModel.getSmpldtl().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                }
            }
        }
        //Update BenId in ClHistory Arr
        for (int i = 0; i < orderBookingRequestModel.getClHistory().size(); i++) {
            for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                if ((orderBookingRequestModel.getClHistory().get(i).getBenId() + "").equals(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                    orderBookingRequestModel.getClHistory().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                }
            }
        }
        //Update BenId in LabAlert Arr
        for (int i = 0; i < orderBookingRequestModel.getLabAlert().size(); i++) {
            for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                if ((orderBookingRequestModel.getLabAlert().get(i).getBenId() + "").equals(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                    orderBookingRequestModel.getLabAlert().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                }
            }
        }
        return orderBookingRequestModel;
    }

    private class OrderBookingAPIAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                orderBookingResponseVisitModel = new ResponseParser(activity).getOrderBookingAPIResponse(json, statusCode);
                for (OrderBookingResponseOrderModel obrom :
                        orderBookingResponseVisitModel.getOrderids()) {
                    orderBookingResponseBeneficiaryModelArr.addAll(obrom.getBenfids());
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Amount Payable ₹ " + totalAmountPayable + "/-")
                        .setPositiveButton("Collect", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (btnProceedPayment.getText().equals("Proceed for Payment")) {
                                    final String[] paymentItems = new String[]{"Cash", "Digital"};
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    builder.setTitle("Choose Payment Mode")
                                            .setItems(paymentItems, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (paymentItems[which].equals("Cash")) {
                                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                                                        builder1.setMessage("Confirm Amount Received ₹ " + totalAmountPayable + "")
                                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        PaymentMode = 1;
                                                                        OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_cash");
                                                                        ApiCallAsyncTask workOrderEntryRequestAsyncTask = new AsyncTaskForRequest(activity).getWorkOrderEntryRequestAsyncTask(orderBookingRequestModel);
                                                                        workOrderEntryRequestAsyncTask.setApiCallAsyncTaskDelegate(new WorkOrderEntryAsyncTaskDelegateResult());
                                                                        if (isNetworkAvailable(activity)) {
                                                                            workOrderEntryRequestAsyncTask.execute(workOrderEntryRequestAsyncTask);
                                                                        } else {
                                                                            Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                })
                                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        PaymentMode = 2;
                                                        Intent intentPayments = new Intent(activity, PaymentsActivity.class);
                                                        intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT, totalAmountPayable + "");
                                                        intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 2);
                                                        intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO, orderVisitDetailsModel.getVisitId());
                                                        intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                                                        intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_NAME, orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getName());
                                                        intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_ADDRESS, orderVisitDetailsModel.getAllOrderdetails().get(0).getAddress());
                                                        intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_PIN, orderVisitDetailsModel.getAllOrderdetails().get(0).getPincode());
                                                        intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_MOBILE, orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                                                        intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_EMAIL, orderVisitDetailsModel.getAllOrderdetails().get(0).getEmail());
                                                        startActivityForResult(intentPayments, BundleConstants.PAYMENTS_START);
                                                    }
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                } else if (btnProceedPayment.getText().equals("Submit Work Order")) {
                                    OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_prepaid");
                                    ApiCallAsyncTask workOrderEntryRequestAsyncTask = new AsyncTaskForRequest(activity).getWorkOrderEntryRequestAsyncTask(orderBookingRequestModel);
                                    workOrderEntryRequestAsyncTask.setApiCallAsyncTaskDelegate(new WorkOrderEntryAsyncTaskDelegateResult());
                                    if (isNetworkAvailable(activity)) {
                                        workOrderEntryRequestAsyncTask.execute(workOrderEntryRequestAsyncTask);
                                    } else {
                                        Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "Order Booking Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private class WorkOrderEntryAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Order Status")
                        .setMessage("Work Order Entry Successful!\nPlease note Ref Id - " + orderVisitDetailsModel.getVisitId() + " for future references.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        })
                        .create()
                        .show();
            } else {
                Toast.makeText(activity,""+json,Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Order Status")
                        .setMessage("Work Order Entry Failed!")
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        })
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                btnProceedPayment.performClick();
                            }
                        })
                        .create()
                        .show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "Work Order Entry Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private class CartAPIAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                CartAPIResponseModel cartAPIResponseModel = new ResponseParser(activity).getCartAPIResponse(json, statusCode);
                if (cartAPIResponseModel != null
                        && !InputUtils.isNull(cartAPIResponseModel.getResponse())
                        && cartAPIResponseModel.getResponse().equals("SUCCESS")
                        && cartAPIResponseModel.getOrders() != null
                        && cartAPIResponseModel.getOrders().size() > 0) {

                    for (int i = 0; i < orderVisitDetailsModel.getAllOrderdetails().size(); i++) {
                        int orderAmountPayable = 0;
                        int orderAmountDue = 0;
                        for (int j = 0; j < cartAPIResponseModel.getOrders().size(); j++) {
                            if (orderVisitDetailsModel.getAllOrderdetails().get(i).getOrderNo().equals(cartAPIResponseModel.getOrders().get(j).getOrderNo())) {
                                orderAmountPayable = orderAmountPayable + cartAPIResponseModel.getOrders().get(j).getAmountDue();
                                orderAmountDue = orderAmountDue + cartAPIResponseModel.getOrders().get(j).getTestCharges() + cartAPIResponseModel.getOrders().get(j).getServiceCharge();
                                orderVisitDetailsModel.getAllOrderdetails().get(i).setAmountPayable(orderAmountPayable);
                                orderVisitDetailsModel.getAllOrderdetails().get(i).setAmountDue(orderAmountDue);
                                orderVisitDetailsModel.getAllOrderdetails().get(i).setReportHC(cartAPIResponseModel.getOrders().get(j).isHC() ? 1 : 0);
                                orderDetailsDao.insertOrUpdate(orderVisitDetailsModel.getAllOrderdetails().get(i));
                                break;
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(activity, "Failed to fetch updated Payment Details", Toast.LENGTH_SHORT).show();
            }
            initData();
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "Failed to fetch updated Payment Details", Toast.LENGTH_SHORT).show();
            initData();
        }
    }

}