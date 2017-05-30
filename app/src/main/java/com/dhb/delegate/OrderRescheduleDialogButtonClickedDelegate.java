package com.dhb.delegate;

import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;

/**
 * Created by ISRO on 4/28/2017.
 */
public interface OrderRescheduleDialogButtonClickedDelegate {
    void onOkButtonClicked(OrderDetailsModel orderVisitDetailsModel, String remark,String date);
    void onCancelButtonClicked();
}
