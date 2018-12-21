package com.thyrocare.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thyrocare.R;
import com.thyrocare.delegate.SelectLabAlertsCheckboxDelegate;
import com.thyrocare.models.data.BeneficiaryLabAlertsModel;
import com.thyrocare.models.data.LabAlertMasterModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/10/2017.
 */
public class SelectLabAlertsMasterDisplayAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<BeneficiaryLabAlertsModel> sLAArr;
    private ArrayList<LabAlertMasterModel> labAlertMasterModelsArr;
    private int benId;
    private SelectLabAlertsCheckboxDelegate selectLabAlertsCheckboxDelegate;
    public SelectLabAlertsMasterDisplayAdapter(Activity activity, ArrayList<LabAlertMasterModel> labAlertMasterModelsArr, ArrayList<BeneficiaryLabAlertsModel> sLAArr, int benId,SelectLabAlertsCheckboxDelegate selectLabAlertsCheckboxDelegate) {
        this.activity = activity;
        this.labAlertMasterModelsArr = labAlertMasterModelsArr;
        this.sLAArr = sLAArr;
        this.benId = benId;
        this.selectLabAlertsCheckboxDelegate = selectLabAlertsCheckboxDelegate;
    }

    @Override
    public int getCount() {
        return labAlertMasterModelsArr.size();
    }

    @Override
    public Object getItem(int position) {
        return labAlertMasterModelsArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_select_clinical_history_list_view,parent,false);
            holder = new ViewHolder();
            holder.txtLabAlert = (TextView) convertView.findViewById(R.id.txt_test);
            holder.imgCheck = (ImageView) convertView.findViewById(R.id.img_check);
            holder.imgChecked = (ImageView) convertView.findViewById(R.id.img_checked);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtLabAlert.setText(labAlertMasterModelsArr.get(position).getLabAlert());
        final BeneficiaryLabAlertsModel beneficiaryLabAlertsModel = new BeneficiaryLabAlertsModel();
        beneficiaryLabAlertsModel.setBenId(benId);
        beneficiaryLabAlertsModel.setLabAlertId(labAlertMasterModelsArr.get(position).getLabAlertId());
        if(sLAArr!=null) {
            if (sLAArr.contains(beneficiaryLabAlertsModel)) {
                holder.imgChecked.setVisibility(View.VISIBLE);
                holder.imgCheck.setVisibility(View.GONE);
            } else {
                holder.imgChecked.setVisibility(View.GONE);
                holder.imgCheck.setVisibility(View.VISIBLE);
            }
        }
        else{
            sLAArr = new ArrayList<>();
        }
        holder.imgChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sLAArr.remove(beneficiaryLabAlertsModel);
                selectLabAlertsCheckboxDelegate.onCheckChange(sLAArr);
            }
        });
        holder.imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sLAArr.add(beneficiaryLabAlertsModel);
                selectLabAlertsCheckboxDelegate.onCheckChange(sLAArr);
            }
        });
        return convertView;
    }
    private class ViewHolder{
        TextView txtLabAlert;
        ImageView imgCheck;
        ImageView imgChecked;
    }
}
