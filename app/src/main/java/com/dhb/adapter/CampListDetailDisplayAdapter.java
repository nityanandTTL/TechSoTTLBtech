package com.dhb.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
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

import com.dhb.Manifest;
import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.delegate.CampListDisplayRecyclerViewAdapterDelegate;

import com.dhb.models.api.response.CampListDisplayResponseModel;
import com.dhb.models.data.CampDetailModel;

import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppConstants;
import com.ramotion.foldingcell.FoldingCell;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;


/**
 * Created by ISRO on 4/27/2017.
 */

public class CampListDetailDisplayAdapter extends BaseAdapter {
    private static final String TAG = CampListDetailDisplayAdapter.class.getSimpleName();
    private HomeScreenActivity activity;
    private ArrayList<CampDetailModel> campDetailModelList;
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
      /*  holder.imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campListDisplayRecyclerViewAdapterDelegate.onItemClick(campDetailModelList.get(pos), 8, pos);
            }
        });*/
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
       /* holder.txt_call_leader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campListDisplayRecyclerViewAdapterDelegate.onItemClick(campDetailModelList.get(pos), 0, pos);
            }
        });*/
    }

    private void initData(FoldingCellViewHolder holder, final int pos) {
        holder.bool_check = new boolean[campDetailModelList.size()];
        holder.ids_check = new int[campDetailModelList.size()];
        if (campDetailModelList.size() > pos) {

            holder.tv_location.setText("" + campDetailModelList.get(pos).getLocation());
          /*  holder.tv_camp.setText(String.valueOf(pos + 1));*/
            String campName="" + campDetailModelList.get(pos).getCampName();
            holder.tvName.setSelected(true);
            holder.tvName.setText(campName);
            holder.tv_leader.setText("Leader Name :" + campDetailModelList.get(pos).getLeaderName());
            // holder.tv_date_time.setText("" + campDetailModelList.get(pos).getCampDateTime());
            /*if (campDetailModelList.get(pos).getProduct().equals("")) {
                holder.tv_product.setVisibility(View.INVISIBLE);
            }
            if (campDetailModelList.get(pos).getAmount() == 0) {
                holder.tv_amount.setVisibility(View.INVISIBLE);
            }*/
          /*  if (campDetailModelList.get(pos).getExpectedCrowd() == 0) {
                holder.tv_expected_crowd.setVisibility(View.INVISIBLE);
            }
            if (holder.tv_expected_crowd.getVisibility() == View.INVISIBLE && holder.tv_amount.getVisibility() == View.INVISIBLE && holder.tv_product.getVisibility() == View.INVISIBLE) {
                holder.ll_leader_detail.setVisibility(View.GONE);
            }*/
            if (campDetailModelList.get(pos).getExpectedCrowd() == 0) {
                holder.tv_expected_crowd.setVisibility(View.INVISIBLE);
            }
            String test="" + campDetailModelList.get(pos).getProduct();
          //  holder.tv_product.setSelected(true);
          //  holder.tv_product.setText(test);
          /*  arr[pos] = "" + campDetailModelList.get(pos).getProduct();
            Logger.error("arr: "+ arr[pos]);*/
         //   holder.tv_amount.setText("" + campDetailModelList.get(pos).getAmount());
            holder.tv_expected_crowd.setText("Expected Crowd: " + campDetailModelList.get(pos).getExpectedCrowd());
           /* String p=String.valueOf(pos + 1);
            holder.txt_camp.setSelected(true);
            holder.txt_camp.setText(p);*/
            holder.txt_name.setText("" + campDetailModelList.get(pos).getCampName());
         //   holder.txt_ledger.setText("Leader Name: " + campDetailModelList.get(pos).getLeaderName());
         //   holder.txt_date_time.setText("" + campDetailModelList.get(pos).getCampDateTime());
          //  holder.tv_address.setText("" + campDetailModelList.get(pos).getLocation());
          //  holder.tv_kits.setVisibility(View.GONE);
          //  holder.txt_kits.setVisibility(View.GONE);

            if (campDetailModelList.get(pos).isAccepted()) {
                holder.img_accept.setVisibility(View.GONE);
                holder.btn_start.setVisibility(View.VISIBLE);
            }else {
                holder.img_accept.setVisibility(View.VISIBLE);
                holder.btn_start.setVisibility(View.GONE);
            }

              //  holder.imgedit.setVisibility(View.GONE);
                if ( !campDetailModelList.get(pos).isStarted()) {
                    holder.btn_start.setVisibility(View.VISIBLE);
                    holder.btn_arrived.setVisibility(View.GONE);
                } else {
                    holder.btn_start.setVisibility(View.GONE);
                    holder.btn_arrived.setVisibility(View.VISIBLE);
                }

            } /*else {
               // holder.img_accept.setVisibility(View.VISIBLE);
              //  holder.imgedit.setVisibility(View.VISIBLE);
                if ( !campDetailModelList.get(pos).isStarted()) {
                    holder.btn_start.setVisibility(View.VISIBLE);
                    holder.btn_arrived.setVisibility(View.GONE);
                } else {
                    holder.btn_start.setVisibility(View.GONE);
                    holder.btn_arrived.setVisibility(View.VISIBLE);
                }
            }*/

        if (campDetailModelList.get(pos).getBtechs() != null) {
            if (campDetailModelList.get(pos).getBtechs().size() > 0) {
                Log.e(TAG, "initData: getBtechs " + campDetailModelList.get(pos).getBtechs().size());
                holder.ll_btechs.removeAllViews();
                for (int i = 0; i < campDetailModelList.get(pos).getBtechs().size(); i++) {
                    LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.item_camp_btechs, null);
                    TextView tv_btech_id1 = (TextView) v.findViewById(R.id.tv_btech_id1);
                    TextView tv_sr_no = (TextView) v.findViewById(R.id.tv_sr_no);
                    TextView tv_mob = (TextView) v.findViewById(R.id.tv_mob);
                    TextView tv_status = (TextView) v.findViewById(R.id.tv_status);
                    tv_btech_id1.setText("Btech Name: " + campDetailModelList.get(pos).getBtechs().get(i).getName());
                    tv_mob.setVisibility(View.GONE);
                    // tv_mob.setText("Mob. No:" + campDetailModelList.get(pos).getBtechs().get(i).getMobile());
                    //  tv_sr_no.setText(String.valueOf(i + 1));
                    tv_sr_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                      Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + campDetailModelList.get(pos).getBtechs().get(0).getMobile()));
                        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{android.Manifest.permission.CALL_PHONE},
                                    AppConstants.APP_PERMISSIONS);
                            return;
                        }
                        activity.startActivity(intent);
                    }
                    });

                    tv_status.setText("Status: " + campDetailModelList.get(pos).getBtechs().get(i).getStatus());
                    holder.ll_btechs.addView(v, i, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                }

            }
        } else {
            holder.ll_btechs.setVisibility(View.GONE);
            holder.ll_view_btech.setVisibility(View.GONE);
        }

        if (unfoldedIndexes.contains(pos)) {
            holder.cell.unfold(true);
        } else {
            holder.cell.fold(true);
        }

    }




    private class FoldingCellViewHolder {
        TextView tv_camp, tvName, tv_leader, tv_date_time, tv_location, tv_kits, txt_address, tv_product, tv_amount,
                txt_call_leader,tv_expected_crowd, tv_address;
        TextView txt_camp, txt_name, txt_ledger, txt_date_time, txt_distance, txt_kits ;
        LinearLayout ll_view_btech, ll_leader_detail, ll_btechs;
        ImageView imgedit,tv_call_leader;
        Button btn_start, btn_arrived ,img_accept;
        FoldingCell cell;

        int ids_check[];
        boolean bool_check[];

        FoldingCellViewHolder(View itemView) {
            initUI(itemView);
        }

        private void initUI(View itemView) {
            cell = (FoldingCell) itemView.findViewById(R.id.item_camp_folding_cell);
          //  tv_camp = (TextView) itemView.findViewById(R.id.tv_camp);
            tv_leader = (TextView) itemView.findViewById(R.id.tv_leader);
          //  tv_date_time = (TextView) itemView.findViewById(R.id.tv_date_time);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
          //  tv_kits = (TextView) itemView.findViewById(R.id.tv_kits);
          //  txt_address = (TextView) itemView.findViewById(R.id.txt_address);
           // tv_product = (TextView) itemView.findViewById(R.id.tv_product);
           // tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_expected_crowd = (TextView) itemView.findViewById(R.id.tv_expected_crowd);
            ll_view_btech = (LinearLayout) itemView.findViewById(R.id.ll_view_btech);
            btn_start = (Button) itemView.findViewById(R.id.btn_start);
          //  ll_leader_detail = (LinearLayout) itemView.findViewById(R.id.ll_leader_detail);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
          //  txt_camp = (TextView) itemView.findViewById(R.id.txt_camp);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
           // txt_ledger = (TextView) itemView.findViewById(R.id.txt_ledger);
         //   txt_date_time = (TextView) itemView.findViewById(R.id.txt_date_time);
         //   txt_distance = (TextView) itemView.findViewById(R.id.txt_distance);
         //   txt_kits = (TextView) itemView.findViewById(R.id.txt_kits);
      img_accept = (Button) itemView.findViewById(R.id.img_accept);
          //  imgedit = (ImageView) itemView.findViewById(R.id.imgedit);
           // tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            ll_btechs = (LinearLayout) itemView.findViewById(R.id.ll_btechs);
            btn_arrived = (Button) itemView.findViewById(R.id.btn_arrived);
            tv_call_leader = (ImageView) itemView.findViewById(R.id.tv_call_leader);
          //  txt_call_leader = (TextView) itemView.findViewById(R.id.txt_call_leader);
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
