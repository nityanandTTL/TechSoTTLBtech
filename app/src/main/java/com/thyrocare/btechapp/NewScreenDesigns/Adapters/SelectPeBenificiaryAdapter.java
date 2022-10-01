package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.BtechInterfaces.AppInterfaces;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.models.api.request.GetPatientListResponseModel;

public class SelectPeBenificiaryAdapter extends RecyclerView.Adapter<SelectPeBenificiaryAdapter.ViewHolder> {
    GetPatientListResponseModel patientResponseModel;
    Activity activity;
    int benCounter, requiredBen;
    AppInterfaces.PatientSelector patientSelector;


    public SelectPeBenificiaryAdapter(GetPatientListResponseModel patientResponseModel, Activity activity, AppInterfaces.PatientSelector patientSelector) {
        this.patientResponseModel = patientResponseModel;
        this.activity = activity;
        this.patientSelector = patientSelector;
    }

    @NonNull
    @Override
    public SelectPeBenificiaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_ben_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectPeBenificiaryAdapter.ViewHolder viewHolder, int position) {
        viewHolder.cb_ben_selector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (benCounter <= requiredBen) { //take data to parent activity and fill the model class.
                        benCounter++;
                        patientResponseModel.getPatientDetailsList().get(position).setSelected(true);
                        patientSelector.addPatient(patientResponseModel);
                    } else {
                        Toast.makeText(activity, "You cannot select more than" + requiredBen + "beneficiaries", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    benCounter--;
                    patientResponseModel.getPatientDetailsList().get(position).setSelected(false);
                    patientSelector.addPatient(patientResponseModel);
                }
            }
        });

        viewHolder.tv_editben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientSelector.editPatient(patientResponseModel.getPatientDetailsList().get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_benpic;
        TextView ben_name, ben_age_gender, tv_editben;
        CheckBox cb_ben_selector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cb_ben_selector = itemView.findViewById(R.id.cb_ben_selector);
            iv_benpic = itemView.findViewById(R.id.iv_benpic);
            ben_name = itemView.findViewById(R.id.ben_name);
            ben_age_gender = itemView.findViewById(R.id.ben_age_gender);
            tv_editben = itemView.findViewById(R.id.tv_editben);

        }
    }
}
