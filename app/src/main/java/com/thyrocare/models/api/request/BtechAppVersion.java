package com.thyrocare.models.api.request;

/**
 * Created by e5209@thyrocare.com on 2/1/18.
 */

public class BtechAppVersion {
    int BtechId,Version;

    public BtechAppVersion() {
    }

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public int getVersion() {
        return Version;
    }

    public void setVersion(int version) {
        Version = version;
    }
}
