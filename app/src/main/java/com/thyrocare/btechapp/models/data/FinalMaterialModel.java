package com.thyrocare.btechapp.models.data;

import android.os.Parcelable;

/**
 * Created by Orion on 5/4/2017.
 */

public class FinalMaterialModel extends  BaseModel implements Parcelable{


    private MaterialDetailsModel materialDetailsModel;
    private BTMaterialsModel btMaterialsModel;

    public MaterialDetailsModel getMaterialDetailsModel() {
        return materialDetailsModel;
    }

    public void setMaterialDetailsModel(MaterialDetailsModel materialDetailsModel) {
        this.materialDetailsModel = materialDetailsModel;
    }

    public BTMaterialsModel getBtMaterialsModel() {
        return btMaterialsModel;
    }

    public void setBtMaterialsModel(BTMaterialsModel btMaterialsModel) {
        this.btMaterialsModel = btMaterialsModel;
    }
}
