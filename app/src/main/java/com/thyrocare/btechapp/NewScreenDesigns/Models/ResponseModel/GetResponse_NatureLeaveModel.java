package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;


import androidx.annotation.NonNull;

public class GetResponse_NatureLeaveModel {


    /**
     * Id : 1
     * Nature : UNINFORMED
     */

    private int Id;
    private String Nature;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getNature() {
        return Nature;
    }

    public void setNature(String Nature) {
        this.Nature = Nature;
    }

    @NonNull
    @Override
    public String toString() {
        return this.Nature;
    }
}
