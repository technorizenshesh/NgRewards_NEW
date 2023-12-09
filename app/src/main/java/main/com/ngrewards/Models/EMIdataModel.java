package main.com.ngrewards.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EMIdataModel implements Serializable {

    @Expose
    @SerializedName("key")
    private String key;
    @Expose
    @SerializedName("order_id")
    private String orderId;
    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("merchant_id")
    private String merchantId;
    @Expose
    @SerializedName("merchant_business_no")
    private String merchantBusinessNo;
    @Expose
    @SerializedName("merchant_business_name")
    private String merchantBusinessName;
    @Expose
    @SerializedName("split_amount_x")
    private String splitAmountX;
    @Expose
    @SerializedName("cart_id")
    private String cartId;
    @Expose
    @SerializedName("member_id")
    private String memberId;
    @Expose
    @SerializedName("result")
    private String result;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantBusinessNo() {
        return merchantBusinessNo;
    }

    public void setMerchantBusinessNo(String merchantBusinessNo) {
        this.merchantBusinessNo = merchantBusinessNo;
    }

    public String getMerchantBusinessName() {
        return merchantBusinessName;
    }

    public void setMerchantBusinessName(String merchantBusinessName) {
        this.merchantBusinessName = merchantBusinessName;
    }

    public String getSplitAmountX() {
        return splitAmountX;
    }

    public void setSplitAmountX(String splitAmountX) {
        this.splitAmountX = splitAmountX;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
