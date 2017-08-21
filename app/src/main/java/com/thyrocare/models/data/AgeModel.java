package com.thyrocare.models.data;

/**
 * Created by Orion on 24/8/15.
 */
public class AgeModel {

    private int days;
    private int months;
    private int years;

    private AgeModel()
    {
        //Prevent default constructor
    }

    public AgeModel(int days, int months, int years)
    {
        this.days = days;
        this.months = months;
        this.years = years;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }
    @Override
    public String toString()
    {
        return years + "Y | " + months + "M | " + days + "D";
    }

}
