package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import main.com.ngrewards.Models.CountryBeanList;

/**
 * Created by technorizen on 19/7/18.
 */

public class CountryBean {

    @SerializedName("result")
    @Expose
    private List<CountryBeanList> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<CountryBeanList> getResult() {
        return result;
    }

    public void setResult(List<CountryBeanList> result) {
        this.result = result;
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
