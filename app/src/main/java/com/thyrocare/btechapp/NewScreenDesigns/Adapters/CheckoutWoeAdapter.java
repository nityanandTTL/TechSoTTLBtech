package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;

public class CheckoutWoeAdapter extends RecyclerView.Adapter<CheckoutWoeAdapter.MyViewHolder> {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private AppPreferenceManager appPreferenceManager;
    private Global globalClass;
    private ArrayList<BeneficiaryDetailsModel> BenMasterArray;
    private boolean showProduct = false;
//    private OnClickListeners onClickListeners;

    public CheckoutWoeAdapter(Activity activity, ArrayList<BeneficiaryDetailsModel> BenMasterArray, boolean showProduct) {
        this.activity = activity;
        this.BenMasterArray = BenMasterArray;
        layoutInflater = LayoutInflater.from(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        globalClass = new Global(activity);
        this.showProduct = showProduct;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_woe_adapter_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_benName.setText(Global.toCamelCase(BenMasterArray.get(position).getName()) + " (" + BenMasterArray.get(position).getGender() + " | " + BenMasterArray.get(position).getAge() + " years)");
        if (showProduct) {
            holder.tv_benProduct.setText(!StringUtils.isNull(BenMasterArray.get(position).getTestsCode()) ? BenMasterArray.get(position).getTestsCode() : "");
            holder.lin_benProduct.setVisibility(View.VISIBLE);
        } else {
            holder.lin_benProduct.setVisibility(View.GONE);
        }

        if (BenMasterArray.get(position).getBarcodedtl() != null && BenMasterArray.get(position).getBarcodedtl().size() > 0) {
            String barcodes = "";
            for (int i = 0; i < BenMasterArray.get(position).getBarcodedtl().size(); i++) {
                barcodes = barcodes + BenMasterArray.get(position).getBarcodedtl().get(i).getSamplType() + "  :  " + BenMasterArray.get(position).getBarcodedtl().get(i).getBarcode() + "\n";
            }
            holder.tv_benbarcode.setText(barcodes.trim());
        }


        /*if (BenMasterArray.get(position).getFasting().equalsIgnoreCase("Fasting")) {
            holder.lin_fastingDetails.setVisibility(View.VISIBLE);
        } else {
            holder.lin_fastingDetails.setVisibility(View.GONE);
        }*/


        holder.tv_benbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BenMasterArray.get(position).getBarcodedtl() != null && BenMasterArray.get(position).getBarcodedtl().size() > 0) {
                    String barcodes = "";
                    for (int i = 0; i < BenMasterArray.get(position).getBarcodedtl().size(); i++) {
                        barcodes = barcodes + BenMasterArray.get(position).getBarcodedtl().get(i).getSamplType() + "  :  " + BenMasterArray.get(position).getBarcodedtl().get(i).getBarcode() + "\n";
                    }
                    globalClass.showAlert_OK_WithTitle(barcodes, activity, "Barcode");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return BenMasterArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout linBenDetails, lin_benName, lin_benProduct, lin_fastingDetails;
        private TextView tv_benName, tv_benProduct, tv_benbarcode;

        public MyViewHolder(View itemView) {
            super(itemView);

            linBenDetails = (LinearLayout) itemView.findViewById(R.id.linBenDetails);
            lin_benName = (LinearLayout) itemView.findViewById(R.id.lin_benName);
            lin_benProduct = (LinearLayout) itemView.findViewById(R.id.lin_benProduct);
            lin_fastingDetails = (LinearLayout) itemView.findViewById(R.id.lin_fastingDetails);

            tv_benName = (TextView) itemView.findViewById(R.id.tv_benName);
            tv_benProduct = (TextView) itemView.findViewById(R.id.tv_benProduct);
            tv_benbarcode = (TextView) itemView.findViewById(R.id.tv_benbarcode);

        }
    }


}
