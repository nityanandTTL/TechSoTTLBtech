package com.thyrocare.delegate;

import com.thyrocare.models.data.OrderDetailsModel;

/**
 * Created by Orion on 4/28/2017.
 */
public interface OrderRescheduleDialogButtonClickedDelegate {
    void onOkButtonClicked(OrderDetailsModel orderVisitDetailsModel, String remark,String date);
    void onCancelButtonClicked();
}
