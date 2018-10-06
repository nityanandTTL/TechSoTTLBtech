package com.thyrocare.adapter.LME;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thyrocare.R;
import com.thyrocare.fragment.LME.LME_OrdersDisplayFragment;
import com.thyrocare.models.data.SampleDropDetailsbyTSPLMEDetailsModel;
import com.thyrocare.utils.app.CommonUtils;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by Orion on 4/27/2017.
 */

public class LMEVisitsListAdapter extends RecyclerView.Adapter<LMEVisitsListAdapter.MyViewHolder> {

    ArrayList<SampleDropDetailsbyTSPLMEDetailsModel> mListArray;
    LME_OrdersDisplayFragment mLME_OrdersDisplayFragment;
    private final ArrayList<SampleDropDetailsbyTSPLMEDetailsModel> arraylist;

    public LMEVisitsListAdapter(ArrayList<SampleDropDetailsbyTSPLMEDetailsModel> materialDetailsModels, LME_OrdersDisplayFragment fragment) {
        this.mListArray = materialDetailsModels;
        this.mLME_OrdersDisplayFragment = fragment;

        this.arraylist = new ArrayList<SampleDropDetailsbyTSPLMEDetailsModel>();
        this.arraylist.addAll(mListArray);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lme_itemvisitslist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int pos) {

        holder.txt_code.setText("" + mListArray.get(pos).getSourceCode());
//        holder.txt_cnt.setText("" + mListArray.get(pos).getSampleCount());
        holder.txt_cnt.setText("" + CommonUtils.getSampleCount(mListArray.get(pos)));
        holder.txt_name.setText("" + mListArray.get(pos).getName());
        holder.txt_address.setText("" + mListArray.get(pos).getAddress() + "-" + mListArray.get(pos).getPincode());

        holder.ll_alldata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLME_OrdersDisplayFragment.StartEndButtonClicked(mListArray.get(pos));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListArray.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mListArray.clear();
        if (charText.length() == 0) {
            mListArray.addAll(arraylist);
        } else {
            for (SampleDropDetailsbyTSPLMEDetailsModel wp : arraylist) {
                if (wp.getSourceCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mListArray.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_code, txt_cnt, txt_name, txt_address;
        public LinearLayout ll_alldata;

        public MyViewHolder(View view) {
            super(view);

            this.txt_name = (TextView) view.findViewById(R.id.txt_name);
            this.txt_address = (TextView) view.findViewById(R.id.txt_address);
            this.txt_code = (TextView) view.findViewById(R.id.txt_code);
            this.txt_cnt = (TextView) view.findViewById(R.id.txt_cnt);
            this.ll_alldata = (LinearLayout) view.findViewById(R.id.ll_alldata);
            txt_address.setSelected(true);
        }
    }
}