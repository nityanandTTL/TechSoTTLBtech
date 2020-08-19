package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Fragments.VisitOrdersDisplayFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.DateUtil;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.thyrocare.btechapp.dialog.RescheduleOrderDialog;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.ServiceUpdateRequestModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.OrderDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.DateUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import org.joda.time.DateTimeComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;

public class StartArriveOrderDetailsAdapter extends RecyclerView.Adapter<StartArriveOrderDetailsAdapter.MyViewHolder> {

    private Activity activity;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    private LayoutInflater layoutInflater;
    private AppPreferenceManager appPreferenceManager;
    private Global globalClass;
    private ArrayList<BeneficiaryDetailsModel> BenMasterArray;
    private String Status = "Start";
    private OnClickListeners onClickListeners;
    CharSequence[] items;
    private String cancelVisit;
    private String userChoosenReleaseTask;
    private boolean isCancelRequesGenereted = false;


    public StartArriveOrderDetailsAdapter(Activity activity, OrderVisitDetailsModel orderVisitDetailsModel, ArrayList<BeneficiaryDetailsModel> BenMasterArray, String Status) {
        this.activity = activity;
        this.orderVisitDetailsModel = orderVisitDetailsModel;
        this.BenMasterArray = BenMasterArray;
        layoutInflater = LayoutInflater.from(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        globalClass = new Global(activity);
        this.Status = Status;
    }

    public void updateStatus(String Status) {
        this.Status = Status;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout linBenDetails,lin_BeforeArrive,lin_benProduct, lin_firstBenDetails, lin_benName, lin_Edit_delete_Ben, lin_fastingDetails, lin_MobileNo, lin_EmailID;
        private final TextView tv_FirstBenName, tv_OrderNo, tv_Address, tv_MobileNo, tv_EmailID, tv_AppointmentDateTime, tv_benName, tv_benProduct, tv_benPrice;
        private final ImageView img_editBenDetails, img_DeleteBen,imgCall;

        public MyViewHolder(View itemView) {
            super(itemView);

            linBenDetails = (LinearLayout) itemView.findViewById(R.id.linBenDetails);
            lin_BeforeArrive = (LinearLayout) itemView.findViewById(R.id.lin_BeforeArrive);
            lin_firstBenDetails = (LinearLayout) itemView.findViewById(R.id.lin_firstBenDetails);
            lin_benName = (LinearLayout) itemView.findViewById(R.id.lin_benName);
            lin_benProduct = (LinearLayout) itemView.findViewById(R.id.lin_benProduct);
            lin_Edit_delete_Ben = (LinearLayout) itemView.findViewById(R.id.lin_Edit_delete_Ben);
            lin_fastingDetails = (LinearLayout) itemView.findViewById(R.id.lin_fastingDetails);
            lin_MobileNo = (LinearLayout) itemView.findViewById(R.id.lin_MobileNo);
            lin_EmailID = (LinearLayout) itemView.findViewById(R.id.lin_EmailID);

            tv_FirstBenName = (TextView) itemView.findViewById(R.id.tv_FirstBenName);
            tv_OrderNo = (TextView) itemView.findViewById(R.id.tv_OrderNo);
            tv_Address = (TextView) itemView.findViewById(R.id.tv_Address);
            tv_MobileNo = (TextView) itemView.findViewById(R.id.tv_MobileNo);
            tv_EmailID = (TextView) itemView.findViewById(R.id.tv_EmailID);
            tv_AppointmentDateTime = (TextView) itemView.findViewById(R.id.tv_AppointmentDateTime);
            tv_benName = (TextView) itemView.findViewById(R.id.tv_benName);
            tv_benProduct = (TextView) itemView.findViewById(R.id.tv_benProduct);
            tv_benPrice = (TextView) itemView.findViewById(R.id.tv_benPrice);

            imgCall = (ImageView) itemView.findViewById(R.id.imgCall);
            img_editBenDetails = (ImageView) itemView.findViewById(R.id.img_editBenDetails);
            img_DeleteBen = (ImageView) itemView.findViewById(R.id.img_DeleteBen);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.start_arive_adpater_item_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (position == 0) {
            holder.tv_FirstBenName.setVisibility(View.VISIBLE);
            holder.lin_firstBenDetails.setVisibility(View.VISIBLE);
            holder.lin_benName.setVisibility(View.GONE);
            if (Status.equalsIgnoreCase("Arrive")) {
                holder.lin_BeforeArrive.setVisibility(View.GONE);
            }else{
                holder.lin_BeforeArrive.setVisibility(View.VISIBLE);
            }
        } else {
            holder.tv_FirstBenName.setVisibility(View.GONE);
            holder.lin_firstBenDetails.setVisibility(View.GONE);
            holder.lin_benName.setVisibility(View.VISIBLE);
            holder.imgCall.setVisibility(View.GONE);
        }

        holder.lin_MobileNo.setVisibility(View.GONE); // AS per Input received by Sameer Sir

        if (orderVisitDetailsModel.getAllOrderdetails().get(0).isDirectVisit()) {
            holder.lin_EmailID.setVisibility(View.GONE);
        } else {
            holder.lin_EmailID.setVisibility(View.VISIBLE);
        }

        if (BenMasterArray.get(position).getFasting().equalsIgnoreCase("Fasting")) {
            holder.lin_fastingDetails.setVisibility(View.VISIBLE);
        } else {
            holder.lin_fastingDetails.setVisibility(View.GONE);
        }

        if (Status.equalsIgnoreCase("Arrive")) {
            if (isValidForEditing(BenMasterArray.get(position).getTestsCode())) {
                holder.lin_Edit_delete_Ben.setVisibility(View.GONE);
            } else if (orderVisitDetailsModel.getAllOrderdetails().get(0).isEditOrder()) {
                if (BenMasterArray.size() > 1) {
                    holder.lin_Edit_delete_Ben.setVisibility(View.VISIBLE);
                    holder.img_editBenDetails.setVisibility(View.VISIBLE);
                    holder.img_DeleteBen.setVisibility(View.VISIBLE);
                } else {
                    holder.img_editBenDetails.setVisibility(View.VISIBLE);
                    holder.img_DeleteBen.setVisibility(View.GONE);
                    holder.lin_Edit_delete_Ben.setVisibility(View.VISIBLE);
                }
            } else {
                holder.lin_Edit_delete_Ben.setVisibility(View.GONE);
            }
        } else {
            holder.lin_Edit_delete_Ben.setVisibility(View.GONE);
        }


        holder.tv_FirstBenName.setText(BenMasterArray.get(position).getName() + "(" + BenMasterArray.get(position).getGender() + " / " + BenMasterArray.get(position).getAge() + ")");
        holder.tv_OrderNo.setText(!StringUtils.isNull(orderVisitDetailsModel.getVisitId()) ? orderVisitDetailsModel.getVisitId() : "");
        holder.tv_Address.setText(!StringUtils.isNull(orderVisitDetailsModel.getAllOrderdetails().get(0).getAddress()) ? orderVisitDetailsModel.getAllOrderdetails().get(0).getAddress() : "");
        holder.tv_MobileNo.setText(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
        holder.tv_EmailID.setText(!StringUtils.isNull(orderVisitDetailsModel.getAllOrderdetails().get(0).getEmail()) ? orderVisitDetailsModel.getAllOrderdetails().get(0).getEmail() : "");
        holder.tv_AppointmentDateTime.setText(orderVisitDetailsModel.getAllOrderdetails().get(0).getAppointmentDate() + ", " + DateUtil.Req_Date_Req(orderVisitDetailsModel.getAllOrderdetails().get(0).getSlot(),"hh:mm a","HH:mm"));
        holder.tv_benName.setText(BenMasterArray.get(position).getName() + "(" + BenMasterArray.get(position).getGender() + " / " + BenMasterArray.get(position).getAge() + ")");
        if (orderVisitDetailsModel.getAllOrderdetails().get(0).isDisplayProduct()){
            holder.tv_benProduct.setText(Html.fromHtml( !StringUtils.isNull(BenMasterArray.get(position).getTestsCode()) ? "<u>"+BenMasterArray.get(position).getTestsCode()+"</u>" : ""));
            holder.lin_benProduct.setVisibility(View.VISIBLE);
        }else{
            holder.lin_benProduct.setVisibility(View.GONE);
        }

        holder.tv_benPrice.setText("" + orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue());

        InitListener(holder, position);

    }

    private boolean isValidForEditing(String tests) {

        if (!TextUtils.isEmpty(tests)) {
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
        }


        return false;
    }

    private void InitListener(MyViewHolder holder, final int position) {

        holder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onCallCustomer();
                }
            }
        });


        holder.img_DeleteBen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListeners != null) {
                    onClickListeners.onDeleteBenClicked(orderVisitDetailsModel, BenMasterArray.get(position));
                }
            }
        });

        holder.img_editBenDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListeners != null) {
                    onClickListeners.onEditBenClicked(orderVisitDetailsModel, BenMasterArray.get(position), position);
                }
            }
        });

        holder.tv_benProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListeners != null) {
                    onClickListeners.onShowTestDetailsnClicked(BenMasterArray.get(position).getLeadId());
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return BenMasterArray.size();
    }

    public void setOnItemClickListener(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    public interface OnClickListeners {
        void onEditBenClicked(OrderVisitDetailsModel orderVisitDetailsModel, BeneficiaryDetailsModel beneficiaryDetailsModel, int position);

        void onDeleteBenClicked(OrderVisitDetailsModel orderVisitDetailsModel, BeneficiaryDetailsModel beneficiaryDetailsModel);

        void onRefresh();

        void onShowTestDetailsnClicked(String benId);

        void onCallCustomer();


    }
}
