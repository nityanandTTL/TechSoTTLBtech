package com.thyrocare.btechapp.NewScreenDesigns.Adapters;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.delegate.RemoveSelectedTestFromListDelegate_new;
import com.thyrocare.btechapp.models.api.response.GetPETestResponseModel;
import com.thyrocare.btechapp.models.data.TestRateMasterModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CannotemtytestlistMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SuretoRemoveTestMsg;


/**
 * Created by Orion on 4/27/2017.
 */

public class DisplaySelectedTestsListForCancellationAdapter_new extends BaseAdapter {
    ArrayList<String> newDistest;
    String selectedTest;
    boolean isAddBen, isPePartner;
    ArrayList<String> DisTest;
    int ArraySize;
    AppPreferenceManager appPreferenceManager;
    ArrayList<String> strArray;
    private Activity activity;
    private AlertDialog.Builder alertDialogBuilder;
    private Global globalclass;
    //    private ArrayList<BeneficiaryTestDetailsModel> selectedTestsListArr;
    private ArrayList<TestRateMasterModel> selectedTestsListArr;
    private ArrayList<GetPETestResponseModel.DataDTO> peselectedTestsList;
    private RemoveSelectedTestFromListDelegate_new removeSelectedTestFromListDelegate;
    private LayoutInflater layoutInflater;

    public DisplaySelectedTestsListForCancellationAdapter_new(Activity activity, boolean b, ArrayList<TestRateMasterModel> selectedTestsListArr, ArrayList<GetPETestResponseModel.DataDTO> peselectedTestsList, String selectedTestCode, RemoveSelectedTestFromListDelegate_new removeSelectedTestFromListDelegate) {
        this.activity = activity;
        this.removeSelectedTestFromListDelegate = removeSelectedTestFromListDelegate;
        this.selectedTestsListArr = selectedTestsListArr;
        selectedTest = selectedTestCode;
        this.peselectedTestsList = peselectedTestsList;
        isAddBen = b;
        globalclass = new Global(activity);
       /* if (this.selectedTestsListArr.size()==0 && !InputUtils.isNull(selectedTestCode)){
            DisTest = new ArrayList<>();
            String[] str = selectedTestCode.split(",");
            for (int i = 0; i < str.length; i++) {
                String str1 = str[i].trim();
                DisTest.add(str1);
            }
        }*/
        //   ArraySize = arraySize;
        if (this.selectedTestsListArr == null) {
            this.selectedTestsListArr = new ArrayList<>();
        }
        layoutInflater = activity.getLayoutInflater();
        appPreferenceManager = new AppPreferenceManager(activity);
        strArray = new ArrayList<>();
        String str1 = selectedTest.trim();
        System.out.println("" + str1);
        String[] strings = selectedTest.split(",");
        for (int i = 0; i < strings.length; i++) {
            String st = strings[i].trim();
            strArray.add(st);
        }
    }

    @Override
    public int getCount() {
        //fungible
//        if (BundleConstants.isPEPartner) {
        if (appPreferenceManager.isPEPartner()) {
//        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
            return peselectedTestsList.size();
        }
       /* if (selectedTestsListArr.size()==0 && ArraySize!=0){
            return ArraySize;
        }*/
        /*if (selectedTestsListArr.size()!= strArray.size()){
            return strArray.size();
        }*/
        return selectedTestsListArr.size();
    }

    @Override
    public Object getItem(int position) {
        //fungible
//        if (BundleConstants.isPEPartner) {
        if (appPreferenceManager.isPEPartner()) {
//        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
            return peselectedTestsList.get(position);
        }
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

        //fungible
//        if (BundleConstants.isPEPartner) {
        if (appPreferenceManager.isPEPartner()) {
//        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
            initPEData(holder, position);
            initPEListeners(holder, position);
        } else {
            // initData(holder, position);
            //  initListeners(holder, position);
            //TODO Did if test not present in the ratemaster
            ininTest(holder, isAddBen, position, selectedTest);
//        initData(holder, position);
            initListeners(holder, position);
        }

        return convertView;
    }

    private void initPEData(ViewHolder holder, int position) {
        /*if (!StringUtils.isNull(peselectedTestsList.get(position).getLab_dos_name())&& peselectedTestsList.get(position).getLab_dos_name().equalsIgnoreCase(peselectedTestsList.get(position).getLab_dos_name())){
            holder.txtTestName.setText(peselectedTestsList.get(position).getName());
        } else if(!StringUtils.isNull(peselectedTestsList.get(position).getPartner_lab_test_id())&& peselectedTestsList.get(position).getPartner_lab_test_id().equalsIgnoreCase(peselectedTestsList.get(position).getPartner_lab_test_id())){
            holder.txtTestName.setText(peselectedTestsList.get(position).getName());
        }else{
            holder.txtTestName.setText(peselectedTestsList.get(position).getName());
        }*/

        if (!StringUtils.isNull(peselectedTestsList.get(position).getName())) {
            holder.txtTestName.setText(peselectedTestsList.get(position).getName());
        }

    }

