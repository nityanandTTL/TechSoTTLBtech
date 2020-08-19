package com.thyrocare.btechapp.models.data;

/**
 * Created by e5233@thyrocare.com on 14/8/18.
 */

public class LocationMasterModel {

    private String LocationType;
    private String Id;

    public String getLocationType() {
        return LocationType;
    }

    public void setLocationType(String locationType) {
        LocationType = locationType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
