package main.com.ngrewards.beanclasses;

/**
 * Created by technorizen on 9/10/18.
 */

public class NetworkBean {
    String spent_this_week;
    String ng_cash_earned;
    String friend_count;

    public String getSpent_this_week() {
        return spent_this_week;
    }

    public void setSpent_this_week(String spent_this_week) {
        this.spent_this_week = spent_this_week;
    }

    public String getNg_cash_earned() {
        return ng_cash_earned;
    }

    public void setNg_cash_earned(String ng_cash_earned) {
        this.ng_cash_earned = ng_cash_earned;
    }

    public String getFriend_count() {
        return friend_count;
    }

    public void setFriend_count(String friend_count) {
        this.friend_count = friend_count;
    }
}