    private void initPEListeners(ViewHolder holder, final int pos) {
        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder
                        .setMessage(SuretoRemoveTestMsg)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                peselectedTestsList.remove(pos);
                                removeSelectedTestFromListDelegate.onRemovePEButtonClicked(peselectedTestsList);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                    /*selectedTestsListArr.remove(pos);
                    removeSelectedTestFromListDelegate.onRemoveButtonClicked(selectedTestsListArr);*/

            }
        });
    }

    private void ininTest(ViewHolder holder, boolean isAddBen, int position, String selectedTest) {
        try {
            String st = "";
            if (isAddBen) {
                if (!StringUtils.isNull(selectedTestsListArr.get(position).getTestType()) && (selectedTestsListArr.get(position).getTestType().equals("TEST") || selectedTestsListArr.get(position).getTestType().equals("OFFER"))
                        && !StringUtils.isNull(selectedTestsListArr.get(position).getDescription())) {
                    holder.txtTestName.setText(selectedTestsListArr.get(position).getDescription());
                } else {
                    holder.txtTestName.setText(selectedTestsListArr.get(position).getTestCode());
                }
            } else {
                String[] strings = selectedTest.split(",");
                newDistest = new ArrayList<>();
                for (int i = 0; i < strings.length; i++) {
                    st = strings[i].trim();
                    newDistest.add(st);
                }
                holder.txtTestName.setText(newDistest.get(position));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners(final ViewHolder holder, final int pos) {

        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (selectedTestsListArr.size() > 1) {
                        if (isAddBen) {
                            alertDialogBuilder = new AlertDialog.Builder(activity);
                            alertDialogBuilder
                                    .setMessage(SuretoRemoveTestMsg)
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            selectedTestsListArr.remove(pos);
                                            removeSelectedTestFromListDelegate.onRemoveButtonClicked(selectedTestsListArr, "");
                                            dialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        } else {
                            alertDialogBuilder = new AlertDialog.Builder(activity);
                            alertDialogBuilder
                                    .setMessage(SuretoRemoveTestMsg)
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            String Tests = "";
                                            System.out.println("<<<<<<<<<<<<<<<<" + holder.txtTestName.getText().toString() + ">>>>>>>>>>>>>>>>>>>" + selectedTestsListArr.get(pos).getDescription());
                                            /*if (checkTest(holder.txtTestName.getText().toString(), selectedTestsListArr)) {

                                                Tests = TextUtils.join(",", newDistest);
                                                removeSelectedTestFromListDelegate.onRemoveButtonClicked(selectedTestsListArr, Tests);
                                            } else {
                                                newDistest.remove(pos);
                                                selectedTestsListArr.remove(pos);
                                                Tests = TextUtils.join(",", newDistest);
                                                removeSelectedTestFromListDelegate.onRemoveButtonClicked(selectedTestsListArr, Tests);
                                            }*/

                                            if (!InputUtils.isNull(selectedTestsListArr.get(pos).getDescription()) ? holder.txtTestName.getText().toString().trim().equalsIgnoreCase(selectedTestsListArr.get(pos).getDescription())
                                                    : holder.txtTestName.getText().toString().trim().equalsIgnoreCase(newDistest.get(pos))) {
                                                selectedTestsListArr.remove(pos);
                                                newDistest.remove(pos);
                                                Tests = TextUtils.join(",", newDistest);
                                                selectedTest = Tests;
                                                removeSelectedTestFromListDelegate.onRemoveButtonClicked(selectedTestsListArr, Tests);
                                            } else {
                                                newDistest.remove(pos);
                                                selectedTestsListArr.remove(pos);
                                                Tests = TextUtils.join(",", newDistest);
                                                selectedTest = Tests;
                                                removeSelectedTestFromListDelegate.onRemoveButtonClicked(selectedTestsListArr, Tests);
                                            }
                                            dialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                        /*selectedTestsListArr.remove(pos);
                        removeSelectedTestFromListDelegate.onRemoveButtonClicked(selectedTestsListArr);*/
                    } else {
                        globalclass.showCustomToast(activity, CannotemtytestlistMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean checkTest(String s, ArrayList<TestRateMasterModel> selectedTestsListArr) {
        for (int i = 0; i < selectedTestsListArr.size(); i++) {
            if (!StringUtils.isNull(selectedTestsListArr.get(i).getDescription())) {
                if (s.equalsIgnoreCase(selectedTestsListArr.get(i).getDescription())) {
                    selectedTestsListArr.remove(i);
                    newDistest.remove(i);
                    return true;
                }
            } else {
                if (s.equalsIgnoreCase(selectedTestsListArr.get(i).getTestCode())) {
                    selectedTestsListArr.remove(i);
                    newDistest.remove(i);
                    return true;
                }
            }

        }
        return false;
    }

    private void removeTest(String s, ArrayList<TestRateMasterModel> selectedTestsListArr) {
        for (int i = 0; i < selectedTestsListArr.size(); i++) {
            if (!StringUtils.isNull(selectedTestsListArr.get(i).getDescription())) {
                if (s.equalsIgnoreCase(selectedTestsListArr.get(i).getDescription())) {
                    selectedTestsListArr.remove(i);
                    newDistest.remove(i);
                    break;
                }
            } else {
                if (s.equalsIgnoreCase(selectedTestsListArr.get(i).getTestCode())) {
                    selectedTestsListArr.remove(i);
                    newDistest.remove(i);
                    break;
                }
            }

        }
    }

    private void initData(ViewHolder holder, int pos) {
        //holder.txtTestName.setText(selectedTestsListArr.get(pos).getDescription()+" ("+selectedTestsListArr.get(pos).getTestCode()+")");
        if (!StringUtils.isNull(selectedTestsListArr.get(pos).getTestType()) && (selectedTestsListArr.get(pos).getTestType().equals("TEST") || selectedTestsListArr.get(pos).getTestType().equals("OFFER"))
                && !StringUtils.isNull(selectedTestsListArr.get(pos).getDescription())) {
            holder.txtTestName.setText(selectedTestsListArr.get(pos).getDescription());
        } else {
            holder.txtTestName.setText(selectedTestsListArr.get(pos).getTestCode());
        }
        //holder.txtTestName.setText(selectedTestsListArr.get(pos).getDescription());
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
