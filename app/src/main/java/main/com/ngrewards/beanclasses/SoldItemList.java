package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by technorizen on 11/1/19.
 */

public class SoldItemList {
    @SerializedName("split_invoice")
    @Expose
    private String split_invoice;
    @SerializedName("split_date")
    @Expose
    private String split_date;
    @SerializedName("payment_made_by_emi")
    @Expose
    private String payment_made_by_emi;
    @SerializedName("split_payment")
    @Expose
    private String split_payment;
    @SerializedName("split_amount")
    @Expose
    private String split_amount;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("total_product_price")
    @Expose
    private String totalProductPrice;
    @SerializedName("thumbnail_image")
    @Expose
    private String thumbnailImage;
    @SerializedName("product_image")
    @Expose
    private List<ProductImage> productImage = null;
    @SerializedName("member_detail")
    @Expose
    private List<MemberDetail> memberDetail = null;
    @SerializedName("shipping_first_name")
    @Expose
    private String shippingFirstName;
    @SerializedName("shipping_last_name")
    @Expose
    private String shippingLastName;
    @SerializedName("shipping_company")
    @Expose
    private String shippingCompany;
    @SerializedName("shipping_email")
    @Expose
    private String shippingEmail;
    @SerializedName("shipping_phone")
    @Expose
    private String shippingPhone;
    @SerializedName("shipping_address_1")
    @Expose
    private String shippingAddress1;
    @SerializedName("shipping_address_2")
    @Expose
    private String shippingAddress2;
    @SerializedName("shipping_country")
    @Expose
    private String shippingCountry;
    @SerializedName("shipping_state")
    @Expose
    private String shippingState;
    @SerializedName("shipping_postcode")
    @Expose
    private String shippingPostcode;
    @SerializedName("billing_first_name")
    @Expose
    private String billingFirstName;
    @SerializedName("billing_last_name")
    @Expose
    private String billingLastName;
    @SerializedName("billing_company")
    @Expose
    private String billingCompany;
    @SerializedName("billing_email")
    @Expose
    private String billingEmail;
    @SerializedName("billing_phone")
    @Expose
    private String billingPhone;
    @SerializedName("billing_address_1")
    @Expose
    private String billingAddress1;
    @SerializedName("billing_address_2")
    @Expose
    private String billingAddress2;
    @SerializedName("billing_country")
    @Expose
    private String billingCountry;
    @SerializedName("billing_state")
    @Expose
    private String billingState;
    @SerializedName("billing_postcode")
    @Expose
    private String billingPostcode;
    @SerializedName("ngcash")
    @Expose
    private String ngcash;
    @SerializedName("card_id")
    @Expose
    private String cardId;
    @SerializedName("card_number")
    @Expose
    private String cardNumber;
    @SerializedName("card_brand")
    @Expose
    private String cardBrand;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("total_cost")
    @Expose
    private String totalCost;
    @SerializedName("shipping_price")
    @Expose
    private String shipping_price;
    @SerializedName("reciept_url")
    @Expose
    private String reciept_url;
    @SerializedName("total_price_with_shipping")
    @Expose
    private String total_price_with_shipping;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("color")
    @Expose
    private String color;

    public String getSplit_invoice() {
        return split_invoice;
    }

    public void setSplit_invoice(String split_invoice) {
        this.split_invoice = split_invoice;
    }

    public String getSplit_date() {
        return split_date;
    }

    public void setSplit_date(String split_date) {
        this.split_date = split_date;
    }

    public String getPayment_made_by_emi() {
        return payment_made_by_emi;
    }

    public void setPayment_made_by_emi(String payment_made_by_emi) {
        this.payment_made_by_emi = payment_made_by_emi;
    }

    public String getSplit_payment() {
        return split_payment;
    }

    public void setSplit_payment(String split_payment) {
        this.split_payment = split_payment;
    }

    public String getSplit_amount() {
        return split_amount;
    }

    public void setSplit_amount(String split_amount) {
        this.split_amount = split_amount;
    }

    public String getReciept_url() {
        return reciept_url;
    }

