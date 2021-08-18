package com.thyrocare.btechapp.models.api.response;

import com.thyrocare.btechapp.models.data.Numberofdaysdatamodel;

import java.util.ArrayList;

/**
 * Created by Orion on 6/22/2017.
 */

public class DynamicBtechAvaliabilityResponseModel {
    private Numberofdaysdatamodel NumberOfDays;
    private ArrayList<AllDaysAvalibilty> allDays;

    public DynamicBtechAvaliabilityResponseModel() {
    }

    public ArrayList<AllDaysAvalibilty> getAllDays() {
        return allDays;
    }

    public void setAllDays(ArrayList<AllDaysAvalibilty> allDays) {
        this.allDays = allDays;
    }

    public Numberofdaysdatamodel getNumberOfDays() {
        return NumberOfDays;
    }

    public void setNumberOfDays(Numberofdaysdatamodel numberOfDays) {
        NumberOfDays = numberOfDays;
    }

    public class AllDaysAvalibilty {
        private int DayCount;
        private int day;

        public int getDayCount() {
            return DayCount;
        }

        public void setDayCount(int dayCount) {
            DayCount = dayCount;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }
}
