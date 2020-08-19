package com.thyrocare.btechapp.utils.app;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.HurlStack;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyHurlStack  extends HurlStack {

    private Context mContext;

    public MyHurlStack(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders) {

        Map<String, String> headers = new HashMap<>(additionalHeaders);
        headers.put(Constants.HEADER_USER_AGENT, Global.getHeaderValue(mContext));

        try {
            return super.executeRequest(request, headers);
        } catch (IOException e) {

        } catch (AuthFailureError authFailureError) {
        }

        return null;
    }
}
