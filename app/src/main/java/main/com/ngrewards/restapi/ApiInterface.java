package main.com.ngrewards.restapi;

import java.util.Map;

import main.com.ngrewards.Models.NotificationModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by technorizen on 14/2/18.
 */

public interface ApiInterface {

    @GET("near_by_merchant_list.php?")
    Call<ResponseBody> getNearMarchant(@Query("latitude")
                                               String latitude, @Query("longitude")
                                               String longitude, @Query("country_id") String country_id,
                                       @Query("category_id") String category_id,
                                       @Query("user_id") String user_id, @Query("current_time")
                                               String current_time, @Query("sort_distance")
                                               String sort_distance, @Query("average_rate")
                                               String average_rate, @Query("order_by") String order_by);

    @GET("all_business_list.php?")
    Call<ResponseBody> getMerchantBusNum();

    @GET("all_product_lists.php?")
    Call<ResponseBody> getFeaturedProduct(@Query("user_id") String user_id, @Query("category_id") String category_id, @Query("sort_type") String sort_type, @Query("average_rate") String average_rate);

    @GET("all_merchant_publish_product_lists.php?")
    Call<ResponseBody> allMerchnatItems(@Query("user_id") String user_id, @Query("merchant_id") String merchant_id);

    @GET("all_merchant_product_lists.php?")
    Call<ResponseBody> getMerchantOwnProduct(@Query("merchant_id") String merchant_id);

    @GET("merchant_sold_product_lists.php?")
    Call<ResponseBody> getSoldProductList(@Query("merchant_id") String merchant_id);

    @GET("order_lists.php?")
    Call<ResponseBody> getSoldProduct(@Query("user_id") String user_id, @Query("category_id") String category_id, @Query("sort_type") String sort_type);

    @GET("my_offer.php?")
    Call<ResponseBody> getMyOfferProduct(@Query("user_id") String user_id);
    @GET("member_profile.php?")
    Call<ResponseBody> member_profile(@Query("user_id") String user_id);

    @GET("merchant_offer_for_member.php?")
    Call<ResponseBody> getMemberMerchnatOffer(@Query("merchant_id") String merchant_id, @Query("user_id") String user_id);

    @GET("offer_lists.php?")
    Call<ResponseBody> getMemberOffer(@Query("user_id") String user_id, @Query("category_id") String category_id, @Query("average_rate") String average_rate, @Query("order_by") String order_by, @Query("sort_distance") String sort_distance, @Query("latitude") String latitude, @Query("longitude") String longitude);

    @GET("delete_offer.php?")
    Call<ResponseBody> deleteMyOffer(@Query("offer_id") String offer_id);

    @GET("delete_product.php?")
    Call<ResponseBody> deleteProduct(@Query("product_id") String product_id);

    @GET("hide_offer.php?")
    Call<ResponseBody> hidepublishProduct(@Query("offer_id") String offer_id, @Query("status") String status);

    @GET("hide_product.php?")
    Call<ResponseBody> hideProduct(@Query("product_id") String product_id, @Query("status") String status);

    @GET("update_item_status.php?")
    Call<ResponseBody> hideProduct_status(@Query("product_id") String product_id, @Query("status") String status);

    @GET("product_details.php?")
    Call<ResponseBody> getProductDetail(@Query("product_id") String product_id, @Query("user_id") String user_id);

    @GET("merchant_details.php?")
    Call<ResponseBody> getMerchantDetail(@Query("user_id") String user_id, @Query("merchant_id") String merchant_id, @Query("latitude") String latitude, @Query("longitude") String longitude, @Query("current_time") String current_time);

    @GET("get_merchant_review.php?")
    Call<ResponseBody> getMerchnantReview(@Query("user_id") String user_id, @Query("merchant_id") String merchant_id);

    @GET("member_list.php?")
    Call<ResponseBody> getMembersusername();

    @GET("category_lists.php?")
    Call<ResponseBody> getCategory();

    @GET("offer_category_lists.php?")
    Call<ResponseBody> getOfferCategory();

    @GET("business_category_lists.php?")
    Call<ResponseBody> getBusnessCategory();

    @GET("delete_merchant_gallery_image.php?")
    Call<ResponseBody> removeImages(@Query("image_id") String image_id);

    @GET("delete_product_image.php?")
    Call<ResponseBody> removeProductImages(@Query("product_id") String product_id, @Query("image_id") String image_id);

    @GET("like_offer.php?")
    Call<ResponseBody> likedislikeoffer(@Query("offer_id") String offer_id, @Query("user_id") String user_id);

    @GET("like_merchant.php?")
    Call<ResponseBody> likedislikemerchant(@Query("merchant_id") String merchant_id, @Query("user_id") String user_id);

    @GET("review_like_dislike.php?")
    Call<ResponseBody> likedislikemerchantReview(@Query("review_id") String review_id, @Query("user_id") String user_id);

    @GET("merchant_review_lists.php?")
    Call<ResponseBody> getMerchantReviewList(@Query("merchant_id") String merchant_id, @Query("user_id") String user_id);

    @GET("merchant_top_recent_review_lists.php?")
    Call<ResponseBody> getMytReviewList(@Query("merchant_id") String merchant_id);

    @GET("member_notification_msg_lists.php?")
    Call<ResponseBody> getMemberNotification(@Query("member_id") String member_id);
    @GET("admin_notification_list_new.php")
    Call<NotificationModel> admin_notification_list_new(@Query("user_id") String member_id,
                                                        @Query("type") String type);

    @GET("like_image.php?")
    Call<ResponseBody> likedislikemerchantphoto(@Query("merchant_id") String merchant_id, @Query("image_id") String image_id, @Query("user_id") String user_id);

