package com.dhb.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.delegate.CancelTestsDialogDelegate;

import java.util.ArrayList;


/**
 * Created by ISRO on 4/27/2017.
 */

public class CancelOrderAdapter extends BaseAdapter {
    private static final String TAG = CancelOrderAdapter.class.getSimpleName();
    private Activity activity;
    private ArrayList<String> testCodesArr;
    private CancelTestsDialogDelegate cancelTestsDialogDelegate;
    private LayoutInflater layoutInflater;
    public CancelOrderAdapter(Activity activity, ArrayList<String> testCodesArr, CancelTestsDialogDelegate cancelTestsDialogDelegate) {
        this.activity = activity;
        this.cancelTestsDialogDelegate = cancelTestsDialogDelegate;
        this.testCodesArr = testCodesArr;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return testCodesArr.size();
    }

    @Override
    public Object getItem(int position) {
        return testCodesArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_test, parent, false);
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
holder.cancellogo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        cancelTestsDialogDelegate.onCancelButtonClicked(testCodesArr.get(pos));



    }
});
    }

    private void initData(ViewHolder holder,int pos) {
        holder.textname.setText(testCodesArr.get(pos));
    }

    private class ViewHolder {
        TextView textname;
        ImageView cancellogo;


        ViewHolder(View itemView) {
            initUI(itemView);
        }

        private void initUI(View itemView) {
            textname = (TextView) itemView.findViewById(R.id.testname);
            cancellogo = (ImageView) itemView.findViewById(R.id.cancel);
        }
    }
}
