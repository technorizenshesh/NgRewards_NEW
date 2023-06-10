package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by technorizen on 21/9/18.
 */

public class CartBean {
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("total_shipping_price")
    @Expose
    private String total_shipping_price;
    @SerializedName("total_with_shipping_price")
    @Expose
    private String total_with_shipping_price;
    @SerializedName("cart_count")
    @Expose
    private Integer cartCount;
    @SerializedName("total_cart_count")
    @Expose
    private Integer total_cart_count;
    @SerializedName("result")
    @Expose
    private List<CartListBean> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getCartCount() {
        return cartCount;
    }

    public void setCartCount(Integer cartCount) {
        this.cartCount = cartCount;
    }

    public List<CartListBean> getResult() {
        return result;
    }

    public void setResult(List<CartListBean> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotal_cart_count() {
        return total_cart_count;
    }

    public void setTotal_cart_count(Integer total_cart_count) {
        this.total_cart_count = total_cart_count;
    }

    public String getTotal_with_shipping_price() {
        return total_with_shipping_price;
    }

    public void setTotal_with_shipping_price(String total_with_shipping_price) {
        this.total_with_shipping_price = total_with_shipping_price;
    }

    public String getTotal_shipping_price() {
        return total_shipping_price;
    }

    public void setTotal_shipping_price(String total_shipping_price) {
        this.total_shipping_price = total_shipping_price;
    }
}
