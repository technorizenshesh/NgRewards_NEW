package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by technorizen on 30/1/19.
 */

public class Year {
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("total")
    @Expose
    private double total;
    @SerializedName("year_name")
    @Expose
    private String yearName;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getYearName() {
        return yearName;
    }

    public void setYearName(String yearName) {
        this.yearName = yearName;
    }
}
