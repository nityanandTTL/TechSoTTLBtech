package com.thyrocare.delegate;

import android.view.View;

import com.thyrocare.models.data.OrderVisitDetailsModel;

/**
 * Created by Orion on 4/27/2017.
 */
public interface CallbackforShowCaseDelegate {
    void onFirstPosition(View view, boolean isAccepted);
    void onAcceptOrderFirstPosition(View view);

}
