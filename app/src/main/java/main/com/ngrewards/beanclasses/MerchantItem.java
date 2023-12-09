package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by technorizen on 25/7/18.
 */

public class MerchantItem {
    @SerializedName("result")
    @Expose
    private List<MerchantItemList> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("active_product_count")
    @Expose
    private String activeProductCount;
    @SerializedName("sold_product_count")
    @Expose
    private String soldProductCount;
    @SerializedName("unsold_product_count")
    @Expose
    private String unsoldProductCount;
    @SerializedName("total_earning")
    @Expose
    private String total_earning;
    @SerializedName("total_earning_with_shipping")
    @Expose
    private String total_earning_with_shipping;

    public List<MerchantItemList> getResult() {
        return result;
    }

    public void setResult(List<MerchantItemList> result) {
        this.result = result;
    }

    public String getTotal_earning() {
        return total_earning;
    }

    public void setTotal_earning(String total_earning) {
        this.total_earning = total_earning;
    }

    public String getTotal_earning_with_shipping() {
        return total_earning_with_shipping;
    }

    public void setTotal_earning_with_shipping(String total_earning_with_shipping) {
        this.total_earning_with_shipping = total_earning_with_shipping;
    }

    public String getActiveProductCount() {
        return activeProductCount;
    }

    public void setActiveProductCount(String activeProductCount) {
        this.activeProductCount = activeProductCount;
    }

    public String getSoldProductCount() {
        return soldProductCount;
    }

    public void setSoldProductCount(String soldProductCount) {
        this.soldProductCount = soldProductCount;
    }

    public String getUnsoldProductCount() {
        return unsoldProductCount;
    }

    public void setUnsoldProductCount(String unsoldProductCount) {
        this.unsoldProductCount = unsoldProductCount;
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
}
