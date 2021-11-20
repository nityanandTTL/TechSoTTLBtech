package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thyrocare.btechapp.Controller.SendLatLongforOrderController;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.TSP_OrdersDisplayFragment_new;
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
import com.thyrocare.btechapp.dialog.RescheduleOrderDialog;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.ServiceUpdateRequestModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.KitsCountModel;
import com.thyrocare.btechapp.models.data.OrderDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.DateUtils;
import com.thyrocare.btechapp.utils.app.GPSTracker;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import org.joda.time.DateTimeComparator;

import java.io.IOException;
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

public class TSP_OrderDisplayAdapterNew extends RecyclerView.Adapter<TSP_OrderDisplayAdapterNew.MyViewHolder> {

    private static final String TAG = TSP_OrderDisplayAdapterNew.class.getSimpleName();
    private final Activity activity;
    private String current_date;
    private AppPreferenceManager appPreferenceManager;
    private GPSTracker gpsTracker;
    private Global globalClass;
    private LayoutInflater layoutInflater;
    private HomeScreenActivity homeScreenActivity;
    private ArrayList<OrderVisitDetailsModel> orderVisitDetailsModelsArr;
    private OnClickListeners onClickListeners;
    private int fastingFlagInt;
    private String apiPlusFif, apiMinusFif;
    private String newTimeAfterMinusSixty1,  cancelVisit = "n", apiTime;
    private Date apitimeinHHMMFormat;
    private Date strDate;
    CharSequence[] items;
    TSP_OrdersDisplayFragment_new tsp_ordersDisplayFragment_new;
    private String userChoosenReleaseTask;
    private boolean isCancelRequesGenereted = false;

    public TSP_OrderDisplayAdapterNew(TSP_OrdersDisplayFragment_new mactivity, Activity activity, ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels) {
        this.activity = activity;
        this.tsp_ordersDisplayFragment_new = mactivity;
        this.orderVisitDetailsModelsArr = orderDetailsResponseModels;
        current_date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
        appPreferenceManager = new AppPreferenceManager(activity);
        gpsTracker = new GPSTracker(activity);
        globalClass = new Global(activity);
        layoutInflater = LayoutInflater.from(activity);
    }

