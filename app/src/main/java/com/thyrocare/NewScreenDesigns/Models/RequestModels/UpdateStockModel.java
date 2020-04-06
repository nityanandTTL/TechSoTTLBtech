package com.thyrocare.NewScreenDesigns.Models.RequestModels;

public class UpdateStockModel {

    /**
     * APIKey : 68rbZ40eNeRephUzIDTos9SsQIm4mHlT3lCNnqI)Ivk=
     * Dac_code : A7217
     * Material_id : 11230
     * Used_stock : 10
     * Wastage_stock : 5
     * Defective_stock : 0
     * Closing_stock : 73
     */

    private String APIKey;
    private String Dac_code;
    private String Material_id;
    private String Used_stock;
    private String Wastage_stock;
    private String Defective_stock;
    private String Closing_stock;

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }

    public String getDac_code() {
        return Dac_code;
    }

    public void setDac_code(String Dac_code) {
        this.Dac_code = Dac_code;
    }

    public String getMaterial_id() {
        return Material_id;
    }

    public void setMaterial_id(String Material_id) {
        this.Material_id = Material_id;
    }

    public String getUsed_stock() {
        return Used_stock;
    }

    public void setUsed_stock(String Used_stock) {
        this.Used_stock = Used_stock;
    }

    public String getWastage_stock() {
        return Wastage_stock;
    }

    public void setWastage_stock(String Wastage_stock) {
        this.Wastage_stock = Wastage_stock;
    }

    public String getDefective_stock() {
        return Defective_stock;
    }

    public void setDefective_stock(String Defective_stock) {
        this.Defective_stock = Defective_stock;
    }

    public String getClosing_stock() {
        return Closing_stock;
    }

    public void setClosing_stock(String Closing_stock) {
        this.Closing_stock = Closing_stock;
    }
}
