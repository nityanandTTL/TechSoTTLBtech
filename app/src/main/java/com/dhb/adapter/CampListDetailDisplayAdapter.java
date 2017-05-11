package com.dhb.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.delegate.CampListDisplayRecyclerViewAdapterDelegate;

import com.dhb.models.api.response.CampListDisplayResponseModel;
import com.dhb.models.data.CampBtechModel;
import com.dhb.models.data.CampDetailModel;

import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by ISRO on 4/27/2017.
 */

public class CampListDetailDisplayAdapter extends BaseAdapter {
    private static final String TAG = CampListDetailDisplayAdapter.class.getSimpleName();
    private HomeScreenActivity activity;
    private ArrayList<CampDetailModel> campDetailModelList;
    CampListDisplayResponseModel campListDisplayResponseModel;
    private CampListDisplayRecyclerViewAdapterDelegate campListDisplayRecyclerViewAdapterDelegate;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private LayoutInflater layoutInflater;

    public CampListDetailDisplayAdapter(HomeScreenActivity activity,
                                        ArrayList<CampDetailModel> campDetailModelList,
                                        CampListDisplayRecyclerViewAdapterDelegate campListDisplayRecyclerViewAdapterDelegate) {
        Log.e(TAG, "CampListDetailDisplayAdapter: ");
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
                campListDisplayRecyclerViewAdapterDelegate.onItemClick(campDetailModelList.get(pos));
            }
        });
        holder.btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campListDisplayRecyclerViewAdapterDelegate.onNavigationStart(campDetailModelList.get(pos));
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

    private void initData(FoldingCellViewHolder holder, final int pos) {
        holder.bool_check = new boolean[campDetailModelList.size()];
        holder.ids_check = new int[campDetailModelList.size()];
        if (campDetailModelList.size() > pos) {
            holder.tv_leader.setText("Ledger : " + campDetailModelList.get(pos).getLeader());
            holder.tv_date_time.setText("" + campDetailModelList.get(pos).getCampDateTime());
            holder.tv_location.setText(""/* + campDetailModelList.get(pos).getLocation()*/);
            holder.tv_camp.setText("" + pos + 1);
            holder.tvName.setText("" + campDetailModelList.get(pos).getCampId());
            if (campDetailModelList.get(pos).getProduct().equals("")) {
                holder.tv_product.setVisibility(View.INVISIBLE);
            }
            if (campDetailModelList.get(pos).getAmount() == 0) {
                holder.tv_amount.setVisibility(View.INVISIBLE);
            }
            if (campDetailModelList.get(pos).getExpectedCrowd() == 0) {
                holder.tv_expected_crowd.setVisibility(View.INVISIBLE);
            }
            if (holder.tv_expected_crowd.getVisibility() == View.INVISIBLE && holder.tv_amount.getVisibility() == View.INVISIBLE && holder.tv_product.getVisibility() == View.INVISIBLE) {
                holder.ll_leader_detail.setVisibility(View.GONE);
            }
            holder.tv_product.setText("" + campDetailModelList.get(pos).getProduct());
            holder.tv_amount.setText("" + campDetailModelList.get(pos).getAmount());
            holder.tv_expected_crowd.setText("" + campDetailModelList.get(pos).getExpectedCrowd());
            holder.txt_camp.setText("" + pos + 1);
            holder.txt_name.setText("" + campDetailModelList.get(pos).getCampId());
            holder.txt_ledger.setText("" + campDetailModelList.get(pos).getLeader());
            holder.txt_date_time.setText("" + campDetailModelList.get(pos).getCampDateTime());
            holder.tv_address.setText("" + campDetailModelList.get(pos).getLocation());
            holder.tv_kits.setVisibility(View.GONE);
            holder.txt_kits.setVisibility(View.GONE);
        }
        if (campDetailModelList.get(pos).getBtechs().size() > 0) {
            Log.e(TAG, "initData: getBtechs " + campDetailModelList.get(pos).getBtechs().size());
        /*   ArrayAdapter<CampBtechModel> adapter = new ArrayAdapter<CampBtechModel>(activity,
                   R.layout.item_camp_btechs,R.id.text1, campDetailModelList.get(pos).getBtechs());
            holder.lv_btech_list.setAdapter(adapter);
            holder.lv_btech_list.setVisibility(View.VISIBLE);*/
           /* for (int i = 0; i <campDetailModelList.get(pos).getBtechs().size() ; i++) {
                LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.item_camp_btechs, null);
                TextView textView = (TextView) v.findViewById(R.id.tv_btech_id1);
                textView.setText("" + campDetailModelList.get(pos).getBtechs().get(i).getBtechId());
                holder.insertPoint.addView(v, i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }*/

            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.item_camp_btechs, null);
            TextView textView = (TextView) v.findViewById(R.id.tv_btech_id1);
            textView.setText("" + campDetailModelList.get(pos).getBtechs().get(pos).getBtechId());
            holder.insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //createTBL(pos,holder);
        } else {
            holder.lv_btech_list.setVisibility(View.GONE);
        }

        if (unfoldedIndexes.contains(pos)) {
            holder.cell.unfold(true);
        } else {
            holder.cell.fold(true);
        }

    }

  /*  private void createTBL(int pos,View view) {
        TableRow table_row = new TableRow(activity);
        TextView tv_name = new TextView(activity);
        Button btn_check = new Button(activity);
        ImageView img_line = new ImageView(activity);

        table_row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        table_row.setBackgroundColor(color_white);
        table_row.setGravity(Gravity.CENTER_HORIZONTAL);

        tv_name.setText(campDetailModelList.get(pos).getBtechs().get(pos).getBtechId());
        tv_name.setTextColor(color_blue);
        tv_name.setTextSize(16);
        tv_name.setTypeface(Typeface.DEFAULT_BOLD);
        tv_name.setWidth(150);

        btn_check.setLayoutParams(new TableRow.LayoutParams(30, 30));
        btn_check.setBackgroundResource(R.drawable.call_w_bg);

        img_line.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 2));
        img_line.setBackgroundResource(R.drawable.separater_line);

        table_row.addView(tv_name);
        table_row.addView(btn_check);

        view.tbl.addView(table_row);
        table.addView(img_line);

        int id = i + CHECK_BUTTON_ID;
        btn_check.setId(id);
        ids_check[i] = id;
    }*/

    private class FoldingCellViewHolder {
        TextView tv_camp, tvName, tv_leader, tv_date_time, tv_location, tv_kits, txt_address, tv_product, tv_amount,
                tv_expected_crowd, tv_address;
        TextView txt_camp, txt_name, txt_ledger, txt_date_time, txt_distance, txt_kits;
        LinearLayout ll_view_btech, ll_leader_detail, ll_btechs;
        ImageView img_accept, imgedit;
        Button btn_start;
        FoldingCell cell;
        ListView lv_btech_list;
        ViewGroup insertPoint;
        TableLayout tableLayout;
        final int CHECK_BUTTON_ID = 982301;
        int ids_check[];
        boolean bool_check[];

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
            lv_btech_list = (ListView) itemView.findViewById(R.id.lv_btech_list);
            btn_start = (Button) itemView.findViewById(R.id.btn_start);
            ll_leader_detail = (LinearLayout) itemView.findViewById(R.id.ll_leader_detail);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            txt_camp = (TextView) itemView.findViewById(R.id.txt_camp);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_ledger = (TextView) itemView.findViewById(R.id.txt_ledger);
            txt_date_time = (TextView) itemView.findViewById(R.id.txt_date_time);
            txt_distance = (TextView) itemView.findViewById(R.id.txt_distance);
            txt_kits = (TextView) itemView.findViewById(R.id.txt_kits);
            img_accept = (ImageView) itemView.findViewById(R.id.img_accept);
            imgedit = (ImageView) itemView.findViewById(R.id.imgedit);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            ll_btechs = (LinearLayout) itemView.findViewById(R.id.ll_btechs);
            insertPoint = (ViewGroup) itemView.findViewById(R.id.ll_btechs);
            tableLayout = (TableLayout) itemView.findViewById(R.id.tbl_btechs);
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
