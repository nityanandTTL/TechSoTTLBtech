package com.thyrocare.models.api.response;

/**
 * Created by Orion on 5/2/2017.
 */

public class MaterialResponseModel {


    public Integer getMaterialId() {
        return MaterialId;
    }

    public void setMaterialId(Integer materialId) {
        MaterialId = materialId;
    }

    public String getMaterialName() {
        return MaterialName;
    }

    public void setMaterialName(String materialName) {
        MaterialName = materialName;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getUnitCost() {
        return UnitCost;
    }

    public void setUnitCost(String unitCost) {
        UnitCost = unitCost;
    }

    public String getUnitSize() {
        return UnitSize;
    }

    public void setUnitSize(String unitSize) {
        UnitSize = unitSize;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    Integer MaterialId;
    String MaterialName;
    String ShortName;
    String UnitCost;
    String UnitSize;
    String Tax;
}
