package com.thyrocare.adapter.tsp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;
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
import com.thyrocare.fragment.VisitOrdersDisplayFragment;
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
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import static com.thyrocare.utils.api.NetworkUtils.isNetworkAvailable;

/**
 * Created by Orion on 4/27/2017.
 */

public class Tsp_OrderDisplayAdapter extends BaseAdapter {
    private HomeScreenActivity activity;
    private ArrayList<OrderVisitDetailsModel> orderVisitDetailsModelsArr;
    private ArrayList<OrderDetailsModel> allOrderdetailsesarr;
    private VisitOrderDisplayRecyclerViewAdapterDelegate visitOrderDisplayRecyclerViewAdapterDelegate;
    private VisitOrderDisplayyRecyclerViewAdapterDelegate visitOrderDisplayyRecyclerViewAdapterDelegate;
    private OrderPassRecyclerViewAdapterDelegate orderPassRecyclerViewAdapterDelegate;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private LayoutInflater layoutInflater;
    private AppPreferenceManager appPreferenceManager;
    private SimpleDateFormat dateFormatbefore;
    private SimpleDateFormat dateFormat2;
    private String apiPlusFif, apiMinusFif;
    String newTimeAfterMinusSixty1;
    private refreshDelegate refreshDelegate1;
    Date date, strDate;
    int fastingFlagInt = 0;
    String apiTime;
    Date apitimeinHHMMFormat;
    String out;
    String in;
    boolean isAutoTimeSelected = false;
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
    private String todate = "";
    CharSequence[] items;
    private String test;
    private Date strDate2;
    private String apiMinusFifdisp, apiPlusFifdisp, apiPlusTwoPBBS;
    private String Test;
    private String apiPlusTwoPBBS2;
    private String Test2;
    private int apihours;
    private int apiminutes;
    private Date strDate3;

    //private refreshDelegate refreshDelegate;
    public Tsp_OrderDisplayAdapter(HomeScreenActivity activity, ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels, VisitOrderDisplayRecyclerViewAdapterDelegate visitOrderDisplayRecyclerViewAdapterDelegate, refreshDelegate refreshDelegate, VisitOrderDisplayyRecyclerViewAdapterDelegate visitOrderDisplayyRecyclerViewAdapterDelegate, OrderPassRecyclerViewAdapterDelegate orderPassRecyclerViewAdapterDelegate) {
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
            convertView = layoutInflater.inflate(R.layout.tsp_order_row_layout, parent, false);
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

                if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment") || orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED")) {

                    items = new String[]{"Order Reschedule",
                            "Order Release", "Order Pass"};
                    Toast.makeText(activity, "Reschedule,Release,Order Pass", Toast.LENGTH_SHORT).show();
                } else {

                    items = new String[]{"Order Reschedule",
                            "Request Release", "Order Pass"};
                    Toast.makeText(activity, "Reschedule,Request,order  pass", Toast.LENGTH_SHORT).show();
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


                        } else if (items[item].equals("Order Pass")) {

                            orderPassRecyclerViewAdapterDelegate.onItemReleaseto(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getPincode(), orderVisitDetailsModelsArr.get(pos));

                            Logger.error("pincode " + orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getPincode());

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
                if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment") || orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED")) {

                    items = new String[]{"Order Reschedule",
                            "Order Release"};
                    Toast.makeText(activity, "Reschedule,Release", Toast.LENGTH_SHORT).show();
                } else {

                    items = new String[]{"Order Reschedule",
                            "Request Release"};
                    Toast.makeText(activity, "Reschedule,Request", Toast.LENGTH_SHORT).show();


                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Select Action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Order Reschedule")) {


                            userChoosenReleaseTask = "Order Reschedule";
                            cdd = new RescheduleOrderDialog(activity, new OrderRescheduleDialogButtonClickedDelegateResult(), orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0));
                            cdd.show();


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
        holder.btnStartNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().equals(AppConstants.PPBS)|| orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().equals(AppConstants.INSPP)) {
                    isAutoTimeSelected();
                    if (isAutoTimeSelected == true) {
                        if (timeCheckPPBS(pos)) {
                            //true
                            goAheadWithNormalFlow();
                        } else {
                            Toast.makeText(activity, orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase()+" test can be served in between ", Toast.LENGTH_SHORT).show();

                            AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                            alertDialog.setMessage(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase()+" test can be served in between " + apiMinusFifdisp + " to " + apiPlusFifdisp);
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    });

                            alertDialog.show();
                        }
                    }



                } else if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains("PPBS") && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains("FBS")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    Calendar cal = Calendar.getInstance();
                    Date currentTime = cal.getTime();
                    Logger.error(">> " + currentTime);
                    String CurrentStr = currentTime.toString();
                    strDate = null;
                    try {
                        Test = orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + orderVisitDetailsModelsArr.get(pos).getSlot();
                        Test2 = orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + orderVisitDetailsModelsArr.get(pos).getSlot();
                        strDate2 = sdf.parse("" + orderVisitDetailsModelsArr.get(pos).getSlot());
                        strDate3 = sdf.parse("" + orderVisitDetailsModelsArr.get(pos).getSlot());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(strDate2);
                        apihours = calendar.get(Calendar.HOUR_OF_DAY);
                        apiminutes = calendar.get(Calendar.MINUTE);
                        Logger.error(">>apihours ....." + apihours);
                        Logger.error(">>apiminutes ....." + apiminutes);


                    } catch (ParseException e) {
                        e.printStackTrace();
                        Logger.error(">> " + e.getMessage());
                    }


                    Logger.error(">> " + orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + orderVisitDetailsModelsArr.get(pos).getSlot());
                    Calendar cal7 = Calendar.getInstance();
                    Logger.error("strDate3   " + strDate3);
                    cal7.setTime(strDate3);

                    cal7.add(Calendar.MINUTE, +120);
                    int hours = cal7.get(Calendar.HOUR_OF_DAY);
                    int Minutes = cal7.get(Calendar.MINUTE);
                    Logger.error(">>hours ....." + hours);
                    Logger.error(">>Minutes ....." + Minutes);
                    apiPlusTwoPBBS = sdf.format(cal7.getTime());
                    apiPlusTwoPBBS2 = sdf.format(cal7.getTime());