    public void setReciept_url(String reciept_url) {
        this.reciept_url = reciept_url;
    }

    public String getShipping_price() {
        return shipping_price;
    }

    public void setShipping_price(String shipping_price) {
        this.shipping_price = shipping_price;
    }

    public String getTotal_price_with_shipping() {
        return total_price_with_shipping;
    }

    public void setTotal_price_with_shipping(String total_price_with_shipping) {
        this.total_price_with_shipping = total_price_with_shipping;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(String totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public List<ProductImage> getProductImage() {
        return productImage;
    }

    public void setProductImage(List<ProductImage> productImage) {
        this.productImage = productImage;
    }

    public List<MemberDetail> getMemberDetail() {
        return memberDetail;
    }

    public void setMemberDetail(List<MemberDetail> memberDetail) {
        this.memberDetail = memberDetail;
    }

    public String getShippingFirstName() {
        return shippingFirstName;
    }

    public void setShippingFirstName(String shippingFirstName) {
        this.shippingFirstName = shippingFirstName;
    }

    public String getShippingLastName() {
        return shippingLastName;
    }

    public void setShippingLastName(String shippingLastName) {
        this.shippingLastName = shippingLastName;
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public String getShippingEmail() {
        return shippingEmail;
    }

    public void setShippingEmail(String shippingEmail) {
        this.shippingEmail = shippingEmail;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getShippingAddress1() {
        return shippingAddress1;
    }

    public void setShippingAddress1(String shippingAddress1) {
        this.shippingAddress1 = shippingAddress1;
    }

    public String getShippingAddress2() {
        return shippingAddress2;
    }

    public void setShippingAddress2(String shippingAddress2) {
        this.shippingAddress2 = shippingAddress2;
    }

    public String getShippingCountry() {
        return shippingCountry;
    }

    public void setShippingCountry(String shippingCountry) {
        this.shippingCountry = shippingCountry;
    }

    public String getShippingState() {
        return shippingState;
    }

    public void setShippingState(String shippingState) {
        this.shippingState = shippingState;
    }

    public String getShippingPostcode() {
        return shippingPostcode;
    }

    public void setShippingPostcode(String shippingPostcode) {
        this.shippingPostcode = shippingPostcode;
    }

    public String getBillingFirstName() {
        return billingFirstName;
    }

    public void setBillingFirstName(String billingFirstName) {
        this.billingFirstName = billingFirstName;
    }

    public String getBillingLastName() {
        return billingLastName;
    }

    public void setBillingLastName(String billingLastName) {
        this.billingLastName = billingLastName;
    }

    public String getBillingCompany() {
        return billingCompany;
    }

    public void setBillingCompany(String billingCompany) {
        this.billingCompany = billingCompany;
    }

    public String getBillingEmail() {
        return billingEmail;
    }

    public void setBillingEmail(String billingEmail) {
        this.billingEmail = billingEmail;
    }

    public String getBillingPhone() {
        return billingPhone;
    }

    public void setBillingPhone(String billingPhone) {
        this.billingPhone = billingPhone;
    }

    public String getBillingAddress1() {
        return billingAddress1;
    }

    public void setBillingAddress1(String billingAddress1) {
        this.billingAddress1 = billingAddress1;
    }

    public String getBillingAddress2() {
        return billingAddress2;
    }

    public void setBillingAddress2(String billingAddress2) {
        this.billingAddress2 = billingAddress2;
    }

    public String getBillingCountry() {
        return billingCountry;
    }

    public void setBillingCountry(String billingCountry) {
        this.billingCountry = billingCountry;
    }

    public String getBillingState() {
        return billingState;
    }

    public void setBillingState(String billingState) {
        this.billingState = billingState;
    }

    public String getBillingPostcode() {
        return billingPostcode;
    }

    public void setBillingPostcode(String billingPostcode) {
        this.billingPostcode = billingPostcode;
    }

    public String getNgcash() {
        return ngcash;
    }

    public void setNgcash(String ngcash) {
        this.ngcash = ngcash;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

}
