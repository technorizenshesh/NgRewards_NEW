package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by technorizen on 21/7/18.
 */

public class MemberDetail implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("b_name")
    @Expose
    private String b_name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("affiliate_name")
    @Expose
    private String affiliateName;
    @SerializedName("how_invited_you")
    @Expose
    private String howInvitedYou;
    @SerializedName("affiliate_number")
    @Expose
    private String affiliateNumber;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("member_image")
    @Expose
    private String memberImage;
    @SerializedName("business_name")

    @Expose
    private String business_name;
    @SerializedName("order_id")
    @Expose
    private String order_id;
    @SerializedName("order_date")
    @Expose
    private String order_date;

    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAffiliateName() {
        return affiliateName;
    }

    public void setAffiliateName(String affiliateName) {
        this.affiliateName = affiliateName;
    }

    public String getHowInvitedYou() {
        return howInvitedYou;
    }

    public void setHowInvitedYou(String howInvitedYou) {
        this.howInvitedYou = howInvitedYou;
    }

    public String getAffiliateNumber() {
        return affiliateNumber;
    }

    public void setAffiliateNumber(String affiliateNumber) {
        this.affiliateNumber = affiliateNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getMemberImage() {
        return memberImage;
    }

    public void setMemberImage(String memberImage) {
        this.memberImage = memberImage;
    }

    @Override
    public String toString() {
        return "MemberDetail{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", fullname='" + fullname + '\'' +
                ", username='" + username + '\'' +
                ", b_name='" + b_name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", affiliateName='" + affiliateName + '\'' +
                ", howInvitedYou='" + howInvitedYou + '\'' +
                ", affiliateNumber='" + affiliateNumber + '\'' +
                ", userType='" + userType + '\'' +
                ", countryName='" + countryName + '\'' +
                ", memberImage='" + memberImage + '\'' +
                ", business_name='" + business_name + '\'' +
                ", order_id='" + order_id + '\'' +
                ", order_date='" + order_date + '\'' +
                '}';
    }
}