/*
                    if (isTimeBetweenTwoHours(apihours, hours, Calendar.getInstance(), apiminutes, Minutes)) {
                        holder.btnStartNavigation.setEnabled(true);
                        goAheadWithNormalFlow();
                        Toast.makeText(activity, " in between ", Toast.LENGTH_SHORT).show();

                    } else {
                        holder.btnStartNavigation.setEnabled(false);
                        Toast.makeText(activity, "not in between ", Toast.LENGTH_SHORT).show();
                    }*/
                    goAheadWithNormalFlow();

                } else if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.PPBS) && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.FBS)){

                    goAheadWithNormalFlow();
                } else {

                    goAheadWithNormalFlow();
                }

            }

            private boolean isTimeBetweenTwoHours(
                    int fromHour, int toHour, Calendar now, int fromMin, int toMin) {
                //Start Time
                Calendar from = Calendar.getInstance();
                from.set(Calendar.HOUR_OF_DAY, fromHour);
                from.set(Calendar.MINUTE, fromMin);
                //Stop Time
                Calendar to = Calendar.getInstance();
                to.set(Calendar.HOUR_OF_DAY, toHour);
                to.set(Calendar.MINUTE, toMin);
                if (to.before(from)) {
                    if (now.after(to)) to.add(Calendar.DATE, 1);
                    else from.add(Calendar.DATE, -1);
                }
                return now.after(from) && now.before(to);
            }

            private void goAheadWithNormalFlow() {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
                Date date = DateUtils.dateFromString(newTimeAfterMinusSixty1, sdf);
                Logger.error("strDate btnStartNavigation " + date);
                Logger.error("new Date() " + new Date());

                if (new Date().before(date)) {
                    //  Toast.makeText(activity, "is After", Toast.LENGTH_SHORT).show();
                    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                    alertDialog.setMessage("Appointment time of this order is at " + orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + orderVisitDetailsModelsArr.get(pos).getSlot() + " " + "Still do you want to start?");
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
                    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                    alertDialog.setMessage("Do you want to Confirm?");
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
                }
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

    private void initData(final FoldingCellViewHolder holder, final int pos) {

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
            holder.locationdata.setSelected(true);
            holder.locationdata.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getAddress());
            holder.timedata.setText(orderVisitDetailsModelsArr.get(pos).getSlot() + "  HRS");
            dateCheck(pos);


            holder.txtorderno.setText(orderVisitDetailsModelsArr.get(pos).getVisitId());
            holder.txtAge.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getAge() + " Y | " + orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getGender());
            holder.txtName.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getName());
            holder.txtName.setSelected(true);
            holder.txtSrNo.setText(pos + 1 + "");
            Logger.error("LOcationnnn" + orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getLocation());

            holder.txt_distance.setText(String.valueOf(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getDistance()) + "KM");
            holder.txtKits.setText(String.valueOf(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getKits().get(0).getKit()));
            holder.txtAddress.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getAddress());


            //jai
            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().equals("PPBS")) {
                holder.imgRelease.setVisibility(View.GONE);
                holder.imgRelease2.setVisibility(View.GONE);
            }
            //jai
            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains("PPBS") && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains("FBS")) {
                // checkWithApiTimeForPPBS("11:00 AM","00:00 AM",""+new Date())


                if (checkIfBefore11()) {
                    holder.imgRelease.setVisibility(View.VISIBLE);
                    holder.imgRelease2.setVisibility(View.VISIBLE);
                } else {
                    holder.imgRelease.setVisibility(View.GONE);
                    holder.imgRelease2.setVisibility(View.GONE);
                }
            }
            //jai


