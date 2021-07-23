package com.thyrocare.btechapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.StartAndArriveActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.response.FetchOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.OrderDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.io.File;
import java.util.ArrayList;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;

public class KIOSK_Scanner_Activity extends AppCompatActivity {

    private CardView cdView_ScanQRCode;
    Activity mActivity;
    KIOSK_Scanner_Activity mKIOSK_Scanner_Activity;
    private Global global;
    private AppPreferenceManager appPreferenceManager;
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels = new ArrayList<>();
    public static final String TAG_FRAGMENT = "KIOSK_Scanner_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiosk_scanner);

        mActivity = this;
        mKIOSK_Scanner_Activity = this;
        global = new Global(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);

        BundleConstants.isKIOSKOrder = false;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        InItUI();
        InItClickListner();
    }

    private void InItUI() {
        cdView_ScanQRCode = (CardView) findViewById(R.id.cdView_ScanQRCode);
    }

    private void InItClickListner() {
        cdView_ScanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenScanPatientQrCodeScreen();
            }
        });
    }

    private void OpenScanPatientQrCodeScreen() {
        IntentIntegrator integrator = new IntentIntegrator(mActivity);
        integrator.setPrompt("Place QR code inside the rectangle to scan.");
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BundleConstants.START_BARCODE_SCAN) {

            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if ((scanningResult != null) && (scanningResult.getContents() != null)) {
                final String scanned_barcode = scanningResult.getContents().trim();
                if (!InputUtils.isNull(scanned_barcode)) {
                    System.out.println("" + scanned_barcode);
                    CallGetPatientDetailAPI(scanned_barcode);
                } else {
                    Toast.makeText(mActivity, ConstantsMessages.RetryScanningbarcode, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void CallGetPatientDetailAPI(String scanned_barcode) {
        fetchData(scanned_barcode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void fetchData(String scanned_barcode) {

        GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<FetchOrderDetailsResponseModel> fetchOrderDetailsResponseModelCall = getAPIInterface.getOrderAllVisitDetails(appPreferenceManager.getLoginResponseModel().getUserID(), scanned_barcode);
        global.showProgressDialog(mActivity, getResources().getString(R.string.fetchingOrders), false);
        fetchOrderDetailsResponseModelCall.enqueue(new Callback<FetchOrderDetailsResponseModel>() {
            @Override
            public void onResponse(Call<FetchOrderDetailsResponseModel> call, Response<FetchOrderDetailsResponseModel> response) {
                global.hideProgressDialog(mActivity);
                try {
                    if (response.isSuccessful()) {

                        orderDetailsResponseModels = null;
                        orderDetailsResponseModels = new ArrayList<>();
                        FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel = response.body();

                        if (fetchOrderDetailsResponseModel != null && fetchOrderDetailsResponseModel.getOrderVisitDetails().size() > 0) {
                            for (OrderVisitDetailsModel orderVisitDetailsModel :
                                    fetchOrderDetailsResponseModel.getOrderVisitDetails()) {
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
                                    orderDetailsResponseModels.add(orderVisitDetailsModel);
                                    MessageLogger.LogError(TAG_FRAGMENT, "onResponse: " + orderDetailsResponseModels.size());
                                }
                            }
                        }
                        initData();
                    } else {
                        global.hideProgressDialog(mActivity);
                    }


                } catch (Exception e) {
                    global.hideProgressDialog(mActivity);
                    e.printStackTrace();
                    global.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<FetchOrderDetailsResponseModel> call, Throwable t) {
                global.hideProgressDialog(mActivity);
                global.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
            }
        });

    }

    private void initData() {
        if (orderDetailsResponseModels != null) {
            if (orderDetailsResponseModels.size() != 0) {
                ProceedToArriveScreen(orderDetailsResponseModels.get(0));
            }
        }
    }

    private void ProceedToArriveScreen(OrderVisitDetailsModel orderVisitDetailsModel) {
        try {
            BundleConstants.isKIOSKOrder = true;
            Intent intentNavigate = new Intent(mActivity, StartAndArriveActivity.class);
            intentNavigate.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
            mActivity.startActivity(intentNavigate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}