package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by technorizen on 28/11/18.
 */

/*
        if(bundle != null && !bundle.isEmpty()) {
                reciept_url = bundle.getString("reciept_url");
                member_id = bundle.getString("member_id");
                transfer_request_user_id = bundle.getString("transfer_request_user_id");
                total_amt_tv_str = bundle.getString("total_amt_tv_str");
                due_amt_tv_str = bundle.getString("due_amt_tv_str");
                comment = bundle.getString("comment");
                }*/

public class OrderBean {

    @SerializedName("transfer_request_user_id")
    @Expose
    private String transferRequestUserId;

    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("amount_by_card")
    @Expose
    private String amount_by_card;

    @Expose
    private String customerId;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("my_request")
    @Expose
    private String myRequest;
    @SerializedName("member_detail")
    @Expose
    private List<MemberDetail> memberDetail = null;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_description")
    @Expose
    private String productDescription;

    public String getOreder_meber_Name() {
        return oreder_meber_Name;
    }

    public void setOreder_meber_Name(String oreder_meber_Name) {
        this.oreder_meber_Name = oreder_meber_Name;
    }

    @SerializedName("oreder_meber_Name")
    @Expose
    private String oreder_meber_Name;

   //
    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("size")

    @Expose
    private String size;

    @SerializedName("order_special_request")

    @Expose
    private String order_special_request;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("merchant_detail")
    @Expose
    private List<MerchantBean> merchantDetail = null;

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
    @SerializedName("paid_by_card")
    @Expose
    private String paid_by_card;
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
    @SerializedName("order_Date")
    @Expose
    private String orderDate2;
    @SerializedName("order_Time")
    @Expose
    private String order_Time;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;

    @SerializedName("total_cost")
    @Expose
    private String totalCost;
    @SerializedName("thumbnail_image")
    @Expose
    private String thumbnailImage;
    @SerializedName("total_product_price")
    @Expose
    private String totalproductprice;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("review_status")
    @Expose
    private String reviewstatus;
    @SerializedName("product_image")
    @Expose
    private List<ProductImage> productImage = null;

    @SerializedName("merchant_id")
    @Expose
    private String merchant_id;

    @SerializedName("merchant_no")
    @Expose
    private String merchant_no;

    @SerializedName("member_id")
    @Expose
    private String member_id;

    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("total_amount")
    @Expose
    private String total_amount;
    @SerializedName("tip_amount")
    @Expose
    private String tip_amount;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("created_date")
    @Expose
    private String created_date;

    @SerializedName("shipping_price")
    @Expose
    private String shipping_price;
    @SerializedName("total_price_with_shipping")
    @Expose
    private String total_price_with_shipping;

    @SerializedName("employee_name")
    @Expose
    private String employeeName;

    public String getSearch_id() {
        return search_id;
    }

    public void setSearch_id(String search_id) {
        this.search_id = search_id;
    }

    @SerializedName("search_id")
    @Expose
    private String search_id;

    public String getOrder_cart_id() {
        return order_cart_id;
    }

    public void setOrder_cart_id(String order_cart_id) {
        this.order_cart_id = order_cart_id;
    }

    @SerializedName("order_cart_id")
    @Expose
    private String order_cart_id;



    //implimnet by sagar panse //

    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public String getSymbol_amount() {
        return symbol_amount;
    }

    public void setSymbol_amount(String symbol_amount) {
        this.symbol_amount = symbol_amount;
    }

    @SerializedName("b_name")
    @Expose
    private String b_name;

    @SerializedName("symbol_amount")
    @Expose
    private String symbol_amount;




    // impliment by sagar panse //




    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("order_guset_No")
    @Expose
    private String order_guset_No;
    @SerializedName("order_Table_No")

    @Expose
    private String order_Table_No;
    @SerializedName("reciept_url")
    @Expose
    private String reciept_url;

    public String getOrder_special_request() {
        return order_special_request;
    }

    public void setOrder_special_request(String order_special_request) {
        this.order_special_request = order_special_request;
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

    public String getTotal_price_with_shipping() {
        return total_price_with_shipping;
    }

    public void setTotal_price_with_shipping(String total_price_with_shipping) {
        this.total_price_with_shipping = total_price_with_shipping;
    }
    public String getShipping_price() {
        return shipping_price;
    }

    public void setShipping_price(String shipping_price) {
        this.shipping_price = shipping_price;
    }

    public String getTransferRequestUserId() {
        return transferRequestUserId;
    }

    public void setTransferRequestUserId(String transferRequestUserId) {
        this.transferRequestUserId = transferRequestUserId;
    }

    public String getAmount_by_card() {
        return amount_by_card;
    }

    public void setAmount_by_card(String amount_by_card) {
        this.amount_by_card = amount_by_card;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getMyRequest() {
        return myRequest;
    }

    public void setMyRequest(String myRequest) {
        this.myRequest = myRequest;
    }

    public List<MemberDetail> getMemberDetail() {
        return memberDetail;
    }

    public void setMemberDetail(List<MemberDetail> memberDetail) {
        this.memberDetail = memberDetail;
    }

    public String getPaid_by_card() {
        return paid_by_card;
    }

    public void setPaid_by_card(String paid_by_card) {
        this.paid_by_card = paid_by_card;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_no() {
        return merchant_no;
    }

    public void setMerchant_no(String merchant_no) {
        this.merchant_no = merchant_no;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getTip_amount() {
        return tip_amount;
    }

    public void setTip_amount(String tip_amount) {
        this.tip_amount = tip_amount;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getReviewstatus() {
        return reviewstatus;
    }

    public void setReviewstatus(String reviewstatus) {
        this.reviewstatus = reviewstatus;
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

    public List<MerchantBean> getMerchantDetail() {
        return merchantDetail;
    }

    public void setMerchantDetail(List<MerchantBean> merchantDetail) {
        this.merchantDetail = merchantDetail;
    }

    public String getTotalproductprice() {
        return totalproductprice;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getAverage_rating() {
        return rating;
    }

    public void setAverage_rating(String rating) {
        this.rating = rating;
    }

    public void setTotalproductprice(String totalproductprice) {
        this.totalproductprice = totalproductprice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getOrder_Time() {
        return order_Time;
    }

    public void setOrder_Time(String order_Time) {
        this.order_Time = order_Time;
    }

    public String getOrderDate2() {
        return orderDate2;
    }

    public void setOrderDate2(String orderDate2) {
        this.orderDate2 = orderDate2;
    }

    public String getOrder_guset_No() {
        return order_guset_No;
    }

    public void setOrder_guset_No(String order_guset_No) {
        this.order_guset_No = order_guset_No;
    }

    public String getOrder_Table_No() {
        return order_Table_No;
    }

    public void setOrder_Table_No(String order_Table_No) {
        this.order_Table_No = order_Table_No;
    }

    public String getReciept_url() {
        return reciept_url;
    }

    public void setReciept_url(String reciept_url) {
        this.reciept_url = reciept_url;
    }
}
