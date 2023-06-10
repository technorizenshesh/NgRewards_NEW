package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by technorizen on 7/9/18.
 */

public class ReviewTopRecent {
    @SerializedName("all_top_reviews")
    @Expose
    private List<AllTopReview> allTopReviews = null;
    @SerializedName("all_recent_reviews")
    @Expose
    private List<MerchantTopReview> allRecentReviews = null;

    public List<AllTopReview> getAllTopReviews() {
        return allTopReviews;
    }

    public void setAllTopReviews(List<AllTopReview> allTopReviews) {
        this.allTopReviews = allTopReviews;
    }

    public List<MerchantTopReview> getAllRecentReviews() {
        return allRecentReviews;
    }

    public void setAllRecentReviews(List<MerchantTopReview> allRecentReviews) {
        this.allRecentReviews = allRecentReviews;
    }
}
