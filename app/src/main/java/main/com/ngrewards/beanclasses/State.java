package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by technorizen on 19/12/18.
 */

public class State {
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("state_percent")
    @Expose
    private String statePercent;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStatePercent() {
        return statePercent;
    }

    public void setStatePercent(String statePercent) {
        this.statePercent = statePercent;
    }
}
