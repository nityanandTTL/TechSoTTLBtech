package com.dhb.adapter;

/**
 * Created by vendor1 on 4/21/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.delegate.DispatchToHubAdapterOnItemClickedDelegate;
import com.dhb.models.data.HUBBTechModel;
import com.dhb.utils.api.Logger;

import java.util.List;

public class DispatchToHubDisplayDetailsAdapter extends RecyclerView.Adapter<DispatchToHubDisplayDetailsAdapter.MyViewHolder> {

    private List<HUBBTechModel> hubbTechModels;
    HomeScreenActivity activity;
    DispatchToHubAdapterOnItemClickedDelegate mcallback;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sr_no, tv_name, tv_age, tv_aadhar_no;//, tv_distance, tv_kits;
        public ImageView img_release, img_edit, title_aadhar_icon;//, title_distance_icon, title_kits_icon;
        View itemView;

        public MyViewHolder(View view) {
            super(view);
            this.itemView =  view;
            initComp(view);
        }

        private void initComp(View view) {
            tv_sr_no = (TextView) view.findViewById(R.id.txt_sr_no);
            tv_name = (TextView) view.findViewById(R.id.txt_name);
            tv_age = (TextView) view.findViewById(R.id.txt_age);
            tv_aadhar_no = (TextView) view.findViewById(R.id.txt_aadhar_no);
//            tv_distance = (TextView) view.findViewById(R.id.tv_distance);
//            tv_kits = (TextView) view.findViewById(R.id.tv_kits);
            img_edit = (ImageView) view.findViewById(R.id.img_edit);
            img_release = (ImageView) view.findViewById(R.id.img_release);
            title_aadhar_icon = (ImageView) view.findViewById(R.id.title_aadhar_icon);
//            title_distance_icon = (ImageView) view.findViewById(R.id.title_distance_icon);
//            title_kits_icon = (ImageView) view.findViewById(R.id.title_kits_icon);
            img_edit.setVisibility(View.GONE);
            img_release.setVisibility(View.GONE);
        }
    }


    public DispatchToHubDisplayDetailsAdapter(List<HUBBTechModel> hubbTechModels, HomeScreenActivity activity, DispatchToHubAdapterOnItemClickedDelegate mCallback) {
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
            holder.tv_age.setText(hubbTechModel.getCutOffTime());
            holder.tv_aadhar_no.setText(hubbTechModel.getAddress());
            holder.title_aadhar_icon.setVisibility(View.GONE);
            holder.tv_sr_no.setText(pos+1+"");
            if(holder.itemView.getParent()!=null)
                ((ViewGroup)holder.itemView.getParent()).removeView(holder.itemView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mcallback.onItemClicked(hubbTechModel);
                    //activity.pushFragments(HubDetailMapDisplayFragmentActivity.newInstance(hubbTechModels.get(pos)), false, false, HubDetailMapDisplayFragmentActivity.TAG_FRAGMENT, R.id.fl_homeScreen, DispatchToHubFragment.TAG_FRAGMENT);
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
