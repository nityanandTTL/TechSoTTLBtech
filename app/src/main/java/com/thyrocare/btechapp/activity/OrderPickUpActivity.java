package com.thyrocare.btechapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thyrocare.btechapp.Controller.BottomSheetController;
import com.thyrocare.btechapp.Controller.GetPickupOrderController;
import com.thyrocare.btechapp.Controller.PostPickupOrderController;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.adapter.PickupOrderAdapter;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.models.api.request.PickupOrderRequestModel;
import com.thyrocare.btechapp.models.api.request.PostPickupOrderRequestClass;
import com.thyrocare.btechapp.models.api.response.PickupOrderResponseModel;
import com.thyrocare.btechapp.models.api.response.PostPickupOrderResponseModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;
import static com.thyrocare.btechapp.utils.app.OtpListenerUtil.mActivity;

public class OrderPickUpActivity extends AppCompatActivity implements PickupOrderAdapter.OnItemClickListener {

    RecyclerView recycler_pickup;
    AppPreferenceManager appPreferenceManager;
    String Btechid;
    Global globalclass;
    TextView tv_noDatafound;
    TextView tv_toolbar;
    ImageView iv_back, iv_home;
    PickupOrderAdapter pickupOrderAdapter;
    private ConnectionDetector cd;

//    SwipeableButton swipeableButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pick_up);
        initView();
        listnere();
        callAPIPickupOrder();
    }

    private void listnere() {
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void callAPIPickupOrder() {
        if (cd.isConnectingToInternet()) {
            CallPickupOrderList();
        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }
    }

    private void CallPickupOrderList() {
        PickupOrderRequestModel orderRequestModel = new PickupOrderRequestModel();
        orderRequestModel.setBtechid(Btechid);

        GetPickupOrderController getPickupOrderController = new GetPickupOrderController(this);
        getPickupOrderController.PickupOrderAPI(orderRequestModel);

    }

    private void initView() {
        mActivity = OrderPickUpActivity.this;
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        recycler_pickup = findViewById(R.id.recycler_pickup);
        tv_noDatafound = findViewById(R.id.tv_noDatafound);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_toolbar.setText("Pickup Order");
        iv_back = findViewById(R.id.iv_back);
        iv_home = findViewById(R.id.iv_home);
        appPreferenceManager = new AppPreferenceManager(OrderPickUpActivity.this);
        Btechid = appPreferenceManager.getLoginResponseModel().getUserID();

//        swipeableButton = findViewById(R.id.swipe_button);

    }

    public void getPickupOrderList(PickupOrderResponseModel pickupOrderResponseModel) {
        if (!InputUtils.isNull(pickupOrderResponseModel)) {
            if (!InputUtils.isNull(pickupOrderResponseModel.getRespId()) && pickupOrderResponseModel.getRespId().equalsIgnoreCase("RES000")) {
                if (!InputUtils.isNull(pickupOrderResponseModel.getPickuporders())) {
                    pickupOrderAdapter = new PickupOrderAdapter(OrderPickUpActivity.this, pickupOrderResponseModel.getPickuporders(), OrderPickUpActivity.this);
                    recycler_pickup.setAdapter(pickupOrderAdapter);
                    pickupOrderAdapter.notifyDataSetChanged();
                }

            } else {
                tv_noDatafound.setVisibility(View.VISIBLE);
//                globalclass.showCustomToast(mActivity, pickupOrderResponseModel.getResponse(), Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public void onClientNameClicked(final PickupOrderResponseModel.PickupordersDTO pickupordersDTO) {
        String s = "Are you sure you want to pickup order?";
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);
        View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.logout_bottomsheet, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));
        TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
        tv_text.setText(s);
        Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    callPickupAPI(pickupordersDTO.getOrderNo(), Btechid);
                } else {
                    Toast.makeText(OrderPickUpActivity.this, CheckInternetConnectionMsg, Toast.LENGTH_SHORT).show();
                }
                bottomSheetDialog.dismiss();
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

    private void callPickupAPI(String orderNo, String btechid) {
        PostPickupOrderRequestClass pickupOrderRequestClass = new PostPickupOrderRequestClass();
        pickupOrderRequestClass.setOrderno(orderNo);
        pickupOrderRequestClass.setBtechid(btechid);
        PostPickupOrderController postPickupOrderController = new PostPickupOrderController(this);
        postPickupOrderController.PostPickupOrder(pickupOrderRequestClass);
    }

    public void getPostResponse(PostPickupOrderResponseModel body) {
        if (!InputUtils.isNull(body)) {
            if (!InputUtils.isNull(body.getRespId()) && body.getRespId().equalsIgnoreCase("RES000")) {
                Global.showCustomStaticToast(mActivity, body.getResponse());
                Intent intent = new Intent(OrderPickUpActivity.this, OrderPickUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Global.showCustomStaticToast(mActivity, body.getResponse());
            }
        }
    }

    public void getbottomsheetresponse(BottomSheetDialog bottomSheetDialog) {
        bottomSheetDialog.dismiss();
    }

    public void callPickup(BottomSheetDialog bottomSheetDialog, PickupOrderResponseModel.PickupordersDTO pickupordersDTO) {
        if (pickupordersDTO != null) {
            callPickupAPI(pickupordersDTO.getOrderNo(), Btechid);
            bottomSheetDialog.dismiss();
        }
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
    }
}