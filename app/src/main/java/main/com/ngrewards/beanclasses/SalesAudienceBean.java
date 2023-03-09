package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by technorizen on 12/12/18.
 */

public class SalesAudienceBean {

    @SerializedName("result")
    @Expose
    private List<DetailList> result = null;
    @SerializedName("male_percent")
    @Expose
    private double malePercent;
    @SerializedName("female_percent")
    @Expose
    private double femalePercent;
    @SerializedName("total_age_range1_percent_all")
    @Expose
    private double totalAgeRange1PercentAll;
    @SerializedName("total_age_range2_percent_all")
    @Expose
    private double totalAgeRange2PercentAll;
    @SerializedName("total_age_range3_percent_all")
    @Expose
    private double totalAgeRange3PercentAll;
    @SerializedName("total_age_range4_percent_all")
    @Expose
    private double totalAgeRange4PercentAll;
    @SerializedName("total_age_range5_percent_all")
    @Expose
    private double totalAgeRange5PercentAll;
    @SerializedName("total_age_range6_percent_all")
    @Expose
    private double totalAgeRange6PercentAll;
    @SerializedName("total_age_range1_percent_male")
    @Expose
    private double totalAgeRange1PercentMale;
    @SerializedName("total_age_range2_percent_male")
    @Expose
    private double totalAgeRange2PercentMale;
    @SerializedName("total_age_range3_percent_male")
    @Expose
    private double totalAgeRange3PercentMale;
    @SerializedName("total_age_range4_percent_male")
    @Expose
    private double totalAgeRange4PercentMale;
    @SerializedName("total_age_range5_percent_male")
    @Expose
    private double totalAgeRange5PercentMale;
    @SerializedName("total_age_range6_percent_male")
    @Expose
    private double totalAgeRange6PercentMale;
    @SerializedName("total_age_range1_percent_female")
    @Expose
    private double totalAgeRange1PercentFemale;
    @SerializedName("total_age_range2_percent_female")
    @Expose
    private double totalAgeRange2PercentFemale;
    @SerializedName("total_age_range3_percent_female")
    @Expose
    private double totalAgeRange3PercentFemale;
    @SerializedName("total_age_range4_percent_female")
    @Expose
    private double totalAgeRange4PercentFemale;
    @SerializedName("total_age_range5_percent_female")
    @Expose
    private double totalAgeRange5PercentFemale;
    @SerializedName("total_age_range6_percent_female")
    @Expose
    private double totalAgeRange6PercentFemale;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("city")
    @Expose
    private List<CityLoc> city = null;
    @SerializedName("state")
    @Expose
    private List<State> state = null;

    public List<CityLoc> getCity() {
        return city;
    }

    public void setCity(List<CityLoc> city) {
        this.city = city;
    }

    public List<State> getState() {
        return state;
    }

    public void setState(List<State> state) {
        this.state = state;
    }

    public List<DetailList> getResult() {
        return result;
    }

    public void setResult(List<DetailList> result) {
        this.result = result;
    }

    public double getMalePercent() {
        return malePercent;
    }

    public void setMalePercent(double malePercent) {
        this.malePercent = malePercent;
    }

    public double getFemalePercent() {
        return femalePercent;
    }

    public void setFemalePercent(double femalePercent) {
        this.femalePercent = femalePercent;
    }

    public double getTotalAgeRange1PercentAll() {
        return totalAgeRange1PercentAll;
    }

    public void setTotalAgeRange1PercentAll(double totalAgeRange1PercentAll) {
        this.totalAgeRange1PercentAll = totalAgeRange1PercentAll;
    }

    public double getTotalAgeRange2PercentAll() {
        return totalAgeRange2PercentAll;
    }

    public void setTotalAgeRange2PercentAll(double totalAgeRange2PercentAll) {
        this.totalAgeRange2PercentAll = totalAgeRange2PercentAll;
    }

    public double getTotalAgeRange3PercentAll() {
        return totalAgeRange3PercentAll;
    }

    public void setTotalAgeRange3PercentAll(double totalAgeRange3PercentAll) {
        this.totalAgeRange3PercentAll = totalAgeRange3PercentAll;
    }

    public double getTotalAgeRange4PercentAll() {
        return totalAgeRange4PercentAll;
    }

