package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.AddEditBenificaryActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.LoginActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.StartAndArriveActivity;
import com.thyrocare.btechapp.NewScreenDesigns.AddRemoveTestProfileActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.SendEventRequestModel;
import com.thyrocare.btechapp.models.api.response.GetPETestResponseModel;
import com.thyrocare.btechapp.models.api.response.LoginResponseModel;
import com.thyrocare.btechapp.models.api.response.PEAuthResponseModel;
import com.thyrocare.btechapp.models.api.response.SendEventResponeModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;

public class PEAuthorizationController {

    Activity activity;
    StartAndArriveActivity startAndArriveActivity;
    Global globalClass;
    AddEditBenificaryActivity addEditBenificaryActivity;
    AddRemoveTestProfileActivity addRemoveTestProfileActivity;
    AppPreferenceManager appPreferenceManager;
    int flag;
    String Str_pincode, Str_auth_token;

    public PEAuthorizationController(StartAndArriveActivity startAndArriveActivity) {
        this.activity = startAndArriveActivity;
        this.startAndArriveActivity = startAndArriveActivity;
        globalClass = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        flag = 0;
    }

    public PEAuthorizationController(AddEditBenificaryActivity addEditBenificaryActivity) {
        this.activity = addEditBenificaryActivity;
        this.addEditBenificaryActivity = addEditBenificaryActivity;
        globalClass = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        flag = 1;
    }

    public PEAuthorizationController(AddRemoveTestProfileActivity addRemoveTestProfileActivity) {
        this.activity = addRemoveTestProfileActivity;
        this.addRemoveTestProfileActivity = addRemoveTestProfileActivity;
        globalClass = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        flag = 2;
    }

    public void getAuthorizationToken(final int flag, String pincode, final String orderNo) {

        try {
            Str_pincode = pincode;
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.PE_API))).create(GetAPIInterface.class);
            Call<PEAuthResponseModel> peAuthResponseModelCall = getAPIInterface.callPEAuthorization(Constants.content_type, Constants.XSource, Constants.clientid);
            String str = EncryptionUtils.Dcrp_Hex(activity.getString(R.string.PE_API));
            System.out.println("Mith>>>>PE Auth<<<" + "" + str + "api/integration/v1/auth");
            System.out.println("x-source: " + Constants.content_type);
            System.out.println("client-id: " + Constants.XSource);
            System.out.println("Content-Type: " + Constants.clientid);
            peAuthResponseModelCall.enqueue(new Callback<PEAuthResponseModel>() {
                @Override
                public void onResponse(Call<PEAuthResponseModel> call, retrofit2.Response<PEAuthResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            PEAuthResponseModel peAuthResponseModel = response.body();
                            if (peAuthResponseModel.isStatus() == true) {
                                appPreferenceManager.setAuthToken(peAuthResponseModel.getData().getAuthtoken());
                                System.out.println("shared>>>mith-----" + appPreferenceManager.getAuthToken());
                                getPETests(flag, Str_pincode, orderNo);

                            } else if (peAuthResponseModel.isStatus() == false) {
                                getAuthorizationToken(flag, Str_pincode, orderNo);
                            }
                        } else {
                            globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PEAuthResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPETests(final int flag, String pincode, final String orderNo) {

        try {
//            if (!EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD)).equalsIgnoreCase(EncryptionUtils.Dcrp_Hex(activity.getString(R.string.BASE_URL_TOCHECK)))) {
//                //TODO Done for staging.
//                Str_pincode = "400072";
//                System.out.println("Mith>>>>>>>>>>>>>>" + appPreferenceManager.getAuthToken());
//                Str_auth_token = appPreferenceManager.getAuthToken();
//            } else {
            Str_pincode = pincode;
            Str_auth_token = appPreferenceManager.getAuthToken();
//            }
            /*Str_pincode = "666666";
            Str_auth_token = appPreferenceManager.getAuthToken();
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            //TODO testing purpose
            String URl = "E291CF4F82A03696E0F7634E83DA563B08B4275B7F4EB30316E3897625725DA98B22B69FA38BC6D26EF397806A32B6F4BA5572A314BCCEDA71EF14BE2CEBF5B3966E11B1799939B881DAC9A27040A534";*/

            //GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.PE_API))).create(GetAPIInterface.class);
            GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.PE_API))).create(GetAPIInterface.class);
            Call<GetPETestResponseModel> getPETestResponseModelCall = getAPIInterface.getPETests(Constants.content_type, Constants.XSource, Str_auth_token, Str_pincode, orderNo);
            getPETestResponseModelCall.enqueue(new Callback<GetPETestResponseModel>() {
                @Override
                public void onResponse(Call<GetPETestResponseModel> call, retrofit2.Response<GetPETestResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            GetPETestResponseModel getPETestResponseModel = response.body();
                            if (getPETestResponseModel.getStatus() == 1) {
                                if (flag == 0) {
                                    startAndArriveActivity.getTestList(getPETestResponseModel);
                                } else if (flag == 1) {
                                    addEditBenificaryActivity.getTestList(getPETestResponseModel);
                                } else if (flag == 2) {
                                    addRemoveTestProfileActivity.getTestList(getPETestResponseModel.getData());
                                } else {
                                    getAuthorizationToken(flag, Str_pincode, orderNo);
                                }
                            } else {
                                getAuthorizationToken(flag, Str_pincode, orderNo);
                            }
                        } else if (response.code() == 400) {
                            System.out.println("Mith>>>>>>>>" + response.message());
                            getAuthorizationToken(flag, Str_pincode, orderNo);
                        } else {
                            //getAuthorizationToken(flag,appPreferenceManager.getLoginResponseModel());
                            globalClass.showCustomToast(activity, InputUtils.isNull(response.message()) ? response.message() : ConstantsMessages.SOMETHING_WENT_WRONG);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetPETestResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendEventArrived(final SendEventRequestModel sendEventRequestModel) {

        try {
//
            PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<SendEventResponeModel> sendEventResponeModelCall = postAPIInterface.sendEventArrived(sendEventRequestModel);
            sendEventResponeModelCall.enqueue(new Callback<SendEventResponeModel>() {
                @Override
                public void onResponse(Call<SendEventResponeModel> call, retrofit2.Response<SendEventResponeModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            SendEventResponeModel sendEventResponeModel = response.body();
                            if (InputUtils.CheckEqualIgnoreCase(sendEventResponeModel.getResponse(),"SUCCESS")){


                            }else{

                            }

                        }else {
                            globalClass.showCustomToast(activity, InputUtils.isNull(response.message()) ? response.message() : ConstantsMessages.SOMETHING_WENT_WRONG);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SendEventResponeModel> call, Throwable t) {
//                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
