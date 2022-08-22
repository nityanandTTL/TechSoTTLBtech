package com.thyrocare.btechapp.NewScreenDesigns.Activities;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CHECK_INTERNET_CONN;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.NO_DATA_FOUND;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.OrdercontainonlyOneBeneficaryCannotremovedMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;
import static com.thyrocare.btechapp.utils.app.AppConstants.MSG_SERVER_EXCEPTION;
import static com.thyrocare.btechapp.utils.app.CommonUtils.isValidForEditing;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.Controller.OrderReleaseRemarksController;
import com.thyrocare.btechapp.Controller.PEAuthorizationController;
import com.thyrocare.btechapp.Controller.PEOrderEditController;
import com.thyrocare.btechapp.Controller.SendLatLongforOrderController;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.DisplaySelectedTestsListForCancellationAdapter_new;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.ExpandableTestMasterListDisplayAdapter_new;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.StartArriveOrderDetailsAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.B2BVisitOrdersDisplayFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.VisitOrdersDisplayFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.SendSMSAfterBenRemovedRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.DateUtil;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.activity.KIOSK_Scanner_Activity;
import com.thyrocare.btechapp.activity.NewOrderReleaseActivity;
import com.thyrocare.btechapp.activity.PaymentsActivity;
import com.thyrocare.btechapp.adapter.OrderReleaseAdapter;
import com.thyrocare.btechapp.adapter.SlotDateAdapter;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.thyrocare.btechapp.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.thyrocare.btechapp.dialog.ConfirmRequestReleaseDialog;
import com.thyrocare.btechapp.dialog.RescheduleOrderDialog;
import com.thyrocare.btechapp.models.api.request.OrderBookingRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderPassRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.PEOrderEditRequestModel;
import com.thyrocare.btechapp.models.api.request.RemoveBeneficiaryAPIRequestModel;
import com.thyrocare.btechapp.models.api.request.SendEventRequestModel;
import com.thyrocare.btechapp.models.api.request.SendOTPRequestModel;
import com.thyrocare.btechapp.models.api.request.ServiceUpdateRequestModel;
import com.thyrocare.btechapp.models.api.request.SetDispositionDataModel;
import com.thyrocare.btechapp.models.api.response.CommonResponseModel2;
import com.thyrocare.btechapp.models.api.response.FetchOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.api.response.GetPECancelRemarksResponseModel;
import com.thyrocare.btechapp.models.api.response.GetPETestResponseModel;
import com.thyrocare.btechapp.models.api.response.GetRemarksResponseModel;
import com.thyrocare.btechapp.models.api.response.GetTestListResponseModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.DispositionDataModel;
import com.thyrocare.btechapp.models.data.DispositionDetailsModel;
import com.thyrocare.btechapp.models.data.OrderDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.models.data.TestRateMasterModel;
import com.thyrocare.btechapp.service.TrackerService;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.DateUtils;
import com.thyrocare.btechapp.utils.app.GPSTracker;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;
import com.thyrocare.btechapp.utils.app.VenuPuntureUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTimeComparator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import application.ApplicationController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StartAndArriveActivity extends AppCompatActivity {

    Activity mActivity;
    Global globalclass;
    ConnectionDetector cd;
    AppPreferenceManager appPreferenceManager;
    TextView txt_amount;
    Button btn_start, btn_arrive, btn_Proceed;
    CardView lin_bottom;
    RecyclerView recyle_OrderDetail;
    TextView txtNoRecord;
    LinearLayout ll_amt;
    TextView txtAmountPayable, btnclose, txtTestsList;
    Button btnOrderSubmit;
    ArrayList<TestRateMasterModel> selectedTestsList = new ArrayList<>();
    ArrayList<TestRateMasterModel> edit_selectedTestsList = new ArrayList<>();
    RelativeLayout customSwipeButton2;
    TextView tv_toolbar, tv_timerCount, tv_timerTxt, tv_order_release;
    ImageView iv_back, iv_home;
    String productType;
    ArrayList<GetPETestResponseModel.DataDTO> peTestArraylist;
    LinearLayout ll_timer, LL_swipe;
    ArrayList<GetRemarksResponseModel> remarksArray;
    CountDownTimer countDownTimer;
    BottomSheetDialog bottomSheetOrderReschedule, bottomSheetOrderRelease;
    private OrderVisitDetailsModel orderDetailsModel;
    private Intent FirebaselocationUpdateIntent;
    private GPSTracker gpsTracker;
    private StartArriveOrderDetailsAdapter startArriveOrderDetailsAdapter;
    private NestedScrollView nestedScroll_StartArrive;
    private TextView btn_floating_add_ben, tv_note_res;
    private String strOrderNo, pincode;
    // add edit ben dialog
    private Dialog bendialog;
    private boolean FlagADDEditBen = false;
    private EditText edtBenAge, edtBenName;
    private ImageView imgBenGenderF, imgBenGenderM, imgReportHC, imgBenAddTests;
    private boolean isM = false;
    private boolean isRHC = false;
    private String SelectedTestCode = "";
    private int CallCartAPIFlag = 0;
    private boolean testListFlag;
    private int PSelected_position;
    private OrderBookingRequestModel FinalSubmitDataModel;
    private int AddBenCartFlag = 0;
    private int benId = 0;
    private ExpandableTestMasterListDisplayAdapter_new expAdapter;
    private DisplaySelectedTestsListForCancellationAdapter_new displayAdapter;
    private boolean isTestEdit = false;
    private Dialog CustomDialogforOTPValidation;
    private RemoveBeneficiaryAPIRequestModel removebenModel;
    private Dialog dialog_ready;
    private ArrayList<String> remarks_notc_arr;
    private DispositionDetailsModel remarksDataModel;
    private String remarks_notc_str = "";
    private ArrayList<DispositionDetailsModel> remarksDataModelsarr;
    private ArrayList<String> remarksarr;
    private ConfirmRequestReleaseDialog crr;
    private CharSequence[] items;
    private String cancelVisit = "n";
    private int totalAmountPayable = 0;
    private String[] paymentItems;
    private int PaymentMode;

    @Override
    public void onBackPressed() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetTheme);

        View bottomSheet = LayoutInflater.from(this).inflate(R.layout.logout_bottomsheet, (ViewGroup) this.findViewById(R.id.bottom_sheet_dialog_parent));
        TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
        tv_text.setText("Are you sure, you want to go back ?");
        Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BundleConstants.isKIOSKOrder = false;
                bottomSheetDialog.dismiss();
                finish();
            }
        });

        Button btn_no = bottomSheet.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

            }
        });
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (BundleConstants.PE_HARD_BLOCKING) {
            btn_Proceed.setEnabled(true);
            btn_Proceed.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_and_arrive);
        mActivity = StartAndArriveActivity.this;

        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        gpsTracker = new GPSTracker(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);
        FirebaselocationUpdateIntent = new Intent(this, TrackerService.class);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        orderDetailsModel = bundle.getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
        strOrderNo = orderDetailsModel.getVisitId();
        pincode = orderDetailsModel.getAllOrderdetails().get(0).getPincode();
        BundleConstants.addPaymentFlag = 0;

        initView();
