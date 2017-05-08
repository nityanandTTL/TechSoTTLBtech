package com.dhb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhb.R;

import com.dhb.delegate.EditTestExpandListAdapterCheckboxDelegate;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.models.data.TestTypeWiseTestRateMasterModelsList;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.InputUtils;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/4/2017.
 */

public class EditTestExpandListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<TestTypeWiseTestRateMasterModelsList> testRateMasterModels;
    private ArrayList<TestRateMasterModel> selectedTests = new ArrayList<>();
    private EditTestExpandListAdapterCheckboxDelegate mcallback;

    public EditTestExpandListAdapter(Context context, ArrayList<TestTypeWiseTestRateMasterModelsList> testRateMasterModels1,ArrayList<TestRateMasterModel> selectedTests,EditTestExpandListAdapterCheckboxDelegate mcallback) {
        this.context = context;
        this.testRateMasterModels = testRateMasterModels1;
        this.mcallback=mcallback;
        this.selectedTests = selectedTests;
        if(this.selectedTests==null){
            this.selectedTests = new ArrayList<>();
        }
    }

    @Override
    public int getGroupCount() {
        return testRateMasterModels.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return testRateMasterModels.get(groupPosition).getTestRateMasterModels().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return testRateMasterModels.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return testRateMasterModels.get(groupPosition).getTestRateMasterModels().get(childPosition);
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
        holder.txtHeader.setText(testRateMasterModels.get(groupPosition).getTestType());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewChildHolder holder = null;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_select_test_list_view, null);
            holder = new ViewChildHolder();

            holder.txt_test = (TextView) convertView.findViewById(R.id.txt_test);
            holder.txt_dis_amt = (TextView) convertView.findViewById(R.id.txt_dis_amt);
            holder.img_test_type = (ImageView) convertView.findViewById(R.id.img_test_type);
            holder.chk_collected = (CheckBox) convertView.findViewById(R.id.chk_collected);
            convertView.setTag(holder);
        } else {
            holder = (ViewChildHolder) convertView.getTag();
        }
        final TestRateMasterModel testRateMasterModel = testRateMasterModels.get(groupPosition).getTestRateMasterModels().get(childPosition);
        holder.txt_dis_amt.setText("" + testRateMasterModel.getRate());
        holder.chk_collected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Logger.error("checked test " + testRateMasterModel.getTestCode());
                    selectedTests.add(testRateMasterModel);
                    mcallback.onCheckChange(selectedTests);
                } else {
                    selectedTests.remove(testRateMasterModel);
                    mcallback.onCheckChange(selectedTests);
                }
            }
        });
        if (testRateMasterModel.getChldtests() != null && testRateMasterModel.getChldtests().size() > 0) {
            holder.txt_test.setText(testRateMasterModel.getTestCode());
        } else {
            if (InputUtils.isNull(testRateMasterModel.getDescription())) {
                holder.txt_test.setText(testRateMasterModel.getTestCode());
            } else {
                holder.txt_test.setText(testRateMasterModel.getDescription());
            }

        }
        if (testRateMasterModel.getChldtests() == null || testRateMasterModel.getChldtests().size() == 0) {
            holder.img_test_type.setImageDrawable(context.getResources().getDrawable(R.drawable.t));
        } else {
            if (testRateMasterModel.getTestType().equalsIgnoreCase("profile")) {
                holder.img_test_type.setImageDrawable(context.getResources().getDrawable(R.drawable.p));
            } else {
                holder.img_test_type.setImageDrawable(context.getResources().getDrawable(R.drawable.o));
            }
        }
        if(selectedTests.contains(testRateMasterModel)){
            holder.chk_collected.setChecked(true);
        }
        return convertView;
    }

    private class ViewParentHolder {
        TextView txtHeader;
    }

    private class ViewChildHolder {
        ImageView img_test_type;
        CheckBox chk_collected;
        TextView txt_test, txt_dis_amt;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
