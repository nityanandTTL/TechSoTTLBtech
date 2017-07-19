package com.dhb.models.api.request;

import java.util.Date;

/**
 * Created by Orion on 4/26/2017.
 */

public class LedgerRequestModel {

    Integer BTechId;
    Date FromDate;
    Date ToDate;

    public Integer getBTechId() {
        return BTechId;
    }

    public void setBTechId(Integer BTechId) {
        this.BTechId = BTechId;
    }

    public Date getFromDate() {
        return FromDate;
    }

    public void setFromDate(Date fromDate) {
        FromDate = fromDate;
    }

    public Date getToDate() {
        return ToDate;
    }

    public void setToDate(Date toDate) {
        ToDate = toDate;
    }


}