//            holder.tvAadharNo.setVisibility(View.GONE);
            // holder.txtAadharNo.setVisibility(View.GONE);
            if (pos != 0) {
                holder.btnStartNavigation.setVisibility(View.VISIBLE);
            }
            if (unfoldedIndexes.contains(pos)) {
                holder.cell.unfold(true);
            } else {
                holder.cell.fold(true);
            }
            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment") || orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED")) {
                fastingFlagInt = 0;
                holder.imgCBAccept.setVisibility(View.VISIBLE);
                holder.tvName.setVisibility(View.INVISIBLE);
                holder.txtName.setVisibility(View.INVISIBLE);
                holder.txtAge.setVisibility(View.INVISIBLE);
                holder.tvAge.setVisibility(View.INVISIBLE);
                holder.imgcall.setVisibility(View.INVISIBLE);
                holder.imgFastingStatus.setVisibility(View.INVISIBLE);
                Logger.error("IMGCALL2 INVISIBLE");

                //  holder.txtSrNo.setVisibility(View.INVISIBLE);
                holder.txtSrNo.setVisibility(View.GONE);
                // holder.imgcall.setVisibility(View.GONE);
                holder.imgCBAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//jai
                        Logger.error("clicked items test: " + orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode());



                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                        builder1.setTitle("Warning ")
                                .setMessage("Do you Want to accept order?").setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fastingFlagInt = 1;
                                holder.tvName.setVisibility(View.VISIBLE);
                                holder.txtName.setVisibility(View.VISIBLE);
                                holder.txtAge.setVisibility(View.VISIBLE);
                                holder.tvAge.setVisibility(View.VISIBLE);
                                holder.imgFastingStatus.setVisibility(View.VISIBLE);
                                holder.imgcall.setVisibility(View.VISIBLE);
                                if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains("PPBS") && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains("FBS")) {
                                    if (checkIfBefore11()) {
                                        holder.imgRelease2.setVisibility(View.GONE);
                                    }
                                } else {
                                    holder.imgRelease2.setVisibility(View.VISIBLE);
                                }


                                Logger.error("IMGCALL3 VISIBLE");
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


