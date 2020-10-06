package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.btechapp.Controller.SendLatLongforOrderController;
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
import application.ApplicationController;
import com.thyrocare.btechapp.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.thyrocare.btechapp.delegate.refreshDelegate;
import com.thyrocare.btechapp.dialog.RescheduleOrderDialog;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.ServiceUpdateRequestModel;
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
import java.util.Iterator;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;

public class Btech_VisitDisplayAdapter extends RecyclerView.Adapter<Btech_VisitDisplayAdapter.MyViewHolder> {
    private static final String TAG = Btech_VisitDisplayAdapter.class.getSimpleName();
    private final Activity activity;
    private HomeScreenActivity homeScreenActivity;
    private final GPSTracker gpsTracker;
    private LayoutInflater layoutInflater;
    private ArrayList<OrderVisitDetailsModel> orderVisitDetailsModelsArr;
    private final String current_date;
    private OnClickListeners onClickListeners;
    private AppPreferenceManager appPreferenceManager;
    private int fastingFlagInt;
    private String apiPlusFif, apiMinusFif;
    private String newTimeAfterMinusSixty1,  cancelVisit = "n", apiTime, MaskedPhoneNumber = "";
    private refreshDelegate refreshDelegate1;
    private Date strDate;
    private Date apitimeinHHMMFormat;
    boolean isAutoTimeSelected = false;
    private Date strDate2;
    private String apiMinusFifdisp, apiPlusFifdisp, apiPlusTwoPBBS;
    CharSequence[] items;

    //neha g------------
    String finaltime = "";
    Long time, currenttime;
    int hr = 0;
    int minnew = 0;
    private String userChoosenReleaseTask;
    //neha g----------------------
    private String Test;
    private String apiPlusTwoPBBS2;
    private String Test2;
    private int apihours;
    private int apiminutes;
    private Date strDate3;
    private Global globalClass;
    private boolean isCancelRequesGenereted = false;

    public Btech_VisitDisplayAdapter(HomeScreenActivity homeScreenActivity , Activity activity, ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels) {
        this.activity = activity;
        this.orderVisitDetailsModelsArr = orderDetailsResponseModels;
        layoutInflater = LayoutInflater.from(activity);
        current_date = DateUtil.getDateFromLong(System.currentTimeMillis(),"dd-MM-yyyy");
        appPreferenceManager = new AppPreferenceManager(activity);
        gpsTracker = new GPSTracker(activity);
        this.homeScreenActivity = homeScreenActivity;
        globalClass = new Global(activity);
    }

    public void UpdateList(ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels){
        this.orderVisitDetailsModelsArr = orderDetailsResponseModels;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  txtCustomerName, txtOrderNo, txtDate, txtSlot, txtBeneficiary, txtSamples, txtAddress,txtPPBSStatus,txtFastingStatus,txtRBSStatus,direct_visit,txtKits,txt_visit_day;
        ImageView imgRelease, imgCall,img_accept, imgStart;
        LinearLayout layoutAccept_Release_Ord, layoutMain,lin_bencount,lin_kits, layoutFasingStatus;
        View view_seperater;

        public MyViewHolder(View view) {
            super(view);

            txtOrderNo = (TextView) view.findViewById(R.id.txtOrderNo);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtSlot = (TextView) view.findViewById(R.id.txtSlot);
            txtBeneficiary = (TextView) view.findViewById(R.id.txtBeneficiary);
            txtKits = (TextView) view.findViewById(R.id.txtKits);
            txtSamples = (TextView) view.findViewById(R.id.txtSamples);
            txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            layoutAccept_Release_Ord = (LinearLayout) view.findViewById(R.id.layoutAccept_Release_Ord);
            layoutMain = (LinearLayout) view.findViewById(R.id.layoutMain);
            lin_bencount = (LinearLayout) view.findViewById(R.id.lin_bencount);
            lin_kits = (LinearLayout) view.findViewById(R.id.lin_kits);
            txtFastingStatus = (TextView) view.findViewById(R.id.txtFastingStatus);
            txtPPBSStatus = (TextView) view.findViewById(R.id.txtPPBSStatus);
            txtRBSStatus = (TextView) view.findViewById(R.id.txtRBSStatus);
            direct_visit = (TextView) view.findViewById(R.id.direct_visit);
            txt_visit_day = (TextView) view.findViewById(R.id.txt_visit_day);
            img_accept = (ImageView) view.findViewById(R.id.img_accept);
            imgStart = (ImageView) view.findViewById(R.id.imgStart);
            imgRelease = (ImageView) view.findViewById(R.id.imgRelease);
            imgCall = (ImageView) view.findViewById(R.id.imgCall);
            view_seperater = (View) view.findViewById(R.id.view_seperater);
            layoutFasingStatus = (LinearLayout) view.findViewById(R.id.layoutFastingStatus);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.btech_visit_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int pos) {

        if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().size() > 0
                && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().size() > 0
                && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0) != null) {

            holder.txtCustomerName.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getName());

