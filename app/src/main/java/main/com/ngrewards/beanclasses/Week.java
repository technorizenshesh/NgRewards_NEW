package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by technorizen on 30/1/19.
 */

public class Week {

    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("total")
    @Expose
    private float total;
    @SerializedName("day_name")
    @Expose
    private String dayName;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }
}
