package com.thyrocare.adapter;


import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.delegate.RemoveSelectedTestFromListDelegate;
import com.thyrocare.fragment.BeneficiariesDisplayFragment;
import com.thyrocare.models.data.BeneficiaryTestDetailsModel;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;

import java.util.ArrayList;


/**
 * Created by Orion on 4/27/2017.
 */

public class DisplaySelectedTestsListForCancellationAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<BeneficiaryTestDetailsModel> selectedTestsListArr;
    private RemoveSelectedTestFromListDelegate removeSelectedTestFromListDelegate;
    private LayoutInflater layoutInflater;
    private boolean isINSPPpresent=false;
    private boolean isPPBSpresent=false;
    private boolean isFBSpresent=false;
    private boolean isFastingPresent=false;
    private int FastingCount=0;

    public DisplaySelectedTestsListForCancellationAdapter(Activity activity, ArrayList<BeneficiaryTestDetailsModel> selectedTestsListArr, RemoveSelectedTestFromListDelegate removeSelectedTestFromListDelegate) {
        this.activity = activity;
        this.removeSelectedTestFromListDelegate = removeSelectedTestFromListDelegate;
        this.selectedTestsListArr = selectedTestsListArr;
        if (this.selectedTestsListArr == null) {
            this.selectedTestsListArr = new ArrayList<>();
        }
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
                FastingCount=0;
                if (selectedTestsListArr.size() > 1) {
                    Logger.error("is fasting "+selectedTestsListArr.get(pos).getFasting());
                    for (int i = 0; i <selectedTestsListArr.size() ; i++) {
                        if(selectedTestsListArr.get(i).getTests().equalsIgnoreCase(AppConstants.INSPP)){
                            isINSPPpresent=true;
                        }
                        if (selectedTestsListArr.get(i).getTests().equalsIgnoreCase(AppConstants.PPBS)){
                            isPPBSpresent=true;
                        }
                        if(selectedTestsListArr.get(i).getTests().equalsIgnoreCase(AppConstants.FBS)){
                            isFBSpresent=true;

                        }
                        if(selectedTestsListArr.get(i).getFasting().equalsIgnoreCase("Fasting")){
                            FastingCount= FastingCount+1;
                            Logger.error("FastingCount "+FastingCount);
                            isFastingPresent=true;
                        }

                    }

                    if (selectedTestsListArr.get(pos).getTests().equalsIgnoreCase(AppConstants.FBS) && isPPBSpresent ||
                            selectedTestsListArr.get(pos).getTests().equalsIgnoreCase(AppConstants.INSFA) && isINSPPpresent
                            ) {
                        Logger.error("isINSPPpresent size " + isINSPPpresent);
                        Logger.error("selectedTestsListArr size " + selectedTestsListArr.size());
                        Logger.error("selectedTestsListArr name " + selectedTestsListArr.toArray());
                        Logger.error("selectedTestsListArr string " + selectedTestsListArr.toString());

                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                        builder1.setTitle("Warning ");
                        if (selectedTestsListArr.get(pos).getTests().equalsIgnoreCase(AppConstants.FBS)) {

                            builder1.setMessage("Please note in case you are removing FBBS test then PPBS test will automatically removed ");
                        } else {
                            builder1.setMessage("Please note in case you are removing INSFA test then INSPP test will automatically removed ");
                        }

                        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (selectedTestsListArr.get(pos).getTests().equalsIgnoreCase(AppConstants.FBS)) {
                                    BeneficiariesDisplayFragment.isFBSTestRemoved = "removed";
                                } else if (selectedTestsListArr.get(pos).getTests().equalsIgnoreCase(AppConstants.INSFA)) {
                                    BeneficiariesDisplayFragment.isINSFATestRemoved = "removed";
                                }


                                selectedTestsListArr.remove(pos);
                                for (int i = 0; i < selectedTestsListArr.size(); i++) {
                                    if (selectedTestsListArr.get(i).getTests().equalsIgnoreCase(AppConstants.PPBS)) {
                                        selectedTestsListArr.remove(i);

                                    }
                                }

                                removeSelectedTestFromListDelegate.onRemoveButtonClicked(selectedTestsListArr);

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder1.show();


                    }

                    //jai
                    else if (selectedTestsListArr.get(pos).getTests().equalsIgnoreCase(AppConstants.PPBS)) {
                        BeneficiariesDisplayFragment.isPPBSTestRemoved = "ppbs removed";
                        isPPBSpresent=false;
                        Logger.error("removing PPBS");
                        selectedTestsListArr.remove(pos);
                    } else if (selectedTestsListArr.get(pos).getTests().equalsIgnoreCase(AppConstants.INSPP)) {
                        BeneficiariesDisplayFragment.isINSPPTestRemoved = "inspp removed";
                        isINSPPpresent=false;

                        Logger.error("removing INSPP");
                        selectedTestsListArr.remove(pos);
                    }
                    //jai

                    else  if( !selectedTestsListArr.get(pos).getTests().equalsIgnoreCase(AppConstants.FBS)
                            && isFBSpresent==true && FastingCount==2 &&
                            selectedTestsListArr.get(pos).getFasting().equalsIgnoreCase("Fasting") )
                            {
                                Toast.makeText(activity, "Cannot remove "+selectedTestsListArr.get(pos).getTests()+" test.", Toast.LENGTH_SHORT).show();
                            }
                    else {
                        selectedTestsListArr.remove(pos);
                    }
                    removeSelectedTestFromListDelegate.onRemoveButtonClicked(selectedTestsListArr);
                } else {
                    Toast.makeText(activity, "Cannot empty the test list", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData(ViewHolder holder, int pos) {
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
