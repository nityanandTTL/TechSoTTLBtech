package com.thyrocare.adapter;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.dao.models.OrderDetailsDao;
import com.thyrocare.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.thyrocare.delegate.VisitOrderDisplayRecyclerViewAdapterDelegate;
import com.thyrocare.delegate.refreshDelegate;
import com.thyrocare.dialog.RescheduleOrderDialog;
import com.thyrocare.fragment.MaterialFragment;
import com.thyrocare.fragment.TSP_SendFragment;
import com.thyrocare.fragment.VisitOrdersDisplayFragment;
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
import com.thyrocare.utils.app.InputUtils;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static com.thyrocare.utils.api.NetworkUtils.isNetworkAvailable;

/**
 * Created by Orion on 4/27/2017.
 */

public class VisitOrderDisplayAdapter extends BaseAdapter {
    private HomeScreenActivity activity;
    private ArrayList<OrderVisitDetailsModel> orderVisitDetailsModelsArr;
    private VisitOrderDisplayRecyclerViewAdapterDelegate visitOrderDisplayRecyclerViewAdapterDelegate;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private LayoutInflater layoutInflater;
    private AppPreferenceManager appPreferenceManager;
    private SimpleDateFormat dateFormatbefore;
    private SimpleDateFormat dateFormat2;
    private refreshDelegate refreshDelegate1;
    Date date, strDate;
    String out;
    String in;
    private boolean isCancelRequesGenereted = false;

    private String MaskedPhoneNumber = "";
    private boolean isSelected;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private RescheduleOrderDialog cdd;
    private String userChoosenReleaseTask;
    private DhbDao dhbDao;
    private long difference;
    private int days;
    private int hours;
    private int min;

