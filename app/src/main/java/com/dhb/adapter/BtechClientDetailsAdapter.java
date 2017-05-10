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
import com.dhb.delegate.BtechClientDetailsAdapterOnItemClickDelegate;
import com.dhb.models.data.BtechClientsModel;
import com.dhb.utils.api.Logger;

import java.util.List;

public class BtechClientDetailsAdapter extends RecyclerView.Adapter<BtechClientDetailsAdapter.MyViewHolder> {

    private List<BtechClientsModel> btechClientsModels;
    HomeScreenActivity activity;
    BtechClientDetailsAdapterOnItemClickDelegate mcallback;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_call, tv_name, tv_distance;
        public ImageView title_distance_icon;
        View itemView;

        public MyViewHolder(View view) {
            super(view);
            this.itemView = view;
            initComp(view);
        }

        private void initComp(View view) {
            tv_call = (TextView) view.findViewById(R.id.txt_call);
            tv_name = (TextView) view.findViewById(R.id.txt_name);
            tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        }
    }


    public BtechClientDetailsAdapter(List<BtechClientsModel> btechClientsModels, HomeScreenActivity activity, BtechClientDetailsAdapterOnItemClickDelegate mCallback) {
        this.mcallback = mCallback;
        this.btechClientsModels = btechClientsModels;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_btech_client, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BtechClientsModel btechClientsModel = btechClientsModels.get(position);

        final int pos = position;
        if (btechClientsModel != null) {
            holder.tv_name.setText("" + btechClientsModel.getName());
            if (holder.itemView.getParent() != null)
                ((ViewGroup) holder.itemView.getParent()).removeView(holder.itemView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mcallback.onItemClick(btechClientsModel);
                }
            });
            holder.tv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mcallback.onItemCallClick(btechClientsModel, position);
                }
            });
            holder.tv_distance.setText("" + btechClientsModel.getDistance() + " Km");
        } else {
            Logger.error("BtechClientDetailsResponseModel is null ");
        }
    }

    @Override
    public int getItemCount() {
        return btechClientsModels.size();
    }
}
