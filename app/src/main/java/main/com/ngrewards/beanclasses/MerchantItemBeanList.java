package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by technorizen on 26/2/19.
 */

public class MerchantItemBeanList {

    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("contact_name")
    @Expose
    private String contactName;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("share_link")
    @Expose
    private String shareLink;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("sold_by")
    @Expose
    private String soldBy;
    @SerializedName("shipping_time")
    @Expose
    private String shippingTime;
    @SerializedName("shipping_price")
    @Expose
    private String shippingPrice;
    @SerializedName("stock")
    @Expose
    private String stock;
    @SerializedName("average_rating")
    @Expose
    private String averageRating;
    @SerializedName("review_count")
    @Expose
    private String reviewCount;
    @SerializedName("thumbnail_image")
    @Expose
    private String thumbnailImage;
    @SerializedName("product_images")
    @Expose
    private List<ProductImage> productImages = null;
    @SerializedName("split_payments")
    @Expose
    private String split_payments;
    @SerializedName("split_amount")
    @Expose
    private String split_amount;

    public String getSplit_payments() {
        return split_payments;
    }

    public void setSplit_payments(String split_payments) {
        this.split_payments = split_payments;
    }

    public String getSplit_amount() {
        return split_amount;
    }

    public void setSplit_amount(String split_amount) {
        this.split_amount = split_amount;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    public String getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(String shippingTime) {
        this.shippingTime = shippingTime;
    }

    public String getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(String shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

}
