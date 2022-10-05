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
    int benCounter = 1, requiredBen;
    AppInterfaces.PatientSelector patientSelector;
    boolean isPatientListEdit;

    public SelectPeBenificiaryAdapter(boolean isPatientListEdit, int patientCount, GetPatientListResponseModel patientResponseModel, Activity activity, AppInterfaces.PatientSelector patientSelector) {
        this.patientResponseModel = patientResponseModel;
        this.activity = activity;
        this.patientSelector = patientSelector;
        this.requiredBen = patientCount;
        this.isPatientListEdit = isPatientListEdit;
    }

    @NonNull
    @Override
    public SelectPeBenificiaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_ben_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectPeBenificiaryAdapter.ViewHolder viewHolder, int position) {
        GetPatientListResponseModel.Data.patients singlePatient = patientResponseModel.getData().getPatients().get(position);

        if (isPatientListEdit) {
            viewHolder.cb_ben_selector.setSelected(singlePatient.getSelected());
        }

        viewHolder.cb_ben_selector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (benCounter < requiredBen) { //take data to parent activity and fill the model class.
                        benCounter++;
                        patientSelector.addPatient(position);
                    } else {
                        Toast.makeText(activity, "You cannot select more than" + requiredBen + "beneficiaries", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    benCounter--;
                    patientSelector.removePatient(position);
                }
            }
        });

        viewHolder.tv_editben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientSelector.editPatient(singlePatient);
            }
        });

        viewHolder.ben_name.setText(singlePatient.getName());
        viewHolder.ben_age_gender.setText(singlePatient.getAge() + " | " + singlePatient.getGender());

    }

    @Override
    public int getItemCount() {
        return patientResponseModel.getData().getPatients().size();
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