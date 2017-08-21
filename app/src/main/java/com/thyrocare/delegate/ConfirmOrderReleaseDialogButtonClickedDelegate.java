package com.thyrocare.delegate;

import com.thyrocare.models.data.OrderVisitDetailsModel;

/**
 * Created by Orion on 4/28/2017.
 */
public interface ConfirmOrderReleaseDialogButtonClickedDelegate {
    void onOkButtonClicked(OrderVisitDetailsModel orderVisitDetailsModel, String remark);
    void onCancelButtonClicked();
}
