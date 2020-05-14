package com.thyrocare.fragment;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.Controller.GetAcessTokenAndOTPAPIController;
import com.thyrocare.R;
import com.thyrocare.Retrofit.PostAPIInteface;
import com.thyrocare.Retrofit.RetroFit_APIClient;
import com.thyrocare.activity.AddEditBeneficiaryDetailsActivity;
import com.thyrocare.activity.OrderBookingActivity;
import com.thyrocare.activity.PaymentsActivity;
import com.thyrocare.adapter.BeneficiaryScreenSlidePagerAdapter;
import com.thyrocare.adapter.VisitOrderDisplayAdapter;
import com.thyrocare.application.ApplicationController;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.dao.models.BeneficiaryDetailsDao;
import com.thyrocare.dao.models.OrderDetailsDao;
import com.thyrocare.dao.utils.ConnectionDetector;
import com.thyrocare.delegate.RefreshBeneficiariesSliderDelegate;
import com.thyrocare.models.api.request.CartAPIRequestModel;
import com.thyrocare.models.api.request.OrderAllocationTrackLocationRequestModel;
import com.thyrocare.models.api.request.OrderBookingRequestModel;
import com.thyrocare.models.api.request.OrderPassRequestModel;
import com.thyrocare.models.api.request.RequestOTPModel;
import com.thyrocare.models.api.request.WOEOtpValidationRequestModel;
import com.thyrocare.models.api.response.CartAPIResponseModel;
import com.thyrocare.models.api.response.CommonPOSTResponseModel;
import com.thyrocare.models.api.response.ErrorModel;
import com.thyrocare.models.api.response.FetchOrderDetailsResponseModel;
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
import com.thyrocare.models.data.REMOVEBENSMSPOSTDATAModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.MyBroadcastReceiver;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.GPSTracker;
import com.thyrocare.utils.app.Global;
import com.thyrocare.utils.app.InputUtils;
import com.thyrocare.utils.app.VenuPuntureUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.network.AbstractApiModel.B2C_API_VERSION;
import static com.thyrocare.network.AbstractApiModel.SERVER_BASE_API_URL;
import static com.thyrocare.utils.app.BundleConstants.API_FOR_OTP;

