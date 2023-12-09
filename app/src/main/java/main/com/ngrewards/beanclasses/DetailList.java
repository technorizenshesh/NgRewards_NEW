package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DetailList {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("shipping_price")
    @Expose
    private String shipping_price;
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
    @SerializedName("cart_status")
    @Expose
    private String cart_status;
    @SerializedName("shipping_time")
    @Expose
    private String shippingTime;
    @SerializedName("review_count")
    @Expose
    private String reviewCount;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("product_cart_price")
    @Expose
    private String product_cart_price;
    @SerializedName("thumbnail_image")
    @Expose
    private String thumbnailImage;
    @SerializedName("like_status")
    @Expose
    private String likeStatus;
    @SerializedName("like_count")
    @Expose
    private String likeCount;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("comment_data")
    @Expose
    private List<CommentDatum> commentData = null;
    @SerializedName("user_details")
    @Expose
    private List<UserDetail> userDetails = null;
    @SerializedName("product_images")
    @Expose
    private List<ProductImage> productImages = null;
    @SerializedName("similar_products")
    @Expose
    private List<SimilarProduct> similarProducts = null;


    @SerializedName("review_status")
    @Expose
    private String reviewStatus;

    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("stock")
    @Expose
    private String stock;
    @SerializedName("average_rating")
    @Expose
    private String averageRating;
    @SerializedName("split_amount")
    @Expose
    private String split_amount;
    @SerializedName("product_top_review")
    @Expose
    private List<ProductTopReview> productTopReview = null;

    public List<ProductTopReview> getProductTopReview() {
        return productTopReview;
    }

    public void setProductTopReview(List<ProductTopReview> productTopReview) {
        this.productTopReview = productTopReview;
    }

    public String getSplit_amount() {
        return split_amount;
    }

    public void setSplit_amount(String split_amount) {
        this.split_amount = split_amount;
    }

    public String getProduct_cart_price() {
        return product_cart_price;
    }

    public void setProduct_cart_price(String product_cart_price) {
        this.product_cart_price = product_cart_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCart_status() {
        return cart_status;
    }

    public void setCart_status(String cart_status) {
        this.cart_status = cart_status;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
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

    public List<SimilarProduct> getSimilarProducts() {
        return similarProducts;
    }

    public void setSimilarProducts(List<SimilarProduct> similarProducts) {
        this.similarProducts = similarProducts;
    }

    public String getShipping_price() {
        return shipping_price;
    }

    public void setShipping_price(String shipping_price) {
        this.shipping_price = shipping_price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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

    public String getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(String shippingTime) {
        this.shippingTime = shippingTime;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
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

    public List<CommentDatum> getCommentData() {
        return commentData;
    }

    public void setCommentData(List<CommentDatum> commentData) {
        this.commentData = commentData;
    }

    public List<UserDetail> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<UserDetail> userDetails) {
        this.userDetails = userDetails;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }
}
