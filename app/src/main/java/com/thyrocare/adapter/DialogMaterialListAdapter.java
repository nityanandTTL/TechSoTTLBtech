package com.thyrocare.adapter;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thyrocare.R;
import com.thyrocare.fragment.MaterialOrderPlaceFragment;
import com.thyrocare.models.data.MaterialOrderDataModel;

import java.util.ArrayList;


/**
 * Created by Orion on 4/27/2017.
 */

public class DialogMaterialListAdapter extends RecyclerView.Adapter<DialogMaterialListAdapter.MyViewHolder> {

    ArrayList<MaterialOrderDataModel> materialOrders;
    Context mContext;
    MaterialOrderPlaceFragment mfragment;

    private LinearLayoutManager layoutmanager;


    public DialogMaterialListAdapter(Context applicationContext, ArrayList<MaterialOrderDataModel> _filterorder, MaterialOrderPlaceFragment fragment) {
        this.materialOrders = _filterorder;
        this.mContext = applicationContext;
        this.mfragment = fragment;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.materialorderdialoglist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DialogMaterialListAdapter.MyViewHolder holder, final int pos) {

        holder.txt_finalitem.setText(""+materialOrders.get(pos).getItem_name()+" ("+materialOrders.get(pos).getItem_UnitSize()+")");
        holder.edit_quantity.setText(""+materialOrders.get(pos).getOrderQty());
        holder.txt_rate.setText(""+materialOrders.get(pos).getItem_UnitCost());

        Float temprate = Float.parseFloat(materialOrders.get(pos).getItem_UnitCost().toString());
        Float total = temprate * (Float.parseFloat(String.valueOf(materialOrders.get(pos).getOrderQty())));

        holder.txt_total.setText(""+total);

    }


    @Override
    public int getItemCount() {
        return materialOrders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_finalitem, txt_total, edit_quantity, txt_rate;
        public MyViewHolder(View view) {
            super(view);

            this.txt_finalitem = (TextView) view.findViewById(R.id.txt_finalitem);
            this.txt_total = (TextView) view.findViewById(R.id.txt_total);
            this.edit_quantity = (TextView) view.findViewById(R.id.edit_quantity);
            this.txt_rate = (TextView) view.findViewById(R.id.txt_rate);
        }
    }
}