package com.thyrocare.btechapp.adapter;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.delegate.EditTestExpandListAdapterCheckboxDelegate;
import com.thyrocare.btechapp.models.data.ChildTestsModel;
import com.thyrocare.btechapp.models.data.TestRateMasterModel;
import com.thyrocare.btechapp.models.data.TestTypeWiseTestRateMasterModelsList;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orion on 5/4/2017.
 */

public class ExpandableTestMasterListDisplayAdapter extends BaseExpandableListAdapter {
    private Activity activity;
    private ArrayList<TestTypeWiseTestRateMasterModelsList> testRateMasterModels;
    private ArrayList<TestTypeWiseTestRateMasterModelsList> filteredList;
    private ArrayList<TestRateMasterModel> selectedTests = new ArrayList<>();
    private EditTestExpandListAdapterCheckboxDelegate mcallback;
    private ArrayList<TestRateMasterModel> tempselectedTests;
    private List<String> tempselectedTests1;
    private AlertDialog.Builder alertDialogBuilder;

    public ExpandableTestMasterListDisplayAdapter(Activity activity, ArrayList<TestTypeWiseTestRateMasterModelsList> testRateMasterModels1, ArrayList<TestRateMasterModel> selectedTests, EditTestExpandListAdapterCheckboxDelegate mcallback) {
        this.activity = activity;
        this.testRateMasterModels = testRateMasterModels1;
        this.filteredList = testRateMasterModels1;
        this.mcallback = mcallback;
        this.selectedTests = selectedTests;
        if (this.selectedTests == null) {
            this.selectedTests = new ArrayList<>();
        }
    }

    @Override
    public int getGroupCount() {
        return filteredList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return filteredList.get(groupPosition).getTestRateMasterModels().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return filteredList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return filteredList.get(groupPosition).getTestRateMasterModels().get(childPosition);
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
            LayoutInflater inf = activity.getLayoutInflater();
            convertView = inf.inflate(R.layout.item_select_test_header, parent, false);
            holder = new ViewParentHolder();
            holder.txtHeader = (TextView) convertView.findViewById(R.id.txt_header);
            convertView.setTag(holder);
        } else {
            holder = (ViewParentHolder) convertView.getTag();
        }
        holder.txtHeader.setText(filteredList.get(groupPosition).getTestType());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewChildHolder holder = null;
        if (convertView == null) {
            LayoutInflater infalInflater = activity.getLayoutInflater();
            convertView = infalInflater.inflate(R.layout.item_select_test_list_view, parent, false);
            holder = new ViewChildHolder();
            holder.txt_test = (TextView) convertView.findViewById(R.id.txt_test);
            holder.txt_dis_amt = (TextView) convertView.findViewById(R.id.txt_dis_amt);
            holder.img_test_type = (ImageView) convertView.findViewById(R.id.img_test_type);
            holder.imgCheck = (ImageView) convertView.findViewById(R.id.img_check);
            holder.imgChecked = (ImageView) convertView.findViewById(R.id.img_checked);
            holder.isSelectedDueToParent = false;
            holder.parentTestCode = "";
            holder.imgTestFasting = (ImageView) convertView.findViewById(R.id.test_fasting);
            convertView.setTag(holder);
        } else {
            holder = (ViewChildHolder) convertView.getTag();
        }
        final boolean isSelectedDueToParent = holder.isSelectedDueToParent;
        final String parentTestCode = holder.parentTestCode;
        final TestRateMasterModel testRateMasterModel = filteredList.get(groupPosition).getTestRateMasterModels().get(childPosition);
        holder.txt_dis_amt.setText("₹ " + testRateMasterModel.getRate() + "/-");

        if (!InputUtils.isNull(testRateMasterModel.getTestType()) && (testRateMasterModel.getTestType().equals("TEST") || testRateMasterModel.getTestType().equals("OFFER"))
                && !InputUtils.isNull(testRateMasterModel.getDescription())) {
            holder.txt_test.setText(testRateMasterModel.getDescription() + "(" + testRateMasterModel.getTestCode() + ")");
        } else {
            holder.txt_test.setText(testRateMasterModel.getTestCode());
        }

