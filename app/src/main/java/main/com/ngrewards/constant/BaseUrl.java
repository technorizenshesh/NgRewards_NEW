package main.com.ngrewards.constant;

/**
 * Created by technorizen on 5/6/17.
 */

public class BaseUrl {
    public static String baseurl = "https://myngrewards.com/wp-content/plugins/webservice/";
    public static String image_baseurl = "https://myngrewards.com/uploads/images/";
    public static String video_baseurl = "https://myngrewards.com/uploads/videos/";
    public static String file_baseurl = "https://myngrewards.com/uploads/files/";
    public static String admin_email = "info@myngrewards.com";
    public static String helpurl = "https://myngrewards.com/help-center/";
    public static String stripe_publish = "pk_live_OP15yODmjzdV2KnPWSjh5Pgo";
    // public static String stripe_publish="pk_test_7nLR7Pn11K4vaC8Hspvuzocf";
    public static String STRIPE_OAUTH_URL = "https://connect.stripe.com/express/oauth/authorize?redirect_uri=https://myngrewards.com/wp-content/plugins/webservice/stripe_payment_form.php&client_id=ca_DtpgZQXOAtlpfbfH9LrMkVts9Lt8n2qY&scope=read_write&always_prompt=false&stripe_landing=register&state=";
    public static String STRIPE_OAUTH_URL_MEMBER = "https://connect.stripe.com/express/oauth/authorize?redirect_uri=https://myngrewards.com/wp-content/plugins/webservice/stripe_payment_form_member.php&client_id=ca_DtpgZQXOAtlpfbfH9LrMkVts9Lt8n2qY&scope=read_write&always_prompt=false&stripe_landing=register&state=";

    public static String getMerchantMenuSettingList() {
        return baseurl.concat("merchant_menu_setting_list.php");
    }

    public static String memberDelivery() {
        return baseurl.concat("member_delivery.php");
    }

    public static String memberMenuList() {
        return baseurl.concat("member_menu_list.php");
    }

    public static String addUpdateQuantity() {

        return baseurl.concat("add_update_quantity.php");
    }

    public static String memberMenuCardList() {
        return baseurl.concat("member_menu_card_list.php");
    }

    public static String deleteItem() {
        return baseurl.concat("delete_item.php");
    }

    public static String addOrderCart() {
        return baseurl.concat("add_order_cart.php");
    }

    public static String orderPayBill() {
        return baseurl.concat("order_pay_bill.php");
    }

    public static String order_cart_list() {
        return baseurl.concat("order_cart_list.php");
    }
}
