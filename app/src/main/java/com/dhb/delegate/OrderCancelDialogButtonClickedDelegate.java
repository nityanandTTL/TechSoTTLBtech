package com.dhb.delegate;

import com.dhb.models.data.OrderDetailsModel;

/**
 * Created by ISRO on 4/28/2017.
 */
public interface OrderCancelDialogButtonClickedDelegate {
    void onOkButtonClicked(OrderDetailsModel orderVisitDetailsModel, String remark);
    void onCancelButtonClicked();
}
