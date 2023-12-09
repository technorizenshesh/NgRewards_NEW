package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by technorizen on 30/1/19.
 */

public class SalesBeanList {
    @SerializedName("week")
    @Expose
    private List<Week> week = null;
    @SerializedName("month")
    @Expose
    private List<Month> month = null;
    @SerializedName("year")
    @Expose
    private List<Year> year = null;

    public List<Week> getWeek() {
        return week;
    }

    public void setWeek(List<Week> week) {
        this.week = week;
    }

    public List<Month> getMonth() {
        return month;
    }

    public void setMonth(List<Month> month) {
        this.month = month;
    }

    public List<Year> getYear() {
        return year;
    }

    public void setYear(List<Year> year) {
        this.year = year;
    }
}
