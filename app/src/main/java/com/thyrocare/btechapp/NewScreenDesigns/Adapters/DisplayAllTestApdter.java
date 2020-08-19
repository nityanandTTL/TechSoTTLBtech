package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.ChildTestsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.models.data.TestRateMasterModel;
import com.thyrocare.btechapp.models.data.TestTypeWiseTestRateMasterModelsList;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;
import java.util.List;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.PSAandFPSAforMaleMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SelectingPOPwillreplaceofferMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SelectingofferwillreplaceofferMsg;

public class DisplayAllTestApdter extends RecyclerView.Adapter<DisplayAllTestApdter.MyViewHolder> {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private AppPreferenceManager appPreferenceManager;
    private Global globalClass;
    private boolean isM = false;
    private ArrayList<TestRateMasterModel> testRateMasterModels;
    private ArrayList<TestRateMasterModel> allProductList;
    private ArrayList<TestRateMasterModel> filteredList;
    private ArrayList<TestRateMasterModel> selectedTests = new ArrayList<>();
    private ArrayList<TestRateMasterModel> tempselectedTests;
    private List<String> tempselectedTests1;
    private OnClickListeners onClickListeners;


    public DisplayAllTestApdter(Activity activity, ArrayList<TestRateMasterModel> testRateMasterModelArry, ArrayList<TestRateMasterModel> allProductList , ArrayList<TestRateMasterModel> selectedTests, boolean isM) {
        this.activity = activity;
        appPreferenceManager = new AppPreferenceManager(activity);
        globalClass = new Global(activity);
        layoutInflater = LayoutInflater.from(activity);
        this.allProductList = allProductList;
        this.testRateMasterModels = testRateMasterModelArry;
        this.filteredList = testRateMasterModelArry;
        this.selectedTests = selectedTests;
        this.isM = isM;

    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_test_type;
        ImageView imgCheck, imgChecked,img_unCheckDisabled;
        TextView txt_test, txt_dis_amt;
        ImageView imgTestFasting;

        public MyViewHolder(View itemView) {
            super(itemView);

            txt_test = (TextView) itemView.findViewById(R.id.txt_test);
            txt_dis_amt = (TextView) itemView.findViewById(R.id.txt_dis_amt);
            img_test_type = (ImageView) itemView.findViewById(R.id.img_test_type);
            imgCheck = (ImageView) itemView.findViewById(R.id.img_check);
            imgChecked = (ImageView) itemView.findViewById(R.id.img_checked);
            img_unCheckDisabled = (ImageView) itemView.findViewById(R.id.img_unCheckDisabled);
            imgTestFasting = (ImageView) itemView.findViewById(R.id.test_fasting);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_test_list_view_new, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final TestRateMasterModel testRateMasterModel = filteredList.get(position);
        holder.txt_dis_amt.setText("₹ " + testRateMasterModel.getRate() + "/-");
        if (!StringUtils.isNull(testRateMasterModel.getTestType()) && (testRateMasterModel.getTestType().equals("TEST") || testRateMasterModel.getTestType().equals("OFFER"))
                && !StringUtils.isNull(testRateMasterModel.getDescription())) {
            // holder.txt_test.setText(testRateMasterModel.getDescription()+"("+testRateMasterModel.getTestCode()+")");
            holder.txt_test.setText(testRateMasterModel.getDescription());
        } else {
            //holder.txt_test.setText(testRateMasterModel.getDescription());
            holder.txt_test.setText(testRateMasterModel.getTestCode());
        }

        //todo tejas t  dont show fasting non fasting ----------------
        holder.imgTestFasting.setVisibility(View.GONE);//todo tejas t ------
       /* if(testRateMasterModel.getFasting().equalsIgnoreCase("fasting")){
            holder.imgTestFasting.setVisibility(View.VISIBLE);
            holder.imgTestFasting.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_fasting));
        }
        else if(testRateMasterModel.getFasting().equalsIgnoreCase("non-fasting")){
            holder.imgTestFasting.setVisibility(View.VISIBLE);
            holder.imgTestFasting.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_non_fasting));
        }
        else{
            holder.imgTestFasting.setVisibility(View.INVISIBLE);
        }*/
        //todo tejas t  dont show fasting non fasting ----------------

        holder.imgChecked.setVisibility(View.GONE);
        holder.img_unCheckDisabled.setVisibility(View.GONE);
        holder.imgCheck.setVisibility(View.VISIBLE);
        if (selectedTests != null && selectedTests.size() > 0) {
            for (int i = 0; i < selectedTests.size(); i++) {
                TestRateMasterModel selectedTestModel = selectedTests.get(i);
                if (selectedTestModel.getTestCode().equals(testRateMasterModel.getTestCode())) {
                    holder.imgChecked.setVisibility(View.VISIBLE);
                    holder.imgCheck.setVisibility(View.GONE);
                    break;
                } else if (selectedTestModel.getChldtests() != null && testRateMasterModel.getChldtests() != null && selectedTestModel.checkIfChildsContained(testRateMasterModel)) {
                    holder.img_unCheckDisabled.setVisibility(View.VISIBLE);
                    holder.imgChecked.setVisibility(View.GONE);
                    holder.imgCheck.setVisibility(View.GONE);
                    break;
                } else {
                    if (selectedTestModel.getChldtests() != null && selectedTestModel.getChldtests().size() > 0) {
                        for (ChildTestsModel ctm :
                                selectedTestModel.getChldtests()) {
                            if (ctm.getChildTestCode().equals(testRateMasterModel.getTestCode())) {
                                holder.img_unCheckDisabled.setVisibility(View.VISIBLE);
                                holder.imgChecked.setVisibility(View.GONE);
                                holder.imgCheck.setVisibility(View.GONE);
                                break;
                            } else {
                                holder.img_unCheckDisabled.setVisibility(View.GONE);
                                holder.imgChecked.setVisibility(View.GONE);
                                holder.imgCheck.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    } else {
                        holder.img_unCheckDisabled.setVisibility(View.GONE);
                        holder.imgChecked.setVisibility(View.GONE);
                        holder.imgCheck.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            holder.img_unCheckDisabled.setVisibility(View.GONE);
            holder.imgChecked.setVisibility(View.GONE);
            holder.imgCheck.setVisibility(View.VISIBLE);
        }

        holder.imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                if (testRateMasterModel.getTestType().equalsIgnoreCase("OFFER")) {
                    str = str + testRateMasterModel.getDescription() + ",";
                } else {
                    str = str + testRateMasterModel.getTestCode() + ",";
                }
                if ((str.contains("PSA") || str.contains("FPSA")) && !isM) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage(PSAandFPSAforMaleMsg)
                            .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .show();
                } else {
                    String slectedpackage = "";

                    if (testRateMasterModel.getTestType().equals("TEST") || testRateMasterModel.getTestType().equals("OFFER")
                            && !StringUtils.isNull(testRateMasterModel.getDescription())) {
                        slectedpackage = testRateMasterModel.getDescription();
                    } else {
                        slectedpackage = testRateMasterModel.getTestCode();
                    }
                    if (testRateMasterModel.getTestType().equalsIgnoreCase("OFFER") && checkIfOfferExists(selectedTests)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Confirm Action")
                                .setMessage(SelectingofferwillreplaceofferMsg)
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        if (onClickListeners != null) {
                                            onClickListeners.onCheckChange(selectedTests);
                                        }
                                    }
                                })
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selectedTests = replaceOffer(selectedTests, testRateMasterModel);
                                        if (onClickListeners != null) {
                                            onClickListeners.onCheckChange(selectedTests);
                                        }
                                    }
                                })
                                .setCancelable(false)
                                .show();

                    }else if (testRateMasterModel.getTestType().equalsIgnoreCase("POP") && checkIfPOPExists(selectedTests)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Confirm Action")
                                .setMessage(SelectingPOPwillreplaceofferMsg)
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        if (onClickListeners != null) {
                                            onClickListeners.onCheckChange(selectedTests);
                                        }
                                    }
                                })
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selectedTests = replacePOP(selectedTests, testRateMasterModel);
                                        if (onClickListeners != null) {
                                            onClickListeners.onCheckChange(selectedTests);
                                        }
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
                                                && !StringUtils.isNull(selectedTests.get(j).getDescription())) {
                                            tempselectedTests1.add(selectedTests.get(j).getDescription());
                                        } else {
                                            tempselectedTests1.add(selectedTests.get(j).getTestCode());
                                        }

                                        tempselectedTests.add(selectedTests.get(j));
                                    }
                                }
                            }
                        }
                        for (int j = 0; j < selectedTests.size(); j++) {
                            TestRateMasterModel selectedTestModel123 = selectedTests.get(j);
                            if (selectedTestModel123.getChldtests() != null && testRateMasterModel.getChldtests() != null && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {

                                if (selectedTests.get(j).getTestType().equals("TEST") || selectedTests.get(j).getTestType().equals("OFFER")
                                        && !StringUtils.isNull(selectedTests.get(j).getDescription())) {
                                    tempselectedTests1.add(selectedTests.get(j).getDescription());
                                } else {
                                    tempselectedTests1.add(selectedTests.get(j).getTestCode());
                                }
                                tempselectedTests.add(selectedTestModel123);
                            }
                        }

                        if (tempselectedTests.size() > 0 && tempselectedTests != null) {
                            String cartproduct = TextUtils.join(",", tempselectedTests1);
                            globalClass.showalert_OK(Html.fromHtml("<b>" + cartproduct + "</b>" + " has been upgraded to " + "<b>" + slectedpackage + "</b>").toString(), activity);
                        }
                        for (int i = 0; i < tempselectedTests.size(); i++) {
                            for (int j = 0; j < selectedTests.size(); j++) {
                                if (tempselectedTests.get(i).getTestCode().equalsIgnoreCase(selectedTests.get(j).getTestCode())) {
                                    selectedTests.remove(j);
                                }
                            }
                        }
                        //tejas t -----------------------------
                        selectedTests.add(testRateMasterModel);
                        if (onClickListeners != null) {
                            onClickListeners.onCheckChange(selectedTests);
                        }
                    }
                }


            }
        });

        holder.img_unCheckDisabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelectedDueToParent = false;
                String parentTestCode = "";
                if (selectedTests != null && selectedTests.size() > 0) {
                    for (int i = 0; i < selectedTests.size(); i++) {
                        TestRateMasterModel selectedTestModel = selectedTests.get(i);
                        if (selectedTestModel.getTestCode().equals(testRateMasterModel.getTestCode())) {
                            isSelectedDueToParent = false;
                            parentTestCode = "";
                            break;
                        }else if (selectedTestModel.getChldtests() != null && testRateMasterModel.getChldtests() != null && selectedTestModel.checkIfChildsContained(testRateMasterModel)) {
                            isSelectedDueToParent = true;
                            parentTestCode = selectedTestModel.getTestCode();
                        } else {
                            if (selectedTestModel.getChldtests() != null && selectedTestModel.getChldtests().size() > 0) {
                                for (ChildTestsModel ctm :
                                        selectedTestModel.getChldtests()) {
                                    if (ctm.getChildTestCode().equals(testRateMasterModel.getTestCode())) {
                                        isSelectedDueToParent = true;
                                        parentTestCode = selectedTestModel.getTestCode();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                if (isSelectedDueToParent) {
                    Toast.makeText(activity, "This test was selected because of its parent. If you wish to remove this test please remove the parent: " + parentTestCode, Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.imgChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedTests.remove(testRateMasterModel);
                if (onClickListeners != null) {
                    onClickListeners.onCheckChange(selectedTests);
                }
            }
        });

    }

    private boolean checkIfOfferExists(ArrayList<TestRateMasterModel> selTests) {
        for (TestRateMasterModel trmm :
                selTests) {
            if (!StringUtils.isNull(trmm.getTestType()) && trmm.getTestType().equalsIgnoreCase("OFFER")) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<TestRateMasterModel> replaceOffer(ArrayList<TestRateMasterModel> selTests, TestRateMasterModel newOffer) {
        for (int i = 0; i < selTests.size(); i++) {
            if (!StringUtils.isNull(selTests.get(i).getTestType()) && selTests.get(i).getTestType().equalsIgnoreCase("OFFER")) {
                selTests.remove(i);
                break;
            }
        }
        selTests.add(newOffer);
        return selTests;
    }

    private boolean checkIfPOPExists(ArrayList<TestRateMasterModel> selTests) {
        for (TestRateMasterModel trmm :
                selTests) {
            if (!StringUtils.isNull(trmm.getTestType()) && trmm.getTestType().equalsIgnoreCase("POP")) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<TestRateMasterModel> replacePOP(ArrayList<TestRateMasterModel> selTests, TestRateMasterModel newPOP) {
        for (int i = 0; i < selTests.size(); i++) {
            if (!StringUtils.isNull(selTests.get(i).getTestType()) && selTests.get(i).getTestType().equalsIgnoreCase("POP")) {
                selTests.remove(i);
                break;
            }
        }
        selTests.add(newPOP);
        return selTests;
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
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

            ArrayList<TestRateMasterModel> oldList = allProductList;
            ArrayList<TestRateMasterModel> newList = new ArrayList<TestRateMasterModel>();
            for (TestRateMasterModel testModel : oldList) {
                if (testModel.getTestCode().toLowerCase().contains(query) ||
                        testModel.getDescription().toLowerCase().contains(query)) {
                    newList.add(testModel);
                }
            }
            if (newList.size() > 0) {
                filteredList.addAll(newList);
            }
        }
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    public interface OnClickListeners {

        void onCheckChange(ArrayList<TestRateMasterModel> selectedTests);

    }
}
