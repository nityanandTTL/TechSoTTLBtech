package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.nfc.cardemulation.OffHostApduService;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CampModuleMISResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.DateUtil;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.models.data.TestRateMasterModel;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CampWOE_MIS_Adpter extends RecyclerView.Adapter<CampWOE_MIS_Adpter.MyViewHolder> {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private AppPreferenceManager appPreferenceManager;
    private Global globalClass;
    private ArrayList<CampModuleMISResponseModel.Output> CampWOEMisArrayList;
    private ArrayList<CampModuleMISResponseModel.Output> filterArryList;
    private OnClickListeners onClickListeners;

    public CampWOE_MIS_Adpter(Activity activity, ArrayList<CampModuleMISResponseModel.Output> CampWOEMisArrayList) {
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        globalClass = new Global(activity);
        this.CampWOEMisArrayList = CampWOEMisArrayList;
        this.filterArryList = CampWOEMisArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_patientId, tv_sct,tv_Name_ageGender,tv_vailPhoto,tv_Barcode;
        LinearLayout lin_main;

        public MyViewHolder(View view) {
            super(view);

            tv_patientId = (TextView) view.findViewById(R.id.tv_patientId);
            tv_sct = (TextView) view.findViewById(R.id.tv_sct);
            tv_Name_ageGender = (TextView) view.findViewById(R.id.tv_Name_ageGender);
            tv_vailPhoto = (TextView) view.findViewById(R.id.tv_vailPhoto);
            tv_Barcode = (TextView) view.findViewById(R.id.tv_Barcode);
            lin_main = (LinearLayout) view.findViewById(R.id.lin_main);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.camp_woe_mis_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tv_patientId.setText(filterArryList.get(position).getPatientID());
        holder.tv_sct.setText(DateUtil.Req_Date_Req(filterArryList.get(position).getSct(),"MM/dd/yyyy hh:mm:ss a","dd/MM/yyyy  HH:mm"));
        holder.tv_Name_ageGender.setText(filterArryList.get(position).getName());
        /*List<String> items = null;
        try {
            items = Arrays.asList(filterArryList.get(position).getBarcode().split("\\s*,\\s*"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tv_Barcode.setEnabled(false);
        if (items != null && items.size() > 0){
            if (items.size() == 1){
                holder.tv_Barcode.setText(filterArryList.get(position).getBarcode());
            }else{
                int size = items.size() - 1;
                holder.tv_Barcode.setText(Html.fromHtml("<u>" + items.get(0) + ", +" + size + "</u>"));
                holder.tv_Barcode.setEnabled(true);
            }
        }else{
            holder.tv_Barcode.setText(filterArryList.get(position).getBarcode());
        }*/
        holder.tv_Barcode.setText(filterArryList.get(position).getBarcode());


        if (StringUtils.isNull(filterArryList.get(position).getVialImage())){
            holder.tv_vailPhoto.setText(Html.fromHtml("<u>Upload Vial Image</u>"));
        }else{
            holder.tv_vailPhoto.setText(Html.fromHtml("<u>Vial Image</u>"));
        }

       /* holder.tv_Barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalClass.showalert_OK(filterArryList.get(position).getBarcode(),activity);
            }
        });*/

        holder.tv_vailPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isNull(filterArryList.get(position).getVialImage())){
                    globalClass.OpenImageDialog(filterArryList.get(position).getVialImage(),activity,true);
                }else{
                    if (onClickListeners != null){
                        onClickListeners.onUploadVialImageClicked(filterArryList.get(position));
                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return filterArryList.size();
    }

    public void filterData(String query) {
        query = query.toLowerCase();

        if (query.isEmpty()) {
            filterArryList = new ArrayList<>();
            filterArryList.addAll(CampWOEMisArrayList);
            if (onClickListeners != null){
                onClickListeners.onFilter(true);
            }
        } else {
            filterArryList = new ArrayList<>();

            ArrayList<CampModuleMISResponseModel.Output> oldList = CampWOEMisArrayList;
            ArrayList<CampModuleMISResponseModel.Output> newList = new ArrayList<CampModuleMISResponseModel.Output>();
            for (CampModuleMISResponseModel.Output output : oldList) {
                if (output.getName().toLowerCase().contains(query) ||
                        output.getPatientID().toLowerCase().contains(query) ||
                        output.getBarcode().toLowerCase().contains(query)) {
                    newList.add(output);
                }
            }
            if (newList.size() > 0) {
                filterArryList.addAll(newList);
                if (onClickListeners != null){
                    onClickListeners.onFilter(true);
                }
            }else{
                if (onClickListeners != null){
                    onClickListeners.onFilter(false);
                }
            }
        }
        notifyDataSetChanged();
    }



    public void setOnItemClickListener(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    public interface OnClickListeners {

        void onFilter(boolean isDataavailable);
        void onUploadVialImageClicked(CampModuleMISResponseModel.Output output);


    }
}
