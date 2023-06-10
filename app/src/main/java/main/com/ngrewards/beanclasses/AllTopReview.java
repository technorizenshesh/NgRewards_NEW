package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by technorizen on 7/9/18.
 */

public class AllTopReview {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("fullname")
    @Expose
    private Object fullname;
    @SerializedName("username")
    @Expose
    private Object username;
    @SerializedName("member_image")
    @Expose
    private String memberImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getFullname() {
        return fullname;
    }

    public void setFullname(Object fullname) {
        this.fullname = fullname;
    }

    public Object getUsername() {
        return username;
    }

    public void setUsername(Object username) {
        this.username = username;
    }

    public String getMemberImage() {
        return memberImage;
    }

    public void setMemberImage(String memberImage) {
        this.memberImage = memberImage;
    }

}
