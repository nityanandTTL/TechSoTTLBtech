package com.dhb.models.api.request;

import java.util.Date;

/**
 * Created by E4904 on 4/26/2017.
 */

public class LedgerRequestModel {

    Integer id;
    Date FromDate;
    Date ToDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