    @GET("get_cart.php?")
    Call<ResponseBody> getMyCart(@Query("user_id") String user_id);

    @GET("remove_cart_product.php?")
    Call<ResponseBody> removeSinglecartItem(@Query("cart_id") String cart_id);

    @GET("my_friend.php?")
    Call<ResponseBody> getMyCodeUseFriend(@Query("affiliate_no") String affiliate_no, @Query("member_id") String user_id);

    @GET("add_to_cart.php?")
    Call<ResponseBody> updatCartItem(@Query("user_id") String user_id, @Query("product_id") String product_id, @Query("quantity") String quantity);

    @GET("member_order_lists.php?")
    Call<ResponseBody> getMemberOrder(@Query("user_id") String user_id);

    @GET("employee_sales.php?")
    Call<ResponseBody> employee_sales(@Query("user_id") String user_id, @Query("employee_id") String employee_id);

    @GET("merchant_order_lists.php?")
    Call<ResponseBody> getMerchantOrder(@Query("merchant_id") String merchant_id);
   @GET("notification-emi.php")
    Call<ResponseBody> notification_emi(@QueryMap Map<String, String> paramHashMap);

    @GET("pay_bill.php?")
    Call<ResponseBody> payBillToMerchant(@Query("member_id") String user_id, @Query("merchant_id") String merchant_id, @Query("merchant_no") String merchant_number, @Query("amount") String due_amount_str, @Query("tip_amount") String tip_amt_str, @Query("ngcash") String ngcash_app_str, @Query("card_id") String card_id, @Query("card_number") String card_number, @Query("card_brand") String card_brand, @Query("customer_id") String customer_id, @Query("type") String type, @Query("timezone") String timezone, @Query("employee_id") String employee_id, @Query("employee_name") String employee_name);

/*/*pay_bill_emi.php?member_id=\(USER_DEFAULT.value(forKey: MemberID)!)&merchant_id=\(self.strMerchntId!)
&merchant_no=\(text_Search.text!)&amount=\(text_AmountDue.text!)&tip_amount=\(strAmountTip!)&ngcash=\
(strNgCash!)&card_id=\(dic_SelectCard["id"] ?? "")&card_number=\(dic_SelectCard["last4"] ?? "")
&card_brand=\(dic_SelectCard["brand"] ?? "")&customer_id=\(dic_SelectCard["customer"] ?? "")
&type=Paybill&timezone=\(Calendar.current.timeZone.identifier)&employee_name=\(lbl_Employee.text ??
"")&employee_id=\(strEmployeeinviteId ?? "")&cart_id=\(strOrderCartId!)*/
    @GET("pay_bill_emi.php?")
    Call<ResponseBody> payBillEmiToMerchant(@Query("member_id") String user_id,
                                            @Query("merchant_id") String merchant_id,
                                            @Query("merchant_no") String merchant_number,
                                            @Query("amount") String due_amount_str,
                                            @Query("tip_amount") String tip_amt_str,
                                            @Query("ngcash") String ngcash_app_str,
                                            @Query("card_id") String card_id,
                                            @Query("card_number") String card_number,
                                            @Query("card_brand") String card_brand,
                                            @Query("customer_id") String customer_id,
                                            @Query("type") String type,
                                            @Query("timezone") String timezone,
                                            @Query("employee_id") String employee_id,
                                            @Query("employee_name") String employee_name ,
                                            @Query("cart_id") String cart_id);

    @GET("audience_lists.php?")
    Call<ResponseBody> getMerchantSalesAudience(@Query("merchant_id") String merchant_id);

    @GET("member_transfer_request.php?")
    Call<ResponseBody> transferorrequest(@Query("member_id") String user_id, @Query("transfer_request_user_id") String transfer_request_user_id, @Query("comment") String comment, @Query("amount") String due_amount_str, @Query("ngcash") String ngcash_app_str, @Query("card_id") String card_id, @Query("card_number") String card_number, @Query("card_brand") String card_brand, @Query("customer_id") String customer_id, @Query("type") String type, @Query("timezone") String timezone);

    @GET("merchant_commission_list_for_member.php?")
    Call<ResponseBody> getCommissionData(@Query("how_invite_you") String how_invite_you);

    @GET("member_withdraw.php?")
    Call<ResponseBody> withdrawCommision(@Query("member_id") String member_id, @Query("week_no") String week_no, @Query("week_year") String week_year, @Query("withdraw_total_amount") String withdraw_total_amount, @Query("ngcash_amount") String ngcash_amount, @Query("bank_amount") String bank_amount, @Query("account_id") String account_id);

    @GET("total_merchant_sales.php?")
    Call<ResponseBody> getSalesMerchantData(@Query("merchant_id") String merchant_id);

    @GET("change_member_password.php?")
    Call<ResponseBody> changeMemberPass(@Query("tid") String tid, @Query("tech_password") String tech_password, @Query("old_password") String old_password);

    @GET("change_merchant_password.php?")
    Call<ResponseBody> changeMerchnatPass(@Query("id") String tid, @Query("team_Password") String team_Password, @Query("old_password") String old_password);

    @GET("get_merchant_video.php?")
    Call<ResponseBody> getMerchantTutorialVideo();

    @GET("get_member_video.php?")
    Call<ResponseBody> getMemberTutorialVideo();

    @FormUrlEncoded
    @POST("mail_deactivate_account_memeber.php/")
    Call<ResponseBody> requestOtp(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("deactivate_account_memeber.php")
    Call<ResponseBody> deleteMyAccount(@FieldMap Map<String, String> paramHashMap);

}
