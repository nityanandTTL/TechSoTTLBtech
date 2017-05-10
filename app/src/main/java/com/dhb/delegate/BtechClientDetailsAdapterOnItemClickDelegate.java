package com.dhb.delegate;

import com.dhb.models.data.BtechClientsModel;

/**
 * Created by vendor1 on 5/8/2017.
 */

public interface BtechClientDetailsAdapterOnItemClickDelegate {
    void onItemClick(BtechClientsModel btechClientsModel);
    void onItemCallClick(BtechClientsModel btechClientsModel,int position);
}
