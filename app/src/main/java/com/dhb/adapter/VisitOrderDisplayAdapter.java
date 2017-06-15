package com.dhb.adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.delegate.VisitOrderDisplayRecyclerViewAdapterDelegate;
import com.dhb.models.api.request.CallPatchRequestModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppConstants;
import com.dhb.utils.app.AppPreferenceManager;
import com.ramotion.foldingcell.FoldingCell;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by ISRO on 4/27/2017.
 */

public class VisitOrderDisplayAdapter extends BaseAdapter {
    private HomeScreenActivity activity;
    private ArrayList<OrderVisitDetailsModel> orderVisitDetailsModelsArr;
    private VisitOrderDisplayRecyclerViewAdapterDelegate visitOrderDisplayRecyclerViewAdapterDelegate;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private LayoutInflater layoutInflater;
    private AppPreferenceManager appPreferenceManager;
    private String MaskedPhoneNumber = "";
    private boolean isSelected;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    public VisitOrderDisplayAdapter(HomeScreenActivity activity, ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels, VisitOrderDisplayRecyclerViewAdapterDelegate visitOrderDisplayRecyclerViewAdapterDelegate) {
        this.activity = activity;
        this.orderVisitDetailsModelsArr = orderDetailsResponseModels;
        this.visitOrderDisplayRecyclerViewAdapterDelegate = visitOrderDisplayRecyclerViewAdapterDelegate;
        layoutInflater = LayoutInflater.from(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
    }


    @Override
    public int getCount() {
        return orderVisitDetailsModelsArr.size();
    }

    @Override
    public Object getItem(int position) {
        return orderVisitDetailsModelsArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FoldingCellViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_folding_cell, parent, false);
            holder = new FoldingCellViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (FoldingCellViewHolder) convertView.getTag();
        }
        initData(holder, position);
        initListeners(holder, position);
        return convertView;
    }

    private void initListeners(final FoldingCellViewHolder holder, final int pos) {


        holder.imgRelease2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitOrderDisplayRecyclerViewAdapterDelegate.onItemRelease(orderVisitDetailsModelsArr.get(pos));
            }
        });
        holder.imgRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitOrderDisplayRecyclerViewAdapterDelegate.onItemRelease(orderVisitDetailsModelsArr.get(pos));
            }
        });
        holder.btnStartNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.error("Status: "+orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus());
                if (!orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equals("Y") ) {

                    visitOrderDisplayRecyclerViewAdapterDelegate.onNavigationStart(orderVisitDetailsModelsArr.get(pos));
                } else {
                    Toast.makeText(activity, "Please accept the order first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos == 0) {
                    registerToggle(pos);
                    holder.cell.toggle(false);
                    initData(holder, pos);
                } else {
                    Toast.makeText(activity, "Please service the earlier orders first", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }





    private void initData(FoldingCellViewHolder holder, final int pos) {

        if (orderVisitDetailsModelsArr.size() > pos
                && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().size() > 0
                && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().size() > 0
                && orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0) != null) {
            holder.tvAge.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getAge() + " Y | " + orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getGender());

            holder.tvName.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getName());
            holder.tvName.setSelected(true);
            holder.tvSrNo.setText(pos + 1 + "");
            holder.txtorderno.setSelected(true);

            holder.pindata.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getPincode());
            holder.timedata.setText(orderVisitDetailsModelsArr.get(pos).getSlot() + "  HRS");
            holder.txtorderno.setText(orderVisitDetailsModelsArr.get(pos).getVisitId());
            holder.txtAge.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getAge() + " Y | " + orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getGender());
            holder.txtName.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getBenMaster().get(0).getName());
            holder.txtName.setSelected(true);
            holder.txtSrNo.setText(pos + 1 + "");


            holder.txt_distance.setText(String.valueOf(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getDistance()) + "KM");
            holder.txtKits.setText(String.valueOf(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getKits().get(0).getKit()));

            holder.txtAddress.setText(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getAddress());
