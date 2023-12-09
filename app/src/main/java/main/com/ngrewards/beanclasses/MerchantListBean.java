package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by technorizen on 17/7/18.
 */

public class MerchantListBean implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("business_no")
    @Expose
    private String businessNo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("contact_name")
    @Expose
    private String contactName;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("account_no")
    @Expose
    private String accountNo;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("zip_code")
    @Expose
    private String zipCode;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("reward_percent")
    @Expose
    private String rewardPercent;
    @SerializedName("how_invite_you")
    @Expose
    private String howInviteYou;
    @SerializedName("username")
    @Expose
    private String affiliateName;
    @SerializedName("business_description")
    @Expose
    private String businessDescription;
    @SerializedName("website_url")
    @Expose
    private String websiteUrl;
    @SerializedName("facebook_url")
    @Expose
    private String facebookUrl;
    @SerializedName("twitter_url")
    @Expose
    private String twitterUrl;
    @SerializedName("opening_time")
    @Expose
    private String opening_time;
    @SerializedName("closing_time")
    @Expose
    private String closing_time;
    @SerializedName("linkdin_url")
    @Expose
    private String linkdinUrl;
    @SerializedName("employee_sale_name")
    @Expose
    private String employee_sale_name;
    @SerializedName("employee_sale_id")
    @Expose
    private String employee_sale_id;
    @SerializedName("google_plus_url")
    @Expose
    private String googlePlusUrl;
    @SerializedName("instagram_url")
    @Expose
    private String instagramUrl;
    @SerializedName("youtube_url")
    @Expose
    private String youtube_url;
    @SerializedName("business_category")
    @Expose
    private String businessCategory;
    @SerializedName("business_category_name")
    @Expose
    private String businessCategoryName;
    @SerializedName("merchant_image")
    @Expose
    private String merchantImage;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("open_close_status")
    @Expose
    private String openCloseStatus;
    @SerializedName("gallery_images")
    @Expose
    private List<GalleryBean> galleryImages = null;
    @SerializedName("hours")
    @Expose
    private List<HourBean> hours = null;
    @SerializedName("like_status")
    @Expose
    private String likeStatus;
    @SerializedName("like_count")
    @Expose
    private String likeCount;
    @SerializedName("review_count")
    @Expose
    private String reviewCount;
    @SerializedName("review_status")
    @Expose
    private String reviewStatus;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("average_rating")
    @Expose
    private String averageRating;
    @SerializedName("keyword")
    @Expose
    private String keywordS;
    @SerializedName("receiver_type")
    @Expose
    private String receiver_type;
    @SerializedName("order_status")
    @Expose
    private String order_status;
    @SerializedName("share_link")
    @Expose
    private String share_link;
    @SerializedName("one_star_percentage")
    @Expose
    private Integer oneStarPercentage;
    @SerializedName("two_star_percentage")
    @Expose
    private Integer twoStarPercentage;
    @SerializedName("three_star_percentage")
    @Expose
    private Integer threeStarPercentage;
    @SerializedName("four_star_percentage")
    @Expose
    private Integer fourStarPercentage;
    @SerializedName("five_star_percentage")
    @Expose
    private Integer fiveStarPercentage;
    @SerializedName("merchant_top_review")
    @Expose
    private List<MerchantTopReview> merchantTopReview = null;

    public String getEmployee_sale_name() {
        return employee_sale_name;
    }

    public void setEmployee_sale_name(String employee_sale_name) {
        this.employee_sale_name = employee_sale_name;
    }

    public String getEmployee_sale_id() {
        return employee_sale_id;
    }

    public void setEmployee_sale_id(String employee_sale_id) {
        this.employee_sale_id = employee_sale_id;
    }

    public String getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(String opening_time) {
        this.opening_time = opening_time;
    }

    public String getClosing_time() {
        return closing_time;
    }

    public void setClosing_time(String closing_time) {
        this.closing_time = closing_time;
    }

    public String getShare_link() {
        return share_link;
    }

    public void setShare_link(String share_link) {
        this.share_link = share_link;
    }

    public String getYoutube_url() {
        return youtube_url;
    }

    public void setYoutube_url(String youtube_url) {
        this.youtube_url = youtube_url;
    }

    public String getReceiver_type() {
        return receiver_type;
    }

    public void setReceiver_type(String receiver_type) {
        this.receiver_type = receiver_type;
    }

    public String getKeywordS() {
        return keywordS;
    }

    public void setKeywordS(String keywordS) {
        this.keywordS = keywordS;
    }

    public String getBusinessCategoryName() {
        return businessCategoryName;
    }

    public void setBusinessCategoryName(String businessCategoryName) {
        this.businessCategoryName = businessCategoryName;
    }

    public Integer getOneStarPercentage() {
        return oneStarPercentage;
    }

    public void setOneStarPercentage(Integer oneStarPercentage) {
        this.oneStarPercentage = oneStarPercentage;
    }

    public Integer getTwoStarPercentage() {
        return twoStarPercentage;
    }

    public void setTwoStarPercentage(Integer twoStarPercentage) {
        this.twoStarPercentage = twoStarPercentage;
    }

    public Integer getThreeStarPercentage() {
        return threeStarPercentage;
    }

    public void setThreeStarPercentage(Integer threeStarPercentage) {
        this.threeStarPercentage = threeStarPercentage;
    }

    public Integer getFourStarPercentage() {
        return fourStarPercentage;
    }

    public void setFourStarPercentage(Integer fourStarPercentage) {
        this.fourStarPercentage = fourStarPercentage;
    }

    public Integer getFiveStarPercentage() {
        return fiveStarPercentage;
    }

    public void setFiveStarPercentage(Integer fiveStarPercentage) {
        this.fiveStarPercentage = fiveStarPercentage;
    }

    public List<MerchantTopReview> getMerchantTopReview() {
        return merchantTopReview;
    }

    public void setMerchantTopReview(List<MerchantTopReview> merchantTopReview) {
        this.merchantTopReview = merchantTopReview;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public String getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRewardPercent() {
        return rewardPercent;
    }

    public void setRewardPercent(String rewardPercent) {
        this.rewardPercent = rewardPercent;
    }

    public String getHowInviteYou() {
        return howInviteYou;
    }

    public void setHowInviteYou(String howInviteYou) {
        this.howInviteYou = howInviteYou;
    }

    public String getAffiliateName() {
        return affiliateName;
    }

    public void setAffiliateName(String affiliateName) {
        this.affiliateName = affiliateName;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getLinkdinUrl() {
        return linkdinUrl;
    }

    public void setLinkdinUrl(String linkdinUrl) {
        this.linkdinUrl = linkdinUrl;
    }

    public String getGooglePlusUrl() {
        return googlePlusUrl;
    }

    public void setGooglePlusUrl(String googlePlusUrl) {
        this.googlePlusUrl = googlePlusUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    public String getMerchantImage() {
        return merchantImage;
    }

    public void setMerchantImage(String merchantImage) {
        this.merchantImage = merchantImage;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getOpenCloseStatus() {
        return openCloseStatus;
    }

    public void setOpenCloseStatus(String openCloseStatus) {
        this.openCloseStatus = openCloseStatus;
    }

    public List<GalleryBean> getGalleryImages() {
        return galleryImages;
    }

    public void setGalleryImages(List<GalleryBean> galleryImages) {
        this.galleryImages = galleryImages;
    }

    public List<HourBean> getHours() {
        return hours;
    }

    public void setHours(List<HourBean> hours) {
        this.hours = hours;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
