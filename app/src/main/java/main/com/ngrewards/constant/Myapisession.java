package main.com.ngrewards.constant;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by technorizen on 7/8/18.
 */

public class Myapisession {
    public static final String KEY_COUNTRY = "country_data";
    public static final String KEY_MERCHANTCATE = "merchant_category";
    public static final String KEY_MERCHANTCOUNTRY = "merchant_country";
    public static final String KEY_PRODUCTCATE = "product_category";
    public static final String KEY_OFFERCATE = "offer_category";
    public static final String KEY_BUSINESSNUMBER = "business_number";
    public static final String KEY_MEMBERUSERNAME = "member_username";
    public static final String KEY_ADDRESSDATA = "member_address";
    public static final String KEY_CARTITEM = "member_cartitem";
    private static final String PREF_NAME = "MyApiPref";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public Myapisession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setMerchantcat(String merchantdata) {
        editor.putString(KEY_MERCHANTCATE, merchantdata);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public void setMerchantcountry(String merchantcountry) {
        editor.putString(KEY_MERCHANTCOUNTRY, merchantcountry);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public void setCountrydata(String countrydata) {
        editor.putString(KEY_COUNTRY, countrydata);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public String getKeyBusinessnumber() {
        return pref.getString(KEY_BUSINESSNUMBER, null);
    }

    public void setKeyBusinessnumber(String businessnumber) {
        editor.putString(KEY_BUSINESSNUMBER, businessnumber);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public String getKeyMemberusername() {
        return pref.getString(KEY_MEMBERUSERNAME, null);
    }

    public void setKeyMemberusername(String memberusername) {
        editor.putString(KEY_MEMBERUSERNAME, memberusername);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public String getKeyOffercate() {
        return pref.getString(KEY_OFFERCATE, null);
    }

    public void setKeyOffercate(String offerdata) {
        editor.putString(KEY_OFFERCATE, offerdata);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public String getKeyCountry() {
        return pref.getString(KEY_COUNTRY, null);
    }

    public String getKeyMerchantcate() {
        return pref.getString(KEY_MERCHANTCATE, null);
    }

    public String getKeyMerchantcountry() {
        return pref.getString(KEY_MERCHANTCOUNTRY, null);
    }

    public String getProductdata() {
        return pref.getString(KEY_PRODUCTCATE, null);
    }

    public void setProductdata(String productdata) {
        editor.putString(KEY_PRODUCTCATE, productdata);
        editor.commit();

    }

    public String getKeyAddressdata() {
        return pref.getString(KEY_ADDRESSDATA, null);
    }

    public void setKeyAddressdata(String addressdata) {
        editor.putString(KEY_ADDRESSDATA, addressdata);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public String getKeyCartitem() {
        return pref.getString(KEY_CARTITEM, null);
    }

    public void setKeyCartitem(String cartitem) {
        editor.putString(KEY_CARTITEM, cartitem);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

}
