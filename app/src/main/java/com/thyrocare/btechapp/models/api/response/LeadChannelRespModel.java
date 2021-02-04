package com.thyrocare.btechapp.models.api.response;

import java.util.ArrayList;

public class LeadChannelRespModel {


    /**
     * channelList : [{"data":"SMS"},{"data":"Mail"},{"data":"WhatsApp"},{"data":"Call"}]
     * fromList : [{"data":"SGC"},{"data":"PGC"},{"data":"TSP"},{"data":"OLC"},{"data":"Diggy"},{"data":"Public"}]
     * respId : RES00001
     * response : Success
     */

    private String respId;
    private String response;
    private ArrayList<ChannelListDTO> channelList;
    private ArrayList<FromListDTO> fromList;

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

    public ArrayList<ChannelListDTO> getChannelList() {
        return channelList;
    }

    public void setChannelList(ArrayList<ChannelListDTO> channelList) {
        this.channelList = channelList;
    }

    public ArrayList<FromListDTO> getFromList() {
        return fromList;
    }

    public void setFromList(ArrayList<FromListDTO> fromList) {
        this.fromList = fromList;
    }

    public static class ChannelListDTO {
        /**
         * data : SMS
         */

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static class FromListDTO {
        /**
         * data : SGC
         */

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
