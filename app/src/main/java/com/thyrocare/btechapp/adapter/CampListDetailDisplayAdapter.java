package com.thyrocare.btechapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.delegate.CampListDisplayRecyclerViewAdapterDelegate;
import com.thyrocare.btechapp.models.data.CampDetailModel;

import com.thyrocare.btechapp.utils.app.AppConstants;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by Orion on 4/27/2017.<br/>
 * /CallPatchSrcDest/CallPatchRequest<br/>
 */

public class CampListDetailDisplayAdapter extends BaseAdapter {
    private static final String TAG = CampListDetailDisplayAdapter.class.getSimpleName();
    String[] testsList;
    private HomeScreenActivity activity;
    private ArrayList<CampDetailModel> campDetailModelList;
    private CampListDisplayRecyclerViewAdapterDelegate campListDisplayRecyclerViewAdapterDelegate;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private LayoutInflater layoutInflater;

    public CampListDetailDisplayAdapter(HomeScreenActivity activity,
                                        ArrayList<CampDetailModel> campDetailModelList,
                                        CampListDisplayRecyclerViewAdapterDelegate campListDisplayRecyclerViewAdapterDelegate) {
        MessageLogger.LogError(TAG, "CampListDetailDisplayAdapter: ");
        this.activity = activity;
        this.campDetailModelList = campDetailModelList;
        this.campListDisplayRecyclerViewAdapterDelegate = campListDisplayRecyclerViewAdapterDelegate;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return campDetailModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return campDetailModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FoldingCellViewHolder holder;
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
        holder.img_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campListDisplayRecyclerViewAdapterDelegate.onItemClick(campDetailModelList.get(pos), 8, pos);
            }
        });
        holder.btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campListDisplayRecyclerViewAdapterDelegate.onItemClick(campDetailModelList.get(pos), 7, pos);
            }
        });
        holder.btn_arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campListDisplayRecyclerViewAdapterDelegate.onItemClick(campDetailModelList.get(pos), 3, pos);
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
        holder.tv_call_leader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campListDisplayRecyclerViewAdapterDelegate.onItemClick(campDetailModelList.get(pos), 0, pos);
            }
        });
    }

    private void initData(FoldingCellViewHolder holder, final int pos) {
        holder.bool_check = new boolean[campDetailModelList.size()];
        holder.ids_check = new int[campDetailModelList.size()];
        if (campDetailModelList.size() > pos) {
            String upperString1 = "" + campDetailModelList.get(pos).getLocation();
            holder.tv_location.setText(upperString1);
            String upperString = "" + campDetailModelList.get(pos).getLocation();
            holder.txt_location.setText(upperString);
            String campName = "" + campDetailModelList.get(pos).getCampName();
            holder.tvName.setSelected(true);
            holder.tvName.setText(campName);
            holder.txt_name.setText(campName);
            holder.txt_name.setSelected(true);
            holder.tv_leader.setText("Leader Name :" + campDetailModelList.get(pos).getLeaderName());
            if (campDetailModelList.get(pos).getExpectedCrowd() == 0) {
                holder.tv_expected_crowd.setVisibility(View.GONE);
            }
            String test = "" + campDetailModelList.get(pos).getProduct();
            holder.tv_expected_crowd.setText("Expected Crowd: " + campDetailModelList.get(pos).getExpectedCrowd());
            holder.txt_name.setText("" + campDetailModelList.get(pos).getCampName());
            if (campDetailModelList.get(pos).isAccepted()) {
                holder.img_accept.setVisibility(View.GONE);
                holder.btn_start.setVisibility(View.VISIBLE);
            } else {
                holder.img_accept.setVisibility(View.VISIBLE);
                holder.btn_start.setVisibility(View.GONE);
            }
            if (!campDetailModelList.get(pos).isStarted()) {
                holder.btn_start.setVisibility(View.VISIBLE);
                holder.btn_arrived.setVisibility(View.GONE);
            } else {
                holder.btn_start.setVisibility(View.GONE);
                holder.btn_arrived.setVisibility(View.VISIBLE);
            }

        }
        if (campDetailModelList.get(pos).getBtechs() != null) {
            if (campDetailModelList.get(pos).getBtechs().size() > 0) {
                holder.ll_btechs.removeAllViews();
                for (int i = 0; i < campDetailModelList.get(pos).getBtechs().size(); i++) {
                    LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.item_camp_btechs, null);
                    TextView tv_btech_id1 = (TextView) v.findViewById(R.id.tv_btech_id1);
                    TextView tv_sr_no = (TextView) v.findViewById(R.id.tv_sr_no);
                    TextView tv_mob = (TextView) v.findViewById(R.id.tv_mob);
                    TextView tv_status = (TextView) v.findViewById(R.id.tv_status);
                    tv_btech_id1.setText("Btech Name: " + campDetailModelList.get(pos).getBtechs().get(i).getName());
                    tv_btech_id1.setSelected(true);
                    tv_mob.setVisibility(View.GONE);
                    tv_sr_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + campDetailModelList.get(pos).getBtechs().get(pos).getMobile()));
                            activity.startActivity(intent);

                        }
                    });
                    tv_status.setText("Status: " + campDetailModelList.get(pos).getBtechs().get(i).getStatus());
                    holder.ll_btechs.addView(v, i, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                }

            }
        } else {
            holder.ll_btechs.setVisibility(View.GONE);
            holder.sc_ll_btechs.setVisibility(View.GONE);
        }

        if (unfoldedIndexes.contains(pos)) {
            holder.cell.unfold(true);
        } else {
            holder.cell.fold(true);
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

    private class FoldingCellViewHolder {
        TextView tvName, tv_leader, tv_location, tv_expected_crowd, tv_test;
        TextView txt_name, txt_location;
        LinearLayout ll_btechs;
        HorizontalScrollView sc_ll_btechs;
        ImageView tv_call_leader;
        Button btn_start, btn_arrived, img_accept;
        FoldingCell cell;

        int ids_check[];
        boolean bool_check[];

        FoldingCellViewHolder(View itemView) {
            initUI(itemView);
        }

        private void initUI(View itemView) {
            tv_test = (TextView) itemView.findViewById(R.id.tv_test);
            String tests = campDetailModelList.get(0).getProduct();
            testsList = tests.split(",");
            tv_test.setText("Test/Product: " + testsList[0]);
            cell = (FoldingCell) itemView.findViewById(R.id.item_camp_folding_cell);
            tv_leader = (TextView) itemView.findViewById(R.id.tv_leader);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            txt_location = (TextView) itemView.findViewById(R.id.txt_location);
            tv_expected_crowd = (TextView) itemView.findViewById(R.id.tv_expected_crowd);
            btn_start = (Button) itemView.findViewById(R.id.btn_start);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            img_accept = (Button) itemView.findViewById(R.id.img_accept);
            ll_btechs = (LinearLayout) itemView.findViewById(R.id.ll_btechs);
            sc_ll_btechs = (HorizontalScrollView) itemView.findViewById(R.id.sc_ll_btechs);
            btn_arrived = (Button) itemView.findViewById(R.id.btn_arrived);
            tv_call_leader = (ImageView) itemView.findViewById(R.id.tv_call_leader);
        }
    }


}