    //private refreshDelegate refreshDelegate;
    public VisitOrderDisplayAdapter(HomeScreenActivity activity, ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels, VisitOrderDisplayRecyclerViewAdapterDelegate visitOrderDisplayRecyclerViewAdapterDelegate, refreshDelegate refreshDelegate) {
        this.activity = activity;
        this.orderVisitDetailsModelsArr = orderDetailsResponseModels;
        this.visitOrderDisplayRecyclerViewAdapterDelegate = visitOrderDisplayRecyclerViewAdapterDelegate;
        this.refreshDelegate1 = refreshDelegate;
        layoutInflater = LayoutInflater.from(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        dhbDao = new DhbDao(activity);

    }


    @Override
    public int getCount() {
        return orderVisitDetailsModelsArr.size();
    }

    @Override
    public Object getItem(int position) {
        return orderVisitDetailsModelsArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FoldingCellViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_folding_cell, parent, false);
            holder = new FoldingCellViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (FoldingCellViewHolder) convertView.getTag();
        }
        initData(holder, position);
        initListeners(holder, position);
        return convertView;
    }

    private void initListeners(final FoldingCellViewHolder holder, final int pos) {


        holder.imgRelease2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Order Reschedule",
                        "Order Release"};
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
                            visitOrderDisplayRecyclerViewAdapterDelegate.onItemRelease(orderVisitDetailsModelsArr.get(pos));
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
        holder.imgRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Order Reschedule",
                        "Order Release"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Select Action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Order Reschedule")) {
                            userChoosenReleaseTask = "Order Reschedule";
                            cdd = new RescheduleOrderDialog(activity, new OrderRescheduleDialogButtonClickedDelegateResult(), orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0));
                            cdd.show();
                        } else if (items[item].equals("Order Release")) {
                            visitOrderDisplayRecyclerViewAdapterDelegate.onItemRelease(orderVisitDetailsModelsArr.get(pos));
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
        holder.btnStartNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Date().before(strDate)) {
                    //  Toast.makeText(activity, "is After", Toast.LENGTH_SHORT).show();
                    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                    alertDialog.setMessage("Appointment time of this order is at " + orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + orderVisitDetailsModelsArr.get(pos).getSlot() + "Still do you want to start");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    assigned();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                    alertDialog.show();
                } else {
                    //Toast.makeText(activity, "is before", Toast.LENGTH_SHORT).show();

                    assigned();
                }
             /*   if (new Date().after(strDate)) {
                    Toast.makeText(activity, "is After", Toast.LENGTH_SHORT).show();

                    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                    alertDialog.setMessage("Appointment time of this order is at "+orderVisitDetailsModelsArr.get(pos).getAppointmentDate()+" "+orderVisitDetailsModelsArr.get(pos).getSlot()+"Still do you want to start");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    assigned();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                    alertDialog.show();
                    //catalog_outdated = 1;
                }else {
                    Toast.makeText(activity, "is Before", Toast.LENGTH_SHORT).show();
                    assigned();
                }*/


            }

            private void assigned() {

                Logger.error("status***" + orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus());

                if (!orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED") && !orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment")) {

                    visitOrderDisplayRecyclerViewAdapterDelegate.onNavigationStart(orderVisitDetailsModelsArr.get(pos));
                } else {
                    Toast.makeText(activity, "Please accept the order first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos == 0) {
                    if (!orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED") && !orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment")) {

                        registerToggle(pos);
                        holder.cell.toggle(false);
                        initData(holder, pos);
                    } else {
                        Toast.makeText(activity, "Please accept the order first", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Please service the earlier orders first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData(FoldingCellViewHolder holder, final int pos) {

        if (orderVisitDetailsModelsArr.size() > pos
                && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().size() > 0
                && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().size() > 0
                && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0) != null) {
            holder.tvAge.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getAge() + " Y | " + orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getGender());

            holder.tvName.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getName());
            holder.tvName.setSelected(true);
            holder.tvSrNo.setText(pos + 1 + "");
            holder.txtorderno.setSelected(true);

            Logger.debug("apptDate" + String.valueOf(orderVisitDetailsModelsArr.get(pos).getVisitId() + orderVisitDetailsModelsArr.get(pos).getAppointmentDate()));

            if (!InputUtils.isNull(orderVisitDetailsModelsArr.get(pos).getAppointmentDate())) {
                holder.apptDateValue.setVisibility(View.VISIBLE);
                holder.apptDate.setVisibility(View.VISIBLE);
                holder.apptDateValue.setText(orderVisitDetailsModelsArr.get(pos).getAppointmentDate());
            } else {
                holder.apptDateValue.setVisibility(View.INVISIBLE);
                holder.apptDate.setVisibility(View.INVISIBLE);
            }

            holder.pindata.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getPincode());
            holder.locationdata.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getLocation());
            holder.timedata.setText(orderVisitDetailsModelsArr.get(pos).getSlot() + "  HRS");
            //jai
            //minus 30 min
            String myTime = orderVisitDetailsModelsArr.get(pos).getSlot();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            Date d = null;
            try {
                d = df.parse(myTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, -30);
            String newTime = df.format(cal.getTime());
            //  Toast.makeText(activity, "newTime "+newTime, Toast.LENGTH_SHORT).show();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            strDate = null;
            try {
                // strDate = sdf.parse("18-08-2017 19:30");//click
             /*  Date strDate1 = sdf.parse("18-08-2017 06:00");//oncreate

                if (new Date().before(strDate1)){
                    Toast.makeText(activity, "strDate1 is After", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, "strDate1 is before", Toast.LENGTH_SHORT).show();
                }*/
                strDate = sdf.parse("" + orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + newTime);
                //strDate = sdf.parse(""+orderVisitDetailsModelsArr.get(pos).getAppointmentDate()+" "+orderVisitDetailsModelsArr.get(pos).getSlot());
            } catch (ParseException e) {
                e.printStackTrace();
            }


            //jai

         /*   SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String time1 = orderVisitDetailsModelsArr.get(pos).getSlot();
            String time2 = "06:30";//format.format(new Date());
            Logger.error("time2: " + time2);

            Date date1 = null;
            try {
                date1 = format.parse(time1);
                Logger.error("date1: " + date1.getTime());
                Date date2 = format.parse(time2);
                Logger.error("date2: " + date2.getTime());
                difference = date2.getTime() - date1.getTime();
                days = (int) (difference / (1000 * 60 * 60 * 24));
                hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                hours = (hours < 0 ? -hours : hours);
                Log.i("======= days", " :: " + days);
                Log.i("======= Hours", " :: " + hours);
                Log.i("======= Min", " :: " + min);
                Logger.error("difference " + difference);
            } catch (ParseException e) {
                e.printStackTrace();
            }*/


            holder.txtorderno.setText(orderVisitDetailsModelsArr.get(pos).getVisitId());
            holder.txtAge.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getAge() + " Y | " + orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getGender());
            holder.txtName.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getName());
            holder.txtName.setSelected(true);
            holder.txtSrNo.setText(pos + 1 + "");
            Logger.error("LOcationnnn" + orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getLocation());

            holder.txt_distance.setText(String.valueOf(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getDistance()) + "KM");
            holder.txtKits.setText(String.valueOf(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getKits().get(0).getKit()));
            holder.txtAddress.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getAddress());
