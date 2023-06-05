package main.com.ngrewards.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationModel {

    @SerializedName("result")
    @Expose
    private ArrayList<Result> result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
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

    public class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("reciever_id")
        @Expose
        private String recieverId;
        @SerializedName("chat_message")
        @Expose
        private String chatMessage;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date_time")
        @Expose
        private String dateTime;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("payload")
        @Expose
        private Payload payload;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRecieverId() {
            return recieverId;
        }

        public void setRecieverId(String recieverId) {
            this.recieverId = recieverId;
        }

        public String getChatMessage() {
            return chatMessage;
        }

        public void setChatMessage(String chatMessage) {
            this.chatMessage = chatMessage;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public Payload getPayload() {
            return payload;
        }

        public void setPayload(Payload payload) {
            this.payload = payload;
        }


        public class Payload {

            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("text")
            @Expose
            private String text;
            @SerializedName("ios_status")
            @Expose
            private String iosStatus;
            @SerializedName("sound")
            @Expose
            private String sound;
            @SerializedName("badge")
            @Expose
            private String badge;
            @SerializedName("type")
            @Expose
            private Integer type;
            @SerializedName("content-available")
            @Expose
            private Integer contentAvailable;
            @SerializedName("cart_id")
            @Expose
            private String cartId;
            @SerializedName("order_id")
            @Expose
            private String orderId;
            @SerializedName("split_amount_x")
            @Expose
            private String splitAmountX;
            @SerializedName("merchant_id")
            @Expose
            private String merchantId;
            @SerializedName("merchant_business_no")
            @Expose
            private String merchantBusinessNo;
            @SerializedName("merchant_business_name")
            @Expose
            private String merchantBusinessName;
            @SerializedName("member_id")
            @Expose
            private String memberId;
            @SerializedName("due_date")
            @Expose
            private String dueDate;
            @SerializedName("message")
            @Expose
            private String message;
            @SerializedName("number_of_emi")
            @Expose
            private String numberOfEmi;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getIosStatus() {
                return iosStatus;
            }

            public void setIosStatus(String iosStatus) {
                this.iosStatus = iosStatus;
            }

            public String getSound() {
                return sound;
            }

            public void setSound(String sound) {
                this.sound = sound;
            }

            public String getBadge() {
                return badge;
            }

            public void setBadge(String badge) {
                this.badge = badge;
            }

            public Integer getType() {
                return type;
            }

            public void setType(Integer type) {
                this.type = type;
            }

            public Integer getContentAvailable() {
                return contentAvailable;
            }

            public void setContentAvailable(Integer contentAvailable) {
                this.contentAvailable = contentAvailable;
            }

            public String getCartId() {
                return cartId;
            }

            public void setCartId(String cartId) {
                this.cartId = cartId;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getSplitAmountX() {
                return splitAmountX;
            }

            public void setSplitAmountX(String splitAmountX) {
                this.splitAmountX = splitAmountX;
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

            public String getMemberId() {
                return memberId;
            }

            public void setMemberId(String memberId) {
                this.memberId = memberId;
            }

            public String getDueDate() {
                return dueDate;
            }

            public void setDueDate(String dueDate) {
                this.dueDate = dueDate;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getNumberOfEmi() {
                return numberOfEmi;
            }

            public void setNumberOfEmi(String numberOfEmi) {
                this.numberOfEmi = numberOfEmi;
            }

            @Override
            public String toString() {

                /*         merchant_id = data.getString("merchant_id");
                    merchant_name = data.getString("merchant_business_name");
                   merchant_number = data.getString("merchant_business_no");
                    order_cart_id = data.getString("cart_id");
                  sub_total_price = data.getString("split_amount_x");
                   tax_price = data.getString("split_amount_x");
                  total_amount_due = data.getString("split_amount_x");
                   type = data.getString("type");
                  quantity = data.getString("split_amount_x");
                employee_sales_id = data.getString("split_amount_x");
                employee_slaes_name = data.getString("split_amount_x");
                order_id = data.getString("order_id");*/
                return "{" +
                        "title='" + title + '\'' +
                        ", text='" + text + '\'' +
                        ", iosStatus='" + iosStatus + '\'' +
                        ", sound='" + sound + '\'' +
                        ", badge='" + badge + '\'' +
                        ", type=" + type +
                        ", contentAvailable=" + contentAvailable +
                        ", cart_id='" + cartId + '\'' +
                        ", order_id='" + orderId + '\'' +
                        ", split_amount_x='" + splitAmountX + '\'' +
                        ", merchant_id='" + merchantId + '\'' +
                        ", merchant_business_no='" + merchantBusinessNo + '\'' +
                        ", merchant_business_name='" + merchantBusinessName + '\'' +
                        ", memberId='" + memberId + '\'' +
                        ", dueDate='" + dueDate + '\'' +
                        ", message='" + message + '\'' +
                        ", numberOfEmi='" + numberOfEmi + '\'' +
                        '}';
            }
        }

    }
}
