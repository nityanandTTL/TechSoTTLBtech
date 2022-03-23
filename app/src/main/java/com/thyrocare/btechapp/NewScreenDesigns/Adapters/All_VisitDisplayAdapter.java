package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.Controller.BottomSheetController;
import com.thyrocare.btechapp.Controller.SendLatLongforOrderController;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.B2BVisitOrdersDisplayFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.VisitOrdersDisplayFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.DateUtil;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.thyrocare.btechapp.delegate.refreshDelegate;
import com.thyrocare.btechapp.dialog.RescheduleOrderDialog;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.ServiceUpdateRequestModel;
import com.thyrocare.btechapp.models.api.response.GetOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.KitsCountModel;
import com.thyrocare.btechapp.models.data.OrderDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.network.MyBroadcastReceiver;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.DateUtils;
import com.thyrocare.btechapp.utils.app.GPSTracker;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import org.joda.time.DateTimeComparator;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import application.ApplicationController;
import cheekiat.slideview.OnFinishListener;
import cheekiat.slideview.SlideView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class All_VisitDisplayAdapter extends RecyclerView.Adapter<All_VisitDisplayAdapter.MyViewHolder> {
    private static final String TAG = All_VisitDisplayAdapter.class.getSimpleName();
    private Activity activity;
    private final String current_date;
    GetOrderDetailsResponseModel orderDetailsResponseModel;
    private ArrayList<GetOrderDetailsResponseModel.GetVisitcountDTO> orderVisitDetailsModelsArr;
    private AppPreferenceManager appPreferenceManager;
    B2BVisitOrdersDisplayFragment mB2BVisitOrdersDisplayFragment;

    public All_VisitDisplayAdapter(B2BVisitOrdersDisplayFragment visitOrdersDisplayFragment_new, GetOrderDetailsResponseModel orderDetailsResponseModels, Activity activity) {
        this.activity = activity;
        this.orderVisitDetailsModelsArr = orderDetailsResponseModels.getGetVisitcount();
        this.orderDetailsResponseModel = orderDetailsResponseModels;
        appPreferenceManager = new AppPreferenceManager(this.activity);
        mB2BVisitOrdersDisplayFragment = visitOrdersDisplayFragment_new;
        current_date = DateUtil.getDateFromLong(System.currentTimeMillis(), "dd-MM-yyyy");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_visit_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int pos) {
        holder.txtOrderNo.setText(orderVisitDetailsModelsArr.get(pos).getVisitId());

        holder.txtDate.setText(orderVisitDetailsModelsArr.get(pos).getAppointmentDate());
        holder.txtCustomerName.setText(Global.toCamelCase(orderVisitDetailsModelsArr.get(pos).getName()));
        if (InputUtils.CheckEqualIgnoreCase(orderVisitDetailsModelsArr.get(pos).getStatus(), BundleConstants.ACCEPTED) ||
                !InputUtils.CheckEqualIgnoreCase(orderVisitDetailsModelsArr.get(pos).getStatus(), "fix appointment") ||
                !InputUtils.CheckEqualIgnoreCase(orderVisitDetailsModelsArr.get(pos).getStatus(), "ASSIGNED")) {
            holder.txtStatus.setText("Status : " + orderVisitDetailsModelsArr.get(pos).getStatus());
        } else {
            holder.txtStatus.setVisibility(View.GONE);
        }
        holder.txtDate.setText(DateUtils.Req_Date_Req(orderVisitDetailsModelsArr.get(pos).getAppointmentDate(), "dd-MM-yyyy hh:mm a", "dd-MM-yyyy HH:mm"));

        if (DateUtils.Req_Date_Req(orderVisitDetailsModelsArr.get(pos).getAppointmentDate(), "dd-MM-yyyy hh:mm a", "dd-MM-yyyy").equals(DateUtils.Req_Date_Req(orderDetailsResponseModel.getCurrentDatetime(), "dd-MM-yyyy hh:mm a", "dd-MM-yyyy"))) {
            holder.layoutMain.setBackgroundResource(R.drawable.rounded_background_green);
        } else {
            holder.layoutMain.setBackgroundResource(R.drawable.rounded_background_yellow);
        }
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BundleConstants.setSelectedOrder = orderVisitDetailsModelsArr.get(pos).getVisitId();
                mB2BVisitOrdersDisplayFragment.orderSelected(pos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderVisitDetailsModelsArr.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutMain;
        TextView txtCustomerName, txtOrderNo, txtDate, txtSlot, txtStatus;

        public MyViewHolder(View view) {
            super(view);

            txtOrderNo = (TextView) view.findViewById(R.id.txtOrderNo);
            layoutMain = (LinearLayout) view.findViewById(R.id.layoutMain);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtSlot = (TextView) view.findViewById(R.id.txtSlot);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
        }
    }
}