    public void UpdateList(ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels){
        this.orderVisitDetailsModelsArr = orderDetailsResponseModels;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  txtBtechName, txtOrderNo, txtDate, txtSlot, txtBeneficiary, txtSamples, txtAddress,txtPPBSStatus,txtFastingStatus,txtRBSStatus,direct_visit,txtKits,txt_visit_day;
        ImageView imgRelease, imgCall,img_accept,imgProceed;
        LinearLayout layoutAccept_Release_Ord, layoutMain,lin_bencount,lin_kits,lin_btechName,LL_swipe,ll_accept;
        View view_seperater;
        RelativeLayout rel_imgRelease;

        public MyViewHolder(View view) {
            super(view);

            txtBtechName = (TextView) view.findViewById(R.id.txtBtechName);
            txtOrderNo = (TextView) view.findViewById(R.id.txtOrderNo);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtSlot = (TextView) view.findViewById(R.id.txtSlot);
            txtBeneficiary = (TextView) view.findViewById(R.id.txtBeneficiary);
            txtKits = (TextView) view.findViewById(R.id.txtKits);
            txtSamples = (TextView) view.findViewById(R.id.txtSamples);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            layoutAccept_Release_Ord = (LinearLayout) view.findViewById(R.id.layoutAccept_Release_Ord);
            layoutMain = (LinearLayout) view.findViewById(R.id.layoutMain);
            lin_bencount = (LinearLayout) view.findViewById(R.id.lin_bencount);
            lin_btechName = (LinearLayout) view.findViewById(R.id.lin_btechName);
            lin_kits = (LinearLayout) view.findViewById(R.id.lin_kits);
            txtFastingStatus = (TextView) view.findViewById(R.id.txtFastingStatus);
            txtPPBSStatus = (TextView) view.findViewById(R.id.txtPPBSStatus);
            txtRBSStatus = (TextView) view.findViewById(R.id.txtRBSStatus);
            direct_visit = (TextView) view.findViewById(R.id.direct_visit);
            txt_visit_day = (TextView) view.findViewById(R.id.txt_visit_day);
            img_accept = (ImageView) view.findViewById(R.id.img_accept);
            imgProceed = (ImageView) view.findViewById(R.id.imgStart);
            imgRelease = (ImageView) view.findViewById(R.id.imgRelease);
            imgCall = (ImageView) view.findViewById(R.id.imgCall);
            view_seperater = (View) view.findViewById(R.id.view_seperater);
            LL_swipe = view.findViewById(R.id.LL_swipe);
            rel_imgRelease = view.findViewById(R.id.rel_imgRelease);
            ll_accept = view.findViewById(R.id.ll_accept);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.btech_visit_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int pos) {

        try {
            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().size() > 0
                    && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().size() > 0
                    && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0) != null) {

                if (orderVisitDetailsModelsArr.get(pos).getAppointmentDate().equals(current_date)) {
                    holder.layoutMain.setBackgroundResource(R.drawable.rounded_background_green);
                    holder.LL_swipe.setBackgroundResource(R.drawable.rounded_background_green_2);
                } else {
                    holder.layoutMain.setBackgroundResource(R.drawable.rounded_background_yellow);
                    holder.LL_swipe.setBackgroundResource(R.drawable.rounded_background_yellow_2);
                }


                holder.txtDate.setText(orderVisitDetailsModelsArr.get(pos).getAppointmentDate());
                holder.txtSlot.setText(", "+DateUtil.Req_Date_Req(orderVisitDetailsModelsArr.get(pos).getSlot(),"hh:mm a","HH:mm"));
                holder.txtAddress.setSelected(true);
                holder.txtAddress.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getAddress());
                holder.txtOrderNo.setText(orderVisitDetailsModelsArr.get(pos).getVisitId());

                // Display Btech Name
                holder.lin_btechName.setVisibility(View.VISIBLE);
                holder.txtBtechName.setText(Global.toCamelCase(orderVisitDetailsModelsArr.get(pos).getBtechName()));

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

            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initLIsteners(final int pos, final MyViewHolder holder) {

        holder.ll_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAcceptButtonClicked(holder,pos);
            }
        });

        holder.rel_imgRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReleaseButtonClicked(pos,holder,holder.txtBtechName.getText().toString(),holder.txtOrderNo.getText().toString());
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
            holder.imgCall.setVisibility(View.GONE);
        }else {
            holder.direct_visit.setVisibility(View.GONE);
            holder.imgCall.setVisibility(View.VISIBLE);
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
//                        holder.imgRelease.setVisibility(View.GONE);
                        holder.rel_imgRelease.setVisibility(View.GONE);
                    }
                }
            }

            if (isValidForEditing(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase())) {
//                holder.imgRelease.setVisibility(View.GONE);
                holder.rel_imgRelease.setVisibility(View.GONE);
            }

            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.PPBS)
                    && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.FBS)) {

                if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.NBTTSP_ROLE_ID)) {
//                    holder.imgRelease.setVisibility(View.GONE);
                    holder.rel_imgRelease.setVisibility(View.GONE);
                } else {
//                    holder.imgRelease.setVisibility(View.VISIBLE);
                    holder.rel_imgRelease.setVisibility(View.VISIBLE);

                }
            }
            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.RBS)
                    && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode().toUpperCase().contains(AppConstants.FBS)) {

                if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.NBTTSP_ROLE_ID)) {
//                    holder.imgRelease.setVisibility(View.GONE);
                    holder.rel_imgRelease.setVisibility(View.GONE);
                } else {
//                    holder.imgRelease.setVisibility(View.VISIBLE);
                    holder.rel_imgRelease.setVisibility(View.VISIBLE);
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

        holder.imgProceed.setVisibility(View.GONE); // Proceed Option should be hide for TSP
        if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment") || orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED")) {
            fastingFlagInt = 0;
            holder.ll_accept.setVisibility(View.VISIBLE);
            holder.txtFastingStatus.setVisibility(View.GONE);
        } else {
            fastingFlagInt = 1;
            holder.ll_accept.setVisibility(View.GONE);
            holder.txtFastingStatus.setVisibility(View.VISIBLE);
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
                holder.txtFastingStatus.setVisibility(View.VISIBLE);
                holder.txtFastingStatus.setText("F/N");
            } else if (isFasting && !isNonFasting) {
                MessageLogger.LogError(TAG, "isFasting && !isNonFasting");
                holder.txtFastingStatus.setVisibility(View.VISIBLE);
                holder.txtFastingStatus.setText("F");
            } else if (!isFasting && isNonFasting) {
                MessageLogger.LogError(TAG, "!isFasting && isNonFasting");
                holder.txtFastingStatus.setVisibility(View.VISIBLE);
                holder.txtFastingStatus.setText("NF");
            } else {
                holder.txtFastingStatus.setVisibility(View.GONE);
            }
        }else {
            holder.txtFastingStatus.setVisibility(View.GONE);
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
            holder.txtKits.setText(strKit);
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
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.logout_bottomsheet, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));
        String s = "Do you want to accept order?";
        TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
        tv_text.setText(s);
        Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
        btn_yes.setText("Accept");
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fastingFlagInt = 1;
                holder.txtFastingStatus.setVisibility(View.VISIBLE);
                holder.imgRelease.setVisibility(View.VISIBLE);//remove 11 validation

                if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).isDirectVisit()) {
                    holder.imgCall.setVisibility(View.GONE);
                }else {
                    holder.imgCall.setVisibility(View.VISIBLE);
                }
                if (onClickListeners != null) {
                    onClickListeners.onAcceptClicked(orderVisitDetailsModelsArr.get(pos));
                }
                SendinglatlongOrderAllocation(pos);
                bottomSheetDialog.dismiss();
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

        /*final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage("Do you want to accept order?").setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                fastingFlagInt = 1;
                holder.txtFastingStatus.setVisibility(View.VISIBLE);
                holder.imgRelease.setVisibility(View.VISIBLE);//remove 11 validation

                if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).isDirectVisit()) {
                    holder.imgCall.setVisibility(View.GONE);
                }else {
                    holder.imgCall.setVisibility(View.VISIBLE);
                }

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
        builder1.show();*/

    }

    private void onReleaseButtonClicked(final int pos, MyViewHolder holder,String s_name,String s_order) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);

        final View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.layout_bottomsheet_release, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));

        TextView tv_name = bottomSheet.findViewById(R.id.tv_name);
        TextView tv_order_no = bottomSheet.findViewById(R.id.tv_order_no);
        TextView tv_ord_resch = bottomSheet.findViewById(R.id.tv_ord_resch);
        TextView tv_ord_rel = bottomSheet.findViewById(R.id.tv_ord_rel);
        TextView tv_ord_pass = bottomSheet.findViewById(R.id.tv_ord_pass);
        TextView tv_cancel_vst = bottomSheet.findViewById(R.id.tv_cancel_vst);
        Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
        Button btn_no = bottomSheet.findViewById(R.id.btn_no);
        LinearLayout ll_cancl = bottomSheet.findViewById(R.id.ll_cancl);

        tv_name.setText(s_name);
        tv_order_no.setText(s_order);

        boolean toShowResheduleOption = false;
        if (!InputUtils.isNull(orderVisitDetailsModelsArr.get(pos).getAppointmentDate())) {
            Date DeviceDate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date AppointDate = DateUtils.dateFromString(orderVisitDetailsModelsArr.get(pos).getAppointmentDate(), format);
            int daycount = DateTimeComparator.getDateOnlyInstance().compare(AppointDate, DeviceDate);
            if (daycount == 0) {
                toShowResheduleOption = true;
            } else {
                toShowResheduleOption = false;
            }
        }
       /* boolean toShowResheduleOption = false;
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
        }*/
        if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("fix appointment")
                || orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equalsIgnoreCase("ASSIGNED")) {
            if (isValidForEditing(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())) {
//                items = new String[]{"Do you want to cancel the visit?"};
                tv_ord_pass.setVisibility(View.GONE);
                tv_ord_rel.setVisibility(View.GONE);
                tv_ord_resch.setVisibility(View.GONE);
                ll_cancl.setVisibility(View.VISIBLE);
                tv_cancel_vst.setText("Do you want to cancel the visit?");
                cancelVisit = "y";
            } else {
                ll_cancl.setVisibility(View.GONE);
                tv_ord_pass.setVisibility(View.GONE);
                tv_ord_rel.setVisibility(View.VISIBLE);
                tv_ord_resch.setVisibility(View.GONE);
//                items = new String[]{"Order Release"};

            }
        } else {
            if (isValidForEditing(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())) {
                tv_ord_pass.setVisibility(View.GONE);
                tv_ord_rel.setVisibility(View.GONE);
                tv_ord_resch.setVisibility(View.GONE);
                ll_cancl.setVisibility(View.VISIBLE);
                tv_cancel_vst.setText("Do you want to cancel the visit?");
//                cancelVisit = "y";
                cancelVisit = "y";
            } else {
                if (toShowResheduleOption){
                    tv_ord_pass.setVisibility(View.GONE);
                    tv_ord_rel.setVisibility(View.VISIBLE);
                    tv_ord_resch.setVisibility(View.VISIBLE);
                    ll_cancl.setVisibility(View.GONE);
               /*
                    items = new String[]{"Order Reschedule",
                            "Request Release"};*/
                }else{
                    tv_ord_pass.setVisibility(View.GONE);
                    tv_ord_rel.setVisibility(View.VISIBLE);
                    tv_ord_resch.setVisibility(View.GONE);
                    ll_cancl.setVisibility(View.GONE);
//                    items = new String[]{"Request Release"};
                }
            }
        }

        tv_ord_resch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoosenReleaseTask = "Order Reschedule";
                RescheduleOrderDialog cdd = new RescheduleOrderDialog(activity, new TSP_OrderDisplayAdapterNew.OrderRescheduleDialogButtonClickedDelegateResult(), orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0));
                cdd.show();
                bottomSheetDialog.dismiss();
            }
        });

        tv_ord_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
                View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.logout_bottomsheet, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));
                TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
                TextView tv_text1 = bottomSheet.findViewById(R.id.tv_text1);
                tv_text.setText("Warning");
                tv_text1.setText("Rs 200 debit will be levied for releasing this Order");
                Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
                btn_yes.setText("Accept Debit");
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListeners != null) {
                            onClickListeners.onItemRelease(orderVisitDetailsModelsArr.get(pos));
                        }
                        bottomSheetDialog.dismiss();
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
                bottomSheetDialog.show();
            }
        });

        tv_ord_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onItemRelease(orderVisitDetailsModelsArr.get(pos));
                }
                bottomSheetDialog.dismiss();
            }
        });
       /* final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
                }  else if (items[item].equals("Do you want to cancel the visit?")) {

                }
            }
        });*/

        if (ll_cancl.getVisibility() == View.VISIBLE) {
            if (cancelVisit.equals("y")) {
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cancelVisit.equals("y")) {
                            if (isNetworkAvailable(activity)) {
                                CallServiceUpdateAPI(pos);
                            } else {
                                Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
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

  /*      if (cancelVisit.equals("y")) {
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
        builder.show();*/
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

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallOrderStatusChangeAPI(orderStatusChangeRequestModel,orderStatusChangeRequestModel.getId());
        globalClass.showProgressDialog(activity,activity.getResources().getString(R.string.progress_message_changing_order_status_please_wait));

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalClass.hideProgressDialog(activity);
                if (response.code() == 200) {
                    if (onClickListeners != null) {
                        onClickListeners.onRefreshAdapter();
                    }
                    if (userChoosenReleaseTask.equals("Visit Cancellation")) {
                        if (!isCancelRequesGenereted) {
                            isCancelRequesGenereted = true;
                            Toast.makeText(activity, "Order rescheduled successfully", Toast.LENGTH_SHORT).show();
                            if (onClickListeners != null) {
                                onClickListeners.onRefreshAdapter();
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
                globalClass.hideProgressDialog(activity);
                Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void CallServiceUpdateAPI(int pos) {

        ServiceUpdateRequestModel serviceUpdateRequestModel = new ServiceUpdateRequestModel();
        serviceUpdateRequestModel.setVisitId(orderVisitDetailsModelsArr.get(pos).getVisitId());
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallServiceUpdateAPI(serviceUpdateRequestModel);
        globalClass.showProgressDialog(activity,activity.getResources().getString(R.string.progress_message_changing_order_status_please_wait));

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalClass.hideProgressDialog(activity);
                if (response.code() == 200) {
                    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                    alertDialog.setMessage("Your visit has been cancelled successfully");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(activity,TSP_OrdersDisplayFragment_new.class);
                                    activity.startActivity(intent);
//                                    homeScreenActivity.pushFragments(TSP_OrdersDisplayFragment_new.newInstance(), false, false, TSP_OrdersDisplayFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TSP_OrdersDisplayFragment_new.TAG_FRAGMENT);
                                    dialog.dismiss();

                                }
                            });

                    alertDialog.show();

                } else {
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalClass.hideProgressDialog(activity);
                MessageLogger.LogDebug("Errror", t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderVisitDetailsModelsArr.size();
    }

    public void setOnItemClickListener(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    public interface OnClickListeners {
        void onAcceptClicked(OrderVisitDetailsModel orderVisitDetailsModels);

        void onCallCustomer(OrderVisitDetailsModel orderVisitDetailsModels);

        void onRefreshAdapter();

        void onItemRelease(OrderVisitDetailsModel orderVisitDetailsModel);

    }


}
