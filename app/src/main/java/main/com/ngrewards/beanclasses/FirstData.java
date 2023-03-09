package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by technorizen on 11/1/19.
 */

public class FirstData {

    @SerializedName("week")
    @Expose
    private String week;
    @SerializedName("availabel_week_withdraw_status")
    @Expose
    private String availabel_week_withdraw_status;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("data")
    @Expose
    private List<CommissionData> data = null;
    @SerializedName("week_comission")
    @Expose
    private String weekComission;
    @SerializedName("withdraw_status")
    @Expose
    private String withdraw_status;

    public String getWithdraw_status() {
        return withdraw_status;
    }

    public void setWithdraw_status(String withdraw_status) {
        this.withdraw_status = withdraw_status;
    }

    public String getAvailabel_week_withdraw_status() {
        return availabel_week_withdraw_status;
    }

    public void setAvailabel_week_withdraw_status(String availabel_week_withdraw_status) {
        this.availabel_week_withdraw_status = availabel_week_withdraw_status;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<CommissionData> getData() {
        return data;
    }

    public void setData(List<CommissionData> data) {
        this.data = data;
    }

    public String getWeekComission() {
        return weekComission;
    }

    public void setWeekComission(String weekComission) {
        this.weekComission = weekComission;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
