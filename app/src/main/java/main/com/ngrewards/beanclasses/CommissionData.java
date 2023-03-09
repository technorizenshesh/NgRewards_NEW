package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by technorizen on 11/1/19.
 */

public class CommissionData {

    @SerializedName("week_start")
    @Expose
    private String weekStart;
    @SerializedName("withdraw_status")
    @Expose
    private String withdraw_status;
    @SerializedName("week_end")
    @Expose
    private String weekEnd;
    @SerializedName("total_week")
    @Expose
    private String totalWeek;
    @SerializedName("marchant_id")
    @Expose
    private String marchantId;
    @SerializedName("total_cost")
    @Expose
    private String totalCost;
    @SerializedName("reward_percent")
    @Expose
    private String rewardPercent;
    @SerializedName("merchant_detail")
    @Expose
    private MerchantBean merchantDetail;
    @SerializedName("merchant_comision")
    @Expose
    private String merchantComision;

    public String getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(String weekStart) {
        this.weekStart = weekStart;
    }

    public String getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(String weekEnd) {
        this.weekEnd = weekEnd;
    }

    public String getTotalWeek() {
        return totalWeek;
    }

    public void setTotalWeek(String totalWeek) {
        this.totalWeek = totalWeek;
    }

    public String getMarchantId() {
        return marchantId;
    }

    public void setMarchantId(String marchantId) {
        this.marchantId = marchantId;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getRewardPercent() {
        return rewardPercent;
    }

    public void setRewardPercent(String rewardPercent) {
        this.rewardPercent = rewardPercent;
    }

    public MerchantBean getMerchantDetail() {
        return merchantDetail;
    }

    public void setMerchantDetail(MerchantBean merchantDetail) {
        this.merchantDetail = merchantDetail;
    }

    public String getMerchantComision() {
        return merchantComision;
    }

    public void setMerchantComision(String merchantComision) {
        this.merchantComision = merchantComision;
    }

    public String getWithdraw_status() {
        return withdraw_status;
    }

    public void setWithdraw_status(String withdraw_status) {
        this.withdraw_status = withdraw_status;
    }
}
