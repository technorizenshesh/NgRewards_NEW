package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by technorizen on 27/7/18.
 */

public class OfferBeanList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("offer_name")
    @Expose
    private String offerName;
    @SerializedName("offer_description")
    @Expose
    private String offerDescription;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("offer_discount")
    @Expose
    private String offerDiscount;
    @SerializedName("offer_price")
    @Expose
    private String offerPrice;
    @SerializedName("offer_image")
    @Expose
    private String offerImage;
    @SerializedName("like_status")
    @Expose
    private String likeStatus;
    @SerializedName("like_count")
    @Expose
    private String likeCount;

    @SerializedName("category_id")
    @Expose
    private String category_id;

    @SerializedName("distance")
    @Expose
    private String distance;


    @SerializedName("share_link")
    @Expose
    private String shareLink;
    @SerializedName("business_name")
    @Expose
    private String business_name;
    @SerializedName("merchant_id")
    @Expose
    private String merchant_id;
    @SerializedName("merchant_image")
    @Expose
    private String merchant_image;
    @SerializedName("business_no")
    @Expose
    private String business_no;
    @SerializedName("contact_name")
    @Expose
    private String contact_name;
    @SerializedName("offer_discount_price")
    @Expose
    private String offer_discount_price;

    public String getOffer_discount_price() {
        return offer_discount_price;
    }

    public void setOffer_discount_price(String offer_discount_price) {
        this.offer_discount_price = offer_discount_price;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_image() {
        return merchant_image;
    }

    public void setMerchant_image(String merchant_image) {
        this.merchant_image = merchant_image;
    }

    public String getBusiness_no() {
        return business_no;
    }

    public void setBusiness_no(String business_no) {
        this.business_no = business_no;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOfferDiscount() {
        return offerDiscount;
    }

    public void setOfferDiscount(String offerDiscount) {
        this.offerDiscount = offerDiscount;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(String offerImage) {
        this.offerImage = offerImage;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