public class BeneficiariesDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "BEN_DISPLAY_FRAGMENT";
    private OrderBookingActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    private ViewPager vpBeneficiaries;
    private TextView txtAmtPayable;
    private Button btnProceedPayment;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    private int totalAmountPayable = 0;
    String newTimeaddTwoHrs, newTimeaddTwoHalfHrs;
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
    private String test;
    public static String isPPBSTestRemoved = "normal";
    public static String isRBSTestRemoved = "normal";
    public static String isINSPPTestRemoved = "normal";
    public static String isFBSTestRemoved = "normal";
    public static String isINSFATestRemoved = "normal";
    boolean isOnlyWOE = false, isaddbeneficiary = false;
    //neha g -----------
    String datefrom_model = "";
    //neha g -------------
    //changes_17june2017
    //private TextView title_add_beneficiary;
    //changes_17june2017
    Date apitimeinHHMMFormat;
    private boolean isFetchingOrders = false;
    private boolean isEditMobile_email = true;
    private String OrderMode = "";
    private String[] paymentItems;
    private Dialog CustomDialogfor_WOE_OTPValidation;
    Global globalclass;
    private ConnectionDetector cd;
    private Button btn_MobileGetOTP,btn_MobileVerifyOTP,btn_MobileVerified,btn_EmailGetOTP,btn_EmailVerifyOTP,btn_EmailVerified;
    private boolean isMobilenoOTPVerfied = false,isEmailIDOTPVerfied = false;
    private EditText edt_mobileOTP,edt_EmailOTP;


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
        globalclass = new Global(activity);
        globalclass.setLoadingGIF(activity);
        cd = new ConnectionDetector(activity);
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
        if (totalAmountPayable > 0) {
            Logger.error("totalAmountPayable if1 " + totalAmountPayable);
            fetchOrderDetailByVisitRefreshAmountDue();
        } else {
            Logger.error("totalAmountPayable else1 " + totalAmountPayable);
            isEditMobile_email = orderVisitDetailsModel.getAllOrderdetails().get(0).isEditME();
        }


        // fetchDataOfVisitOrderForRefreshAmountDue();
        initListeners();

        return rootView;
    }

    private void fetchOrderDetailByVisitRefreshAmountDue() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchOrderDetailApiAsyncTask = asyncTaskForRequest.getFetchOrderDetailsByVisitRequestAsyncTask(orderVisitDetailsModel.getVisitId());
        fetchOrderDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchOrderDetailsByVisitIdApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            if (!isFetchingOrders) {
                isFetchingOrders = true;
                fetchOrderDetailApiAsyncTask.execute(fetchOrderDetailApiAsyncTask);
            }
        } else {
            TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);

        }
    }

    private void fetchDataOfVisitOrderForRefreshAmountDue() {

        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchOrderDetailApiAsyncTask = asyncTaskForRequest.getFetchOrderDetailsRequestAsyncTask(true);
        fetchOrderDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchOrderDetailsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            if (!isFetchingOrders) {
                isFetchingOrders = true;
                fetchOrderDetailApiAsyncTask.execute(fetchOrderDetailApiAsyncTask);
            }
        } else {
            TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);

        }
    }

    private void initListeners() {


        llAddBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isaddbeneficiary) {
                    OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("Button_proceed_payment");/*BeneficiaryDetailsModel*/

                    Log.e(TAG_FRAGMENT, "tests: " + orderBookingRequestModel.getBendtl().get(0).getTests());

                    if (isValidForEditing(orderBookingRequestModel.getBendtl().get(0).getTests())) {
                        //llAddBeneficiary.setEnabled(false);
                        Toast.makeText(activity, "This " + orderBookingRequestModel.getBendtl().get(0).getTests() + " Test Here you cannot Add Benificary  ", Toast.LENGTH_SHORT).show();
                        isOnlyWOE = true;
                    } else {
                        //0   Toast.makeText(getActivity(),"Feature Coming Soon Stay tuned...... ",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Confirm action")
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
                        */


                                tempBeneficiaryDetailsModel = new BeneficiaryDetailsModel();

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
                } else {
                    Toast.makeText(activity, "You cannot edit this Order", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btnProceedPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Have you verified Name/Age/gender of all beneficiary")
                        .setPositiveButton("Yes (Proceed)", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OrderBookingRequestModel orderBookingRequestModel = VenuPuntureUtils.ADD_ALL_VenupumturesInMainBookingRequestModel(generateOrderBookingRequestModel("Button_proceed_payment"));
                                if (!isValidForEditing(orderBookingRequestModel.getBendtl().get(0).getTests())) {
                                    if (validate(orderBookingRequestModel)) {
                                        ShowDialogToVerifyOTP();
                                    }
                                }else{
                                    ProceedWOEonSubmit();
                                }

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setCancelable(true)
                        .show();

            }
        });
    }

    private void ProceedWOEonSubmit() {

        Logger.error("btn proceed coming");

        // TODO code to Add again the Venupunture images stored in global array in MainbookingRequestModel
        OrderBookingRequestModel orderBookingRequestModel = VenuPuntureUtils.ADD_ALL_VenupumturesInMainBookingRequestModel(generateOrderBookingRequestModel("Button_proceed_payment"));
        if (validate(orderBookingRequestModel)) {
            Logger.error("Selcted testssssssss" + orderBookingRequestModel.getBendtl().get(0).getTests());

            if (isValidForEditing(orderBookingRequestModel.getBendtl().get(0).getTests())) {

                //llAddBeneficiary.setEnabled(false);
                //Toast.makeText(activity, "This"+orderBookingRequestModel.getBendtl().get(0).getTests()+" Test Here you cannot Add Benificary  ", Toast.LENGTH_SHORT).show();
                Logger.error("for PPBS");
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Payment already received. Please proceed")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                PaymentMode = 1;
                                OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_cash");
                                ApiCallAsyncTask workOrderEntryRequestAsyncTask = new AsyncTaskForRequest(activity).getWorkOrderEntryRequestAsyncTask(orderBookingRequestModel);

                                Logger.error("isINSPPTestRemoved status : " + isINSPPTestRemoved);
                                Logger.error("isINSFATestRemoved status : " + isINSFATestRemoved);


                                workOrderEntryRequestAsyncTask.setApiCallAsyncTaskDelegate(new WorkOrderEntryAsyncTaskDelegateResult());
                                if (isNetworkAvailable(activity)) {
                                    workOrderEntryRequestAsyncTask.execute(workOrderEntryRequestAsyncTask);
                                } else {
                                    Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                //  Logger.error("Selcted testssssssss"+orderBookingRequestModel.getBendtl().get(i).getTests());


                //neha g -----------------------------------
                CheckDelay();

                if (BundleConstants.delay != 0) {
                    System.out.println("notify enter");
                    showNotiication();
//neha g---------------------
                }
            } else if (!isEditMobile_email) {
                // TODO code to Add again the Venupunture images stored in global array in MainbookingRequestModel
                OrderBookingRequestModel orderBookingRequestModel1 = VenuPuntureUtils.ADD_ALL_VenupumturesInMainBookingRequestModel(generateOrderBookingRequestModel("work_order_entry_prepaid"));
                ApiCallAsyncTask workOrderEntryRequestAsyncTask = new AsyncTaskForRequest(activity).getWorkOrderEntryRequestAsyncTask(orderBookingRequestModel1);
                workOrderEntryRequestAsyncTask.setApiCallAsyncTaskDelegate(new WorkOrderEntryAsyncTaskDelegateResult());
                if (isNetworkAvailable(activity)) {
                    workOrderEntryRequestAsyncTask.execute(workOrderEntryRequestAsyncTask);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                Logger.error("Other than PPBS");

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
        }
    }

    private boolean isValidForEditing(String tests) {

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

    private boolean isValidForEditingRBS(String tests) {

        if (tests.equalsIgnoreCase(AppConstants.PPBS + "," + AppConstants.RBS)
                || tests.equalsIgnoreCase(AppConstants.RBS + "," + AppConstants.PPBS)
        ) {
            return true;
        }


        return false;
    }

    private boolean validate(OrderBookingRequestModel orderBookingRequestModel) {
        Logger.error("on btn proceed: " + AddEditBeneficiaryDetailsActivity.testEdit);
        if (isValidForEditingRBS(orderBookingRequestModel.getBendtl().get(0).getTests()) && BeneficiaryDetailsScanBarcodeFragment.rbsbarcode.equals("")) {
            globalclass.showalert_OK("Please scan all barcodes  ",activity);
            return false;
        }

        for (BeneficiaryBarcodeDetailsModel barcodesModel :
                orderBookingRequestModel.getBarcodedtl()) {
            if (InputUtils.isNull(barcodesModel.getBarcode())) {
                for (BeneficiaryDetailsModel bdm : orderBookingRequestModel.getBendtl()) {
                    if (barcodesModel.getBenId() == bdm.getBenId()) {
                        if (!BeneficiaryDetailsScanBarcodeFragment.IS_RBS_PPBS) {
                            globalclass.showalert_OK("Please scan all barcodes for " + bdm.getName(),activity);
                            return false;
                        } else {
                            return true;
                        }


                    }
                }
            }
        }

        for (BeneficiaryDetailsModel bdm : orderBookingRequestModel.getBendtl()) {
            if (InputUtils.isNull(bdm.getTests())) {
                globalclass.showalert_OK("Please select atleast one test for " + bdm.getName(),activity);
                return false;
            }
            if (bdm.getVenepuncture() == null
                    || bdm.getVenepuncture().toString().equalsIgnoreCase("null") || bdm.getVenepuncture().isEmpty()) {
                globalclass.showalert_OK("Please capture Beneficiary Barcode image for " + bdm.getName(),activity);
                return false;
            } else {
                Logger.error("bdm not null " + bdm.getVenepuncture());
            }

            // TODO code for validating TRF upload
            /*if (bdm.isTRF()){
                for (int i = 0; i < BundleConstants.TempVenuImageArylist.size(); i++) {
                    if (BundleConstants.TempVenuImageArylist.get(i).getBenID() == bdm.getBenId()){
                        if (InputUtils.isNull(BundleConstants.TempVenuImageArylist.get(i).getTRFImagePath())){
                             globalclass.showalert_OK("Please upload TRF for " + bdm.getName(),activity);
                            return false;
                        }
                    }
                }
            }*/
            // TODO code for validating TRF upload
        }


        for (BeneficiaryDetailsModel bdm : orderBookingRequestModel.getBendtl()) {

            Log.e(BeneficiariesDisplayFragment.class.getSimpleName(), "validateeeeee: " + bdm.getTests());
            Log.e(BeneficiariesDisplayFragment.class.getSimpleName(), "genderrr: " + bdm.getGender());

            if (bdm.getGender().equalsIgnoreCase("F") && (bdm.getTests().contains("PSA"))) {

                if (bdm.getTests().contains("FPSA")) {
                    globalclass.showalert_OK("FPSA test is not for Womens. Please change it for " + bdm.getName(),activity);
                } else {
                    globalclass.showalert_OK("PSA test is not for Womens. Please change it for " + bdm.getName(),activity);
                }
                return false;
            }
        }

        return true;
    }
    // neha g --------------------

    private void showNotiication() { //TODO CHANGE MSG
        AlarmManager alarmMgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, MyBroadcastReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(activity, 0, intent, 0);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmIntent);
    }

    //neha g---------------------------

    //neha g---------------------------

    private void CheckDelay() {
        int apptime = 0;
        int hours = new Time(System.currentTimeMillis()).getHours();
        int min = new Time(System.currentTimeMillis()).getMinutes();
        String currTime = hours + ":" + min;
        System.out.println("currtime" + currTime); //17:39
        datefrom_model = BundleConstants.ShowTimeInNotificatn; //17:30
        System.out.println();
        try {
            String[] timesplit = datefrom_model.split(":");

            int slothr = Integer.parseInt(timesplit[0]);
            int slotmin = Integer.parseInt(timesplit[1]);

            int subhr = hours - slothr;
            int submin = min - slotmin;
            if (slotmin == 00) {
                subhr = subhr - 1;
                submin = 30;
            }


            System.out.println("sub min" + submin);
            BundleConstants.delay = subhr + submin;
            BundleConstants.DoneworkOrder = 1;
            appPreferenceManager.setDelay(subhr + submin);


            System.out.println("dealy in order" + BundleConstants.delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //neha g ---------------------

    private void dateCheck() {
        //jai
        //minus 30 min
        Date strDate = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("hh:mm:a");
            Calendar cal = Calendar.getInstance();
            Date currentTime = cal.getTime();
            Logger.error(">> " + currentTime);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(currentTime);
            cal1.add(Calendar.HOUR, +2);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(currentTime);
            cal2.add(Calendar.MINUTE, +150);
            newTimeaddTwoHrs = df.format(cal1.getTime());
            newTimeaddTwoHalfHrs = df.format(cal2.getTime());
            Logger.error(">> ....." + newTimeaddTwoHrs);
            Logger.error(">> ....." + newTimeaddTwoHalfHrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        String Slot = orderVisitDetailsModel.getSlot();
        Logger.error("Slot" + Slot);
        dateCheck();
        if (from.equals("Button_proceed_payment")) {
            for (int i = 0; i < ordtl.size(); i++) {
                ordtl.get(i).setAddBen(false);

            }
        }
        Logger.error("tejas Amount when order booking " + totalAmountPayable);
        for (int i = 0; i < ordtl.size(); i++) {
            ordtl.get(i).setAmountDue(totalAmountPayable);
            ordtl.get(i).setAmountPayable(totalAmountPayable);
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


//jai last task

        if (BeneficiaryDetailsScanBarcodeFragment.IS_RBS_PPBS == true) {
            BeneficiaryDetailsScanBarcodeFragment.IS_RBS_PPBS = false;
            benBarcodeArr.add(0, BeneficiaryDetailsScanBarcodeFragment.beneficiaryBarcodeDetailsModelRBS);
        }


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


        //last task


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
        for (OrderDetailsModel orderDetailsModel : orderVisitDetailsModel.getAllOrderdetails()) {
            totalAmountPayable = totalAmountPayable + orderDetailsModel.getAmountPayable();
        }

        if (isOnlyWOE) {
            txtAmtPayable.setText("0/-");
        } else {
            txtAmtPayable.setText("" + totalAmountPayable + "/-");
        }

        if (totalAmountPayable == 0) {
            btnProceedPayment.setText("Submit Work Order");
        } else {
            btnProceedPayment.setText("PAY");
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
        beneficiaryScreenSlidePagerAdapter = new BeneficiaryScreenSlidePagerAdapter(getFragmentManager(), activity, beneficiariesArr,
                orderVisitDetailsModel.getAllOrderdetails(), new RefreshBeneficiariesSliderDelegate() {
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
                        crbm.setRemarks(ben.getRemarks());
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


        //jai
        OrderDetailsModel orderDetailsModel = new OrderDetailsModel();
        orderDetailsModel = orderVisitDetailsModel.getAllOrderdetails().get(0);
        if (orderDetailsModel.isEditOrder()) {
//            llAddBeneficiary.setEnabled(true);
            isaddbeneficiary = true;
            Logger.error("isEditOrder " + orderDetailsModel.isEditOrder());
        } else {
//            llAddBeneficiary.setEnabled(false);
            isaddbeneficiary = false;
            Logger.error("isEditOrder " + orderDetailsModel.isEditOrder());
        }
        //jai

    }

    @Override
    public void initUI() {
        super.initUI();
        vpBeneficiaries = (ViewPager) rootView.findViewById(R.id.vp_beneficiaries);
        btnProceedPayment = (Button) rootView.findViewById(R.id.btn_proceed_payment);
        llAddBeneficiary = (LinearLayout) rootView.findViewById(R.id.ll_add_beneficiary);
        txtAmtPayable = (TextView) rootView.findViewById(R.id.title_amt_payable);
        pagerIndicator = (LinearLayout) rootView.findViewById(R.id.viewPagerCountDots);
        OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("Button_proceed_payment");

        try {
            for (int i = 0; i < orderBookingRequestModel.getBendtl().size(); i++) {
                test = test + orderBookingRequestModel.getBendtl().get(i).getTests();
            }
        } catch (Exception e) {
            e.printStackTrace();
            test = "";
        }

        if (orderBookingRequestModel != null && orderBookingRequestModel.getBendtl().size() > 0) {
            Log.e(TAG_FRAGMENT, "initUI: " + orderBookingRequestModel.getBendtl().get(0).getTests());
            if (orderBookingRequestModel.getBendtl().get(0).getTests() != null) {
                if (isValidForEditing(orderBookingRequestModel.getBendtl().get(0).getTests())) {

//                    llAddBeneficiary.setEnabled(false);
                    isaddbeneficiary = false;
                    isOnlyWOE = true;

                }
            }
        }
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
                    crbm.setBenId(ben.getBenId() + "");
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
                // TODO code to reduce the size of Json by temporary storing Venupunture in global array
                OrderBookingRequestModel orderBookingRequestModel = VenuPuntureUtils.ADD_ALL_VenupumturesInMainBookingRequestModel(generateOrderBookingRequestModel("work_order_entry_digital"));
                orderBookingRequestModel = fixForAddBeneficiary(orderBookingRequestModel);
                ApiCallAsyncTask workOrderEntryRequestAsyncTask = new AsyncTaskForRequest(activity).getWorkOrderEntryRequestAsyncTask(orderBookingRequestModel);
                workOrderEntryRequestAsyncTask.setApiCallAsyncTaskDelegate(new WorkOrderEntryAsyncTaskDelegateResult());
                if (isNetworkAvailable(activity)) {
                    workOrderEntryRequestAsyncTask.execute(workOrderEntryRequestAsyncTask);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "Payment failed", Toast.LENGTH_SHORT).show();
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

    private void SendinglatlongOrderAllocation() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        OrderAllocationTrackLocationRequestModel orderAllocationTrackLocationRequestModel = new OrderAllocationTrackLocationRequestModel();

        orderAllocationTrackLocationRequestModel.setVisitId(orderVisitDetailsModel.getVisitId());
        orderAllocationTrackLocationRequestModel.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
        orderAllocationTrackLocationRequestModel.setStatus(5);

//Latlong added
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.canGetLocation()) {
            orderAllocationTrackLocationRequestModel.setLatitude(String.valueOf(gpsTracker.getLatitude()));
            orderAllocationTrackLocationRequestModel.setLongitude(String.valueOf(gpsTracker.getLongitude()));
        }


        ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderAllocationpost(orderAllocationTrackLocationRequestModel);
        orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderAllocationTrackLocationiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);

        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class OrderAllocationTrackLocationiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Logger.error("" + json);
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private class OrderBookingAPIAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {

                SendinglatlongOrderAllocation();

                orderBookingResponseVisitModel = new ResponseParser(activity).getOrderBookingAPIResponse(json, statusCode);
                for (OrderBookingResponseOrderModel obrom :
                        orderBookingResponseVisitModel.getOrderids()) {
                    orderBookingResponseBeneficiaryModelArr.addAll(obrom.getBenfids());
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Amount payable ₹ " + totalAmountPayable + "/-")
                        .setPositiveButton("Collect", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (btnProceedPayment.getText().equals("PAY")) {

                                    if (OrderMode.equalsIgnoreCase("LTD-BLD") || OrderMode.equalsIgnoreCase("LTD-NBLD")) {
                                        paymentItems = new String[]{"Cash"};
                                    } else {
                                        paymentItems = new String[]{"Cash", "Digital"};
                                    }

                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    builder.setTitle("Choose payment mode")
                                            .setItems(paymentItems, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (paymentItems[which].equals("Cash")) {

                                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                                                        builder1.setMessage("Confirm amount received ₹ " + totalAmountPayable + "")
                                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        PaymentMode = 1;
                                                                        // TODO code to Add again the Venupunture images stored in global array in MainbookingRequestModel
                                                                        OrderBookingRequestModel orderBookingRequestModel = VenuPuntureUtils.ADD_ALL_VenupumturesInMainBookingRequestModel(generateOrderBookingRequestModel("work_order_entry_cash"));
                                                                        ApiCallAsyncTask workOrderEntryRequestAsyncTask = new AsyncTaskForRequest(activity).getWorkOrderEntryRequestAsyncTask(orderBookingRequestModel);

                                                                        Logger.error("isINSPPTestRemoved status : " + isINSPPTestRemoved);
                                                                        Logger.error("isINSFATestRemoved status : " + isINSFATestRemoved);
                                                                        Logger.error("test123 : " + test.toUpperCase());

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
                                                        Logger.error("tejastotalAmountPayableatsending " + totalAmountPayable);
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
                                    // TODO code to Add again the Venupunture images stored in global array in MainbookingRequestModel
                                    OrderBookingRequestModel orderBookingRequestModel = VenuPuntureUtils.ADD_ALL_VenupumturesInMainBookingRequestModel(generateOrderBookingRequestModel("work_order_entry_prepaid"));
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
                try {
                    if (statusCode == 400) {
                        ErrorModel loginResponseModel = new ResponseParser(activity).getErrorResponseModel(json, statusCode);

                        if (loginResponseModel != null) {
                            Toast.makeText(activity, "" + loginResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "Order booking failed", Toast.LENGTH_SHORT).show();
        }
    }

    private class WorkOrderEntryAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {

                //show notification


                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Order Status")
                        .setMessage("Work order entry successful!\nPlease note ref id - " + orderVisitDetailsModel.getVisitId() + " for future references.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                /*   if (test.toUpperCase().contains("PPBS") && test.toUpperCase().contains("FBS"))  {*/
                                //  llAddBeneficiary.setEnabled(false);
                                Logger.error("isPPBSTestRemoved status : " + isPPBSTestRemoved);
                                Logger.error("isRBSTestRemoved status : " + isRBSTestRemoved);
                                Logger.error("isINSPPTestRemoved status : " + isINSPPTestRemoved);
                                Logger.error("isINSFATestRemoved status : " + isINSFATestRemoved);
                                Logger.error("test123 : " + test.toUpperCase());

                                if (test.toUpperCase().contains(AppConstants.PPBS)) {
                                    Logger.error("contains PPBS : ");
                                } else {
                                    Logger.error("not contains PPBS : ");
                                }
                                if (test.toUpperCase().contains(AppConstants.FBS)) {
                                    Logger.error("contains FBS : ");
                                } else {
                                    Logger.error("not contains FBS : ");
                                }


                                if (test.toUpperCase().contains(AppConstants.PPBS) && test.toUpperCase().contains(AppConstants.FBS) && /*jai*/isPPBSTestRemoved.equals("normal")
                                        && test.toUpperCase().contains("INSPP") && test.toUpperCase().contains("INSFA") && /*jai*/isINSPPTestRemoved.equals("normal")
                                        && test.toUpperCase().contains(AppConstants.RBS) && test.toUpperCase().contains(AppConstants.FBS) && /*jai*/isRBSTestRemoved.equals("normal")) {

                                    if (!isINSFATestRemoved.equals("removed") && !isFBSTestRemoved.equals("removed")) {
                                        Logger.error("should print revisit dialog for both: ");

                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for PPBS/RBS and INSPP in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        activity.finish();


                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();
                                    }

                                } else if (test.toUpperCase().contains(AppConstants.PPBS) && /*jai*/isPPBSTestRemoved.equals("normal")
                                        && test.toUpperCase().contains(AppConstants.RBS) && /*jai*/isRBSTestRemoved.equals("normal")
                                        && test.toUpperCase().contains(AppConstants.FBS)) {

                                    Logger.error("isFBSTestRemoved status : " + isFBSTestRemoved);
                                    //Dailog for PPBS after WOE Abhi//
                                    if (!isFBSTestRemoved.equals("removed")) {

                                        Logger.error("should print revisit dialog for ppbs and rbs: ");

                                        Logger.error("for PPBS and RBS");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for PPBS and RBS in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        activity.finish();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();

                                    }
                                    //  Logger.error("Selcted testssssssss"+orderBookingRequestModel.getBendtl().get(i).getTests());
                                } else if (test.toUpperCase().contains(AppConstants.INSPP) && test.toUpperCase().contains(AppConstants.INSFA)
                                        && /*jai*/isINSPPTestRemoved.equals("normal")) {

                                    Logger.error("isINSFATestRemoved status : " + isINSFATestRemoved);
                                    if (!isINSFATestRemoved.equals("removed")) {

                                        Logger.error("should print revisit dialog for insfa: ");

                                        Logger.error("for INSPP");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for INSPP in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        activity.finish();


                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();
                                    }
                                    //  Logger.error("Selcted testssssssss"+orderBookingRequestModel.getBendtl().get(i).getTests());
                                } else if (test.toUpperCase().contains(AppConstants.RBS) && test.toUpperCase().contains(AppConstants.FBS)
                                        && /*jai*/isRBSTestRemoved.equals("normal")) {

                                    Logger.error("isRBSTestRemoved status : " + isFBSTestRemoved);
                                    if (!isFBSTestRemoved.equals("removed")) {

                                        Logger.error("should print revisit dialog for rbs: ");

                                        Logger.error("for rbs");

                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for RBS in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        activity.finish();


                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();
                                    }
                                    //  Logger.error("Selcted testssssssss"+orderBookingRequestModel.getBendtl().get(i).getTests());
                                } else if (test.toUpperCase().contains(AppConstants.PPBS) && test.toUpperCase().contains(AppConstants.FBS) && isPPBSTestRemoved.equals("normal")) {

                                    Logger.error("isPPBSTestRemoved status : " + isFBSTestRemoved);
                                    if (!isFBSTestRemoved.equals("removed")) {

                                        Logger.error("should print revisit dialog for rbs: ");

                                        Logger.error("for ppbs");

                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for PPBS in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        activity.finish();


                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();
                                    }
                                    //  Logger.error("Selcted testssssssss"+orderBookingRequestModel.getBendtl().get(i).getTests());
                                } else {
                                    Logger.error("testcode in else " + test.toUpperCase());
                                    activity.finish();
                                }

                            }
                        })
                        .create()
                        .show();
            } else {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(activity, "Work order entry failed", Toast.LENGTH_SHORT).show();
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

                if (BundleConstants.RemoveBenId != 0) {
                    CallApiForRemoveBenSMS(BundleConstants.RemoveBenId, cartAPIResponseModel.getOrders().get(0).getOrderNo(), cartAPIResponseModel.getOrders().get(0).getAmountDue());
                    BundleConstants.RemoveBenId = 0;
                }

            } else {
                Toast.makeText(activity, "Failed to fetch updated payment details", Toast.LENGTH_SHORT).show();
            }
            initData();
            if (totalAmountPayable > 0) {
                Logger.error("totalAmountPayable if2 " + totalAmountPayable);
                fetchOrderDetailByVisitRefreshAmountDue();
            } else {
                Logger.error("totalAmountPayable else2 " + totalAmountPayable);
            }

            // fetchDataOfVisitOrderForRefreshAmountDue();
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "Failed to fetch updated payment details", Toast.LENGTH_SHORT).show();
            initData();
            if (totalAmountPayable > 0) {
                Logger.error("totalAmountPayable if3 " + totalAmountPayable);
                fetchOrderDetailByVisitRefreshAmountDue();
            } else {
                Logger.error("totalAmountPayable else3 " + totalAmountPayable);
            }

            // fetchDataOfVisitOrderForRefreshAmountDue();
        }
    }

    private void CallApiForRemoveBenSMS(int removeBenId, String orderNo, int amountDue) {
        REMOVEBENSMSPOSTDATAModel ent = new REMOVEBENSMSPOSTDATAModel();
        ent.setBenId(removeBenId);
        ent.setOrderNo(orderNo);
        ent.setRate1("" + amountDue);
        ApiCallAsyncTask auxlAsyncTask = new AsyncTaskForRequest(activity).getRemoveBenSMSAsyncTask(ent);
        auxlAsyncTask.setApiCallAsyncTaskDelegate(new getReMOveBenSMSAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            auxlAsyncTask.execute(auxlAsyncTask);
        } else {
            Toast.makeText(activity, getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
        }
    }

    private class getReMOveBenSMSAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private class FetchOrderDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

            if (statusCode == 200) {

                //jai
                JSONObject jsonObject = new JSONObject(json);


                ResponseParser responseParser = new ResponseParser(activity);
                FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel = new FetchOrderDetailsResponseModel();
                fetchOrderDetailsResponseModel = responseParser.getFetchOrderDetailsResponseModel(json, statusCode);
                if (fetchOrderDetailsResponseModel != null && fetchOrderDetailsResponseModel.getOrderVisitDetails().size() > 0) {

                    for (OrderVisitDetailsModel orderVisitDetailsModel :
                            fetchOrderDetailsResponseModel.getOrderVisitDetails()) {


                        if (orderVisitDetailsModel.getAllOrderdetails() != null && orderVisitDetailsModel.getAllOrderdetails().size() > 0) {
                            Logger.error("tejas3");
                            Logger.error("tejas4 " + VisitOrderDisplayAdapter.posForAmountDue);

                            for (OrderDetailsModel orderDetailsModel :
                                    orderVisitDetailsModel.getAllOrderdetails()) {
                                Logger.error("tejas5 " + orderDetailsModel.getAmountDue());
                            }

                            totalAmountPayable = orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue();
                            Logger.error("tttejas1 " + orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue());

                            txtAmtPayable.setText("" + orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue());

                            for (OrderDetailsModel orderDetailsModel :
                                    orderVisitDetailsModel.getAllOrderdetails()) {
                                orderDetailsModel.setAmountPayable(orderVisitDetailsModel.getAllOrderdetails().get(VisitOrderDisplayAdapter.posForAmountDue).getAmountDue());
                                Logger.error("tttejas2 " + orderVisitDetailsModel.getAllOrderdetails().get(VisitOrderDisplayAdapter.posForAmountDue).getAmountDue());
                                txtAmtPayable.setText("" + orderVisitDetailsModel.getAllOrderdetails().get(VisitOrderDisplayAdapter.posForAmountDue).getAmountDue());
                            }

                        }
                    }
                }


            }
            //  isFetchingOrders = false;
            //initData();
        }

        @Override
        public void onApiCancelled() {
            // isFetchingOrders = false;
            TastyToast.makeText(activity, getString(R.string.network_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            //  Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
        /*

         */
    }

    private class FetchOrderDetailsByVisitIdApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.error("OrderDetailsByVisitIdApiAsyncTaskDelegateResult " + json);
            JSONObject jsonObject = new JSONObject(json);

            JSONArray orderVisitDetailsArray = jsonObject.getJSONArray("orderVisitDetails");
            JSONObject orderVisitDetailsObject = orderVisitDetailsArray.getJSONObject(0);
            JSONArray allOrderdetailsArray = orderVisitDetailsObject.getJSONArray("allOrderdetails");
            JSONObject allOrderdetailsObject = allOrderdetailsArray.getJSONObject(0);
            // Logger.error("a123mount "+allOrderdetailsObject.getString("AmountDue"));

            Logger.error("a123mount int " + allOrderdetailsObject.getInt("AmountDue"));

//            totalAmountPayable = jsonObject.getInt("AmountDue");

            OrderMode = allOrderdetailsObject.optString("OrderMode", "");
            isEditMobile_email = allOrderdetailsObject.optBoolean("EditME", true);
            totalAmountPayable = allOrderdetailsObject.getInt("AmountDue");
            isaddbeneficiary = allOrderdetailsObject.optBoolean("EditOrder", false);

            Logger.error("tttejas1 " + totalAmountPayable);
            if (isOnlyWOE) {
                txtAmtPayable.setText("0/-");
            } else {
                txtAmtPayable.setText("" + totalAmountPayable);
            }


        }

        @Override
        public void onApiCancelled() {

        }
    }


    private void ShowDialogToVerifyOTP() {
        isMobilenoOTPVerfied = false;
        isEmailIDOTPVerfied = false;

        CustomDialogfor_WOE_OTPValidation = new Dialog(activity);
        CustomDialogfor_WOE_OTPValidation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomDialogfor_WOE_OTPValidation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomDialogfor_WOE_OTPValidation.setContentView(R.layout.woe_otp_validation_dialog);
        CustomDialogfor_WOE_OTPValidation.setCancelable(false);

        RelativeLayout rel_main = (RelativeLayout) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.rel_main);
        ImageView img_close = (ImageView) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.img_close);

        edt_mobileOTP = (EditText) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.edt_mobileOTP);
        edt_EmailOTP = (EditText) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.edt_EmailOTP);
        btn_MobileGetOTP = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_MobileGetOTP);
        btn_MobileVerifyOTP = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_MobileVerifyOTP);
        btn_MobileVerified = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_MobileVerified);
        btn_EmailGetOTP = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_EmailGetOTP);
        btn_EmailVerifyOTP = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_EmailVerifyOTP);
        btn_EmailVerified = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_EmailVerified);
        Button btn_proceed_afterOTP = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_proceed_afterOTP);



        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = 0,width = 0;
        if (displayMetrics != null) {
            try {
                height = displayMetrics.heightPixels;
                width = displayMetrics.widthPixels;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width - 150, FrameLayout.LayoutParams.WRAP_CONTENT);
        rel_main.setLayoutParams(lp);

        final String mobileNo = orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile();
        final String OrderNo = orderVisitDetailsModel.getVisitId();

        CustomDialogfor_WOE_OTPValidation.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogfor_WOE_OTPValidation.dismiss();
            }
        });

        btn_MobileGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    CallGenerateOTPApi(mobileNo,"SENDOTPALL","Mobile",OrderNo);
                } else {
                    globalclass.showCustomToast(activity, activity.getResources().getString(R.string.plz_chk_internet));
                }
            }
        });


        btn_MobileVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strOTP = edt_mobileOTP.getText().toString().trim();
                if (InputUtils.isNull(strOTP) ) {
                    globalclass.showalert_OK("Please enter OTP",activity);
                    edt_mobileOTP.requestFocus();
                }else if (strOTP.length() != 4) {
                    globalclass.showalert_OK("Please enter valid OTP. Length required : 4",activity);
                    edt_mobileOTP.requestFocus();
                } else {
                    WOEOtpValidationRequestModel model = new WOEOtpValidationRequestModel();
                    model.setOrderno(OrderNo);
                    model.setOtp(strOTP);
                    model.setOtpTo("Mobile");
                    if (cd.isConnectingToInternet()) {
                        CallValidateOTPAPI(model);
                    } else {
                        globalclass.showCustomToast(activity, activity.getResources().getString(R.string.plz_chk_internet));
                    }
                }

            }
        });

        btn_EmailGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    CallGenerateOTPApi(mobileNo,"SENDOTPALL","Email",OrderNo);
                } else {
                    globalclass.showCustomToast(activity, activity.getResources().getString(R.string.plz_chk_internet));
                }
            }
        });


        btn_EmailVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strOTP = edt_EmailOTP.getText().toString().trim();
                if (InputUtils.isNull(strOTP) ) {
                    globalclass.showalert_OK("Please enter OTP",activity);
                    edt_EmailOTP.requestFocus();
                }else if ( strOTP.length() != 4) {
                    globalclass.showalert_OK("Please enter valid OTP. Length required : 4",activity);
                    edt_EmailOTP.requestFocus();
                } else {
                    WOEOtpValidationRequestModel model = new WOEOtpValidationRequestModel();
                    model.setOrderno(OrderNo);
                    model.setOtp(strOTP);
                    model.setOtpTo("Email");
                    if (cd.isConnectingToInternet()) {
                        CallValidateOTPAPI(model);
                    } else {
                        globalclass.showCustomToast(activity, activity.getResources().getString(R.string.plz_chk_internet));
                    }
                }

            }
        });

        btn_proceed_afterOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMobilenoOTPVerfied){
                    globalclass.showalert_OK("Please verify OTP for Mobile number.",activity);
                    edt_mobileOTP.requestFocus();
                }else if (!isEmailIDOTPVerfied) {
                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder;
                    alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(activity);
                    alertDialogBuilder
                            .setMessage("Do you want to proceed without verifying Email-ID OTP ?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ProceedWOEonSubmit();
                                    if (CustomDialogfor_WOE_OTPValidation!= null && CustomDialogfor_WOE_OTPValidation.isShowing()){
                                        CustomDialogfor_WOE_OTPValidation.dismiss();
                                    }
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{

                    if (CustomDialogfor_WOE_OTPValidation!= null && CustomDialogfor_WOE_OTPValidation.isShowing()){
                        CustomDialogfor_WOE_OTPValidation.dismiss();
                    }
                    ProceedWOEonSubmit();
                }
            }
        });

    }

    private void CallGenerateOTPApi(String mobileNumber, String Purpose, final String OTPVia, String orderno) {

        if (ApplicationController.getAcessTokenAndOTPAPIController != null) {
            ApplicationController.getAcessTokenAndOTPAPIController = null;
        }

        ApplicationController.getAcessTokenAndOTPAPIController = new GetAcessTokenAndOTPAPIController(activity);
        ApplicationController.getAcessTokenAndOTPAPIController.CallGetTokenAPIForOTP(mobileNumber, Purpose,OTPVia,orderno);
        ApplicationController.getAcessTokenAndOTPAPIController.setOnResponseListener(new GetAcessTokenAndOTPAPIController.OnResponseListener() {
            @Override
            public void onSuccess(CommonPOSTResponseModel commonPOSTResponseModel) {
                onGetOTPResponseReceived(commonPOSTResponseModel,OTPVia);
            }

            @Override
            public void onfailure(CommonPOSTResponseModel commonPOSTResponseModel) {
                onGetOTPResponseReceived(commonPOSTResponseModel,OTPVia);
            }
        });
    }

    private void onGetOTPResponseReceived(CommonPOSTResponseModel model1,String OtpVia) {

        if (!InputUtils.isNull(model1.getResponse1()) && model1.getResponse1().equalsIgnoreCase("SUCCESS")) {
            if (OtpVia.equalsIgnoreCase("Mobile")){
                btn_MobileGetOTP.setVisibility(View.GONE);
                btn_MobileVerified.setVisibility(View.GONE);
                btn_MobileVerifyOTP.setVisibility(View.VISIBLE);
                edt_mobileOTP.setVisibility(View.VISIBLE);
            }else if (OtpVia.equalsIgnoreCase("Email")){
                btn_EmailGetOTP.setVisibility(View.GONE);
                btn_EmailVerified.setVisibility(View.GONE);
                btn_EmailVerifyOTP.setVisibility(View.VISIBLE);
                edt_EmailOTP.setVisibility(View.VISIBLE);
            }
        } else if (!InputUtils.isNull(model1.getResponse1()) && ( model1.getResponse1().contains("Mailbox unavailable") || model1.getResponse1().contains("Failure sending mail"))) {
            if (OtpVia.equalsIgnoreCase("Mobile")){
                btn_MobileGetOTP.setVisibility(View.GONE);
                btn_MobileVerified.setVisibility(View.GONE);
                btn_MobileVerifyOTP.setVisibility(View.VISIBLE);
                edt_mobileOTP.setVisibility(View.VISIBLE);

            }else if (OtpVia.equalsIgnoreCase("Email")){
                btn_EmailGetOTP.setVisibility(View.GONE);
                btn_EmailVerified.setVisibility(View.GONE);
                btn_EmailVerifyOTP.setVisibility(View.VISIBLE);
                edt_EmailOTP.setVisibility(View.VISIBLE);
            }
        } else {
            globalclass.showalert_OK(!InputUtils.isNull(model1.getResponse1()) ? model1.getResponse1() : "Sorry we are facing issue while sending OTP. Please try again later.",activity);

        }

    }

    private void CallValidateOTPAPI(final WOEOtpValidationRequestModel model) {

        PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, SERVER_BASE_API_URL).create(PostAPIInteface.class);
        Call<CommonPOSTResponseModel> responseCall = apiInterface.ValidateWoeOTPAPI(model);
        globalclass.showProgressDialog();
        responseCall.enqueue(new Callback<CommonPOSTResponseModel>() {
            @Override
            public void onResponse(Call<CommonPOSTResponseModel> call, Response<CommonPOSTResponseModel> response) {
                globalclass.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    CommonPOSTResponseModel ResponseModel = response.body();
                    if (ResponseModel != null && !InputUtils.isNull(ResponseModel.getResponse1()) && ResponseModel.getResponse1().equalsIgnoreCase("SUCCESS")){

                        if (model.getOtpTo().equalsIgnoreCase("Mobile")){
                            btn_MobileVerified.setVisibility(View.VISIBLE);
                            btn_MobileVerifyOTP.setVisibility(View.GONE);
                            btn_MobileGetOTP.setVisibility(View.GONE);
                            edt_mobileOTP.setEnabled(false);
                            isMobilenoOTPVerfied = true;
                        }else  if (model.getOtpTo().equalsIgnoreCase("Email")){
                            btn_EmailVerified.setVisibility(View.VISIBLE);
                            btn_EmailVerifyOTP.setVisibility(View.GONE);
                            btn_EmailGetOTP.setVisibility(View.GONE);
                            edt_EmailOTP.setEnabled(false);
                            isEmailIDOTPVerfied = true;
                        }

                    }else{
                        globalclass.showCustomToast(activity, "OTP Validation Failed. Please enter Valid OTP.");
                    }
                } else {
                    globalclass.showCustomToast(activity, "Unable to connect to the server. Please try after sometime.");

                }
            }
            @Override
            public void onFailure(Call<CommonPOSTResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog();
                globalclass.showCustomToast(activity, "Something went wrong. Please try after sometime.");
            }
        });

    }

}
