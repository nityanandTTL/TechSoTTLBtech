package com.thyrocare.btechapp.models.api.response;

import androidx.annotation.NonNull;

import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;

public class GetCollectionRespModel {


    /**
     * centerList : [{"centerName":"REENA KUMBHAR","city":"MUMBAI","location":"MAHARASHTRA","address":"BLD NO 252 B WING ROOM NUMBER 9853 KANNAMWAR NAGAR 1 VIKHROLI EAST 400083","missNo":"9702466333","whatsNo":"9870666333","latitude":"","longitude":"","time":9}]
     * respId : RES00001
     * response : Success
     * flag : true
     */

    private String respId;
    private String response;
    private Boolean flag;
    private ArrayList<CenterListDTO> centerList;

    public String getRespId() {
        return respId;
    }

    public void setRespId(String respId) {
        this.respId = respId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Boolean isFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public ArrayList<CenterListDTO> getCenterList() {
        return centerList;
    }

    public void setCenterList(ArrayList<CenterListDTO> centerList) {
        this.centerList = centerList;
    }

    public static class CenterListDTO {
        /**
         * centerName : REENA KUMBHAR
         * city : MUMBAI
         * location : MAHARASHTRA
         * address : BLD NO 252 B WING ROOM NUMBER 9853 KANNAMWAR NAGAR 1 VIKHROLI EAST 400083
         * missNo : 9702466333
         * whatsNo : 9870666333
         * latitude :
         * longitude :
         * time : 9
         */

        private String centerName;
        private String city;
        private String location;
        private String address;
        private String missNo;
        private String whatsNo;
        private String latitude;
        private String longitude;
        private String time;
        private String mobile;
        private String available;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAvailable() {
            return available;
        }

        public void setAvailable(String available) {
            this.available = available;
        }

        public String getCenterName() {
            return centerName;
        }

        public void setCenterName(String centerName) {
            this.centerName = centerName;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMissNo() {
            return missNo;
        }

        public void setMissNo(String missNo) {
            this.missNo = missNo;
        }

        public String getWhatsNo() {
            return whatsNo;
        }

        public void setWhatsNo(String whatsNo) {
            this.whatsNo = whatsNo;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @Override
        public String toString() {
            String center = "";
            if (!InputUtils.isNull(centerName)) {
                center = centerName;
            } else {
                center = city;
            }
            return center;
        }
    }
}
