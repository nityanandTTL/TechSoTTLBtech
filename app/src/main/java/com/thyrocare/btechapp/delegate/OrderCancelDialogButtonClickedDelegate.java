package com.thyrocare.btechapp.delegate;

import com.thyrocare.btechapp.models.data.OrderDetailsModel;

/**
 * Created by Orion on 4/28/2017.
 */
public interface OrderCancelDialogButtonClickedDelegate {
    void onOkButtonClicked(OrderDetailsModel orderVisitDetailsModel, String remark, int status);
    void onCancelButtonClicked();
}