//        initToolBar();s
        initListerners();

        if (BundleConstants.setRefreshStartArriveActivity == 1) {
            BundleConstants.setRefreshStartArriveActivity = 0;
            btn_arrive.setVisibility(View.GONE);
            btn_start.setVisibility(View.GONE);

            //hardblocking
//            if (BundleConstants.isPEPartner || BundleConstants.PEDSAOrder) {
            if (appPreferenceManager.isPEPartner() || appPreferenceManager.PEDSAOrder()) {
                if (checkForTest(1)) {
                    btn_Proceed.setEnabled(true);
                    btn_Proceed.setVisibility(View.VISIBLE);
                }
            } else {
                btn_Proceed.setVisibility(View.VISIBLE);
            }
//            btn_Proceed.setVisibility(View.VISIBLE);


            //fungible
//            if (BundleConstants.isPEPartner && BundleConstants.PEDSAOrder) {
            if (appPreferenceManager.isPEPartner() && appPreferenceManager.PEDSAOrder()) {
//            if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                btn_floating_add_ben.setVisibility(View.GONE);
//            } else if (BundleConstants.isPEPartner) {
            } else if (appPreferenceManager.isPEPartner()) {
                btn_floating_add_ben.setVisibility(View.VISIBLE);
            } else {
                if (orderDetailsModel.getAllOrderdetails().get(0).getAmountPayable() == 0) {
                    btn_floating_add_ben.setVisibility(View.GONE);
                } else {
                    btn_floating_add_ben.setVisibility(View.VISIBLE);
                }
            }
            if (cd.isConnectingToInternet()) {
                CallOrderDetailAPI("Arrive");
            } else {
                globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);
            }
        } else {
            if (BundleConstants.isKIOSKOrder) {
                SetTitleHead("Order Details");
                btn_arrive.setVisibility(View.GONE);
                btn_start.setVisibility(View.GONE);
                btn_Proceed.setVisibility(View.VISIBLE);
                customSwipeButton2.setVisibility(View.GONE);
       /* if (cd.isConnectingToInternet()) {
            CallOrderDetailAPI("Arrive");
        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);
        }*/
                initData("Arrive");
            } else {
                if (orderDetailsModel.getAllOrderdetails().get(0).isKCF()) {
                    btn_arrive.setVisibility(View.GONE);
                    btn_start.setVisibility(View.GONE);
                    btn_Proceed.setVisibility(View.VISIBLE);
                    if (cd.isConnectingToInternet()) {
                        CallOrderDetailAPI("Arrive");
                    } else {
                        globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);
                    }
                } else {
                    btn_arrive.setVisibility(View.VISIBLE);
                    btn_start.setVisibility(View.GONE);
                    if (cd.isConnectingToInternet()) {
                        CallOrderDetailAPI("Start");
                    } else {
                        globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);
                    }
                }
            }
        }

    }

  /*  private void initTimer() {
        if (!InputUtils.isNull(appPreferenceManager.getOrderNo()) &&
                InputUtils.CheckEqualIgnoreCase(appPreferenceManager.getOrderNo(), orderDetailsModel.getVisitId())) {
            if (Global.toDisplayTimerFlag) {
                if (Global.TimerFlag) {
                    reloadActivity();
                } else {
                    displayTimerCount();
                    ll_timer.setVisibility(View.VISIBLE);
                }
            } else {
                ll_timer.setVisibility(View.GONE);
            }
        } else {
            ll_timer.setVisibility(View.GONE);
        }
    }*/

    private void SetTitleHead(String head_titl) {
        try {
            setTitle(head_titl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initToolBar() {
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarStockAvailablity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_arrive_menu_screen, menu);
        MenuItem menu_release = menu.findItem(R.id.menu_release);
        if (BundleConstants.isKIOSKOrder) {
            menu_release.setVisible(false);
        } else {
            menu_release.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_release) {
            if (BundleConstants.isKIOSKOrder) {

            } else {
                onReleaseButtonClicked();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initView() {
        lin_bottom = findViewById(R.id.lin_bottom);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        iv_back = findViewById(R.id.iv_back);
        iv_home = findViewById(R.id.iv_home);
        tv_note_res = (TextView) findViewById(R.id.tv_note_res);
        ll_timer = findViewById(R.id.ll_timer);
        LL_swipe = findViewById(R.id.LL_swipe);
        tv_timerCount = findViewById(R.id.tv_timerCount);
        tv_timerTxt = findViewById(R.id.tv_timerTxt);
        tv_order_release = findViewById(R.id.tv_order_release);
        txt_amount = (TextView) findViewById(R.id.txt_amount);
        txtNoRecord = (TextView) findViewById(R.id.txtNoRecord);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_arrive = (Button) findViewById(R.id.btn_arrive);
        btn_Proceed = (Button) findViewById(R.id.btn_Proceed);
        recyle_OrderDetail = (RecyclerView) findViewById(R.id.recyle_OrderDetail);
        btn_floating_add_ben = (TextView) findViewById(R.id.btn_floating_add_ben);
        tv_toolbar.setText("Arrive");
        customSwipeButton2 = findViewById(R.id.customSwipeButton2);
        tv_toolbar.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        iv_home.setVisibility(View.GONE);

        // TODO Add ben not allowed for PE-Btech time being  GG Sir's instruction
        //   if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName()) || btn_arrive.getVisibility() == View.VISIBLE) {
        //      btn_floating_add_ben.setVisibility(View.GONE);
        //   } else {
        //      btn_floating_add_ben.setVisibility(View.VISIBLE);
        //  }

        if (BundleConstants.isKIOSKOrder) {
            customSwipeButton2.setVisibility(View.GONE);
        } else {
            customSwipeButton2.setVisibility(View.VISIBLE);
        }
        Constants.clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());

        //Mith
        //fungible
//        if (BundleConstants.isPEPartner || BundleConstants.PEDSAOrder) {
        if (appPreferenceManager.isPEPartner() || appPreferenceManager.PEDSAOrder()) {
//        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
            callAPIForPETest();
        }

    }

    private void reloadActivity() {
        Global.TimerFlag = false;
        startActivity(getIntent());
    }

    private void displayTimerCount() {
        Global.displayedtimer = true;
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), "Wait for %02d min: %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                tv_timerCount.setText(text);
                customSwipeButton2.setEnabled(false);
                LL_swipe.setBackground(getResources().getDrawable(R.drawable.rounded_background_grey));
            }

            public void onFinish() {
                tv_timerCount.setText("Wait for 00:00");
                tv_timerTxt.setText("Release the order if you did not connect with the customer");
                customSwipeButton2.setEnabled(true);
                LL_swipe.setBackground(getResources().getDrawable(R.drawable.rounded_background_green_2));
            }
        };
    }

    private void callAPIForPETest() {

        String productType = "Integration_Order";
        PEAuthorizationController peAC = new PEAuthorizationController(this);
        peAC.getAuthorizationToken(0, orderDetailsModel.getAllOrderdetails().get(0).getPincode(), orderDetailsModel.getAllOrderdetails().get(0).getVisitId());
    }

    private void initListerners() {

        customSwipeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO for testing order release.
                //fungible
                //Mith
                /*if (BundleConstants.isPEPartner && !BundleConstants.PEDSAOrder) {
                    if (orderDetailsModel.getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("PARTIAL SERVICED") && isValidForEditing(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())){
                        onReleaseButtonClicked();
                    }else {
                        onOrderRelease();
                    }
                } else if (BundleConstants.isPEPartner && BundleConstants.PEDSAOrder) {
                    onReleaseButtonClicked();
                } else {
                    onReleaseButtonClicked();
                }*/

                //Mith CX delay
                if (orderDetailsModel.getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("PARTIAL SERVICED") && isValidForEditing(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())) {
                    onReleaseButtonClicked();
                } else {
                    onOrderRelease();
                }


//                 onOrderRelease();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BundleConstants.isKIOSKOrder) {
                    Intent intent1 = new Intent(mActivity, KIOSK_Scanner_Activity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                } else {
                    Intent intent = new Intent(mActivity, VisitOrdersDisplayFragment_new.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

//                finish();
            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, HomeScreenActivity.class);
                startActivity(i);
            }
        });


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gpsTracker.isGPSon()) {
                    if (isNetworkAvailable(mActivity)) {
                        callOrderStatusChangeApi(7, "Start", "", "");
                    } else {
                        Toast.makeText(mActivity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    gpsTracker.showSettingsAlert();
                    Toast.makeText(mActivity, "Check Internet connection and gps settings", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
                alertDialog.setMessage("Do you want to confirm?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                try {
                                    if (isNetworkAvailable(mActivity)) {
                                        VenuPuntureUtils.ClearVenupumtureTempGlobalArry();
//                                        stopService(new Intent(getApplicationContext(), TrackerService.class));
                                        callOrderStatusChangeApi(3, "Arrive", "", "");
                                    } else {
                                        Toast.makeText(mActivity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                alertDialog.show();*/

                try {
                    if (isNetworkAvailable(mActivity)) {
                        VenuPuntureUtils.ClearVenupumtureTempGlobalArry();
//                                        stopService(new Intent(getApplicationContext(), TrackerService.class));
                        //fungible
//                        if (BundleConstants.callOTPFlag == 0 && !BundleConstants.isPEPartner || BundleConstants.PEDSAOrder) {
                        if (BundleConstants.callOTPFlag == 0 && !appPreferenceManager.isPEPartner() || appPreferenceManager.PEDSAOrder()) {
//                        if (BundleConstants.callOTPFlag == 0 && !Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                            callOrderStatusChangeApi(3, "Arrive", "", "");
                        } else {
                            if (!orderDetailsModel.getAllOrderdetails().get(0).isOTP()) {
                                callOrderStatusChangeApi(3, "Arrive", "", "");
                            } else {
//                                if (BundleConstants.isPEPartner || orderDetailsModel.getAllOrderdetails().get(0).isOTP()) {
                                if (appPreferenceManager.isPEPartner() || orderDetailsModel.getAllOrderdetails().get(0).isOTP()) {

                                    callAPIforStatusMark(orderDetailsModel);
                                    callAPIforOTP(orderDetailsModel);
                                }
                            }
                        }
                    } else {
                        Toast.makeText(mActivity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    //fungible
//                    if (BundleConstants.isPEPartner || BundleConstants.PEDSAOrder) {
                    if (appPreferenceManager.isPEPartner() || appPreferenceManager.PEDSAOrder()) {
                        //hardblocking
                        if (checkForTest(2)) {
                            if (checkBeneficiaryDtls()) {
                                String string = "You wont be able to modify the order after proceeding.Please verify all details before proceeding.\nAre you sure you want to proceed ?";
                                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);

                                View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.logout_bottomsheet, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));

                                TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
                                tv_text.setText(string);

                                Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
                                btn_yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bottomSheetDialog.dismiss();
                                        Intent intentOrderBooking = new Intent(mActivity, ScanBarcodeWoeActivity.class);
                                        intentOrderBooking.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
                                        startActivity(intentOrderBooking);
//                                    finish();
                                    }
                                });

                                Button btn_no = bottomSheet.findViewById(R.id.btn_no);
                                btn_no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bottomSheetDialog.dismiss();

                                    }
                                });

                                bottomSheetDialog.setContentView(bottomSheet);
                                bottomSheetDialog.setCancelable(false);
                                bottomSheetDialog.show();
                        /*AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
                        alertDialog.setMessage("You wont be able to modify the order after proceeding.Please verify all details before proceeding.\nAre you sure you want to proceed ?");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intentOrderBooking = new Intent(mActivity, ScanBarcodeWoeActivity.class);
                                        intentOrderBooking.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
                                        startActivity(intentOrderBooking);
                                        finish();
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();*/
                            }
                        }
                    } else {
                        //fungible
                        /*if (BundleConstants.companyOrderFlag) {
//                        if (orderDetailsModel.getAllOrderdetails().get(0).isPEPartner()) {
                            //To check ben details for DSA migration order
                            if (checkBeneficiaryDtls()) {
                                String string = "You wont be able to modify the order after proceeding.Please verify all details before proceeding.\nAre you sure you want to proceed ?";
                                final BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);

                                View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.logout_bottomsheet, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));

                                TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
                                tv_text.setText(string);

                                Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
                                btn_yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bottomSheetDialog2.dismiss();
                                        Intent intentOrderBooking = new Intent(mActivity, ScanBarcodeWoeActivity.class);
                                        intentOrderBooking.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
                                        startActivity(intentOrderBooking);
//                                        finish();
                                    }
                                });

                                Button btn_no = bottomSheet.findViewById(R.id.btn_no);
                                btn_no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bottomSheetDialog2.dismiss();

                                    }
                                });

                                bottomSheetDialog2.setContentView(bottomSheet);
                                bottomSheetDialog2.setCancelable(false);
                                bottomSheetDialog2.show();
//                                bottomsheetScanBarcode();
                            }
                        } else {*/
                        if (checkBeneficiaryDtls()) {
                            totalAmountPayable = 0;
                            setpayMentActivity();
                        }
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_floating_add_ben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlagADDEditBen = true;
                Intent intent = new Intent(mActivity, AddEditBenificaryActivity.class);
                intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
                intent.putExtra("IsAddBen", FlagADDEditBen);
                startActivityForResult(intent, BundleConstants.ADDEDITBENREQUESTCODE);

            }
        });

        recyle_OrderDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                try {
                    if (btn_Proceed.getVisibility() == View.VISIBLE) {
                        if (orderDetailsModel != null
                                && orderDetailsModel.getAllOrderdetails() != null
                                && orderDetailsModel.getAllOrderdetails().size() > 0
                                && orderDetailsModel.getAllOrderdetails().get(0).getBenMaster() != null
                                && orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().size() > 3) {

                            //fungible
                            //TODO hide add ben for TC PE DSA order
                            //fungible
//                            if (BundleConstants.isPEPartner && BundleConstants.PEDSAOrder) {
                            if (appPreferenceManager.isPEPartner() && appPreferenceManager.PEDSAOrder()) {
//                            if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                                btn_floating_add_ben.setVisibility(View.GONE);
//                            } else if (BundleConstants.isPEPartner) {
                            } else if (appPreferenceManager.isPEPartner()) {
                                btn_floating_add_ben.setVisibility(View.VISIBLE);
                            } else {
                                if (isValidForEditing(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())) {
                                    btn_floating_add_ben.setVisibility(View.GONE);
                                } else if (orderDetailsModel.getAllOrderdetails().get(0).isEditOrder()) {
                                    if (!recyle_OrderDetail.canScrollVertically(1)) {
                                        btn_floating_add_ben.setVisibility(View.VISIBLE);
                                    } else {
                                        btn_floating_add_ben.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    btn_floating_add_ben.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            //fungible
//                            if (BundleConstants.isPEPartner && BundleConstants.PEDSAOrder) {
                            if (appPreferenceManager.isPEPartner() && appPreferenceManager.PEDSAOrder()) {
//                            if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                                btn_floating_add_ben.setVisibility(View.GONE);
                            } else if (appPreferenceManager.isPEPartner()) {
                                btn_floating_add_ben.setVisibility(View.VISIBLE);
                            } else {
                                if (isValidForEditing(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())) {
                                    btn_floating_add_ben.setVisibility(View.GONE);
                                } else if (orderDetailsModel.getAllOrderdetails().get(0).isEditOrder()) {
                                    btn_floating_add_ben.setVisibility(View.VISIBLE);
                                } else {
                                    btn_floating_add_ben.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else {
//                        if (BundleConstants.isPEPartner && BundleConstants.PEDSAOrder) {
                        if (appPreferenceManager.isPEPartner() && appPreferenceManager.PEDSAOrder()) {
//                            if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                            btn_floating_add_ben.setVisibility(View.GONE);
                        } else if (appPreferenceManager.isPEPartner()) {
                            btn_floating_add_ben.setVisibility(View.GONE);
                        } else {
                            btn_floating_add_ben.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


    }

    private boolean checkForTest(int value) {
        if (value == 1 && !BundleConstants.PE_HARD_BLOCKING) {
            for (int i = 0; i < orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().size(); i++) {
                if (InputUtils.isNull(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestsCode())) {
                    btn_Proceed.setTextColor(getResources().getColor(R.color.gray));
                    btn_Proceed.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FF8B8888")));
                    btn_Proceed.setVisibility(View.VISIBLE);
                    return false;
                }
            }
        } else if (value == 2 && !BundleConstants.PE_HARD_BLOCKING) {
            for (int i = 0; i < orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().size(); i++) {
                if (InputUtils.isNull(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestsCode())) {
                    btn_Proceed.setTextColor(getResources().getColor(R.color.gray));
                    btn_Proceed.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FF8B8888")));
                    btn_Proceed.setVisibility(View.VISIBLE);
                    Toast.makeText(mActivity, ConstantsMessages.PLEASE_ADD_TEST, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    private void callAPIforStatusMark(OrderVisitDetailsModel orderDetailsModel) {
        if (cd.isConnectingToInternet()) {
            SendEventRequestModel snrm = new SendEventRequestModel();
            snrm.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
            snrm.setId(orderDetailsModel.getSlotId());
            snrm.setOrderno(orderDetailsModel.getVisitId());
            snrm.setRemarks("ARRIVED MARKED");
            snrm.setStatus(BundleConstants.EVENT_STATUS);

            PEAuthorizationController peac = new PEAuthorizationController(this);
            peac.sendEventArrived(snrm);

        } else {
            Toast.makeText(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void callAPIforOrderCancellationsRemarks(String s) {
        if (cd.isConnectingToInternet()) {
            OrderReleaseRemarksController orrC = new OrderReleaseRemarksController(this);
            orrC.getReasons(s);
        } else {
            Toast.makeText(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void onOrderRelease() {
        if (cd.isConnectingToInternet()) {
            OrderReleaseRemarksController orc = new OrderReleaseRemarksController(this);
            orc.getRemarks(BundleConstants.OrderReleaseOptionsID, 0);
        } else {
            globalclass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.plz_chk_internet));
        }
    }

    private void bottomsheetScanBarcode() {
        final BottomSheetDialog bottomSheetDialog1;
        String string = "You wont be able to modify the order after proceeding.Please verify all details before proceeding.\nAre you sure you want to proceed ?";
        bottomSheetDialog1 = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);

        View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.logout_bottomsheet, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));

        TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
        tv_text.setText(string);

        Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    bottomSheetDialog1.dismiss();
                    Intent intentOrderBooking = new Intent(mActivity, ScanBarcodeWoeActivity.class);
                    intentOrderBooking.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentOrderBooking.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
                    startActivity(intentOrderBooking);
                    bottomSheetDialog1.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button btn_no = bottomSheet.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog1.dismiss();

            }
        });

        bottomSheetDialog1.setContentView(bottomSheet);
        bottomSheetDialog1.setCancelable(false);
        bottomSheetDialog1.show();
    }


    private boolean checkBeneficiaryDtls() {
        for (int i = 0; i < orderDetailsModel.getAllOrderdetails().size(); i++) {
            for (int j = 0; j < orderDetailsModel.getAllOrderdetails().get(i).getBenMaster().size(); j++) {
                if (orderDetailsModel.getAllOrderdetails().get(i).getBenMaster().get(j).getName().trim().contains("Test_user") || orderDetailsModel.getAllOrderdetails().get(i).getBenMaster().get(j).getName().trim().contains("TEST_USER")) {
                    Toast.makeText(mActivity, "Kindly edit beneficiary name", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (orderDetailsModel.getAllOrderdetails().get(i).getBenMaster().get(j).getAge() == 150) {
                    Toast.makeText(mActivity, "Kindly edit beneficiary age", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (orderDetailsModel.getAllOrderdetails().get(i).getBenMaster().get(j).getGender().equalsIgnoreCase("Dummy")) {
                    Toast.makeText(mActivity, "Kindly edit beneficiary gender", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    private void callAPIforOTP(final OrderVisitDetailsModel orderDetailsModel) {
        SendOTPRequestModel model = new SendOTPRequestModel();
        model.setMobile(orderDetailsModel.getAllOrderdetails().get(0).getMobile());
        model.setOrderno(orderDetailsModel.getAllOrderdetails().get(0).getVisitId());
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<CommonResponseModel2> responseCall = apiInterface.CallSendOTPAPI(model);
        globalclass.showProgressDialog(mActivity, "Requesting for OTP. Please wait..");
        responseCall.enqueue(new Callback<CommonResponseModel2>() {
            @Override
            public void onResponse(Call<CommonResponseModel2> call, Response<CommonResponseModel2> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    CommonResponseModel2 responseModel = response.body();
                    if (!TextUtils.isEmpty(responseModel.getRES_ID()) && responseModel.getRES_ID().equalsIgnoreCase("RES0000")) {
//                        globalclass.showCustomToast(mActivity, "OTP send successfully to mobile number mapped to this order.");
                        ShowDialogToVerifyOTP(orderDetailsModel);
                    } else {
                        globalclass.showCustomToast(mActivity, "OTP Generation Failed.");
                    }
                } else {
                    globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<CommonResponseModel2> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
            }
        });
    }

    private void ShowDialogToVerifyOTP(final OrderVisitDetailsModel orderVisitDetailsModel) {

        CustomDialogforOTPValidation = new Dialog(mActivity);
        CustomDialogforOTPValidation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomDialogforOTPValidation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomDialogforOTPValidation.setContentView(R.layout.validate_otp_dialog);
        CustomDialogforOTPValidation.setCancelable(false);
        CustomDialogforOTPValidation.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RelativeLayout rel_main = (RelativeLayout) CustomDialogforOTPValidation.findViewById(R.id.rel_main);
        TextView tv_header = (TextView) CustomDialogforOTPValidation.findViewById(R.id.tv_header);
        ImageView img_btn_validateOTP = (ImageView) CustomDialogforOTPValidation.findViewById(R.id.img_btn_validateOTP);
        ImageView img_close = (ImageView) CustomDialogforOTPValidation.findViewById(R.id.img_close);
        final EditText edt_OTP = (EditText) CustomDialogforOTPValidation.findViewById(R.id.edt_OTP);

        tv_header.setText("Enter OTP send to mobile number linked with this order, to proceed with WOE.");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        int height = 0, width = 0;
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

        CustomDialogforOTPValidation.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogforOTPValidation.dismiss();
            }
        });


        img_btn_validateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strOTP = edt_OTP.getText().toString().trim();
                if (!InputUtils.isNull(strOTP) && strOTP.length() != 4) {
                    globalclass.showalert_OK("Please enter valid OTP. Length required : 4", mActivity);
                    edt_OTP.requestFocus();
                } else {
                    OrderPassRequestModel model = new OrderPassRequestModel();
                    model.setMobile(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                    model.setOTP(strOTP);
                    model.setVisitId(orderVisitDetailsModel.getAllOrderdetails().get(0).getVisitId());
//                    if (BundleConstants.isPEPartner || BundleConstants.PEDSAOrder) {
                    if (appPreferenceManager.isPEPartner() || appPreferenceManager.PEDSAOrder()) {
                        model.setOTPStatus(BundleConstants.PE_OTP_ARRIVED);
                    }
                    if (cd.isConnectingToInternet()) {
                        CallValidateOTPAPI(model);
                    } else {
                        globalclass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.plz_chk_internet));
                    }
                }

            }
        });

    }
        /*final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);
        final View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.bottomsheet_otp, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));
        RelativeLayout rel_main = (RelativeLayout) bottomSheet.findViewById(R.id.rel_main);
        TextView tv_header = (TextView) bottomSheet.findViewById(R.id.tv_header);
        ImageView img_btn_validateOTP = (ImageView) bottomSheet.findViewById(R.id.img_btn_validateOTP);
        ImageView img_close = (ImageView) bottomSheet.findViewById(R.id.img_close);
        final EditText edt_OTP = (EditText) bottomSheet.findViewById(R.id.edt_OTP);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tv_header.setText("Enter OTP send to mobile number linked with this order, to proceed with WOE.");

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });


        img_btn_validateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strOTP = edt_OTP.getText().toString().trim();
                if (!InputUtils.isNull(strOTP) && strOTP.length() != 4) {
                    globalclass.showalert_OK("Please enter valid OTP. Length required : 4", mActivity);
                    edt_OTP.requestFocus();
                } else {
                    OrderPassRequestModel model = new OrderPassRequestModel();
                    model.setMobile(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                    model.setOTP(strOTP);
                    model.setVisitId(orderVisitDetailsModel.getAllOrderdetails().get(0).getVisitId());
                    if (cd.isConnectingToInternet()) {
                        CallValidateOTPAPI(model, bottomSheetDialog);
                    } else {
                        globalclass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.plz_chk_internet));
                    }
                }
            }
        });
        if (!mActivity.isFinishing()) {
            bottomSheetDialog.show();
        }
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }*/

    private void CallValidateOTPAPI(OrderPassRequestModel model) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallValidateOTPAPI(model);
        globalclass.showProgressDialog(mActivity, "Validating OTP. Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    String strresponse = response.body();
                    if (!TextUtils.isEmpty(strresponse) && strresponse.toUpperCase().contains("SUCCESS")) {
                        CustomDialogforOTPValidation.dismiss();
                        Toast.makeText(mActivity, "OTP Validated Successfully.", Toast.LENGTH_SHORT).show();
//                        globalclass.showCustomToast(mActivity, "OTP Validated Successfully.");
                        callOrderStatusChangeApi(3, "Arrive", "", "");
                        BundleConstants.callOTPFlag = 1;

                    } else {
                        globalclass.showCustomToast(mActivity, "Invalid OTP.");
                    }
                } else if (response.code() == 401) {
                    globalclass.showCustomToast(mActivity, "Invalid OTP.");
                } else {
                    globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
            }
        });

    }

    private void setpayMentActivity() {

        final String OrderMode = !StringUtils.isNull(orderDetailsModel.getAllOrderdetails().get(0).getOrderMode()) ? orderDetailsModel.getAllOrderdetails().get(0).getOrderMode() : "";

        for (OrderDetailsModel orderDetailsModel : orderDetailsModel.getAllOrderdetails()) {
            totalAmountPayable = totalAmountPayable + orderDetailsModel.getAmountPayable();
        }

        if (totalAmountPayable == 0) {
            String string = "You wont be able to modify the order after proceeding.Please verify all details before proceeding.\nAre you sure you want to proceed ?";

            final BottomSheetDialog bottomSheetDialog3 = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);

            View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.logout_bottomsheet, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));

            TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
            tv_text.setText(string);

            Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog3.dismiss();
                    Intent intentOrderBooking = new Intent(mActivity, ScanBarcodeWoeActivity.class);
                    intentOrderBooking.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
                    startActivity(intentOrderBooking);
//                    finish();
                }
            });

            Button btn_no = bottomSheet.findViewById(R.id.btn_no);
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog3.dismiss();

                }
            });

            bottomSheetDialog3.setContentView(bottomSheet);
            bottomSheetDialog3.setCancelable(false);
            bottomSheetDialog3.show();

//            bottomsheetScanBarcode();

           /* AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
            alertDialog.setMessage("You wont be able to modify the order after proceeding.Please verify all details before proceeding.\nAre you sure you want to proceed ?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intentOrderBooking = new Intent(mActivity, ScanBarcodeWoeActivity.class);
                            intentOrderBooking.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
                            startActivity(intentOrderBooking);
                            finish();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();*/
        } else {

            String string = "You wont be able to modify the order after proceeding.Please verify all details before proceeding.\nAre you sure you want to proceed ?";

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);

            View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.logout_bottomsheet, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));

            TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
            tv_text.setText(string);

            Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    if (orderDetailsModel.getAllOrderdetails().get(0).isDigital()) {
                        GoingToPaymentActivity(0);
                                               /* PaymentMode = 2;
                                                Intent intentPayments = new Intent(mActivity, PaymentsActivity.class);
                                                Logger.error("tejastotalAmountPayableatsending " + totalAmountPayable);
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT, totalAmountPayable + "");
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 2);
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO, orderDetailsModel.getVisitId());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_NAME, orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getName());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_ADDRESS, orderDetailsModel.getAllOrderdetails().get(0).getAddress());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_PIN, orderDetailsModel.getAllOrderdetails().get(0).getPincode());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_MOBILE, orderDetailsModel.getAllOrderdetails().get(0).getMobile());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_EMAIL, orderDetailsModel.getAllOrderdetails().get(0).getEmail());
                                                startActivityForResult(intentPayments, BundleConstants.PAYMENTS_START);
                                          */
                    } else {
                        if (OrderMode.equalsIgnoreCase("LTD-BLD") || OrderMode.equalsIgnoreCase("LTD-NBLD")) {
//                                                    paymentItems = new String[]{"Cash"};
                            GoingToPaymentActivity(1);
                        } else {
//                                                    paymentItems = new String[]{"Cash", "Digital"};
                            GoingToPaymentActivity(2);
                        }

                                             /*   AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                                builder.setTitle("Choose payment mode")
                                                        .setItems(paymentItems, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if (paymentItems[which].equals("Cash")) {

                                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
                                                                    builder1.setMessage("Confirm amount received ₹ " + totalAmountPayable + "")
                                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    *//*PaymentMode = 1;
                                                                    // TODO code to Add again the Venupunture images stored in global array in MainbookingRequestModel
                                                                    OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_cash");
                                                                    if (cd.isConnectingToInternet()) {
                                                                        CallWorkOrderEntryAPI(orderBookingRequestModel);
                                                                    } else {
                                                                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                                                    }*//*

                                                                                    BundleConstants.addPaymentFlag = 1;

                                                                                    Intent intentOrderBooking = new Intent(mActivity, ScanBarcodeWoeActivity.class);
                                                                                    intentOrderBooking.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
                                                                                    startActivity(intentOrderBooking);
                                                                                    finish();
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
                                                                    BundleConstants.addPaymentFlag = 0;
                                                                    PaymentMode = 2;
                                                                    Intent intentPayments = new Intent(mActivity, PaymentsActivity.class);
                                                                    Logger.error("tejastotalAmountPayableatsending " + totalAmountPayable);
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT, totalAmountPayable + "");
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 2);
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO, orderDetailsModel.getVisitId());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_NAME, orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getName());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_ADDRESS, orderDetailsModel.getAllOrderdetails().get(0).getAddress());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_PIN, orderDetailsModel.getAllOrderdetails().get(0).getPincode());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_MOBILE, orderDetailsModel.getAllOrderdetails().get(0).getMobile());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_EMAIL, orderDetailsModel.getAllOrderdetails().get(0).getEmail());
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
                                                        }).show();*/
                                /*            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();*/

                    }

                }

            });
            Button btn_no = bottomSheet.findViewById(R.id.btn_no);
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();

                }
            });

            bottomSheetDialog.setContentView(bottomSheet);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();


