package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.StartAndArriveActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.VisitOrdersDisplayFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.FixAppointmentDataModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.ResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.NewOrderReleaseActivity;
import com.thyrocare.btechapp.activity.RescheduleSlotActivity;
import com.thyrocare.btechapp.models.api.request.PECutomerIntimationSMSRequestModel;
import com.thyrocare.btechapp.models.api.response.GetPEBtechSlotResponseModel;
import com.thyrocare.btechapp.models.api.response.GetPECancelRemarksResponseModel;
import com.thyrocare.btechapp.models.api.response.GetRemarksResponseModel;
import com.thyrocare.btechapp.models.api.response.PECutomerIntimationSMSResponeModel;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderReleaseRemarksController {

    Activity activity;
    StartAndArriveActivity startAndArriveActivity;
    NewOrderReleaseActivity newOrderReleaseActivity;
    RescheduleSlotActivity rescheduleSlotActivity;
    VisitOrdersDisplayFragment_new visitOrdersDisplayFragment_new;
    Global globalClass;
    int flag;

    public OrderReleaseRemarksController(StartAndArriveActivity startAndArriveActivity) {
        this.activity = startAndArriveActivity;
        this.startAndArriveActivity = startAndArriveActivity;
        globalClass = new Global(activity);
        flag = 1;
    }

    public OrderReleaseRemarksController(VisitOrdersDisplayFragment_new visitOrdersDisplayFragment_new) {
        this.activity = visitOrdersDisplayFragment_new;
        this.visitOrdersDisplayFragment_new = visitOrdersDisplayFragment_new;
        globalClass = new Global(activity);
        flag = 2;
    }

    public OrderReleaseRemarksController(NewOrderReleaseActivity newOrderReleaseActivity) {
        this.activity = newOrderReleaseActivity;
        this.newOrderReleaseActivity = newOrderReleaseActivity;
        globalClass = new Global(activity);
        flag = 3;
    }

    public OrderReleaseRemarksController(RescheduleSlotActivity rescheduleSlotActivity) {
        this.activity = rescheduleSlotActivity;
        this.rescheduleSlotActivity = rescheduleSlotActivity;
        globalClass = new Global(activity);
        flag = 4;
    }

    public void getSMS(final PECutomerIntimationSMSRequestModel smsRequestModel) {
        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<PECutomerIntimationSMSResponeModel> smsResponeModelCall = postAPIInterface.getSMS(smsRequestModel);
            smsResponeModelCall.enqueue(new Callback<PECutomerIntimationSMSResponeModel>() {
                @Override
                public void onResponse(Call<PECutomerIntimationSMSResponeModel> call, retrofit2.Response<PECutomerIntimationSMSResponeModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            PECutomerIntimationSMSResponeModel smsResponeModel = response.body();
                            if (smsResponeModel.getResponseId().equalsIgnoreCase(ConstantsMessages.RES0000)) {
                                newOrderReleaseActivity.smsSent(smsResponeModel);
                            } else {
                                TastyToast.makeText(activity, smsResponeModel.getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            }
                        } else {
                            globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PECutomerIntimationSMSResponeModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateOrderHistory(final int i, String token, FixAppointmentDataModel fadm) {
        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<ResponseModel> smsResponeModelCall = postAPIInterface.updateOrderHistory(token, fadm);
            smsResponeModelCall.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            ResponseModel responseModel = response.body();
                            if (i==1){
                                newOrderReleaseActivity.slotSubmitresponse(responseModel);
                            }else{
                                rescheduleSlotActivity.slotSubmitresponse(responseModel);
                            }

                        } else {
                            globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPEbtechSlot(String token, String pincode, String date) {
        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<ArrayList<GetPEBtechSlotResponseModel>> smsResponeModelCall = getAPIInterface.getPEbtechSlot(token, pincode, date);
            smsResponeModelCall.enqueue(new Callback<ArrayList<GetPEBtechSlotResponseModel>>() {
                @Override
                public void onResponse(Call<ArrayList<GetPEBtechSlotResponseModel>> call, retrofit2.Response<ArrayList<GetPEBtechSlotResponseModel>> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<GetPEBtechSlotResponseModel> slotResponseModelArrayList = new ArrayList<>();
                            slotResponseModelArrayList = response.body();
                            rescheduleSlotActivity.slotresponse(slotResponseModelArrayList);
                        } else {
                            globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<GetPEBtechSlotResponseModel>> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getRemarks(String ID, final int i) {
        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<ArrayList<GetRemarksResponseModel>> smsResponeModelCall = getAPIInterface.getRemarks(ID);
            smsResponeModelCall.enqueue(new Callback<ArrayList<GetRemarksResponseModel>>() {
                @Override
                public void onResponse(Call<ArrayList<GetRemarksResponseModel>> call, retrofit2.Response<ArrayList<GetRemarksResponseModel>> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<GetRemarksResponseModel> responseModelArrayList = new ArrayList<>();
                            responseModelArrayList = response.body();
                            if (i==2){
                                for (int j = 0; j < responseModelArrayList.size(); j++) {
                                    if (responseModelArrayList.get(j).getId() == 109){
                                        responseModelArrayList.remove(j);
                                    }
                                }
                                visitOrdersDisplayFragment_new.remarksArrayList(responseModelArrayList);
                            }else {
                                startAndArriveActivity.remarksArrayList(responseModelArrayList, i);
                            }
                        } else {
                            globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<GetRemarksResponseModel>> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getReasons(final String ID) {
        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<ArrayList<GetPECancelRemarksResponseModel>> smsResponeModelCall = getAPIInterface.getReasons(ID);
            smsResponeModelCall.enqueue(new Callback<ArrayList<GetPECancelRemarksResponseModel>>() {
                @Override
                public void onResponse(Call<ArrayList<GetPECancelRemarksResponseModel>> call, retrofit2.Response<ArrayList<GetPECancelRemarksResponseModel>> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<GetPECancelRemarksResponseModel> responseModelArrayList = new ArrayList<>();
                            responseModelArrayList = response.body();
                            if (flag ==1){
                                startAndArriveActivity.getReasons(responseModelArrayList,ID);
                            }else{
                                visitOrdersDisplayFragment_new.getReasons(responseModelArrayList,ID);
                            }

                        } else {
                            globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<GetPECancelRemarksResponseModel>> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
