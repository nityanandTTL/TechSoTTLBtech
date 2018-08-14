package com.thyrocare.Controller;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.R;
import com.thyrocare.activity.LMEMapDisplayFragmentActivity;
import com.thyrocare.fragment.LME.LMEMasterBarcodeScanFragment;
import com.thyrocare.fragment.LME.LME_OrdersDisplayFragment;
import com.thyrocare.fragment.LME.LME_WLMISFragment;
import com.thyrocare.models.api.request.SendScannedbarcodeLME;
import com.thyrocare.models.data.SampleDropDetailsbyTSPLMEDetailsModel;
import com.thyrocare.models.data.ScannedMasterBarcodebyLMEPOSTDATAModel;
import com.thyrocare.models.data.WLMISDetailsModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;

import org.json.JSONException;

import java.util.ArrayList;

import static com.thyrocare.utils.api.NetworkUtils.isNetworkAvailable;

/**
 * Created by E5233 on 4/30/2018.
 */

public class TSPLMESampleDropController {

    LMEMapDisplayFragmentActivity mLMEMapDisplayFragmentActivity;
    Context mContext;
    LME_OrdersDisplayFragment mLME_OrdersDisplayFragment;
    LME_WLMISFragment mLME_WLMISFragment;
    int flag = 0;

    public TSPLMESampleDropController(Context activity, LME_OrdersDisplayFragment fragment) {
        this.mContext = activity;
        this.mLME_OrdersDisplayFragment = fragment;
        flag = 1;
    }

    public TSPLMESampleDropController(Context activity, LME_WLMISFragment fragment) {
        this.mContext = activity;
        this.mLME_WLMISFragment = fragment;
    }

    public TSPLMESampleDropController(FragmentActivity activity, LMEMapDisplayFragmentActivity pLMEMapDisplayFragmentActivity) {
        this.mContext = activity;
        this.mLMEMapDisplayFragmentActivity = pLMEMapDisplayFragmentActivity;
        flag = 3;
    }

    public void CallGetSampleDropDetailsbyTSPLME(String Id, int batch_f) {

        ApiCallAsyncTask sampleDropDetailsbyTSPLME = new AsyncTaskForRequest(mContext).getSampleDropDetailsbyTSPLME(Id, batch_f);
        sampleDropDetailsbyTSPLME.setApiCallAsyncTaskDelegate(new SampleDropDetailsbyTSPLMEAsyncTaskDelegateResult());
        if (isNetworkAvailable(mContext)) {
            sampleDropDetailsbyTSPLME.execute(sampleDropDetailsbyTSPLME);
        } else {
            Toast.makeText(mContext, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    public void CallPostScannedMasterBarcodebyLME(SendScannedbarcodeLME[] n) {

        ApiCallAsyncTask logoutDeviceAsyncTask = new AsyncTaskForRequest(mContext).getPostScannedMasterBarcodebyLMEAsyncTask(n);
        logoutDeviceAsyncTask.setApiCallAsyncTaskDelegate(new PostScannedMasterBarcodebyLMEAsyncTaskDelegateResult());
        if (isNetworkAvailable(mContext)) {
            logoutDeviceAsyncTask.execute(logoutDeviceAsyncTask);
        } else {
            Toast.makeText(mContext, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class SampleDropDetailsbyTSPLMEAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {

                ResponseParser responseParser = new ResponseParser(mContext);
                ArrayList<SampleDropDetailsbyTSPLMEDetailsModel> materialDetailsModels = new ArrayList<>();
                materialDetailsModels = responseParser.getSampleDropDetailsbyTSPLMEResponseModel(json, statusCode);
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

            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private class PostScannedMasterBarcodebyLMEAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                TastyToast.makeText(mContext, "" + json, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                if (flag == 1) {
                    mLME_OrdersDisplayFragment.StartButtonClickedSuccess();
                } else if (flag == 3) {
                    mLMEMapDisplayFragmentActivity.EndButtonClickedSuccess();
                }
            } else {

            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    public void CallGetWLMIS(String Id) {

        ApiCallAsyncTask sampleDropDetailsbyTSPLME = new AsyncTaskForRequest(mContext).getWLMIS(Id);
        sampleDropDetailsbyTSPLME.setApiCallAsyncTaskDelegate(new WLMISAsyncTaskDelegateResult());
        if (isNetworkAvailable(mContext)) {
            sampleDropDetailsbyTSPLME.execute(sampleDropDetailsbyTSPLME);
        } else {
            Toast.makeText(mContext, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class WLMISAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {

                ResponseParser responseParser = new ResponseParser(mContext);
                ArrayList<WLMISDetailsModel> materialDetailsModels = new ArrayList<>();
                materialDetailsModels = responseParser.getWLMISResponseModel(json, statusCode);
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

                } finally {
                    if (materialDetailsModels != null) {
                        materialDetailsModels = null;
                    }
                }
            } else {

            }
        }

        @Override
        public void onApiCancelled() {

        }
    }
}