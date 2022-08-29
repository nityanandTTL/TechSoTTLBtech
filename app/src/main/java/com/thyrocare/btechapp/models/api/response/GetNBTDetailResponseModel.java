package com.thyrocare.btechapp.models.api.response;

import java.util.ArrayList;
import java.util.List;

public class GetNBTDetailResponseModel {


    /**
     * Response : Success
     * ResponseId : RES00001
     * NBTDetailList : [{"Code":"884543843","Name":"MR RANGANATHAN K","Email":"ranganathan1010@gmail.com","Mobile":"7022621979"},{"Code":"884547052","Name":"Mr. Kirankumar K","Email":"gomathiuthamaraj@gmail.com","Mobile":"9902625918"}]
     */

    private String Response;
    private String ResponseId;
    private ArrayList<NBTDetailListDTO> NBTDetailList;

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

    public ArrayList<NBTDetailListDTO> getNBTDetailList() {
        return NBTDetailList;
    }

    public void setNBTDetailList(ArrayList<NBTDetailListDTO> NBTDetailList) {
        this.NBTDetailList = NBTDetailList;
    }

    public static class NBTDetailListDTO {
        /**
         * Code : 884543843
         * Name : MR RANGANATHAN K
         * Email : ranganathan1010@gmail.com
         * Mobile : 7022621979
         */

        private String Code;
        private String Name;
        private String Email;
        private String Mobile;

        public String getCode() {
            return Code;
        }

        public void setCode(String Code) {
            this.Code = Code;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        @Override
        public String toString() {
            return Name;
        }
    }
}
