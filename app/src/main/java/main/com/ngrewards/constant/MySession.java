package main.com.ngrewards.constant;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by shyam on 11/04/2016.
 */
public class MySession {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;


    private static final String PREF_NAME = "MyPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_PUSH_NOTIFICATION = "push_noti";
    public static final String KEY_ID = "id";
    private static final String IS_ONLINE = "IsOnline";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_SENDVIAMAIL = "Sendofferviamail";
    public static final String KEY_USERREGISTERED = "user_registered";
    public static final String KEY_EMAIL = "user_email";
    public static final String KEY_BIRTHDATE = "DOB";
    public static final String KEY_MOBILE = "Mobileno";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_EMAILVERFY = "email_verify";
    public static final String KEY_USERSTATUS = "user_status";
    public static final String KEY_ALLDATA = "alldata";
    public static final String KEY_PRO_WEL = "welpop";
    public static final String KEY_COUNTRY = "country_data";
    public static final String KEY_MERCHANTCATE = "merchant_category";
    public static final String KEY_PRODUCTCATE = "product_category";
    public static final String KEY_TOUCH_ENABLED = "touchid";
    public static final String APP_UPDATE = "appupdates";
    public static final String admin_created_password = "admin_created_password";
    public static final String sell_items_reomve_access = "sell_items_reomve_access";
    public static final String PassSet = "pass_set";

    public MySession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setsell_items_reomve_access(String appupdate) {
        editor.putString(sell_items_reomve_access, appupdate);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public String getsell_items_reomve_access() {
        return pref.getString(sell_items_reomve_access, "");
    }
    public void setPassSet(String appupdate) {
        editor.putString(PassSet, appupdate);
        editor.commit();

    }

    public String getPassSet() {
        return pref.getString(PassSet, "");
    }

    public void setadmin_created_password(String appupdate) {
        editor.putString(admin_created_password, appupdate);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public String getadmin_created_password() {
        return pref.getString(admin_created_password, "");
    }


    public void setuserId(String uid) {
        editor.putString(KEY_ID, uid);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public String getId() {
        return pref.getString(KEY_ID, null);
    }

    public void setAppUpdate(String appupdate) {
        editor.putString(APP_UPDATE, appupdate);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public String getAppUpdate() {
        return pref.getString(APP_UPDATE, "cancel");
    }

    public void setProductdata(String productdata) {
        editor.putString(KEY_PRODUCTCATE, productdata);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public void setMerchantcat(String merchantdata) {
        editor.putString(KEY_MERCHANTCATE, merchantdata);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public void setCountrydata(String countrydata) {
        editor.putString(KEY_COUNTRY, countrydata);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public String getKeyCountry() {
        return pref.getString(KEY_COUNTRY, null);
    }

    public String getKeyMerchantcate() {
        return pref.getString(KEY_MERCHANTCATE, null);
    }

    public String getProductdata() {
        return pref.getString(KEY_PRODUCTCATE, null);
    }

    public void setuserfirstName(String firstname) {
        editor.putString(KEY_FIRSTNAME, firstname);
        //editor.putString(KEY_TYPE, type);
        editor.commit();

    }

    public void pushnotificationofoff(boolean val) {
        editor.putBoolean(KEY_PUSH_NOTIFICATION, val);
        editor.commit();

    }

    public boolean isPushOn() {
        return pref.getBoolean(KEY_PUSH_NOTIFICATION, true);
    }

    public void touchid(boolean val) {
        editor.putBoolean(KEY_TOUCH_ENABLED, val);
        editor.commit();

    }

    public boolean isTouchOn() {
        return pref.getBoolean(KEY_TOUCH_ENABLED, false);
    }

    public void setlogindata(String alldata) {
        editor.putString(KEY_ALLDATA, alldata);
        editor.commit();

    }

    public String getKeyAlldata() {
        return pref.getString(KEY_ALLDATA, null);
    }

    private String language = "", userid, name_str, birthdate_str, location_str, occupation_str, gender_str, imagepath_str, maritalstatus_str, children_str, smoking_habits_str, drinking_habits_str, education_str, height_str, country_str;


    public void createLoginSession(String id, String firstname, String lastname, String sendviamail, String user_registered, String user_email, String DOB, String Mobileno, String email_verify, String user_status) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_FIRSTNAME, firstname);
        editor.putString(KEY_LASTNAME, lastname);
        editor.putString(KEY_SENDVIAMAIL, sendviamail);
        editor.putString(KEY_USERREGISTERED, user_registered);
        editor.putString(KEY_EMAIL, user_email);
        editor.putString(KEY_BIRTHDATE, DOB);
        editor.putString(KEY_MOBILE, Mobileno);
        editor.putString(KEY_EMAILVERFY, email_verify);
        editor.putString(KEY_USERSTATUS, user_status);


        editor.commit();
    }

    /* public void checkLogin() {
         if (!this.IsLoggedIn()) {
             Intent intent = new Intent(_context, LoginActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             _context.startActivity(intent);
         }
     }
 */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        user.put(KEY_ID, pref.getString(KEY_ID, ""));
        user.put(KEY_FIRSTNAME, pref.getString(KEY_FIRSTNAME, ""));
        user.put(KEY_LASTNAME, pref.getString(KEY_LASTNAME, ""));
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, ""));
        user.put(KEY_SENDVIAMAIL, pref.getString(KEY_SENDVIAMAIL, ""));

        user.put(KEY_USERREGISTERED, pref.getString(KEY_USERREGISTERED, ""));
        user.put(KEY_BIRTHDATE, pref.getString(KEY_BIRTHDATE, ""));
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, ""));
        user.put(KEY_EMAILVERFY, pref.getString(KEY_EMAILVERFY, ""));
        user.put(KEY_USERSTATUS, pref.getString(KEY_USERSTATUS, ""));


        return user;
    }

    public void signinusers(boolean val) {
        editor.putBoolean(IS_LOGIN, val);
        editor.commit();

    }

    public void prouser(boolean val) {
        editor.putBoolean(KEY_PRO_WEL, val);
        editor.commit();

    }

    public boolean Isprouser() {
        return pref.getBoolean(KEY_PRO_WEL, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public void onlineUser(boolean val) {
        editor.putBoolean(IS_ONLINE, val);
        editor.commit();

    }

    public boolean IsLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getFirstName() {
        return pref.getString(KEY_FIRSTNAME, null);
    }

    public String getLastName() {
        return pref.getString(KEY_LASTNAME, null);
    }

    public boolean IsOnline() {
        return pref.getBoolean(IS_ONLINE, false);
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }


}