package com.thyrocare.btechapp.Controller;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.LMEMapDisplayFragmentActivity;
import com.thyrocare.btechapp.fragment.LME.LME_OrdersDisplayFragment;
import com.thyrocare.btechapp.fragment.LME.LME_WLMISFragment;
import com.thyrocare.btechapp.models.api.request.SendScannedbarcodeLME;
import com.thyrocare.btechapp.models.data.DateWiseWLMISDetailsModel;
import com.thyrocare.btechapp.models.data.SampleDropDetailsbyTSPLMEDetailsModel;
import com.thyrocare.btechapp.models.data.WLMISDetailsModel;


import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;

/**
 * Created by E5233 on 4/30/2018.
 */

public class TSPLMESampleDropController {

    LMEMapDisplayFragmentActivity mLMEMapDisplayFragmentActivity;
    Context mContext;
    LME_OrdersDisplayFragment mLME_OrdersDisplayFragment;
    LME_WLMISFragment mLME_WLMISFragment;
    int flag = 0;
    private Global globalclass;

    public TSPLMESampleDropController(Context activity, LME_OrdersDisplayFragment fragment) {
        this.mContext = activity;
        globalclass = new Global(mContext);
        this.mLME_OrdersDisplayFragment = fragment;
        flag = 1;
    }

    public TSPLMESampleDropController(Context activity, LME_WLMISFragment fragment) {
        this.mContext = activity;
        globalclass = new Global(mContext);
        this.mLME_WLMISFragment = fragment;
    }

    public TSPLMESampleDropController(FragmentActivity activity, LMEMapDisplayFragmentActivity pLMEMapDisplayFragmentActivity) {
        this.mContext = activity;
        globalclass = new Global(mContext);
        this.mLMEMapDisplayFragmentActivity = pLMEMapDisplayFragmentActivity;
        flag = 3;
    }

    public void CallGetSampleDropDetailsbyTSPLME(String Id, int batch_f) {

        if (isNetworkAvailable(mContext)) {
            CallgetSampleDropDetailsbyTSPLMEApi(Id,batch_f);
        } else {
            Toast.makeText(mContext, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    public void CallPostScannedMasterBarcodebyLME(SendScannedbarcodeLME[] n) {

        if (isNetworkAvailable(mContext)) {
            CallPostScannedMasterBarcodebyLMEAPI(n);
        } else {
            Toast.makeText(mContext, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void CallgetSampleDropDetailsbyTSPLMEApi(String Id, int batch_f) {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mContext, EncryptionUtils.DecodeString64(mContext.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<SampleDropDetailsbyTSPLMEDetailsModel>> responseCall = apiInterface.CallgetSampleDropDetailsbyTSPLMEApi(Id, batch_f);
        globalclass.showProgressDialog(mContext,"Please wait..");
        responseCall.enqueue(new Callback<ArrayList<SampleDropDetailsbyTSPLMEDetailsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SampleDropDetailsbyTSPLMEDetailsModel>> call, retrofit2.Response<ArrayList<SampleDropDetailsbyTSPLMEDetailsModel>> response) {
                globalclass.hideProgressDialogg();
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<SampleDropDetailsbyTSPLMEDetailsModel> materialDetailsModels = response.body();
                    try {
                        if (materialDetailsModels != null) {
                            if (materialDetailsModels.size() != 0) {
                                mLME_OrdersDisplayFragment.SetOrdersList(materialDetailsModels);
                            } else {
                                mLME_OrdersDisplayFragment.NodataFound();
                            }
                        } else {
                            mLME_OrdersDisplayFragment.NodataFound();
                        }
                    } catch (Exception e) {

                    } finally {
                        if (materialDetailsModels != null) {
                            materialDetailsModels = null;
                        }
                    }

                } else {
                    Toast.makeText(mContext, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<SampleDropDetailsbyTSPLMEDetailsModel>> call, Throwable t) {
                globalclass.hideProgressDialogg();
                Toast.makeText(mContext, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void CallPostScannedMasterBarcodebyLMEAPI(SendScannedbarcodeLME[] n){

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mContext, EncryptionUtils.DecodeString64(mContext.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallPostScannedMasterBarcodebyLMEAPI(n);
        globalclass.showProgressDialog(mContext,"Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialogg();
                if (response.isSuccessful() && response.body() != null){
                    TastyToast.makeText(mContext, "" + response.body(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    if (flag == 1) {
                        mLME_OrdersDisplayFragment.StartButtonClickedSuccess();
                    } else if (flag == 3) {
                        mLMEMapDisplayFragmentActivity.EndButtonClickedSuccess();
                    }
                }else{
                    Toast.makeText(mContext, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialogg();
            }
        });
    }


    public void CallGetWLMIS(String Id) {
        if (isNetworkAvailable(mContext)) {
            CallgetWLMISApi(Id);
        } else {
            Toast.makeText(mContext, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void CallgetWLMISApi(String Id) {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mContext, EncryptionUtils.DecodeString64(mContext.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<WLMISDetailsModel>> responseCall = apiInterface.CallgetWLMISApi(Id);
        globalclass.showProgressDialog(mContext,"Please wait..");
        responseCall.enqueue(new Callback<ArrayList<WLMISDetailsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<WLMISDetailsModel>> call, retrofit2.Response<ArrayList<WLMISDetailsModel>> response) {
                globalclass.hideProgressDialogg();
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<WLMISDetailsModel> materialDetailsModels = response.body();
                    try {
                        if (materialDetailsModels != null) {
                            if (materialDetailsModels.size() != 0) {
                                mLME_WLMISFragment.SetOrdersList(materialDetailsModels);
                            } else {
                                mLME_WLMISFragment.NodataFound();
                            }
                        } else {
                            mLME_WLMISFragment.NodataFound();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (materialDetailsModels != null) {
                            materialDetailsModels = null;
                        }
                    }
                } else {
                    Toast.makeText(mContext, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<WLMISDetailsModel>> call, Throwable t) {
                globalclass.hideProgressDialogg();
                Toast.makeText(mContext, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void CallGetDateWiseWLMIS(String Id) {

        if (isNetworkAvailable(mContext)) {
            CallgetWLMISDateWiseApi(Id);
        } else {
            Toast.makeText(mContext, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void CallgetWLMISDateWiseApi(String Id) {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mContext, EncryptionUtils.DecodeString64(mContext.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<DateWiseWLMISDetailsModel>> responseCall = apiInterface.CallgetWLMISDateWiseApi(Id);
        globalclass.showProgressDialog(mContext,"Please wait..");
        responseCall.enqueue(new Callback<ArrayList<DateWiseWLMISDetailsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<DateWiseWLMISDetailsModel>> call, retrofit2.Response<ArrayList<DateWiseWLMISDetailsModel>> response) {
                globalclass.hideProgressDialogg();
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<DateWiseWLMISDetailsModel> materialDetailsModels = response.body();
                    try {
                        if (materialDetailsModels != null) {
                            if (materialDetailsModels.size() != 0) {
                                mLME_WLMISFragment.SetDateWiseOrdersList(materialDetailsModels);
                            } else {
                                mLME_WLMISFragment.NodataFound();
                            }
                        } else {
                            mLME_WLMISFragment.NodataFound();
                        }
                    } catch (Exception e) {

                    } finally {
                        if (materialDetailsModels != null) {
                            materialDetailsModels = null;
                        }
                    }
                } else {
                    Toast.makeText(mContext, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<DateWiseWLMISDetailsModel>> call, Throwable t) {
                globalclass.hideProgressDialogg();
                Toast.makeText(mContext, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}