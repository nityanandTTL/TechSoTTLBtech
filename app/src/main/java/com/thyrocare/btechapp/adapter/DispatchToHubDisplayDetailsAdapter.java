package com.thyrocare.btechapp.adapter;

/**
 * Created by Orion on 4/21/2017.
 */

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.activity.HubDetailMapDisplayFragmentActivity;
import com.thyrocare.btechapp.delegate.DispatchToHubAdapterOnItemClickedDelegate;
import com.thyrocare.btechapp.models.data.HUBBTechModel;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.BundleConstants;

import java.util.List;

public class DispatchToHubDisplayDetailsAdapter extends RecyclerView.Adapter<DispatchToHubDisplayDetailsAdapter.MyViewHolder> {

    private List<HUBBTechModel> hubbTechModels;
    Activity activity;
    DispatchToHubAdapterOnItemClickedDelegate mcallback;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sr_no, tv_name, tv_age, tv_aadhar_no, txtorder_no, txt_title;//, tv_distance, tv_kits;
        public ImageView img_release, img_edit, title_aadhar_icon;//, title_distance_icon, title_kits_icon;
        View itemView;

        public MyViewHolder(View view) {
            super(view);
            this.itemView = view;
            initComp(view);
        }

        private void initComp(View view) {
            txtorder_no = (TextView) view.findViewById(R.id.tv_orderno);
            txt_title = (TextView) view.findViewById(R.id.oderno_title);

            tv_sr_no = (TextView) view.findViewById(R.id.txt_sr_no);
            tv_name = (TextView) view.findViewById(R.id.txt_name);
            tv_name.setSelected(true);
            tv_age = (TextView) view.findViewById(R.id.txt_age);
            tv_aadhar_no = (TextView) view.findViewById(R.id.txt_aadhar_no);
//            tv_distance = (TextView) view.findViewById(R.id.tv_distance);
//            tv_kits = (TextView) view.findViewById(R.id.tv_kits);
            img_edit = (ImageView) view.findViewById(R.id.img_edit);
            img_release = (ImageView) view.findViewById(R.id.img_release2);
            title_aadhar_icon = (ImageView) view.findViewById(R.id.title_aadhar_icon);
//            title_distance_icon = (ImageView) view.findViewById(R.id.title_distance_icon);
//            title_kits_icon = (ImageView) view.findViewById(R.id.title_kits_icon);
            img_edit.setVisibility(View.GONE);
            img_release.setVisibility(View.GONE);
        }
    }


    public DispatchToHubDisplayDetailsAdapter(List<HUBBTechModel> hubbTechModels, Activity activity, DispatchToHubAdapterOnItemClickedDelegate mCallback) {
        this.mcallback = mCallback;
        this.hubbTechModels = hubbTechModels;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_title_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final HUBBTechModel hubbTechModel = hubbTechModels.get(position);

        final int pos = position;
        if (hubbTechModel != null) {

            holder.tv_name.setText("" + hubbTechModel.getIncharge());
            holder.tv_age.setText("CutOff Time -" + hubbTechModel.getCutOffTime());
            holder.tv_aadhar_no.setText(hubbTechModel.getAddress());
            holder.title_aadhar_icon.setVisibility(View.GONE);
            holder.txtorder_no.setVisibility(View.GONE);
            holder.txt_title.setVisibility(View.GONE);
            holder.tv_sr_no.setText(pos + 1 + "");
            if (holder.itemView.getParent() != null)
                ((ViewGroup) holder.itemView.getParent()).removeView(holder.itemView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mcallback.onItemClicked(hubbTechModel);
//                    activity.startActivity(new Intent(activity, HubDetailMapDisplayFragmentActivity.class));
                    Intent intent = new Intent(activity,HubDetailMapDisplayFragmentActivity.class);
                    intent.putExtra(BundleConstants.HUB_BTECH_MODEL,hubbTechModels.get(pos));
                    activity.startActivity(intent);
                    //activity.pushFragments(HubDetailMapDisplayFragmentActivity.newInstance(hubbTechModels.get(pos)), false, false, HubDetailMapDisplayFragmentActivity.TAG_FRAGMENT, R.id.fl_homeScreen, HubListDisplayFragment.TAG_FRAGMENT);
                }
            });
        } else {
            Logger.error("hubDetailsResponseModel is null ");
        }
    }

    @Override
    public int getItemCount() {
        return hubbTechModels.size();
    }
}