/*
            AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
            alertDialog.setMessage("You wont be able to modify the order after proceeding.Please verify all details before proceeding.\nAre you sure you want to proceed ?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();


                   *//*         AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            builder.setMessage("Amount payable ₹ " + totalAmountPayable + "/-")
                                    .setPositiveButton("Collect", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {*//*

//                            if (!orderDetailsModel.getAllOrderdetails().get(0).isDigital()) {
                            if (orderDetailsModel.getAllOrderdetails().get(0).isDigital()) {
                                GoingToPaymentActivity(0);
                                               *//* PaymentMode = 2;
                                                Intent intentPayments = new Intent(mActivity, PaymentsActivity.class);
                                                Logger.error("tejastotalAmountPayableatsending " + totalAmountPayable);
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT, totalAmountPayable + "");
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 2);
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO, orderDetailsModel.getVisitId());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_NAME, orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getName());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_ADDRESS, orderDetailsModel.getAllOrderdetails().get(0).getAddress());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_PIN, orderDetailsModel.getAllOrderdetails().get(0).getPincode());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_MOBILE, orderDetailsModel.getAllOrderdetails().get(0).getMobile());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_EMAIL, orderDetailsModel.getAllOrderdetails().get(0).getEmail());
                                                startActivityForResult(intentPayments, BundleConstants.PAYMENTS_START);
                                          *//*
                            } else {
                                if (OrderMode.equalsIgnoreCase("LTD-BLD") || OrderMode.equalsIgnoreCase("LTD-NBLD")) {
//                                                    paymentItems = new String[]{"Cash"};
                                    GoingToPaymentActivity(1);
                                } else {
//                                                    paymentItems = new String[]{"Cash", "Digital"};
                                    GoingToPaymentActivity(2);
                                }

                                             *//*   AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                                builder.setTitle("Choose payment mode")
                                                        .setItems(paymentItems, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if (paymentItems[which].equals("Cash")) {

                                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
                                                                    builder1.setMessage("Confirm amount received ₹ " + totalAmountPayable + "")
                                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    *//**//*PaymentMode = 1;
                                                                    // TODO code to Add again the Venupunture images stored in global array in MainbookingRequestModel
                                                                    OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_cash");
                                                                    if (cd.isConnectingToInternet()) {
                                                                        CallWorkOrderEntryAPI(orderBookingRequestModel);
                                                                    } else {
                                                                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                                                    }*//**//*

                                                                                    BundleConstants.addPaymentFlag = 1;

                                                                                    Intent intentOrderBooking = new Intent(mActivity, ScanBarcodeWoeActivity.class);
                                                                                    intentOrderBooking.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
                                                                                    startActivity(intentOrderBooking);
                                                                                    finish();
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
                                                                    BundleConstants.addPaymentFlag = 0;
                                                                    PaymentMode = 2;
                                                                    Intent intentPayments = new Intent(mActivity, PaymentsActivity.class);
                                                                    Logger.error("tejastotalAmountPayableatsending " + totalAmountPayable);
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT, totalAmountPayable + "");
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 2);
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO, orderDetailsModel.getVisitId());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_NAME, orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getName());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_ADDRESS, orderDetailsModel.getAllOrderdetails().get(0).getAddress());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_PIN, orderDetailsModel.getAllOrderdetails().get(0).getPincode());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_MOBILE, orderDetailsModel.getAllOrderdetails().get(0).getMobile());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_EMAIL, orderDetailsModel.getAllOrderdetails().get(0).getEmail());
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
                                                        }).show();*//*
             *//*            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();*//*

                            }

                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();*/

        }

    }

    private void GoingToPaymentActivity(int flag) {
        Intent intentPayments = new Intent(mActivity, PaymentsActivity.class);

        intentPayments.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
        intentPayments.putExtra(BundleConstants.PAYMENTS_OPTION_FLAG, flag);
        intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT, totalAmountPayable + "");
        intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 2);
        intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO, orderDetailsModel.getVisitId());
        intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_NAME, orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getName());
        intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_ADDRESS, orderDetailsModel.getAllOrderdetails().get(0).getAddress());
        intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_PIN, orderDetailsModel.getAllOrderdetails().get(0).getPincode());
        intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_MOBILE, orderDetailsModel.getAllOrderdetails().get(0).getMobile());
        intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_EMAIL, orderDetailsModel.getAllOrderdetails().get(0).getEmail());
        startActivityForResult(intentPayments, BundleConstants.PAYMENTS_START);
    }

    private void CallOrderDetailAPI(final String Status) {
        if (BundleConstants.isKIOSKOrder) {
            try {
                GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
                Call<FetchOrderDetailsResponseModel> fetchOrderDetailsResponseModelCall = getAPIInterface.getOrderAllVisitDetails(appPreferenceManager.getLoginResponseModel().getUserID(), strOrderNo);
                globalclass.showProgressDialog(mActivity, mActivity.getResources().getString(R.string.fetchingOrders), false);
                fetchOrderDetailsResponseModelCall.enqueue(new Callback<FetchOrderDetailsResponseModel>() {
                    @Override
                    public void onResponse(Call<FetchOrderDetailsResponseModel> call, Response<FetchOrderDetailsResponseModel> response) {
                        globalclass.hideProgressDialog(mActivity);

                        FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel = response.body();
                        if (fetchOrderDetailsResponseModel != null && fetchOrderDetailsResponseModel.getOrderVisitDetails() != null && fetchOrderDetailsResponseModel.getOrderVisitDetails().size() > 0) {
                            for (OrderVisitDetailsModel orderVisitDetailsModel :
                                    fetchOrderDetailsResponseModel.getOrderVisitDetails()) {
                                if (orderVisitDetailsModel.getVisitId().equalsIgnoreCase(strOrderNo)) {
                                    if (orderVisitDetailsModel.getAllOrderdetails() != null && orderVisitDetailsModel.getAllOrderdetails().size() > 0) {
                                        for (OrderDetailsModel orderDetailsModel :
                                                orderVisitDetailsModel.getAllOrderdetails()) {
                                            orderDetailsModel.setVisitId(orderVisitDetailsModel.getVisitId());
                                            orderDetailsModel.setResponse(orderVisitDetailsModel.getResponse());
                                            orderDetailsModel.setSlot(orderVisitDetailsModel.getSlot());
                                            orderDetailsModel.setSlotId(orderVisitDetailsModel.getSlotId());
                                            orderDetailsModel.setAmountPayable(orderDetailsModel.getAmountDue());
                                            orderDetailsModel.setEstIncome(orderVisitDetailsModel.getEstIncome());
                                            orderDetailsModel.setAppointmentDate(orderVisitDetailsModel.getAppointmentDate());
                                            orderDetailsModel.setBtechName(orderVisitDetailsModel.getBtechName());
                                            orderDetailsModel.setAddBen(orderDetailsModel.isEditOrder());
                                            if (orderDetailsModel.getBenMaster() != null && orderDetailsModel.getBenMaster().size() > 0) {
                                                for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                                                        orderDetailsModel.getBenMaster()) {
                                                    beneficiaryDetailsModel.setOrderNo(orderDetailsModel.getOrderNo());
                                                    beneficiaryDetailsModel.setTests(beneficiaryDetailsModel.getTestsCode());
                                                    for (int i = 0; i < beneficiaryDetailsModel.getSampleType().size(); i++) {
                                                        beneficiaryDetailsModel.getSampleType().get(i).setBenId(beneficiaryDetailsModel.getBenId());
                                                    }
                                                }
                                            }
                                        }
                                        orderDetailsModel = null;
                                        orderDetailsModel = orderVisitDetailsModel;
                                        break;
                                    }
                                }
                            }
                        }
                        initData(Status);
                    }

                    @Override
                    public void onFailure(Call<FetchOrderDetailsResponseModel> call, Throwable t) {
                        globalclass.hideProgressDialog(mActivity);
                        globalclass.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                    }
                });
            } catch (Exception e) {
                globalclass.hideProgressDialog(mActivity);
                e.printStackTrace();
                globalclass.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
            }
        } else {
            /*if (!Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName()) && !Global.callAPIEditOrder) {
                if (appPreferenceManager.getfetchOrderDetailsResponseModel() != null && appPreferenceManager.getfetchOrderDetailsResponseModel().getOrderVisitDetails() != null && appPreferenceManager.getfetchOrderDetailsResponseModel().getOrderVisitDetails().size() > 0) {
                    for (OrderVisitDetailsModel orderVisitDetailsModel :
                            appPreferenceManager.getfetchOrderDetailsResponseModel().getOrderVisitDetails()) {
                        if (orderVisitDetailsModel.getVisitId().equalsIgnoreCase(strOrderNo)) {
                            if (orderVisitDetailsModel.getAllOrderdetails() != null && orderVisitDetailsModel.getAllOrderdetails().size() > 0) {
                                for (OrderDetailsModel orderDetailsModel :
                                        orderVisitDetailsModel.getAllOrderdetails()) {
                                    orderDetailsModel.setVisitId(orderVisitDetailsModel.getVisitId());
                                    orderDetailsModel.setResponse(orderVisitDetailsModel.getResponse());
                                    orderDetailsModel.setSlot(orderVisitDetailsModel.getSlot());
                                    orderDetailsModel.setSlotId(orderVisitDetailsModel.getSlotId());
                                    orderDetailsModel.setAmountPayable(orderDetailsModel.getAmountDue());
                                    orderDetailsModel.setEstIncome(orderVisitDetailsModel.getEstIncome());
                                    orderDetailsModel.setAppointmentDate(orderVisitDetailsModel.getAppointmentDate());
                                    orderDetailsModel.setBtechName(orderVisitDetailsModel.getBtechName());
                                    orderDetailsModel.setAddBen(orderDetailsModel.isEditOrder());
                                    if (orderDetailsModel.getBenMaster() != null && orderDetailsModel.getBenMaster().size() > 0) {
                                        for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                                                orderDetailsModel.getBenMaster()) {
                                            beneficiaryDetailsModel.setOrderNo(orderDetailsModel.getOrderNo());
                                            beneficiaryDetailsModel.setTests(beneficiaryDetailsModel.getTestsCode());
                                            for (int i = 0; i < beneficiaryDetailsModel.getSampleType().size(); i++) {
                                                beneficiaryDetailsModel.getSampleType().get(i).setBenId(beneficiaryDetailsModel.getBenId());
                                            }
                                        }
                                    }
                                }
                                orderDetailsModel = null;
                                orderDetailsModel = orderVisitDetailsModel;
                                break;
                            }
                        }
                    }
                }
            } else if(!Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName()) && Global.callAPIEditOrder) {*/
            try {
                GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
//                    Call<FetchOrderDetailsResponseModel> fetchOrderDetailsResponseModelCall = getAPIInterface.getAllVisitDetails(appPreferenceManager.getLoginResponseModel().getUserID());
                Call<FetchOrderDetailsResponseModel> fetchOrderDetailsResponseModelCall = getAPIInterface.getOrderDetail(BundleConstants.setSelectedOrder.trim());
                globalclass.showProgressDialog(mActivity, mActivity.getResources().getString(R.string.fetchingOrders), false);
                fetchOrderDetailsResponseModelCall.enqueue(new Callback<FetchOrderDetailsResponseModel>() {
                    @Override
                    public void onResponse(Call<FetchOrderDetailsResponseModel> call, Response<FetchOrderDetailsResponseModel> response) {
                        globalclass.hideProgressDialog(mActivity);
                        FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel = response.body();
                        if (fetchOrderDetailsResponseModel != null && fetchOrderDetailsResponseModel.getOrderVisitDetails() != null && fetchOrderDetailsResponseModel.getOrderVisitDetails().size() > 0) {
                            if (BundleConstants.PE_HARD_BLOCKING) {
                                btn_Proceed.setEnabled(true);
                                btn_Proceed.setTextColor(getResources().getColor(R.color.bg_new_color));
                                btn_Proceed.setVisibility(View.VISIBLE);
                            }
//                            BundleConstants.isPEPartner = fetchOrderDetailsResponseModel.getOrderVisitDetails().get(0).getAllOrderdetails().get(0).isPEPartner();
                            appPreferenceManager.setPE_Partner(fetchOrderDetailsResponseModel.getOrderVisitDetails().get(0).getAllOrderdetails().get(0).isPEPartner());
//                            BundleConstants.PEDSAOrder = fetchOrderDetailsResponseModel.getOrderVisitDetails().get(0).getAllOrderdetails().get(0).isPEDSAOrder();
                            appPreferenceManager.setPE_DSA(fetchOrderDetailsResponseModel.getOrderVisitDetails().get(0).getAllOrderdetails().get(0).isPEDSAOrder());
                            for (OrderVisitDetailsModel orderVisitDetailsModel :
                                    fetchOrderDetailsResponseModel.getOrderVisitDetails()) {
                                if (orderVisitDetailsModel.getVisitId().equalsIgnoreCase(strOrderNo)) {
                                    if (orderVisitDetailsModel.getAllOrderdetails() != null && orderVisitDetailsModel.getAllOrderdetails().size() > 0) {
                                        for (OrderDetailsModel orderDetailsModel :
                                                orderVisitDetailsModel.getAllOrderdetails()) {
                                            orderDetailsModel.setVisitId(orderVisitDetailsModel.getVisitId());
                                            orderDetailsModel.setResponse(orderVisitDetailsModel.getResponse());
                                            orderDetailsModel.setSlot(orderVisitDetailsModel.getSlot());
                                            orderDetailsModel.setSlotId(orderVisitDetailsModel.getSlotId());
                                            orderDetailsModel.setAmountPayable(orderDetailsModel.getAmountDue());
                                            orderDetailsModel.setEstIncome(orderVisitDetailsModel.getEstIncome());
                                            orderDetailsModel.setAppointmentDate(orderVisitDetailsModel.getAppointmentDate());
                                            orderDetailsModel.setBtechName(orderVisitDetailsModel.getBtechName());
                                            orderDetailsModel.setAddBen(orderDetailsModel.isEditOrder());
                                            orderDetailsModel.setEditME(orderDetailsModel.isEditME());
                                            if (orderDetailsModel.getBenMaster() != null && orderDetailsModel.getBenMaster().size() > 0) {
                                                for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                                                        orderDetailsModel.getBenMaster()) {
                                                    beneficiaryDetailsModel.setOrderNo(orderDetailsModel.getOrderNo());
                                                    beneficiaryDetailsModel.setTests(beneficiaryDetailsModel.getTestsCode());
                                                    for (int i = 0; i < beneficiaryDetailsModel.getSampleType().size(); i++) {
                                                        beneficiaryDetailsModel.getSampleType().get(i).setBenId(beneficiaryDetailsModel.getBenId());
                                                    }
                                                }
                                            }
                                        }
                                        orderDetailsModel = null;
                                        orderDetailsModel = orderVisitDetailsModel;
                                        break;
                                    }
                                }
                            }
                        }
                        initData(Status);
                    }

                    @Override
                    public void onFailure(Call<FetchOrderDetailsResponseModel> call, Throwable t) {
                        globalclass.hideProgressDialog(mActivity);
                        globalclass.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                    }
                });
            } catch (Exception e) {
                globalclass.hideProgressDialog(mActivity);
                e.printStackTrace();
                globalclass.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
            }
           /* }else if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName()) && InputUtils.CheckEqualIgnoreCase(BundleConstants.B2BLogin, appPreferenceManager.getLoginResponseModel().getB2bLogin())) {
                if (appPreferenceManager.getfetchOrderDetailsResponseModel() != null && appPreferenceManager.getfetchOrderDetailsResponseModel().getOrderVisitDetails() != null && appPreferenceManager.getfetchOrderDetailsResponseModel().getOrderVisitDetails().size() > 0) {
                    for (OrderVisitDetailsModel orderVisitDetailsModel :
                            appPreferenceManager.getfetchOrderDetailsResponseModel().getOrderVisitDetails()) {
                        if (orderVisitDetailsModel.getVisitId().equalsIgnoreCase(strOrderNo)) {
                            if (orderVisitDetailsModel.getAllOrderdetails() != null && orderVisitDetailsModel.getAllOrderdetails().size() > 0) {
                                for (OrderDetailsModel orderDetailsModel :
                                        orderVisitDetailsModel.getAllOrderdetails()) {
                                    orderDetailsModel.setVisitId(orderVisitDetailsModel.getVisitId());
                                    orderDetailsModel.setResponse(orderVisitDetailsModel.getResponse());
                                    orderDetailsModel.setSlot(orderVisitDetailsModel.getSlot());
                                    orderDetailsModel.setSlotId(orderVisitDetailsModel.getSlotId());
                                    orderDetailsModel.setAmountPayable(orderDetailsModel.getAmountDue());
                                    orderDetailsModel.setEstIncome(orderVisitDetailsModel.getEstIncome());
                                    orderDetailsModel.setAppointmentDate(orderVisitDetailsModel.getAppointmentDate());
                                    orderDetailsModel.setBtechName(orderVisitDetailsModel.getBtechName());
                                    orderDetailsModel.setAddBen(orderDetailsModel.isEditOrder());
                                    if (orderDetailsModel.getBenMaster() != null && orderDetailsModel.getBenMaster().size() > 0) {
                                        for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                                                orderDetailsModel.getBenMaster()) {
                                            beneficiaryDetailsModel.setOrderNo(orderDetailsModel.getOrderNo());
                                            beneficiaryDetailsModel.setTests(beneficiaryDetailsModel.getTestsCode());
                                            for (int i = 0; i < beneficiaryDetailsModel.getSampleType().size(); i++) {
                                                beneficiaryDetailsModel.getSampleType().get(i).setBenId(beneficiaryDetailsModel.getBenId());
                                            }
                                        }
                                    }
                                }
                                orderDetailsModel = null;
                                orderDetailsModel = orderVisitDetailsModel;
                                break;
                            }
                        }
                    }
                }
            }*/
            //   initData(Status);
        }
        //TODO clevertao event-----------------------------
       /* if (orderDetailsModel != null) {
            setArriveOrderCleverTapEvent(orderDetailsModel);
        }*/
    }

    private void setArriveOrderCleverTapEvent(OrderVisitDetailsModel orderDetailsModel) {
        HashMap<String, Object> arriveOrderCleverTapEvent = new HashMap<>();
        arriveOrderCleverTapEvent.put("Order ID", orderDetailsModel.getVisitId());
        arriveOrderCleverTapEvent.put("Phlebo Phone No", appPreferenceManager.getLoginResponseModel().getMobile());
        arriveOrderCleverTapEvent.put("Order date and slot time", orderDetailsModel.getAppointmentDate());
        arriveOrderCleverTapEvent.put("slot interval", orderDetailsModel.getSlot());
        arriveOrderCleverTapEvent.put("timestamp of event trigger", Global.getCurrentDateandTime());
        arriveOrderCleverTapEvent.put("order status", orderDetailsModel.getAllOrderdetails().get(0).getStatus());
        Constants.clevertapDefaultInstance.pushEvent("order_arrive_order_slot", arriveOrderCleverTapEvent);
    }
    //TODO clevertao event-----------------------------

    private void setRefreshActivity() {
        Intent intentNavigate = new Intent(mActivity, StartAndArriveActivity.class);
        intentNavigate.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
        mActivity.startActivity(intentNavigate);
        mActivity.finish();
    }

    private void initData(String status) {
        if (orderDetailsModel != null
                && orderDetailsModel.getAllOrderdetails() != null
                && orderDetailsModel.getAllOrderdetails().size() > 0
                && orderDetailsModel.getAllOrderdetails().get(0).getBenMaster() != null
                && orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().size() > 0) {
            btn_start.setEnabled(true);
            if (orderDetailsModel.getAllOrderdetails().get(0).getAmountDue() == 0) {
                customSwipeButton2.setVisibility(View.GONE);
                if (!BundleConstants.isKIOSKOrder) {
                    if (isValidForEditing(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())) {
                        customSwipeButton2.setVisibility(View.VISIBLE);
                    } else {
                        //fungible
//                        if ((BundleConstants.isPEPartner && BundleConstants.PEDSAOrder) || BundleConstants.isPEPartner) {
                        if ((appPreferenceManager.isPEPartner() && appPreferenceManager.PEDSAOrder()) || appPreferenceManager.isPEPartner()) {
                            customSwipeButton2.setVisibility(View.VISIBLE);
                        } else {
                            customSwipeButton2.setVisibility(View.GONE);
                        }
                    }
                } else {
                    customSwipeButton2.setVisibility(View.GONE);
                }

                if (InputUtils.isNull(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())) {
                    txt_amount.setText(mActivity.getResources().getString(R.string.rupee_symbol) + " " + "0" + "/-");
                } else {
                    txt_amount.setText("Paid");
                }
            } else {
                if (BundleConstants.isKIOSKOrder) {
                    customSwipeButton2.setVisibility(View.GONE);
                } else {
                    customSwipeButton2.setVisibility(View.VISIBLE);
                }
                txt_amount.setText(mActivity.getResources().getString(R.string.rupee_symbol) + " " + orderDetailsModel.getAllOrderdetails().get(0).getAmountDue() + "/-");
            }
            //TO show Time and note
            String orderTime = orderDetailsModel.getAllOrderdetails().get(0).getAppointmentDate() + " " + DateUtil.Req_Date_Req(orderDetailsModel.getAllOrderdetails().get(0).getSlot(), "hh:mm a", "HH:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            boolean toShowRemainingTime = false;
            long minutes = 0;
            try {
                Date slotDate = dateFormat.parse(orderTime);
                Date currentDate = new Date();
                if (currentDate.getTime() < slotDate.getTime()) {
                    long diff = slotDate.getTime() - currentDate.getTime();
                    long seconds = diff / 1000;
                    minutes = seconds / 60;
                    if (minutes <= 30) {
                        tv_note_res.setVisibility(View.VISIBLE);
                        toShowRemainingTime = true;
                    } else if (minutes <= 60) {
                        tv_note_res.setVisibility(View.GONE);
                        toShowRemainingTime = true;
                    } else {
                        tv_note_res.setVisibility(View.GONE);
                        toShowRemainingTime = false;
                    }
                } else {
                    tv_note_res.setVisibility(View.GONE);
                    toShowRemainingTime = false;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            //^^^^TO show Time and note^^^^

            txtNoRecord.setVisibility(View.GONE);
            recyle_OrderDetail.setVisibility(View.VISIBLE);

            //Sushil
            startArriveOrderDetailsAdapter = new StartArriveOrderDetailsAdapter(mActivity, orderDetailsModel, orderDetailsModel.getAllOrderdetails().get(0).getBenMaster(), status, this, toShowRemainingTime, minutes);
            startArriveOrderDetailsAdapter.setOnItemClickListener(new StartArriveOrderDetailsAdapter.OnClickListeners() {
                @Override
                public void onEditBenClicked(OrderVisitDetailsModel orderVisitDetailsModel, BeneficiaryDetailsModel SelectedbeneficiaryDetailsModel, int position, String str_benProduct) {
                    FlagADDEditBen = false;
                    PSelected_position = position;

                    Intent intent = new Intent(mActivity, AddEditBenificaryActivity.class);
                    intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
                    intent.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, SelectedbeneficiaryDetailsModel);
                    intent.putExtra("IsAddBen", FlagADDEditBen);
                    intent.putExtra("SelectedBenPosition", PSelected_position);
                    intent.putExtra(BundleConstants.BENPRODUCT, str_benProduct);
                    startActivityForResult(intent, BundleConstants.ADDEDITBENREQUESTCODE);
                }

                @Override
                public void onDeleteBenClicked(OrderVisitDetailsModel orderVisitDetailsModel, BeneficiaryDetailsModel SelectedbeneficiaryDetailsModel) {
                    OnDeleteBenClicked(orderVisitDetailsModel, SelectedbeneficiaryDetailsModel);
                }

                @Override
                public void onRefresh() {

                }

                @Override
                public void onShowTestDetailsnClicked(String benId) {
                    ViewTestData(benId);
                }

                @Override
                public void onCallCustomer() {
                    if (isNetworkAvailable(mActivity) && cd.isConnectingToInternet()) {
                        if (!InputUtils.isNull(orderDetailsModel.getAllOrderdetails().get(0).getPhone())) {
                            openTwoContactNoDialog(orderDetailsModel);
                        } else {
                            CallPatchRequestAPI(orderDetailsModel, orderDetailsModel.getAllOrderdetails().get(0).getMobile());
                            CallgetDispositionApi(orderDetailsModel);
                        }
                    } else {
                        TastyToast.makeText(mActivity, CheckInternetConnectionMsg, TastyToast.LENGTH_LONG, TastyToast.INFO);
                    }

                    /*if (isNetworkAvailable(mActivity)) {
                        CallgetDispositionApi(orderDetailsModel);
                    } else {
                        TastyToast.makeText(mActivity, CheckInternetConnectionMsg, TastyToast.LENGTH_LONG, TastyToast.INFO);
                    }

                    if (cd.isConnectingToInternet()) {
                        CallPatchRequestAPI(orderDetailsModel, orderDetailsModel.getAllOrderdetails().get(0).getMobile());
                    } else {
                        Toast.makeText(mActivity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                    }*/

                }
            });

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
            recyle_OrderDetail.setLayoutManager(mLayoutManager);
            recyle_OrderDetail.setAdapter(startArriveOrderDetailsAdapter);

//            displayDelayTimer(orderDetailsModel);

        } else {
            btn_start.setEnabled(false);
            recyle_OrderDetail.setVisibility(View.GONE);
            txtNoRecord.setVisibility(View.VISIBLE);
        }

    }

    private void openTwoContactNoDialog(OrderVisitDetailsModel orderDetailsModel) {
        Dialog dialog = new Dialog(mActivity);
        dialog.setContentView(R.layout.dialog_twocontacts);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tv_defaultphone, tv_optionalMobile;
        tv_defaultphone = dialog.findViewById(R.id.tv_defaultMobile);
        tv_optionalMobile = dialog.findViewById(R.id.tv_optionalMobile);

        tv_defaultphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CallPatchRequestAPI(orderDetailsModel, orderDetailsModel.getAllOrderdetails().get(0).getMobile());
                CallgetDispositionApi(orderDetailsModel);

            }
        });
        tv_optionalMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CallPatchRequestAPI(orderDetailsModel, orderDetailsModel.getAllOrderdetails().get(0).getPhone());
                CallgetDispositionApi(orderDetailsModel);
            }
        });

        dialog.show();
    }

    private void CallPatchRequestAPI(OrderVisitDetailsModel orderVisitDetailsModels, String str_SelectedMobileNumber) {
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallpatchRequestAPI(appPreferenceManager.getLoginResponseModel().getUserID(), str_SelectedMobileNumber, orderVisitDetailsModels.getVisitId());
        globalclass.showProgressDialog(mActivity, mActivity.getResources().getString(R.string.loading));
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        final String MaskedPhoneNumber = response.body();
                        TedPermission.with(mActivity)
                                .setPermissions(Manifest.permission.CALL_PHONE)
                                .setRationaleMessage("We need permission to make call from your device.")
                                .setRationaleConfirmText("OK")
                                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > Permission > Telephone")
                                .setPermissionListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted() {
                                        if (!StringUtils.isNull(MaskedPhoneNumber)) {
                                            Intent intent = new Intent(Intent.ACTION_CALL);
                                            intent.setData(Uri.parse("tel:" + MaskedPhoneNumber.replace("\"", "")));
                                            mActivity.startActivity(intent);
                                        } else {
                                            globalclass.showCustomToast(mActivity, "Invalid number");
                                        }
                                    }

                                    @Override
                                    public void onPermissionDenied(List<String> deniedPermissions) {
                                        Toast.makeText(mActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }).check();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);

            }
        });
    }

    private void CallgetDispositionApi(final OrderVisitDetailsModel orderDet) {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<DispositionDataModel> responseCall = apiInterface.CallgetDispositionApi();
        responseCall.enqueue(new Callback<DispositionDataModel>() {
            @Override
            public void onResponse(Call<DispositionDataModel> call, retrofit2.Response<DispositionDataModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DispositionDataModel dispositionDataModel = response.body();
                    if (dispositionDataModel != null) {
                        MessageLogger.PrintMsg("");
                        if (dispositionDataModel.getAllDisp() != null) {
                            if (dispositionDataModel.getAllDisp().size() != 0) {
                                CallDespositionDialog(orderDet, dispositionDataModel.getAllDisp());
                            }
                        }
                    }
                } else {
                    globalclass.showcenterCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<DispositionDataModel> call, Throwable t) {
                globalclass.showcenterCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private void CallDespositionDialog(final OrderVisitDetailsModel orderVisitDetailsModel, ArrayList<DispositionDetailsModel> allDisp) {
        dialog_ready = new Dialog(mActivity);
        dialog_ready.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog_ready.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_ready.setContentView(R.layout.dialog_desposition);
//        dialog_ready.setCanceledOnTouchOutside(false);
        dialog_ready.setCancelable(false);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
//        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.60);
        dialog_ready.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (!mActivity.isFinishing()) {
            dialog_ready.show();
        }

        ImageView img_cnc = (ImageView) dialog_ready.findViewById(R.id.img_cnc);
        img_cnc.setVisibility(View.GONE);
        img_cnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_ready.dismiss();
            }
        });

        TextView txt_odn = (TextView) dialog_ready.findViewById(R.id.txt_odn);
        txt_odn.setText("" + orderVisitDetailsModel.getVisitId());

        final EditText edt_desprem = (EditText) dialog_ready.findViewById(R.id.edt_desprem);
        final LinearLayout ll_rem = (LinearLayout) dialog_ready.findViewById(R.id.ll_rem);
        final LinearLayout ll_spnrem = (LinearLayout) dialog_ready.findViewById(R.id.ll_spnrem);
        final Spinner spn_rem = (Spinner) dialog_ready.findViewById(R.id.spn_rem);

        remarks_notc_arr = new ArrayList<>();
        remarks_notc_arr.add("Select");
        remarks_notc_arr.add("Ringing / No Response");
        remarks_notc_arr.add("Busy");
        remarks_notc_arr.add("Invalid Number");
        remarks_notc_arr.add("Number Does Not Exist");
        remarks_notc_arr.add("Switch off / Not reachable");

        ArrayAdapter<String> spnrnotconnectedremarks = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, remarks_notc_arr);
        spnrnotconnectedremarks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_rem.setAdapter(spnrnotconnectedremarks);
        spn_rem.setSelection(0);
        spn_rem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    remarks_notc_str = "";
                } else {
                    remarks_notc_str = remarks_notc_arr.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spn_desp = (Spinner) dialog_ready.findViewById(R.id.spn_desp);
        remarksDataModelsarr = allDisp;
        remarksDataModel = new DispositionDetailsModel();
        if (remarksDataModelsarr != null && remarksDataModelsarr.size() > 0) {

            remarksarr = new ArrayList<>();
            remarksarr.add("Select");
            if (remarksDataModelsarr != null && remarksDataModelsarr.size() > 0) {
                for (DispositionDetailsModel remarksDataModels :
                        remarksDataModelsarr) {
                    remarksarr.add(remarksDataModels.getDisposition().toUpperCase());
                }
            }

            ArrayAdapter<String> spinneradapterremarks = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, remarksarr);
            spinneradapterremarks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_desp.setAdapter(spinneradapterremarks);
            spn_desp.setSelection(0);
            spn_desp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        remarksDataModel = null;
                        ll_rem.setVisibility(View.GONE);
                    } else {
                        if (position == 1) {
                            ll_rem.setVisibility(View.VISIBLE);
                            ll_spnrem.setVisibility(View.GONE);
                            edt_desprem.setVisibility(View.VISIBLE);
                        } else if (position == 2) {
                            ll_rem.setVisibility(View.VISIBLE);
                            ll_spnrem.setVisibility(View.VISIBLE);
                            edt_desprem.setVisibility(View.GONE);
                        }
                        remarksDataModel = remarksDataModelsarr.get(position - 1);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        edt_desprem.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        edt_desprem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().startsWith(".") || s.toString().trim().startsWith(",")) {
                    // CommonUtils.toastytastyError(activity, "Enter valid Mobile Number ", false);

                    final android.app.AlertDialog.Builder builder;
                    builder = new android.app.AlertDialog.Builder(mActivity);
                    builder.setCancelable(false);
                    builder.setTitle("")
                            .setMessage("Invalid text")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    edt_desprem.setText("");
                                    dialog.dismiss();
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    edt_desprem.setFocusable(true);
                }
            }
        });

        Button btn_proceed = (Button) dialog_ready.findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remarksDataModel != null) {
                    if (remarksDataModel.getDispId() == 0) {
                        TastyToast.makeText(mActivity, "Select desposition", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (edt_desprem.getText().toString().trim().equals("") && edt_desprem.getVisibility() == View.VISIBLE) {
                        TastyToast.makeText(mActivity, "Enter Remarks", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (remarks_notc_str.equals("") && ll_spnrem.getVisibility() == View.VISIBLE) {
                        TastyToast.makeText(mActivity, "Select Remarks", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        String st = "";
                        if (remarksDataModel.getDispId() == 1) {
                            st = edt_desprem.getText().toString().trim();
                        } else if (remarksDataModel.getDispId() == 2) {
                            st = remarks_notc_str;
                        }

                        SetDispositionDataModel nm = new SetDispositionDataModel();
                        nm.setAppId(1);
                        nm.setDispId(remarksDataModel.getDispId());
                        nm.setOrderNo("" + orderVisitDetailsModel.getVisitId());
                        nm.setUserId("" + appPreferenceManager.getLoginResponseModel().getUserID());
                        String s = "" + appPreferenceManager.getLoginResponseModel().getUserName();
                        nm.setFrmNo("" + s.substring(0, Math.min(s.length(), 18)));
                        nm.setToNo("" + orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                        nm.setRemarks("" + st);

                        if (isNetworkAvailable(mActivity)) {
                            new Btech_AsyncLoadBookingFreqApi(nm).execute();
                        } else {
                            TastyToast.makeText(mActivity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                } else {
                    TastyToast.makeText(mActivity, "Select desposition", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });

    }

    private void displayBottomsheet(ArrayList<GetRemarksResponseModel> arrayList) {

        bottomSheetOrderRelease = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);

        final View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.layout_bottomsheet_pe_release, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));

        RecyclerView rec_orderRelease = bottomSheet.findViewById(R.id.rec_orderRelease);
        TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
        tv_text.setText("Select reason for not serving");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rec_orderRelease.setLayoutManager(linearLayoutManager);

        SlotDateAdapter orAdapter = new SlotDateAdapter(this, arrayList);
        rec_orderRelease.setAdapter(orAdapter);

        bottomSheetOrderRelease.setContentView(bottomSheet);
        bottomSheetOrderRelease.setCancelable(true);
        bottomSheetOrderRelease.show();

    }

    public void remarksArrayList(ArrayList<GetRemarksResponseModel> responseModelArrayList, int i) {
        remarksArray = new ArrayList<>();
        remarksArray = responseModelArrayList;
        if (i == 0) {
            setBottomSheet(remarksArray, i);
        } else {
            displayBottomsheet(remarksArray);
        }
    }

    private void setBottomSheet(ArrayList<GetRemarksResponseModel> remarksArray, int i) {
        bottomSheetOrderReschedule = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);

        final View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.layout_bottomsheet_pe_release, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));

        RecyclerView rec_orderRelease = bottomSheet.findViewById(R.id.rec_orderRelease);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rec_orderRelease.setLayoutManager(linearLayoutManager);

        OrderReleaseAdapter orAdapter = new OrderReleaseAdapter(this, remarksArray);
        rec_orderRelease.setAdapter(orAdapter);

        bottomSheetOrderReschedule.setContentView(bottomSheet);
        bottomSheetOrderReschedule.setCancelable(true);
        bottomSheetOrderReschedule.show();
    }


    public void onRemarksClick(GetRemarksResponseModel getRemarksResponseModel, int position) {
        if (bottomSheetOrderReschedule.isShowing()) {
            bottomSheetOrderReschedule.dismiss();
        }
        Global.selectedPosition = position;
        Global.selectedRemarksID = getRemarksResponseModel.getId();
        if (Integer.parseInt(getRemarksResponseModel.getReCallRemarksId()) != 0) {
            OrderReleaseRemarksController orc = new OrderReleaseRemarksController(this);
            orc.getRemarks(getRemarksResponseModel.getReCallRemarksId().toString(), 1);
        } else {
            callAPIforOrderCancellationsRemarks(getRemarksResponseModel.getId().toString());
        }

    }

    public void getReasons(ArrayList<GetPECancelRemarksResponseModel> responseModelArrayList, String ID) {
        ArrayList<GetPECancelRemarksResponseModel> arrayList = new ArrayList<>();
        arrayList = responseModelArrayList;
        Intent intent = new Intent(this, NewOrderReleaseActivity.class);
        intent.putExtra(BundleConstants.RELEASE_REMARKS, arrayList);
        intent.putExtra(BundleConstants.ORDER, strOrderNo);
        intent.putExtra(BundleConstants.ORDER_SLOTID, orderDetailsModel.getSlotId());
        intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
        intent.putExtra(BundleConstants.APPOINTMENT_DATE, orderDetailsModel.getAppointmentDate());
        intent.putExtra(BundleConstants.REMARKS_ID, ID);
        int count = orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().size();
        intent.putExtra("Bencount", count);
//        intent.putExtra(BundleConstants.POS, 3);
        intent.putExtra(BundleConstants.PINCODE, pincode);
        startActivity(intent);

    }

    public void onClickposition(GetRemarksResponseModel getRemarksResponseModel) {
        if (bottomSheetOrderRelease.isShowing()) {
            bottomSheetOrderRelease.dismiss();
        }
        callAPIforOrderCancellationsRemarks(getRemarksResponseModel.getId().toString());

    }

    public void getTestList(GetPETestResponseModel getPETestResponseModel) {
        peTestArraylist = new ArrayList<>();
        peTestArraylist = getPETestResponseModel.getData();
    }

    public void pePatientDetailsUpdated() {
        BundleConstants.setRefreshStartArriveActivity = 1;
        startActivity(getIntent());
    }

    private void onReleaseButtonClicked() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);

        final View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.layout_bottomsheet_release, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));

        TextView tv_ord_resch = bottomSheet.findViewById(R.id.tv_ord_resch);
        TextView tv_ord_rel = bottomSheet.findViewById(R.id.tv_ord_rel);
        TextView tv_ord_pass = bottomSheet.findViewById(R.id.tv_ord_pass);
        TextView tv_cancel_vst = bottomSheet.findViewById(R.id.tv_cancel_vst);
        Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
        Button btn_no = bottomSheet.findViewById(R.id.btn_no);
        LinearLayout ll_cancl = bottomSheet.findViewById(R.id.ll_cancl);
        tv_ord_pass.setText("Order Release");

        boolean toShowResheduleOption = false;
        if (!InputUtils.isNull(orderDetailsModel.getAllOrderdetails().get(0).getAppointmentDate())) {
            Date DeviceDate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date AppointDate = DateUtils.dateFromString(orderDetailsModel.getAllOrderdetails().get(0).getAppointmentDate(), format);
            int daycount = DateTimeComparator.getDateOnlyInstance().compare(AppointDate, DeviceDate);
            if (daycount == 0) {
                toShowResheduleOption = true;
            } else {
                toShowResheduleOption = false;
            }
        }
        if (orderDetailsModel.getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment")
                || orderDetailsModel.getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED")) {
            if (isValidForEditing(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())) {
            /*    items = new String[]{"Do you want to cancel the visit?"};
                cancelVisit = "y";
            } else {
                items = new String[]{"Order Release"};
            }*/
                tv_ord_pass.setVisibility(View.GONE);
                tv_ord_rel.setVisibility(View.GONE);
                tv_ord_resch.setVisibility(View.GONE);
                ll_cancl.setVisibility(View.VISIBLE);
                tv_cancel_vst.setText("Do you want to cancel the visit?");
                cancelVisit = "y";
            } else {
                ll_cancl.setVisibility(View.GONE);
                tv_ord_pass.setVisibility(View.VISIBLE);
                tv_ord_rel.setVisibility(View.GONE);
                tv_ord_resch.setVisibility(View.GONE);
            }
        } else {
            if (isValidForEditing(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())) {

                tv_ord_pass.setVisibility(View.GONE);
                tv_ord_rel.setVisibility(View.GONE);
                tv_ord_resch.setVisibility(View.GONE);
                ll_cancl.setVisibility(View.VISIBLE);
                tv_cancel_vst.setText("Do you want to cancel the visit?");
                cancelVisit = "y";
            } else {
                if (toShowResheduleOption) {
                    tv_ord_pass.setVisibility(View.GONE);
                    tv_ord_rel.setVisibility(View.VISIBLE);
                    tv_ord_resch.setVisibility(View.VISIBLE);
                    ll_cancl.setVisibility(View.GONE);

                } else {
                    tv_ord_pass.setVisibility(View.GONE);
                    tv_ord_rel.setVisibility(View.VISIBLE);
                    tv_ord_resch.setVisibility(View.GONE);
                    ll_cancl.setVisibility(View.GONE);
                }
            }
               /* items = new String[]{"Do you want to cancel the visit?"};
                cancelVisit = "y";
            } else {
                if (toShowResheduleOption) {
                    items = new String[]{"Order Reschedule",
                            "Request Release"};
                } else {
                    items = new String[]{"Request Release"};
                }
            }*/
        }

        tv_ord_resch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RescheduleOrderDialog cdd = new RescheduleOrderDialog(mActivity, new OrderRescheduleDialogButtonClickedDelegateResult(), orderDetailsModel.getAllOrderdetails().get(0));
                cdd.show();
                bottomSheetDialog.dismiss();
            }
        });

        tv_ord_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crr = new ConfirmRequestReleaseDialog(mActivity, new CConfirmOrderReleaseDialogButtonClickedDelegateResult(), orderDetailsModel);
                crr.show();
                //TODO Removed Relese pop up
               /* final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);
                View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.logout_bottomsheet, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));
                TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
                TextView tv_text1 = bottomSheet.findViewById(R.id.tv_text1);
                tv_text.setText("Warning");
                tv_text1.setVisibility(View.VISIBLE);
                tv_text1.setText("Rs 200 debit will be levied for releasing this Order");
                Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
                btn_yes.setText("Accept Debit");
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        crr = new ConfirmRequestReleaseDialog(mActivity, new CConfirmOrderReleaseDialogButtonClickedDelegateResult(), orderDetailsModel);
                        crr.show();
                    }
                });
                Button btn_no = bottomSheet.findViewById(R.id.btn_no);
                btn_no.setText("Cancel Request");
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheet);
                bottomSheetDialog.setCancelable(false);
                bottomSheetDialog.show();*/
            }
        });

        tv_ord_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crr = new ConfirmRequestReleaseDialog(mActivity, new CConfirmOrderReleaseDialogButtonClickedDelegateResult(), orderDetailsModel);
                crr.show();
                //TODO Removed Release pop up
               /* final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);
                View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.logout_bottomsheet, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));
                TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
                TextView tv_text1 = bottomSheet.findViewById(R.id.tv_text1);
                tv_text.setText("Warning");
                tv_text1.setVisibility(View.VISIBLE);
                tv_text1.setText("Rs 200 debit will be levied for releasing this Order");
                Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
                btn_yes.setText("Accept Debit");
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        crr = new ConfirmRequestReleaseDialog(mActivity, new CConfirmOrderReleaseDialogButtonClickedDelegateResult(), orderDetailsModel);
                        crr.show();
                    }
                });
                Button btn_no = bottomSheet.findViewById(R.id.btn_no);
                btn_no.setText("Cancel Request");
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheet);
                bottomSheetDialog.setCancelable(false);
                bottomSheetDialog.show();*/
            }
        });

        if (ll_cancl.getVisibility() == View.VISIBLE) {
            if (cancelVisit.equals("y")) {
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cancelVisit.equals("y")) {
                            if (isNetworkAvailable(mActivity)) {
                                CallServiceUpdateAPI();
                            } else {
                                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            bottomSheetDialog.dismiss();
                        }
                    }
                });

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        }
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();

    /*    final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Select Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Order Reschedule")) {
                    RescheduleOrderDialog cdd = new RescheduleOrderDialog(mActivity, new OrderRescheduleDialogButtonClickedDelegateResult(), orderDetailsModel.getAllOrderdetails().get(0));
                    cdd.show();
                } else if (items[item].equals("Order Release")) {
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
                    builder1.setTitle("Warning ")
                            .setMessage("Rs 200 debit will be levied for releasing this Order ").setPositiveButton("Accept Debit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            crr = new ConfirmRequestReleaseDialog(mActivity, new CConfirmOrderReleaseDialogButtonClickedDelegateResult(), orderDetailsModel);
                            crr.show();
                        }
                    }).setNegativeButton("Cancel Request", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder1.show();
                } else if (items[item].equals("Request Release")) {
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
                    builder1.setTitle("Warning ")
                            .setMessage("Rs 200 debit will be levied for releasing this order ").setPositiveButton("Accept debit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            crr = new ConfirmRequestReleaseDialog(mActivity, new CConfirmOrderReleaseDialogButtonClickedDelegateResult(), orderDetailsModel);
                            crr.show();
                        }
                    }).setNegativeButton("Cancel Request", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder1.show();
                }
            }
        });

        if (cancelVisit.equals("y")) {
            builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (cancelVisit.equals("y")) {
                        if (isNetworkAvailable(mActivity)) {
                            CallServiceUpdateAPI();
                        } else {
                            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.dismiss();
                    }
                }
            });
        }
        if (cancelVisit.equals("y")) {
            builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
        builder.show();*/

    }

    private void CallServiceUpdateAPI() {

        ServiceUpdateRequestModel serviceUpdateRequestModel = new ServiceUpdateRequestModel();
        serviceUpdateRequestModel.setVisitId(orderDetailsModel.getVisitId());
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallServiceUpdateAPI(serviceUpdateRequestModel);
        globalclass.showProgressDialog(mActivity, mActivity.getResources().getString(R.string.progress_message_changing_order_status_please_wait));

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.code() == 200) {
                    AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();

                    alertDialog.setMessage("Your visit has been cancelled successfully");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(mActivity, B2BVisitOrdersDisplayFragment.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
//                                    finish();
                                }
                            });

                    alertDialog.show();

                } else {
                    globalclass.showCustomToast(mActivity, SomethingWentwrngMsg);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                MessageLogger.LogDebug("Errror", t.getMessage());
            }
        });
    }

    private void ViewTestData(String benId) {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<GetTestListResponseModel> responseCall = apiInterface.CallGetTestDetailsAPI(benId);
        globalclass.showProgressDialog(mActivity, "Please wait..", false);
        responseCall.enqueue(new Callback<GetTestListResponseModel>() {
            @Override
            public void onResponse(Call<GetTestListResponseModel> call, retrofit2.Response<GetTestListResponseModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    GetTestListResponseModel TestListResponseModel = response.body();
                    DisplayTestListDialog(TestListResponseModel);
                } else {
                    Toast.makeText(mActivity, NO_DATA_FOUND, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetTestListResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                Toast.makeText(mActivity, NO_DATA_FOUND, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void DisplayTestListDialog(GetTestListResponseModel testListResponseModel) {
        CustomDialogClass cdd = new CustomDialogClass(mActivity, testListResponseModel);
        cdd.show();
    }

    private void iflateTestGroupName(LinearLayout ll_tests, GetTestListResponseModel testListResponseModel) {
        if (testListResponseModel != null && testListResponseModel.getTestGroupList() != null && testListResponseModel.getTestGroupList().size() > 0) {
            // Logger.error("if ");
            ll_tests.removeAllViews();
            for (int i = 0; i < testListResponseModel.getTestGroupList().size(); i++) {
                LayoutInflater vi = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.item_view_test, null);
                final TextView tv_test = (TextView) v.findViewById(R.id.tv_test);
                final LinearLayout ll_child = (LinearLayout) v.findViewById(R.id.ll_child);
                tv_test.setText("" + testListResponseModel.getTestGroupList().get(i).getGroupName() + " (" + testListResponseModel.getTestGroupList().get(i).getTestCount() + ")");
                tv_test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_group_collapse_15, 0);

                tv_test.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ll_child.getVisibility() == View.VISIBLE) {
                            ll_child.setVisibility(View.GONE);
                            tv_test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_group_collapse_15, 0);
                        } else {
                            ll_child.setVisibility(View.VISIBLE);
                            tv_test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_group_expand_15, 0);

                        }
                    }
                });
                ll_child.removeAllViews();
                for (int j = 0; j < testListResponseModel.getTestGroupList().get(i).getTestDetails().size(); j++) {
                    LayoutInflater vj = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v1 = vj.inflate(R.layout.item_view_test, null);
                    TextView tv_test1 = (TextView) v1.findViewById(R.id.tv_test);
                    tv_test1.setBackgroundColor(Color.parseColor("#ffffff"));
                    tv_test1.setTextColor(Color.parseColor("#000000"));
                    tv_test1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    tv_test1.setText(">> Description:  " + testListResponseModel.getTestGroupList().get(i).getTestDetails().get(j).getDescription() + " \n     Test Code: " + testListResponseModel.getTestGroupList().get(i).getTestDetails().get(j).getTestCode() + "\n     Unit: " + testListResponseModel.getTestGroupList().get(i).getTestDetails().get(j).getUnit());

                    ll_child.addView(v1);

                    ll_child.invalidate();
                }

                ll_tests.addView(v);
                ll_tests.invalidate();
            }
        } else {
            Global.showCustomStaticToast(mActivity, "" + testListResponseModel.getStatus());
        }
    }

    private void OnDeleteBenClicked(final OrderVisitDetailsModel orderVisitDetailsModel, final BeneficiaryDetailsModel selectedbeneficiaryDetailsModel) {

        int Bencount = orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size();
        MessageLogger.PrintMsg("Count : " + Bencount);
        String Benname = selectedbeneficiaryDetailsModel.getName();
        if (Bencount <= 1) {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);
            View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.logout_bottomsheet, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));
            TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
            tv_text.setText(Html.fromHtml(OrdercontainonlyOneBeneficaryCannotremovedMsg));
            Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
            Button btn_no = bottomSheet.findViewById(R.id.btn_no);
            btn_no.setVisibility(View.GONE);
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.setContentView(bottomSheet);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();
            /*AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
            builder1.setMessage(Html.fromHtml(OrdercontainonlyOneBeneficaryCannotremovedMsg))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();*/
        } else {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);
            View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.paymentbottomsheet, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));
            TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
            TextView tv_label = bottomSheet.findViewById(R.id.tv_label);
            tv_text.setText(Html.fromHtml("Do you really want to remove Beneficiary (" + "<b>" + Benname + "</b>" + ") ?"));
            tv_label.setText("Confirm Action");

            Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removebenModel = null;
                    removebenModel = new RemoveBeneficiaryAPIRequestModel();
                    removebenModel.setBenId(selectedbeneficiaryDetailsModel.getBenId());
                    removebenModel.setOrderNo(selectedbeneficiaryDetailsModel.getOrderNo());
                    removebenModel.setUserId("" + appPreferenceManager.getLoginResponseModel().getUserID());
                    removebenModel.setIsAdded(orderVisitDetailsModel.getAllOrderdetails().get(0).isAddBen() ? "1" : "0");
                    bottomSheetDialog.dismiss();
                    if (cd.isConnectingToInternet()) {
                        //fungible
//                        if (BundleConstants.isPEPartner) {
                        if (appPreferenceManager.isPEPartner()) {
//                        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                            if (!orderVisitDetailsModel.getAllOrderdetails().get(0).isOTP()) {
                                callAPIforOrderEditDelete("Delete", selectedbeneficiaryDetailsModel.getBenId());
                                //   CallRemoveBenAPI(removebenModel);
                            } else {
                                CallsendOTPAPIforOrderEdit("Delete", orderVisitDetailsModel, orderVisitDetailsModel.getVisitId(), selectedbeneficiaryDetailsModel.getBenId());
                            }
                        } else {
                            CallsendOTPAPIforOrderEdit("Delete", orderVisitDetailsModel, orderVisitDetailsModel.getVisitId(), selectedbeneficiaryDetailsModel.getBenId());
                        }
                    } else {
                        globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
                    }
                }
            });
            Button btn_no = bottomSheet.findViewById(R.id.btn_no);
            btn_no.setText("Cancel");
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();

                }
            });
            bottomSheetDialog.setContentView(bottomSheet);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            /*AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
            builder1.setTitle("Confirm Action")
                    .setMessage(Html.fromHtml("Do you really want to remove Beneficiary (" + "<b>" + Benname + "</b>" + ") ?"))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {

                            removebenModel = null;
                            removebenModel = new RemoveBeneficiaryAPIRequestModel();
                            removebenModel.setBenId(selectedbeneficiaryDetailsModel.getBenId());
                            removebenModel.setOrderNo(selectedbeneficiaryDetailsModel.getOrderNo());
                            removebenModel.setUserId("" + appPreferenceManager.getLoginResponseModel().getUserID());
                            removebenModel.setIsAdded(orderVisitDetailsModel.getAllOrderdetails().get(0).isAddBen() ? "1" : "0");
                            dialog.dismiss();
                            if (cd.isConnectingToInternet()) {
                                CallsendOTPAPIforOrderEdit("Delete", orderVisitDetailsModel, orderVisitDetailsModel.getVisitId(), selectedbeneficiaryDetailsModel.getBenId());
                            } else {
                                globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
                            }

                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
*/
        }
    }

    private void callAPIforOrderEditDelete(String action, int benId) {
        /*for (int i = 0; i < orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().size(); i++) {
            if (benId == orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getBenId()){
                orderDetailsModel.getAllOrderdetails()
            }

        }*/

        ArrayList<PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO.TestsDTO> dtoArrayList = new ArrayList<>();
        PEOrderEditRequestModel peOrderEditRequestModel = new PEOrderEditRequestModel();
        PEOrderEditRequestModel.DataDTO dataDTO = new PEOrderEditRequestModel.DataDTO();
        PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO extraPayloadDTO;
        dataDTO.setPartner_order_id(orderDetailsModel.getVisitId());
        dataDTO.setEvent(BundleConstants.EDIT_TEST);
        dataDTO.setUserId(appPreferenceManager.getLoginResponseModel().getUserID());
        dataDTO.setChild_order_id("");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        extraPayloadDTO = new PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO();
        extraPayloadDTO.setEvent_timestamp(currentDateandTime);

        dataDTO.setExtra_payload(extraPayloadDTO);

        for (int i = 0; i < orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().size(); i++) {
            if (benId != orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getBenId()) {
                ArrayList<String> strArr = new ArrayList<>();
                String strTest = orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestsCode();
                String[] str = strTest.split(",");
                for (String s : str) {
                    String str1 = s.trim();
                    strArr.add(str1);
                }

                for (int j = 0; j < strArr.size(); j++) {
                    for (int k = 0; k < orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestSampleType().size(); k++) {
                        if (strArr.get(j).equalsIgnoreCase(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestSampleType().get(k).getTests())) {
                            PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO.TestsDTO testsDTO = new PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO.TestsDTO();
                            if (orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestSampleType().get(k).getTestType().equalsIgnoreCase("test") || orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestSampleType().get(k).getTestType().equalsIgnoreCase("Profile")) {
                                testsDTO.setDos_code(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestSampleType().get(k).getTests());
                                testsDTO.setLab_dos_name(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestSampleType().get(k).getTests());
                            } else if (orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestSampleType().get(k).getTestType().equalsIgnoreCase("package")) {
                                if (!InputUtils.isNull(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getProjId()) ||
                                        !InputUtils.CheckEqualIgnoreCase(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getProjId(), "")) {
                                    testsDTO.setDos_code(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getProjId());
                                    testsDTO.setLab_dos_name(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getProjId());
                                } else {
                                    testsDTO.setDos_code(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestSampleType().get(k).getTests());
                                    testsDTO.setLab_dos_name(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestSampleType().get(k).getTests());
                                }
                            }

                            for (int l = 0; l < peTestArraylist.size(); l++) {
                                if (strArr.get(j).equalsIgnoreCase(peTestArraylist.get(l).getName()) || strArr.get(j).equalsIgnoreCase(peTestArraylist.get(l).getPartner_lab_test_id()) || strArr.get(j).equalsIgnoreCase(peTestArraylist.get(l).getDos_code())) {
                                    testsDTO.setPrice(Integer.parseInt(peTestArraylist.get(l).getPrice()));
                                    testsDTO.setType(peTestArraylist.get(l).getType());
                                }
                            }
                            testsDTO.setPartner_patient_id(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getLeadId());
                            //  testsDTO.setType(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestSampleType().get(k).getTestType());
                            dtoArrayList.add(testsDTO);
                            break;
                        }
                    }

                }
            }
        }


        extraPayloadDTO.setTests(dtoArrayList);
        dataDTO.setExtra_payload(extraPayloadDTO);

        peOrderEditRequestModel.setData(dataDTO);

        callAPIFORPEORDEREDIT(action, peOrderEditRequestModel, 1);

    }

    private void callAPIFORPEORDEREDIT(String action, PEOrderEditRequestModel peOrderEditRequestModel, int i) {
        if (cd.isConnectingToInternet()) {
            PEOrderEditController peOrderEditController = new PEOrderEditController(this);
            peOrderEditController.postPEOrderEdit(action, peOrderEditRequestModel, i);
        } else {
            globalclass.showCustomToast(mActivity, CHECK_INTERNET_CONN, Toast.LENGTH_SHORT);
        }
    }

    private void CallRemoveBenAPI(final FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel, final RemoveBeneficiaryAPIRequestModel rembenmode, final String orderNo, final int benId) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<OrderVisitDetailsModel> responseCall = apiInterface.CallRemoveBenAPI("Bearer " + appPreferenceManager.getLoginResponseModel().getAccess_token(), rembenmode);
        globalclass.showProgressDialog(mActivity, getResources().getString(R.string.progress_message_removing_beneficiary_please_wait));
        responseCall.enqueue(new Callback<OrderVisitDetailsModel>() {
            @Override
            public void onResponse(Call<OrderVisitDetailsModel> call, retrofit2.Response<OrderVisitDetailsModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    OrderVisitDetailsModel visitDetailsModel = response.body();
                    onRemoveBenAPIResponseReceived(fetchOrderDetailsResponseModel, visitDetailsModel, rembenmode, orderNo, benId);
                } else {

                    globalclass.showCustomToast(mActivity, SomethingWentwrngMsg);
                }
            }

            @Override
            public void onFailure(Call<OrderVisitDetailsModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, SomethingWentwrngMsg);
            }
        });
    }

    private void onRemoveBenAPIResponseReceived(FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel, OrderVisitDetailsModel orderVisitDetailsModel, RemoveBeneficiaryAPIRequestModel rembenmode, String orderNo, int benId) {

        Toast.makeText(mActivity, "Beneficiary Removed Successfully", Toast.LENGTH_SHORT).show();
        try {
            CallSendSMSForBeneficicaryRemoveAPI(String.valueOf(orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue()), orderVisitDetailsModel.getVisitId(), rembenmode.getBenId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*if (fetchOrderDetailsResponseModel != null) {
            for (int i = 0; i < fetchOrderDetailsResponseModel.getOrderVisitDetails().size(); i++) {
                if (InputUtils.CheckEqualIgnoreCase(fetchOrderDetailsResponseModel.getOrderVisitDetails().get(i).getVisitId(), orderNo)) {
                    for (int j = 0; j < fetchOrderDetailsResponseModel.getOrderVisitDetails().get(i).getAllOrderdetails().size(); j++) {
                        for (int k = 0; k < fetchOrderDetailsResponseModel.getOrderVisitDetails().get(i).getAllOrderdetails().get(j).getBenMaster().size(); k++) {
                            if (benId == fetchOrderDetailsResponseModel.getOrderVisitDetails().get(i).getAllOrderdetails().get(j).getBenMaster().get(k).getBenId()) {
                                fetchOrderDetailsResponseModel.getOrderVisitDetails().get(i).getAllOrderdetails().get(j).getBenMaster().remove(k);
                            }
                        }
                    }
                }
            }
        }
        appPreferenceManager.setfetchOrderDetailsResponseModel(fetchOrderDetailsResponseModel);*/

        //  startActivity(new Intent(this, StartAndArriveActivity.class));

        if (cd.isConnectingToInternet()) {
            CallOrderDetailAPI("Arrive");
        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);
        }

    }

    private void CallSendSMSForBeneficicaryRemoveAPI(String amount, String orderNo, int Ben_ID) {
        SendSMSAfterBenRemovedRequestModel sendSMSAfterBenRemovedRequestModel = new SendSMSAfterBenRemovedRequestModel();
        sendSMSAfterBenRemovedRequestModel.setBenId(Ben_ID);
        sendSMSAfterBenRemovedRequestModel.setOrderNo(orderNo);
        sendSMSAfterBenRemovedRequestModel.setRate1(amount);

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallSendSMSafterBeneficaryRemovedAPI("Bearer " + appPreferenceManager.getLoginResponseModel().getAccess_token(), sendSMSAfterBenRemovedRequestModel);
//        globalclass.showProgressDialog(mActivity,"Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                globalclass.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    MessageLogger.PrintMsg("Success");
                } else {
                    MessageLogger.PrintMsg("Failure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                globalclass.hideProgressDialog();
                MessageLogger.PrintMsg("Failure");
            }
        });
    }

    private void callOrderStatusChangeApi(int status, final String strButton, String remarks, String date) {

        OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
        orderStatusChangeRequestModel.setId(orderDetailsModel.getSlotId() + "");
        orderStatusChangeRequestModel.setStatus(status);
        if (!InputUtils.isNull(remarks)) {
            orderStatusChangeRequestModel.setRemarks(remarks);
        }
        if (!InputUtils.isNull(date)) {
            orderStatusChangeRequestModel.setAppointmentDate(date);
        }

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallOrderStatusChangeAPI(orderStatusChangeRequestModel, orderStatusChangeRequestModel.getId());
        globalclass.showProgressDialog(mActivity, getResources().getString(R.string.progress_message_changing_order_status_please_wait));

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.code() == 200 || response.code() == 204) {
                    onOrderStatusChangedResponseReceived(strButton, orderDetailsModel.getVisitId());
                } else {
                    try {
                        Toast.makeText(mActivity, response.errorBody() != null ? response.errorBody().string() : SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(mActivity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                Toast.makeText(mActivity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onOrderStatusChangedResponseReceived(String strButton, String visitId) {
        btn_start.setEnabled(false);
        if (strButton.equalsIgnoreCase("Start")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn_arrive.setVisibility(View.VISIBLE);
                    btn_start.setVisibility(View.GONE);
                }
            }, 1000);

//            Toast.makeText(mActivity, "Started Successfully", Toast.LENGTH_SHORT).show();
            startTrackerService();
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q=" + orderDetailsModel.getAllOrderdetails().get(0).getLatitude() + "," + orderDetailsModel.getAllOrderdetails().get(0).getLongitude()));
            startActivity(intent);
            SendinglatlongOrderAllocation(7);

        } else if (strButton.equalsIgnoreCase("Arrive")) {
            try {
                if (isValidForEditing(orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())) {
                    //fungible
//                    if (BundleConstants.isPEPartner && BundleConstants.PEDSAOrder) {
                    if (appPreferenceManager.isPEPartner() && appPreferenceManager.PEDSAOrder()) {
//                    if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                        btn_floating_add_ben.setVisibility(View.GONE);
//                    } else if (BundleConstants.isPEPartner) {
                    } else if (appPreferenceManager.isPEPartner()) {
                        btn_floating_add_ben.setVisibility(View.VISIBLE);
                    } else {
                        btn_floating_add_ben.setVisibility(View.VISIBLE);
                    }
                    //fungible
                }
//                if (BundleConstants.isPEPartner && BundleConstants.PEDSAOrder) {
                if (appPreferenceManager.isPEPartner() && appPreferenceManager.PEDSAOrder()) {
//                    if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                    btn_floating_add_ben.setVisibility(View.GONE);
//                } else if (BundleConstants.isPEPartner) {
                } else if (appPreferenceManager.isPEPartner()) {
                    btn_floating_add_ben.setVisibility(View.VISIBLE);
                } else if (orderDetailsModel.getAllOrderdetails().get(0).isEditOrder()) {
                    btn_floating_add_ben.setVisibility(View.VISIBLE);
                } else {
                    btn_floating_add_ben.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //fungible
//                if (BundleConstants.isPEPartner && BundleConstants.PEDSAOrder) {
                if (appPreferenceManager.isPEPartner() && appPreferenceManager.PEDSAOrder()) {
//                    if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                    btn_floating_add_ben.setVisibility(View.GONE);
//                } else if (BundleConstants.isPEPartner) {
                } else if (appPreferenceManager.isPEPartner()) {
                    btn_floating_add_ben.setVisibility(View.VISIBLE);
                } else if (orderDetailsModel.getAllOrderdetails().get(0).isEditOrder()) {
                    btn_floating_add_ben.setVisibility(View.VISIBLE);
                } else {
                    btn_floating_add_ben.setVisibility(View.GONE);
                }

            }

            //hardblocking
//            if (BundleConstants.isPEPartner || BundleConstants.PEDSAOrder) {
            if (appPreferenceManager.isPEPartner() || appPreferenceManager.PEDSAOrder()) {
                if (checkForTest(1)) {
                    btn_Proceed.setEnabled(true);
                    btn_Proceed.setVisibility(View.VISIBLE);
                }
            } else {
                btn_Proceed.setVisibility(View.VISIBLE);
            }
//            btn_Proceed.setVisibility(View.VISIBLE);


            btn_arrive.setVisibility(View.GONE);
            if (startArriveOrderDetailsAdapter != null) {
                startArriveOrderDetailsAdapter.updateStatus("Arrive");
                startArriveOrderDetailsAdapter.notifyDataSetChanged();
            }
//            Toast.makeText(mActivity, "Arrived Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
            setResult(BundleConstants.VOMD_ARRIVED, intent);
            StopService();
            SendinglatlongOrderAllocation(3);

        } else if (strButton.equalsIgnoreCase("Manipulation")) {
//            globalclass.showCustomToast(mActivity,"Order Released Successfully");
            /*if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName()) && InputUtils.CheckEqualIgnoreCase(appPreferenceManager.getLoginResponseModel().getB2bLogin(), BundleConstants.B2BLogin)) {
                RemoveOrderFromLocal(visitId);
            }*/
            TastyToast.makeText(mActivity, "Order Released Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
//            Intent intent = new Intent(this, VisitOrdersDisplayFragment_new.class);
            Intent intent = new Intent(this, B2BVisitOrdersDisplayFragment.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // finish();
        } else if (strButton.equalsIgnoreCase("Reschedule")) {
           /* if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName()) && InputUtils.CheckEqualIgnoreCase(appPreferenceManager.getLoginResponseModel().getB2bLogin(), BundleConstants.B2BLogin)) {
                appPreferenceManager.setfetchOrderDetailsResponseModel(null);
            }*/
            Toast.makeText(mActivity, "Order Rescheduled successfully", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, VisitOrdersDisplayFragment_new.class);
            Intent intent = new Intent(this, B2BVisitOrdersDisplayFragment.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void RemoveOrderFromLocal(String orderVisitID) {
        try {
            FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel = appPreferenceManager.getfetchOrderDetailsResponseModel();
            if (fetchOrderDetailsResponseModel != null) {
                if (fetchOrderDetailsResponseModel.getOrderVisitDetails() != null) {
                    for (int i = 0; i < fetchOrderDetailsResponseModel.getOrderVisitDetails().size(); i++) {
                        System.out.println("Order ID >> " + orderVisitID + " >> " + fetchOrderDetailsResponseModel.getOrderVisitDetails().get(i).getVisitId());
                        if (orderVisitID.toString().trim().equalsIgnoreCase(fetchOrderDetailsResponseModel.getOrderVisitDetails().get(i).getVisitId().toString().trim())) {
                            fetchOrderDetailsResponseModel.getOrderVisitDetails().remove(i);
                            break;
                        }
                    }
                }
            }
            appPreferenceManager.setfetchOrderDetailsResponseModel(fetchOrderDetailsResponseModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SendinglatlongOrderAllocation(int status) {

        if (ApplicationController.sendLatLongforOrderController != null) {
            ApplicationController.sendLatLongforOrderController = null;
        }
        ApplicationController.sendLatLongforOrderController = new SendLatLongforOrderController(mActivity);
        ApplicationController.sendLatLongforOrderController.SendLatlongToToServer(orderDetailsModel.getVisitId(), status);
        ApplicationController.sendLatLongforOrderController.setOnResponseListener(new SendLatLongforOrderController.OnResponseListener() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onfailure(String msg) {

            }
        });
    }

    private void startTrackerService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(FirebaselocationUpdateIntent);
        } else {
            startService(FirebaselocationUpdateIntent);
        }
    }

    private void StopService() {
        try {
            stopService(new Intent(mActivity, TrackerService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CallsendOTPAPIforOrderEdit(final String Action, final OrderVisitDetailsModel orderVisitDetailsModel, final String orderNo, final int finalBenId) {

        SendOTPRequestModel model = new SendOTPRequestModel();
        model.setMobile(orderDetailsModel.getAllOrderdetails().get(0).getMobile());
        model.setOrderno(orderDetailsModel.getAllOrderdetails().get(0).getVisitId());
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<CommonResponseModel2> responseCall = apiInterface.CallSendOTPAPI(model);
        globalclass.showProgressDialog(mActivity, "Requesting for OTP. Please wait..");
        responseCall.enqueue(new Callback<CommonResponseModel2>() {
            @Override
            public void onResponse(Call<CommonResponseModel2> call, Response<CommonResponseModel2> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    CommonResponseModel2 responseModel = response.body();
                    if (!TextUtils.isEmpty(responseModel.getRES_ID()) && responseModel.getRES_ID().equalsIgnoreCase("RES0000")) {
                        globalclass.showCustomToast(mActivity, "OTP send successfully to mobile number mapped to this order.");
                        ShowDialogToVerifyOTP(Action, orderVisitDetailsModel, orderNo, finalBenId);
                    } else {
                        globalclass.showCustomToast(mActivity, "OTP Generation Failed.");
                    }
                } else {
                    globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<CommonResponseModel2> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
            }
        });
    }

    private void ShowDialogToVerifyOTP(final String Action, final OrderVisitDetailsModel orderVisitDetailsModel, final String orderNo, final int finalBenId) {

        CustomDialogforOTPValidation = new Dialog(mActivity);
        CustomDialogforOTPValidation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomDialogforOTPValidation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomDialogforOTPValidation.setContentView(R.layout.validate_otp_dialog);
        CustomDialogforOTPValidation.setCancelable(false);
        CustomDialogforOTPValidation.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RelativeLayout rel_main = (RelativeLayout) CustomDialogforOTPValidation.findViewById(R.id.rel_main);
        TextView tv_header = (TextView) CustomDialogforOTPValidation.findViewById(R.id.tv_header);
        ImageView img_btn_validateOTP = (ImageView) CustomDialogforOTPValidation.findViewById(R.id.img_btn_validateOTP);
        ImageView img_close = (ImageView) CustomDialogforOTPValidation.findViewById(R.id.img_close);
        final EditText edt_OTP = (EditText) CustomDialogforOTPValidation.findViewById(R.id.edt_OTP);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = 0, width = 0;
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

        CustomDialogforOTPValidation.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogforOTPValidation.dismiss();
            }
        });

        img_btn_validateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strOTP = edt_OTP.getText().toString().trim();
                if (!InputUtils.isNull(strOTP) && strOTP.length() != 4) {
                    globalclass.showalert_OK("Please enter valid OTP. Length required : 4", mActivity);
                    edt_OTP.requestFocus();
                } else {
                    OrderPassRequestModel model = new OrderPassRequestModel();

                    model.setMobile(orderDetailsModel.getAllOrderdetails().get(0).getMobile());
                    model.setOTP(strOTP);
                    model.setVisitId(orderDetailsModel.getAllOrderdetails().get(0).getVisitId());
                    if (cd.isConnectingToInternet()) {
                        CallValidateOTPAPI(model, Action, orderVisitDetailsModel, orderNo, finalBenId);
                    } else {
                        globalclass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.plz_chk_internet));
                    }
                }
            }
        });
    }

    private void CallValidateOTPAPI(OrderPassRequestModel model, final String Action, final OrderVisitDetailsModel orderVisitDetailsModel, final String orderNo, final int finalBenId) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallValidateOTPAPI(model);
        globalclass.showProgressDialog(mActivity, "Requesting for OTP. Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog(mActivity);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String strresponse = response.body();
                        if (!InputUtils.isNull(strresponse) && strresponse.toUpperCase().contains("SUCCESS")) {
                            Toast.makeText(mActivity, "OTP Validated Successfully.", Toast.LENGTH_SHORT).show();
                            if (!mActivity.isFinishing() && CustomDialogforOTPValidation != null && CustomDialogforOTPValidation.isShowing()) {
                                CustomDialogforOTPValidation.dismiss();
                            }
                            if (Action.equalsIgnoreCase("Delete")) {
                                //fungible
//                                if (BundleConstants.isPEPartner) {
                                if (appPreferenceManager.isPEPartner()) {
                                    //                            if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                                    callAPIforOrderEditDelete(Action, removebenModel.getBenId());
                                } else {
                                    CallRemoveBenAPI(appPreferenceManager.getfetchOrderDetailsResponseModel(), removebenModel, orderNo, benId);
                                }
                            }
                        } else {
                            globalclass.showCustomToast(mActivity, "Invalid OTP.");
                        }
                    } else if (response.code() == 401) {
                        globalclass.showCustomToast(mActivity, "Invalid OTP.");
                    } else {
                        globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
            }
        });
    }

    private void displayDelayTimer(OrderVisitDetailsModel orderVisitDetailsModel) {

    }
    // TODO ADD and Edit Ben Functionality-------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BundleConstants.ADDEDITBENREQUESTCODE:
                if (resultCode == RESULT_OK) {
                    if (cd.isConnectingToInternet()) {
                        CallOrderDetailAPI("Arrive");
                    } else {
                        globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);
                    }
                }
                break;

            case BundleConstants.PAYMENTS_START:
                if (resultCode == BundleConstants.PAYMENTS_FINISH) {
                    boolean isPaymentSuccess = data.getBooleanExtra(BundleConstants.PAYMENT_STATUS, false);
                    if (isPaymentSuccess) {
                        // TODO code to reduce the size of Json by temporary storing Venupunture in global array
                        BundleConstants.setRefreshStartArriveActivity = 1;
                        setRefreshActivity();

                    } else {
                        Toast.makeText(mActivity, "Payment failed", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<FetchOrderDetailsResponseModel> fetchOrderDetailsResponseModelCall = getAPIInterface.getOrderDetail(BundleConstants.setSelectedOrder);
            fetchOrderDetailsResponseModelCall.enqueue(new Callback<FetchOrderDetailsResponseModel>() {
                @Override
                public void onResponse(Call<FetchOrderDetailsResponseModel> call, Response<FetchOrderDetailsResponseModel> response) {
                    FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel = response.body();
                    if (fetchOrderDetailsResponseModel != null && fetchOrderDetailsResponseModel.getOrderVisitDetails() != null && fetchOrderDetailsResponseModel.getOrderVisitDetails().size() > 0) {
                        if (BundleConstants.PE_HARD_BLOCKING) {
                            btn_Proceed.setEnabled(true);
                            btn_Proceed.setTextColor(getResources().getColor(R.color.bg_new_color));
                            btn_Proceed.setVisibility(View.VISIBLE);
                        }
//                        BundleConstants.isPEPartner = fetchOrderDetailsResponseModel.getOrderVisitDetails().get(0).getAllOrderdetails().get(0).isPEPartner();
                        appPreferenceManager.setPE_Partner(fetchOrderDetailsResponseModel.getOrderVisitDetails().get(0).getAllOrderdetails().get(0).isPEPartner());
                        appPreferenceManager.setPE_DSA(fetchOrderDetailsResponseModel.getOrderVisitDetails().get(0).getAllOrderdetails().get(0).isPEDSAOrder());
//                        BundleConstants.PEDSAOrder = fetchOrderDetailsResponseModel.getOrderVisitDetails().get(0).getAllOrderdetails().get(0).isPEDSAOrder();
                        for (OrderVisitDetailsModel orderVisitDetailsModel : fetchOrderDetailsResponseModel.getOrderVisitDetails()) {
                            if (orderVisitDetailsModel.getVisitId().equalsIgnoreCase(strOrderNo)) {
                                if (orderVisitDetailsModel.getAllOrderdetails() != null && orderVisitDetailsModel.getAllOrderdetails().size() > 0) {
                                    if (orderVisitDetailsModel.getAllOrderdetails().get(0).getTimestamp() != null && orderVisitDetailsModel.getAllOrderdetails().get(0).getCancelSMSSentTime() != null) {
                                        if (!InputUtils.isNull(appPreferenceManager.getOrderNo()) &&
                                                InputUtils.CheckEqualIgnoreCase(appPreferenceManager.getOrderNo(), orderDetailsModel.getVisitId())) {
                                            /*if (Global.TimerFlag) {
                                                reloadActivity();
                                            } else {*/
                                            try {
                                                String TimeStamp = DateUtil.Req_Date_Req(orderVisitDetailsModel.getAllOrderdetails().get(0).getTimestamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss");
                                                String CancelSMS = DateUtil.Req_Date_Req(orderVisitDetailsModel.getAllOrderdetails().get(0).getCancelSMSSentTime(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss");
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                Date ServerTime = dateFormat.parse(TimeStamp);
                                                Date SMSCancelTime = dateFormat.parse(CancelSMS);
                                                long difference = ServerTime.getTime() - SMSCancelTime.getTime();
                                                long futuerMili = 300000 - difference;
                                                if (difference < 300000) {
                                                    ll_timer.setVisibility(View.VISIBLE);
                                                    if (countDownTimer != null) {
                                                        countDownTimer.cancel();
                                                    }
                                                    countDownTimer = new CountDownTimer(futuerMili, 1000) {

                                                        public void onTick(long millisUntilFinished) {
                                                            String text = String.format(Locale.getDefault(), "Wait for %02d min: %02d sec", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60, TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                                                            tv_timerCount.setText(text);
                                                            tv_order_release.setTextColor(getResources().getColor(R.color.gray));
                                                            customSwipeButton2.setEnabled(false);
                                                            LL_swipe.setBackground(getResources().getDrawable(R.drawable.rounded_background_grey));
                                                        }

                                                        public void onFinish() {
                                                            tv_timerCount.setText("Wait for 00:00");
                                                            tv_order_release.setTextColor(getResources().getColor(R.color.bg_new_color));
                                                            tv_timerTxt.setText("Release the order if you did not connect with the customer");
                                                            customSwipeButton2.setEnabled(true);
                                                            LL_swipe.setBackground(getResources().getDrawable(R.drawable.rounded_background_green_2));
                                                        }
                                                    }.start();
                                                } else {
                                                    ll_timer.setVisibility(View.GONE);
                                                    if (countDownTimer != null) {
                                                        countDownTimer.cancel();
                                                    }
                                                }

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
//                                            }
                                        } else {
                                            ll_timer.setVisibility(View.GONE);
                                        }
                                    } else {
                                        ll_timer.setVisibility(View.GONE);
                                    }
                                }
                            }
                            break;
                        }
                    }

                }

                @Override
                public void onFailure(Call<FetchOrderDetailsResponseModel> call, Throwable t) {
                    globalclass.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        initTimer();
    }

    public class Btech_AsyncLoadBookingFreqApi extends AsyncTask<Void, Void, String> {

        SetDispositionDataModel setDispositionDataModel;
        private int statusCode;

        public Btech_AsyncLoadBookingFreqApi(SetDispositionDataModel nm) {
            this.setDispositionDataModel = nm;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            globalclass.showProgressDialog(mActivity, ConstantsMessages.PLEASE_WAIT);
        }

        @Override
        protected String doInBackground(Void... voids) {

            HttpClient httpClient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            String json = "";
            try {
                HttpPost request = new HttpPost(EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD)) + "/api/OrderAllocation/MediaUpload");

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                entity.addPart("AppId", new StringBody("" + setDispositionDataModel.getAppId()));
                entity.addPart("DispId", new StringBody("" + setDispositionDataModel.getDispId()));
                entity.addPart("FrmNo", new StringBody("" + setDispositionDataModel.getFrmNo()));
                entity.addPart("OrderNo", new StringBody("" + setDispositionDataModel.getOrderNo()));
                entity.addPart("Remarks", new StringBody("" + setDispositionDataModel.getRemarks()));
                entity.addPart("ToNo", new StringBody("" + setDispositionDataModel.getToNo()));
                entity.addPart("UserId", new StringBody("" + setDispositionDataModel.getUserId()));

                request.setEntity(entity);
                HttpResponse response = httpClient.execute(request);
                StatusLine statusLine = response.getStatusLine();
                statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity responseEntity = response.getEntity();
                    InputStream content = responseEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    json = builder.toString();
                    MessageLogger.PrintMsg("Nitya >> " + json);

                } else {
                    HttpEntity responseEntity = response.getEntity();
                    InputStream content = responseEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    json = builder.toString();
                    MessageLogger.PrintMsg("Nitya >> " + json);
                }

            } catch (IllegalStateException e) {
                MessageLogger.LogError("FileUpload Illegal", e.getMessage());
            } catch (IOException e) {
                MessageLogger.LogError("FileUpload IOException ", e.getMessage());
            } catch (Exception e) {
                MessageLogger.LogError("Exception ", e.getMessage());
            } finally {
                // close connections
                httpClient.getConnectionManager().shutdown();
            }

            return json;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            try {
                globalclass.hideProgressDialog(mActivity);
                if (statusCode == 200) {
                    if (dialog_ready != null) {
                        if (dialog_ready.isShowing()) {
                            dialog_ready.dismiss();
                        }
                    }
                    TastyToast.makeText(mActivity, "" + res, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                } else {
                    TastyToast.makeText(mActivity, "" + res, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class OrderRescheduleDialogButtonClickedDelegateResult implements OrderRescheduleDialogButtonClickedDelegate {

        @Override
        public void onOkButtonClicked(OrderDetailsModel orderDetailsModel, String remark, String date) {

            if (cd.isConnectingToInternet()) {
                callOrderStatusChangeApi(11, "Reschedule", remark, date);
            } else {
                Toast.makeText(mActivity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelButtonClicked() {

        }
    }

    public class CConfirmOrderReleaseDialogButtonClickedDelegateResult implements ConfirmOrderReleaseDialogButtonClickedDelegate {
        @Override
        public void onOkButtonClicked(OrderVisitDetailsModel orderVisitDetailsModel, String remarks) {
            if (cd.isConnectingToInternet()) {
                callOrderStatusChangeApi(27, "Manipulation", remarks, "");
            } else {
                TastyToast.makeText(mActivity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        }

        @Override
        public void onCancelButtonClicked() {

        }
    }

    private class CustomDialogClass extends Dialog {

        private Activity c;
        private Dialog d;
        private Button yes, no;
        private TextView tv_test;
        private LinearLayout ll_tests;
        private GetTestListResponseModel testListResponseModel;

        public CustomDialogClass(Activity mActivity, GetTestListResponseModel testListResponseModel) {
            super(mActivity);
            this.c = mActivity;
            this.testListResponseModel = testListResponseModel;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.item_test_list_display);
            ll_tests = (LinearLayout) findViewById(R.id.ll_tests);
            iflateTestGroupName(ll_tests, testListResponseModel);
            ImageView img_close = (ImageView) findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

    }
}
