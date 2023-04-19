package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by technorizen on 21/9/18.
 */

public class CartListBean {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("product_cart_price")
    @Expose
    private String product_cart_price;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("product_detail")
    @Expose
    private DetailList productDetail;
    @SerializedName("user_details")
    @Expose
    private List<UserDetail> userDetails = null;


    @SerializedName("shipping_price")
    @Expose
    private String shipping_price;
    @SerializedName("estimated_delivery_date")
    @Expose
    private String estimated_delivery_date;
    @SerializedName("split_amount")
    @Expose
    private String split_amount;
    @SerializedName("split_payment")
    @Expose
    private String split_payment;
    @SerializedName("pay_by_emi")
    @Expose
    private String pay_by_emi;
    @SerializedName("split_date")
    @Expose
    private String split_date;
    @SerializedName("split_invoice")
    @Expose
    private String split_invoice;


    public String getSplit_amount() {
        return split_amount;
    }

    public void setSplit_amount(String split_amount) {
        this.split_amount = split_amount;
    }

    public String getSplit_payment() {
        return split_payment;
    }

    public void setSplit_payment(String split_payment) {
        this.split_payment = split_payment;
    }

    public String getPay_by_emi() {
        return pay_by_emi;
    }

    public void setPay_by_emi(String pay_by_emi) {
        this.pay_by_emi = pay_by_emi;
    }

    public String getSplit_date() {
        return split_date;
    }

    public void setSplit_date(String split_date) {
        this.split_date = split_date;
    }

    public String getSplit_invoice() {
        return split_invoice;
    }

    public void setSplit_invoice(String split_invoice) {
        this.split_invoice = split_invoice;
    }

    public String getShipping_price() {
        return shipping_price;
    }

    public void setShipping_price(String shipping_price) {
        this.shipping_price = shipping_price;
    }

    public String getEstimated_delivery_date() {
        return estimated_delivery_date;
    }

    public void setEstimated_delivery_date(String estimated_delivery_date) {
        this.estimated_delivery_date = estimated_delivery_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public DetailList getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(DetailList productDetail) {
        this.productDetail = productDetail;
    }

    public List<UserDetail> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<UserDetail> userDetails) {
        this.userDetails = userDetails;
    }

    public String getProduct_cart_price() {
        return product_cart_price;
    }

    public void setProduct_cart_price(String product_cart_price) {
        this.product_cart_price = product_cart_price;
    }
}
