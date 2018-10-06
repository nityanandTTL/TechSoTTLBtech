package com.thyrocare.delegate;

import com.thyrocare.models.data.OrderDetailsModel;

/**
 * Created by Orion on 4/28/2017.
 */
public interface OrderCancelDialogButtonClickedDelegate {
    void onOkButtonClicked(OrderDetailsModel orderVisitDetailsModel, String remark, int status);
    void onCancelButtonClicked();
}
