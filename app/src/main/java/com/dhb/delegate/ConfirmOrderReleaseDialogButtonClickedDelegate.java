package com.dhb.delegate;

import com.dhb.models.data.OrderVisitDetailsModel;

/**
 * Created by Orion on 4/28/2017.
 */
public interface ConfirmOrderReleaseDialogButtonClickedDelegate {
    void onOkButtonClicked(OrderVisitDetailsModel orderVisitDetailsModel, String remark);
    void onCancelButtonClicked();
}
