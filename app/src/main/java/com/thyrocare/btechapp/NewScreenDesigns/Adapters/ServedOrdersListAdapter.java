package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.btechapp.Controller.BottomSheetController;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.ServedOrderResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;


public class ServedOrdersListAdapter extends RecyclerView.Adapter<ServedOrdersListAdapter.MyViewHolder> {

    Activity mActivity;
    ArrayList<ServedOrderResponseModel.btchOrd> ServedOrderArylist;
    Global globalclass;
    ConnectionDetector cd;
    OnItemClickListener onItemClickListener;

    public ServedOrdersListAdapter(Activity pActivity, ArrayList<ServedOrderResponseModel.btchOrd> ServedOrderArylist) {

        mActivity = pActivity;
        this.ServedOrderArylist = ServedOrderArylist;
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Name, tv_OrderID, tv_barcodes, tv_Bencount, tv_Fasting, tv_Order_status, tv_orderAmount, tv_sample_location;
        RelativeLayout rl_receipt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name = (TextView) itemView.findViewById(R.id.tv_Name);
            tv_OrderID = (TextView) itemView.findViewById(R.id.tv_OrderID);
            tv_barcodes = (TextView) itemView.findViewById(R.id.tv_barcodes);
            tv_Bencount = (TextView) itemView.findViewById(R.id.tv_Bencount);
            tv_Fasting = (TextView) itemView.findViewById(R.id.tv_Fasting);
            tv_Order_status = (TextView) itemView.findViewById(R.id.tv_Order_status);
            tv_orderAmount = (TextView) itemView.findViewById(R.id.tv_orderAmount);
            tv_sample_location = itemView.findViewById(R.id.tv_sample_location);
            rl_receipt = itemView.findViewById(R.id.rl_receipt);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.served_order_list_item_new, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        if (ServedOrderArylist.get(position).getLocation() != null) {
            if (ServedOrderArylist.get(position).getLocation().equalsIgnoreCase(ConstantsMessages.RPL)) {
                holder.tv_sample_location.setText("Sample Processed at RPL");
//                holder.tv_Name.setTextColor(mActivity.getResources().getColor(R.color.blue_shade));
            } else if (ServedOrderArylist.get(position).getLocation().equalsIgnoreCase(ConstantsMessages.CPL)) {
                holder.tv_sample_location.setText("Sample Processed at CPL");
//                holder.tv_Name.setTextColor(mActivity.getResources().getColor(R.color.highlight_color));
            } else if (ServedOrderArylist.get(position).getLocation().equalsIgnoreCase(ConstantsMessages.ZPL)) {
                holder.tv_sample_location.setText("Sample Processed at ZPL");
//                holder.tv_Name.setTextColor(mActivity.getResources().getColor(R.color.sample_type_serum));
            }
        } else {
            holder.tv_Name.setTextColor(mActivity.getResources().getColor(R.color.black));
        }

        holder.tv_Name.setText(ServedOrderArylist.get(position).getOrderBy());
        holder.tv_OrderID.setText(ServedOrderArylist.get(position).getOrderNo());
        holder.tv_Order_status.setText(ServedOrderArylist.get(position).getStatus());
        holder.tv_orderAmount.setText("" + ServedOrderArylist.get(position).getAmountCollected());
        holder.tv_Bencount.setText("(" + ServedOrderArylist.get(position).getBenCount() + " Ben)");

        if (ServedOrderArylist.get(position).getBtchBracodeDtl() != null && ServedOrderArylist.get(position).getBtchBracodeDtl().size() > 0) {

            if (ServedOrderArylist.get(position).getBtchBracodeDtl().size() == 1) {
                holder.tv_barcodes.setText("" + ServedOrderArylist.get(position).getBtchBracodeDtl().get(0).getBarcode());
            } else {
                int barcodecount = ServedOrderArylist.get(position).getBtchBracodeDtl().size() - 1;
                holder.tv_barcodes.setText("" + ServedOrderArylist.get(position).getBtchBracodeDtl().get(0).getBarcode() + ", + " + barcodecount);
                holder.tv_barcodes.setPaintFlags(holder.tv_barcodes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }

        }
        if (!StringUtils.isNull(ServedOrderArylist.get(position).getFasting())) {
            if (ServedOrderArylist.get(position).getFasting().equalsIgnoreCase("yes")) {
                holder.tv_Fasting.setText("Fasting : Yes");
            } else {
                holder.tv_Fasting.setText("Non-Fasting : No");
            }
        }


        holder.tv_barcodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ServedOrderArylist.get(position).getBtchBracodeDtl().size() > 1) {
                    String barcode = "";
                    for (int i = 0; i < ServedOrderArylist.get(position).getBtchBracodeDtl().size(); i++) {
                        int a = i + 1;
                        barcode = barcode + a + ". " + ServedOrderArylist.get(position).getBtchBracodeDtl().get(i).getBarcode() + "\n";
                    }

                    BottomSheetController bottomSheetController = new BottomSheetController(mActivity);
                    bottomSheetController.SetOKBottomSheet(barcode);

//                    globalclass.showalert_OK(barcode, mActivity);
                }
            }
        });


        holder.rl_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cd.isConnectingToInternet()) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onReceiptDownloadClicked(ServedOrderArylist.get(position).getOrderNo());
                    }
                } else
                    globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);

            }
        });
    }

    @Override
    public int getItemCount() {
        return ServedOrderArylist.size();
    }

    public void setOnItemClickListener(OnItemClickListener onOrdersItemClickListener) {
        this.onItemClickListener = onOrdersItemClickListener;
    }

    public interface OnItemClickListener {

        void onCallbuttonClicked(String mobilenumber);

        void onReceiptDownloadClicked(String Orderno);
    }
}
