package com.dhb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.delegate.CampListDisplayRecyclerViewAdapterDelegate;
import com.dhb.delegate.VisitOrderDisplayRecyclerViewAdapterDelegate;
import com.dhb.models.data.CampListDisplayResponseModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by ISRO on 4/27/2017.
 */

public class CampListDetailDisplayAdapter extends BaseAdapter {
    private HomeScreenActivity activity;
    private ArrayList<CampListDisplayResponseModel> campListDisplayResponseModels;
    private CampListDisplayRecyclerViewAdapterDelegate campListDisplayRecyclerViewAdapterDelegate;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private LayoutInflater layoutInflater;

    public CampListDetailDisplayAdapter(HomeScreenActivity activity, ArrayList<CampListDisplayResponseModel> campListDisplayResponseModels, CampListDisplayRecyclerViewAdapterDelegate campListDisplayRecyclerViewAdapterDelegate) {
        this.activity = activity;
        this.campListDisplayResponseModels = campListDisplayResponseModels;
        this.campListDisplayRecyclerViewAdapterDelegate = campListDisplayRecyclerViewAdapterDelegate;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return campListDisplayResponseModels.size();
    }

    @Override
    public Object getItem(int position) {
        return campListDisplayResponseModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FoldingCellViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_camp_detail_folding_cell, parent, false);
            holder = new FoldingCellViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (FoldingCellViewHolder) convertView.getTag();
        }
        initData(holder, position);
        initListeners(holder, position);
        return convertView;
    }

    private void initListeners(final FoldingCellViewHolder holder, final int pos) {
        holder.imgRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campListDisplayRecyclerViewAdapterDelegate.onItemClick(campListDisplayResponseModels.get(pos));
            }
        });
        holder.btn_scan_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campListDisplayRecyclerViewAdapterDelegate.onNavigationStart(campListDisplayResponseModels.get(pos));
            }
        });
        holder.cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerToggle(pos);
                holder.cell.toggle(false);
                initData(holder, pos);
            }
        });
    }

    private void initData(final FoldingCellViewHolder holder, final int pos) {
        if (campListDisplayResponseModels.size() > pos
                && campListDisplayResponseModels.get(pos).getBtechs().size() > 0
                ) {
            holder.tv_leader.setText("Ledger : "+campListDisplayResponseModels.get(pos).getLeader());
          //  holder.tvName.setText(campListDisplayResponseModels.get(pos).get().get(0).getBenMaster().get(0).getName());
           /* holder.tv_date_time.setText(campListDisplayResponseModels.get(pos).getCampDateTime());
            holder.txtAge.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getAge() + " Y | " + orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getGender());
            holder.txtName.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getName());
            holder.txtSrNo.setText(pos + 1 + "");
            holder.txtAddress.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getAddress());
            holder.tvAadharNo.setVisibility(View.GONE);
            holder.txtAadharNo.setVisibility(View.GONE);*/
            if (pos != 0) {
                holder.btn_scan_qrcode.setVisibility(View.GONE);
            }
            if (unfoldedIndexes.contains(pos)) {
                holder.cell.unfold(true);
            } else {
                holder.cell.fold(true);
            }
        }
    }

    private class FoldingCellViewHolder {
        TextView tv_camp, tvName, tv_leader, tv_date_time, tv_location, tv_kits, txt_address, tv_product, tv_amount, tv_expected_crowd;
        TextView txtSrNo, txtName, txtAge, txtAadharNo;
        LinearLayout ll_view_btech;
        ImageView imgRelease;
        Button btn_scan_qrcode;
        FoldingCell cell;
        ListView lv_btech_list;

        FoldingCellViewHolder(View itemView) {
            initUI(itemView);
        }

        private void initUI(View itemView) {
            cell = (FoldingCell) itemView.findViewById(R.id.item_camp_folding_cell);
            tv_camp = (TextView) itemView.findViewById(R.id.tv_camp);
            tv_leader = (TextView) itemView.findViewById(R.id.tv_leader);
            tv_date_time = (TextView) itemView.findViewById(R.id.tv_date_time);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            tv_kits = (TextView) itemView.findViewById(R.id.tv_kits);
            txt_address = (TextView) itemView.findViewById(R.id.txt_address);
            tv_product = (TextView) itemView.findViewById(R.id.tv_product);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_expected_crowd = (TextView) itemView.findViewById(R.id.tv_expected_crowd);
            ll_view_btech = (LinearLayout) itemView.findViewById(R.id.ll_view_btech);
            lv_btech_list=(ListView)itemView.findViewById(R.id.lv_btech_list);
            btn_scan_qrcode=(Button)itemView.findViewById(R.id.btn_scan_qrcode);

        }
    }

    // simple methods for register cell state changes
    private void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    private void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    private void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

}
