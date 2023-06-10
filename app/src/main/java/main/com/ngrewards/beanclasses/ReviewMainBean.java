package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by technorizen on 7/9/18.
 */

public class ReviewMainBean {
    @SerializedName("result")
    @Expose
    private ReviewTopRecent result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public ReviewTopRecent getResult() {
        return result;
    }

    public void setResult(ReviewTopRecent result) {
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
