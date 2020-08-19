package com.thyrocare.btechapp.models.api.response;

import java.io.Serializable;
import java.util.ArrayList;

public class VideoLangaugesResponseModel implements Serializable {

    ArrayList<Outputlang> Output;
    String resId;
    String response;

    public ArrayList<Outputlang> getOutput() {
        return Output;
    }

    public void setOutput(ArrayList<Outputlang> output) {
        Output = output;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public static class Outputlang {

        String IID_NEW;
        String LANGUAGE;

        public String getIID_NEW() {
            return IID_NEW;
        }

        public void setIID_NEW(String IID_NEW) {
            this.IID_NEW = IID_NEW;
        }

        public String getLANGUAGE() {
            return LANGUAGE;
        }

        public void setLANGUAGE(String LANGUAGE) {
            this.LANGUAGE = LANGUAGE;
        }


        @Override
        public String toString() {
            return LANGUAGE;
        }
    }
}
