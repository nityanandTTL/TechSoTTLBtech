package com.dhb.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.delegate.RemoveSelectedTestFromListDelegate;
import com.dhb.models.data.BeneficiaryTestDetailsModel;

import java.util.ArrayList;


/**
 * Created by ISRO on 4/27/2017.
 */

public class DisplaySelectedTestsListForCancellationAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<BeneficiaryTestDetailsModel> selectedTestsListArr;
    private RemoveSelectedTestFromListDelegate removeSelectedTestFromListDelegate;
    private LayoutInflater layoutInflater;
    public DisplaySelectedTestsListForCancellationAdapter(Activity activity, ArrayList<BeneficiaryTestDetailsModel> selectedTestsListArr, RemoveSelectedTestFromListDelegate removeSelectedTestFromListDelegate) {
        this.activity = activity;
        this.removeSelectedTestFromListDelegate = removeSelectedTestFromListDelegate;
        this.selectedTestsListArr = selectedTestsListArr;
        layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return selectedTestsListArr.size();
    }

    @Override
    public Object getItem(int position) {
        return selectedTestsListArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_remove_test, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initData(holder, position);
        initListeners(holder, position);
        return convertView;
    }

    private void initListeners(ViewHolder holder, final int pos) {
        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTestsListArr.remove(pos);
                removeSelectedTestFromListDelegate.onRemoveButtonClicked(selectedTestsListArr);
            }
        });
    }

    private void initData(ViewHolder holder,int pos) {
        holder.txtTestName.setText(selectedTestsListArr.get(pos).getTests());
    }

    private class ViewHolder {
        TextView txtTestName;
        ImageView imgRemove;

        ViewHolder(View itemView) {
            initUI(itemView);
        }

        private void initUI(View itemView) {
            txtTestName = (TextView) itemView.findViewById(R.id.txt_test_name);
            imgRemove = (ImageView) itemView.findViewById(R.id.img_remove);
        }
    }
}
