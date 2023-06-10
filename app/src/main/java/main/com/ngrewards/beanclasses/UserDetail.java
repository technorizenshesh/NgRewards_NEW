package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by technorizen on 21/8/18.
 */

public class UserDetail {
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
    @SerializedName("affiliate_name")
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
    @SerializedName("linkdin_url")
    @Expose
    private String linkdinUrl;
    @SerializedName("google_plus_url")
    @Expose
    private String googlePlusUrl;
    @SerializedName("instagram_url")
    @Expose
    private String instagramUrl;
    @SerializedName("business_category")
    @Expose
    private String businessCategory;
    @SerializedName("merchant_image")
    @Expose
    private String merchantImage;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

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
}