//            holder.tvAadharNo.setVisibility(View.GONE);
            holder.txtAadharNo.setVisibility(View.GONE);
            if (pos != 0) {
                holder.btnStartNavigation.setVisibility(View.VISIBLE);
            }
            if (unfoldedIndexes.contains(pos)) {
                holder.cell.unfold(true);
            } else {
                holder.cell.fold(true);
            }
            Logger.error("ASASASASA"+orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus());
            if (orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().trim().equalsIgnoreCase("FIX APPOINTMENT") ||orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getStatus().equals("ASSIGNED")) {
                Toast.makeText(activity, "inside", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, "inside", Toast.LENGTH_SHORT).show();
                holder.imgCBAccept.setVisibility(View.VISIBLE);
                holder.imgCBAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        visitOrderDisplayRecyclerViewAdapterDelegate.onOrderAccepted(orderVisitDetailsModelsArr.get(pos));
                    }
                });
            }else {
                holder.imgCBAccept.setVisibility(View.INVISIBLE);
            }


            holder.imgcall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CallPatchRequestModel callPatchRequestModel = new CallPatchRequestModel();
                    callPatchRequestModel.setSrcnumber(appPreferenceManager.getLoginResponseModel().getUserID());
                    callPatchRequestModel.setDestNumber(orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getMobile());


                         Logger.error("orderVisitDetailsModelsArr"+orderVisitDetailsModelsArr.get(pos).getAllOrderdetails().get(0).getMobile());
                        callPatchRequestModel.setVisitID(orderVisitDetailsModelsArr.get(0).getVisitId());
                        ApiCallAsyncTask callPatchRequestAsyncTask = new AsyncTaskForRequest(activity).getCallPatchRequestAsyncTask(callPatchRequestModel);
                        callPatchRequestAsyncTask.setApiCallAsyncTaskDelegate(new CallPatchRequestAsyncTaskDelegateResult());
                        callPatchRequestAsyncTask.execute(callPatchRequestAsyncTask);


                }
            });
        }


    }

    class CallPatchRequestAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    MaskedPhoneNumber = json;
                    intent.setData(Uri.parse("tel:" + MaskedPhoneNumber));
                    Logger.error("MaskedPhoneNumber"+MaskedPhoneNumber);

                    appPreferenceManager.setMaskNumber(MaskedPhoneNumber);
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{
                                        Manifest.permission.CALL_PHONE},
                                AppConstants.APP_PERMISSIONS);
                    } else {
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onApiCancelled() {

        }

    }



    private class FoldingCellViewHolder {
        TextView tvSrNo, tvName, tvAge, txtAddress, txtorderno, txtKits, timedata, timetitle, txt_distance, pintitle, pindata;//tvAadharNo,
        ImageView imgCBAccept;
        TextView txtSrNo, txtName, txtAge, txtAadharNo;
        ImageView imgRelease, imgRelease2,imgcall;
        Button btnStartNavigation;
        FoldingCell cell;

        FoldingCellViewHolder(View itemView) {
            initUI(itemView);
        }

        private void initUI(View itemView) {

            imgcall=(ImageView) itemView.findViewById(R.id.call);
            imgcall.setVisibility(View.VISIBLE);
            pintitle = (TextView) itemView.findViewById(R.id.pincode_title);
            pindata = (TextView) itemView.findViewById(R.id.pincode_data);
            cell = (FoldingCell) itemView.findViewById(R.id.item_folding_cell);
            tvSrNo = (TextView) itemView.findViewById(R.id.tv_sr_no);
            timedata = (TextView) itemView.findViewById(R.id.time_data);
            timetitle = (TextView) itemView.findViewById(R.id.time_title);
            txtSrNo = (TextView) itemView.findViewById(R.id.txt_sr_no);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtAge = (TextView) itemView.findViewById(R.id.txt_age);
            txtAadharNo = (TextView) itemView.findViewById(R.id.txt_aadhar_no);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAge = (TextView) itemView.findViewById(R.id.tv_age);
//            tvAadharNo = (TextView) itemView.findViewById(R.id.tv_aadhar_no);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            txtorderno = (TextView) itemView.findViewById(R.id.tv_orderno);
            txtKits = (TextView) itemView.findViewById(R.id.txt_num_kits);
            imgRelease = (ImageView) itemView.findViewById(R.id.img_release);
            btnStartNavigation = (Button) itemView.findViewById(R.id.btn_start_navigation);
            imgCBAccept = (ImageView) itemView.findViewById(R.id.img_oas);
            imgRelease2 = (ImageView) itemView.findViewById(R.id.img_release2);

            txt_distance = (TextView) itemView.findViewById(R.id.txt_distance_1);


            timedata.setVisibility(View.VISIBLE);
            timetitle.setVisibility(View.VISIBLE);

            pintitle.setVisibility(View.VISIBLE);
            pindata.setVisibility(View.VISIBLE);
        }
    }

    // simple methods for register cell state changes
    private void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    private void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    private void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

}










