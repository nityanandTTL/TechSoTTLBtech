package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import java.io.Serializable;

public class NewBtechAvaliabilityResponseModel implements Serializable {

    private Numberofdaysdatamodel NumberOfDays;

    public NewBtechAvaliabilityResponseModel() {
    }


    public Numberofdaysdatamodel getNumberOfDays() {
        return NumberOfDays;
    }

    public void setNumberOfDays(Numberofdaysdatamodel numberOfDays) {
        NumberOfDays = numberOfDays;
    }

    public class Numberofdaysdatamodel implements Serializable {

        private int Day1;
        private int Day2;
        private int Day3;
        private int Day4;

        private Numberofdaysdatamodel() {
            //Prevent default constructor
        }

        public int getDay1() {
            return Day1;
        }

        public void setDay1(int day1) {
            Day1 = day1;
        }

        public int getDay2() {
            return Day2;
        }

        public void setDay2(int day2) {
            Day2 = day2;
        }

        public int getDay3() {
            return Day3;
        }

        public void setDay3(int day3) {
            Day3 = day3;
        }

        public int getDay4() {
            return Day4;
        }

        public void setDay4(int day4) {
            Day4 = day4;
        }
    }
}
