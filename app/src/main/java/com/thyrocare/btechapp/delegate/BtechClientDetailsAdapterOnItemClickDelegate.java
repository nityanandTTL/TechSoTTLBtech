package com.thyrocare.btechapp.delegate;

import com.thyrocare.btechapp.models.data.BtechClientsModel;

/**
 * Created by Orion on 5/8/2017.
 */

public interface BtechClientDetailsAdapterOnItemClickDelegate {
    void onItemClick(BtechClientsModel btechClientsModel);

    void onItemCallClick(BtechClientsModel btechClientsModel, int position);
}