//            holder.tvAadharNo.setVisibility(View.GONE);
            holder.txtAadharNo.setVisibility(View.GONE);
            if (pos != 0) {
                holder.btnStartNavigation.setVisibility(View.VISIBLE);
            }
            if (unfoldedIndexes.contains(pos)) {
                holder.cell.unfold(true);
            } else {
                holder.cell.fold(true);
            }
            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment") || orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equals("ASSIGNED")) {
                holder.imgCBAccept.setVisibility(View.VISIBLE);

                holder.txtSrNo.setVisibility(View.INVISIBLE);
                holder.imgcall.setVisibility(View.GONE);
                holder.imgRelease2.setVisibility(View.INVISIBLE);
                holder.imgCBAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        visitOrderDisplayRecyclerViewAdapterDelegate.onOrderAccepted(orderVisitDetailsModelsArr.get(pos));
                    }
                });
            } else {
                holder.imgCBAccept.setVisibility(View.INVISIBLE);
                holder.imgcall.setVisibility(View.GONE);
                holder.txtSrNo.setVisibility(View.VISIBLE);
                holder.imgRelease2.setVisibility(View.VISIBLE);
            }


            holder.imgcall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    visitOrderDisplayRecyclerViewAdapterDelegate.onCallCustomer(orderVisitDetailsModelsArr.get(pos));
                }
            });
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
            if (isFasting && isNonFasting) {
                holder.imgFastingStatus.setVisibility(View.VISIBLE);
                holder.imgFastingStatus.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_fasting_mix));
            } else if (isFasting && !isNonFasting) {
                holder.imgFastingStatus.setVisibility(View.VISIBLE);
                holder.imgFastingStatus.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_fasting));
            } else if (!isFasting && isNonFasting) {
                holder.imgFastingStatus.setVisibility(View.VISIBLE);
                holder.imgFastingStatus.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_non_fasting));
            } else {
                holder.imgFastingStatus.setVisibility(View.INVISIBLE);
            }
            holder.imgFastingStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Fasting Details")
                            .setItems(benFastingDetails.toArray(new String[benFastingDetails.size()]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });
        }
    }

    class CallPatchRequestAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    MaskedPhoneNumber = json;
                    intent.setData(Uri.parse("tel:" + MaskedPhoneNumber));
                    Logger.error("MaskedPhoneNumber" + MaskedPhoneNumber);

                    appPreferenceManager.setMaskNumber(MaskedPhoneNumber);
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{
                                        Manifest.permission.CALL_PHONE},
                                AppConstants.APP_PERMISSIONS);
                    } else {
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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


    private class FoldingCellViewHolder {
        TextView tvSrNo, tvName, tvAge, txtAddress, txtorderno, txtKits, timedata, timetitle, txt_distance, pintitle, pindata, apptDate, apptDateValue, locationtitle, locationdata;//tvAadharNo,
        ImageView imgCBAccept;
        TextView txtSrNo, txtName, txtAge, txtAadharNo;
        ImageView imgRelease, imgRelease2, imgcall;
        ImageView imgFastingStatus;
        Button btnStartNavigation;
        FoldingCell cell;

        FoldingCellViewHolder(View itemView) {
            initUI(itemView);
        }

        private void initUI(View itemView) {
            imgFastingStatus = (ImageView) itemView.findViewById(R.id.title_fasting);
            imgcall = (ImageView) itemView.findViewById(R.id.call);
            locationtitle = (TextView) itemView.findViewById(R.id.location_title);
            locationdata = (TextView) itemView.findViewById(R.id.location_datas);
            pintitle = (TextView) itemView.findViewById(R.id.pincode_title);
            pindata = (TextView) itemView.findViewById(R.id.pincode_data);
            cell = (FoldingCell) itemView.findViewById(R.id.item_folding_cell);
            tvSrNo = (TextView) itemView.findViewById(R.id.tv_sr_no);
            timedata = (TextView) itemView.findViewById(R.id.time_data);
            timetitle = (TextView) itemView.findViewById(R.id.time_title);
            txtSrNo = (TextView) itemView.findViewById(R.id.txt_sr_no);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtAge = (TextView) itemView.findViewById(R.id.txt_age);
            txtAadharNo = (TextView) itemView.findViewById(R.id.txt_aadhar_no);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAge = (TextView) itemView.findViewById(R.id.tv_age);
//            tvAadharNo = (TextView) itemView.findViewById(R.id.tv_aadhar_no);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            txtorderno = (TextView) itemView.findViewById(R.id.tv_orderno);
            txtKits = (TextView) itemView.findViewById(R.id.txt_num_kits);
            imgRelease = (ImageView) itemView.findViewById(R.id.img_release);
            btnStartNavigation = (Button) itemView.findViewById(R.id.btn_start_navigation);
            imgCBAccept = (ImageView) itemView.findViewById(R.id.img_oas);
            imgRelease2 = (ImageView) itemView.findViewById(R.id.img_release2);
            txt_distance = (TextView) itemView.findViewById(R.id.txt_distance_1);

            //change22june2017
            apptDateValue = (TextView) itemView.findViewById(R.id.apptdateValue);
            apptDate = (TextView) itemView.findViewById(R.id.apptdate);
            //change22june2017

            timedata.setVisibility(View.VISIBLE);
            timetitle.setVisibility(View.VISIBLE);
            locationtitle.setVisibility(View.VISIBLE);
            locationdata.setVisibility(View.VISIBLE);
            pintitle.setVisibility(View.VISIBLE);
            pindata.setVisibility(View.VISIBLE);
        }
    }

    // simple methods for register cell state changes
    private void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    private void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    private void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

}










