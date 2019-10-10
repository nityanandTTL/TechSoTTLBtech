package com.thyrocare.adapter.tsp;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.dao.models.OrderDetailsDao;
import com.thyrocare.delegate.OrderPassRecyclerViewAdapterDelegate;
import com.thyrocare.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.thyrocare.delegate.VisitOrderDisplayRecyclerViewAdapterDelegate;
import com.thyrocare.delegate.VisitOrderDisplayyRecyclerViewAdapterDelegate;
import com.thyrocare.delegate.refreshDelegate;
import com.thyrocare.dialog.RescheduleOrderDialog;
import com.thyrocare.models.api.request.OrderAllocationTrackLocationRequestModel;
import com.thyrocare.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.models.data.BeneficiaryDetailsModel;
import com.thyrocare.models.data.OrderDetailsModel;
import com.thyrocare.models.data.OrderVisitDetailsModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.DateUtils;
import com.thyrocare.utils.app.GPSTracker;
import com.thyrocare.utils.app.InputUtils;

import org.joda.time.DateTimeComparator;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.thyrocare.utils.api.NetworkUtils.isNetworkAvailable;

/**
 * Created by Orion on 4/27/2017.
 */

public class Tsp_OrderDisplayAdapter_new extends RecyclerView.Adapter<Tsp_OrderDisplayAdapter_new.MyViewHolder> {

    private final HomeScreenActivity activity;
    private final VisitOrderDisplayRecyclerViewAdapterDelegate visitOrderDisplayRecyclerViewAdapterDelegate;
    private final VisitOrderDisplayyRecyclerViewAdapterDelegate visitOrderDisplayyRecyclerViewAdapterDelegate;
    private final OrderPassRecyclerViewAdapterDelegate orderPassRecyclerViewAdapterDelegate;
    private final refreshDelegate refreshDelegate1;
    private final LayoutInflater layoutInflater;
    private final AppPreferenceManager appPreferenceManager;
    private final DhbDao dhbDao;
    private final String todate;
    ArrayList<OrderVisitDetailsModel> orderVisitDetailsModelsArr;
    private int fastingFlagInt;
    CharSequence[] items;
    private String userChoosenReleaseTask;
    private RescheduleOrderDialog cdd;
    private boolean isCancelRequesGenereted = false;