           if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getCHC() > 0){
               holder.txtOrderNo.setText(orderVisitDetailsModelsArr.get(pos).getVisitId() + "  (₹"+orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getCHC()+")");
           }else{
               holder.txtOrderNo.setText(orderVisitDetailsModelsArr.get(pos).getVisitId());
           }

            holder.txtDate.setText(orderVisitDetailsModelsArr.get(pos).getAppointmentDate());
            holder.txtSlot.setText(", "+ DateUtil.Req_Date_Req(orderVisitDetailsModelsArr.get(pos).getSlot(),"hh:mm a","HH:mm"));
//            holder.txtAddress.setSelected(true);
//            holder.txtAddress.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getAddress());
            DisplayBencount(pos,holder);
            // TODO logic needs to be set for sample count
            DisplayDayWiselayoutColor(pos,holder);
            DisplayDirectVisit(pos,holder);
            holder.view_seperater.setVisibility(View.GONE);
            CheckPPBSisPresent(pos, holder);
            CheckRBSisPresent(pos, holder);
            ShowreleaseOption(pos, holder);
            ShowAndHideAcceptOption(pos,holder);
            ShowFastingNonFasting(pos, holder);
            dateCheck(pos); // To Check Time for PPBS and RBS orders
            DisplayKitData(holder, pos);
            initLIsteners(pos,holder);
        }
    }

    private void initLIsteners(final int pos, final MyViewHolder holder) {

        holder.img_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAcceptButtonClicked(holder,pos);
            }
        });

        holder.imgRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReleaseButtonClicked(pos,holder);
            }
        });


        holder.imgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pos == 0) {
                    if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails() != null && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().size() > 0 && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).isPPE()){
                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                        builder1.setMessage(ConstantsMessages.EnsureToWearPPE).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PerformStartFunction(pos, holder);
                            }
                        });
                        builder1.show();
                    }else{
                        PerformStartFunction(pos, holder);
                    }
                } else {
                    Toast.makeText(activity, "Please service the earlier orders first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListeners != null) {
                    onClickListeners.onCallCustomer(orderVisitDetailsModelsArr.get(pos));
                }
            }
        });

        holder.txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getAddress() + "\n"+orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getPincode();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Address")
                        .setMessage(address)
                        .setCancelable(false)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void PerformStartFunction(int pos, MyViewHolder holder) {
        if (!orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED") && !orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment")) {
            String appointmentDate = orderVisitDetailsModelsArr.get(pos).getAppointmentDate();
            Date ApptTime = DateUtil.dateFromString(appointmentDate + " "+ orderVisitDetailsModelsArr.get(pos).getSlot(),"dd-MM-yyyy hh:mm a");
            Date CurrentTime = new Date();
            long difference = ApptTime.getTime() - CurrentTime.getTime();
            int days = (int) (difference / (1000*60*60*24));
            int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            if (hours < 3){
                onStartClicked(pos,holder);
            }else{
                globalClass.showalert_OK("You Cannot start order before 3 hours of Appointment Time.", activity);
            }

        } else {
            Toast.makeText(activity, "Please accept the order first", Toast.LENGTH_SHORT).show();
        }
    }

    private void DisplayDayWiselayoutColor(int pos , MyViewHolder holder) {
        if (orderVisitDetailsModelsArr.get(pos).getAppointmentDate().equals(current_date)) {
            holder.txt_visit_day.setBackgroundColor(activity.getResources().getColor(R.color.test1));
            holder.txt_visit_day.setText("Today");
        } else {
            holder.txt_visit_day.setBackgroundColor(activity.getResources().getColor(R.color.test2));
            holder.txt_visit_day.setText("Tomorrow");
        }
    }

    private void DisplayDirectVisit(int pos, MyViewHolder holder) {

        if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).isDirectVisit()) {
//            layoutMain.setBackgroundColor(activity.getResources().getColor(R.color.directVisit));
            holder.direct_visit.setVisibility(View.VISIBLE);
        }else {
            holder.direct_visit.setVisibility(View.INVISIBLE);
        }
    }

    private void DisplayBencount(int pos, MyViewHolder holder) {

        int Bencount = 1;
        boolean displayBencount = false;
        try {
            if (orderVisitDetailsModelsArr != null) {
                if (orderVisitDetailsModelsArr.size() != 0) {
                    if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails() != null) {
                        if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster() != null) {
                            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().size() != 0) {
                                displayBencount = true;
                                Bencount = orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().size();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (displayBencount){
            if (Bencount == 1) {
                holder.txtBeneficiary.setText("" + Bencount + " Beneficiary");
            } else {
                holder.txtBeneficiary.setText("" + Bencount + " Beneficiaries");
            }
        }else{
            holder.lin_bencount.setVisibility(View.GONE);
        }
    }

    private void CheckPPBSisPresent(int pos, MyViewHolder holder) {
        boolean isPPBSpresent = false;
        String secondVisitTest  = orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getSecondVisitTest();
        if (!InputUtils.isNull(secondVisitTest) && secondVisitTest.contains(AppConstants.PPBS)) {
            isPPBSpresent =  true;
        }
        if (isPPBSpresent) {
            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED")) {
                holder.txtPPBSStatus.setVisibility(View.GONE);
            } else {
                holder.view_seperater.setVisibility(View.VISIBLE);
                holder.txtPPBSStatus.setVisibility(View.VISIBLE);
            }
        }else {
            holder.txtPPBSStatus.setVisibility(View.GONE);
        }
    }

    private void CheckRBSisPresent(int pos, MyViewHolder holder) {

        boolean isRBSpresent = false;
        String secondVisitTest  = orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getSecondVisitTest();
        if (!InputUtils.isNull(secondVisitTest) && secondVisitTest.contains(AppConstants.RBS)) {
            isRBSpresent =  true;
        }
        if (isRBSpresent) {
            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED")) {
                holder.txtRBSStatus.setVisibility(View.GONE);
            } else {
                holder.view_seperater.setVisibility(View.VISIBLE);
                holder.txtRBSStatus.setVisibility(View.VISIBLE);
            }
        }else {
            holder.txtRBSStatus.setVisibility(View.GONE);
        }
    }

    private void ShowreleaseOption(int pos, MyViewHolder holder) {
        try {
            if (appPreferenceManager.getLoginResponseModel() != null) {
                if (appPreferenceManager.getLoginResponseModel().getRole() != null) {
                    if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.NBTTSP_ROLE_ID)) {
                        holder.imgRelease.setVisibility(View.GONE);
                    }
                }
            }

            if (isValidForEditing(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase())) {
                holder.imgRelease.setVisibility(View.GONE);
            }

            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.PPBS)
                    && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.FBS)) {

                if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.NBTTSP_ROLE_ID)) {
                    holder.imgRelease.setVisibility(View.GONE);
                } else {
                    holder.imgRelease.setVisibility(View.VISIBLE);
                }
            }
            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.RBS)
                    && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.FBS)) {

                if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.NBTTSP_ROLE_ID)) {
                    holder.imgRelease.setVisibility(View.GONE);
                } else {
                    holder.imgRelease.setVisibility(View.VISIBLE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    private void ShowAndHideAcceptOption(int pos, MyViewHolder holder) {

        if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment") || orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED")) {
            fastingFlagInt = 0;
            holder.img_accept.setVisibility(View.VISIBLE);
//            holder.txtFastingStatus.setVisibility(View.GONE);
            holder.layoutFasingStatus.setVisibility(View.GONE);
            holder.imgStart.setVisibility(View.GONE);
            holder.imgCall.setVisibility(View.GONE);
        } else {
            fastingFlagInt = 1;
            holder.img_accept.setVisibility(View.GONE);
//            holder.txtFastingStatus.setVisibility(View.VISIBLE);
            holder.layoutFasingStatus.setVisibility(View.VISIBLE);
            holder.imgStart.setVisibility(View.VISIBLE);
            holder.imgCall.setVisibility(View.VISIBLE);
        }
    }

    private void ShowFastingNonFasting(int pos, MyViewHolder holder) {

        boolean isFasting = false;
        boolean isNonFasting = false;

        final ArrayList<String> benFastingDetails = new ArrayList<>();
        for (OrderDetailsModel odm :
                orderVisitDetailsModelsArr.get(pos).getAllOrderdetails()) {
            for (BeneficiaryDetailsModel bdm :
                    odm.getBenMaster()) {
                if (bdm.getFasting().equalsIgnoreCase("Fasting")) {
                    isFasting = true;
                    MessageLogger.LogError(TAG, "onBindViewHolder: Fasting");
                } else if (bdm.getFasting().equalsIgnoreCase("Non-Fasting")) {
                    isNonFasting = true;
                    MessageLogger.LogError(TAG, "onBindViewHolder: Non-Fasting");
                }
                benFastingDetails.add("" + bdm.getName() + " : " + bdm.getFasting());
            }
        }

        if (fastingFlagInt == 1) {
            fastingFlagInt = 0;
            if (isFasting && isNonFasting) {
                MessageLogger.LogError(TAG, "isFasting && isNonFasting");
//                holder.txtFastingStatus.setVisibility(View.VISIBLE);
                holder.layoutFasingStatus.setVisibility(View.VISIBLE);
                holder.txtFastingStatus.setText("Yes");
            } else if (isFasting && !isNonFasting) {
                MessageLogger.LogError(TAG, "isFasting && !isNonFasting");
//                holder.txtFastingStatus.setVisibility(View.VISIBLE);
                holder.layoutFasingStatus.setVisibility(View.VISIBLE);
//                holder.txtFastingStatus.setText("F");
                holder.txtFastingStatus.setText("Yes");
            } else if (!isFasting && isNonFasting) {
                MessageLogger.LogError(TAG, "!isFasting && isNonFasting");
//                holder.txtFastingStatus.setVisibility(View.VISIBLE);
                holder.layoutFasingStatus.setVisibility(View.VISIBLE);
//                holder.txtFastingStatus.setText("NF");
                holder.txtFastingStatus.setText("No");
            } else {
//                holder.txtFastingStatus.setVisibility(View.GONE);
                holder.layoutFasingStatus.setVisibility(View.GONE);
            }
        }else {
//            holder.txtFastingStatus.setVisibility(View.GONE);
            holder.layoutFasingStatus.setVisibility(View.GONE);
        }

        holder.txtFastingStatus.setOnClickListener(new View.OnClickListener() {
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

            strDate = sdf.parse("" + orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + orderVisitDetailsModelsArr.get(pos).getSlot());
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(strDate);
            cal1.add(Calendar.HOUR, -1);
            newTimeAfterMinusSixty1 = sdf.format(cal1.getTime());
            strDate = sdf.parse("28-08-2017 19:42");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void DisplayKitData(MyViewHolder holder, int pos) {
        String strKit = "";
        if (orderVisitDetailsModelsArr != null) {
            if (orderVisitDetailsModelsArr.size() != 0) {
                if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails() != null) {
                    if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getKits() != null) {
                        if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getKits().size() != 0) {
                            strKit = CallViewKitsstr(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getKits());
                        }
                    }
                }
            }
        }
        if (!StringUtils.isNull(strKit)){
            holder.txtKits.setText(strKit+" Kits");
        }else{
            holder.lin_kits.setVisibility(View.GONE);
        }
    }

    private String CallViewKitsstr(ArrayList<KitsCountModel> kits) {

        HashMap<String, Integer> kitsCount = new HashMap<>();
        String kitsReq = "";

        for (KitsCountModel kt :
                kits) {
            if (kitsCount.containsKey(kt.getKit())) {
                kitsCount.put(kt.getKit(), kitsCount.get(kt.getKit()) + kt.getValue());
            } else {
                kitsCount.put(kt.getKit(), kt.getValue());
            }
        }


        Iterator it = kitsCount.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            if (InputUtils.isNull(kitsReq)) {
                kitsReq = pair.getValue() + " " + pair.getKey();
            } else {
                kitsReq = kitsReq + " | " + pair.getValue() + " " + pair.getKey();
            }
            it.remove(); // avoids a ConcurrentModificationException
        }

        String regex = "\\s*\\bKIT\\b\\s*";
        kitsReq = kitsReq.replaceAll(regex, "");

        return kitsReq;
    }

    // TODO ---------------- methods of listeners -----------------------------------------------------

    private void SendinglatlongOrderAllocation(int pos) {

        if (ApplicationController.sendLatLongforOrderController != null) {
            ApplicationController.sendLatLongforOrderController = null;
        }
        ApplicationController.sendLatLongforOrderController = new SendLatLongforOrderController(activity);
        ApplicationController.sendLatLongforOrderController.SendLatlongToToServer(orderVisitDetailsModelsArr.get(pos).getVisitId(),8);
        ApplicationController.sendLatLongforOrderController.setOnResponseListener(new SendLatLongforOrderController.OnResponseListener() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onfailure(String msg) {

            }
        });

    }

    private void onAcceptButtonClicked(final MyViewHolder holder, final int pos) {

        if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails() != null && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().size() > 0 && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).isPPE()){
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
            String msg = !InputUtils.isNull(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getPPE_AlertMsg()) ? orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getPPE_AlertMsg() : ConstantsMessages.CustomerOptedForPPE;
            builder1.setMessage(msg).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ShowAcceptAlert(holder, pos);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder1.show();
        }else{
            ShowAcceptAlert(holder, pos);
        }


    }

    private void ShowAcceptAlert(final MyViewHolder holder, final int pos) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage("Do you want to accept order?").setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //neha g ---------------------
                BundleConstants.OrderAccept = 2;
                appPreferenceManager.setOrderAccept(2);
                startAlert();
                //neha g-----------------------
                fastingFlagInt = 1;
                holder.txtFastingStatus.setVisibility(View.VISIBLE);
                holder.imgRelease.setVisibility(View.VISIBLE);//remove 11 validation

                if (onClickListeners != null) {
                    onClickListeners.onAcceptClicked(orderVisitDetailsModelsArr.get(pos));
                }
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

    private void onReleaseButtonClicked(final int pos, MyViewHolder holder) {

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
        if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment")
                || orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED")) {
            if (isValidForEditing(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())) {
                items = new String[]{"Do you want to cancel the visit?"};
                cancelVisit = "y";
            } else {
                items = new String[]{"Order Release", "Order Pass"};
            }
        } else {
            if (isValidForEditing(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())) {
                items = new String[]{"Do you want to cancel the visit?"};
                cancelVisit = "y";
            } else {
                if (toShowResheduleOption){
                    items = new String[]{"Order Reschedule",
                            "Request Release", "Order Pass"};
                }else{
                    items = new String[]{"Request Release", "Order Pass"};
                }
            }
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Order Reschedule")) {
                    userChoosenReleaseTask = "Order Reschedule";
                    RescheduleOrderDialog cdd = new RescheduleOrderDialog(activity, new OrderRescheduleDialogButtonClickedDelegateResult(), orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0));
                    cdd.show();
                } else if (items[item].equals("Order Release")) {
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                    builder1.setTitle("Warning ")
                            .setMessage("Rs 200 debit will be levied for releasing this Order ").setPositiveButton("Accept Debit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (onClickListeners != null) {
                                onClickListeners.onItemRelease(orderVisitDetailsModelsArr.get(pos));
                            }
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
                            .setMessage("Rs 200 debit will be levied for releasing this order ").setPositiveButton("Accept debit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (onClickListeners != null) {
                                onClickListeners.onItemRelease(orderVisitDetailsModelsArr.get(pos));
                            }
                        }
                    }).setNegativeButton("Cancel Request", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder1.show();
                } else if (items[item].equals("Order Pass")) {
                    if (onClickListeners != null) {
                        onClickListeners.onItemReleaseTo(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getPincode(),orderVisitDetailsModelsArr.get(pos));
                    }
                } else if (items[item].equals("Do you want to cancel the visit?")) {

                }
            }
        });

        if (cancelVisit.equals("y")) {
            builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (cancelVisit.equals("y")) {
                        if (isNetworkAvailable(activity)) {
                            CallServiceUpdateAPI(pos);
                        } else {
                            Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
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
        builder.show();

    }

    private void CallServiceUpdateAPI(int pos) {

        ServiceUpdateRequestModel serviceUpdateRequestModel = new ServiceUpdateRequestModel();
        serviceUpdateRequestModel.setVisitId(orderVisitDetailsModelsArr.get(pos).getVisitId());
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallServiceUpdateAPI(serviceUpdateRequestModel);
        globalClass.showProgressDialog(activity,activity.getResources().getString(R.string.progress_message_changing_order_status_please_wait));

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalClass.hideProgressDialog();
                if (response.code() == 200) {
                    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                    alertDialog.setMessage("Your visit has been cancelled successfully");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    homeScreenActivity.pushFragments(VisitOrdersDisplayFragment_new.newInstance(), false, false, VisitOrdersDisplayFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, VisitOrdersDisplayFragment_new.TAG_FRAGMENT);
                                    dialog.dismiss();

                                }
                            });

                    alertDialog.show();

                } else {
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalClass.hideProgressDialog();
                MessageLogger.LogDebug("Errror", t.getMessage());
            }
        });
    }


    private class OrderRescheduleDialogButtonClickedDelegateResult implements OrderRescheduleDialogButtonClickedDelegate {

        @Override
        public void onOkButtonClicked(OrderDetailsModel orderDetailsModel, String remark, String date) {

            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(orderDetailsModel.getSlotId() + "");
            orderStatusChangeRequestModel.setRemarks(remark);
            orderStatusChangeRequestModel.setStatus(11);
            orderStatusChangeRequestModel.setAppointmentDate(date);

            if (isNetworkAvailable(activity)) {
                CallOrderStatusChangeAPI(orderStatusChangeRequestModel);
            } else {
                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelButtonClicked() {

        }
    }

    private void CallOrderStatusChangeAPI(OrderStatusChangeRequestModel orderStatusChangeRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallOrderStatusChangeAPI(orderStatusChangeRequestModel,orderStatusChangeRequestModel.getId());
        globalClass.showProgressDialog(activity,activity.getResources().getString(R.string.progress_message_changing_order_status_please_wait));

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalClass.hideProgressDialog();
                if (response.code() == 200) {
                    if (onClickListeners != null) {
                        onClickListeners.onRefresh();
                    }
                    if (userChoosenReleaseTask.equals("Visit Cancellation")) {
                        if (!isCancelRequesGenereted) {
                            isCancelRequesGenereted = true;
                            Toast.makeText(activity, "Order rescheduled successfully", Toast.LENGTH_SHORT).show();
                            if (onClickListeners != null) {
                                onClickListeners.onRefresh();
                            }
                            activity.finish();
                        }
                    }
                } else {
                    try {
                        Toast.makeText(activity, response.errorBody() != null ? response.errorBody().string() : SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalClass.hideProgressDialog();
                Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onStartClicked(final int pos, final MyViewHolder holder) {

        if (gpsTracker.canGetLocation()) {

            if (isValidForEditing(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase())) {
                isAutoTimeSelected();
                if (isAutoTimeSelected == true) {
                    if (timeCheckPPBS(pos)) {
                        goAheadWithNormalFlow(pos);
                    } else {
                        goAheadWithNormalFlow(pos);
                    }

                }
            } else if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.PPBS)
                    && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.FBS)) {

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                Calendar cal = Calendar.getInstance();
                Date currentTime = cal.getTime();
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
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal7 = Calendar.getInstance();
                cal7.setTime(strDate3);

                cal7.add(Calendar.MINUTE, +360);
                int hours = cal7.get(Calendar.HOUR_OF_DAY);
                int Minutes = cal7.get(Calendar.MINUTE);
                apiPlusTwoPBBS = sdf.format(cal7.getTime());
                apiPlusTwoPBBS2 = sdf.format(cal7.getTime());

                goAheadWithNormalFlow(pos);




            } else if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains("INSPP")
                    && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains("INSFA")) {
                goAheadWithNormalFlow(pos);
            } else {
                goAheadWithNormalFlow(pos);
            }
        } else {
            gpsTracker.showSettingsAlert();
        }

    }

    private void isAutoTimeSelected() {
        try {
            if (Settings.Global.getInt(activity.getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
                isAutoTimeSelected = true;
            } else {
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                builder1.setTitle("Warning ").setMessage("You have to enable automatic date and time setting").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                        try {
                            if (Settings.Global.getInt(activity.getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
                                isAutoTimeSelected = true;
                            } else {
                                isAutoTimeSelected = false;
                                homeScreenActivity.pushFragments(VisitOrdersDisplayFragment_new.newInstance(), false, false, VisitOrdersDisplayFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, VisitOrdersDisplayFragment_new.TAG_FRAGMENT);
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

    private boolean isTimeBetweenTwoHours(int fromHour, int toHour, Calendar now, int fromMin, int toMin) {
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

    private void goAheadWithNormalFlow(final int pos) {
        if (gpsTracker.canGetLocation()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
            Date date = DateUtils.dateFromString(newTimeAfterMinusSixty1, sdf);

            if (new Date().before(date)) {
                //  Toast.makeText(activity, "is After", Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                alertDialog.setMessage("Appointment time of this order is at " + orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + orderVisitDetailsModelsArr.get(pos).getSlot() + " " + "still do you want to start?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                assigned(pos);
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

                alertDialog.setMessage("Do you want to Start ?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                assigned(pos);
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
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void assigned(int pos) {

        if (!orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED") && !orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment")) {
            if (onClickListeners != null) {
                onClickListeners.onStart(orderVisitDetailsModelsArr.get(pos));
            }
        } else {
            Toast.makeText(activity, "Please accept the order first", Toast.LENGTH_SHORT).show();
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



        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
        strDate = null;
        try {


            strDate2 = sdf.parse("" + orderVisitDetailsModelsArr.get(pos).getAppointmentDate() + " " + orderVisitDetailsModelsArr.get(pos).getSlot());
            Calendar cal = Calendar.getInstance();
            Date currentTime = cal.getTime();
            strDate = currentTime;
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(strDate);
            String strdate3 = df.format(strDate);
            cal1.add(Calendar.MINUTE, -30);
            apiMinusFif = sdf.format(cal1.getTime());

            apiMinusFif = apiMinusFif.substring(11, 19);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(strDate);
            cal2.add(Calendar.MINUTE, +240);
            // cal2.add(Calendar.MINUTE, +15);
            apiPlusFif = sdf.format(cal2.getTime());
            apiPlusFif = apiPlusFif.substring(11, 19);
            String apiTime = null;
            apiTime = orderVisitDetailsModelsArr.get(pos).getSlot();

            ////////////////////////////////////////////////////////////////////////Alert Display time //////////////////////

            Calendar cal3 = Calendar.getInstance();
            cal3.setTime(strDate2);
            cal3.add(Calendar.MINUTE, -30);
            apiMinusFifdisp = sdf.format(cal3.getTime());

            apiMinusFifdisp = apiMinusFifdisp.substring(11, 19);
            Calendar cal4 = Calendar.getInstance();
            cal4.setTime(strDate2);
            //jai 9/10/17
            cal4.add(Calendar.MINUTE, +240);// 4 hours
            //  cal4.add(Calendar.MINUTE, +15);
            apiPlusFifdisp = sdf.format(cal4.getTime());
            apiPlusFifdisp = apiPlusFifdisp.substring(11, 19);
            apiTime = orderVisitDetailsModelsArr.get(pos).getSlot();


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

            MessageLogger.LogDebug("curTime", userDate.toString());
            MessageLogger.LogDebug("start", start.toString());
            MessageLogger.LogDebug("end", end.toString());


            if (userDate.after(start) && userDate.before(end)) {
                MessageLogger.LogDebug("result", "falls between start and end , go to screen 1 ");
                return true;
            } else {
                MessageLogger.LogDebug("result", "does not fall between start and end , go to screen 2 ");
                return false;
            }
        } catch (ParseException e) {
            // Invalid date was entered
        }
        return false;
    }

    //TODO NEHA
    public void startAlert() {
        String pattern = "dd-MM-yyyy HH:mm:ss.S";
        String datefrom_model = "";
        String notifyTime = "";
        String AMPM = "";
        String AppointmentDate = "";

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.S");
            for (int i = 0; i < orderVisitDetailsModelsArr.size(); i++) {
                MessageLogger.PrintMsg("ordervisitdetailsmodelsarr" + orderVisitDetailsModelsArr.size());
                datefrom_model = orderVisitDetailsModelsArr.get(i).getSlot();
                AppointmentDate = orderVisitDetailsModelsArr.get(i).getAppointmentDate();
                MessageLogger.PrintMsg("slot " + datefrom_model); //01:00 PM
                String[] spl = datefrom_model.split(" ");
                notifyTime = spl[0];
                AMPM = spl[1];
                String[] timesplit = notifyTime.split(":");
                String time1 = timesplit[0];
                MessageLogger.PrintMsg("AMPM" + AMPM);

                CheckDayOrEve(time1, AMPM);
                hr = Integer.parseInt(finaltime);
                minnew = Integer.parseInt(timesplit[1]);
                appPreferenceManager.setShowTimeInNotificatn(finaltime + ":" + timesplit[1]);
                BundleConstants.ShowTimeInNotificatn = finaltime + ":" + timesplit[1];

                MessageLogger.PrintMsg(" BundleConstants.ShowTimeInNotificatn" + BundleConstants.ShowTimeInNotificatn);
                Get15MinEarly(hr, minnew);
                MessageLogger.PrintMsg("final time " + finaltime);


                Date date = format.parse(AppointmentDate + " " + hr + ":" + minnew + ":" + "00.0");
                MessageLogger.PrintMsg("date" + date);
                time = date.getTime();

                currenttime = System.currentTimeMillis();


                if (currenttime - time < 1) {
                    MessageLogger.PrintMsg("condition in");
                    Intent intentAlarm = new Intent(activity, MyBroadcastReceiver.class);
                    MessageLogger.PrintMsg("broadcast called ");
                    AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(activity, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
                }
            }

            //TODO neha
            int currenthour = new Time(System.currentTimeMillis()).getHours();
            MessageLogger.PrintMsg("Current hour18" + currenthour);

            if (currenthour == 18 && orderVisitDetailsModelsArr.size() == 0) {
                MessageLogger.PrintMsg("in condition 1818" + currenthour);
                appPreferenceManager.setDataInVisitModel(2);
                BundleConstants.DataInVisitModel = 2;
                Notify();//TODO neha
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    private void Get15MinEarly(int a, int b) {
        MessageLogger.PrintMsg("hour" + a + " " + "min" + b);
        if (b < 15) {
            hr = a - 1;
            minnew = b + 45;
            MessageLogger.PrintMsg("hr less" + hr + " " + "minnew" + minnew);
        } else if (b > 15) {
            hr = a;
            minnew = b - 15;
            MessageLogger.PrintMsg("hr " + hr + " " + "minnew" + minnew);
        }
    }
    private void CheckDayOrEve(String time1, String AMPM) {

        MessageLogger.PrintMsg("time1 " + time1);
        MessageLogger.PrintMsg("AMPM1" + AMPM);
        if (AMPM.equals("PM")) {
            if (time1.equals("01")) {
                finaltime = "13";
            } else if (time1.equals("02")) {
                finaltime = "14";
            } else if (time1.equals("03")) {
                finaltime = "15";
            } else if (time1.equals("04")) {
                finaltime = "16";
                MessageLogger.PrintMsg("finaltime04 : " + finaltime);
            } else if (time1.equals("05")) {
                finaltime = "17";
            } else if (time1.equals("02")) {
                finaltime = "14";
            } else if (time1.equals("06")) {
                finaltime = "18";
            } else if (time1.equals("07")) {
                finaltime = "19";
            } else if (time1.equals("08")) {
                finaltime = "20";
            } else if (time1.equals("09")) {
                finaltime = "21";
            } else if (time1.equals("10")) {
                finaltime = "22";
            } else if (time1.equals("11")) {
                finaltime = "23";
            } else if (time1.equals("12")) {
                finaltime = "24";
            }
        } else {
            finaltime = time1;
        }
    }
    private void Notify() {
        Intent intentAlarm = new Intent(activity, MyBroadcastReceiver.class);
        MessageLogger.PrintMsg("broadcast called ");
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), PendingIntent.getBroadcast(activity, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));


    }
    //TODO NEHA



    @Override
    public int getItemCount() {
        return orderVisitDetailsModelsArr.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }



    public void setOnItemClickListener(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    public interface OnClickListeners {
        void onAcceptClicked(OrderVisitDetailsModel orderVisitDetailsModels);

        void onCallCustomer(OrderVisitDetailsModel orderVisitDetailsModels);

        void onItemRelease(OrderVisitDetailsModel orderVisitDetailsModel);

        void onItemReleaseTo(String pincode, OrderVisitDetailsModel orderVisitDetailsModel);

        void onStart(OrderVisitDetailsModel orderVisitDetailsModel);

        void onRefresh();
    }
}
