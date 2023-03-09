package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by technorizen on 19/12/18.
 */

public class CityLoc {
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("city_percent")
    @Expose
    private String cityPercent;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCityPercent() {
        return cityPercent;
    }

    public void setCityPercent(String cityPercent) {
        this.cityPercent = cityPercent;
    }
}
