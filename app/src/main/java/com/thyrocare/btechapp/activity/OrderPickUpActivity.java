package com.thyrocare.btechapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
    private ConnectionDetector cd;
    String Btechid;
    Global globalclass;

    PickupOrderAdapter pickupOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pick_up);

        setTitle("Pickup Order");

        initView();
        callAPIPickupOrder();
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
        appPreferenceManager = new AppPreferenceManager(OrderPickUpActivity.this);
        Btechid = appPreferenceManager.getLoginResponseModel().getUserID();
    }

    public void getPickupOrderList(PickupOrderResponseModel pickupOrderResponseModel) {
        if (!InputUtils.isNull(pickupOrderResponseModel)) {
            if (!InputUtils.isNull(pickupOrderResponseModel.getRespId()) && pickupOrderResponseModel.getRespId().equalsIgnoreCase("RES000")) {
                if (!InputUtils.isNull(pickupOrderResponseModel.getPickuporders())) {
                    pickupOrderAdapter = new PickupOrderAdapter(OrderPickUpActivity.this, pickupOrderResponseModel.getPickuporders(), OrderPickUpActivity.this);
                    recycler_pickup.setAdapter(pickupOrderAdapter);
                    pickupOrderAdapter.notifyDataSetChanged();
                }

            }
        }
    }

    @Override
    public void onClientNameClicked(final PickupOrderResponseModel.PickupordersDTO pickupordersDTO) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Are you sure you want to pickup order?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cd.isConnectingToInternet()) {
                            callPickupAPI(pickupordersDTO.getOrderNo(), Btechid);
                        } else {
                            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

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
}