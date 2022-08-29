package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import java.util.List;

public class MainMaterialModel {
    /**
     * Code : UTT91
     * MaterialDetails : [{"MaterialId":"10015","MaterialName":"BARCODED EDTA VIALS","OpeningStock":"50","UsedStock":"0"},{"MaterialId":"10044","MaterialName":"URINE CONTAINER","OpeningStock":"18","UsedStock":"0"},{"MaterialId":"11231","MaterialName":"KIT - BS","OpeningStock":"50","UsedStock":"0"},{"MaterialId":"11245","MaterialName":"SAMPLE POUCH - 50 SAMPLES","OpeningStock":"18","UsedStock":"0"},{"MaterialId":"11251","MaterialName":"BARCODED GEL TUBE - (5ML)","OpeningStock":"18","UsedStock":"0"},{"MaterialId":"11422","MaterialName":"BARCODED FLUORIDE VIALS","OpeningStock":"50","UsedStock":"0"},{"MaterialId":"11589","MaterialName":"HAND SANITIZER","OpeningStock":"50","UsedStock":"0"}]
     * Response : SUCCESS
     * ResponseId : RES0000
     */

    private String Code;
    private String Response;
    private String ResponseId;
    private List<MaterialDetailsBean> MaterialDetails;

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public String getResponseId() {
        return ResponseId;
    }

    public void setResponseId(String ResponseId) {
        this.ResponseId = ResponseId;
    }

    public List<MaterialDetailsBean> getMaterialDetails() {
        return MaterialDetails;
    }

    public void setMaterialDetails(List<MaterialDetailsBean> MaterialDetails) {
        this.MaterialDetails = MaterialDetails;
    }

    public static class MaterialDetailsBean {
        /**
         * MaterialId : 10015
         * MaterialName : BARCODED EDTA VIALS
         * OpeningStock : 50
         * UsedStock : 0
         */

        private String MaterialId;
        private String MaterialName;
        private String OpeningStock;
        private String UsedStock;
        private String ExpectedStock;

        public String getExpectedStock() {
            return ExpectedStock;
        }

        public void setExpectedStock(String expectedStock) {
            ExpectedStock = expectedStock;
        }

        public String getMaterialId() {
            return MaterialId;
        }

        public void setMaterialId(String MaterialId) {
            this.MaterialId = MaterialId;
        }

        public String getMaterialName() {
            return MaterialName;
        }

        public void setMaterialName(String MaterialName) {
            this.MaterialName = MaterialName;
        }

        public String getOpeningStock() {
            return OpeningStock;
        }

        public void setOpeningStock(String OpeningStock) {
            this.OpeningStock = OpeningStock;
        }

        public String getUsedStock() {
            return UsedStock;
        }

        public void setUsedStock(String UsedStock) {
            this.UsedStock = UsedStock;
        }
    }
}
