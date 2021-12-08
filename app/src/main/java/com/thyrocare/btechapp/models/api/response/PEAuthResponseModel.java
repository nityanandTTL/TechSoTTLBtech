package com.thyrocare.btechapp.models.api.response;

import com.google.gson.annotations.SerializedName;

public class PEAuthResponseModel {


    /**
     * status : true
     * error : null
     * data : {"auth-token":"UEhBUk1FQVNZVEhZQkFTRYn+jTgPVbGSyIGobBeDMtvGQQMJquNzy2AQ9xj/xy26"}
     */

    private Boolean status;
    private Object error;
    private DataDTO data;

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        /**
         * auth-token : UEhBUk1FQVNZVEhZQkFTRYn+jTgPVbGSyIGobBeDMtvGQQMJquNzy2AQ9xj/xy26
         */

        @SerializedName("auth-token")
        private String authtoken;

        public String getAuthtoken() {
            return authtoken;
        }

        public void setAuthtoken(String authtoken) {
            this.authtoken = authtoken;
        }
    }
}
