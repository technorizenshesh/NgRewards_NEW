package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by technorizen on 3/8/18.
 */

public class HourBean {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("day_name")
    @Expose
    private String dayName;
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
    @SerializedName("opening_time")
    @Expose
    private String openingTime;
    @SerializedName("closing_time")
    @Expose
    private String closingTime;
    @SerializedName("opening_status")
    @Expose
    private String opening_status;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("fullday_open_status")
    @Expose
    private String fullday_open_status;

    public String getOpening_status() {
        return opening_status;
    }

    public void setOpening_status(String opening_status) {
        this.opening_status = opening_status;
    }

    public String getFullday_open_status() {
        return fullday_open_status;
    }

    public void setFullday_open_status(String fullday_open_status) {
        this.fullday_open_status = fullday_open_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
