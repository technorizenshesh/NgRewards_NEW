package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by technorizen on 21/7/18.
 */

public class MemberBean {
    @SerializedName("result")
    @Expose
    private List<MemberDetail> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("total_friends")
    @Expose
    private String totalfriends;
    @SerializedName("total_spent_this_week")
    @Expose
    private String total_spent_this_week;
    @SerializedName("total_ng_cash_earned")
    @Expose
    private String total_ng_cash_earned;
    @SerializedName("total_earning_this_week")
    @Expose
    private String total_earning_this_week;

    public String getTotal_earning_this_week() {
        return total_earning_this_week;
    }

    public void setTotal_earning_this_week(String total_earning_this_week) {
        this.total_earning_this_week = total_earning_this_week;
    }

    public List<MemberDetail> getResult() {
        return result;
    }

    public void setResult(List<MemberDetail> result) {
        this.result = result;
    }

    public String getTotalfriends() {
        return totalfriends;
    }

    public void setTotalfriends(String totalfriends) {
        this.totalfriends = totalfriends;
    }

    public String getTotal_spent_this_week() {
        return total_spent_this_week;
    }

    public void setTotal_spent_this_week(String total_spent_this_week) {
        this.total_spent_this_week = total_spent_this_week;
    }

    public String getTotal_ng_cash_earned() {
        return total_ng_cash_earned;
    }

    public void setTotal_ng_cash_earned(String total_ng_cash_earned) {
        this.total_ng_cash_earned = total_ng_cash_earned;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
