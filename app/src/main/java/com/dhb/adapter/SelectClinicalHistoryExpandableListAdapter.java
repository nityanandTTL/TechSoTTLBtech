package com.dhb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.delegate.SelectClinicalHistoryCheckboxDelegate;
import com.dhb.models.data.TestClinicalHistoryModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.models.data.TestWiseBeneficiaryClinicalHistoryModel;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/4/2017.
 */

public class SelectClinicalHistoryExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<TestRateMasterModel> testsList = new ArrayList<>();
    private ArrayList<TestWiseBeneficiaryClinicalHistoryModel> chArr;
    private int BenId;
    private SelectClinicalHistoryCheckboxDelegate selectClinicalHistoryCheckboxDelegate;
    public SelectClinicalHistoryExpandableListAdapter(Context context, ArrayList<TestRateMasterModel> testsList,ArrayList<TestWiseBeneficiaryClinicalHistoryModel> chArr,int benId,SelectClinicalHistoryCheckboxDelegate selectClinicalHistoryCheckboxDelegate) {
        this.context = context;
        this.testsList = testsList;
        this.BenId = benId;
        this.chArr = chArr;
        this.selectClinicalHistoryCheckboxDelegate = selectClinicalHistoryCheckboxDelegate;
    }

    @Override
    public int getGroupCount() {
        return testsList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return testsList.get(groupPosition).getTstClinicalHistory().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return testsList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return testsList.get(groupPosition).getTstClinicalHistory().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewParentHolder holder = null;
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.item_select_test_header, null);
            holder = new ViewParentHolder();
            holder.txtHeader = (TextView) convertView.findViewById(R.id.txt_header);
            convertView.setTag(holder);
        } else {
            holder = (ViewParentHolder) convertView.getTag();
        }
        holder.txtHeader.setText(testsList.get(groupPosition).getTestCode());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewChildHolder holder = null;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_select_clinical_history_list_view, null);
            holder = new ViewChildHolder();

            holder.txt_test = (TextView) convertView.findViewById(R.id.txt_test);
            holder.chk_collected = (CheckBox) convertView.findViewById(R.id.chk_collected);
            convertView.setTag(holder);
        } else {
            holder = (ViewChildHolder) convertView.getTag();
        }
        TestClinicalHistoryModel testClinicalHistoryModel = testsList.get(groupPosition).getTstClinicalHistory().get(childPosition);

        final TestWiseBeneficiaryClinicalHistoryModel  testWiseBeneficiaryClinicalHistoryModel = new TestWiseBeneficiaryClinicalHistoryModel();
        testWiseBeneficiaryClinicalHistoryModel.setBenId(BenId);
        testWiseBeneficiaryClinicalHistoryModel.setTest(testsList.get(groupPosition).getTestCode());
        testWiseBeneficiaryClinicalHistoryModel.setClinicalHistoryId(testClinicalHistoryModel.getClinicalHtrId());
        holder.chk_collected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chArr.add(testWiseBeneficiaryClinicalHistoryModel);
                } else {
                    chArr.remove(testWiseBeneficiaryClinicalHistoryModel);
                }
                selectClinicalHistoryCheckboxDelegate.onCheckChange(chArr);
            }
        });
        holder.txt_test.setText(testClinicalHistoryModel.getClinicalHistory());
        if(chArr.contains(testWiseBeneficiaryClinicalHistoryModel)){
            holder.chk_collected.setChecked(true);
        }
        return convertView;
    }

    private class ViewParentHolder {
        TextView txtHeader;
    }

    private class ViewChildHolder {
        CheckBox chk_collected;
        TextView txt_test;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