        if (!InputUtils.isNull(testRateMasterModel.getTestType()) && testRateMasterModel.getTestType().equalsIgnoreCase("profile")) {
            holder.img_test_type.setImageDrawable(activity.getResources().getDrawable(R.drawable.p));
        } else if (!InputUtils.isNull(testRateMasterModel.getTestType()) && testRateMasterModel.getTestType().equalsIgnoreCase("test")) {
            holder.img_test_type.setImageDrawable(activity.getResources().getDrawable(R.drawable.t));
        } else {
            holder.img_test_type.setImageDrawable(activity.getResources().getDrawable(R.drawable.o));
        }
        if (testRateMasterModel.getFasting().equalsIgnoreCase("fasting")) {
            holder.imgTestFasting.setVisibility(View.VISIBLE);
            holder.imgTestFasting.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_fasting));
        } else if (testRateMasterModel.getFasting().equalsIgnoreCase("non-fasting")) {
            holder.imgTestFasting.setVisibility(View.VISIBLE);
            holder.imgTestFasting.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_non_fasting));
        } else {
            holder.imgTestFasting.setVisibility(View.INVISIBLE);
        }
        boolean isChecked = false;
        holder.isSelectedDueToParent = false;
        holder.parentTestCode = "";
        holder.imgChecked.setVisibility(View.GONE);
        holder.imgCheck.setVisibility(View.VISIBLE);
        if (selectedTests != null && selectedTests.size() > 0) {
            for (int i = 0; !isChecked && i < selectedTests.size(); i++) {
                TestRateMasterModel selectedTestModel = selectedTests.get(i);
                if (selectedTestModel.getTestCode().equals(testRateMasterModel.getTestCode())) {
                    holder.imgChecked.setVisibility(View.VISIBLE);
                    holder.imgCheck.setVisibility(View.GONE);
                    holder.isSelectedDueToParent = false;
                    holder.parentTestCode = "";
                    isChecked = true;
                } else if (selectedTestModel.getChldtests() != null && testRateMasterModel.getChldtests() != null && selectedTestModel.checkIfChildsContained(testRateMasterModel)) {
                    holder.imgChecked.setVisibility(View.VISIBLE);
                    holder.imgCheck.setVisibility(View.GONE);
                    holder.isSelectedDueToParent = true;
                    holder.parentTestCode = selectedTestModel.getTestCode();
                    isChecked = true;
                } else {
                    if (selectedTestModel.getChldtests() != null && selectedTestModel.getChldtests().size() > 0) {
                        for (ChildTestsModel ctm :
                                selectedTestModel.getChldtests()) {
                            if (ctm.getChildTestCode().equals(testRateMasterModel.getTestCode())) {
                                holder.imgChecked.setVisibility(View.VISIBLE);
                                holder.imgCheck.setVisibility(View.GONE);
                                holder.isSelectedDueToParent = true;
                                holder.parentTestCode = selectedTestModel.getTestCode();
                                isChecked = true;
                                break;
                            } else {
                                holder.imgChecked.setVisibility(View.GONE);
                                holder.imgCheck.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        holder.imgChecked.setVisibility(View.GONE);
                        holder.imgCheck.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            holder.imgChecked.setVisibility(View.GONE);
            holder.imgCheck.setVisibility(View.VISIBLE);
        }
        holder.imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String slectedpackage = "";

                if (testRateMasterModel.getTestType().equals("TEST") || testRateMasterModel.getTestType().equals("OFFER")
                        && !InputUtils.isNull(testRateMasterModel.getDescription())) {
                    slectedpackage = testRateMasterModel.getDescription();
                } else {
                    slectedpackage = testRateMasterModel.getTestCode();
                }

                if (testRateMasterModel.getTestType().equals("OFFER") && checkIfOfferExists(selectedTests)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Confirm Action")
                            .setMessage("Selecting an OFFER will replace previously selected OFFER. Do you still wish to proceed?")
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    mcallback.onCheckChange(selectedTests);
                                }
                            })
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    selectedTests = replaceOffer(selectedTests, testRateMasterModel);
                                    mcallback.onCheckChange(selectedTests);
                                }
                            })
                            .setCancelable(false)
                            .show();

                } else {

                    tempselectedTests = new ArrayList<>();
                    tempselectedTests1 = new ArrayList<>();
                    if (testRateMasterModel.getChldtests() != null) {
                        for (int i = 0; i < testRateMasterModel.getChldtests().size(); i++) {


                            //tejas t -----------------------------
                            for (int j = 0; j < selectedTests.size(); j++) {

                                if (testRateMasterModel.getChldtests().get(i).getChildTestCode().equalsIgnoreCase(selectedTests.get(j).getTestCode())) {
                                    MessageLogger.PrintMsg("Cart selectedtestlist Description :" + selectedTests.get(j).getDescription() + "Cart selectedtestlist Code :" + selectedTests.get(j).getTestCode());

                                    if (selectedTests.get(j).getTestType().equals("TEST") || selectedTests.get(j).getTestType().equals("OFFER")
                                            && !InputUtils.isNull(selectedTests.get(j).getDescription())) {
                                        tempselectedTests1.add(selectedTests.get(j).getDescription());
                                    } else {
                                        tempselectedTests1.add(selectedTests.get(j).getTestCode());
                                    }

                                    tempselectedTests.add(selectedTests.get(j));
                                }
                            }
                        }
                    }

                    if (selectedTests != null) {
                        for (int j = 0; j < selectedTests.size(); j++) {
                            TestRateMasterModel selectedTestModel123 = selectedTests.get(j);
                            if (selectedTestModel123.getChldtests() != null && testRateMasterModel.getChldtests() != null && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {

                                if (selectedTests.get(j).getTestType().equals("TEST") || selectedTests.get(j).getTestType().equals("OFFER")
                                        && !InputUtils.isNull(selectedTests.get(j).getDescription())) {
                                    tempselectedTests1.add(selectedTests.get(j).getDescription());
                                } else {
                                    tempselectedTests1.add(selectedTests.get(j).getTestCode());
                                }
                                tempselectedTests.add(selectedTestModel123);
                            }
                        }

                    }

                    if (tempselectedTests != null && tempselectedTests.size() > 0) {
                        String cartproduct = TextUtils.join(",", tempselectedTests1);
                        alertDialogBuilder = new AlertDialog.Builder(activity);
                        alertDialogBuilder
                                .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                    if (tempselectedTests != null) {
                        for (int i = 0; i < tempselectedTests.size(); i++) {
                            for (int j = 0; j < selectedTests.size(); j++) {
                                if (tempselectedTests.get(i).getTestCode().equalsIgnoreCase(selectedTests.get(j).getTestCode())) {
                                    selectedTests.remove(j);
                                }
                            }
                        }
                    }


                    selectedTests.add(testRateMasterModel);
                    mcallback.onCheckChange(selectedTests);
                }
            }
        });
        holder.imgChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelectedDueToParent) {
                    selectedTests.remove(testRateMasterModel);
                    mcallback.onCheckChange(selectedTests);
                } else {
                    Toast.makeText(activity, "This test was selected because of its parent. If you wish to remove this test please remove the parent: " + parentTestCode, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    private boolean checkIfOfferExists(ArrayList<TestRateMasterModel> selTests) {
        for (TestRateMasterModel trmm :
                selTests) {
            if (!InputUtils.isNull(trmm.getTestType()) && trmm.getTestType().equals("OFFER")) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<TestRateMasterModel> replaceOffer(ArrayList<TestRateMasterModel> selTests, TestRateMasterModel newOffer) {
        for (int i = 0; i < selTests.size(); i++) {
            if (!InputUtils.isNull(selTests.get(i).getTestType()) && selTests.get(i).getTestType().equals("OFFER")) {
                selTests.remove(i);
                break;
            }
        }
        selTests.add(newOffer);
        return selTests;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void filterData(String query) {

        query = query.toLowerCase();
        Logger.verbose("FilteredListSizeBeforeFilter: " + String.valueOf(filteredList.size()));

        if (query.isEmpty()) {
            filteredList = new ArrayList<>();
            filteredList.addAll(testRateMasterModels);
            Logger.verbose("FilteredListSizeAfterFilerQueryEmpty: " + String.valueOf(filteredList.size()));
        } else {
            filteredList = new ArrayList<>();
            for (TestTypeWiseTestRateMasterModelsList testTypeModel : testRateMasterModels) {

                ArrayList<TestRateMasterModel> oldList = testTypeModel.getTestRateMasterModels();
                ArrayList<TestRateMasterModel> newList = new ArrayList<TestRateMasterModel>();
                for (TestRateMasterModel testModel : oldList) {
                    if (testModel.getTestCode().toLowerCase().contains(query) ||
                            testModel.getDescription().toLowerCase().contains(query)) {
                        newList.add(testModel);
                    }
                }
                if (newList.size() > 0) {
                    TestTypeWiseTestRateMasterModelsList nContinent = new TestTypeWiseTestRateMasterModelsList(testTypeModel.getTestType(), newList);
                    filteredList.add(nContinent);
                }
            }
        }

        Logger.verbose("FilteredListSizeAfterFilter: " + String.valueOf(filteredList.size()));
        notifyDataSetChanged();

    }

    private class ViewParentHolder {
        TextView txtHeader;
    }

    private class ViewChildHolder {
        ImageView img_test_type;
        ImageView imgCheck, imgChecked;
        TextView txt_test, txt_dis_amt;
        boolean isSelectedDueToParent;
        String parentTestCode;
        ImageView imgTestFasting;
    }
}
