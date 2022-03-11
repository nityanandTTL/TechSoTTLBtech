package com.thyrocare.btechapp.models.api.response;

import java.util.ArrayList;
import java.util.List;

public class GetOrderDetailsResponseModel {

    /**
     * GetVisitcount : [{"VisitId":"VLDDE005"},{"VisitId":"VLE1A84D"},{"VisitId":"VLE7B1B2"},{"VisitId":"VLF036ED"},{"VisitId":"VL77B074"},{"VisitId":"VL96C4D7"},{"VisitId":"VLAD6660"},{"VisitId":"VL58E531"},{"VisitId":"VLC5657B"}]
     * Response : SUCCESS
     */

    private String Response;
    private String CurrentDatetime;
    private ArrayList<GetVisitcountDTO> GetVisitcount;

    public String getCurrentDatetime() {
        return CurrentDatetime;
    }

    public void setCurrentDatetime(String currentDatetime) {
        CurrentDatetime = currentDatetime;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public ArrayList<GetVisitcountDTO> getGetVisitcount() {
        return GetVisitcount;
    }

    public void setGetVisitcount(ArrayList<GetVisitcountDTO> GetVisitcount) {
        this.GetVisitcount = GetVisitcount;
    }

    public static class GetVisitcountDTO {
        /**
         * VisitId : VLDDE005
         */

        private String VisitId;
        private String Name;
        private String AppointmentDate;
        private String Status;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getAppointmentDate() {
            return AppointmentDate;
        }

        public void setAppointmentDate(String appointmentDate) {
            AppointmentDate = appointmentDate;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getVisitId() {
            return VisitId;
        }

        public void setVisitId(String VisitId) {
            this.VisitId = VisitId;
        }
    }
}