//                    }
                });
            } else {
                fastingFlagInt = 1;
                holder.imgFastingStatus.setVisibility(View.VISIBLE);
                holder.imgCBAccept.setVisibility(View.INVISIBLE);
                holder.tvName.setVisibility(View.VISIBLE);
                holder.txtName.setVisibility(View.VISIBLE);
                holder.txtAge.setVisibility(View.VISIBLE);
                holder.tvAge.setVisibility(View.VISIBLE);
                holder.imgcall.setVisibility(View.VISIBLE);
                Logger.error("IMGCALL4 VISIBLE");


                //   holder.imgcall.setVisibility(View.GONE);
                holder.txtSrNo.setVisibility(View.VISIBLE);
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

    private void SendinglatlongOrderAllocation(int pos) {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        OrderAllocationTrackLocationRequestModel orderAllocationTrackLocationRequestModel = new OrderAllocationTrackLocationRequestModel();

        orderAllocationTrackLocationRequestModel.setVisitId(orderVisitDetailsModelsArr.get(pos).getVisitId());
        orderAllocationTrackLocationRequestModel.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
        orderAllocationTrackLocationRequestModel.setStatus(8);
        orderAllocationTrackLocationRequestModel.setLatitude(appPreferenceManager.getLatitude());
        orderAllocationTrackLocationRequestModel.setLongitude(appPreferenceManager.getLongitude());

        ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderAllocationpost(orderAllocationTrackLocationRequestModel);
        orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderAllocationTrackLocationiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);

        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void dateCheck(int pos) {
        //jai
        //minus 30 min
        apiTime = orderVisitDetailsModelsArr.get(pos).getSlot();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        apitimeinHHMMFormat = null;
        try {
            apitimeinHHMMFormat = df.parse(apiTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar cal = Calendar.getInstance();
        cal.setTime(apitimeinHHMMFormat);
        cal.add(Calendar.MINUTE, -60);
        String newTimeAfterMinusSixty = df.format(cal.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
        strDate = null;
        try {

            Logger.error("newTimeAfterMinusSixty " + newTimeAfterMinusSixty);
            // strDate = sdf.parse("" + orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + apitimeinHHMMFormat);
            strDate = sdf.parse("" + orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + orderVisitDetailsModelsArr.get(pos).getSlot());
            Logger.error(">> " + orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + orderVisitDetailsModelsArr.get(pos).getSlot());
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(strDate);
            cal1.add(Calendar.HOUR, -1);
            newTimeAfterMinusSixty1 = sdf.format(cal1.getTime());
            Logger.error(">> ....." + newTimeAfterMinusSixty1);
            strDate = sdf.parse("28-08-2017 19:42");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean timeCheckPPBS(int pos) {
        //jai
        //plus 15 min
        //minus 15 min
        apiTime = orderVisitDetailsModelsArr.get(pos).getSlot();

        SimpleDateFormat df = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        apitimeinHHMMFormat = null;
        try {
            apitimeinHHMMFormat = df.parse(apiTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }



     /*   SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");*/
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
        strDate = null;
        try {


            strDate2 = sdf.parse("" + orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + orderVisitDetailsModelsArr.get(pos).getSlot());
            Calendar cal = Calendar.getInstance();
            Date currentTime = cal.getTime();
            Logger.error(">> " + currentTime);
            strDate = currentTime;
            Logger.error(">> " + orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + orderVisitDetailsModelsArr.get(pos).getSlot());
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(strDate);
            String strdate3 = df.format(strDate);
            cal1.add(Calendar.MINUTE, -15);
            apiMinusFif = sdf.format(cal1.getTime());

            apiMinusFif = apiMinusFif.substring(11, 19);
            Logger.error(">>apiMinusFif ....." + apiMinusFif);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(strDate);
            cal2.add(Calendar.MINUTE, +15);
            apiPlusFif = sdf.format(cal2.getTime());
            apiPlusFif = apiPlusFif.substring(11, 19);
            Logger.error(">>apiPlusFif ....." + apiPlusFif);
            String apiTime = null;
            apiTime = orderVisitDetailsModelsArr.get(pos).getSlot();
            Logger.error(">>apiTime: " + orderVisitDetailsModelsArr.get(pos).getSlot());

            ////////////////////////////////////////////////////////////////////////Alert Display time //////////////////////

            Logger.error(">> " + orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + orderVisitDetailsModelsArr.get(pos).getSlot());
            Calendar cal3 = Calendar.getInstance();
            cal3.setTime(strDate2);
            cal3.add(Calendar.MINUTE, -15);
            apiMinusFifdisp = sdf.format(cal3.getTime());

            apiMinusFifdisp = apiMinusFifdisp.substring(11, 19);
            Logger.error(">>apiMinusFifdisp ....." + apiMinusFifdisp);
            Calendar cal4 = Calendar.getInstance();
            cal4.setTime(strDate2);
            cal4.add(Calendar.MINUTE, +15);
            apiPlusFifdisp = sdf.format(cal4.getTime());
            apiPlusFifdisp = apiPlusFifdisp.substring(11, 19);
            Logger.error(">>apiPlusFifdisp ....." + apiPlusFifdisp);
            apiTime = orderVisitDetailsModelsArr.get(pos).getSlot();
            Logger.error(">>apiPlusFifdisp: " + orderVisitDetailsModelsArr.get(pos).getSlot());


            Logger.error(">>Checker api: " + strdate3);
            Logger.error(">>Checkerplus15min: " + apiPlusFifdisp);
            Logger.error(">>CheckerMinus15min: " + apiMinusFifdisp);
          /*  if (checkWithApiTimeForPPBS(apiPlusFif, apiMinusFif, apiTime)) {
*/
            if (checkWithApiTimeForPPBS(apiPlusFifdisp, apiMinusFifdisp, strdate3)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkWithApiTimeForPPBS(String apiPlusFif, String apiMinusFif, String apiTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
            Date mToday = sdf.parse(apiTime);


            String curTime = sdf.format(mToday);
            Date start = sdf.parse(apiMinusFif);
            Date end = sdf.parse(apiPlusFif);
            Date userDate = sdf.parse(curTime);

            if (end.before(start)) {
                Calendar mCal = Calendar.getInstance();
                mCal.setTime(end);
                mCal.add(Calendar.DAY_OF_YEAR, 1);
                end.setTime(mCal.getTimeInMillis());
            }

            Log.d("curTime", userDate.toString());
            Log.d("start", start.toString());
            Log.d("end", end.toString());


            if (userDate.after(start) && userDate.before(end)) {
                Log.d("result", "falls between start and end , go to screen 1 ");
                return true;
            } else {
                Log.d("result", "does not fall between start and end , go to screen 2 ");
                return false;
            }
        } catch (ParseException e) {
            // Invalid date was entered
        }
        return false;
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
        FrameLayout fm_title;
        FoldingCell cell;
        LinearLayout mainleft, mainright, main;

        FoldingCellViewHolder(View itemView) {
            initUI(itemView);
        }

        private void initUI(View itemView) {

            imgFastingStatus = (ImageView) itemView.findViewById(R.id.title_fasting);
            imgcall = (ImageView) itemView.findViewById(R.id.call);
            locationtitle = (TextView) itemView.findViewById(R.id.location_title);
            //  locationdata = (TextView) itemView.findViewById(R.id.location_datas);
            pintitle = (TextView) itemView.findViewById(R.id.pincode_title);
            //   pindata = (TextView) itemView.findViewById(R.id.pincode_data);
            cell = (FoldingCell) itemView.findViewById(R.id.item_folding_cell);
            tvSrNo = (TextView) itemView.findViewById(R.id.tv_sr_no);
            //  timedata = (TextView) itemView.findViewById(R.id.time_data);
            timetitle = (TextView) itemView.findViewById(R.id.time_title);
            txtSrNo = (TextView) itemView.findViewById(R.id.txt_sr_no);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            mainleft = (LinearLayout) itemView.findViewById(R.id.mainleft);
            mainright = (LinearLayout) itemView.findViewById(R.id.mainright);
            main = (LinearLayout) itemView.findViewById(R.id.main);
            txtAge = (TextView) itemView.findViewById(R.id.txt_age);
            //   txtAadharNo = (TextView) itemView.findViewById(R.id.txt_aadhar_no);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAge = (TextView) itemView.findViewById(R.id.tv_age);

            txtorderno = (TextView) itemView.findViewById(R.id.tv_orderno);
            apptDate = (TextView) itemView.findViewById(R.id.apptdate);
            timedata = (TextView) itemView.findViewById(R.id.time_data);
            locationdata = (TextView) itemView.findViewById(R.id.location_datas);
            pindata = (TextView) itemView.findViewById(R.id.pincode_data);

            tvName.setVisibility(View.INVISIBLE);
            txtName.setVisibility(View.INVISIBLE);
            txtAge.setVisibility(View.INVISIBLE);
            tvAge.setVisibility(View.INVISIBLE);
            imgcall.setVisibility(View.INVISIBLE);
            Logger.error("IMGCALL1 INVISIBLE");
//            tvAadharNo = (TextView) itemView.findViewById(R.id.tv_aadhar_no);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            //    txtorderno = (TextView) itemView.findViewById(R.id.tv_orderno);
            txtKits = (TextView) itemView.findViewById(R.id.txt_num_kits);
            imgRelease = (ImageView) itemView.findViewById(R.id.img_release);
            btnStartNavigation = (Button) itemView.findViewById(R.id.btn_start_navigation);
            imgCBAccept = (ImageView) itemView.findViewById(R.id.img_oas);
            imgRelease2 = (ImageView) itemView.findViewById(R.id.img_release2);
            txt_distance = (TextView) itemView.findViewById(R.id.txt_distance_1);

            //change22june2017
            apptDateValue = (TextView) itemView.findViewById(R.id.apptdateValue);
            // apptDate = (TextView) itemView.findViewById(R.id.apptdate);
            //change22june2017

            timedata.setVisibility(View.VISIBLE);
            timetitle.setVisibility(View.VISIBLE);
            locationtitle.setVisibility(View.VISIBLE);
            locationdata.setVisibility(View.VISIBLE);
            pintitle.setVisibility(View.VISIBLE);
            pindata.setVisibility(View.VISIBLE);

        }
    }

    private void isAutoTimeSelected() {
        try {
            if (Settings.Global.getInt(activity.getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
                Logger.error("enabled");
                isAutoTimeSelected = true;
            } else {
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                builder1.setTitle("Warning ").setMessage("You have to Enable Automatic date and time setting").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                        try {
                            if (Settings.Global.getInt(activity.getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
                                isAutoTimeSelected = true;
                            } else {
                                isAutoTimeSelected = false;
                                activity.pushFragments(VisitOrdersDisplayFragment.newInstance(), false, false, VisitOrdersDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, VisitOrdersDisplayFragment.TAG_FRAGMENT);
                            }
                        } catch (Settings.SettingNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder1.setCancelable(false);
                builder1.show();
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
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
}