    public Tsp_OrderDisplayAdapter_new(HomeScreenActivity activity, ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels, VisitOrderDisplayRecyclerViewAdapterDelegate visitOrderDisplayRecyclerViewAdapterDelegate, refreshDelegate refreshDelegate, VisitOrderDisplayyRecyclerViewAdapterDelegate visitOrderDisplayyRecyclerViewAdapterDelegate, OrderPassRecyclerViewAdapterDelegate orderPassRecyclerViewAdapterDelegate) {
        this.activity = activity;
        this.orderVisitDetailsModelsArr = orderDetailsResponseModels;
        this.visitOrderDisplayRecyclerViewAdapterDelegate = visitOrderDisplayRecyclerViewAdapterDelegate;
        this.visitOrderDisplayyRecyclerViewAdapterDelegate = visitOrderDisplayyRecyclerViewAdapterDelegate;
        this.orderPassRecyclerViewAdapterDelegate = orderPassRecyclerViewAdapterDelegate;
        this.refreshDelegate1 = refreshDelegate;
        layoutInflater = LayoutInflater.from(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        dhbDao = new DhbDao(activity);
        todate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_age, tv_orderno, apptdateValue, time_data, location_datas, pincode_data,tv_btech_name;
        TextView txtSrNo;
        ImageView imgCBAccept, imgcall, imgFastingStatus, imgRelease2;
        LinearLayout mainleft, mainright, main;

        public MyViewHolder(View view) {
            super(view);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_age = (TextView) view.findViewById(R.id.txt_age);
            tv_orderno = (TextView) view.findViewById(R.id.tv_orderno);
            apptdateValue = (TextView) view.findViewById(R.id.apptdateValue);
            time_data = (TextView) view.findViewById(R.id.time_data);
            location_datas = (TextView) view.findViewById(R.id.location_datas);
            pincode_data = (TextView) view.findViewById(R.id.pincode_data);
            tv_btech_name=(TextView)view.findViewById(R.id.tv_btech_name);
            txtSrNo = (TextView) itemView.findViewById(R.id.txt_sr_no);

            imgCBAccept = (ImageView) itemView.findViewById(R.id.img_oas);
            imgcall = (ImageView) itemView.findViewById(R.id.call);
            imgFastingStatus = (ImageView) itemView.findViewById(R.id.title_fasting);
            imgRelease2 = (ImageView) itemView.findViewById(R.id.img_release2);

            mainleft = (LinearLayout) itemView.findViewById(R.id.mainleft);
            mainright = (LinearLayout) itemView.findViewById(R.id.mainright);
            main = (LinearLayout) itemView.findViewById(R.id.main);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tsp_order_row_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {

        if (orderVisitDetailsModelsArr.size() > pos
                && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().size() > 0
                && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().size() > 0
                && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0) != null) {
            Logger.error("allloy bua");
            if (orderVisitDetailsModelsArr.get(pos).getAppointmentDate().equals(todate)) {
                Logger.error("allloy bua1");
                holder.mainright.setBackgroundColor(activity.getResources().getColor(R.color.test1));
                holder.mainleft.setBackgroundColor(activity.getResources().getColor(R.color.test1));
                holder.main.setBackgroundColor(activity.getResources().getColor(R.color.test1));

            } else {
                Logger.error("allloy bua2");
                holder.mainright.setBackgroundColor(activity.getResources().getColor(R.color.test2));
                holder.mainleft.setBackgroundColor(activity.getResources().getColor(R.color.test2));
                holder.main.setBackgroundColor(activity.getResources().getColor(R.color.test2));
            }
            //btech name
            holder.tv_btech_name.setText(orderVisitDetailsModelsArr.get(pos).getBtechName());
            holder.txt_name.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getName());
            holder.txt_age.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getAge() + " Y | " + orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getGender());
            holder.tv_orderno.setText(orderVisitDetailsModelsArr.get(pos).getVisitId());
            holder.apptdateValue.setText(orderVisitDetailsModelsArr.get(pos).getAppointmentDate());
            holder.time_data.setText(orderVisitDetailsModelsArr.get(pos).getSlot() + "  HRS");
            holder.location_datas.setSelected(true);
            holder.location_datas.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getAddress());
            holder.pincode_data.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getPincode());
            holder.txtSrNo.setText(pos + 1 + "");
            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment") || orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED")) {
                fastingFlagInt = 0;
                holder.imgCBAccept.setVisibility(View.VISIBLE);
                holder.txt_name.setVisibility(View.INVISIBLE);
                holder.tv_btech_name.setVisibility(View.VISIBLE);
                holder.txt_age.setVisibility(View.INVISIBLE);
                holder.imgcall.setVisibility(View.INVISIBLE);
                holder.imgFastingStatus.setVisibility(View.INVISIBLE);
                holder.txtSrNo.setVisibility(View.GONE);
            } else {
                fastingFlagInt = 1;
                holder.imgFastingStatus.setVisibility(View.VISIBLE);
                holder.imgCBAccept.setVisibility(View.INVISIBLE);
                holder.txt_name.setVisibility(View.VISIBLE);
                holder.tv_btech_name.setVisibility(View.VISIBLE);
                holder.txt_age.setVisibility(View.VISIBLE);
                holder.imgcall.setVisibility(View.VISIBLE);
                holder.txtSrNo.setVisibility(View.VISIBLE);
            }

            boolean isFasting = false;
            boolean isNonFasting = false;

            final ArrayList<String> benFastingDetails = new ArrayList<>();
            for (OrderDetailsModel odm :
                    orderVisitDetailsModelsArr.get(pos).getAllOrderdetails()) {
                for (BeneficiaryDetailsModel bdm :
                        odm.getBenMaster()) {
                    if (bdm.getFasting().equalsIgnoreCase("Fasting")) {
                        isFasting = true;
                    } else if (bdm.getFasting().equalsIgnoreCase("Non-Fasting")) {
                        isNonFasting = true;
                    }
                    benFastingDetails.add("" + bdm.getName() + " : " + bdm.getFasting());
                }
            }

            if (fastingFlagInt == 1) {
                fastingFlagInt = 0;
                if (isFasting && isNonFasting) {
                    Logger.error("isFasting && isNonFasting");
                    holder.imgFastingStatus.setVisibility(View.VISIBLE);
                    holder.imgFastingStatus.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_fasting_mix));
                } else if (isFasting && !isNonFasting) {
                    Logger.error("isFasting && !isNonFasting");
                    holder.imgFastingStatus.setVisibility(View.VISIBLE);
                    holder.imgFastingStatus.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_fasting));
                } else if (!isFasting && isNonFasting) {
                    Logger.error("!isFasting && isNonFasting");
                    holder.imgFastingStatus.setVisibility(View.VISIBLE);
                    holder.imgFastingStatus.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_non_fasting));
                } else {
                    Logger.error("else");
                    holder.imgFastingStatus.setVisibility(View.INVISIBLE);
                }
            }

            holder.imgCBAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                    builder1.setTitle("Warning ")
                            .setMessage("Do you Want to accept order?").setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fastingFlagInt = 1;
                            holder.txt_name.setVisibility(View.VISIBLE);
                            holder.tv_btech_name.setVisibility(View.VISIBLE);
                            holder.txt_age.setVisibility(View.VISIBLE);
                            holder.imgFastingStatus.setVisibility(View.VISIBLE);
                            holder.imgcall.setVisibility(View.VISIBLE);

                            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.PPBS)
                                    && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.FBS)) {
                                if (checkIfBefore11()) {
                                    holder.imgRelease2.setVisibility(View.GONE);
                                }
                            } else {
                                holder.imgRelease2.setVisibility(View.VISIBLE);
                            }
                            visitOrderDisplayRecyclerViewAdapterDelegate.onOrderAccepted(orderVisitDetailsModelsArr.get(pos));
                            SendinglatlongOrderAllocation(pos);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder1.show();
                }
            });

            holder.imgcall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    visitOrderDisplayRecyclerViewAdapterDelegate.onCallCustomer(orderVisitDetailsModelsArr.get(pos));
                }
            });

            holder.imgRelease2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    boolean toShowResheduleOption = false;
                    if (!InputUtils.isNull(orderVisitDetailsModelsArr.get(pos).getAppointmentDate())) {
                        Date DeviceDate = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        Date AppointDate  = DateUtils.dateFromString(orderVisitDetailsModelsArr.get(pos).getAppointmentDate(),format);
                        int daycount = DateTimeComparator.getDateOnlyInstance().compare(AppointDate, DeviceDate);
                        if (daycount == 0){
                            toShowResheduleOption = true;
                        }else {
                            toShowResheduleOption = false;
                        }
                    }

                    if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment") || orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED")) {

                   /* items = new String[]{"Order Reschedule",
                            "Order Release", "Order Pass"};*/

                        if (toShowResheduleOption){
                            items = new String[]{"Order Reschedule", "Order Release"};
                        }else{
                            items = new String[]{"Order Release"};
                        }



//                        Toast.makeText(activity, "Reschedule,Release,Order Pass", Toast.LENGTH_SHORT).show();
                    } else {

                        if (toShowResheduleOption){
                            items = new String[]{"Order Reschedule", "Request Release"};
                        }else{
                            items = new String[]{"Request Release"};
                        }

//                        Toast.makeText(activity, "Reschedule,Request,order  pass", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(activity, "mI aLLO1", Toast.LENGTH_SHORT).show();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Select Action");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Order Reschedule")) {


                                Logger.error("Order Reschedule");
                                userChoosenReleaseTask = "Order Reschedule";
                                cdd = new RescheduleOrderDialog(activity, new OrderRescheduleDialogButtonClickedDelegateResult(), orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0));
                                cdd.show();


                            } else if (items[item].equals("Order Release")) {


                                Logger.error(" Order Release dialog");

                                final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                                builder1.setTitle("Warning ")
                                        .setMessage("Rs 200 debit will be levied for Releasing this Order ").setPositiveButton("Accept Debit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        visitOrderDisplayRecyclerViewAdapterDelegate.onItemRelease(orderVisitDetailsModelsArr.get(pos));
                                    }
                                }).setNegativeButton("Cancel Request", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder1.show();


                            } else if (items[item].equals("Request Release")) {

                                final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                                builder1.setTitle("Warning ")
                                        .setMessage("Rs 200 debit will be levied for Releasing this Order ").setPositiveButton("Accept Debit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        visitOrderDisplayyRecyclerViewAdapterDelegate.onItemRelease(orderVisitDetailsModelsArr.get(pos));
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
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }
    }

    private void SendinglatlongOrderAllocation(int pos) {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        OrderAllocationTrackLocationRequestModel orderAllocationTrackLocationRequestModel = new OrderAllocationTrackLocationRequestModel();

        orderAllocationTrackLocationRequestModel.setVisitId(orderVisitDetailsModelsArr.get(pos).getVisitId());
        orderAllocationTrackLocationRequestModel.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
        orderAllocationTrackLocationRequestModel.setStatus(8);
        //Latlong added
        GPSTracker gpsTracker = new GPSTracker(activity);
        if (gpsTracker.canGetLocation()){
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

    private boolean checkIfBefore11() {
        Calendar cal = Calendar.getInstance(); //Create Calendar-Object
        cal.setTime(new Date());               //Set the Calendar to now
        int hour = cal.get(Calendar.HOUR_OF_DAY); //Get the hour from the calendar
        if (hour <= 11)              // Check if hour is between 8 am and 11pm
        {
            return true;   // do whatever you want
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return orderVisitDetailsModelsArr.size();
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

    private class OrderRescheduleDialogButtonClickedDelegateResult implements OrderRescheduleDialogButtonClickedDelegate {

        @Override
        public void onOkButtonClicked(OrderDetailsModel orderDetailsModel, String remark, String date) {
            Logger.error("onOkButtonClicked");
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();

            orderStatusChangeRequestModel.setId(orderDetailsModel.getSlotId() + "");
            orderStatusChangeRequestModel.setRemarks(remark);
            orderStatusChangeRequestModel.setStatus(11);
            orderStatusChangeRequestModel.setAppointmentDate(date);

            ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderStatusChangeRequestAsyncTask(orderStatusChangeRequestModel);
            orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderStatusChangeApiAsyncTaskDelegateResult(orderDetailsModel));
            if (isNetworkAvailable(activity)) {
                orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);

            } else {
                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelButtonClicked() {

        }
    }

    private class OrderStatusChangeApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        OrderDetailsModel orderDetailsModel;

        public OrderStatusChangeApiAsyncTaskDelegateResult(OrderDetailsModel orderDetailsModel) {
            this.orderDetailsModel = orderDetailsModel;
        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Logger.error("200 success");
                Logger.error("before refresh");
                refreshDelegate1.onRefreshClicked();
                Logger.error("after refresh");
                if (userChoosenReleaseTask.equals("Visit Cancellation")) {
                    Logger.error("success 1");
                    if (!isCancelRequesGenereted) {
                        Logger.error("success 2");
                        isCancelRequesGenereted = true;
                        orderDetailsModel.setStatus("RESCHEDULED");
                        OrderDetailsDao orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
                        orderDetailsDao.insertOrUpdate(orderDetailsModel);
                        Toast.makeText(activity, "Order rescheduled successfully", Toast.LENGTH_SHORT).show();
                        Logger.error("before refresh");
                        refreshDelegate1.onRefreshClicked();
                        Logger.error("after refresh");

                        activity.finish();

                    }
                }
            } else {
                Logger.error("success 11");
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}










