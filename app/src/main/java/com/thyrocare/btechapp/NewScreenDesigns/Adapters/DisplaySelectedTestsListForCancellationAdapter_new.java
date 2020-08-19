package com.thyrocare.btechapp.NewScreenDesigns.Adapters;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.delegate.RemoveSelectedTestFromListDelegate_new;
import com.thyrocare.btechapp.models.data.TestRateMasterModel;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CannotemtytestlistMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SuretoRemoveTestMsg;


/**
 * Created by Orion on 4/27/2017.
 */

public class DisplaySelectedTestsListForCancellationAdapter_new extends BaseAdapter {
    private Activity activity;
    private AlertDialog.Builder alertDialogBuilder;
    private Global globalclass;
//    private ArrayList<BeneficiaryTestDetailsModel> selectedTestsListArr;
    private ArrayList<TestRateMasterModel> selectedTestsListArr;
    private RemoveSelectedTestFromListDelegate_new removeSelectedTestFromListDelegate;
    private LayoutInflater layoutInflater;

    public DisplaySelectedTestsListForCancellationAdapter_new(Activity activity, ArrayList<TestRateMasterModel> selectedTestsListArr, RemoveSelectedTestFromListDelegate_new removeSelectedTestFromListDelegate) {
        this.activity = activity;
        this.removeSelectedTestFromListDelegate = removeSelectedTestFromListDelegate;
        this.selectedTestsListArr = selectedTestsListArr;
        globalclass = new Global(activity);
        if(this.selectedTestsListArr==null){
            this.selectedTestsListArr=new ArrayList<>();
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
                if(selectedTestsListArr.size()>1) {

                    alertDialogBuilder = new AlertDialog.Builder(activity);
                    alertDialogBuilder
                            .setMessage(SuretoRemoveTestMsg)
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    selectedTestsListArr.remove(pos);
                                    removeSelectedTestFromListDelegate.onRemoveButtonClicked(selectedTestsListArr);
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
                else{
                    globalclass.showCustomToast(activity, CannotemtytestlistMsg);

                }
            }
        });
    }

    private void initData(ViewHolder holder,int pos) {
        //holder.txtTestName.setText(selectedTestsListArr.get(pos).getDescription()+" ("+selectedTestsListArr.get(pos).getTestCode()+")");
        if(!StringUtils.isNull(selectedTestsListArr.get(pos).getTestType())&&(selectedTestsListArr.get(pos).getTestType().equals("TEST")||selectedTestsListArr.get(pos).getTestType().equals("OFFER"))
                && !StringUtils.isNull(selectedTestsListArr.get(pos).getDescription())){
            holder.txtTestName.setText(selectedTestsListArr.get(pos).getDescription());
        }else{
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
