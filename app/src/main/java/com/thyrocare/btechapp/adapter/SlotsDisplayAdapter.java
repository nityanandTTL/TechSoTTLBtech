package com.thyrocare.btechapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.delegate.SlotsSelectionDelegate;
import com.thyrocare.btechapp.models.data.SlotModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/2/2017.
 */
public class SlotsDisplayAdapter extends BaseAdapter{
    private ArrayList<SlotModel> slotsArr;
    private Activity activity;
    private LayoutInflater inflater;
    private SlotsSelectionDelegate slotsSelectionDelegate;
    private ArrayList<SlotModel> selectedSlots;

    public SlotsDisplayAdapter(ArrayList<SlotModel> slotsArr, Activity activity,SlotsSelectionDelegate slotsSelectionDelegate, ArrayList<SlotModel> selectedSlots) {
        this.slotsArr = slotsArr;
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.slotsSelectionDelegate = slotsSelectionDelegate;
        this.selectedSlots = selectedSlots;
    }

    @Override
    public int getCount() {
        return slotsArr.size();
    }

    @Override
    public Object getItem(int position) {
        return slotsArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_slots,parent,false);
            holder = new ViewHolder();
            holder.txtSlot = (TextView) convertView.findViewById(R.id.txt_slots);
            holder.card_view = (CardView)convertView.findViewById(R.id.card_view);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(selectedSlots.contains(slotsArr.get(position))){
            holder.txtSlot.setTextColor(activity.getResources().getColor(R.color.white));
            holder.card_view.setBackgroundColor(activity.getResources().getColor(R.color.bg_new_color));
        }
        else{
            holder.txtSlot.setTextColor(activity.getResources().getColor(R.color.bg_new_color));
            holder.card_view.setBackgroundColor(activity.getResources().getColor(R.color.white));
        }
        holder.txtSlot.setText(slotsArr.get(position).getSlot());
        holder.txtSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedSlots.contains(slotsArr.get(position))&&slotsArr.get(position).isMandatorySlot()) {
                    Toast.makeText(activity,"Cannot Remove Mandatory Slots",Toast.LENGTH_SHORT).show();
                }
                else if(selectedSlots.contains(slotsArr.get(position))){
                    selectedSlots.remove(slotsArr.get(position));
                }
                else{
                    selectedSlots.add(slotsArr.get(position));
                }
                slotsSelectionDelegate.onSlotSelected(selectedSlots);
            }
        });
        return convertView;
    }
    private class ViewHolder{
        private TextView txtSlot;
        private CardView card_view;
    }
}