    public void setTotalAgeRange4PercentAll(double totalAgeRange4PercentAll) {
        this.totalAgeRange4PercentAll = totalAgeRange4PercentAll;
    }

    public double getTotalAgeRange5PercentAll() {
        return totalAgeRange5PercentAll;
    }

    public void setTotalAgeRange5PercentAll(double totalAgeRange5PercentAll) {
        this.totalAgeRange5PercentAll = totalAgeRange5PercentAll;
    }

    public double getTotalAgeRange6PercentAll() {
        return totalAgeRange6PercentAll;
    }

    public void setTotalAgeRange6PercentAll(double totalAgeRange6PercentAll) {
        this.totalAgeRange6PercentAll = totalAgeRange6PercentAll;
    }

    public double getTotalAgeRange1PercentMale() {
        return totalAgeRange1PercentMale;
    }

    public void setTotalAgeRange1PercentMale(double totalAgeRange1PercentMale) {
        this.totalAgeRange1PercentMale = totalAgeRange1PercentMale;
    }

    public double getTotalAgeRange2PercentMale() {
        return totalAgeRange2PercentMale;
    }

    public void setTotalAgeRange2PercentMale(double totalAgeRange2PercentMale) {
        this.totalAgeRange2PercentMale = totalAgeRange2PercentMale;
    }

    public double getTotalAgeRange3PercentMale() {
        return totalAgeRange3PercentMale;
    }

    public void setTotalAgeRange3PercentMale(double totalAgeRange3PercentMale) {
        this.totalAgeRange3PercentMale = totalAgeRange3PercentMale;
    }

    public double getTotalAgeRange4PercentMale() {
        return totalAgeRange4PercentMale;
    }

    public void setTotalAgeRange4PercentMale(double totalAgeRange4PercentMale) {
        this.totalAgeRange4PercentMale = totalAgeRange4PercentMale;
    }

    public double getTotalAgeRange5PercentMale() {
        return totalAgeRange5PercentMale;
    }

    public void setTotalAgeRange5PercentMale(double totalAgeRange5PercentMale) {
        this.totalAgeRange5PercentMale = totalAgeRange5PercentMale;
    }

    public double getTotalAgeRange6PercentMale() {
        return totalAgeRange6PercentMale;
    }

    public void setTotalAgeRange6PercentMale(double totalAgeRange6PercentMale) {
        this.totalAgeRange6PercentMale = totalAgeRange6PercentMale;
    }

    public double getTotalAgeRange1PercentFemale() {
        return totalAgeRange1PercentFemale;
    }

    public void setTotalAgeRange1PercentFemale(double totalAgeRange1PercentFemale) {
        this.totalAgeRange1PercentFemale = totalAgeRange1PercentFemale;
    }

    public double getTotalAgeRange2PercentFemale() {
        return totalAgeRange2PercentFemale;
    }

    public void setTotalAgeRange2PercentFemale(double totalAgeRange2PercentFemale) {
        this.totalAgeRange2PercentFemale = totalAgeRange2PercentFemale;
    }

    public double getTotalAgeRange3PercentFemale() {
        return totalAgeRange3PercentFemale;
    }

    public void setTotalAgeRange3PercentFemale(double totalAgeRange3PercentFemale) {
        this.totalAgeRange3PercentFemale = totalAgeRange3PercentFemale;
    }

    public double getTotalAgeRange4PercentFemale() {
        return totalAgeRange4PercentFemale;
    }

    public void setTotalAgeRange4PercentFemale(double totalAgeRange4PercentFemale) {
        this.totalAgeRange4PercentFemale = totalAgeRange4PercentFemale;
    }

    public double getTotalAgeRange5PercentFemale() {
        return totalAgeRange5PercentFemale;
    }

    public void setTotalAgeRange5PercentFemale(double totalAgeRange5PercentFemale) {
        this.totalAgeRange5PercentFemale = totalAgeRange5PercentFemale;
    }

    public double getTotalAgeRange6PercentFemale() {
        return totalAgeRange6PercentFemale;
    }

    public void setTotalAgeRange6PercentFemale(double totalAgeRange6PercentFemale) {
        this.totalAgeRange6PercentFemale = totalAgeRange6PercentFemale;
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
