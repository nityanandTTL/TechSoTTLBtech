package com.thyrocare.btechapp.delegate;

import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;

/**
 * Created by Orion on 4/28/2017.
 */
public interface ConfirmOrderReleaseDialogButtonClickedDelegate {
    void onOkButtonClicked(OrderVisitDetailsModel orderVisitDetailsModel, String remark);
    void onCancelButtonClicked();
}
